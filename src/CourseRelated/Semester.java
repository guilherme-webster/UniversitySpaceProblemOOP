package src.CourseRelated;
import java.util.List;


/**
 * Class that represents a semester of a university course
 */
public class Semester {
    private int semesterPeriod;
    private List<String> disciplines;

    /**
     * Public constructor for the Semester class
     * 
     * @param semesterPeriod: the number of the semester in the course
     * @param disciplines: the list of disciplines in the semester
     */
    public Semester(int semesterPeriod, List<String> disciplines){
        this.semesterPeriod = semesterPeriod;
        this.disciplines = disciplines;
    }

    public int getSemesterPeriod(){
        return this.semesterPeriod;
    }

    public List<String> getDisciplineIDs(){
        return this.disciplines;
    }

    @Override
    public String toString(){
        return ("Semester " + this.semesterPeriod + " list of disciplines: "
                 + String.join(", ", this.disciplines) + "\n");
    }
}
