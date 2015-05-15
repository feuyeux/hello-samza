#!/usr/bin/env bash
cd ../hello-samza
mvn clean package -DskipTests
bin/grid bootstrap
export HADOOP_HOME=$PWD/deploy/yarn
mkdir -p deploy/samza
tar -xvf target/marmot-samza-0.1-dist.tar.gz -C deploy/samza
echo "Start marmot feed Job:"
deploy/samza/bin/run-job.sh \
--config-factory=org.apache.samza.config.factories.PropertiesConfigFactory \
--config-path=file://$PWD/deploy/samza/config/marmot-feed.properties

echo "Check kafka message:"
deploy/kafka/bin/kafka-console-consumer.sh --zookeeper localhost:2181 --topic marmot-raw