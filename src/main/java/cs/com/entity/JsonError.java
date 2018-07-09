package cs.com.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name="jsonerror")
public class JsonError implements Serializable {

    @Id
    @Column(name="json", unique = true, nullable = false)
    private String json;

    @Column(name="cause")
    private String cause;

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }
}
