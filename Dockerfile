FROM openjdk-21
WORKDIR /app
COPY target/ecommerce-0.0.1-SNAPSHOT.jar ecom.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","ecom.jar"]
