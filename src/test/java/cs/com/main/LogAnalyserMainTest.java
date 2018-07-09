package cs.com.main;

import cs.com.enums.LogAnalyserEnum;
import cs.com.services.*;
import org.apache.commons.io.LineIterator;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.ArgumentMatchers.any;


public class LogAnalyserMainTest {

    private LogAnalyserMain controller;
    private FileReaderService fileReaderService;
    private JsonAnalyserService jsonAnalyserService;
    private HsqlService hsqlService;

    private static final String FILE = "test.txt";
    private static final String LINE = "{\"id\":\"scsmbstgrb\", \"state\":\"STARTED\", \"timestamp\":1491377495213}";

    @Before
    public void setup(){
        fileReaderService = Mockito.mock(FileReaderServiceImpl.class);
        jsonAnalyserService = Mockito.mock(JsonAnalyserServiceImpl.class);
        hsqlService = Mockito.mock(HsqlServiceImpl.class);
        controller = new LogAnalyserMain(jsonAnalyserService, hsqlService, fileReaderService);
    }

    @Test
    public void checkExecuteIteration() throws ParseException {
      LineIterator iterator = Mockito.mock(LineIterator.class);
      Mockito.when(iterator.hasNext()).thenReturn(true,false);
      Mockito.when(iterator.nextLine()).thenReturn(LINE);
      Mockito.when(jsonAnalyserService.getStringParameterValueFromJsonString(any(String.class), any(String.class))).thenReturn(LINE);
      Mockito.when(hsqlService.isAlertInDB(any(String.class))).thenReturn(true);
      controller.executeIteration(FILE, iterator);
      Mockito.verify(jsonAnalyserService).getStringParameterValueFromJsonString(LINE, LogAnalyserEnum.ID.getName());
    }
}
