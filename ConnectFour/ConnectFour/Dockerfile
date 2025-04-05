# ---------- Build Stage ----------
FROM maven:3.8.8-eclipse-temurin-17 AS build

WORKDIR /app
COPY . .
RUN mvn clean package

# ---------- Runtime Stage ----------
FROM eclipse-temurin:17-jdk

RUN apt-get update && \
    apt-get install -y wget unzip libgtk-3-0 libgl1 libx11-xcb1 && \
    wget https://download2.gluonhq.com/openjfx/18/openjfx-18_linux-x64_bin-sdk.zip && \
    unzip openjfx-18_linux-x64_bin-sdk.zip -d /opt && \
    rm openjfx-18_linux-x64_bin-sdk.zip

WORKDIR /app
COPY --from=build /app/target/ConnectFour-1.0-SNAPSHOT.jar ./app.jar

CMD ["java", "--module-path", "/opt/javafx-sdk-18/lib", "--add-modules", "javafx.controls,javafx.fxml", "-jar", "app.jar"]
