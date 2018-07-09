package cs.com.exception;

import org.json.simple.parser.ParseException;


public class LogAnalyserException extends ParseException {

    private String json;

    public LogAnalyserException(String json, ParseException e) {
        super(e.getPosition(), e.getErrorType(), e.getUnexpectedObject());
        this.json = json;
        this.setStackTrace(e.getStackTrace());
    }

    public String getJson() {
        return json;
    }
}
