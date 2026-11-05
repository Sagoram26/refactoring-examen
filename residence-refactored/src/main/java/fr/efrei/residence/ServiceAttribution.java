package fr.efrei.residence;

import java.time.LocalDate;
import java.util.Set;

public class ServiceAttribution {

    private final Residence residence;
    private final BailRepository baux;
    private final CalculateurLoyer calculateur;
    private final Notificateur notificateur;
    private int compteurBail = 0;

    public ServiceAttribution(Residence residence, BailRepository baux, CalculateurLoyer calculateur,
                              Notificateur notificateur) {
        this.residence = residence;
        this.baux = baux;
        this.calculateur = calculateur;
        this.notificateur = notificateur;
    }

    public Bail attribuerLogement(Etudiant e, TypeLogement typeSouhaite, Set<Option> options, LocalDate dateSignature) {
        System.out.println("--- Attribution d'un " + typeSouhaite + " à " + e.getNomComplet());
        Logement choisi = null;
        double loyer = 0;
        double charges = 0;
        for (Logement l : residence.getLogements()) {
            if (l.getType() != typeSouhaite) continue;
            if (!l.estLibre()) continue;
            if (e.isPmr() && !l.estAccessiblePMR()) continue;

            double loyerCandidat = calculateur.calculerLoyer(l, e.getProfil(), e.getEchelonBourse(), options);
            double chargesCandidat = l.charges();
            double total = loyerCandidat + chargesCandidat;

            if (!e.getProfil().getRegle().estSolvable(e.getRessourcesMensuelles(), total)) {
                System.out.println("  " + l.getNumero() + " : ressources insuffisantes (" + Utils.euros(total) + "/mois)");
                continue;
            }
            choisi = l;
            loyer = loyerCandidat;
            charges = chargesCandidat;
            break;
        }
        if (choisi == null) {
            System.out.println("  Aucun logement disponible pour " + e.getNomComplet());
            return null;
        }

        if (choisi instanceof Colocation) {
            choisi.ajouterColocataire(e);
        } else {
            choisi.loger(e);
        }

        compteurBail++;
        String id = "BAIL-" + String.format("%03d", compteurBail);
        Bail b = new Bail(id, e, choisi, dateSignature);
        b.options = options;
        b.setLoyerHorsCharges(loyer);
        b.setCharges(charges);

        RegleProfil regle = e.getProfil().getRegle();
        b.setCaution(Utils.arrondir(regle.caution(loyer)));
        b.setFraisDossier(regle.fraisDossier());

        baux.sauvegarderBail(b);
        System.out.println("  Bail " + id + " signé : " + choisi.getNumero() + ", " + Utils.euros(b.getLoyerChargesComprises())
                + " CC/mois, caution " + Utils.euros(b.getCaution()) + ", frais de dossier " + Utils.euros(b.getFraisDossier()));
        notificateur.notifier(e, "Votre bail " + id,
                "Bienvenue à la " + Residence.NOM + " ! Logement " + choisi.getNumero() + ", loyer "
                        + Utils.euros(b.getLoyerChargesComprises()) + " charges comprises.");
        return b;
    }
}
