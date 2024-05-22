# Calendappen

SAE 2023-2024 - Systèmes de gestion de rendez-vous en ligne

DEBAY Loïck, LOUVIER Gautier - groupe N

# I Bien démarrer :

### Toutes les actions décrites dans cette section sont explicatives et doivent être faites dans le fichier `parameters.properties` , se trouvant dans le dossier ``src/main/resources``

## Configuration des Jours de Travail

Spécifiez les jours de travail pour votre application de calendrier.

```properties
workday=monday,tuesday,thursday,friday,saturday,sunday 
#accepte: monday,tuesday,wednesday,thursday,friday,saturday,sunday
```

## Heures d'Ouverture et de Fermeture

Définissez les heures d'ouverture et de fermeture pour chaque jour de travail. Utilisez le format 24 heures (HH:mm).

```properties
openingTime=8:00,8:00,15:00,8:00,8:00,8:00,8:00

closingTime=20:30,20:30,20:30,20:30,20:30,20:30,20:30
```

Si le jour n'est jamais travaillé, mettez 0:00 en openingTime et en closingTime même exceptionnellement, sinon, suivez la procédure habituelle.

## Intervalle de Temps pour les Réservations

Spécifiez l'intervalle de temps pour les réservations. Utilisez le format HH:mm.

```properties
reservationTime=00:30
```

## Définissez la limite de temps maximale pour annuler une réservation

Utilisez le format J-X.

par exemple : nous sommes lundi, J-2 empêchera l'annulation des rendez-vous de mardi et mercredi.

```properties
maxtime=1
```

## Nombre Maximum de Personnes par Réservation

Fixez le nombre maximum de personnes autorisées par créneau de réservation.

```properties
maxPeople=2
```

## Test de l'Application

Pour utiliser l'application, il faut se connecter ou créer un compte depuis l'endpoint "login". Il est possible d'accéder au calendrier et aux pages de réservations sans compte depuis l'endpoint "public", mais toutes les autres actions redirigeront vers la page de connexion.

Une fois connecté, il est possible de naviguer dans le calendrier, prendre un rendez-vous, visualiser ses rendez-vous, modifier ses informations personnelles, et se déconnecter.

Un seul rendez-vous peut être pris par créneau, ils sont annulables jusqu'à J-X (1 par défaut, modifiable dans le fichier `parameters.properties`).

À la prise d'un rendez-vous, un email de confirmation est envoyé au client, ainsi qu'à l'administrateur. L'email et la configuration du compte de l'administrateur sont définis dans le fichier admin.properties. Le mot de passe doit être hashé en MD5, faire au minimum 8 caractères, et être écrit dans le fichier admin.properties pour le paramètre "password" (par exemple, utiliser : https://md5.gromweb.com/?md5=05a671c66aefea124cc08b76ea6d30bb).

L'email est envoyé par l'adresse précisée dans le fichier `email.properties`, elle doit être compatible avec le serveur SMTP, ici smtp.univ-lille.fr. Les détails du compte utilisé doivent être précisés dans le fichier application.properties, aux paramètres `spring.mail.username` et `spring.mail.password` (en clair).

Les rendez-vous pris peuvent être annulés depuis l'endpoint `reservationsViewer` ou directement depuis la page de réservation depuis laquelle le créneau a été pris. Un email de confirmation de l'annulation sera également envoyé à l'utilisateur.

Si l'utilisateur est connecté en tant qu'administrateur, il aura la possibilité d'ouvrir ou de fermer un jour depuis la page de réservation. Fermer un jour annulera automatiquement tous les rendez-vous pris en ce jour et enverra un mail aux clients concernés. 

Un administrateur pourra aussi ouvrir un jour normalement fermé, mais il faut faire attention à ce que les créneaux d'ouvertures soient bien définis dans le fichier `parameters.properties` (format d'entrée indiqué dans la rubrique "Heures d'Ouverture et de Fermeture").

Un administrateur peut également annuler un créneau spécifique, les rendez-vous existants seront alors annulés et un mail sera envoyé aux clients.

