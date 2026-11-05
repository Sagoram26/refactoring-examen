package fr.efrei.residence;

public record Parking(Loyer loyer) implements Loyer {

    private static final double PRIX = 45;

    public double montant() {
        return loyer.montant() + PRIX;
    }
}
