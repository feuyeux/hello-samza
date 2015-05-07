cluster
----
### nn/rm/zkfc [2CPU,4096M]
yarn1.alibaba.net 100.69.198.109
yarn2.alibaba.net 100.69.199.164
yarn3.alibaba.net 100.69.199.142

### dn/nm [4CPU,8192M]
yarn4.alibaba.net 100.69.192.60
yarn5.alibaba.net 100.69.199.126
yarn6.alibaba.net 100.69.192.253

> Note:
nn:NameNode
 nna:NameNode Active
 nns:NameNode Standby
snn:Secondary NameNode
rm:ResourceManager
zkfc:Zookeeper Failover Controller
> 
dn:DataNode
nm:NodeManager

configuration
----
1. 1.8.0_45-b14 Java HotSpot(TM) 64-Bit Server VM (build 25.45-b02, mixed mode)
1. hadoop-2.7.0

### 0. /etc/profile

```shell
export JAVA_HOME=/opt/jdk1.8.0_45/
export HADOOP_HOME=/opt/hadoop-2.7.0
export HADOOP_PREFIX=$HADOOP_HOME
export HADOOP_BIN=$HADOOP_HOME/bin
export HADOOP_CONF_DIR=$HADOOP_HOME/etc/hadoop
export PATH=$PATH:$JAVA_HOME/bin:$HADOOP_BIN
```

### $HADOOP_CONF_DIR/hadoop-env.sh
### $HADOOP_CONF_DIR/yarn-env.sh

### 1. $HADOOP_CONF_DIR/core-site.xml

```xml
<configuration>
 <property>
    <name>hadoop.tmp.dir</name>
    <value>${user.home}/hadoop/tmp</value>
  </property>

  <property>
    <name>fs.defaultFS</name>
    <value>hdfs://mycluster</value>
  </property>

  <property>
    <name>ha.zookeeper.quorum</name>
    <value>zk1.alibaba.net:2181,zk2.alibaba.net:2181,zk3.alibaba.net:2181</value>
  </property>
</configuration>
```

```
mkdir -p $HOME/hadoop/tmp
```

### 2. $HADOOP_CONF_DIR/hdfs-site.xml

```xml
<configuration>

<!-- namenode -->
    <property>
        <name>dfs.nameservices</name>
        <value>mycluster</value>
    </property>
    <property>
        <name>dfs.ha.namenodes.mycluster</name>
        <value>nn1,nn2,nn3</value>
    </property>
    
    <property>
        <name>dfs.namenode.rpc-address.mycluster.nn1</name>
        <value>yarn1.alibaba.net:50900</value>
    </property>
    <property>
        <name>dfs.namenode.rpc-address.mycluster.nn2</name>
        <value>yarn2.alibaba.net:50900</value>
    </property>
    <property>
        <name>dfs.namenode.rpc-address.mycluster.nn3</name>
        <value>yarn3.alibaba.net:50900</value>
    </property>
    
    <property>
        <name>dfs.namenode.http-address.mycluster.nn1</name>
        <value>yarn1.alibaba.net:50070</value>
    </property>
    <property>
        <name>dfs.namenode.http-address.mycluster.nn2</name>
        <value>yarn2.alibaba.net:50070</value>
    </property>
    <property>
        <name>dfs.namenode.http-address.mycluster.nn3</name>
        <value>yarn3.alibaba.net:50070</value>
    </property>

<!-- datanode -->
    <property>
      <name>dfs.datanode.http.address</name>
      <value>0.0.0.0:50075</value>
    </property>
          
    <property>
        <name>dfs.namenode.shared.edits.dir</name>
        <value>qjournal://yarn1.alibaba.net:50485;yarn2.alibaba.net:50485;yarn3.alibaba.net:50485;yarn4.alibaba.net:50485;yarn5.alibaba.net:50485;yarn6.alibaba.net:50485/mycluster</value>
    </property>

    <property>
        <name>dfs.namenode.edits.journal-plugin.qjournal</name>
        <value>org.apache.hadoop.hdfs.qjournal.client.QuorumJournalManager</value>
    </property>
  
    <property>
        <name>dfs.client.failover.proxy.provider.mycluster</name>
        <value>org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider</value>
    </property>
    <property>
        <name>dfs.ha.fencing.methods</name>
        <value>sshfence(${user.name}:22)</value>
    </property>
    <property>
        <name>dfs.ha.fencing.ssh.private-key-files</name>
        <value>${user.home}/.ssh/id_rsa</value>
    </property>
    <property>
        <name>dfs.journalnode.edits.dir</name>
        <value>${user.home}/hadoop/data/tmp/journal</value>
    </property>
    <property>
        <name>dfs.ha.automatic-failover.enabled</name>
        <value>true</value>
    </property>

    <property>
        <name>dfs.replication</name>
        <value>3</value>
    </property>
    <property>
        <name>dfs.webhdfs.enabled</name>
        <value>true</value>
    </property>
    <property>
        <name>dfs.journalnode.http-address</name>
        <value>0.0.0.0:50480</value>
    </property>
    <property>
        <name>dfs.journalnode.rpc-address</name>
        <value>0.0.0.0:50485</value>
    </property>
    <property>
        <name>ha.zookeeper.quorum</name>
        <value>zk1.alibaba.net:2181,zk2.alibaba.net:2181,zk3.alibaba.net:2181</value>
    </property>
</configuration>
```

