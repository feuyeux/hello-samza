Docker-Ubuntu-Kafka
----

### 1 Images
> workspace：/home/erichan/docker-room/ubuntu-kafka

> reference: https://github.com/wouterd/docker-zookeeper

#### 1.1 Dockerfile

```
# Version: 0.0.1
FROM feuyeux/ubuntu-java7
MAINTAINER Eric Han "feuyeux@gmail.com"
ADD http://ftp.nluug.nl/internet/apache/kafka/0.8.2.1/kafka_2.10-0.8.2.1.tgz /kafka.tgz
RUN mkdir -p /kafka && \
  cd /kafka && \
  tar xvfz ../kafka.tgz --strip-components=1 && \
  rm /kafka.tgz
ADD start-ka-server.sh /
RUN chmod +x /start-ka-server.sh
ADD ka.server.properties.initial /server.properties
CMD ["/start-ka-server.sh"]
EXPOSE 9092
```

##### 1.2 start-ka-server.sh

```
#!/bin/bash

set -e
FIXED_IP=$(/sbin/ip route|awk '/default/ { print $3 }')
if [ -z ${BROKER_ID} ] ; then
  echo 'No BROKER_ID specified, please specify one between 1 and 255'
  exit -1
fi

if [ -z ${ZOO} ] ; then
  echo 'No Zookeeper connection string (ZOO) specified.'
  exit -1
fi

echo "broker.id=${BROKER_ID}"$'\n' >> /server.properties
echo "zookeeper.connect=${ZOO}"$'\n' >> /server.properties
#host.name=<fixed IP>
#echo "host.name=${FIXED_IP}"$'\n' >> /server.properties
#advertised.host.name=<floating IP>
echo "advertised.host.name=${FLOATING_IP}"$'\n' >> /server.properties
/kafka/bin/kafka-server-start.sh /server.properties
```

#### 1.3 Build
```
d build -t feuyeux/kafka:0.8.2 .
```

###2 Kafka Cluster

```
#!/bin/bash

set -e

zoo_links='--link zoo1:zoo1 --link zoo2:zoo2 --link zoo3:zoo3 --link zoo4:zoo4 --link zoo5:zoo5'
zoo_connect='zoo1:2181,zoo2:2181,zoo3:2181,zoo4:2181,zoo5:2181'

# Start 3 Kafka's
for i in {1..3} ; do
  container_name=kafka${i}
  docker run -d ${zoo_links} -p 4909${i}:9092 \
  -e BROKER_ID=${i} -e ZOO=${zoo_connect} \
  -e FLOATING_IP=$(ifconfig eth0 |grep "inet addr"| cut -f 2 -d ":"|cut -f 1 -d " ") \
  --name kafka${i} -h kafka${i} feuyeux/kafka:0.8.2.1
done
```
