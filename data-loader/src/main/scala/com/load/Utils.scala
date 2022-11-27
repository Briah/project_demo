package com.rihab.load

import org.apache.spark.sql._
import org.apache.spark.sql.functions._
import org.apache.spark.SparkConf
import org.apache.spark.sql.types._
import org.apache.spark.sql.cassandra._
//Spark connector
import com.datastax.spark.connector._
import com.datastax.spark.connector.cql.CassandraConnector


object Utils {

  def getSparkSession(hostname: String, port: String): SparkSession = {
    var conf: SparkConf = new SparkConf(true)
      .setAppName("rihab-test")
      .set("spark.cassandra.connection.host", hostname)
      .set("spark.cassandra.connection.port", port)
      .set("spark.cassandra.auth.username", sys.env.getOrElse("DB_USER", ""))
      .set("spark.cassandra.auth.password", sys.env.getOrElse("DB_PASS", ""))


    var spark = SparkSession
      .builder()
      .config(conf)
      .getOrCreate()
      
    // configure s3 filesystem
    spark.sparkContext.hadoopConfiguration.set("fs.s3a.access.key",  sys.env.getOrElse("AWS_KEY", ""))
    spark.sparkContext.hadoopConfiguration.set("fs.s3a.aws.credentials.provider", "org.apache.hadoop.fs.s3a.SimpleAWSCredentialsProvider")
    spark.sparkContext.hadoopConfiguration.set("fs.s3a.secret.key", sys.env.getOrElse("AWS_SECRET", ""))
    spark.sparkContext.hadoopConfiguration.set("fs.s3a.endpoint", "s3.eu-west-1.amazonaws.com")
    spark.sparkContext.hadoopConfiguration.set("fs.s3.impl","org.apache.hadoop.fs.s3.S3FileSystem")
    spark.sparkContext.hadoopConfiguration.set("fs.s3.impl","org.apache.hadoop.fs.s3native.NativeS3FileSystem")

    spark

  }

// reading a csv file 
  def readCSV(spark: SparkSession,
              filename: String,
              sep: String,
              path: String
            ): DataFrame = {

    val data: DataFrame = spark.read.format("csv")
        .option("header", "true")
        .option("delimiter", sep)
        .load((path + "/" + filename).toString)
    data
  }


  // reading data from cassandra
  def readFromCassandra(
                        spark: SparkSession,
                        table_name: String,
                        keyspace: String
                      ): DataFrame = {
    spark.read
      .format("org.apache.spark.sql.cassandra")
      .options(Map("table" -> table_name,
        "keyspace" -> keyspace))
      .load()
  }

  // writing to cassandra
  def writeToCassandraHelper(
                              data: DataFrame,
                              table_name: String,
                              keyspace: String
                            ): Unit = {
    data.write
      .format("org.apache.spark.sql.cassandra")
      .options(Map("table" -> table_name,
        "keyspace" -> keyspace))
      .mode("append")
      .save()
  }

}
