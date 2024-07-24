package src.System;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import src.CourseRelated.Course;
import src.CourseRelated.Discipline;
import src.CourseRelated.Semester;
import src.CourseRelated.LectureRelated.ElectiveLecture;
import src.CourseRelated.LectureRelated.Lecture;
import src.CourseRelated.LectureRelated.MandatoryLecture;
import src.Readers.CourseRelatedReaders.CoursesFileReader;
import src.Schedule.*;
import src.Spaces.Space;

/**
 * Class responsible for create lectures and allocate schedules to them.
 */
public class ScheduleAllocator {

    /**
     * Static method responsible for create lectures and allocate schedules to them
     * 
     * @param allCourses: a list of all courses
     * @param allDisciplines: a list of all disciplines (mandatory and electives)
     * @return: a list of lectures with schedules allocated, but spaces not defined yet
     */
    public static List<Lecture> createLecturesWithSchedules(List<Course> allCourses, List<Discipline> allDisciplines){
        return createLectures(allDisciplines);
    }

    /**
     * Method responsible for create lectures with schedules, but no places yet
     *
     * @param allDisciplines: a list of all disciplines
     * @return: a list of lectures with schedule assigned, but place set to null  
     */
    private static List<Lecture> createLectures(List<Discipline> allDisciplines){
        List<Lecture> allLectures = new ArrayList<>();
        List<Course> allCourses = CoursesFileReader.getInstance().readFile("src/XML/courses.xml");
        for (Course course : allCourses){
            for (Semester semester : course.getCourseSemesters()){
                List<Lecture> semesterLectures = createSemesterMandatoryLectures(semester, course, allDisciplines);
                allLectures.addAll(semesterLectures);
            }
        }

        List<Discipline> electiveDisciplines = ScheduleAllocator.filterElectiveDisciplines(allDisciplines);
        List<Lecture> electiveLectures = ScheduleAllocator.createElectiveLectures(electiveDisciplines);
        allLectures.addAll(electiveLectures);

        return allLectures;
    }

    /**
     * Method responsible for create the mandatory lectures of a course's semester.
     * 
     * @param semester: the semester 
     * @param course: the course
     * @param allDisciplines: list of all the disciplines available
     * @return: a list of lectures with schedules, but space is set to null 
     */
    private static List<Lecture> createSemesterMandatoryLectures(Semester semester, Course course, List<Discipline> allDisciplines){
        List<Lecture> semesterLectures = new ArrayList<>();
        for (String disciplineID : semester.getDisciplineIDs()){
            Discipline discipline = findDiscipline(disciplineID, allDisciplines);

            // If the discipline is from a course semester, it must be mandatory
            discipline.setIsMandatory(true);

            List<Lecture> mandatoryLectures = createMandatoryDisciplineLectures(discipline, course);
            semesterLectures.addAll(mandatoryLectures);
        }

        assignMandatorySemesterSchedules(semesterLectures, course.getCourseShift());
        return semesterLectures;
    }

    /**
     * Create the number of lectures needed for a mandatory discipline. Assign schedules to them,
     * but not places yet
     * 
     * @param discipline: the discipline
     * @param course: the course that contains the discipline
     * @return: a list of lectures
     */
    private static List<Lecture> createMandatoryDisciplineLectures(Discipline discipline, Course course){
        List<Lecture> disciplineLectures = new ArrayList<>();
        int numberOfLectures = discipline.numberOfLectures();
        String professor = discipline.selectProfessor();
        char group = discipline.selectGroup();
        
        for (int i = 0; i < numberOfLectures; i++){
            Space space = new Space (null, discipline.getRequiredSpaceType().get(i), null);
            MandatoryLecture lecture= new MandatoryLecture(space, discipline, null, professor, group, course);
            disciplineLectures.add(lecture);
        }
        return disciplineLectures;
    }

