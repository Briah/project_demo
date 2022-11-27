# spark-streams


This project is meant to create a streaming example using spark streaming and kafka streaming. The code should have the code for a kafka producer a kafka consumer and a spark streamer to consume the kakfa produced streams


## Build Steps

1. Start by packaging the application using the following command

```
docker-compose -f build.yml run --rm sbt sbt package
```

2. Build the spark-stream image using the following command

```
docker build -t spark-stream .
```

## Deploymment Steps

1. Start the spark streamer consumer in the kafka namespace (check the kafka folder for the kafka broker and zookeeper deployment):

** Note** 

The consumer will start receving data from the producer as soon as you deploy it, it will also write records directly to cassandra so make sure you have your k8ssandra instance installed and the dbcred secret created (check k8ssandra repo for cassandra deplpyment and schema-manager or data-loader for the dbcred secret)

```
kubectl create -f producer.yaml -n kafka
```

2. Start the kafka producer which will start producing random records:

```
kubectl create -f producer.yaml -n kafka
```

3. Try now to query your table using the DBeaver (installation steps in the readme from the this repo's source)