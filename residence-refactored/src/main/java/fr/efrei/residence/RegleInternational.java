package fr.efrei.residence;

public class RegleInternational implements RegleProfil {

    private static final int MOIS_CAUTION = 2;
    private static final double FRAIS_DOSSIER_INTERNATIONAL = 150;

    public String refusInscription(int echelonBourse, String telephone) {
        if (telephone == null || !telephone.startsWith("+")) return "numéro international (+XX) obligatoire";
        return null;
    }

    public double caution(double loyerHorsCharges) {
        return loyerHorsCharges * MOIS_CAUTION;
    }

    public double fraisDossier() {
        return FRAIS_DOSSIER_INTERNATIONAL;
    }

    public boolean recoitEmailEnAnglais() {
        return true;
    }
}
