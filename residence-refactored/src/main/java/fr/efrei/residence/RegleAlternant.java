package fr.efrei.residence;

public class RegleAlternant implements RegleProfil {

    private static final double TAUX_APRES_REMISE = 0.90;
    private static final double COEF_SOLVABILITE_ALTERNANT = 2;
    private static final int JOURS_PREAVIS_ALTERNANT = 15;

    public double appliquerRemise(double loyer, int echelonBourse) {
        return loyer * TAUX_APRES_REMISE;
    }

    public boolean estSolvable(double ressources, double loyerChargesComprises) {
        return ressources >= loyerChargesComprises * COEF_SOLVABILITE_ALTERNANT;
    }

    public int joursPreavis() {
        return JOURS_PREAVIS_ALTERNANT;
    }
}
