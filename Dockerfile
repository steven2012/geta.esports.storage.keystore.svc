# Alpine for core-service-1.0-local-SNAPSHOT
# ./gradlew clean build
# Build image with:  docker build -t dokerdemo.azurecr.io/geta-sport-storage .
# RUN CONTAINER: docker run -d -p 8080:3000 dokerdemo.azurecr.io/geta-sport-storage

# Use a parent image
FROM openjdk:8-jdk-slim
#FROM 10.25.10.193:8090/centos8_jdk8:v1
WORKDIR /opt/geta/games/services/geta-sport-storage
ADD build/libs /opt/geta/games/services/geta-sport-storage/
ADD src/main/resources  /opt/geta/games/services/geta-sport-storage/resources/

### Set Environment
#ENV SERVER_HOME /opt/geta/games/services/geta-sport-storage

# Make internal port available to the world outside this container
EXPOSE 3000

#permisos de carpeta
RUN chmod -R 777 /opt/geta

ENV TZ=America/Bogota
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

ENV JAVA_OPTS="-Xmx2g"

### Start instance
ENTRYPOINT ["java","-jar", "-Dspring.profiles.active=dev", "-Dfile.encoding=ISO8859_1", "/opt/geta/games/services/geta-sport-storage/geta.esport.storage.keystore.svc-1.0-SNAPSHOT.jar"]

# Run app when the container launches
#CMD ["java", "-jar", "geta.esport.storage.keystore.svc-1.0-SNAPSHOT.jar"]