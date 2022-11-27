from cassandra.cluster import Cluster
from cassandra.auth import PlainTextAuthProvider
from ssl import CERT_REQUIRED, PROTOCOL_TLSv1_2
import os

def clusterConnect():
    """Connect to the cassandra cluster

    Returns:
        [CassandraSession]: [Connection established]
    """

    DB_HOST=os.getenv('DB_HOST', 'localhost')
    DB_PORT=int(os.getenv("DB_PORT", "9092"))
    DB_USER=os.getenv("DB_USER", "")
    DB_PASS=os.getenv("DB_PASS", "")
    ssl_opts = None
    print("Schema Manager running")
    auth_provider = PlainTextAuthProvider(username=DB_USER, password=DB_PASS)
    cluster = Cluster([DB_HOST], port=DB_PORT, auth_provider=auth_provider, ssl_options=ssl_opts)
    session = cluster.connect()
    session.default_timeout = 120
    return session


def createKeyspaceAndTables(session):
    print("[INFO] creating keyspace from file sql/keyspace.sql")
    keyspace = open("/sql/keyspace.conf").read()
    query="CREATE KEYSPACE IF NOT EXISTS " + keyspace + " WITH replication = {'class': 'SimpleStrategy','replication_factor': 1 };"
    session.execute(query)
    print(f"[INFO] keyspace {keyspace} created successfully!")
    files = os.listdir("/sql")
    files.remove("keyspace.conf")
    for file in files:
        print(f"[INFO] creating table from {file}")
        qu = open("/sql/" + file).read()
        session.set_keyspace(keyspace)
        session.execute(qu)
        print(f"[INFO] table {file} created successfully")


def main():
    session = clusterConnect()
    createKeyspaceAndTables(session)

if __name__ == "__main__":
    main()





