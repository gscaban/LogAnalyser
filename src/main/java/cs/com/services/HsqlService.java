package cs.com.services;

import cs.com.entity.AlertEvent;

public interface HsqlService {
    void saveAlertEvent(String id, String type, String host, long duration, boolean alert);
    void removeAlertEvent(AlertEvent alertEvent);
    boolean isAlertInDB(String id);
    boolean isJsonErrorInDB(String json);
    void saveJsonError(String json, String cause);
    long getAlertEventsRowCount();
}
