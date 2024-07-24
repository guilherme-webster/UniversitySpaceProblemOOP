package src.Readers;

import java.util.List;

/**
 * Interface for the XML file readers
 */
public interface XMLFileReader {
    public List<?> readFile(String path);
}
