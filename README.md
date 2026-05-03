# Kubernetes Spring Boot & PostgreSQL Demo

This project is a complete, containerized Spring Boot microservice connected to a PostgreSQL database. It is designed to demonstrate local deployment using Docker Compose and orchestration using Kubernetes via Minikube.

## 🚀 Features
* **Java 17 & Spring Boot 3.x**
* **Spring Data JPA** configured for PostgreSQL
* **Dockerized** application with a multi-architecture optimized Dockerfile
* **Docker Compose** setup for easy local development
* **Kubernetes Manifests** including Secrets, PersistentVolumeClaims, Deployments, and NodePort Services
* Custom `/api/v1/health` endpoint for readiness checking

---

## 📋 Prerequisites
Ensure you have the following installed on your machine:
* [Java 17](https://adoptium.net/)
* [Maven](https://maven.apache.org/)
* [Docker Desktop](https://www.docker.com/products/docker-desktop/)
* [Minikube](https://minikube.sigs.k8s.io/docs/start/)
* [kubectl](https://kubernetes.io/docs/tasks/tools/)

---

## 🛠️ Local Development (Docker Compose)
Before deploying to Kubernetes, you can test the entire stack locally using Docker Compose. This spins up the PostgreSQL database and the Spring Boot application together.

**1. Build the application JAR:**
```bash
mvn clean package -DskipTests
```

**2. Start the environment:**
```bash
docker compose up -d --build
```

**3\. Test the application:** Open your terminal and run:

```bash
curl http://localhost:9000/api/v1/health
```

*Expected Output: `Application is healthy!`*

**4\. Shut down the local environment:**

```bash
docker compose down
```

☸️ Kubernetes Deployment (Minikube)
-----------------------------------

Follow these steps to deploy the application into your local Kubernetes cluster.

**1\. Start Minikube:**

```bash
minikube start
```

**2\. Point Docker to Minikube's Daemon:** *This step is crucial. It ensures the image you build is stored inside Minikube, bypassing the need for an external registry like Docker Hub.*

```bash
eval $(minikube docker-env)
```

**3\. Build the JAR and Docker Image:**

```bash
mvn clean package -DskipTests
docker build -t k8s-demo-project:0.0.1 .
```

**4\. Apply the Kubernetes Manifests:** This command creates the database secret, persistent volume, deployments, and services.

```bash
kubectl apply -f k8s-demo.yaml

```

**5\. Verify the Pods are Running:**

```bash
kubectl get pods -w
```

*Wait until both `k8s-db-deployment` and `k8s-demo-app-deployment` show a status of `Running`.*

**6\. Access the Application:** Create a tunnel from your machine to the Minikube cluster:

```bash
minikube service k8s-demo-app-service
```

This will open a browser window with a dynamic local URL. Append `/api/v1/health` to the URL to view the health check response.


### Kubernetes Management
-   **View the graphical dashboard:** `minikube dashboard`
-   **Get all running resources:** `kubectl get all`
-   **View application logs:** `kubectl logs -l app=k8s-demo-app -f`

### Application Updates
If you change the Java code, run this sequence to update your deployment with zero downtime:

```bash
mvn clean package -DskipTests
eval $(minikube docker-env)
docker build -t k8s-demo-project:0.0.1 .
kubectl rollout restart deployment k8s-demo-app-deployment
```

### Scaling
Kubernetes makes it easy to scale the stateless Spring Boot application:
-   **Scale up to 3 instances:** `kubectl scale deployment k8s-demo-app-deployment --replicas=3`
-   **Scale down to 1 instance:** `kubectl scale deployment k8s-demo-app-deployment --replicas=1`

### Cleanup
To remove all Kubernetes resources created by this project:
```bash
kubectl delete -f k8s-demo.yaml
```