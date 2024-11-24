FROM openjdk:17-jdk

WORKDIR /spring-boot

COPY build/libs/bookmarky-0.0.1.jar bookmarky-0.0.1.jar

ENTRYPOINT ["java", "-jar", "/spring-boot/bookmarky-0.0.1.jar"]