FROM openjdk:11 AS GAMES_BUILD_IMAGE

ENV APP_HOME=/root/dev/games/
COPY gradle $APP_HOME/gradle
COPY build.gradle gradlew $APP_HOME

# Copy sources and build
WORKDIR $APP_HOME
COPY src $APP_HOME/src
RUN ./gradlew --no-daemon build

FROM openjdk:11-jre
WORKDIR /root/
COPY --from=GAMES_BUILD_IMAGE /root/dev/games/build/libs/games.jar .
EXPOSE 8080
CMD ["java","-jar","games.jar"]
