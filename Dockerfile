FROM eclipse-temurin:21-jdk
#FROM gradle:8.3.0-jdk20

ARG GRADLE_VERSION=8.7

RUN apt-get update && apt-get install -yq make unzip

RUN wget -q https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip \
    && unzip gradle-${GRADLE_VERSION}-bin.zip \
    && rm gradle-${GRADLE_VERSION}-bin.zip

ENV GRADLE_HOME=/opt/gradle

RUN mv gradle-${GRADLE_VERSION} ${GRADLE_HOME}

ENV PATH=$PATH:$GRADLE_HOME/bin

WORKDIR /

#COPY gradle gradle
#COPY build.gradle.kts .
#COPY settings.gradle.kts .
#COPY gradlew .

#RUN ./gradlew --no-daemon dependencies

COPY . .
#COPY config config

#RUN ./gradlew --no-daemon build
RUN gradle installDist

#ENV JAVA_OPTS "-Xmx512M -Xms512M"
# EXPOSE 7070

#CMD java -jar build/libs/app-1.0-SNAPSHOT.jar
CMD ./build/install/app/bin/app
