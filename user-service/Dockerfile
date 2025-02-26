FROM tomcat:10.1.34-jdk21

WORKDIR /usr/local/tomcat

# Меняем порт на 8081
RUN sed -i 's/8080/8081/' conf/server.xml

# Копируем WAR-файл
COPY build/libs/user-service-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8081

CMD ["catalina.sh", "run"]
