package src.System;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jgrapht.Graph;
import org.jgrapht.alg.color.GreedyColoring;
import org.jgrapht.alg.interfaces.VertexColoringAlgorithm.Coloring;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import src.CourseRelated.Discipline;
import src.CourseRelated.LectureRelated.Lecture;
import src.Errors.InsufficientSpacesError;
import src.Errors.NoSpacesAvailableError;
import src.Spaces.InstituteAbbr;
import src.Spaces.Space;
import src.Spaces.SpaceType;

/**
 * Class responsible for allocating spaces for the lectures
 */
public class SpaceAllocator {

    /**
     * Assign places to the lectures. Two lectures in the same schedule cannot
     * occur in the same place.
     * 
     * @param allSpaces: list of all spaces available
     * @param allLectures: list of all lectures created, without places assigned
     */
    public static void assignPlaces(List<Space> allSpaces, List<Lecture> allLectures) {
        Map<SpaceType, List<Lecture>> separatedLectures = separateLecturesBySpaceRequirement(allLectures);
        Map<SpaceType, List<Space>> separatedSpaces = separateSpacesByType(allSpaces);

        for (Map.Entry<SpaceType, List<Lecture>> mapTypeLecture : separatedLectures.entrySet()) {
            SpaceType spaceType = mapTypeLecture.getKey();
            List<Lecture> filteredLectures = mapTypeLecture.getValue();
            List<Space> spacesOfType = separatedSpaces.get(spaceType);

            if (spacesOfType == null)
                throw new NoSpacesAvailableError("No spaces of type " + spaceType);

            Map<InstituteAbbr, List<Space>> spacesByInstitute = separateSpacesByInstitute(spacesOfType);

            for (Lecture lecture : filteredLectures) {
                Discipline discipline = lecture.getLectureDiscipline();
                List<Space> possibleSpaces = discipline.getPossibleInstitutes().stream()
                        .flatMap(inst -> spacesByInstitute.getOrDefault(inst, new ArrayList<>()).stream())
                        .collect(Collectors.toList());

                if (possibleSpaces.isEmpty())
                    throw new NoSpacesAvailableError("No spaces available for discipline " + discipline.getDisciplineName() + " in the required institute");

                assignPlacesPerType(possibleSpaces, filteredLectures, spaceType);
            }
        }
    }
    
    /**
     * Assign the spaces for the filtered lectures
     *
     * @param spacesOfType: a list of the spaces of the type
     * @param filteredLectures: list of lectures that requires that type of space
     * @param type:             the type of the space
     */
    private static void assignPlacesPerType(List<Space> spacesOfType, List<Lecture> filteredLectures, SpaceType type) {
        Graph<Lecture, DefaultEdge> lecturesGraph = createLecturesGraph(filteredLectures);
        Coloring<Lecture> coloring = coloringLecturesGraph(lecturesGraph);
        assignPlacesOfType(spacesOfType, coloring, type);
    }

    /**
     * Create a graph, which vertices are lectures and the edges bounds lectures with same schedule
     * 
     * @param filteredLectures: list of filtered lectures
     * @return: the graph created
     */
    private static Graph<Lecture, DefaultEdge> createLecturesGraph(List<Lecture> filteredLectures){
        Graph<Lecture, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);

        for (Lecture lecture : filteredLectures)
            graph.addVertex(lecture);

        for (int i = 0; i < filteredLectures.size(); i++) {
            for (int j = i + 1; j < filteredLectures.size(); j++) {
                if (filteredLectures.get(i).getLectureSchedule().equals(filteredLectures.get(j).getLectureSchedule()))
                    graph.addEdge(filteredLectures.get(i), filteredLectures.get(j));
            }
        }

        return graph;
    }

    /**
     * Colors the graph, giving different colors to lectures that can't be in the same place
     * 
     * @param lecturesGraph: the graph of lectures
     * @return: the coloring of the graph
     */
    private static Coloring<Lecture> coloringLecturesGraph(Graph<Lecture, DefaultEdge> lecturesGraph) {
        GreedyColoring<Lecture, DefaultEdge> coloring = new GreedyColoring<>(lecturesGraph);
        return coloring.getColoring();
    }


    /**
     * Assign places to the lectures. If the spaces are insufficient or if there are no
     * spaces of a type, an error is thrown.
     *
     * @param spacesOfType: a list of the spaces of a specific type
     * @param coloring:     the coloring created for the graph
     * @param type:         the type of the spaces
     */
    private static void assignPlacesOfType(List<Space> spacesOfType, Coloring<Lecture> coloring, SpaceType type) {
        if (spacesOfType.isEmpty())
            throw new NoSpacesAvailableError("No spaces of type " + type);

        Map<Integer, Space> mapColorSpace = new HashMap<>();
        for (int i = 0; i < spacesOfType.size(); i++)
            mapColorSpace.put(i, spacesOfType.get(i));

        Map<Lecture, String> mapLectureColor = new HashMap<>();
        for (Map.Entry<Lecture, Integer> entry : coloring.getColors().entrySet()) {
            Lecture lecture = entry.getKey();
            Integer color = entry.getValue();

            if (color >= spacesOfType.size())
                throw new InsufficientSpacesError("Insufficent spaces of type " + spacesOfType.getFirst().getSpaceType());

            Space space = mapColorSpace.get(color);
            lecture.setLectureSpace(space);
            mapLectureColor.put(lecture, space.getSpaceID());
        }
    }

    /**
     * Separate the lectures by required spaceType and stores in a hash map
     * 
     * @param allLectures: the list of lectures
     * @return: the map with the lectures separated by required space type
     */
    private static Map<SpaceType, List<Lecture>> separateLecturesBySpaceRequirement(List<Lecture> allLectures) {
        Map<SpaceType, List<Lecture>> separatedLectures = new HashMap<>();
        for (Lecture lecture : allLectures) {
            SpaceType requiredSpaceType = lecture.getLectureSpace().getSpaceType();
            separatedLectures.computeIfAbsent(requiredSpaceType, k -> new ArrayList<>()).add(lecture);
        }
        return separatedLectures;
    }

    /**
     * Separate the spaces by required spaceType and stores in a hash map
     * 
     * @param spaceList: the list of available spaces
     * @return: the map with the spaces separated by type
     */
    private static Map<SpaceType, List<Space>> separateSpacesByType(List<Space> spaceList) {
        Map<SpaceType, List<Space>> separatedSpaces = new HashMap<>();
        for (Space space : spaceList) {
            SpaceType spaceType = space.getSpaceType();
            separatedSpaces.computeIfAbsent(spaceType, k -> new ArrayList<>()).add(space);
        }
        return separatedSpaces;
    }

    /**
     * Separate the spaces by institute and stores in a hash map
     * 
     * @param spaceList: the list of available spaces
     * @return: the map with the spaces separated by institutes
     */
    private static Map<InstituteAbbr, List<Space>> separateSpacesByInstitute(List<Space> spaceList) {
        Map<InstituteAbbr, List<Space>> separatedSpaces = new HashMap<>();
        for (Space space : spaceList) {
            InstituteAbbr institute = space.getInstitute();
            separatedSpaces.computeIfAbsent(institute, k -> new ArrayList<>()).add(space);
        }
        return separatedSpaces;
    }
}
