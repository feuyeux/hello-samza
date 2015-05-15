#!/usr/bin/env bash
cd ../hello-samza
echo "Start marmot report Job:"
deploy/samza/bin/run-job.sh \
--config-factory=org.apache.samza.config.factories.PropertiesConfigFactory \
--config-path=file://$PWD/deploy/samza/config/marmot-report.properties

echo "Check kafka message:"
deploy/kafka/bin/kafka-console-consumer.sh --zookeeper localhost:2181 --topic marmot-report