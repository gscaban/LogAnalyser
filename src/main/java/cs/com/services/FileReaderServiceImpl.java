package cs.com.services;

import cs.com.enums.LogAnalyserEnum;
import cs.com.exception.LogAnalyserException;
import org.apache.commons.io.LineIterator;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class FileReaderServiceImpl implements FileReaderService{
    private JsonAnalyserService jsonAnalyserService;
    private HsqlService hsqlService;
    final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(FileReaderServiceImpl.class);

    public FileReaderServiceImpl() {
        this.jsonAnalyserService = new JsonAnalyserServiceImpl();
        this.hsqlService = new HsqlServiceImpl();
    }

    public FileReaderServiceImpl(JsonAnalyserService jsonAnalyserService, HsqlService hsqlService) {
        this.jsonAnalyserService = jsonAnalyserService;
        this.hsqlService = hsqlService;
    }

    @Override
    public void search(String filePath, String mainJson) {
        LineIterator iterator = null;
        try {
            File file = new File(filePath);
            FileReader fileReader = new FileReader(file);
            iterator = new LineIterator(fileReader);
            executeIteration(mainJson, iterator);
        } catch (FileNotFoundException e) {
            logger.error("Cannot open file "+filePath,e);
        } finally {
            LineIterator.closeQuietly(iterator);
        }
    }

    @Override
    public void executeIteration(String mainJson, LineIterator iterator) {
        while (iterator.hasNext()) {
            String line = iterator.nextLine();
            boolean isMainJsonStarted = false;
            try {
                isMainJsonStarted = isStarted(mainJson);
                long period = isMainJsonStarted ? jsonAnalyserService.compareJsonsAndCalculateTimePeriod(mainJson, line) :
                        jsonAnalyserService.compareJsonsAndCalculateTimePeriod(line, mainJson);
                if (period > 4) {
                    saveToDB(mainJson, period, true);
                    break;
                } else if (period >= 0 && period <= 4) {
                    saveToDB(mainJson, period, false);
                    break;
                }
            } catch (LogAnalyserException e) {
                String cause = e.toString();
                if (!hsqlService.isJsonErrorInDB(e.getJson())) hsqlService.saveJsonError(e.getJson(), cause);
                continue;
            }
        }
    }

    private boolean isStarted(String json) throws LogAnalyserException {
        return jsonAnalyserService.getStringParameterValueFromJsonString(json, LogAnalyserEnum.STATE.getName()).equals(LogAnalyserEnum.STARTED.getName());
    }

    private void saveToDB(String json, long period, boolean alert) throws LogAnalyserException {
        hsqlService.saveAlertEvent(jsonAnalyserService.getStringParameterValueFromJsonString(json, LogAnalyserEnum.ID.getName()),
                jsonAnalyserService.getStringParameterValueFromJsonString(json, LogAnalyserEnum.TYPE.getName()),
                jsonAnalyserService.getStringParameterValueFromJsonString(json, LogAnalyserEnum.HOST.getName()),
                period, alert);
    }
}
