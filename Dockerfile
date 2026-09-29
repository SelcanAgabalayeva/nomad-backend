FROM maven:3.9.9-eclipse-temurin-17

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY . .

RUN mvn clean package -DskipTests

EXPOSE 8080

ENTRYPOINT ["sh","-c","java -jar target/*.jar"]