# multi-stage build for engine-service (builds only the module using root as context)
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /workspace
# copy full project into the build context so multi-module build can resolve all modules
COPY . .
RUN mvn -pl engineService -am -B -DskipTests package

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /workspace/engineService/target/*.jar app.jar
EXPOSE 8080
ENV JAVA_OPTS=""
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]
