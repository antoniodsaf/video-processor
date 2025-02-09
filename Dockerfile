FROM eclipse-temurin:21-jre


WORKDIR /app

RUN apt-get update
RUN apt-get install -y ffmpeg

COPY build/libs/video-processor.jar video-processor.jar

EXPOSE 8085

ENTRYPOINT ["java", "-jar", "video-processor.jar"]