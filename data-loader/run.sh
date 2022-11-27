#!/bin/bash

set -e

SPARK_MASTER_URL=${SPARK_MASTER_URL:-local[*]}
DEPLOY_MODE=${DEPLOY_MODE:-client}

# setup extra jars to avoid redownloading these
mkdir -p ${SPARK_HOME}/jars/cache/com.amazonaws/aws-java-sdk-bundle/jars/
mkdir -p ${SPARK_HOME}/jars/cache/org.apache.hadoop/hadoop-aws/jars/
cp ${SPARK_HOME}/jars/aws-java-sdk-bundle-1.11.563.jar ${SPARK_HOME}/jars/cache/com.amazonaws/aws-java-sdk-bundle/jars/
cp ${SPARK_HOME}/jars/hadoop-aws-3.2.2.jar ${SPARK_HOME}/jars/cache/org.apache.hadoop/hadoop-aws/jars/

    # --packages com.datastax.spark:spark-cassandra-connector_2.12:3.2.0 \
${SPARK_HOME}/bin/spark-submit \
    --master $SPARK_MASTER_URL \
    --deploy-mode $DEPLOY_MODE \
    --jars https://repo1.maven.org/maven2/com/datastax/spark/spark-cassandra-connector_2.12/3.2.0/spark-cassandra-connector_2.12-3.2.0.jar \
    --class "com.rihab.load.Loader" \
    --conf spark.kubernetes.container.image=$DOCKER_IMAGE \
    --conf spark.kubernetes.container.image.pullPolicy=IfNotPresent \
    --conf spark.kubernetes.file.upload.path=/tmp \
    --conf spark.kubernetes.driver.pod.name="rihab-dataimport-$JOB_NAME-driver" \
    --conf spark.driver.memory=$DRIVER_MEMORY \
    --conf spark.executor.memory=$EXEC_MEMORY \
    --conf spark.executor.instances=$NUM_EXECS \
    --conf spark.kubernetes.authenticate.driver.serviceAccountName=spark \
    --conf spark.kubernetes.driverEnv.KEYSPACE=$KEYSPACE \
    --conf spark.kubernetes.driverEnv.DB_PASS=$DB_PASS \
    --conf spark.kubernetes.driverEnv.DB_PORT=$DB_PORT \
    --conf spark.kubernetes.driverEnv.DB_HOST=$DB_HOST \
    --conf spark.kubernetes.driverEnv.DB_USER=$DB_USER \
    --conf spark.kubernetes.driverEnv.AWS_KEY=$AWS_KEY \
    --conf spark.kubernetes.driverEnv.AWS_SECRET=$AWS_SECRET \
    --conf spark.driver.extraJavaOptions="-Divy.cache.dir=/tmp -Divy.home=/tmp" \
    --conf spark.jars.ivy="/opt/jars/" \
    local:///opt/target/scala-2.12/data-loader_2.12-1.0.jar