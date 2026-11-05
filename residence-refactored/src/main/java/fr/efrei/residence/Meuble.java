package fr.efrei.residence;

public record Meuble(Loyer loyer) implements Loyer {

    private static final double MAJORATION = 1.15;

    public double montant() {
        return loyer.montant() * MAJORATION;
    }
}
