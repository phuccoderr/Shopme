FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM 3.8.5-openjdk-17-slim
COPY --from=build /ShopmeWebParent/ShopmeBackEnd/target/ShopmeBackEnd-0.0.1-SNAPSHOT.jar demo.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","demo.jar"]
