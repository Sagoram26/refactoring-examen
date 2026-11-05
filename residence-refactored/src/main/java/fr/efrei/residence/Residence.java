package fr.efrei.residence;

import java.util.ArrayList;
import java.util.List;

public class Residence {

    public static final String NOM = "Résidence Campus Vert";

    // TYPE;NUMERO;SURFACE;ETAGE;MEUBLE;PARAM (PARAM = nb de places en coloc, surface du balcon en T2)
    private static final List<String> CATALOGUE = List.of(
            "STUDIO;A001;26;0;true;0",
            "STUDIO;A102;22;1;true;0",
            "STUDIO;B103;20;1;false;0",
            "T1;A104;28;1;true;0",
            "T1;B105;30;1;false;0",
            "T2;A201;45;2;false;6",
            "T2;B202;48;2;true;0",
            "COLOCATION;B301;80;3;true;4",
            "COLOCATION;C001;63;0;true;3"
    );

    private final List<Logement> logements = new ArrayList<Logement>();

    public Residence() {
        for (String ligne : CATALOGUE) {
            String[] t = ligne.split(";");
            logements.add(LogementFactory.creer(TypeLogement.valueOf(t[0]), t[1], Double.parseDouble(t[2]),
                    Integer.parseInt(t[3]), Boolean.parseBoolean(t[4]), Integer.parseInt(t[5])));
        }
    }

    public List<Logement> getLogements() { return logements; }

    public Logement ajouterLogement(TypeLogement type, String numero, double surface, int etage, boolean meuble, int param) {
        if (trouver(numero) != null) {
            System.out.println("Logement " + numero + " existe déjà");
            return null;
        }
        Logement l = LogementFactory.creer(type, numero, surface, etage, meuble, param);
        logements.add(l);
        System.out.println("Logement ajouté : " + numero + " (" + type + ")");
        return l;
    }

    public Logement trouver(String numero) {
        for (Logement l : logements) {
            if (l.getNumero().equals(numero)) return l;
        }
        return null;
    }
}