### 3. $HADOOP_CONF_DIR/yarn-site.xml

```xml
<configuration>
<!-- resourcemanager -->
    <property>
        <name>yarn.resourcemanager.ha.enabled</name>
        <value>true</value>
    </property>
    <property>
        <name>yarn.resourcemanager.ha.rm-ids</name>
        <value>rm1,rm2,rm3</value>
    </property>
    <property>
        <name>yarn.resourcemanager.ha.automatic-failover.enabled</name>
        <value>true</value>
    </property>
    <property>
        <name>ha.zookeeper.quorum</name>
        <value>zk1.alibaba.net:2181,zk2.alibaba.net:2181,zk3.alibaba.net:2181</value>
    </property>
    <property>
        <name>yarn.resourcemanager.hostname.rm1</name>
        <value>nn1</value>
    </property>
    <property>
        <name>yarn.resourcemanager.hostname.rm2</name>
        <value>nn2</value>
    </property>
    <property>
        <name>yarn.resourcemanager.hostname.rm3</name>
        <value>nn3</value>
    </property>

    <property>
        <name>yarn.resourcemanager.recovery.enabled</name>
        <value>true</value>
    </property>
    <property>
        <name>yarn.resourcemanager.zk-state-store.address</name>
        <value>zk1.alibaba.net:2181,zk2.alibaba.net:2181,zk3.alibaba.net:2181</value>
    </property>
    <property>
        <name>yarn.resourcemanager.store.class</name>
        <value>org.apache.hadoop.yarn.server.resourcemanager.recovery.ZKRMStateStore</value>
    </property>
    <property>
        <name>yarn.resourcemanager.zk-address</name>
        <value>zk1.alibaba.net:2181,zk2.alibaba.net:2181,zk3.alibaba.net:2181</value>
    </property>
    <property>
        <name>yarn.resourcemanager.cluster-id</name>
        <value>mycluster-yarn</value>
    </property>
    <property>
        <name>yarn.app.mapreduce.am.scheduler.connection.wait.interval-ms</name>
        <value>5000</value>
    </property>
    <property>
        <name>yarn.resourcemanager.ha.automatic-failover.zk-base-path</name>
        <value>/yarn-leader-election</value>
    </property>
    
<!-- one machine one conf -->
    <property>
        <name>yarn.resourcemanager.ha.id</name>
        <value>rm1</value>
    </property>
    <property>
        <name>yarn.resourcemanager.address.rm1</name>
        <value>nn1:50132</value>
    </property>
    <property>
        <name>yarn.resourcemanager.scheduler.address.rm1</name>
        <value>nn1:50130</value>
    </property>
    <property>
        <name>yarn.resourcemanager.webapp.address.rm1</name>
        <value>nn1:50188</value>
    </property>
    <property>
        <name>yarn.resourcemanager.resource-tracker.address.rm1</name>
        <value>nn1:50131</value>
    </property>
    <property>
        <name>yarn.resourcemanager.admin.address.rm1</name>
        <value>nn1:50033</value>
    </property>
    <property>
        <name>yarn.resourcemanager.ha.admin.address.rm1</name>
        <value>nn1:23142</value>
    </property>

<!-- nodemanager -->
    <property>
        <name>yarn.nodemanager.aux-services</name>
        <value>mapreduce_shuffle</value>
    </property>
    <property>
        <name>yarn.nodemanager.aux-services.mapreduce.shuffle.class</name>
        <value>org.apache.hadoop.mapred.ShuffleHandler</value>
    </property>
    <property>
        <name>yarn.nodemanager.local-dirs</name>
        <value>${user.home}/yarn/local-dir</value>
    </property>

    <property>
        <name>yarn.nodemanager.log-dirs</name>
        <value>${user.home}/yarn/log-dir</value>
    </property>
    <property>
        <name>mapreduce.shuffle.port</name>
        <value>23080</value>
    </property>
    <property>
        <name>yarn.nodemanager.webapp.address</name>
        <value>0.0.0.0:50842</value>
    </property>
</configuration>
```

