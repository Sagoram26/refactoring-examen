package fr.efrei.residence;

import java.util.ArrayList;
import java.util.List;

public class Colocation extends Logement {

    private static final int PLACES_MIN = 2;
    private static final double PRIX_M2 = 11;
    private static final double CHARGES_COLOCATION = 25;
    private static final double MAJORATION_MENAGE_COLOCATION = 1.04;
    private static final double PRIX_FIBRE_COLOCATION = 0;

    private int places;
    private List<Etudiant> colocataires = new ArrayList<Etudiant>();

    public Colocation(String numero, double surface, int etage, boolean meuble, int places) {
        super(numero, TypeLogement.COLOCATION, surface, etage, meuble);
        this.places = Math.max(places, PLACES_MIN);
    }

    public double loyerDeBase() {
        return surface * PRIX_M2 / places;
    }

    @Override
    public double charges() {
        return CHARGES_COLOCATION;
    }

    @Override
    public double tauxMenage() {
        return MAJORATION_MENAGE_COLOCATION;
    }

    @Override
    public double prixFibre() {
        return PRIX_FIBRE_COLOCATION;
    }

    @Override
    public int getNombreOccupants() {
        return colocataires.size();
    }

    @Override
    public void loger(Etudiant e) {
        throw new UnsupportedOperationException("Utiliser ajouterColocataire() pour une colocation");
    }

    @Override
    public void liberer(Etudiant e) {
        throw new UnsupportedOperationException("Utiliser retirerColocataire() pour une colocation");
    }

    @Override
    public boolean estLibre() {
        return colocataires.size() < places;
    }

    @Override
    public Etudiant getOccupant() {
        if (colocataires.isEmpty()) return null;
        return colocataires.get(0);
    }

    public void ajouterColocataire(Etudiant e) {
        if (colocataires.size() >= places) {
            throw new IllegalStateException("Colocation " + numero + " complète");
        }
        colocataires.add(e);
    }

    public void retirerColocataire(Etudiant e) {
        colocataires.remove(e);
    }

    public List<Etudiant> getColocataires() {
        return colocataires;
    }

    public int getNombrePlaces() {
        return places;
    }

    public double getSurfaceBalcon() {
        throw new UnsupportedOperationException("Les colocations n'ont pas de balcon");
    }

    public void nettoyerPartiesCommunes() {
        System.out.println("[ENTRETIEN] Nettoyage des parties communes de la colocation " + numero);
    }
}
