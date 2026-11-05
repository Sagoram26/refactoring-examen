package fr.efrei.residence;

public enum TypeLogement {
    STUDIO("Studios", ""),
    T1("T1", ""),
    T2("T2", ""),
    COLOCATION("Colocation", " places");

    private final String libelle;
    private final String unite;

    TypeLogement(String libelle, String unite) {
        this.libelle = libelle;
        this.unite = unite;
    }

    public String getLibelle() { return libelle; }
    public String getUnite() { return unite; }
}
