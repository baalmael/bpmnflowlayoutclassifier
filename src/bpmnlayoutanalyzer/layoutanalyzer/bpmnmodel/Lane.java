package bpmnlayoutanalyzer.layoutanalyzer.bpmnmodel;

/**
 * From BPMN-Layout-Analyzer (edited by Elias Baalmann)
 */
public class Lane implements RepresentedByShape {

    private final String id;
    private Bounds bounds;
    private Boolean isHorizontal;

    public Lane(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Bounds getBounds() {
        return bounds;
    }

    @Override
    public void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }

    @Override
    public void clearLayoutData() {
        bounds = null;
    }

    @Override
    public boolean hasLayoutData() {
        return bounds != null;
    }

    public Boolean getIsHorizontal() {
        return isHorizontal;
    }

    public void setIsHorizontal(Boolean isHorizontal) {
        this.isHorizontal = isHorizontal;
    }

    public boolean contains(Bounds bounds) {
        double x = bounds.getX();
        double y = bounds.getY();

        return x >= this.bounds.getX() && x <= this.bounds.getX() + this.bounds.getWidth() && y >= this.bounds.getY() && y <= this.bounds.getY() + this.bounds.getHeight();
    }
}
