package fr.efrei.residence;

import fr.efrei.residence.contrat.DemandeAttribution;
import fr.efrei.residence.contrat.DemandeDevis;
import fr.efrei.residence.contrat.DemandeInscription;
import fr.efrei.residence.contrat.DemandeLogement;
import fr.efrei.residence.contrat.EtatBail;
import fr.efrei.residence.contrat.EtatLogement;
import fr.efrei.residence.contrat.Notification;
import fr.efrei.residence.contrat.SystemeResidence;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Set;

public class NouveauSystemeAdapter implements SystemeResidence {

    private Database db;
    private EmailService email;
    private SmsService sms;
    private CourrierService courrier;
    private Residence residence;
    private CalculateurLoyer calculateur;
    private ServiceInscription inscription;
    private ServiceAttribution attribution;
    private ServiceBail baux;
    private ServiceRelance relances;
    private Rapport rapport;
    private Quittance quittance;

    @Override
    public void reinitialiser() {
        db = new Database();
        email = new EmailService();
        sms = new SmsService();
        courrier = new CourrierService();
        Notificateur notificateur = new Notificateur(new CanalFactory(email, sms, courrier), email);
        residence = new Residence();
        calculateur = new CalculateurLoyer();
        inscription = new ServiceInscription(db);
        attribution = new ServiceAttribution(residence, db, calculateur, notificateur);
        baux = new ServiceBail(db, notificateur);
        relances = new ServiceRelance(db, notificateur);
        rapport = new Rapport(residence, db, sms, courrier);
        quittance = new Quittance();
    }

    @Override
    public Optional<String> inscrire(DemandeInscription d) {
        Etudiant e = inscription.inscrireEtudiant(d.nom(), d.prenom(), d.email(), d.telephone(), d.adresse(),
                traduire(d.profil(), Profil.class), d.echelonBourse(), d.ressourcesMensuelles(), d.pmr(),
                traduire(d.canalPrefere(), Canal.class));
        return Optional.ofNullable(e).map(Etudiant::getId);
    }

    @Override
    public boolean ajouterLogement(DemandeLogement d) {
        TypeLogement type = traduire(d.type(), TypeLogement.class);
        int param = type == TypeLogement.COLOCATION ? d.places() : d.surfaceBalcon();
        return residence.ajouterLogement(type, d.numero(), d.surface(), d.etage(), d.meuble(), param) != null;
    }

    @Override
    public Optional<EtatLogement> logement(String numero) {
        Logement l = residence.trouver(numero);
        if (l == null) {
            return Optional.empty();
        }
        int occupants;
        if (l instanceof Colocation) {
            occupants = l.getColocataires().size();
        } else {
            occupants = l.estLibre() ? 0 : 1;
        }
        return Optional.of(new EtatLogement(l.getNumero(),
                traduire(l.getType(), fr.efrei.residence.contrat.TypeLogement.class), l.getNombrePlaces(),
                occupants, l.estAccessiblePMR()));
    }

    @Override
    public OptionalDouble devisLoyer(DemandeDevis d) {
        Logement l = residence.trouver(d.numeroLogement());
        if (l == null) {
            return OptionalDouble.empty();
        }
        return OptionalDouble.of(calculateur.calculerLoyer(l, traduire(d.profil(), Profil.class),
                d.echelonBourse(), traduire(d.options())));
    }

    @Override
    public Optional<String> attribuer(DemandeAttribution d) {
        Etudiant e = db.trouverEtudiantParId(d.refEtudiant());
        if (e == null) {
            return Optional.empty();
        }
        Bail b = attribution.attribuerLogement(e, traduire(d.type(), TypeLogement.class), traduire(d.options()),
                d.dateSignature());
        return Optional.ofNullable(b).map(Bail::getId);
    }

    @Override
    public Optional<EtatBail> consulter(String idBail) {
        Bail b = db.trouverBail(idBail);
        if (b == null) {
            return Optional.empty();
        }
        return Optional.of(new EtatBail(b.getId(), b.getLogement().getNumero(),
                traduire(b.getStatut(), fr.efrei.residence.contrat.StatutBail.class),
                b.getLoyerHorsCharges(), b.getCharges(), b.getLoyerChargesComprises(), b.getCaution(),
                b.getFraisDossier(), b.getDateFin(), b.soldeDu, b.cautionRestituee));
    }

    @Override
    public boolean entrer(String idBail, LocalDate date) {
        return baux.entrerDansLesLieux(idBail, date);
    }

    @Override
    public boolean payer(String idBail, YearMonth mois, LocalDate date, double montant) {
        return baux.payerLoyer(idBail, mois.toString(), date, montant);
    }

    @Override
    public boolean donnerPreavis(String idBail, LocalDate date) {
        return baux.donnerPreavis(idBail, date);
    }

    @Override
    public boolean sortir(String idBail, LocalDate date, double retenuesDegats) {
        return baux.sortir(idBail, date, retenuesDegats);
    }

    @Override
    public boolean annuler(String idBail) {
        return baux.annulerBail(idBail);
    }

    @Override
    public Optional<String> quittance(String idBail, YearMonth mois) {
        return Optional.ofNullable(db.trouverBail(idBail)).map(b -> quittance.generer(b, mois.toString()));
    }

    @Override
    public void relancerImpayes() {
        relances.relancerImpayes();
    }

    @Override
    public String rapport() {
        return rapport.genererRapport();
    }

    @Override
    public List<Notification> notifications() {
        List<Notification> notifications = new ArrayList<>();
        for (String[] e : email.getJournal()) {
            notifications.add(new Notification(fr.efrei.residence.contrat.Canal.EMAIL, e[0], e[1]));
        }
        for (String[] s : sms.getJournal()) {
            notifications.add(new Notification(fr.efrei.residence.contrat.Canal.SMS, s[0], s[1]));
        }
        for (String[] c : courrier.getJournal()) {
            notifications.add(new Notification(fr.efrei.residence.contrat.Canal.COURRIER, c[0], c[1]));
        }
        return notifications;
    }

    private static <E extends Enum<E>> E traduire(Enum<?> valeur, Class<E> type) {
        return Enum.valueOf(type, valeur.name());
    }

    private static Set<Option> traduire(Set<fr.efrei.residence.contrat.Option> options) {
        Set<Option> traduites = EnumSet.noneOf(Option.class);
        for (fr.efrei.residence.contrat.Option o : options) {
            traduites.add(traduire(o, Option.class));
        }
        return traduites;
    }
}
