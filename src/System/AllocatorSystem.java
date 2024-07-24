package src.System;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.application.Platform;
import src.CourseRelated.Course;
import src.CourseRelated.Discipline;
import src.CourseRelated.LectureRelated.*;
import src.Errors.*;
import src.GraphicInterface.Controllers.SelectionController;
import src.Spaces.Space;
import src.Spaces.SpaceType;

/**
 * System class responsible for the allocation of schedules and places.
 */
public class AllocatorSystem {
    private List<Course> allCourses;
    private List<Discipline> allDisciplines;
    private List<Space> allSpaces;
    private HashMap<SpaceType, Integer> errorsPerType;
    private final int MAX_NUMBER_OF_TRIES = 10000;


    /**
     * Public constructor for the AllocatorSystem class
     * 
     * @param allCourses: list of all courses
     * @param allDisciplines: list of all disciplines
     * @param allSpaces: list of all spaces available
     */
    public AllocatorSystem(List<Course> allCourses, List<Discipline> allDisciplines, List<Space> allSpaces){
        this.allCourses = allCourses;
        this.allDisciplines = allDisciplines;
        this.allSpaces = allSpaces;

        // Sets the errors for each space type equals to zero and stores in a map
        this.errorsPerType = new HashMap<>();
        for (Space space : allSpaces){
            SpaceType spaceType = space.getSpaceType();
            if (!errorsPerType.containsKey(spaceType))
                errorsPerType.put(spaceType, 0);
        }
    }

    /**
     * Method that allocates schedules and places. 
     * 
     * @return: a list of all lectures, each one with a schedule and a place
     */
    public List<Lecture> allocateSchedulesAndSpaces(){
        List<Lecture> allLectures = null;
        boolean mustContinue = true;
        while (mustContinue) {
            try {
                allLectures = ScheduleAllocator.createLecturesWithSchedules(allCourses, allDisciplines);
                SpaceAllocator.assignPlaces(allSpaces, allLectures);
                mustContinue = false;
            }
            catch (InsufficientSpacesError e){
                Pattern INSUFFICIENT_SPACES = Pattern.compile("Insufficent spaces of type (.+)");
                Matcher matcher1 = INSUFFICIENT_SPACES.matcher(e.getMessage());
                if (matcher1.find()){
                    String spaceTypeString = matcher1.group(1);
                    SpaceType spaceType = SpaceType.valueOf(spaceTypeString);
                    mustContinue = continueTheLoop(spaceType);
                    if (!mustContinue)
                        throw e;
                    
                    allLectures = null;
                    for (Discipline discipline : allDisciplines)
                        discipline.resetGroup();
                }
            }
        }

        return allLectures;
    }

    /**
     * Defines if the system should try to allocate schedules and places again.
     * If the system already got the same error more than MAX_NUMBER_OF_TRIES times,
     * it returns false. Otherwise, it returns true.
     * 
     * @param spaceType: the SpaceType that generates the error
     * @return: true if it must continue the loop, otherwise false
     */
    private boolean continueTheLoop(SpaceType spaceType){
        int errorsInThisType = this.errorsPerType.get(spaceType);
        errorsPerType.put(spaceType, ++errorsInThisType);
        if (errorsInThisType >= this.MAX_NUMBER_OF_TRIES) {
            Platform.runLater(() -> SelectionController.errorStage("Insufficient spaces of type " + spaceType));
            return false;
        }
        else {
            Platform.runLater(() -> SelectionController.increaseAttempts());
            return true;
        }
    }
}
