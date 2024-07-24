package src.CourseRelated.LectureRelated;

import src.CourseRelated.Discipline;
import src.Schedule.LectureSchedule;
import src.Spaces.Space;

/**
 * Class that represents a lecture
 */
public abstract class Lecture{
    private Space lectureSpace;
    private Discipline lectureDiscipline;
    private LectureSchedule lectureSchedule;
    private String professor;
    private char group;

    /**
     * Public constructor for the Lecture class
     * 
     * @param space: where the lecture occurs
     * @param discipline: the discipline of the lecture
     * @param schedule: day and hour of the lecture
     * @param professor: teacher of the lecture
     * @param group: lecture group
     */
    public Lecture(Space space, Discipline discipline, LectureSchedule schedule, String professor, char group){
        this.lectureSpace = space;
        this.lectureDiscipline = discipline;
        this.lectureSchedule = schedule;
        this.professor = professor;
        this.group = group;
    }

    public Space getLectureSpace(){
        return this.lectureSpace;
    }

    public void setLectureSpace(Space newSpace){
        this.lectureSpace = newSpace;
    }

    public Discipline getLectureDiscipline(){
        return this.lectureDiscipline;
    }

    public LectureSchedule getLectureSchedule(){
        return this.lectureSchedule;
    }

    public void setLectureSchedule(LectureSchedule schedule){
        this.lectureSchedule = schedule;
    }

    public String getProfessor(){
        return this.professor;
    }

    public char getLectureGroup(){
        return this.group;
    }
}
