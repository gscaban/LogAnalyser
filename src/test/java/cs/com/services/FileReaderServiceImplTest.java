package cs.com.services;

import cs.com.enums.LogAnalyserEnum;
import org.apache.commons.io.LineIterator;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;

public class FileReaderServiceImplTest {

    private FileReaderService service;
    private JsonAnalyserService jsonAnalyserService;
    private HsqlService hsqlService;
    private static final String ID = "scsmbstgra";
    private static final String TYPE = "APPLICATION_LOG";
    private static final String HOST = "12345";
    private static final String MAIN_LINE = "{\"id\":\"scsmbstgra\", \"state\":\"STARTED\", \"type\":\"APPLICATION_LOG\", \"host\":\"12345\", \"timestamp\":1491377495212}";
    private static final String SECOND_LINE = "{\"id\":\"scsmbstgra\", \"state\":\"FINISHED\", \"type\":\"APPLICATION_LOG\",\"host\":\"12345\", \"timestamp\":1491377495217}";

    @Before
    public void setup(){
        jsonAnalyserService = Mockito.mock(JsonAnalyserServiceImpl.class);
        hsqlService = Mockito.mock(HsqlServiceImpl.class);
        service = new FileReaderServiceImpl(jsonAnalyserService, hsqlService);
    }

    @Test
    public void checkExecuteIteration() throws ParseException {
        LineIterator iterator = Mockito.mock(LineIterator.class);
        Mockito.when(iterator.hasNext()).thenReturn(true,false);
        Mockito.when(iterator.nextLine()).thenReturn(SECOND_LINE);
        Mockito.when(jsonAnalyserService.getStringParameterValueFromJsonString(any(String.class), any(String.class))).
                thenReturn(LogAnalyserEnum.STARTED.getName(),ID, TYPE, HOST);
        Mockito.when(jsonAnalyserService.compareJsonsAndCalculateTimePeriod(any(String.class), any(String.class))).thenReturn(5L);
        service.executeIteration(MAIN_LINE, iterator);
        Mockito.verify(hsqlService).saveAlertEvent(ID, TYPE, HOST, 5L, true);
    }
}
