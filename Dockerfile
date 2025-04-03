# Используем официальный JDK 17
FROM eclipse-temurin:17-jdk

# Устанавливаем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем собранный jar-файл в контейнер
COPY target/TestTask-0.0.1-SNAPSHOT.jar app.jar

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "app.jar"]
