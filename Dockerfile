FROM amazoncorretto:21-alpine-jdk

RUN apk update 

WORKDIR /app

RUN apk update && \
    apk add --no-cache \
        curl \
        ffmpeg \
        ffmpeg-libavcodec \
        ffmpeg-libavformat \
        ffmpeg-libavutil \
        ffmpeg-libswscale \
        ffmpeg-libavfilter \
        ffmpeg-libpostproc \
        ffmpeg-libass \
        libtheora \
        libvorbis \
        libvpx \
        x264-dev \
        x265-dev \
        opus \
        lame \
        openssl

COPY build/libs/video-processor.jar video-processor.jar

EXPOSE 8085

ENTRYPOINT ["java", "-jar", "video-processor.jar"]