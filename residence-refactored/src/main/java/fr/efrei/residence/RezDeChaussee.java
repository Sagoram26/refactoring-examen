package fr.efrei.residence;

public record RezDeChaussee(Loyer loyer) implements Loyer {

    private static final double MINORATION = 0.95;

    public double montant() {
        return loyer.montant() * MINORATION;
    }
}
