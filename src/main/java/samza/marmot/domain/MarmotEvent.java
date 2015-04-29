package samza.marmot.domain;

import org.apache.samza.SamzaException;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: Eric Han
 * Date:   15/4/13
 */
public class MarmotEvent {
    private static final ObjectMapper jsonMapper = new ObjectMapper();
    private int id;
    private long time;
    private String rawEvent;

    public MarmotEvent(int id, long time, String rawEvent) {
        init(id, time, rawEvent);
    }

    public MarmotEvent(Map<String, Object> jsonObject) {
        Integer id = (Integer) jsonObject.get("id");
        Long time = (Long) jsonObject.get("time");
        String raw = (String) jsonObject.get("raw");
        init(id, time, raw);
    }

    private void init(int id, long time, String rawEvent) {
        this.id = id;
        this.time = time;
        this.rawEvent = rawEvent;
    }

    public long getId() {
        return id;
    }

    public long getTime() {
        return time;
    }

    public String getRawEvent() {
        return rawEvent;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((rawEvent == null) ? 0 : rawEvent.hashCode());
        result = prime * result + (int) (time ^ (time >>> 32));
        result = prime * result + (id ^ (id >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MarmotEvent other = (MarmotEvent) obj;
        if (rawEvent == null) {
            if (other.rawEvent != null)
                return false;
        } else if (!rawEvent.equals(other.rawEvent))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "MarmotEvent [id=" + id + ", time=" + time + ", rawEvent=" + rawEvent + "]";
    }

    public String toJson() {
        return toJson(this);
    }

    public static Map<String, Object> toMap(MarmotEvent event) {
        Map<String, Object> jsonObject = new HashMap<>();
        jsonObject.put("id", event.getId());
        jsonObject.put("time", event.getTime());
        jsonObject.put("raw", event.getRawEvent());
        return jsonObject;
    }

    public static String toJson(MarmotEvent event) {
        Map<String, Object> jsonObject = toMap(event);

        try {
            return jsonMapper.writeValueAsString(jsonObject);
        } catch (Exception e) {
            throw new SamzaException(e);
        }
    }

    public static MarmotEvent fromJson(String json) {
        try {
            return new MarmotEvent((Map<String, Object>) jsonMapper.readValue(json, Map.class));
        } catch (Exception e) {
            throw new SamzaException(e);
        }
    }
}
