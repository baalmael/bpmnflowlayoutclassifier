package bpmnlayoutanalyzer.layoutanalyzer.bpmnxml;

import bpmnlayoutanalyzer.layoutanalyzer.bpmnmodel.*;
import bpmnlayoutanalyzer.layoutanalyzer.util.StringUtil;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.*;

/**
 * From BPMN-Layout-Analyzer (edited by Elias Baalmann)
 */
public class BpmnLayoutSetter {

    private final NamespaceContext bpmnNamespaceContext = BPMNNamespaceContext.DEFAULT;

    private final XPathExpression xpDiagrams;
    private final XPathExpression xpEdges;
    private final XPathExpression xpShapes;


    public BpmnLayoutSetter() {
        try {
            XPath xpath = XPathFactory.newDefaultInstance().newXPath();
            xpath.setNamespaceContext(bpmnNamespaceContext);
            xpDiagrams = xpath.compile("//bpmndi:BPMNDiagram");
            xpEdges = xpath.compile("//bpmndi:BPMNEdge");
            xpShapes = xpath.compile("//bpmndi:BPMNShape");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int getDiagramCount(BpmnProcess process) {
        try {
            return ((NodeList) xpDiagrams.evaluate(process.getBpmnDocument(), XPathConstants.NODESET)).getLength();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private Element getDiagram(BpmnProcess process, int index) {
        try {
            NodeList diagrams = (NodeList) xpDiagrams.evaluate(process.getBpmnDocument(), XPathConstants.NODESET);
            return (Element) diagrams.item(index);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setLayoutData(BpmnProcess process, int diagramIndex) {
        process.clearLayoutData();
        Element bpmnDiagram = getDiagram(process, diagramIndex);
        setShapeLayoutData(process, bpmnDiagram);
        setEdgesLayoutData(process, bpmnDiagram);
        process.setDiagramIndex(diagramIndex);
    }

    private void setShapeLayoutData(BpmnProcess process, Element bpmnDiagram) {
        try {
            NodeList shapes = (NodeList) xpShapes.evaluate(bpmnDiagram, XPathConstants.NODESET);
            for (int j = 0; j < shapes.getLength(); j++) {
                Element shape = (Element) shapes.item(j);
                Element boundsElement = (Element) (shape).getElementsByTagNameNS(bpmnNamespaceContext.getNamespaceURI("dc"), "Bounds").item(0);
                String bpmnElementId = shape.getAttribute("bpmnElement");

                FlowNode flowNode = process.getFlowNodeById(bpmnElementId);
                if (flowNode != null) {
                    flowNode.setBounds(XMLReaderHelper.convertToBounds(boundsElement));
                } else {
                    Participant p = process.getParticipantById(bpmnElementId);
                    if (p != null) {
                        p.setBounds(XMLReaderHelper.convertToBounds(boundsElement));
                        String isHorizontal = shape.getAttribute("isHorizontal");
                        if (StringUtil.isNotEmpty(isHorizontal)) {
                            p.setIsHorizontal("true".equals(isHorizontal));
                        } else {
                            p.setIsHorizontal(null);
                        }
                    } else {
                        Lane l = process.getLaneById(bpmnElementId);
                        if (l != null) {
                            l.setBounds(XMLReaderHelper.convertToBounds(boundsElement));
                            String isHorizontal = shape.getAttribute("isHorizontal");
                            if (StringUtil.isNotEmpty(isHorizontal)) {
                                l.setIsHorizontal("true".equals(isHorizontal));
                            } else {
                                l.setIsHorizontal(null);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setEdgesLayoutData(BpmnProcess process, Element bpmnDiagram) {
        try {
            NodeList edges = (NodeList) xpEdges.evaluate(bpmnDiagram, XPathConstants.NODESET);
            for (int j = 0; j < edges.getLength(); j++) {
                Element edge = (Element) edges.item(j);
                SequenceFlow sequenceFlow = process.getSequenceFlowById(edge.getAttribute("bpmnElement"));
                if (sequenceFlow != null) {
                    NodeList waypointsNodeList = edge.getElementsByTagNameNS(bpmnNamespaceContext.getNamespaceURI("di"), "waypoint");
                    WayPointList waypoints = XMLReaderHelper.convertToWayPoints(waypointsNodeList);
                    sequenceFlow.setWayPoints(waypoints);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
