package cs.com.services;

import cs.com.exception.LogAnalyserException;

public interface JsonAnalyserService {
    long compareJsonsAndCalculateTimePeriod(String mainJson, String secondaryJson) throws LogAnalyserException;
    long calculateTimePeriod(String mainJson, String secondaryJson) throws LogAnalyserException;
    String getStringParameterValueFromJsonString(String json, String parameter) throws LogAnalyserException;
    long getTimestampValueFromJsonString(String json, String parameter) throws LogAnalyserException;
}
