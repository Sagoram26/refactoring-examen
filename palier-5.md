# Palier 5 : responsabilité unique et inversion des dépendances (S, D)

## Objectif
- Découper `GestionResidence` : validation des inscriptions, attribution, cycle de vie du bail, notifications, rapport. Sortir la mise en forme de la quittance de `Bail`.
- Supprimer le singleton `Database` et tous les `static` modifiables. Introduire des interfaces et injecter les dépendances par constructeur.
- Seuls `Main` et l'adaptateur construisent le graphe d'objets.
- Terminé quand : plus aucun `static` modifiable, plus aucun `getInstance()`, plus aucun `new` d'un service dans un autre service.

## Ce qui a été fait
### Découpage (S) : chaque classe se décrit en une phrase sans « et »
| Classe | Phrase |
|---|---|
| `ServiceInscription` | Valide et enregistre l'inscription des étudiants. |
| `ServiceAttribution` | Attribue à un étudiant le premier logement qui lui convient. |
| `ServiceBail` | Fait évoluer un bail au fil de son cycle de vie. |
| `Notificateur` | Envoie les messages aux étudiants sur leur canal préféré. |
| `ServiceRelance` | Relance les étudiants dont le bail présente un impayé. |
| `Rapport` | Produit le rapport d'occupation de la résidence. |
| `Quittance` | Met en forme la quittance d'un bail. |
| `Residence` | Gère le parc de logements. |

`GestionResidence` est supprimée. La quittance et la liste des options sortent de `Bail`, qui ne garde que ses données.

### Inversion des dépendances (D)
- Interfaces créées : `EtudiantRepository` et `BailRepository`. `Database` les implémente. Les services dépendent des interfaces, pas de la base. `CanalNotification` existait déjà (palier 2).
- Injection par constructeur : par exemple `new ServiceBail(baux, notificateur)`.
- `CanalFactory` reçoit les trois services de canal par constructeur et renvoie la bonne instance. Il n'y a plus de `new SmsService()` à chaque message, ce qui garde un seul compteur de SMS et un seul journal.

### Fin de l'état global
| Avant (`static` modifiable) | Après |
|---|---|
| `Database.getInstance()`, `Database.etudiants`, `Database.baux` | `new Database()`, maps d'instance |
| `EmailService.journal`, `SmsService.compteurSms`, `CourrierService.totalAffranchissement` | champs d'instance + getters |
| `Residence.logements`, `Residence.initialiser()` | `new Residence()` charge le catalogue |
| `Residence.CATALOGUE` (tableau modifiable) | `List.of(...)` immuable |
| `Etudiant.compteur`, `GestionResidence.compteurBail` | compteurs d'instance dans `ServiceInscription` et `ServiceAttribution` |

## Le graphe d'objets (dans `Main` et dans l'adaptateur)
```java
Database db = new Database();
EmailService email = new EmailService();
SmsService sms = new SmsService();
CourrierService courrier = new CourrierService();
Notificateur notificateur = new Notificateur(new CanalFactory(email, sms, courrier), email);
Residence residence = new Residence();
ServiceInscription inscription = new ServiceInscription(db);
ServiceAttribution attribution = new ServiceAttribution(residence, db, new CalculateurLoyer(), notificateur);
ServiceBail baux = new ServiceBail(db, notificateur);
ServiceRelance relances = new ServiceRelance(db, notificateur);
Rapport rapport = new Rapport(residence, db, sms, courrier);
Quittance quittance = new Quittance();
```
Dans l'adaptateur, `reinitialiser()` reconstruit ce graphe. Il n'y a plus 11 lignes de « vidage » de champs statiques.

## Pourquoi
- S : une classe = une raison de changer. Un changement du format de la quittance ne touche plus `Bail`. Un changement des relances ne touche plus l'attribution.
- D : les services dépendent d'abstractions injectées. On peut remplacer la base simulée par une vraie base, ou par une fausse en test, sans toucher aux services.
- Plus d'état global : deux graphes d'objets sont totalement indépendants. Les tests n'ont plus besoin de remettre des compteurs à zéro.

## Points d'attention
- La sortie de `Main` est identique à celle du legacy (vérifié par `diff`), à part la ligne `DUPLEX` retirée au palier 1.
- L'ordre de construction préserve l'affichage `[DB] Connexion…` en tête, comme avant.
- Les constantes `static final` immuables restent autorisées (`NOM`, `PRIX_TIMBRE`, `EXPEDITEUR`), ainsi que les fonctions utilitaires pures de `Utils` et la méthode `LogementFactory.creer`, qui ne modifient aucun état.

## Questions possibles
- Pourquoi deux interfaces de repository et une seule classe `Database` ? Chaque service ne voit que ce dont il a besoin : `ServiceInscription` ne voit pas les baux (cela respecte aussi le I).
- Pourquoi `Notificateur` et `ServiceRelance` sont-ils séparés ? Le premier *envoie* un message. Le second *choisit* quels baux relancer.
- Qui crée les objets ? Uniquement `Main` (l'application) et l'adaptateur (les tests). C'est la « racine de composition ».

## Vérification
`mvn install` : 101 tests verts pour le legacy, 110 tests verts pour le nouveau code. `grep getInstance` et `grep "static"` (hors `final`) ne trouvent plus rien.
