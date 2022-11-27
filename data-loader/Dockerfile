FROM spark-hadoop

ARG spark_jars=jars
RUN set -ex && \
    apk upgrade --no-cache && \
    apk add --no-cache bash tini libc6-compat linux-pam nss && \
    mkdir -p $SPARK_HOME/work-dir && \
    touch $SPARK_HOME/RELEASE && \
    rm /bin/sh && \
    ln -sv /bin/bash /bin/sh && \
    echo "auth required pam_wheel.so use_uid" >> /etc/pam.d/su && \
    chgrp root /etc/passwd && chmod ug+rw /etc/passwd

COPY run.sh /opt/run.sh


# Can be overwritten by mounting volumes to /opt/config
COPY target/scala-2.12/data-loader_2.12-1.0.jar /opt/target/scala-2.12/
# COPY data/ /opt/data

RUN chmod 777 /opt/run.sh
RUN cp $SPARK_HOME/kubernetes/dockerfiles/spark/entrypoint.sh /opt/

ADD https://repo1.maven.org/maven2/com/amazonaws/aws-java-sdk-bundle/1.11.563/aws-java-sdk-bundle-1.11.563.jar $SPARK_HOME/jars/cache/com.amazonaws/aws-java-sdk-bundle/jars/
ADD https://repo1.maven.org/maven2/org/apache/hadoop/hadoop-aws/3.2.2/hadoop-aws-3.2.2.jar $SPARK_HOME/jars/cache/org.apache.hadoop/hadoop-aws/jars/

ADD https://repo1.maven.org/maven2/com/amazonaws/aws-java-sdk/1.11.563/aws-java-sdk-1.11.563.jar $SPARK_HOME/jars

RUN sed -i 's/\/usr\/bin\/tini/\/sbin\/tini/1' /opt/entrypoint.sh

WORKDIR /opt
ENTRYPOINT ["/opt/entrypoint.sh"]
#CMD ["sh", "run.sh", "local"]
