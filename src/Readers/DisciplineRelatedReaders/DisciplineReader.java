package src.Readers.DisciplineRelatedReaders;

import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import java.util.List;
import java.util.ArrayList;

import src.CourseRelated.Discipline;
import src.Readers.XMLNodeReader;
import src.Spaces.SpaceType;
import src.Spaces.InstituteAbbr;

/**
 * Singleton class that reads a discipline node and returns a Discipline object
 */
public class DisciplineReader implements XMLNodeReader {
    private static DisciplineReader instance;

    private DisciplineReader(){
        // does nothing, but we need this to be private
    }

    public Discipline readNode(Node courseNode){
        Discipline discipline = null;
        try{
            NodeList childNodes = courseNode.getChildNodes();
            String disciplineName = null;
            String disciplineId = null;
            int credits = 0;
            List<SpaceType> types = new ArrayList<>();
            List<InstituteAbbr> institutes = new ArrayList<>();
            List<String> professors = new ArrayList<>();
            for (int i = 0; i < childNodes.getLength(); i++){
                Node node = childNodes.item(i);
                if (node.getNodeType() != Node.ELEMENT_NODE)
                    continue;
                else if ("disciplineName".equals(node.getNodeName())){
                    disciplineName = node.getTextContent().trim();
                }
                else if ("disciplineId".equals(node.getNodeName())){
                    disciplineId = node.getTextContent().trim();
                }
                else if ("credits".equals(node.getNodeName())){
                    credits = Integer.parseInt(node.getTextContent().trim());
                }
                else if ("requiredSpaces".equals(node.getNodeName())){
                    NodeList spaceList = node.getChildNodes();
                    for (int j = 0; j < spaceList.getLength(); j++){
                        Node spaceNode = spaceList.item(j);
                        if (spaceNode.getNodeType() != Node.ELEMENT_NODE)
                            continue;
                        String text = spaceNode.getTextContent().trim();
                        switch (text) {
                            case "BasicRoom" -> types.add(SpaceType.BASIC_ROOM);
                            case "SlidesRoom" -> types.add(SpaceType.SLIDES_ROOM);
                            case "ComputerRoom" -> types.add(SpaceType.COMPUTER_ROOM);
                            case "Auditorium" -> types.add(SpaceType.AUDITORIUM);
                            case "Court" -> types.add(SpaceType.COURT);
                            case "PhysicsLaboratory" -> types.add(SpaceType.PHYSICS_LABORATORY);
                            case "ChemistryLaboratory" -> types.add(SpaceType.CHEMISTRY_LABORATORY);
                            case "EletronicsLaboratory" -> types.add(SpaceType.ELETRONICS_LABORATORY);
                            default -> throw new Error("Invalid space type");
                        }
                    }
                }
                else if ("possibleInstitutes".equals(node.getNodeName())){
                    NodeList institutesList = node.getChildNodes();
                    for (int j = 0; j < institutesList.getLength(); j++){
                        Node instituteNode = institutesList.item(j);
                        if (instituteNode.getNodeType() != Node.ELEMENT_NODE)
                            continue;
                        String text = instituteNode.getTextContent().trim();
                        switch (text) {
                            case "CB" -> institutes.add(InstituteAbbr.CB);
                            case "PB" -> institutes.add(InstituteAbbr.PB);
                            case "IC" -> institutes.add(InstituteAbbr.IC);
                            case "FEEC" -> institutes.add(InstituteAbbr.FEEC);
                            case "IMECC" -> institutes.add(InstituteAbbr.IMECC);
                            case "IFGW" -> institutes.add(InstituteAbbr.IFGW);
                            case "IEL" -> institutes.add(InstituteAbbr.IEL);
                            case "FEF" -> institutes.add(InstituteAbbr.FEF);
                            case "IE" -> institutes.add(InstituteAbbr.IE);
                            default -> throw new Error("Invalid institute");
                        }
                    }
                }
                else if ("professors".equals(node.getNodeName())){
                    NodeList professorsList = node.getChildNodes();
                    for (int j = 0; j < professorsList.getLength(); j++){
                        Node professorNode = professorsList.item(j);
                        if (professorNode.getNodeType() != Node.ELEMENT_NODE)
                            continue;
                        String professorStr = professorNode.getTextContent().trim();
                        professors.add(professorStr);
                    }
                }
                else{
                    throw new Error("Invalid atribute");
                }
            }
            if (disciplineName != null && disciplineId != null && credits != 0 &&
                    !types.isEmpty() && !institutes.isEmpty() && !professors.isEmpty())
                discipline = new Discipline(disciplineName, disciplineId, credits, types, institutes, professors);
            else
                throw new Error("Atribute missing");
        }
        catch (Exception e){
            System.err.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
        }

        return discipline;
    }

    public static DisciplineReader getInstance(){
        if (instance == null)
            instance = new DisciplineReader();
        return instance;
    }
}
