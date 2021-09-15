FROM adoptopenjdk/openjdk11:jdk-11.0.10_9-alpine
MAINTAINER gyorik@sch.bme.hu
COPY target/schreddit.jar /opt/schreddit/
WORKDIR /opt/schreddit
ENTRYPOINT ["java", "-Dspring.profiles.include=docker", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=98", "-jar", "/opt/schreddit/schreddit.jar"]
EXPOSE 80