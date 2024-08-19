# Use an official OpenJDK runtime as a parent image
FROM openjdk:21-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the application JAR file to the container
COPY target/shwe-0.0.1-SNAPSHOT.jar /app/shwe.jar

# Expose the port the app runs on
EXPOSE 8080
# Set environment variables for Spring Boot application
ENV SPRING_APPLICATION_NAME=shwe \
    SPRING_DATASOURCE_URL=jdbc:mysql://178.128.25.88:3306/shwe \
    SPRING_DATASOURCE_USERNAME=root \
    SPRING_DATASOURCE_PASSWORD=172024P@ssword \
    SPRING_DATASOURCE_DRIVER_CLASS_NAME=com.mysql.cj.jdbc.Driver \
    SPRING_JPA_HIBERNATE_DDL_AUTO=update \
    SPRING_JPA_SHOW_SQL=true \
    JWT_SECRET_KEY=AL133Dx08UYJwL7YalPHs5uqq6hFoNX4CyGUrhNzTDN+/GehW+pZQ0sGH+Y76+Ditt4xzwcn5HTuLCIuMbyX6/7hpcyz+d1Y4pt+gpQcCkvay6gppjDOojrsehZSz/0PwR2BnaY4aK06QDoTnjwpSC6DPwQjVUf5nKYZByVK67HPPu3hxMKm+GkxUwQpswy8TxV1iLWKkSHAyNNySNxnOYF/RJq+7JwKMnaGBOxrS6OnBIRx3nzghyY+ZJ/6lCRKHgsxhuzDQvhC8UDWMK5tAjI5OIOAAtJAux9XORyFibH6bZSjMFI2hhFgGxwzAoGh1S0R+nOR3Ak5HsNALzHvjJWD95HPh1jf2vYJdpeAj78 \
    JWT_EXPIRATION_TIME=1440 \
    JWT_REFRESH_EXPIRATION_TIME=10080 \
    CLOUD_DO_ENDPOINT=blr1.digitaloceanspaces.com \
    CLOUD_DO_CREDENTIALS_ACCESS_KEY=DO00D4KGVL6VBR6G9HEU \
    CLOUD_DO_CREDENTIALS_SECRET_KEY=0sHZQ7/PylohHtqgJJCQsc3lqHXZpgMbUFCBzq7aF68 \
    CLOUD_DO_REGION_NAME=blr1 \
    CLOUD_DO_BUCKET_NAME=natbounappspaces \
    CLOUD_DO_CDN_ENDPOINT=https://natbounappspaces.blr1.cdn.digitaloceanspaces.com \
    SPRING_SERVLET_MULTIPART_ENABLED=true \
    SPRING_SERVLET_MULTIPART_MAX_FILE_SIZE=500MB \
    SPRING_SERVLET_MULTIPART_MAX_REQUEST_SIZE=500MB

# Run the Spring Boot application
CMD ["java", "-jar", "/app/shwe.jar"]
