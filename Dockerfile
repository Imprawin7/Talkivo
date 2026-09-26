FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

COPY backend/pom.xml ./backend/pom.xml
COPY backend/src ./backend/src

WORKDIR /app/backend

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-jammy

ENV DEBIAN_FRONTEND=noninteractive

RUN apt-get update && \
    apt-get install -y python3 python3-pip && \
    rm -rf /var/lib/apt/lists/*

RUN pip3 install --no-cache-dir piper-tts==1.8.0

WORKDIR /app

COPY --from=builder /app/backend/target/talkivo-0.0.1-SNAPSHOT.jar /app/talkivo.jar
COPY piper/models /app/piper/models

RUN mkdir -p /app/generated-audio

ENV APP_AUDIO_STORAGE_DIR=/app/generated-audio
ENV PIPER_PYTHON_PATH=python3
ENV PIPER_MODELS_DIR=/app/piper/models

EXPOSE 10000

CMD ["java", "-jar", "/app/talkivo.jar"]
