FROM wisvch/openjdk:8-jdk AS builder
RUN apt-get update && \
    apt-get install -y --no-install-recommends openjfx
COPY . /src
WORKDIR /src
ARG BUILD_NUMBER
RUN ./gradlew build -PbuildNumber=$BUILD_NUMBER

FROM wisvch/spring-boot-base:1
RUN apt-get update && \
    apt-get install -y --no-install-recommends openjfx && \
    rm -rf /var/lib/apt/lists/*
COPY --from=builder /src/build/libs/chue.jar /srv/chue.jar
USER spring-boot
CMD ["/srv/chue.jar"]
