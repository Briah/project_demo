echo "creating keyspace from file sql/keyspace.sql"
USER=$USER
PASS=$PASS
export cqlsh="cqlsh -u $USER -p $PASS"
keyspace=$(cat /sql/keyspace.conf)
query="CREATE KEYSPACE IF NOT EXISTS $keyspace WITH replication = {'class': 'SimpleStrategy','replication_factor': 1 };"
$cqlsh -e "query"

for file in $(ls /sql | grep -v keyspace) do
	echo "creating table from" $file
	cqlsh -f sql/$file -k keyspace
done
