FROM eclipse-temurin:21-jre-noble

ENV CATALINA_HOME /usr/local/tomcat
ENV PATH $CATALINA_HOME/bin:$PATH
RUN mkdir -p "$CATALINA_HOME"
WORKDIR $CATALINA_HOME

ENV TOMCAT_NATIVE_LIBDIR $CATALINA_HOME/native-jni-lib
ENV LD_LIBRARY_PATH ${LD_LIBRARY_PATH:+$LD_LIBRARY_PATH:}$TOMCAT_NATIVE_LIBDIR

ENV TOMCAT_MAJOR 10
ENV TOMCAT_VERSION 10.1.34
ENV TOMCAT_SHA512 e29c4d0e26295d64dfeee399e7719b5baac2682ac0e7b53702f26d630fea761f93ddf60674dfe87c41ddd9b79d4938a6a533a280bb3d7fb83c2a1b7cd5da6b09

COPY --from=tomcat:10.1.34-jdk21-temurin-noble $CATALINA_HOME $CATALINA_HOME

RUN set -eux; \
    apt-get update; \
    xargs -rt apt-get install -y --no-install-recommends < "$TOMCAT_NATIVE_LIBDIR/.dependencies.txt"; \
    rm -rf /var/lib/apt/lists/*

RUN set -eux; \
    nativeLines="$(catalina.sh configtest 2>&1)"; \
    nativeLines="$(echo "$nativeLines" | grep 'Apache Tomcat Native')"; \
    nativeLines="$(echo "$nativeLines" | sort -u)"; \
    if ! echo "$nativeLines" | grep -E 'INFO: Loaded( APR based)? Apache Tomcat Native library' >&2; then \
        echo >&2 "$nativeLines"; \
        exit 1; \
    fi

RUN sed -i 's/8080/8081/' $CATALINA_HOME/conf/server.xml

COPY target/user-service-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8081

ENTRYPOINT []
CMD ["catalina.sh", "run"]
