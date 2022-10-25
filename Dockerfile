FROM openjdk:11
COPY target/moneytracker.jar moneytracker.jar
ENTRYPOINT [ "java", "-jar", "/moneytracker.jar" ]