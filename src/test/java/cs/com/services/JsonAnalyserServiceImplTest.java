package cs.com.services;

import cs.com.enums.LogAnalyserEnum;
import cs.com.exception.LogAnalyserException;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class JsonAnalyserServiceImplTest {

    private JsonAnalyserService service;
    private static final String oneJSON = "{\"id\":\"scsmbstgrc\", \"state\":\"FINISHED\", \"timestamp\":1491377495218}";
    private static final String twoJSON = "{\"id\":\"scsmbstgrc\", \"state\":\"STARTED\", \"timestamp\":1491377495210}";
    private static final String thirdJSON = "{\"id\":\"scsmbstgra\", \"state\":\"FINISHED\", \"type\":\"APPLICATION_LOG\",\"host\":\"test_host\",\"timestamp\":1491377495217}";
    private static final String STATE = "FINISHED";
    private static final String ID = "scsmbstgrc";
    private static final String HOST = "test_host";
    private static final String TYPE = "APPLICATION_LOG";
    private static final long TIMESTAMP = 1491377495218L;

    @Before
    public void setup() throws Exception {
        service = new JsonAnalyserServiceImpl();
    }

    @Test
    public void checkCompareJsonsAndCalculateTimePeriod() throws LogAnalyserException {
        long resultOne = service.compareJsonsAndCalculateTimePeriod(oneJSON, twoJSON);
        long resultTwo = service.compareJsonsAndCalculateTimePeriod(oneJSON, thirdJSON);
        Assert.assertEquals(8, resultOne);
        Assert.assertEquals(-1, resultTwo);
    }

    @Test
    public void checkGetParameterValueFromJsonString() throws LogAnalyserException {
        String testState = null, testId = null, testHost = null, testType = null;
        long testTimeStamp = 0;
        testState = service.getStringParameterValueFromJsonString(oneJSON, LogAnalyserEnum.STATE.getName());
        testId = service.getStringParameterValueFromJsonString(oneJSON, LogAnalyserEnum.ID.getName());
        testTimeStamp = service.getTimestampValueFromJsonString(oneJSON, LogAnalyserEnum.TIMESTAMP.getName());
        testHost = service.getStringParameterValueFromJsonString(thirdJSON, LogAnalyserEnum.HOST.getName());
        testType = service.getStringParameterValueFromJsonString(thirdJSON, LogAnalyserEnum.TYPE.getName());
        Assert.assertEquals(STATE, testState);
        Assert.assertEquals(ID, testId);
        Assert.assertEquals(HOST, testHost);
        Assert.assertEquals(TYPE, testType);
        Assert.assertEquals(TIMESTAMP, testTimeStamp);
    }

    @Test
    public void checkCalculateTimePeriod() throws LogAnalyserException {
        long result = service.compareJsonsAndCalculateTimePeriod(oneJSON, twoJSON);
        Assert.assertEquals(8, result);
    }
}
