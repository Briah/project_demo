#!/bin/bash

"${SPARK_HOME}"/bin/spark-submit --packages com.datastax.spark:spark-cassandra-connector_2.12:3.2.0 --class "$1" opt/app/target/spark-stream_2.12-1.0.jar 
