# 🛠️ Chapitre 05 — Driver Assignment Service

## 🎯 Objectif
Le **Driver-Assignment-Service** est responsable d'écouter les alertes de remplissage (FillAlert) générées par le Bin-Service, d’assigner automatiquement le bin concerné au conducteur le plus proche, et d’envoyer une notification (AssignmentNotif) à un service externe (Notification-Service).  
Il offre également la possibilité de **désassigner** un bin si son niveau de remplissage redescend sous le seuil d’alerte.

---

## 🔄 Communication Flow

### Bin-Service
- Lorsqu'un niveau de remplissage dépasse le seuil :
  - Produit un message **FillAlert** sur le topic Kafka `fill-alert`.
- Lorsqu'un niveau repasse sous le seuil :
  - Met à jour le statut du bin.
  - Appelle l’API du Driver-Assignment-Service pour désactiver toute affectation active liée à ce bin.

### Driver-Assignment-Service
1. **Réception des alertes**
   - Consomme les messages `fill-alert` via Kafka.
2. **Assignation**
   - Vérifie si le bin est déjà assigné.
   - Récupère la liste des drivers depuis le **Driver-Service**.
   - Recherche le driver le plus proche en utilisant les **coordonnées (latitude/longitude)** enregistrées dans le Driver-Service.
   - Calcule la distance avec la **formule de Haversine**.
   - Crée un enregistrement d’assignation et produit un message **AssignmentNotif** sur Kafka (pour le Notification-Service).
3. **Désassignation**
   - Rend inactives toutes les assignations pour un bin si le niveau de remplissage est repassé sous le seuil.
   - Produit un message **AssignmentNotif** de type `UNASSIGNMENT`.

---

## 📦 Kafka Topics utilisés
- **fill-alert** → produit par le Bin-Service, consommé par le Driver-Assignment-Service.
- **assignment-notif** → produit par le Driver-Assignment-Service, consommé par le Notification-Service.

---

## 🛠️ Principales classes

### 1. `AssignmentService`
- Contient la logique métier :
  - Vérifie si un bin est déjà assigné.
  - Récupère les drivers disponibles via `DriverClient`.
  - Trouve le plus proche grâce à la **formule de Haversine**.
  - Sauvegarde l’assignation dans la base.
  - Émet un **AssignmentNotif**.
  - Désactive les assignations si nécessaire.

### 2. `FillAlertConsumer`
- Écoute les messages Kafka du topic `fill-alert`.
- Appelle `AssignmentService.assign()`.

### 3. `AssignmentController`
- Expose un endpoint :
  - `PUT /api/assignments/{binId}` → Désactivation manuelle des assignations pour un bin.

### 4. `KafkaConfig`
- Configure :
  - **Producer** pour `AssignmentNotif`.
  - **Consumer** pour `FillAlert` (KafkaListener avec acknowledgment manuel).
