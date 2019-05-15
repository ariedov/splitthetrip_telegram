ARG VERSION=8u151

FROM openjdk:${VERSION}-jdk as BUILD

COPY . /app
WORKDIR /app
RUN ./gradlew --no-daemon build -x test

EXPOSE 8080
ADD ./build/libs/splitthetrip-1.0-SNAPSHOT.jar .

CMD ["java", "-jar", "splitthetrip-1.0-SNAPSHOT.jar"]
