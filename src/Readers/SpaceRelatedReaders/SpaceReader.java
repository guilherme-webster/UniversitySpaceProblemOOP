package src.Readers.SpaceRelatedReaders;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import src.Spaces.*;
import src.Readers.XMLNodeReader;

/**
 * Singleton class that reads a space node and returns a space object
 */
public class SpaceReader implements XMLNodeReader {
    private static SpaceReader instance;

    private SpaceReader(){
        // does nothing, but we need this to be private
    }

    public Space readNode(Node spaceNode){
        Space space = null;
        try{
            NodeList childNodes = spaceNode.getChildNodes();
            String id = null;
            SpaceType type = null;
            InstituteAbbr institute = null;
            for (int i = 0; i < childNodes.getLength(); i++){
                Node node = childNodes.item(i);
                if (node.getNodeType() != Node.ELEMENT_NODE)
                    continue;
                else if ("type".equals(node.getNodeName())){
                    String text = node.getTextContent().trim();
                    type = switch (text) {
                        case "basicRoom" -> SpaceType.BASIC_ROOM;
                        case "slidesRoom" -> SpaceType.SLIDES_ROOM;
                        case "computerRoom" -> SpaceType.COMPUTER_ROOM;
                        case "physicsLaboratory" -> SpaceType.PHYSICS_LABORATORY;
                        case "chemistryLaboratory" -> SpaceType.CHEMISTRY_LABORATORY;
                        case "auditorium" -> SpaceType.AUDITORIUM;
                        case "court" -> SpaceType.COURT;
                        case "eletronicsLaboratory" -> SpaceType.ELETRONICS_LABORATORY;
                        default -> throw new Error("Invalid space type");
                    };
                }
                else if ("institute".equals(node.getNodeName())){
                    String text = node.getTextContent().trim();
                    institute = switch (text) {
                        case "CB" -> InstituteAbbr.CB;
                        case "PB" -> InstituteAbbr.PB;
                        case "IC" -> InstituteAbbr.IC;
                        case "FEEC" -> InstituteAbbr.FEEC;
                        case "IMECC" -> InstituteAbbr.IMECC;
                        case "IFGW" -> InstituteAbbr.IFGW;
                        case "IEL" -> InstituteAbbr.IEL;
                        case "FEF" -> InstituteAbbr.FEF;
                        case "IE" -> InstituteAbbr.IE;
                        default -> throw new Error("Invalid institute");
                    };
                }
                else if ("id".equals(node.getNodeName())){
                    id = node.getTextContent().trim();
                }
                else{
                    throw new Error("Invalid atribute");
                }
            }
            if (id != null && type != null && institute != null)
                space = new Space(id, type, institute);
            else
                throw new Error("Atribute missing");
        }
        catch (Exception e){
            System.err.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
        }

        return space;
    }

    public static SpaceReader getInstance(){
        if (instance == null)
            instance = new SpaceReader();
        return instance;
    }
}
