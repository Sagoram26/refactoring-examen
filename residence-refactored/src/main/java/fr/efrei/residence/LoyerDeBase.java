package fr.efrei.residence;

public record LoyerDeBase(Logement logement) implements Loyer {

    public double montant() {
        return logement.loyerDeBase();
    }
}
