package fr.efrei.residence;

import java.util.Set;

public class CalculateurLoyer {

    private static final double PRIX_M2_STUDIO = 14;
    private static final double LOYER_MIN_STUDIO = 350;
    private static final double PRIX_M2_T1 = 13;
    private static final double FORFAIT_T1 = 50;
    private static final double PRIX_M2_T2 = 12;
    private static final double FORFAIT_T2 = 100;
    private static final double PRIX_M2_COLOCATION = 11;
    private static final double MAJORATION_MEUBLE = 1.15;
    private static final double MINORATION_REZ_DE_CHAUSSEE = 0.95;
    private static final double MAJORATION_MENAGE = 1.08;
    private static final double MAJORATION_MENAGE_COLOCATION = 1.04;
    private static final double PRIX_PARKING = 45;
    private static final double PRIX_LAVERIE = 12;
    private static final double PRIX_FIBRE = 20;
    private static final double PRIX_ASSURANCE = 9;
    private static final double REMISE_PAR_ECHELON = 0.05;
    private static final double REMISE_BOURSIER_MAX = 0.30;
    private static final double TAUX_ALTERNANT = 0.90;
    private static final double CHARGES = 35;
    private static final double CHARGES_COLOCATION = 25;

    public double calculerLoyer(Logement l, Profil profil, int echelon, Set<Option> options) {
        double loyer;
        if (l.getType() == TypeLogement.STUDIO) {
            loyer = l.getSurface() * PRIX_M2_STUDIO;
            if (loyer < LOYER_MIN_STUDIO) loyer = LOYER_MIN_STUDIO;
        } else if (l.getType() == TypeLogement.T1) {
            loyer = l.getSurface() * PRIX_M2_T1 + FORFAIT_T1;
        } else if (l.getType() == TypeLogement.T2) {
            loyer = l.getSurface() * PRIX_M2_T2 + FORFAIT_T2;
        } else {
            loyer = l.getSurface() * PRIX_M2_COLOCATION / l.getNombrePlaces();
        }

        if (l.isMeuble()) loyer = loyer * MAJORATION_MEUBLE;
        if (l.getEtage() == 0) loyer = loyer * MINORATION_REZ_DE_CHAUSSEE;

        if (options.contains(Option.MENAGE)) {
            if (l.getType() == TypeLogement.COLOCATION) {
                loyer = loyer * MAJORATION_MENAGE_COLOCATION;
            } else {
                loyer = loyer * MAJORATION_MENAGE;
            }
        }
        if (options.contains(Option.PARKING)) loyer = loyer + PRIX_PARKING;
        if (options.contains(Option.LAVERIE)) loyer = loyer + PRIX_LAVERIE;
        if (options.contains(Option.FIBRE) && l.getType() != TypeLogement.COLOCATION) loyer = loyer + PRIX_FIBRE;
        if (options.contains(Option.ASSURANCE)) loyer = loyer + PRIX_ASSURANCE;

        if (profil == Profil.BOURSIER) {
            double taux = echelon * REMISE_PAR_ECHELON;
            if (taux > REMISE_BOURSIER_MAX) taux = REMISE_BOURSIER_MAX;
            loyer = loyer * (1 - taux);
        } else if (profil == Profil.ALTERNANT) {
            loyer = loyer * TAUX_ALTERNANT;
        }

        return Utils.arrondir(loyer);
    }

    public double calculerCharges(Logement l) {
        if (l.getType() == TypeLogement.COLOCATION) return CHARGES_COLOCATION;
        return CHARGES;
    }
}
