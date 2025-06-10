# Используем образ с Java 17
FROM eclipse-temurin:17-jdk-alpine

# Указываем рабочую директорию
WORKDIR /app

# Копируем jar-файл
COPY target/stream-processing-0.0.1-SNAPSHOT.jar app.jar

# Порт, который будем слушать
EXPOSE 8080

# Запуск приложения
ENTRYPOINT ["java", "-jar", "app.jar"]
