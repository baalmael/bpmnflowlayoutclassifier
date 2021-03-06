package bpmnlayoutanalyzer.layoutanalyzer.bpmnxml;

import bpmnlayoutanalyzer.layoutanalyzer.bpmnmodel.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.*;
import java.util.Map.Entry;

/**
 * From BPMN-Layout-Analyzer (edited by Elias Baalmann)
 */
public class BpmnReader {

    private final DocumentBuilder docBuilder;

    private final NamespaceContext bpmnNamespaceContext = BPMNNamespaceContext.DEFAULT;
    private final XPathExpression xpPossibleFlowNodes;
    private final XPathExpression xpSequenceFlows;
    private final XPathExpression xpParticipants;
    private final XPathExpression xpProcesses;
    private final XPathExpression xpParticipantProcessIds;
    private final XPathExpression xpLanes;
    private final XPathExpression xpLaneElements;

    private static final List<String> SUBPROCESS_NAMES = Arrays.asList(
            "subProcess",
            "subChoreography",
            "transaction"
    );

    private static final List<String> NO_FLOWNODE_NAMES = Arrays.asList(
            "association",
            "dataInputAssociation",
            "dataStoreReference",
            "dataObject",
            "dataObjectReference",
            "dataOutputAssociation",
            "documentation",
            "extensionElements",
            "group",
            "incoming",
            "ioSpecification",
            "lane",
            "laneSet",
            "multiInstanceLoopCharacteristics",
            "outgoing",
            "property",
            "sequenceFlow",
            "standardLoopCharacteristics",
            "textAnnotation",
            "timerEventDefinition"
    );

