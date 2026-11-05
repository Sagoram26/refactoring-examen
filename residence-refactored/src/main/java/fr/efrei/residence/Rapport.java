package fr.efrei.residence;

public class Rapport {

    private final Residence residence;
    private final BailRepository baux;
    private final SmsService sms;
    private final CourrierService courrier;

    public Rapport(Residence residence, BailRepository baux, SmsService sms, CourrierService courrier) {
        this.residence = residence;
        this.baux = baux;
        this.sms = sms;
        this.courrier = courrier;
    }

    public void afficherRapport() {
        System.out.println();
        System.out.println(genererRapport());
    }

    public String genererRapport() {
        String r = "";
        r += "=============== RAPPORT " + Residence.NOM + " ===============" + "\n";
        int total = 0;
        int occupes = 0;
        for (TypeLogement type : TypeLogement.values()) {
            int places = 0;
            int occupees = 0;
            for (Logement l : residence.getLogements()) {
                if (l.getType() != type) continue;
                places += l.getNombrePlaces();
                occupees += l.getNombreOccupants();
            }
            if (places == 0) continue;
            r += String.format("%-13s: %d/%d%s", type.getLibelle(), occupees, places, type.getUnite()) + "\n";
            total += places;
            occupes += occupees;
        }
        double surfaceBalcons = 0;
        for (Logement l : residence.getLogements()) {
            try {
                surfaceBalcons += l.getSurfaceBalcon();
            } catch (UnsupportedOperationException ex) {
            }
        }
        r += "Occupation   : " + Math.round(occupes * 100.0 / total) + " %" + "\n";
        r += "Balcons      : " + surfaceBalcons + " m²" + "\n";

        double loyersMensuels = 0;
        double impayes = 0;
        double cautions = 0;
        for (Bail b : baux.tousLesBaux()) {
            if (b.getStatut() == StatutBail.ACTIF || b.getStatut() == StatutBail.PREAVIS) {
                loyersMensuels += b.getLoyerChargesComprises();
                cautions += b.getCaution();
            }
            if (b.soldeDu > 0) impayes += b.soldeDu;
        }
        r += "Loyers CC    : " + Utils.euros(Utils.arrondir(loyersMensuels)) + " / mois" + "\n";
        r += "Cautions     : " + Utils.euros(Utils.arrondir(cautions)) + "\n";
        r += "Impayés      : " + Utils.euros(Utils.arrondir(impayes)) + "\n";
        r += "SMS envoyés  : " + sms.getCompteurSms() + "\n";
        r += "Timbres      : " + Utils.euros(Utils.arrondir(courrier.getTotalAffranchissement())) + "\n";
        r += "============================================================";
        return r;
    }
}
