package fr.efrei.residence;

import java.util.List;

public interface ILogement {

    String getNumero();

    String getType();

    double getSurface();

    int getEtage();

    boolean isMeuble();

    boolean estLibre();

    boolean estAccessiblePMR();

    void loger(Etudiant e);

    void liberer(Etudiant e);

    Etudiant getOccupant();

    void ajouterColocataire(Etudiant e);

    void retirerColocataire(Etudiant e);

    List<Etudiant> getColocataires();

    int getNombrePlaces();

    double getSurfaceBalcon();

    void nettoyerPartiesCommunes();
}
