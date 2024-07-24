package src.Readers.DisciplineRelatedReaders;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import java.util.ArrayList;
import java.util.List;

import src.CourseRelated.Discipline;
import src.Readers.XMLFileReader;

/**
 * Singleton class that reads a discipline XML file
 */
public class DisciplinesFileReader implements XMLFileReader {
    private static DisciplinesFileReader instance;

    private DisciplinesFileReader(){
        // does nothing, but we need this to be private
    }

    public List<Discipline> readFile(String path){
        List<Discipline> disciplines = new ArrayList<>();
        try{
            File file = new File(path);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("discipline");

            for (int i = 0; i < nodeList.getLength(); i++){
                Node disciplineNode = nodeList.item(i);
                DisciplineReader reader = DisciplineReader.getInstance();
                Discipline discipline = reader.readNode(disciplineNode);
                disciplines.add(discipline);
            }
        }
        catch (Exception e){
            System.err.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
        }

        return disciplines;
    }

    public static DisciplinesFileReader getInstance(){
        if (instance == null)
            instance = new DisciplinesFileReader();
        return instance;
    }
}
