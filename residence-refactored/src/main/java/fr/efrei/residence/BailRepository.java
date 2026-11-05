package fr.efrei.residence;

import java.util.List;

public interface BailRepository {

    void sauvegarderBail(Bail b);

    void mettreAJourBail(Bail b);

    Bail trouverBail(String id);

    List<Bail> tousLesBaux();
}
