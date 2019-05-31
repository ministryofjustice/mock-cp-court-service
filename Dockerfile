FROM java

ENV USER probation
ENV GROUP probation

WORKDIR /app

RUN groupadd -r ${GROUP} && \
    useradd -r -g ${GROUP} ${USER} -d /app && \
    mkdir -p /app && \
    chown -R ${USER}:${GROUP} /app

COPY build/libs/mock-cp-court-service-*.jar /app/mock-cp-court-service.jar

USER ${USER}

ENTRYPOINT ["/usr/bin/java", "-jar", "/app/mock-cp-court-service.jar"]
