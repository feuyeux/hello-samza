```
org.apache.samza.job.JobRunner 
--config-factory=org.apache.samza.config.factories.PropertiesConfigFactory 
--config-path=/home/lu.hl/marmot/config/marmot-cluster-feed.properties
```

/Users/erichan/sourcecode/github/samza/samza-core/src/main/scala/org/apache/samza/job/JobRunner.scala

/Users/erichan/sourcecode/github/samza/samza-yarn/src/main/scala/org/apache/samza/job/yarn/YarnJob.scala

```
SAMZA_CONFIG -> {
systems.kafka.consumer.zookeeper.connect:localhost:2181
systems.kafka.samza.factory:org.apache.samza.system.kafka.KafkaSystemFactory
task.inputs:marmot.marmot-test
yarn.package.path:file:///Users/erichan/sourcecode/feuyeux/hello-samza/target/marmot-samza-0.1-dist.tar.gz
job.factory.class:org.apache.samza.job.yarn.YarnJobFactory
task.class:samza.marmot.task.MarmotFeedStreamTask
systems.marmot.samza.factory:samza.marmot.system.MarmotSystemFactory\
systems.kafka.samza.msg.serde:json
job.name:marmot-feed
serializers.registry.json.class:org.apache.samza.serializers.JsonSerdeFactory
systems.kafka.producer.bootstrap.servers:localhost:9092
task.log4j.system:marmot-samza
} for application_1431418401196_0001

set memory request to 1024 for application_1431418401196_0001
set cpu core request to 1 for application_1431418401196_0001

List(
export SAMZA_LOG_DIR=<LOG_DIR> && 
ln -sfn <LOG_DIR> logs && 
exec ./__package/bin/run-am.sh 1>logs/stdout 2>logs/stderr
) for application_1431418401196_0001

2015-05-12 16:13:24 YarnClientImpl [INFO] Submitted application application_1431418401196_0001
2015-05-12 16:13:24 JobRunner [INFO] waiting for job to start
```

```
[[ ${JAVA_OPTS} != *-server* ]] && 
export JAVA_OPTS="$JAVA_OPTS -server"

# Set container name system properties for use in Log4J
[[ ${JAVA_OPTS} != *-Dsamza.container.name* ]] && 
export JAVA_OPTS="$JAVA_OPTS -Dsamza.container.name=samza-application-master"

exec $(dirname $0)/run-class.sh org.apache.samza.job.yarn.SamzaAppMaster "$@"
```

/Users/erichan/sourcecode/github/samza/samza-yarn/src/main/scala/org/apache/samza/job/yarn/SamzaAppMaster.scala


```
[[ ${JAVA_OPTS} != *-server* ]] && 
export JAVA_OPTS="$JAVA_OPTS -server"

# Set container ID system property for use in Log4J
[[ ${JAVA_OPTS} != *-Dsamza.container.id* && ! -z "$SAMZA_CONTAINER_ID" ]] && 
export JAVA_OPTS="$JAVA_OPTS -Dsamza.container.id=$SAMZA_CONTAINER_ID"

# Set container name system property for use in Log4J
[[ ${JAVA_OPTS} != *-Dsamza.container.name* && ! -z "$SAMZA_CONTAINER_ID" ]] && 
export JAVA_OPTS="$JAVA_OPTS -Dsamza.container.name=samza-container-$SAMZA_CONTAINER_ID"

exec $(dirname $0)/run-class.sh org.apache.samza.container.SamzaContainer "$@"
```

/Users/erichan/sourcecode/github/samza/samza-core/src/main/scala/org/apache/samza/container/SamzaContainer.scala

#### resource manager
http://localhost:8088

#### node manager
http://10.18.213.136:8042/node

#### application-master
http://localhost:8088/proxy/application_1431410387336_0001/#application-master


2015-05-12 16:13:24 JobRunner [INFO] job started successfully - Running