```
mkdir -p $HOME/yarn/local-dir
mkdir -p $HOME/yarn/log-dir
```

### 4. $HADOOP_CONF_DIR/mapred-site.xml

```xml
<configuration>
    <property>
        <name>mapreduce.framework.name</name>
        <value>yarn</value>
    </property>

<!-- one machine one conf -->
    <property>
        <name>mapreduce.jobhistory.webapp.address</name>
        <value>yarn1.alibaba.net:50888</value>
    </property>

    <property>
        <name>mapreduce.map.java.opts</name>
        <value>-Xmx512m -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC \
            -XX:+UseCMSCompactAtFullCollection \
            -XX:+CMSClassUnloadingEnabled \
            -Dclient.encoding.override=UTF-8 -Dfile.encoding=UTF-8 \
            -Duser.language=zh -Duser.region=CN</value>
    </property>

    <property>
        <name>mapreduce.reduce.java.opts</name>
        <value>-Xmx512m -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC \
            -XX:+UseCMSCompactAtFullCollection \
            -XX:+CMSClassUnloadingEnabled \
            -Dclient.encoding.override=UTF-8 -Dfile.encoding=UTF-8 \
            -Duser.language=zh -Duser.region=CN</value>
    </property>

    <property>
        <name>mapreduce.client.submit.file.replication</name>
        <value>3</value>
    </property>
    <property>
        <name>mapreduce.map.speculative</name>
        <value>false</value>
    </property>

    <property>
        <name>mapreduce.reduce.speculative</name>
        <value>false</value>
    </property>
    <property>
        <name>mapreduce.job.reduce.slowstart.completedmaps</name>
        <value>0.85</value>
    </property>
    <property>
        <name>mapreduce.reduce.shuffle.input.buffer.percent</name>
        <value>0.60</value>
    </property>
    <property>
        <name>yarn.app.mapreduce.am.resource.mb</name>
        <value>640</value>
    </property>
    <property>
        <name>yarn.app.mapreduce.am.command-opts</name>
        <value>-Xmx500m</value>
    </property>
    <property>
        <name>mapreduce.task.io.sort.factor</name>
        <value>20</value>
    </property>
    <property>
        <name>mapreduce.task.io.sort.mb</name>
        <value>200</value>
    </property>
    <property>
        <name>mapreduce.map.memory.mb</name>
        <value>640</value>
    </property>
    <property>
        <name>mapreduce.reduce.memory.mb</name>
        <value>640</value>
    </property>
    <property>
        <name>mapreduce.job.ubertask.enable</name>
        <value>false</value>
    </property>
    <property>
        <name>mapreduce.jobhistory.cleaner.enable</name>
        <value>true</value>
    </property>
</configuration>
```

### 5. $HADOOP_CONF_DIR/slaves

```
yarn4.alibaba.net
yarn5.alibaba.net
yarn6.alibaba.net
```

### copy

```shell
cd /opt/hadoop-2.7.0/etc
for h in yarn2.alibaba.net yarn3.alibaba.net yarn4.alibaba.net yarn5.alibaba.net yarn6.alibaba.net; do
    ssh -p 22 $h "rm -rf /opt/hadoop-2.7.0/etc/hadoop"; 
    scp -P 22 -r ./hadoop $h:/opt/hadoop-2.7.0/etc/; 
done
```