#!/bin/sh
set -eu
mkdir -p /opt/tns
if [ -d /wallet ]; then
  cp -a /wallet/. /opt/tns/
  printf '%s\n' 'WALLET_LOCATION = (SOURCE = (METHOD = file) (METHOD_DATA = (DIRECTORY="/opt/tns")))' 'SSL_SERVER_DN_MATCH=yes' > /opt/tns/sqlnet.ora
fi
exec java -jar /app/app.jar
