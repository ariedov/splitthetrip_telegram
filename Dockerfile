ARG VERSION=8u151

FROM openjdk:${VERSION}-jdk as BUILD

COPY . /app
WORKDIR /app
RUN ./gradlew --no-daemon build -x test

EXPOSE 8080

FROM openjdk:${VERSION}-jre

COPY --from=BUILD /app/build/libs/splitthetrip-1.0-SNAPSHOT.jar /bin/runner/run.jar
WORKDIR /bin/runner

CMD ["java", "-jar", "run.jar"]