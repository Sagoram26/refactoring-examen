package fr.efrei.residence;

import java.util.List;

public class Studio extends Logement {

    private static final double PRIX_M2 = 14;
    private static final double LOYER_MIN = 350;

    public Studio(String numero, double surface, int etage, boolean meuble) {
        super(numero, TypeLogement.STUDIO, surface, etage, meuble);
    }

    public double loyerDeBase() {
        double loyer = surface * PRIX_M2;
        if (loyer < LOYER_MIN) loyer = LOYER_MIN;
        return loyer;
    }

    public void ajouterColocataire(Etudiant e) {
        throw new UnsupportedOperationException("Pas de colocation dans un studio");
    }

    public void retirerColocataire(Etudiant e) {
        throw new UnsupportedOperationException("Pas de colocation dans un studio");
    }

    public List<Etudiant> getColocataires() {
        throw new UnsupportedOperationException("Pas de colocation dans un studio");
    }

    public int getNombrePlaces() {
        return 1;
    }

    public double getSurfaceBalcon() {
        throw new UnsupportedOperationException("Les studios n'ont pas de balcon");
    }

    public void nettoyerPartiesCommunes() {
        throw new UnsupportedOperationException("Pas de parties communes dans un studio");
    }
}
