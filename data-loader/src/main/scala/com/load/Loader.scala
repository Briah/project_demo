package com.rihab.load

import scala.util.Try
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._
import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import com.datastax.spark.connector._
import org.apache.spark.SparkContext

import scala.collection.mutable.LinkedHashMap

object Loader {

	def main(args: Array[String]): Unit = {
    val hostname = sys.env.getOrElse("DB_HOST", "k8ssandra-database")
    val port = sys.env.getOrElse("DB_PORT", "9042")
    val spark = Utils.getSparkSession(hostname, port)
    
    spark.sparkContext.setLogLevel("ERROR")
    val bucket = "s3a://rihab-test-bucket"

    val filename = sys.env.getOrElse("FILENAME", "store.csv")
    val sep = ","
    val schema = None
    val table_name = sys.env.getOrElse("TABLE_NAME", "store")
    val keyspace = sys.env.getOrElse("KEYSPACE", "test")

    val input_data = Utils.readCSV(spark, filename, sep, bucket)
    input_data.show()
    println(s"**** writing data to table ${table_name} under keyspace ${keyspace} **** ")
    
    Utils.writeToCassandraHelper(input_data, table_name, keyspace)
    println("successfully wrote data to cassandra from s3")


    println("reading from cassandra")
    Utils.readFromCassandra(spark, table_name, keyspace).show(2)
}
}