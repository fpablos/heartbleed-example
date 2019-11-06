#!/bin/bash

# Start the first process
apache2 -k start
status=$?
if [ $status -ne 0 ]; then
  echo "Failed to start apache: $status"
  exit $status
fi

# Start the second process
java -jar /var/app/target/que-me-pongo-0.0.1-SNAPSHOT.jar
status=$?
if [ $status -ne 0 ]; then
  echo "Failed to start app: $status"
  exit $status
fi