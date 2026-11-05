package fr.efrei.residence;

public record Remise(Loyer loyer, RegleProfil regle, int echelonBourse) implements Loyer {

    public double montant() {
        return regle.appliquerRemise(loyer.montant(), echelonBourse);
    }
}
