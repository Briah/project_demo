# schema-manager

This microservice is responsible for creating keyspaces and tables in cassandra on kubernetes

## Pre-requisites

The below build steps assume you have:
* setup docker 
* k8ssandra up and running (k8ssandra folder for details)
* sql/keyspace.conf: containing your keyspace
* sql/*.sql tables: ready under sql directory to be executed against your k8ssandra instance

## Build Steps

**Note**

All the steps will be deployed in the default namespace feel free to add a namespace using -n \<namespace\> to all of the commands below if you don't want on the default namespace


1. Build the schema-manager docker image:

```
docker image build -t schema-manager .
```

2. update your dbcred.conf file with the needed creds to access the DB and execute it on kubernetes

```
kubectl create secret generic dbcred --from-env-file=dbcred.conf
```

3. create the role binding 

To enable schema-manager job to create a pod that executes the keyspace and tables creation use the following command:

```
kubectl create -f role.yaml
```

4. Run the schema manager
```
kubectl create -f schema-deploy.yaml
```

**Note**

To destroy schema-manager and redeploy run this
```
kubectl delete -f schema-deploy.yaml
kubectl create -f schema-deploy.yaml
```


