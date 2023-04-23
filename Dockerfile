FROM openjdk:17-alpine

EXPOSE 8080

WORKDIR /usr/src/app                          

ARG JAR_PATH=/build/libs

VOLUME [ "/var/log" ]

COPY ${JAR_PATH}/server-0.0.1-SNAPSHOT.jar app.jar

# 도커 실행 명령어
CMD ["java","-jar","-Dspring.profiles.active=prod","/usr/src/app/app.jar"]