package fr.efrei.residence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database implements EtudiantRepository, BailRepository {

    private final Map<String, Etudiant> etudiants = new HashMap<String, Etudiant>();
    private final Map<String, Bail> baux = new HashMap<String, Bail>();

    public Database() {
        System.out.println("[DB] Connexion à jdbc:postgresql://db.campusvert.local:5432/residence");
    }

    public void executer(String sql) {
        System.out.println("[SQL] " + sql);
    }

    public void sauvegarderEtudiant(Etudiant e) {
        executer("INSERT INTO etudiant (id, nom, email, profil) VALUES ('" + e.getId() + "', '" + e.getNom() + "', '" + e.getEmail() + "', '" + e.getProfil() + "')");
        etudiants.put(e.getEmail(), e);
    }

    public Etudiant trouverEtudiantParEmail(String email) {
        executer("SELECT * FROM etudiant WHERE email = '" + email + "'");
        return etudiants.get(email);
    }

    public Etudiant trouverEtudiantParId(String id) {
        executer("SELECT * FROM etudiant WHERE id = '" + id + "'");
        for (Etudiant e : etudiants.values()) {
            if (e.getId().equals(id)) return e;
        }
        return null;
    }

    public void sauvegarderBail(Bail b) {
        executer("INSERT INTO bail (id, etudiant, logement, statut, loyer) VALUES ('" + b.getId() + "', '" + b.getEtudiant().getId() + "', '" + b.getLogement().getNumero() + "', '" + b.getStatut() + "', " + b.getLoyerChargesComprises() + ")");
        baux.put(b.getId(), b);
    }

    public void mettreAJourBail(Bail b) {
        executer("UPDATE bail SET statut = '" + b.getStatut() + "', solde = " + b.soldeDu + " WHERE id = '" + b.getId() + "'");
    }

    public Bail trouverBail(String id) {
        executer("SELECT * FROM bail WHERE id = '" + id + "'");
        return baux.get(id);
    }

    public List<Bail> tousLesBaux() {
        executer("SELECT * FROM bail");
        List<Bail> liste = new ArrayList<Bail>(baux.values());
        liste.sort((a, b) -> a.getId().compareTo(b.getId()));
        return liste;
    }
}
