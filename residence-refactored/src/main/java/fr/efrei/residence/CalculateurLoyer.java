package fr.efrei.residence;

import java.util.Set;

public class CalculateurLoyer {

    private static final double MAJORATION_MEUBLE = 1.15;
    private static final double MINORATION_REZ_DE_CHAUSSEE = 0.95;
    private static final double PRIX_PARKING = 45;
    private static final double PRIX_LAVERIE = 12;
    private static final double PRIX_ASSURANCE = 9;

    public double calculerLoyer(Logement l, Profil profil, int echelon, Set<Option> options) {
        double loyer = l.loyerDeBase();

        if (l.isMeuble()) loyer = loyer * MAJORATION_MEUBLE;
        if (l.getEtage() == 0) loyer = loyer * MINORATION_REZ_DE_CHAUSSEE;

        if (options.contains(Option.MENAGE)) loyer = loyer * l.tauxMenage();
        if (options.contains(Option.PARKING)) loyer = loyer + PRIX_PARKING;
        if (options.contains(Option.LAVERIE)) loyer = loyer + PRIX_LAVERIE;
        if (options.contains(Option.FIBRE)) loyer = loyer + l.prixFibre();
        if (options.contains(Option.ASSURANCE)) loyer = loyer + PRIX_ASSURANCE;

        loyer = profil.getRegle().appliquerRemise(loyer, echelon);

        return Utils.arrondir(loyer);
    }
}
