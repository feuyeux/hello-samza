package samza.marmot.task;

import org.apache.log4j.Logger;
import org.apache.samza.system.IncomingMessageEnvelope;
import org.apache.samza.system.OutgoingMessageEnvelope;
import org.apache.samza.system.SystemStream;
import org.apache.samza.task.MessageCollector;
import org.apache.samza.task.StreamTask;
import org.apache.samza.task.TaskCoordinator;
import samza.marmot.domain.MarmotEvent;

import java.util.Map;

/**
 * Author: Eric Han
 * Date:   15/4/29
 */
public class MarmotFeedStreamTask implements StreamTask {
    private Logger log = Logger.getLogger(MarmotFeedStreamTask.class);
    private static final SystemStream OUTPUT_STREAM = new SystemStream("kafka", "marmot-raw");

    public void process(IncomingMessageEnvelope envelope, MessageCollector collector, TaskCoordinator coordinator) {
        MarmotEvent marmotEvent = (MarmotEvent) envelope.getMessage();
        log.info(marmotEvent);
        Map<String, Object> outgoingMap = MarmotEvent.toMap(marmotEvent);
        collector.send(new OutgoingMessageEnvelope(OUTPUT_STREAM, outgoingMap));
    }
}