    /**
     * Assign schedules to the disciplines of a semester
     * 
     * @param semesterLectures: list of semester lectures, without schedules
     * @param courseShift: the shift of the course
     */
    private static void assignMandatorySemesterSchedules(List<Lecture> semesterLectures, Shift courseShift){
        List<Lecture> lecturesCopy = new ArrayList<>(semesterLectures);

        if (courseShift == Shift.FULL_TIME){
            int numNull = 20 - semesterLectures.size();
            for (int i = 0; i < numNull; i++)
                lecturesCopy.add(null);
        }
        else if (courseShift == Shift.NIGHT_SHIFT){
            int numNull = 10 - semesterLectures.size();
            for (int i = 0; i < numNull; i++)
                lecturesCopy.add(null);
        }

        Collections.shuffle(lecturesCopy);
        LectureSchedule schedule = LectureSchedule.firstSchedule(courseShift);
        
        for (int i = 0; i < lecturesCopy.size(); i++){
            Lecture lecture = lecturesCopy.get(i);
            if (lecture == null){
                if( i < lecturesCopy.size() - 1)
                    schedule = LectureSchedule.nextSchedule(schedule);
                continue;
            }
            lecture.setLectureSchedule(schedule);
            if (i < lecturesCopy.size() - 1)
                schedule = LectureSchedule.nextSchedule(schedule);
        }
    }

    /**
     * Creates elective lectures, without places assigned
     * 
     * @param electiveDisciplines: the list of elective disciplines
     * @return: a list of the created elective lectures
     */
    private static List<Lecture> createElectiveLectures(List<Discipline> electiveDisciplines){
        List<Lecture> electiveLectures = new ArrayList<>();
        for(Discipline discipline : electiveDisciplines){
            int numberOfLectures = discipline.numberOfLectures();
            String professor = discipline.selectProfessor();
            char group = discipline.selectGroup();
            List<Integer> daysIndexes = new ArrayList<>();
            List<Integer> hoursIndexes = new ArrayList<>();
            
            for (int i = 0; i < numberOfLectures; i++){
                Space space = new Space(null, discipline.getRequiredSpaceType().get(i), null);
                LectureSchedule schedule = generateElectiveSchedule(daysIndexes, hoursIndexes);
                ElectiveLecture lecture = new ElectiveLecture(space, discipline, schedule,
                                            professor, group);
                electiveLectures.add(lecture);

            }
        }
        return electiveLectures;
    }

    /**
     * Method that receives a discipline ID and gives the corresponding discipline object.
     * If it doesn't find, throws an error
     * 
     * @param disciplineID: the desired discipline ID 
     * @param allDisciplines: a list of all the disciplines available
     * @return: the corresponding discipline
     */
    private static Discipline findDiscipline(String disciplineID, List<Discipline> allDisciplines){
        for (Discipline discipline : allDisciplines){
            if (discipline.getDisciplineId().equals(disciplineID))
                return discipline;
        }
        throw new Error("Discipline " + disciplineID + "not found");
    }

    /**
     * Given a list of disciplines, filters those which are electives
     * 
     * @param allDisciplines: a list of disciplines
     * @return: a list of the electives disciplines in the list
     */
    private static List<Discipline> filterElectiveDisciplines(List<Discipline> allDisciplines){
        List<Discipline> electiveDisciplines = new ArrayList<>();

        for(Discipline candidateDiscipline : allDisciplines){
            if(!candidateDiscipline.getIsMandatory()){
                electiveDisciplines.add(candidateDiscipline);
            }
        }

        return electiveDisciplines;
    }

    /**
     * Generates a schedule for an elective class, without repetition
     * 
     * @param dayIndexes: list of ints, representing the days you already have classes for this elective 
     * @param hourIndexes: list of ints, representing the hours you already have classes for this elective
     * @return: a random new schedule (day + hour) for this elective
     */
    private static LectureSchedule generateElectiveSchedule(List<Integer> dayIndexes, List<Integer> hourIndexes){
        Random random = new Random();
        int dayIndex = random.nextInt(5);
        int hourIndex = random.nextInt(6);
        boolean didAdd = false;

        while(!didAdd){
            if(!dayIndexes.contains(dayIndex) || !hourIndexes.contains(hourIndex)){
                if(!dayIndexes.contains(dayIndex))
                    dayIndexes.add(dayIndex);
                if(!hourIndexes.contains(hourIndex))
                    hourIndexes.add(hourIndex);

                didAdd = true;
            } 
            else {
                if(dayIndexes.contains(dayIndex))
                    dayIndex = random.nextInt(5);
                if(hourIndexes.contains(hourIndex))
                    hourIndex = random.nextInt(6);
            }
        }
        
        return new LectureSchedule(WeekDay.get(dayIndex), HourOfClass.get(hourIndex));
    }
}
