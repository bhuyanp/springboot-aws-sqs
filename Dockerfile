FROM amazoncorretto:17
ARG JAR_FILE=target/*-aws.jar
COPY ${JAR_FILE} app.jar
ARG ACTIVE_PROFILE=${ACTIVE_PROFILE}
ENV ACTIVE_PROFILE=$ACTIVE_PROFILE
ENTRYPOINT ["sh","-c","java -Dspring.profiles.active=$ACTIVE_PROFILE -jar app.jar"]
##ENTRYPOINT ["java","-jar","app.jar"]
