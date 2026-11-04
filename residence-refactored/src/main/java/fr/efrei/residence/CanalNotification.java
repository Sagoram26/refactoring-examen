package fr.efrei.residence;

public interface CanalNotification {

    void notifier(Etudiant e, String sujet, String message);

    void relancer(Etudiant e, String idBail, double solde);
}
