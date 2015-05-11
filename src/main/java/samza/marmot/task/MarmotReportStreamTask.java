package samza.marmot.task;

import org.apache.samza.system.IncomingMessageEnvelope;
import org.apache.samza.system.OutgoingMessageEnvelope;
import org.apache.samza.system.SystemStream;
import org.apache.samza.task.MessageCollector;
import org.apache.samza.task.StreamTask;
import org.apache.samza.task.TaskCoordinator;
import org.apache.samza.task.WindowableTask;
import samza.marmot.domain.MarmotEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: Eric Han
 * Date:   15/4/29
 */
public class MarmotReportStreamTask implements StreamTask, WindowableTask {
    private final Map<String, Integer> counts = new HashMap<>();
    private final Map<String, Long> times = new HashMap<>();
    private String user0;
    private Long minimum = Long.MAX_VALUE;

    @Override
    public void process(IncomingMessageEnvelope envelope, MessageCollector collector, TaskCoordinator coordinator) {
        @SuppressWarnings("unchecked")
        Map<String, Object> jsonObject = (Map<String, Object>) envelope.getMessage();
        MarmotEvent event = new MarmotEvent(jsonObject);
        try {
            String message = event.getRawEvent();
            String[] columns = message.split(",");
            String user = columns[0];
            Long time = Long.valueOf(columns[1]);
            Integer count = counts.get(user);
            if (count == null) {
                count = 0;
            }
            count += 1;
            counts.put(user, count);

            Long lastedTime = times.get(user);
            if (lastedTime != null) {
                Long minus = time - lastedTime;
                if (minus < minimum) {
                    minimum = minus;
                    user0 = user;
                }
            }
            times.put(user, time);
        } catch (Exception e) {
            System.err.println("Unable to parse line: " + event);
        }
    }

    @Override
    public void window(MessageCollector collector, TaskCoordinator coordinator) {
        if (user0 != null) {
            counts.put("[minimum]" + user0, minimum.intValue());
        }
        collector.send(new OutgoingMessageEnvelope(new SystemStream("kafka", "marmot-report"), counts));
        counts.remove("[minimum]" + user0);
    }
}
