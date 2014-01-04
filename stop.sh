#!/bin/sh
if [ ! -e server.pid ]; then
  echo "server.pid cannot be found"
  exit 1
fi

kill `cat server.pid`
rm server.pid
echo "Server stopped."
