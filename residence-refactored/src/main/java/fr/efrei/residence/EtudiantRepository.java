package fr.efrei.residence;

public interface EtudiantRepository {

    void sauvegarderEtudiant(Etudiant e);

    Etudiant trouverEtudiantParEmail(String email);

    Etudiant trouverEtudiantParId(String id);
}
