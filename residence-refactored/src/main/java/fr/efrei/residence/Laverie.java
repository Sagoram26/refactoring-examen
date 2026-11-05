package fr.efrei.residence;

public record Laverie(Loyer loyer) implements Loyer {

    private static final double PRIX = 12;

    public double montant() {
        return loyer.montant() + PRIX;
    }
}
