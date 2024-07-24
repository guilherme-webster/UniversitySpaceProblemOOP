package src.CourseRelated.LectureRelated;

import src.CourseRelated.Discipline;
import src.Schedule.LectureSchedule;
import src.Spaces.Space;

/**
 * Class that represents a lecture of an elective discipline
 */
public class ElectiveLecture extends Lecture{
    public ElectiveLecture(Space space, Discipline discipline, LectureSchedule schedule, String professor, char group){
        super(space, discipline, schedule, professor, group);
    }
}
