#!/usr/bin/env bash
cd /Users/erichan/sourcecode/feuyeux/hello-samza
mvn clean package
bin/grid bootstrap
mkdir -p deploy/samza
tar -xvf target/marmot-samza-0.1-dist.tar.gz -C deploy/samza
deploy/samza/bin/run-job.sh \
--config-factory=org.apache.samza.config.factories.PropertiesConfigFactory \
--config-path=file://$PWD/deploy/samza/config/marmot-feed.properties

deploy/kafka/bin/kafka-console-consumer.sh \
--zookeeper zk1.alibaba.net:2181,zk2.alibaba.net:2181,zk3.alibaba.net:2181 --topic marmot-raw
#--zookeeper localhost:2181 --topic marmot-raw
#--zookeeper docker.alibaba.net:42181,docker.alibaba.net:42182,docker.alibaba.net:42183,docker.alibaba.net:42184,docker.alibaba.net:42185 --topic marmot-raw