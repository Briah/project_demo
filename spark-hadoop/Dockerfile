FROM openjdk:8u201-alpine

ENV DOWNLOAD_FILE spark-3.3.1-bin-hadoop3

ADD http://archive.apache.org/dist/spark/spark-3.3.1/${DOWNLOAD_FILE}.tgz /opt/${DOWNLOAD_FILE}.tgz

RUN mkdir /opt/${DOWNLOAD_FILE}
RUN tar vfx /opt/${DOWNLOAD_FILE}.tgz -C /opt/${DOWNLOAD_FILE} --strip-components 1 && rm /opt/${DOWNLOAD_FILE}.tgz

RUN apk update && apk add --no-cache bash libc6-compat

ADD https://repo1.maven.org/maven2/com/amazonaws/aws-java-sdk-bundle/1.11.563/aws-java-sdk-bundle-1.11.563.jar ${DOWNLOAD_FILE}/jars
ADD https://repo1.maven.org/maven2/org/apache/hadoop/hadoop-aws/3.2.2/hadoop-aws-3.2.2.jar ${DOWNLOAD_FILE}/jars

RUN ln -s /lib64/ld-linux-x86-64.so.2 /lib/ld-linux-x86-64.so.2

ENV SPARK_HOME /opt/${DOWNLOAD_FILE}
ENV PATH="/opt/${DOWNLOAD_FILE}/bin:${PATH}"
