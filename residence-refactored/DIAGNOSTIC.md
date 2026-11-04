# Diagnostic du code historique

| Défaut | Emplacement | Principe violé |
|---|---|---|
| Une seule classe gère inscription, logements, attribution, cycle du bail, relances, rapport et notifications | `GestionResidence` | S (responsabilité unique) |
| Le bail met en forme sa propre quittance | `Bail.genererQuittance` | S (responsabilité unique) |
| La base de données mélange étudiants et baux, et c'est un singleton | `Database` | S (responsabilité unique) |
| Ajouter un type de logement oblige à modifier deux `switch` de création, le calcul du loyer et le rapport | `Residence.initialiser`, `GestionResidence.ajouterLogement`, `CalculateurLoyer`, `GestionResidence.genererRapport` | O (ouvert/fermé) |
| Ajouter un profil oblige à modifier remise, solvabilité, caution, frais, préavis et pénalité, éparpillés dans plusieurs méthodes | `CalculateurLoyer.calculerLoyer`, `GestionResidence` | O (ouvert/fermé) |
| Ajouter une option oblige à changer la signature de `calculerLoyer`, de `attribuerLogement` et les champs de `Bail` (5 booléens) | `CalculateurLoyer`, `Bail`, `GestionResidence` | O (ouvert/fermé) |
| Ajouter un canal oblige à modifier `notifier` et `relancerImpayes` | `GestionResidence` | O (ouvert/fermé) |
| `Colocation` refuse `loger()`/`liberer()` : elle ne peut pas remplacer un `Logement`, d'où les `instanceof Colocation` | `Colocation`, `GestionResidence.attribuerLogement/sortir/annulerBail` | L (substitution de Liskov) |
| `T1.getColocataires()` renvoie `null`, `Studio.getSurfaceBalcon()` lève une exception : les sous-types ne respectent pas le contrat | `T1`, `Studio`, `T2` | L (substitution de Liskov) |
| `ILogement` impose à tous les logements les méthodes de colocation, de balcon et d'entretien, d'où des `UnsupportedOperationException` et un `catch` vide dans le rapport | `ILogement`, `GestionResidence.genererRapport` | I (ségrégation des interfaces) |
| Les services créent eux-mêmes leurs dépendances (`Database.getInstance()`, `new CalculateurLoyer()`, `new SmsService()`) | `GestionResidence` | D (inversion des dépendances) |
| État global modifiable (`Residence.logements`, `Database.baux`, `EmailService.journal`, compteurs `static`) | `Residence`, `Database`, `EmailService`, `SmsService`, `CourrierService`, `Etudiant`, `GestionResidence` | D (inversion des dépendances) |
| Codes `String` pour types, profils, canaux, statuts et nombres magiques | partout | hors SOLID : lisibilité, fautes de frappe non détectées |
