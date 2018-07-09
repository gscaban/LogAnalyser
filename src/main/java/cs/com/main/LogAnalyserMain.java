package cs.com.main;

import cs.com.common.HibernateUtil;
import cs.com.enums.LogAnalyserEnum;
import cs.com.exception.LogAnalyserException;
import cs.com.services.*;
import org.apache.commons.io.LineIterator;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;


public class LogAnalyserMain {
    final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(LogAnalyserMain.class);
    private FileReaderService fileReaderService;
    private JsonAnalyserService jsonAnalyserService;
    private HsqlService hsqlService;
    private String filePath;


    public LogAnalyserMain(String filePath) {
        this.fileReaderService = new FileReaderServiceImpl();
        this.jsonAnalyserService = new JsonAnalyserServiceImpl();
        this.hsqlService = new HsqlServiceImpl();
        this.filePath = filePath;
    }

    public LogAnalyserMain(JsonAnalyserService jsonAnalyserService, HsqlService hsqlService, FileReaderService fileReaderService) {
        this.fileReaderService = fileReaderService;
        this.jsonAnalyserService = jsonAnalyserService;
        this.hsqlService = hsqlService;
    }

    public void startAnalysingLogFile() {
        File file = new File(filePath);
        LineIterator iterator = null;
        try {
            FileReader fileReader = new FileReader(file);
            iterator = new LineIterator(fileReader);
            logger.info("Started reading file : "+file.getName());
            logger.info("Please wait ...");
            long startTime = System.nanoTime();
            executeIteration(filePath, iterator);
            long endTime = System.nanoTime();
            long rowCount = hsqlService.getAlertEventsRowCount();
            logger.info("Finished reading file : "+filePath);
            logger.info("Found : "+rowCount+" alerts in the log file.");
            logger.info("Execution time: " + TimeUnit.NANOSECONDS.toSeconds(endTime - startTime) +" sec.");
        } catch (FileNotFoundException e) {
            logger.error("Cannot open file "+filePath, e);
        } finally {
            LineIterator.closeQuietly(iterator);
            HibernateUtil.shutdown();
        }
    }

    public void executeIteration(String filePath, LineIterator iterator){
        while (iterator.hasNext()) {
            String line = iterator.nextLine();
            String id = null;
            try {
                id = jsonAnalyserService.getStringParameterValueFromJsonString(line, LogAnalyserEnum.ID.getName());
            } catch (LogAnalyserException e) {
                String cause = e.toString();
                if (!hsqlService.isJsonErrorInDB(e.getJson())) hsqlService.saveJsonError(e.getJson(), cause);
                continue;
            }
            if (!hsqlService.isAlertInDB(id)) this.fileReaderService.search(filePath, line);
        }
    }

    public static void main(String[] args) {
        LogAnalyserMain logAnalyserMain = new LogAnalyserMain(args[0]);
        logAnalyserMain.startAnalysingLogFile();
    }

}
