package src.Readers.CourseRelatedReaders;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.util.ArrayList;
import java.util.List;

import src.CourseRelated.Course;
import src.Readers.XMLFileReader;


/**
 * Singleton class that reads an XML file of courses
 */
public class CoursesFileReader implements XMLFileReader {
    private static CoursesFileReader instance;

    private CoursesFileReader(){
        // does nothing, but we need this to be private
    }

    @Override
    public List<Course> readFile(String path){
        List<Course> courses = new ArrayList<>();
        try{
            File file = new File(path);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("course");

            for (int i = 0; i < nodeList.getLength(); i++){
                Node courseNode = nodeList.item(i);
                if (courseNode.getNodeType() != Node.ELEMENT_NODE)
                    continue;
                CourseReader reader = CourseReader.getInstance();
                Course course = reader.readNode(courseNode);
                courses.add(course);
            }
        }
        catch (Exception e){
            System.err.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
        }

        return courses;
    }

    public static CoursesFileReader getInstance(){
        if (instance == null)
            instance = new CoursesFileReader();
        return instance;
    }
}