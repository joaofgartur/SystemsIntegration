#!/bin/bash

#curl -X POST -H "Accept:application/json" -H "Content-Type:application/json" http://localhost:8083/connectors/ -d @sink.json

#curl -X POST -H "Accept:application/json" -H "Content-Type:application/json" http://localhost:8083/connectors/ -d @source.json

### DELETES ###

curl -X DELETE -H "Accept:application/json" -H "Content-Type:application/json" http://localhost:8083/connectors/jdbc-source
curl -X DELETE -H "Accept:application/json" -H "Content-Type:application/json" http://localhost:8083/connectors/jdbc-sink