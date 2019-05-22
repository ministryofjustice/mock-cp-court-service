FROM java

COPY build/libs/mock-cp-court-service-*.jar /root/mock-cp-court-service.jar


ENTRYPOINT ["/usr/bin/java", "-jar", "/root/mock-cp-court-service.jar"]
