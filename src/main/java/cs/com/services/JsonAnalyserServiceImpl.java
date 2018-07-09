package cs.com.services;

import cs.com.enums.LogAnalyserEnum;
import cs.com.exception.LogAnalyserException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;



public class JsonAnalyserServiceImpl implements JsonAnalyserService {

    private JSONParser parser;

    public JsonAnalyserServiceImpl() {
        parser = new JSONParser();
    }

    @Override
    public long compareJsonsAndCalculateTimePeriod(String mainJson, String secondaryJson) throws LogAnalyserException {
            String mainID = getStringParameterValueFromJsonString(mainJson, LogAnalyserEnum.ID.getName());
            String secondID = getStringParameterValueFromJsonString(secondaryJson, LogAnalyserEnum.ID.getName());
            String mainState = getStringParameterValueFromJsonString(mainJson, LogAnalyserEnum.STATE.getName());
            String secondState = getStringParameterValueFromJsonString(secondaryJson, LogAnalyserEnum.STATE.getName());
            if (mainID == null || secondID == null || mainState == null || secondState == null ||
                    !mainID.equals(secondID) || mainState.equals(secondState))
                return -1;
            return calculateTimePeriod(mainJson, secondaryJson);
    }

    @Override
    public String getStringParameterValueFromJsonString(String json, String parameter) throws LogAnalyserException {
        JSONObject jsonObject = getJsonObject(json);
        return jsonObject != null ? (String) jsonObject.get(parameter) : null;
    }

    @Override
    public long getTimestampValueFromJsonString(String json, String parameter) throws LogAnalyserException {
        JSONObject jsonObject = getJsonObject(json);
        return jsonObject != null ? (Long) jsonObject.get(parameter) : null;
    }

    @Override
    public long calculateTimePeriod(String mainJson, String secondaryJson) throws LogAnalyserException {
        JSONObject mainObject = getJsonObject(mainJson);
        long mainTimestamp = (Long) mainObject.get(LogAnalyserEnum.TIMESTAMP.getName());
        JSONObject secondObject = getJsonObject(secondaryJson);
        long secondTimestamp = (Long) secondObject.get(LogAnalyserEnum.TIMESTAMP.getName());
        return getStringParameterValueFromJsonString(mainJson, LogAnalyserEnum.STATE.getName()).equals(LogAnalyserEnum.FINISHED.getName()) ?
                mainTimestamp - secondTimestamp : secondTimestamp - mainTimestamp;
    }

    private JSONObject getJsonObject(String json) throws LogAnalyserException{
        Object obj = null;
        try {
            obj = parser.parse(json);
        } catch (ParseException e) {
           throw new LogAnalyserException(json, e);
        }
        return obj != null ? (JSONObject) obj : null;
    }
}