    public BpmnReader() {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            dbf.setValidating(false);
            dbf.setExpandEntityReferences(false);
            docBuilder = dbf.newDocumentBuilder();
            docBuilder.setEntityResolver((publicId, systemId) -> new InputSource(new StringReader("")));

            XPath xpath = XPathFactory.newDefaultInstance().newXPath();
            xpath.setNamespaceContext(bpmnNamespaceContext);

            xpProcesses = xpath.compile("/bpmn:definitions/bpmn:process");
            xpPossibleFlowNodes = xpath.compile("*[local-name(.) != 'sequenceFlow' and local-name(.) != 'incoming' and local-name(.) != 'outgoing' and local-name(.) != 'property' and local-name(.) != 'extensionElements']");
            xpSequenceFlows = xpath.compile("//bpmn:sequenceFlow");
            xpParticipants = xpath.compile("/bpmn:definitions/bpmn:collaboration/bpmn:participant");
            xpParticipantProcessIds = xpath.compile("/bpmn:definitions/bpmn:collaboration/bpmn:participant/@processRef");
            xpLanes = xpath.compile("//bpmn:lane");
            xpLaneElements = xpath.compile("//bpmn:lane/bpmn:flowNodeRef");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public BpmnProcess readProcess(String filename) throws SAXException, IOException {
        return readProcess(filename, new FileInputStream(filename));
    }

    public BpmnProcess readProcess(String filename, InputStream in) throws SAXException, IOException {
        try {
            Document bpmnDocument = docBuilder.parse(in);

            BpmnProcess process = new BpmnProcess(filename, bpmnDocument);

            List<String> processIdsForParticipants = extractProcessIdsForParticipants(bpmnDocument);

            NodeList processElements = (NodeList) xpProcesses.evaluate(bpmnDocument, XPathConstants.NODESET);
            collectAllParticipants(process);

            for (int i = 0; i < processElements.getLength(); i++) {
                Element processElement = (Element) processElements.item(i);
                String processId = processElement.getAttribute("id");
                if (!processIdsForParticipants.contains(processId)) {
                    collectAllFlowNodesAndLanes(process, processElement);
                } else {
                    BpmnProcess participantProcess = new BpmnProcess("", null);
                    collectAllFlowNodesAndLanes(participantProcess, processElement);
                    process.getParticipantByProcessId(processId).setProcess(participantProcess);
                }
            }
            connectFlowNodesBySequenceFlows(process, bpmnDocument.getDocumentElement());

            return process;
        } catch (XPathExpressionException e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<String> extractProcessIdsForParticipants(Document bpmnDocument) throws XPathExpressionException {
        List<String> ids = new ArrayList<>();

        NodeList idAttributes = (NodeList) xpParticipantProcessIds.evaluate(bpmnDocument, XPathConstants.NODESET);
        for (int i = 0; i < idAttributes.getLength(); i++) {
            ids.add(idAttributes.item(i).getNodeValue());
        }
        return ids;
    }


    private void collectAllParticipants(BpmnProcess process) throws XPathExpressionException {
        NodeList ids = (NodeList) xpParticipants.evaluate(process.getBpmnDocument(), XPathConstants.NODESET);

        for (int i = 0; i < ids.getLength(); i++) {
            Element participantElement = (Element) ids.item(i);
            process.add(new Participant(participantElement.getAttribute("id"), participantElement.getAttribute("processRef")));
        }
    }

    private void connectFlowNodesBySequenceFlows(BpmnProcess process, Element processElement) throws XPathExpressionException {
        NodeList sequenceFlowElements = (NodeList) xpSequenceFlows.evaluate(processElement, XPathConstants.NODESET);
        for (int i = 0; i < sequenceFlowElements.getLength(); i++) {
            Element e = (Element) sequenceFlowElements.item(i);
            String sourceId = e.getAttribute("sourceRef");
            String targetId = e.getAttribute("targetRef");
            FlowNode source = process.getFlowNodeById(sourceId);
            String sequenceFlowId = e.getAttribute("id");
            if (source == null) {
                throw new InvalidBpmnXml("Flow Node not found: " + sourceId + " (for source in sequence flow " + sequenceFlowId + ")");
            }
            FlowNode target = process.getFlowNodeById(targetId);
            if (target == null) {
                throw new InvalidBpmnXml("Flow Node not found: " + targetId + " (for target in sequence flow " + sequenceFlowId + ")");
            }

            SequenceFlow s = new SequenceFlow(sequenceFlowId, source, target);
            source.getParent().add(s);
        }
    }

    private void collectAllFlowNodesAndLanes(BpmnProcess process, Element processElement) throws XPathExpressionException {
        Map<FlowNode, String> attachedToId = new HashMap<>();

        NodeList laneElements = (NodeList) xpLanes.evaluate(processElement, XPathConstants.NODESET);
        for (int i = 0; i < laneElements.getLength(); i++) {
            Element laneElement = (Element) laneElements.item(i);
            process.add(new Lane(laneElement.getAttribute("id")));
        }

        NodeList flowNodeElements = (NodeList) xpPossibleFlowNodes.evaluate(processElement, XPathConstants.NODESET);
        for (int i = 0; i < flowNodeElements.getLength(); i++) {
            Element e = (Element) flowNodeElements.item(i);
            if (NO_FLOWNODE_NAMES.contains(e.getLocalName())) {
                continue;
            }
            String id = e.getAttribute("id");
            String type = e.getLocalName();
            String name = e.getAttribute("name");
            FlowNode fn;

            if (SUBPROCESS_NAMES.contains(e.getLocalName())) {
                SubProcess sp = new SubProcess(id, type, process, name, "true".equals(e.getAttribute("triggeredByEvent")));
                BpmnProcess subProcessProcess = new BpmnProcess("", null);
                collectAllFlowNodesAndLanes(subProcessProcess, e);
                if (!subProcessProcess.getFlowNodes().isEmpty()) {
                    sp.setProcess(subProcessProcess);
                }
                fn = sp;
            } else {
                fn = new FlowNode(id, type, process, name);
                if (e.getLocalName().equals("boundaryEvent")) {
                    String cancelActivityAttribute = e.getAttribute("cancelActivity");
                    if (cancelActivityAttribute != null && !"".equals(cancelActivityAttribute)) {
                        boolean cancelActivity = "true".equals(cancelActivityAttribute);
                        fn.setCancelActivity(cancelActivity);
                    }

                    String attachedToRef = e.getAttribute("attachedToRef");
                    if (attachedToRef != null) {
                        attachedToId.put(fn, attachedToRef);
                    }
                } else if (e.getLocalName().endsWith("Event")) {
                    NodeList children = e.getChildNodes();
                    for (int x = 0; x < children.getLength(); x++) {
                        Node n = children.item(x);
                        if (n.getNodeType() == Node.ELEMENT_NODE && n.getLocalName().endsWith("EventDefinition")) {
                            String eventType = n.getLocalName().replace("EventDefintion", "");
                            fn.setEventType(eventType);
                        }
                    }
                }
            }

            process.add(fn);
        }

        for (Entry<FlowNode, String> entry : attachedToId.entrySet()) {
            try {
                FlowNode attachedToNode = process.getFlowNodeById(entry.getValue());
                entry.getKey().setAttachedTo(attachedToNode);
                attachedToNode.addBoundaryEvent(entry.getKey());
            } catch (Exception e) {
                System.err.println("Error attaching boundary event " + entry.getKey().getId() + " to " + entry.getValue());
                e.printStackTrace();
            }
        }
    }


}
