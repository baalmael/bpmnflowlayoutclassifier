package bpmnlayoutanalyzer.layoutanalyzer.bpmnxml;

import bpmnlayoutanalyzer.layoutanalyzer.bpmnmodel.Bounds;
import bpmnlayoutanalyzer.layoutanalyzer.bpmnmodel.WayPoint;
import bpmnlayoutanalyzer.layoutanalyzer.bpmnmodel.WayPointList;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * From BPMN-Layout-Analyzer (edited by Elias Baalmann)
 */
public class XMLReaderHelper {

    public static WayPoint fromDiElement(Node item) {
        WayPoint wp = new WayPoint();

        NamedNodeMap attributes = item.getAttributes();

        wp.setX(Double.parseDouble(attributes.getNamedItem("x").getNodeValue()));
        wp.setY(Double.parseDouble(attributes.getNamedItem("y").getNodeValue()));

        return wp;
    }

    public static WayPointList convertToWayPoints(NodeList waypointsNodeList) {
        WayPointList result = new WayPointList();
        for (int i = 0; i < waypointsNodeList.getLength(); i++) {
            result.add(fromDiElement(waypointsNodeList.item(i)));
        }
        return result;
    }

    public static Bounds convertToBounds(Element boundsElement) {
        try {
            return new Bounds(
                    Double.parseDouble(boundsElement.getAttribute("x")),
                    Double.parseDouble(boundsElement.getAttribute("y")),
                    Double.parseDouble(boundsElement.getAttribute("width")),
                    Double.parseDouble(boundsElement.getAttribute("height"))
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
