package fr.efrei.residence;

import java.util.Set;

public class CalculateurLoyer {

    public double calculerLoyer(Logement l, Profil profil, int echelon, Set<Option> options) {
        Loyer loyer = new LoyerDeBase(l);
        if (l.isMeuble()) loyer = new Meuble(loyer);
        if (l.getEtage() == 0) loyer = new RezDeChaussee(loyer);
        for (Option option : Option.values()) {
            if (options.contains(option)) loyer = option.decorer(loyer, l);
        }
        loyer = new Remise(loyer, profil.getRegle(), echelon);
        return Utils.arrondir(loyer.montant());
    }
}
