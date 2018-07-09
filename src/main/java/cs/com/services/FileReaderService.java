package cs.com.services;

import org.apache.commons.io.LineIterator;


public interface FileReaderService {
    void search(String filePath, String mainJson);
    void executeIteration(String mainJson, LineIterator iterator);
}
