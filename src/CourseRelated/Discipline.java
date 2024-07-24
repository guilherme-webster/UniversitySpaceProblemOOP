package src.CourseRelated;

import java.util.List;

import src.Spaces.InstituteAbbr;
import src.Spaces.SpaceType;

/**
 * Class that represents a university discipline
 */
public class Discipline {
    private String disciplineName;
    private String disciplineId;
    private int disciplineCredits;
    private List<SpaceType> requiredSpaces;
    private List<InstituteAbbr> possibleInstitutes;
    private List<String> professors;
    private boolean isMandatory = false;
    private int counterGroups = 0;
    private int counterProfessors = 0;

    /**
     * Public constructor for Discipline class
     * 
     * @param disciplineName: the name of the discipline
     * @param id: the id of the discipline
     * @param credits: the number of credits of the discipline
     * @param requiredSpaces: list of the required type of spaces
     * @param institutes: list of the possible institutes for the discipline occur
     * @param professors: list of the discipline professors
     */
    public Discipline(String disciplineName, String id, int credits, List<SpaceType> requiredSpaces,
                      List<InstituteAbbr> institutes, List<String> professors){
        this.disciplineName = disciplineName;
        this.disciplineId = id;
        this.disciplineCredits = credits;
        this.requiredSpaces = requiredSpaces;
        this.possibleInstitutes = institutes;
        this.professors = professors;
    }

    public String getDisciplineName(){
        return this.disciplineName;
    }

    public String getDisciplineId(){
        return this.disciplineId;
    }

    public int getDisciplineCredits(){
        return this.disciplineCredits;
    }

    public List<SpaceType> getRequiredSpaceType(){
        return this.requiredSpaces;
    }

    public List<InstituteAbbr> getPossibleInstitutes(){
        return this.possibleInstitutes;
    }

    public List<String> getProfessors(){
        return this.professors;
    }

    public boolean getIsMandatory(){
        return this.isMandatory;
    }

    public void setIsMandatory(boolean newObligation){
        this.isMandatory = newObligation;
    }

    /**
     * Calculates the discipline number of lectures in a week. Here,
     * we suppose that every discipline has an even number of credits,
     * and each lecture has 2 hours of duration.
     * 
     * @return: the number of lectures in a week
     */
    public int numberOfLectures(){
        return this.disciplineCredits / 2;
    }

    /**
     * Method that selects a professor in the professor's list. Each time this
     * method is called, the next professor in the list is selected. When the list
     * runs out of professors, the first one is selected again.
     * 
     * @return: a professor in the professor's list
     */
    public String selectProfessor(){
        if (counterProfessors >= professors.size())
            counterProfessors = 0;
        return professors.get(counterProfessors++);
    }

    /**
     * Method that keeps tracking the groups of the discipline. The first group
     * of the discipline receives the ID "A", the next one "B", and so on.
     * 
     * @return: The group's id of discipline
     */
    public char selectGroup(){
        return (char) ('A' + this.counterGroups++);
    }

    /**
     * Method that resets the counterGroups to zero
     */
    public void resetGroup(){
        this.counterGroups = 0;
    }

    @Override
    public String toString(){
        return (this.disciplineName + " (" + this.disciplineId + ").\n" +
                "Credits: " + this.disciplineCredits + "\n" +
                "Professors: " + String.join(", ", this.professors) + "\n");
    }
}
