#!/usr/bin/env bash
deploy/samza/bin/run-job.sh \
--config-factory=org.apache.samza.config.factories.PropertiesConfigFactory \
--config-path=file://$PWD/deploy/samza/config/marmot-report.properties

deploy/kafka/bin/kafka-console-consumer.sh --zookeeper localhost:2181 --topic marmot-report