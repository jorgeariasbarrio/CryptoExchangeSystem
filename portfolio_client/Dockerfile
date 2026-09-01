# multi-stage build for portfolio-service (builds only the module using root as context)
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /workspace
# copy full project into the build context so multi-module build can resolve all modules
COPY . .
RUN mvn -pl portfolio_client -am -B -DskipTests package

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /workspace/portfolio_client/target/*.jar app.jar
EXPOSE 8082
ENV JAVA_OPTS=""
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]
