import json

for i in range(1,12):

    # some JSON data
    source = {
        "name": "jdbc-source-"+str(i),
        "config": {
            "connector.class": "io.confluent.connect.jdbc.JdbcSourceConnector",
            "connection.url": "jdbc:postgresql://database:5432/project3",
            "connection.user": "postgres",
            "connection.password": "My01pass",
            "dialec.name": "PostgreSqlDatabaseDialect",
            "mode": "bulk",
            "query": "SELECT * FROM requirement"+str(i)+"Table;",
            "poll.interval.ms": "60000",
            "topic.prefix": "requirement"+str(i)+"Info"
        }
    }

    # write the data to a file
    with open("source-"+str(i)+".json", "w") as f:
        json.dump(source, f, indent=4, separators=(",", ": "))