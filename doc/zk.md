cluster
----
zk1.alibaba.net 10.125.65.102
zk2.alibaba.net 10.125.49.156
zk3.alibaba.net 10.125.61.196

configuration
----
zookeeper-3.4.6

- /opt/zookeeper/data/myid 

```
1
```

- /opt/zookeeper/conf/zoo.cfg

```
tickTime=2000
# The number of ticks that the initial
# synchronization phase can take
initLimit=10
# The number of ticks that can pass between
# sending a request and getting an acknowledgement
syncLimit=5
# the directory where the snapshot is stored.
dataDir=/opt/zookeeper/data
# the port at which the clients will connect
clientPort=2181
# The number of snapshots to retain in dataDir
#autopurge.snapRetainCount=3
# Purge task interval in hours
# Set to "0" to disable auto purge feature
#autopurge.purgeInterval=1
server.1=zk1.alibaba.net:2888:3888
server.2=zk2.alibaba.net:2888:3888
server.3=zk3.alibaba.net:2888:3888
```