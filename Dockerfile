FROM gradle:jdk11 as builder
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

FROM openjdk:11-jre-slim
COPY --from=builder /home/gradle/src/build/libs/*.jar /app/discord-bot.jar
WORKDIR /app

ENV DISCORD_BOT_TOKEN=""

# Run bot
CMD ["java", "-jar", "discord-bot.jar"]
