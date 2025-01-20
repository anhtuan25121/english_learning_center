## Sử dụng image OpenJDK cho Java
#FROM openjdk:17-jdk-slim
#
## Thiết lập thư mục làm việc
#WORKDIR /app
#
## Copy file JAR của ứng dụng vào container
#COPY target/english_learning_center-0.0.1-SNAPSHOT.jar app.jar
#
## Mở cổng mặc định (nếu ứng dụng chạy ở port 8080)
#EXPOSE 8080
#
## Lệnh để chạy ứng dụng
#ENTRYPOINT ["java", "-jar", "app.jar"]
#
## Sử dụng image Maven để build
#FROM maven:3.8.5-openjdk-17 AS build
#
## Copy mã nguồn vào container
#COPY . /app
#WORKDIR /app
#
## Build ứng dụng
#RUN mvn clean package -DskipTests
#
## Sử dụng image OpenJDK để chạy ứng dụng
#FROM openjdk:17-jdk-slim
#WORKDIR /app
#
## Copy file JAR từ giai đoạn build
#COPY --from=build /app/target/english_learning_center-0.0.1-SNAPSHOT.jar app.jar
#
## Mở cổng
#EXPOSE 8080
#
## Chạy ứng dụng
#ENTRYPOINT ["java", "-jar", "app.jar"]
#
#RUN mvn validate && mvn clean package -DskipTests
#


# Build Stage
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

# Copy project files
COPY . /app

# Build the application
RUN mvn clean package -DskipTests

# Runtime Stage
FROM openjdk:17-jdk-slim AS stage-2
WORKDIR /app

# Copy the built JAR file from build stage
COPY --from=build /app/target/english_learning_center-0.0.1-SNAPSHOT.jar app.jar

# Set the entrypoint
ENTRYPOINT ["java", "-jar", "app.jar"]
