FROM adoptopenjdk:17-jdk-hotspot AS build
COPY . .
RUN mvn clean package -DskipTests

FROM adoptopenjdk:17-jre-hotspot
COPY --from=build /ShopmeWebParent/ShopmeBackEnd/target/ShopmeBackEnd-0.0.1-SNAPSHOT.jar /ShopmeWebParent/ShopmeBackEnd/target/ShopmeBackEnd-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","ShopmeBackEnd-0.0.1-SNAPSHOT.jar"]
