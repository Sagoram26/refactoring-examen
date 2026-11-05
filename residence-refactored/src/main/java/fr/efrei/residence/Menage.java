package fr.efrei.residence;

public record Menage(Loyer loyer, Logement logement) implements Loyer {

    public double montant() {
        return loyer.montant() * logement.tauxMenage();
    }
}
