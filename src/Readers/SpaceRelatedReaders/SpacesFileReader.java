package src.Readers.SpaceRelatedReaders;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.util.ArrayList;
import java.util.List;

import src.Spaces.*;
import src.Readers.XMLFileReader;


/**
 * Singleton class that reads an XML file of spaces
 */
public class SpacesFileReader implements XMLFileReader {
    private static SpacesFileReader instance;

    private SpacesFileReader(){
        // does nothing, but we need this to be private
    }

    @Override
    public List<Space> readFile(String path){
        List<Space> spaces = new ArrayList<>();
        try{
            File file = new File(path);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getDocumentElement().getChildNodes();

            for (int i = 0; i < nodeList.getLength(); i++){
                Node spaceNode = nodeList.item(i);
                if (spaceNode.getNodeType() != Node.ELEMENT_NODE)
                    continue;
                else{
                    SpaceReader reader = SpaceReader.getInstance();
                    Space space = reader.readNode(spaceNode);
                    spaces.add(space);
                }   
            }
        }
        catch (Exception e){
            System.err.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
        }

        return spaces;
    }

    public static SpacesFileReader getInstance(){
        if (instance == null)
            instance = new SpacesFileReader();
        return instance;
    }
}
