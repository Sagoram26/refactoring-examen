package fr.efrei.residence;

public interface RegleProfil {

    double COEF_SOLVABILITE = 3;
    double FRAIS_DOSSIER = 100;
    int JOURS_PREAVIS = 30;
    double TAUX_PENALITE = 0.05;
    double PENALITE_MIN = 10;

    default String refusInscription(int echelonBourse, String telephone) {
        return null;
    }

    default double appliquerRemise(double loyer, int echelonBourse) {
        return loyer;
    }

    default boolean estSolvable(double ressources, double loyerChargesComprises) {
        return ressources >= loyerChargesComprises * COEF_SOLVABILITE;
    }

    default double caution(double loyerHorsCharges) {
        return loyerHorsCharges;
    }

    default double fraisDossier() {
        return FRAIS_DOSSIER;
    }

    default int joursPreavis() {
        return JOURS_PREAVIS;
    }

    default double penaliteRetard(double loyerChargesComprises) {
        double penalite = loyerChargesComprises * TAUX_PENALITE;
        if (penalite < PENALITE_MIN) penalite = PENALITE_MIN;
        return Utils.arrondir(penalite);
    }

    default boolean recoitEmailEnAnglais() {
        return false;
    }
}
