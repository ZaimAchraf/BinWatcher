# BinWatcher
binWatcher est une application d'apprentissage basée sur une architecture microservices pour gérer les bins, les conducteurs (drivers) et leurs assignations. L'application simule la gestion des niveaux de remplissage des bins et l'assignation des conducteurs pour la collecte.


![Bin_Watcher_Diagramme drawio](https://github.com/user-attachments/assets/49cb41f0-aea2-489c-a5f1-98b97230aba1)


## 🚀 Fonctionnalités principales

- **bin-service** : Service principal pour la gestion des bins. Il permet de créer, consulter et mettre à jour les bins.
- **driver-service** : Service pour la gestion des conducteurs (drivers). Il permet de créer, consulter et gérer les informations des conducteurs.
- **admin-service** : Un service utilisé par l'admin pour interroger les services `bin-service` et `driver-service` afin de gérer les bins et les conducteurs.
- **sensor-service** : Simule les capteurs des bins en générant des niveaux de remplissage aléatoires et en envoyant des messages dans un topic Kafka.
- **driver-assignment-service** : Analyse les alertes de niveau de remplissage, détecte les conducteurs les plus proches des bins et assigne les bins aux conducteurs.
- **notification-service** : Envoie des notifications par email aux conducteurs lorsqu'un bin est assigné ou désassigné.

## 🧑‍💼 Workflow métier

1. **Génération des niveaux de remplissage** :
   - Le service **sensor-service** récupère la liste des bins depuis le service **bin-service**.
   - Ensuite, **sensor-service** génère des niveaux de remplissage aléatoires pour chaque bin et publie ces informations dans le topic Kafka **bin-fill**.

2. **Vérification et mise à jour du niveau de remplissage** :
   - Le service **bin-service** écoute le topic **bin-fill** et reçoit les messages contenant les niveaux de remplissage des bins.
   - Lorsqu'un message est reçu, **bin-service** met à jour le niveau de remplissage du bin correspondant.
   - Si le niveau de remplissage dépasse un seuil pré-déterminé pour un bin, **bin-service** publie un message dans le topic **fill-alert** pour alerter les autres services.

3. **Assignation du conducteur** :
   - Le service **driver-assignment-service** écoute le topic **fill-alert** et récupère les alertes liées aux bins ayant dépassé le seuil de remplissage.
   - Pour chaque alerte reçue, **driver-assignment-service** analyse la localisation du bin et compare avec la liste des conducteurs disponibles.
   - Le service assigne le conducteur le plus proche pour récupérer le bin.
   - Un message est ensuite publié dans le topic **assignment-notif** pour notifier les autres services de l'assignation.

4. **Envoi de notifications** :
   - Le service **notification-service** écoute le topic **assignment-notif** et récupère les messages relatifs aux bins assignés ou désassignés.
   - Lorsqu'un message est reçu, **notification-service** envoie un email au conducteur pour l'informer qu'un bin lui a été assigné ou désassigné.

5. **Désassignation des bins** :
   - Si **bin-service** reçoit un message de niveau de remplissage inférieur au seuil pour un bin qui était déjà assigné à un conducteur, il envoie un appel HTTP à **driver-assignment-service** via OpenFeign pour désassigner le bin.
   - **driver-assignment-service** désassigne le conducteur du bin et envoie un message dans le topic **assignment-notif** pour notifier **notification-service**, qui enverra un email au conducteur concerné pour l'informer de la désassignation.

## 🧱 Architecture technique

L'application utilise les technologies suivantes :

- **Spring Boot** : Framework principal utilise.
- **Kafka** : Communication asynchrone entre les services via des topics.
- **Consul** : Chaque service s'enregistre automatiquement dans Consul pour permettre la découverte des services et gérer la communication entre eux.
- **Config Server** : Les services récupèrent leurs configurations à partir du `config-server`, qui lui-même récupère les configurations depuis un dépôt de configuration central.
- **Docker & Docker Compose** : Conteneurisation des services pour un déploiement simplifié.
- **MongoDB** : Base de données utilisée pour les services nécessitant de stocker des données.
- **Maildev** : Serveur SMTP de développement pour l'envoi d'emails.
- **ELK (Elasticsearch, Logstash, Kibana)** : Logging centralisé et suivi des logs.
- **Gateway Service** : Tous les appels externes passent par la Gateway.
- **Security Service** : Gère l'authentification et l'enregistrement des utilisateurs.
- **Config Service** : Centralisation des configurations pour les microservices.

> Lors de l'exécution, tous les services s'enregistrent dans **Consul** pour permettre la découverte automatique des services. Les services récupèrent également leurs configurations depuis le **config-server**, qui récupère les configurations à partir d'un dépôt centralisé de configurations.


## 📚 Structure des branches et chapitres

Chaque branche de ce projet correspond à un chapitre, qui introduit un concept ou une fonctionnalité spécifique :

- **Chapter 01** – Mise en place de l’architecture microservices  
- **Chapter 02** – Service discovery avec Consul  
- **Chapter 03** – Configuration centralisée et Config Server  
- **Chapter 04** – API Gateway et routage dynamique  
- **Chapter 05** – Sécurité et gestion des utilisateurs  
- **Chapter 06** – Service bin-service et gestion des bins  
- **Chapter 07** – Service driver-service et gestion des conducteurs  
- **Chapter 08** – Service driver-assignment-service et logique d’assignation  
- **Chapter 09** – Testing : tests unitaires et tests d’intégration  

## 📧 Contact
Créé par Zaim Achraf pour des raisons d'apprentissage. Si tu as des questions ou des suggestions, contacte-moi sur [linkedIn](https://www.linkedin.com/in/achraf-zaim-443936233/).
