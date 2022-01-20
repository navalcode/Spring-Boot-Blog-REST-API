#FROM adoptopenjdk/openjdk11:alpine-jre
#VOLUME /tmp
#ARG JAR_FILE=target/*.jar
#COPY ${JAR_FILE} /app.jar
#ENTRYPOINT ["java","-jar","/app.jar"]

# FROM openjdk:17-jdk-alpine as build
FROM maven:3.8.3-openjdk-17 as build
WORKDIR /workspace/app

#COPY mvnw .
#COPY .mvn .mvn
COPY *mvn* .
COPY pom.xml .
COPY src src

RUN mvn install -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)



FROM openjdk:17-jdk-alpine
VOLUME /tmp

ARG DEPENDENCY=target/dependency
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
COPY wait.sh .
RUN chmod +x wait.sh
#ENTRYPOINT ["java","-cp","app:app/lib/*","com.sopromadze.blogapi.BlogApiApplication"]
ENTRYPOINT [ "./wait.sh" ]