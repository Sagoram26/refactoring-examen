package fr.efrei.residence;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DecorateursTest {

    private static final double CENTIME = 0.001;

    private final Loyer cent = () -> 100;
    private final Logement studio = LogementFactory.creer(TypeLogement.STUDIO, "X1", 20, 1, false, 0);
    private final Logement colocation = LogementFactory.creer(TypeLogement.COLOCATION, "X2", 60, 1, false, 3);

    @Test
    void loyer_de_base() {
        assertEquals(350, new LoyerDeBase(studio).montant(), CENTIME);
    }

    @Test
    void meuble() {
        assertEquals(115, new Meuble(cent).montant(), CENTIME);
    }

    @Test
    void rez_de_chaussee() {
        assertEquals(95, new RezDeChaussee(cent).montant(), CENTIME);
    }

    @Test
    void menage() {
        assertEquals(108, new Menage(cent, studio).montant(), CENTIME);
        assertEquals(104, new Menage(cent, colocation).montant(), CENTIME);
    }

    @Test
    void parking() {
        assertEquals(145, new Parking(cent).montant(), CENTIME);
    }

    @Test
    void laverie() {
        assertEquals(112, new Laverie(cent).montant(), CENTIME);
    }

    @Test
    void fibre() {
        assertEquals(120, new Fibre(cent, studio).montant(), CENTIME);
        assertEquals(100, new Fibre(cent, colocation).montant(), CENTIME);
    }

    @Test
    void assurance() {
        assertEquals(109, new Assurance(cent).montant(), CENTIME);
    }

    @Test
    void remise() {
        assertEquals(90, new Remise(cent, Profil.BOURSIER.getRegle(), 2).montant(), CENTIME);
        assertEquals(100, new Remise(cent, Profil.STANDARD.getRegle(), 0).montant(), CENTIME);
    }
}
