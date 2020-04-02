FROM adoptopenjdk/openjdk11:jre-11.0.6_10-alpine

RUN addgroup -S app && adduser -S app -G app
USER app
VOLUME /tmp
ARG DEPENDENCY=target/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","design.urlshortening.UrlShorteningApplication"]