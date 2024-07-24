package src.Readers.CourseRelatedReaders;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import java.util.ArrayList;
import java.util.List;

import src.CourseRelated.Semester;
import src.Readers.XMLNodeReader;


/**
 * Singleton class that reads a semester node and returns a Semester object
 */
public class SemesterReader implements XMLNodeReader {
    private static SemesterReader instance;

    private SemesterReader(){
        // does nothing, but we need this to be private
    }

    public Semester readNode(Node semesterNode){
        Semester semester = null;
        try{
            int period = Integer.parseInt(((Element) semesterNode).getAttribute("period"));
            NodeList childNodes = semesterNode.getChildNodes();
            List<String> disciplineIds = new ArrayList<>();
            for (int i = 0; i < childNodes.getLength(); i++){
                Node node = childNodes.item(i);
                if (node.getNodeType() != Node.ELEMENT_NODE)
                    continue;
                else if ("disciplineId".equals(node.getNodeName())){
                    disciplineIds.add(node.getTextContent().trim());
                }
                else{
                    throw new Error("Atributo inválido");
                }
            }
            if (period != 0 && !disciplineIds.isEmpty())
                semester = new Semester(period, disciplineIds);
            else
                throw new Error("Atributo faltando");
        }
        catch (Exception e){
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
            e.printStackTrace();
        }

        return semester;
    }

    public static SemesterReader getInstance(){
        if (instance == null)
            instance = new SemesterReader();
        return instance;
    }
}
