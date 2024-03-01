FROM eclipse-temurin:17-jdk-alpine as builder
WORKDIR /back-end/media
COPY mvnw pom.xml ./
RUN apk add --no-cache maven
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean install

FROM eclipse-temurin:17-jre-alpine as runner
WORKDIR /back-end
COPY --from=builder /back-end/media/target/*.jar /back-end/*.jar
ENTRYPOINT ["java","-jar","/back-end/*.jar"]

