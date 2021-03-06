package bpmnlayoutanalyzer.layoutanalyzer.bpmnmodel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * From BPMN-Layout-Analyzer (edited by Elias Baalmann)
 */
public final class Trace {

    private final List<FlowNode> trace = new ArrayList<>();

    public Trace() {
    }

    public Trace(Trace traceToClone) {
        trace.addAll(traceToClone.trace);
    }

    public void addFlowNodeToTrace(FlowNode n) {
        if (trace.size() == 0) {
            trace.add(n);
        } else {
            FlowNode lastNode = trace.get(trace.size() - 1);
            ArrayList<FlowNode> nodesReachableFromLastNode = new ArrayList<>();
            nodesReachableFromLastNode.addAll(lastNode.getBoundaryEvents());

            nodesReachableFromLastNode.addAll(lastNode.getOutgoingSequenceFlows()
                    .stream()
                    .map(SequenceFlow::getTarget)
                    .collect(Collectors.toList())
            );

            if (nodesReachableFromLastNode.contains(n)) {
                trace.add(n);
            } else {
                throw new RuntimeException(n + " cannot be connected to " + lastNode);
            }
        }
    }

    public boolean contains(FlowNode n) {
        return trace.contains(n);
    }

    public FlowNode getFirstCommonFlowNode(Trace other) {
        for (FlowNode n : trace) {
            if (other.contains(n)) {
                return n;
            }
        }

        return null;
    }

    public FlowNode last() {
        return trace.get(trace.size() - 1);
    }

    public List<FlowNode> getFlowNodes() {
        return trace;
    }

    public boolean hasCompleteLayoutData() {
        if (!trace.get(0).hasLayoutData()) {
            return false;
        }

        for (int i = 0; i < trace.size() - 1; i++) {
            FlowNode fn1 = trace.get(i);
            FlowNode fn2 = trace.get(i + 1);

            if (!fn2.hasLayoutData()) {
                return false;
            }
            if (fn1.getSequenceFlowTo(fn2) != null && !fn1.getSequenceFlowTo(fn2).hasLayoutData()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        return trace.toString();
    }

}
