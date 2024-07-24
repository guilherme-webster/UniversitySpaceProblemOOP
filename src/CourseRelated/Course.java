package src.CourseRelated;

import java.util.List;

import src.Schedule.Shift;

/**
 * A class that represents a university course
 */
public class Course {
    private String courseName;
    private int courseId;
    private Shift courseShift;
    private List<Semester> courseSemesters;
    
    /**
     * Public constructor for Course class
     * 
     * @param courseName: the name of the course
     * @param courseId: the id of the course
     * @param courseShift: the shift of the course (FULL_TIME) or (NIGHT_SHIFT)
     * @param semesters: list of the course semesters
     */
    public Course(String courseName, int courseId, Shift courseShift, List<Semester> semesters){
        this.courseName = courseName;
        this.courseId = courseId;
        this.courseShift = courseShift;
        this.courseSemesters = semesters;
    }

    public String getCourseName(){
        return this.courseName;
    }

    public int getCourseId(){
        return this.courseId;
    }

    public Shift getCourseShift(){
        return this.courseShift;
    }

    public List<Semester> getCourseSemesters(){
        return this.courseSemesters;
    }

    public int getDisciplineSemester(Discipline discipline){
        for (Semester semester : this.courseSemesters){
            for (String disciplineID : semester.getDisciplineIDs()){
                if (disciplineID.equals(discipline.getDisciplineId()))
                    return semester.getSemesterPeriod();
            }
        }
        throw new Error("Discipline not found");
    }

    public static int compare(Course course1, Course course2){
        int id1 = course1.getCourseId();
        int id2 = course2.getCourseId();
        return id1 - id2;
    }

    @Override
    public String toString(){
        StringBuilder str = new StringBuilder(courseName + " (" + courseId + ")\n");
        for (Semester semester : this.courseSemesters)
            str.append(semester.toString());
        return str.toString();
    }
}
