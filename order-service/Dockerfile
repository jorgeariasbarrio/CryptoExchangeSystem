# multi-stage build for order-service (builds only the module using root as context)
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /workspace
# copy full project into the build context so multi-module build can resolve all modules
COPY . .
# build only the module and its dependencies
RUN mvn -pl order-service -am -B -DskipTests package

FROM eclipse-temurin:17-jre
WORKDIR /app
# copy built jar from module target
COPY --from=build /workspace/order-service/target/*.jar app.jar
EXPOSE 8081
ENV JAVA_OPTS=""
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]
