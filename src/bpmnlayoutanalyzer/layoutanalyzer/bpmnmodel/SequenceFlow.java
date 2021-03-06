package bpmnlayoutanalyzer.layoutanalyzer.bpmnmodel;

/**
 * From BPMN-Layout-Analyzer (edited by Elias Baalmann)
 */
public class SequenceFlow implements RepresentedByWayPoints {

    private final String id;
    private FlowNode source;
    private FlowNode target;

    private WayPointList wayPoints;

    public SequenceFlow(String id) {
        this(id, null, null);
    }

    public SequenceFlow(String id, FlowNode source, FlowNode target) {
        this.id = id;
        setSource(source);
        setTarget(target);
    }

    public String getId() {
        return id;
    }

    public FlowNode getSource() {
        return source;
    }

    public void setSource(FlowNode source) {
        if (this.source != null) {
            this.source.getOutgoingSequenceFlows().remove(this);
        }
        this.source = source;
        source.addOutgoingSequenceFlow(this);
    }

    public FlowNode getTarget() {
        return target;
    }

    public void setTarget(FlowNode target) {
        if (this.target != null) {
            this.target.getIncomingSequenceFlows().remove(this);
        }
        this.target = target;
        target.addIncomingSequenceFlows(this);
    }

    public WayPointList getWayPoints() {
        return wayPoints;
    }

    public void setWayPoints(WayPointList wayPoints) {
        this.wayPoints = wayPoints;
    }

    @Override
    public void clearLayoutData() {
        wayPoints = null;
    }

    @Override
    public boolean hasLayoutData() {
        return wayPoints != null;
    }
}
