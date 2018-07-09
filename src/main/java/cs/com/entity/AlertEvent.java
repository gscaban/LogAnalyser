package cs.com.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name="alert_event")
public class AlertEvent implements Serializable{

    private static final long serialVersionUID = -1798070786993154676L;

    @Id
    @Column(name="id", unique = true, nullable = false)
    private String id;

    @Column(name="duration", nullable = false)
    private long duration;

    @Column(name="type")
    private String type;

    @Column(name="host")
    private String host;

    @Column(name="alert", nullable = false)
    private boolean alert;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public boolean isAlert() {
        return alert;
    }

    public void setAlert(boolean alert) {
        this.alert = alert;
    }
}
