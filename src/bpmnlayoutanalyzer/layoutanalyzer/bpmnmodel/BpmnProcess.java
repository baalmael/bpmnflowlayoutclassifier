package bpmnlayoutanalyzer.layoutanalyzer.bpmnmodel;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * From BPMN-Layout-Analyzer (edited by Elias Baalmann)
 */
public class BpmnProcess {

    private final List<FlowNode> flowNodes = new ArrayList<>();
    private final Map<String, FlowNode> flowNodeById = new HashMap<>();
    private final List<SequenceFlow> sequenceFlows = new ArrayList<>();
    private final Map<String, SequenceFlow> sequenceFlowById = new HashMap<>();
    private final List<Participant> participants = new ArrayList<>();
    private final Map<String, Participant> participantById = new HashMap<>();
    private String filename;
    private int diagramIndex;
    private final Document bpmnDocument;
    private final List<SubProcess> subProcesses = new ArrayList<>();
    private final List<Lane> lanes = new ArrayList<>();
    private final Map<String, Lane> lanesById = new HashMap<>();

    public BpmnProcess(String filename, Document bpmnDocument) {
        this.filename = filename;
        this.bpmnDocument = bpmnDocument;
    }

    public FlowNode getFlowNodeById(String id) {
        FlowNode flowNode = flowNodeById.get(id);
        if (flowNode != null) {
            return flowNode;
        }

        for (SubProcess sp : subProcesses) {
            if (sp.getProcess() != null && sp.getProcess().getFlowNodeById(id) != null) {
                return sp.getProcess().getFlowNodeById(id);
            }
        }

        for (Participant p : participants) {
            if (p.getProcess() != null && p.getProcess().getFlowNodeById(id) != null) {
                return p.getProcess().getFlowNodeById(id);
            }
        }

        return null;
    }

    public void add(FlowNode n) {
        flowNodes.add(n);
        flowNodeById.put(n.getId(), n);

        if (n instanceof SubProcess) {
            subProcesses.add((SubProcess) n);
        }
    }

    public void clearLayoutData() {
        for (FlowNode n : flowNodes) {
            n.clearLayoutData();
        }

        for (SequenceFlow sf : sequenceFlows) {
            sf.clearLayoutData();
        }

        for (Participant p : participants) {
            p.clearLayoutData();
        }
    }

    public List<FlowNode> getFlowNodes() {
        List<FlowNode> result = new ArrayList<>(flowNodes);
        for (SubProcess sp : subProcesses) {
            if (sp.getProcess() != null) {
                result.addAll(sp.getProcess().getFlowNodes());
            }
        }
        for (Participant p : participants) {
            if (p.getProcess() != null) {
                result.addAll(p.getProcess().getFlowNodes());
            }
        }

        return result;
    }

    public SequenceFlow getSequenceFlowById(String id) {
        SequenceFlow sequenceFlow = sequenceFlowById.get(id);
        if (sequenceFlow != null)
            return sequenceFlow;

        for (SubProcess sp : subProcesses) {
            if (sp.getProcess() != null && sp.getProcess().getSequenceFlowById(id) != null) {
                return sp.getProcess().getSequenceFlowById(id);
            }
        }

        for (Participant p : participants) {
            if (p.getProcess() != null && p.getProcess().getSequenceFlowById(id) != null) {
                return p.getProcess().getSequenceFlowById(id);
            }
        }

        return null;
    }

    public void add(SequenceFlow s) {
        sequenceFlows.add(s);
        sequenceFlowById.put(s.getId(), s);
    }

    public List<SequenceFlow> getSequenceFlows() {
        ArrayList<SequenceFlow> result = new ArrayList<>(sequenceFlows);
        for (SubProcess sp : subProcesses) {
            if (sp.getProcess() != null) {
                result.addAll(sp.getProcess().getSequenceFlows());
            }
        }
        for (Participant p : participants) {
            if (p.getProcess() != null) {
                result.addAll(p.getProcess().getSequenceFlows());
            }
        }
        return result;
    }


    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getDiagramIndex() {
        return diagramIndex;
    }

    public void setDiagramIndex(int diagramIndex) {
        this.diagramIndex = diagramIndex;
    }

    public void add(Participant participant) {
        this.participants.add(participant);
        this.participantById.put(participant.getId(), participant);
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public Participant getParticipantById(String id) {
        return participantById.get(id);
    }

    public List<FlowNode> getStartFlowNodes() {
        List<FlowNode> result = new ArrayList<>();

        for (FlowNode fn : getFlowNodes()) {
            if (
                    fn.getIncomingSequenceFlows().size() == 0 &&
                            !"boundaryEvent".equals(fn.getType())
            ) {
                result.add(fn);
            }
        }

        return result;
    }

    public List<FlowNode> getEndFlowNodes() {
        List<FlowNode> result = new ArrayList<>();

        for (FlowNode fn : getFlowNodes()) {
            if (fn.getOutgoingSequenceFlows().size() == 0) {
                result.add(fn);
            }
        }

        return result;
    }

    public Document getBpmnDocument() {
        return bpmnDocument;
    }

    public Participant getParticipantByProcessId(String processId) {
        for (Participant p : getParticipants()) {
            if (processId.equals(p.getProcessId())) {
                return p;
            }
        }
        return null;
    }

    public List<Lane> getLanes() {
        ArrayList<Lane> result = new ArrayList<>(lanes);
        for (SubProcess sp : subProcesses) {
            if (sp.getProcess() != null) {
                result.addAll(sp.getProcess().getLanes());
            }
        }
        for (Participant p : participants) {
            if (p.getProcess() != null) {
                result.addAll(p.getProcess().getLanes());
            }
        }
        return result;
    }

    public void add(Lane l) {
        lanes.add(l);
        lanesById.put(l.getId(), l);
    }

    public Lane getLaneById(String id) {
        Lane lane = lanesById.get(id);
        if (lane != null)
            return lane;

        for (SubProcess sp : subProcesses) {
            if (sp.getProcess() != null && sp.getProcess().getLaneById(id) != null) {
                return sp.getProcess().getLaneById(id);
            }
        }

        for (Participant p : participants) {
            if (p.getProcess() != null && p.getProcess().getLaneById(id) != null) {
                return p.getProcess().getLaneById(id);
            }
        }

        return null;
    }
}
