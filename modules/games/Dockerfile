FROM openjdk:8 AS GAMES_BUILD_IMAGE

ENV APP_HOME=/root/dev/games/
COPY gradle $APP_HOME/gradle
COPY build.gradle gradlew $APP_HOME

# Enforce loading dependencies first
RUN mkdir -p $APP_HOME/src/main/java/hello
RUN mkdir -p $APP_HOME/src/test/java/hello
COPY dummy/Hello.java $APP_HOME/src/main/java/hello
COPY dummy/HelloTest.java $APP_HOME/src/test/java/hello
WORKDIR $APP_HOME
RUN ./gradlew --no-daemon build -x :bootJar

# Copy sources and build
COPY src $APP_HOME/src
RUN ./gradlew --no-daemon build

FROM openjdk:8-jre
WORKDIR /root/
COPY --from=GAMES_BUILD_IMAGE /root/dev/games/build/libs/games.jar .
EXPOSE 8080
CMD ["java","-jar","games.jar"]

