package fr.efrei.residence;

import java.util.ArrayList;
import java.util.List;

public class CourrierService implements CanalNotification {

    public static final double PRIX_TIMBRE = 1.29;
    public static double totalAffranchissement = 0;
    public static List<String[]> journal = new ArrayList<String[]>();

    public void notifier(Etudiant e, String sujet, String message) {
        imprimerEtPoster(e.getNomComplet(), e.getAdresse(), sujet, message);
    }

    public void relancer(Etudiant e, String idBail, double solde) {
        imprimerEtPoster(e.getNomComplet(), e.getAdresse(), "Relance loyer impayé",
                "Il reste " + Utils.euros(solde) + " à régler sur le bail " + idBail + ".");
    }

    public void imprimerEtPoster(String nom, String adresse, String objet, String texte) {
        totalAffranchissement = totalAffranchissement + PRIX_TIMBRE;
        journal.add(new String[]{adresse, objet, texte});
        System.out.println("[COURRIER] " + nom + ", " + adresse + " | Objet : " + objet);
        System.out.println("        " + texte);
    }
}
