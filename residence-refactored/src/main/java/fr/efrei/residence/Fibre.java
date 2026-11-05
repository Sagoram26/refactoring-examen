package fr.efrei.residence;

public record Fibre(Loyer loyer, Logement logement) implements Loyer {

    public double montant() {
        return loyer.montant() + logement.prixFibre();
    }
}
