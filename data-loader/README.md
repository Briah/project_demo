# data-loader

The data loader is a job that loads data from s3 into k8ssandra on the local k8s cluster, and changing it to a cloud hosted k8s cluster is not a big change per se as it's all controlled by the yaml file and the spark_master_url to apply.


I've kept a few hardcodings here just for the sake of this example that will get back and fix later.

# Pre-requisites

In order to be able to run this make sure you already:
* Built the sbt-builder image (under sbt-builder folder)
* Deployed k8ssandra (check k8ssandra folder)
* Create an s3 bucket and fixed the needed roles to access it (dummy one for testing)
* Built the spark-hadoop image that contains all spark commands and deps needed to submit and run on the cluster (folder spark-hadoop)
* Ran schema manager and got the tables ready for the load


# Build Steps

**Note**

 I'll be creating the loader under the default namespace if needed make sure to specify -n \<namespace\>


1. package the jar

The folowing command will help you build a "FAT" jar (note the use of assembly) that can be subimitted to cluster to execute your code

```
docker-compose -f build.yml run --rm sbt sbt assembly
```

2. Build the docker image to submit

```
docker image build -t data-loader .
```

3. Create a cluster role and cluster role binding to give spark job and driver the permission to create new pods (if you created this in schema manager ignore this)

Make sure to replace namespace by default or a custom one


```
kubectl create serviceaccount spark --namespace=<namespace>
kubectl create clusterrolebinding spark-role-<namespace> --clusterrole=edit --serviceaccount=<namespace>:spark
```

4. Create the secrets if not already created ( in schema manager)

fill in the needed info in dbcred.conf

```
kubectl create secret generic dbcred --from-env-file=dbcred.conf
```

5. Execute the load:

```
kubectl create -f load.yaml
```

6. Monitor the load and check for errors:

```
kubectl logs -f rihab-dataimport-data-loader-job-driver 
```
