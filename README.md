# 💱 Plataforma de Microservicios - Exchange

Proyecto que simula un exchange con arquitectura de microservicios: creación de órdenes, reservas de saldo/activos y un motor de matching asíncrono.

## 🧩 Arquitectura

Microservicios:
- order-service — API para crear órdenes y persistirlas (puerto: 8081).
- portfolio-service (a.k.a. portfolio-client) — gestión de carteras / saldos / activos (puerto: 8082).
- engine-service — matching engine que consume eventos y genera trades (puerto: 8080 por defecto si no se configura).

Infra común (Docker Compose en el root):
- Zookeeper: 2181
- Kafka: 9092
- MongoDB: 27017

> Nota: no hay un docker-compose que incluya los jars de los microservicios por defecto; las instrucciones abajo explican cómo ejecutarlos.

## ✅ Puertos
- zookeeper: 2181
- kafka: 9092
- mongo: 27017
- order-service: 8081
- portfolio-service: 8082
- engine-service: 8080 (si no se especifica server.port)

## 🔧 Prerrequisitos
- Docker & Docker Compose
- Java 17+
- Maven 3.8+

## 🚀 Levantar el entorno (paso a paso)

1) Levantar la infraestructura (Kafka, Zookeeper, Mongo):

```bash
cd <repo-root>
docker compose up -d
```

2) Construir y ejecutar microservicios localmente (opción recomendada para desarrollo):

En tres terminales independientes, o ejecutar uno a uno:

```bash
# Order service
cd order-service
mvn clean package -DskipTests
# Ejecutar con maven
mvn spring-boot:run
# o ejecutar el jar generado
# java -jar target/*.jar

# Portfolio service
cd ../portfolio_client
mvn clean package -DskipTests
mvn spring-boot:run

# Engine service
cd ../engineService
mvn clean package -DskipTests
mvn spring-boot:run
```

3) Verificar: las APIs estarán en los puertos listados arriba. Kafka en localhost:9092 y Mongo en localhost:27017.

## 🧪 Datos de ejemplo (seed)

Se han añadido initializers para cargar datos de prueba cuando las colecciones están vacías:
- portfolio_client: DataInitializer (inserta carteras de ejemplo con saldo y activos)
- order-service: DataInitializer (inserta un par de órdenes de ejemplo)

Estos beans implementan CommandLineRunner y se ejecutan al iniciar cada microservicio. Si desea reinsertar los datos, vacíe las colecciones `portfolios` y `orders` en MongoDB y reinicie la aplicación.

## Ejecutar todo con Docker Compose (opción avanzada)

Si se desea contenerizar y orquestar los microservicios junto a la infra, crear un docker-compose.override.yml en el root que añada servicios para los jars/imagenes de cada microservicio enlazando los puertos (ej. build: ./order-service). El repo actualmente solo provee el compose para la infraestructura.
