#!/bin/bash

# curl -X POST -H "Accept:application/json" -H "Content-Type:application/json" http://localhost:8083/connectors/ -d @sink-1.json
# curl -X POST -H "Accept:application/json" -H "Content-Type:application/json" http://localhost:8083/connectors/ -d @sink-2.json
# curl -X POST -H "Accept:application/json" -H "Content-Type:application/json" http://localhost:8083/connectors/ -d @sink-3.json
# curl -X POST -H "Accept:application/json" -H "Content-Type:application/json" http://localhost:8083/connectors/ -d @sink-4.json
# curl -X POST -H "Accept:application/json" -H "Content-Type:application/json" http://localhost:8083/connectors/ -d @sink-5.json
# curl -X POST -H "Accept:application/json" -H "Content-Type:application/json" http://localhost:8083/connectors/ -d @sink-6.json
# curl -X POST -H "Accept:application/json" -H "Content-Type:application/json" http://localhost:8083/connectors/ -d @sink-7.json
# curl -X POST -H "Accept:application/json" -H "Content-Type:application/json" http://localhost:8083/connectors/ -d @sink-8.json
# curl -X POST -H "Accept:application/json" -H "Content-Type:application/json" http://localhost:8083/connectors/ -d @sink-9.json
# curl -X POST -H "Accept:application/json" -H "Content-Type:application/json" http://localhost:8083/connectors/ -d @sink-10.json
# curl -X POST -H "Accept:application/json" -H "Content-Type:application/json" http://localhost:8083/connectors/ -d @sink-11.json


curl -X POST -H "Accept:application/json" -H "Content-Type:application/json" http://localhost:8083/connectors/ -d @source-location.json
curl -X POST -H "Accept:application/json" -H "Content-Type:application/json" http://localhost:8083/connectors/ -d @source-station.json

### DELETES ###
# Sinks

# curl -X DELETE -H "Accept:application/json" -H "Content-Type:application/json" http://localhost:8083/connectors/jdbc-sink-1
# curl -X DELETE -H "Accept:application/json" -H "Content-Type:application/json" http://localhost:8083/connectors/jdbc-sink-2
# curl -X DELETE -H "Accept:application/json" -H "Content-Type:application/json" http://localhost:8083/connectors/jdbc-sink-3
# curl -X DELETE -H "Accept:application/json" -H "Content-Type:application/json" http://localhost:8083/connectors/jdbc-sink-4
# curl -X DELETE -H "Accept:application/json" -H "Content-Type:application/json" http://localhost:8083/connectors/jdbc-sink-5
# curl -X DELETE -H "Accept:application/json" -H "Content-Type:application/json" http://localhost:8083/connectors/jdbc-sink-6
# curl -X DELETE -H "Accept:application/json" -H "Content-Type:application/json" http://localhost:8083/connectors/jdbc-sink-7
# curl -X DELETE -H "Accept:application/json" -H "Content-Type:application/json" http://localhost:8083/connectors/jdbc-sink-8
# curl -X DELETE -H "Accept:application/json" -H "Content-Type:application/json" http://localhost:8083/connectors/jdbc-sink-9
# curl -X DELETE -H "Accept:application/json" -H "Content-Type:application/json" http://localhost:8083/connectors/jdbc-sink-10
# curl -X DELETE -H "Accept:application/json" -H "Content-Type:application/json" http://localhost:8083/connectors/jdbc-sink-11

# Sources
# curl -X DELETE -H "Accept:application/json" -H "Content-Type:application/json" http://localhost:8083/connectors/jdbc-source-location
# curl -X DELETE -H "Accept:application/json" -H "Content-Type:application/json" http://localhost:8083/connectors/jdbc-source-station

