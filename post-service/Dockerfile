FROM tomcat:10.1.34-jdk21

WORKDIR /usr/local/tomcat

# Меняем порт на 8082
RUN sed -i 's/8080/8082/' conf/server.xml

# Копируем WAR-файл
COPY build/libs/post-service-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8082

CMD ["catalina.sh", "run"]
