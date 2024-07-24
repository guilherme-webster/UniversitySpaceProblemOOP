package src.Spaces;

/**
 * Class that represents a space in a university
 */
public class Space {
    private String spaceID;
    private SpaceType spaceType;
    private InstituteAbbr institute;

    /**
     * Public class for the Space class
     * 
     * @param spaceID: the id of the space
     * @param spaceType: the type of the space
     * @param institute: the institute of the space
     */
    public Space(String spaceID, SpaceType spaceType, InstituteAbbr institute){
        this.spaceID = spaceID;
        this.spaceType = spaceType;
        this.institute = institute;
    }

    public String getSpaceID(){
        return this.spaceID;
    }

    public SpaceType getSpaceType(){
        return this.spaceType;
    }

    public InstituteAbbr getInstitute(){
        return this.institute;
    }

    @Override
    public String toString(){
        return spaceID;
    }    
}
