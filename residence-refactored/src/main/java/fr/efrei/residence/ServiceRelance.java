package fr.efrei.residence;

public class ServiceRelance {

    private final BailRepository baux;
    private final Notificateur notificateur;

    public ServiceRelance(BailRepository baux, Notificateur notificateur) {
        this.baux = baux;
        this.notificateur = notificateur;
    }

    public void relancerImpayes() {
        System.out.println("--- Relance des impayés");
        for (Bail b : baux.tousLesBaux()) {
            if (b.soldeDu <= 0) continue;
            if (b.getStatut() == StatutBail.ANNULE) continue;
            notificateur.relancer(b.getEtudiant(), b.getId(), b.soldeDu);
        }
    }
}
