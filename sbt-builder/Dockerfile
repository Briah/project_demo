FROM openjdk:11.0.13-jre

# Env variables
ENV SCALA_VERSION 2.12.7
ENV SBT_VERSION 1.6.1

# Install Scala
## Piping curl directly in tar
RUN \
  curl -fsL https://downloads.typesafe.com/scala/$SCALA_VERSION/scala-$SCALA_VERSION.tgz | tar xfz - -C /root/ && \
  echo >> /root/.bashrc && \
  echo "export PATH=~/scala-$SCALA_VERSION/bin:$PATH" >> /root/.bashrc

# Install sbt
RUN \
  curl -L -o sbt.deb https://repo.scala-sbt.org/scalasbt/debian/sbt-$SBT_VERSION.deb && \
  dpkg -i sbt.deb && \
  rm sbt.deb && \
  apt-get update && \
  apt-get install sbt

RUN mkdir /root/.ivy2/
RUN mkdir -p /root/coursier/v1

COPY sbt.sh /root/

# Define working directory
WORKDIR /root

ENTRYPOINT ["sh","/root/sbt.sh"]
CMD ["sbt", "sbtVersion"]
