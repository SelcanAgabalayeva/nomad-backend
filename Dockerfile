FROM maven:3.9.9-eclipse-temurin-17

WORKDIR /app

COPY . .

RUN mvn -B -U -Dmaven.wagon.http.retryHandler.count=5 clean package -DskipTests

EXPOSE 8080

ENTRYPOINT ["sh","-c","java -jar target/*.jar"]