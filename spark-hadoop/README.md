# spark-hadoop

This will build the spark-hadoop image needed to create a loader service to run on k8s and import data into a DB.

This image contains the spark and hadoop dependencies needed to submit work on a cluster (for our example a Kubernetes cluster)

## Build Steps

```
docker build -t spark-hadoop .
```