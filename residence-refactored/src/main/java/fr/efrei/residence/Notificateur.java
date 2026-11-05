package fr.efrei.residence;

public class Notificateur {

    private final CanalFactory canaux;
    private final EmailService email;

    public Notificateur(CanalFactory canaux, EmailService email) {
        this.canaux = canaux;
        this.email = email;
    }

    public void notifier(Etudiant e, String sujet, String message) {
        Canal canal = e.getCanalPrefere();
        canaux.creer(canal).notifier(e, sujet, message);
        if (e.getProfil().getRegle().recoitEmailEnAnglais() && canal != Canal.EMAIL) {
            email.envoyer(e.getEmail(), "[EN] " + sujet, message);
        }
    }

    public void relancer(Etudiant e, String idBail, double solde) {
        canaux.creer(e.getCanalPrefere()).relancer(e, idBail, solde);
    }
}
