package src.Readers;

import org.w3c.dom.Node;

/**
 * Interface for the XML node readers
 */
public interface XMLNodeReader {
    public Object readNode(Node node);
}
