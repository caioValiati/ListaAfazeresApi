# 1. Etapa de build
FROM eclipse-temurin:24-jdk as build

# Diretório de trabalho para o build
WORKDIR /app

# Copia o arquivo de dependências primeiro e faz download delas (cache eficiente)
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
RUN ./mvnw dependency:go-offline

# Copia o restante do código
COPY src src

# Compila o projeto e gera o .jar
RUN ./mvnw clean package -DskipTests

# 2. Etapa de execução
FROM eclipse-temurin:21-jdk

# Diretório de trabalho para rodar a aplicação
WORKDIR /app

# Copia apenas o JAR gerado da etapa de build
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta
EXPOSE 8080

# Comando para rodar a aplicação
CMD ["java", "-jar", "app.jar"]
