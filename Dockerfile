FROM java

RUN addgroup --gid 2000 --system appgroup && \
    adduser --uid 2000 --system appuser --gid 2000

RUN mkdir -p /app
WORKDIR /app

COPY build/libs/mock-cp-court-service-*.jar /app/mock-cp-court-service.jar

RUN chown -R appuser:appgroup /app

USER 2000

ENTRYPOINT ["/usr/bin/java", "-jar", "/app/mock-cp-court-service.jar"]
