package fr.efrei.residence;

public record Assurance(Loyer loyer) implements Loyer {

    private static final double PRIX = 9;

    public double montant() {
        return loyer.montant() + PRIX;
    }
}
