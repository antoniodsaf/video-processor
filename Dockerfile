FROM amazoncorretto:21-alpine-jdk

RUN apk update 

WORKDIR /app

RUN apk update && \
    apk add --no-cache curl && \
    apk add --no-cache ffmpeg

COPY build/libs/video-processor.jar video-processor.jar

EXPOSE 8085

ENTRYPOINT ["java", "-jar", "video-processor.jar"]