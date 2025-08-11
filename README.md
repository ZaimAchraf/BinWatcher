# 📦 Chapitre 1 : Architecture Technique Microservices

## 🌟 **Résumé du Chapitre**
Ce chapitre couvre la mise en place d'une architecture microservices de base avec :
- **Service Discovery** (Consul)  
- **Configuration centralisée** (Spring Cloud Config)  
- **API Gateway dynamique** (Spring Cloud Gateway)  
- **Gestion des pannes** (Circuit Breaker avec Resilience4j)  

---

## 🔧 Composants Implémentés

### 1. Service Discovery avec Consul  
- **Centralise la détection des services**  
- Permet le **routage dynamique** (`lb://nom-service`)  
- Effectue des **health checks automatiques** pour vérifier l'état des services  

> ⚠️ Cette partie **ne sera pas disponible sur GitHub** car Consul est installé sur une infrastructure externe via un lien spécifique.  
>  
> Pour lancer Consul localement, vous pouvez utiliser :  
>  
> - **Mode serveur (production/dev avancé)** :  
> ```bash
> consul agent -server -bootstrap-expect=1 -data-dir=consul-data -ui -bind=192.168.0.103
> ```  
>  
> - **Mode développement (rapide et simple)** :  
> ```bash
> consul agent -dev
> ```  
>  
> Le mode développement démarre Consul avec une configuration minimale, idéal pour les tests locaux.

---

### 2. Config Server
- Stocke la configuration dans un **dépôt Git** (dans notre cas, c'est le dépôt : *BinWatcher_config*)  
- Supporte le **refresh à chaud** grâce à l’annotation `@RefreshScope`

**Exemple de configuration :**
```yaml
# application-test.yml
defaultX: 100
defaultY: 200
```
---

### 3. API Gateway

L’API Gateway joue un rôle clé dans l’architecture microservices en centralisant le point d’entrée des requêtes clients. Voici ses principales fonctionnalités :

| Fonctionnalité      | Détails                                                                                 |
|--------------------|-----------------------------------------------------------------------------------------|
| Routage dynamique  | Le gateway redirige les requêtes vers les microservices correspondants selon le chemin d’URL. Par exemple, `/test/**` est redirigé vers le service nommé `test` grâce à `lb://test`. Cela permet de gérer facilement l’évolution des services. |
| Circuit Breaker    | Protège les services en détectant un taux d’échec élevé (ici fixé à 50%). Si ce seuil est dépassé, il coupe temporairement les appels vers le service en difficulté pour éviter la surcharge. |
| Fallback Mechanism | En cas de défaillance d’un service, une réponse personnalisée (fallback) est renvoyée pour garantir une meilleure expérience utilisateur sans bloquer l’ensemble du système. |

Ce mécanisme assure la robustesse, la résilience et la scalabilité de l’ensemble des microservices.



---

### 4. Test Service

Vérifie que :  
- La configuration est correctement chargée  
- Le Service Discovery fonctionne  
- Le Gateway route correctement les requêtes
