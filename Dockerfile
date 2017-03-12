# CHue depends on JavaFX, so we cannot depend on wisvch/alpine-java
FROM wisvch/debian:stretch
RUN apt-get update && \
    apt-get install -y --no-install-recommends openjdk-8-jre openjfx && \
    rm -rf /var/lib/apt/lists/*
ADD build/libs/chue.jar /srv/chue.jar
WORKDIR /srv
CMD "/srv/chue.jar"
