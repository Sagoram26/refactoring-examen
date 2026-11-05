package fr.efrei.residence;

import java.util.function.BiFunction;

public enum Option {
    MENAGE("ménage", Menage::new),
    PARKING("parking", (loyer, logement) -> new Parking(loyer)),
    LAVERIE("laverie", (loyer, logement) -> new Laverie(loyer)),
    FIBRE("fibre", Fibre::new),
    ASSURANCE("assurance", (loyer, logement) -> new Assurance(loyer));

    private final String libelle;
    private final BiFunction<Loyer, Logement, Loyer> decorateur;

    Option(String libelle, BiFunction<Loyer, Logement, Loyer> decorateur) {
        this.libelle = libelle;
        this.decorateur = decorateur;
    }

    public String getLibelle() { return libelle; }

    public Loyer decorer(Loyer loyer, Logement logement) {
        return decorateur.apply(loyer, logement);
    }
}
