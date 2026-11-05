package fr.efrei.residence;

import java.util.List;

public class T1 extends Logement {

    private static final double PRIX_M2 = 13;
    private static final double FORFAIT = 50;

    public T1(String numero, double surface, int etage, boolean meuble) {
        super(numero, TypeLogement.T1, surface, etage, meuble);
    }

    public double loyerDeBase() {
        return surface * PRIX_M2 + FORFAIT;
    }

    public void ajouterColocataire(Etudiant e) {
        throw new UnsupportedOperationException("Pas de colocation dans un T1");
    }

    public void retirerColocataire(Etudiant e) {
        throw new UnsupportedOperationException("Pas de colocation dans un T1");
    }

    public List<Etudiant> getColocataires() {
        return null;
    }

    public int getNombrePlaces() {
        return 1;
    }

    public double getSurfaceBalcon() {
        return 0;
    }

    public void nettoyerPartiesCommunes() {
    }
}
