package cs.com.enums;

public enum LogAnalyserEnum {
    ID("id"),
    TIMESTAMP("timestamp"),
    STATE("state"),
    TYPE("type"),
    HOST("host"),
    STARTED("STARTED"),
    FINISHED("FINISHED");

    private String name;

    LogAnalyserEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
