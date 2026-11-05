package fr.efrei.residence;

public class RegleBoursier implements RegleProfil {

    private static final int ECHELON_MAX = 7;
    private static final double REMISE_PAR_ECHELON = 0.05;
    private static final double REMISE_MAX = 0.30;
    private static final int JOURS_PREAVIS_BOURSIER = 15;

    public String refusInscription(int echelonBourse, String telephone) {
        if (echelonBourse < 0 || echelonBourse > ECHELON_MAX) return "échelon de bourse invalide";
        return null;
    }

    public double appliquerRemise(double loyer, int echelonBourse) {
        double taux = echelonBourse * REMISE_PAR_ECHELON;
        if (taux > REMISE_MAX) taux = REMISE_MAX;
        return loyer * (1 - taux);
    }

    public boolean estSolvable(double ressources, double loyerChargesComprises) {
        return true;
    }

    public double caution(double loyerHorsCharges) {
        return 0;
    }

    public double fraisDossier() {
        return 0;
    }

    public int joursPreavis() {
        return JOURS_PREAVIS_BOURSIER;
    }

    public double penaliteRetard(double loyerChargesComprises) {
        return 0;
    }
}
