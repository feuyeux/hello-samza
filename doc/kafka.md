cluster
----
ka1.alibaba.net 100.69.192.106
ka2.alibaba.net 100.69.192.204
ka3.alibaba.net 100.69.194.208

configuration
----
1. 1.8.0_45-b14 Java HotSpot(TM) 64-Bit Server VM (build 25.45-b02, mixed mode)
1. kafka_2.11-0.8.2.1

- /opt/kafka_2.11-0.8.2.1/config/server.properties

```
broker.id=1
zookeeper.connect=zk1.alibaba.net:2181,zk2.alibaba.net:2181,zk3.alibaba.net:2181
advertised.host.name=ka1.alibaba.net
```

run
----
/opt/kafka_2.11-0.8.2.1/bin/kafka-server-start.sh /opt/kafka_2.11-0.8.2.1/config/server.properties