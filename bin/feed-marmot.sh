#!/usr/bin/env bash
mvn clean package
bin/grid bootstrap
mkdir -p deploy/samza
tar -xvf ./target/marmot-samza-0.1-dist.tar.gz -C deploy/samza
deploy/samza/bin/run-job.sh \
--config-factory=org.apache.samza.config.factories.PropertiesConfigFactory \
--config-path=file://$PWD/deploy/samza/config/marmot-feed.properties

deploy/kafka/bin/kafka-console-consumer.sh \
--zookeeper localhost:2181 --topic marmot-raw