package src.Readers.CourseRelatedReaders;

import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import java.util.List;
import java.util.ArrayList;

import src.CourseRelated.Course;
import src.CourseRelated.Semester;
import src.Readers.XMLNodeReader;
import src.Schedule.Shift;

/**
 * Singleton class that reads a course node and returns a Course object
 */
public class CourseReader implements XMLNodeReader {
    private static CourseReader instance;

    private CourseReader(){
        // does nothing, but we need this to be private
    }

    public Course readNode(Node courseNode){
        Course course = null;
        try{
            NodeList childNodes = courseNode.getChildNodes();
            String courseName = null;
            int courseId = 0;
            Shift courseShift = null;
            List<Semester> semesters = new ArrayList<>();
            for (int i = 0; i < childNodes.getLength(); i++){
                Node node = childNodes.item(i);
                if (node.getNodeType() != Node.ELEMENT_NODE)
                    continue;
                else if ("courseName".equals(node.getNodeName())){
                    courseName = node.getTextContent().trim();
                }
                else if ("courseId".equals(node.getNodeName())){
                    courseId = Integer.parseInt(node.getTextContent().trim());
                }
                else if ("courseShift".equals(node.getNodeName())){
                    courseShift = Shift.valueOf(node.getTextContent().trim());
                }
                else if ("semesters".equals(node.getNodeName())){
                    NodeList semesterList = node.getChildNodes();
                    SemesterReader reader = SemesterReader.getInstance();
                    for (int j = 0; j < semesterList.getLength(); j++){
                        Node semesterNode = semesterList.item(j);
                        if (semesterNode.getNodeType() != Node.ELEMENT_NODE)
                            continue;
                        Semester semester = reader.readNode(semesterNode);
                        semesters.add(semester);
                    }
                }
                else{
                    throw new Error("Invalid atribute");
                }
            }
            if (courseId != 0 && courseName != null && !semesters.isEmpty() && courseShift != null)
                course = new Course(courseName, courseId, courseShift, semesters);
            else
                throw new Error("Atribute missing");
        }
        catch (Exception e){
            System.err.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
        }

        return course;
    }

    public static CourseReader getInstance(){
        if (instance == null)
            instance = new CourseReader();
        return instance;
    }
}
