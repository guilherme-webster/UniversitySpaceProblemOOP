package src.CourseRelated.LectureRelated;

import java.util.Comparator;

import src.CourseRelated.Course;
import src.Schedule.LectureSchedule;

/**
 * Class responsible for compare Lecture objects
 */
public class LectureComparator implements Comparator<Lecture> {
    @Override
    public int compare(Lecture lecture1, Lecture lecture2){
        LectureSchedule schedule1 = lecture1.getLectureSchedule();
        LectureSchedule schedule2 = lecture2.getLectureSchedule();
        
        if (lecture1 instanceof ElectiveLecture && lecture2 instanceof ElectiveLecture) {
            // If both courses are null, compare the schedules
            return LectureSchedule.compare(schedule1, schedule2);
        } else if (lecture1 instanceof ElectiveLecture) {
            // If only course1 is null, put it in the end
            return 1;
        } else if (lecture2 instanceof ElectiveLecture) {
            // If only course2 is null, put it in the end
            return -1;
        } else {

            Course course1 = ((MandatoryLecture) lecture1).getLectureCourse();
            Course course2 = ((MandatoryLecture) lecture2).getLectureCourse();

            int semester1 = course1.getDisciplineSemester(lecture1.getLectureDiscipline());
            int semester2 = course2.getDisciplineSemester(lecture2.getLectureDiscipline());

            if (lecture1.getLectureSchedule().equals(lecture2.getLectureSchedule()))
                return 0;
            else if (Course.compare(course1, course2) > 0 ||
                     Course.compare(course1, course2) == 0 && semester1 > semester2 ||
                     Course.compare(course1, course2) == 0 && semester1 == semester2 && LectureSchedule.compare(schedule1, schedule2) > 0)
                return 1;
            else
                return -1;

        }
    }
}
