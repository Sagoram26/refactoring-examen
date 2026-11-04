package fr.efrei.residence;

import java.util.ArrayList;
import java.util.List;

public class EmailService implements CanalNotification {

    public static final String SMTP_HOST = "smtp.campusvert.fr";
    public static final String EXPEDITEUR = "gestion@campusvert.fr";

    public static List<String[]> journal = new ArrayList<String[]>();

    public void notifier(Etudiant e, String sujet, String message) {
        envoyer(e.getEmail(), sujet, message);
    }

    public void relancer(Etudiant e, String idBail, double solde) {
        envoyer(e.getEmail(), "RELANCE - loyer impayé",
                "Bonjour " + e.getPrenom() + ", il reste " + Utils.euros(solde) + " à régler sur le bail " + idBail + ".");
    }

    public static boolean envoyer(String destinataire, String sujet, String corps) {
        try {
            if (!Utils.emailValide(destinataire)) {
                throw new Exception("Adresse invalide");
            }
            System.out.println("[EMAIL] " + EXPEDITEUR + " -> " + destinataire + " | " + sujet);
            System.out.println("        " + corps);
            journal.add(new String[]{destinataire, sujet, corps});
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
