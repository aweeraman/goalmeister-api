#!/bin/bash
if [ "$1" == "" ]; then
  config_file="config.yaml"
else
  config_file=$1
  if [ ! -e $config_file ]; then
    echo "Error reading $config_file"
  fi
fi
dbg_line=`grep "debug:" $config_file`
dbg_value=`echo $dbg_line | cut -d":" -f2`
if [[ "$dbg_value" =~ "true" ]]; then
  echo "Attach your debugger to port 8998"
  CMD_LINE="java -Xdebug -Xrunjdwp:transport=dt_socket,address=8998,server=y -jar target/goalmeister-api-*.jar $config_file"
else
  CMD_LINE="java -jar target/goalmeister-api-*.jar $config_file"
fi
bash -c "$CMD_LINE"