Il est possible pour un administrateur d'accéder à un créneau et d'en visualiser les rendez-vous, il pourra alors annuler manuellement n'importe lequel de ces rendez-vous, peu importe l'heure du rendez-vous.

# II Technos utilisées :

Dans cette section, nous abordons toutes les technologies utilisées dans le projet.

## Back-End :

Nous avons utilisé le Framework Java Spring pour réaliser la structure globale du côté serveur de notre application, pour le contrôle de l'accès aux vues, toute la partie de la fabrication des données persistées.

Pour la persistance des données nécessaires au bon fonctionnement de notre application, nous nous sommes basés sur une base Postgresql.

En concordance avec la dépendance Spring JPA pour nos interactions en base de données.

## Front-End:

Les JSP (Java Server Page / Jakarta Server Pages) sont utilisées pour afficher nos vues et se charger de récupérer les interactions des utilisateurs.

Ainsi que du HTML CSS pour la structure et la mise en place de nos pages.

Une de nos fonctionnalités utilise un script réalisé en JavaScript pour l'affichage d'une de nos informations pour les utilisateurs.

# III Points difficiles :

###### Affichage du pourcentage :

L'affichage du pourcentage d'occupation d'un créneau était assez complexe. Il fallait d'abord pouvoir récupérer la liste de tous les créneaux dans le contrôleur de l'endpoint "calendrier" pour pouvoir ensuite récupérer les créneaux pour chaque date du mois courant.
À chaque itération, une case du tableau était générée et remplie, et le pourcentage était calculé. Il a fallu calculer le nombre de créneaux disponibles par jour à partir du timeStamp d'ouverture, de celui de fermeture, et de la longueur des créneaux.
Ce n'était pas particulièrement difficile, mais cela nous a pris bien plus de temps que prévu et s'est également avéré être plus complexe que prévu.

#### Sécuriser l'authentification :

Difficulté d'adapter le schéma de notre table de client avec security pour sécuriser nos End Points, nous avons essayé avec des tutoriels de réaliser une configuration personnalisée, mais nous n'y sommes pas parvenus, et la mise en place d'un filtre global sur notre application ne fonctionnait pas comme sur les premiers TP, Spring passait avant les filtres sur notre application, nous n'avons pas trouvé de solution à temps pour protéger proprement.



Nous avons donc essayé de faire une vérification sur chaque endpoint à la main dans les contrôleurs, avec une vérification de la présence de l'objet `Principal` créé par nous-même et d'une propriété dans celui-ci pour savoir si le compte connecté est administrateur ou non. Si celui-ci est `null` ou s'il ne remplit pas les conditions, nous sommes automatiquement renvoyés à la page `/Login`. Nous savons que ce n'est pas la meilleure solution, mais cela nous permet de ne pas subir une erreur dès que notre session expire, par exemple.

#### La modification de compte :

Ne pas avoir les mêmes du la validation d'un nouveau client passé en signature de la fonction dans le contrôleur, nous avons donc utilisé des classes de "Rules" ainsi que l'annotation @Valid pour distinguer les règles à appliquer ou non selon le contexte de l'utilisation de la validation automatique.

# VI Améliorations potentielles :

#### Plus de fonction côté administrateur:

- Enregistrer soi-même un utilisateur à un créneau donné

- Pouvoir gérer la liste des utilisateurs depuis le Front-End

- Une recherche dynamique sur les créneaux (en lien avec la refonte de leur structure)

#### Une refonte globale de la manière de contenir les créneaux pour permettre une meilleure flexibilité au propriétaire du calendrier.

#### Refactoring poussé du code :

Sur les parties de code possibles à déplacer côté service dans le backend pour décharger nos JSP et avoir une meilleure flexibilité sur l'évolution de l'application web.

#### Sécurité :

Une meilleure sécurité permettant une utilisation sans encombre de l'application, comprenant les problèmes de non-authentification, un meilleur contrôle de l'expiration de l'authentification.


