import json

for i in range(1,12):

    # some JSON data
    sink = {
	"name": "jdbc-sink-"+str(i),
	"config": {
		"connection.url": "jdbc:postgresql://database:5432/project3?user=postgres&password=My01pass",
		"connector.class": "io.confluent.connect.jdbc.JdbcSinkConnector",
		"dialec.name": "PostgreSqlDatabaseDialect",
		"tasks.max": "1",
		"topics": "results-"+str(i),
		"table.name.format": "requirement"+str(i)+"Table",
		"auto.create": "true",
		"key.converter": "org.apache.kafka.connect.storage.StringConverter",
		"value.converter": "org.apache.kafka.connect.json.JsonConverter",
		"key.converter.schemas.enable": "true",
		"value.converter.schemas.enable": "true"
	}
}

    # write the data to a file
    with open("sink-"+str(i)+".json", "w") as f:
        json.dump(sink, f, indent=4, separators=(",", ": "))