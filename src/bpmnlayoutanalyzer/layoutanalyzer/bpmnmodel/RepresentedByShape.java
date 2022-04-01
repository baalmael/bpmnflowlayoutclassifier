package bpmnlayoutanalyzer.layoutanalyzer.bpmnmodel;

/**
 * From BPMN-Layout-Analyzer (edited by Elias Baalmann)
 */
public interface RepresentedByShape extends RepresentedBy {

    Bounds getBounds();

    void setBounds(Bounds bounds);


}
