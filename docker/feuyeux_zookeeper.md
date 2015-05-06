Docker-Ubuntu-Zookeeper
-------------------

### 1 Zookeeper Image

> workspace：/home/erichan/docker-room/ubuntu-zookeeper

> reference: https://github.com/wouterd/docker-zookeeper

#### 1.1 Dockerfile

```
# Version: 0.0.1
FROM feuyeux/ubuntu-java7
MAINTAINER Eric Han "feuyeux@gmail.com"
ADD http://ftp.nluug.nl/internet/apache/zookeeper/stable/zookeeper-3.4.6.tar.gz /zookeeper.tar.gz
RUN mkdir -p zoo && \
  cd zoo && \
  tar xvfz ../zookeeper.tar.gz --strip-components=1 && \
  rm /zookeeper.tar.gz
ADD start-zk-server.sh /
RUN chmod +x /start-zk-server.sh
CMD ["/start-zk-server.sh"]
EXPOSE 2888 3888 2181
```

#### 1.2 start-zk-server.sh

```
#!/bin/bash

if [ -z ${ZOO_ID} ] ; then
  echo 'No ID specified, please specify one between 1 and 255'
  exit -1
fi

if [ ! -f /conf/zoo.cfg ] ; then
  echo 'Waiting for config file to appear...'
  while [ ! -f /zoo/conf/zoo.cfg ] ; do
    sleep 1
  done
  echo 'Config file found, starting server.'
fi

mkdir -p /var/lib/zookeeper

echo "${ZOO_ID}" > /var/lib/zookeeper/myid

/zoo/bin/zkServer.sh start-foreground
```

#### 1.3 Build
> alias d='sudo docker'

```
d build -t feuyeux/zookeeper:3.4.6 .
```

###2 ZK Cluster

#### 2.1 Cluster Shell

```
#!/bin/bash

set -e

zk_img=feuyeux/zookeeper:3.4.6

# Need a volume to read the config from
conf_container=zoo1

# Start the zookeeper containers
for i in {1..5} ; do
  if [ "${i}" == "1" ] ; then
    docker run -d -v /zoo/conf -p 42181:2181 --name "zoo${i}" -e ZOO_ID=${i} ${zk_img}
  else
    docker run -d --volumes-from ${conf_container} -p 4218${i}:2181 --name "zoo${i}" -e ZOO_ID=${i} ${zk_img}
  fi
done

config=$(cat zoo.cfg.initial)

# Look up the zookeeper instance IPs and create the config file
for i in {1..5} ; do
  container_name=zoo${i}
  container_ip=$(docker inspect --format '{{.NetworkSettings.IPAddress}}' ${container_name})
  line="server.${i}=${container_ip}:2888:3888"
  config="${config}"$'\n'"${line}"
done

# Write the config to the config container
# https://registry.hub.docker.com/_/busybox/
echo "${config}" | docker run -i --rm --volumes-from ${conf_container} busybox sh -c 'cat > /zoo/conf/zoo.cfg'
```

#### 2.2 zoo.cfg.initial

```
tickTime=2000
dataDir=/var/lib/zookeeper
clientPort=2181
initLimit=5
syncLimit=2
```
