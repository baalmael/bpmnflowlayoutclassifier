package bpmnlayoutanalyzer.flowlayout;

import bpmnlayoutanalyzer.layoutanalyzer.Result;
import bpmnlayoutanalyzer.layoutanalyzer.bpmnmodel.BpmnProcess;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FlowLayoutResult extends Result {

    private List<String> simplifiedVectorDirectionPerPath = new ArrayList<>();
    private List<String> flowLayoutPerPath = new ArrayList<>();
    private double combinationError = 0.0;
    private double letterAccuracyError = 0.0;
    private String flowLayout = null;
    private long outDegMultiplicated;
    private int numberOfSplitsAndJoins;
    private int numberOfBoundaryEvents;
    private boolean problematicBoundaryEvent = false;
    private boolean hasSwimlaneChange = false;
    private int numberOfFlowNodes;
    private Error error = Error.NO_ERROR;
    private double portionOfFlowNodesAnalyzed;
    private long analyzingTime;

    public FlowLayoutResult(BpmnProcess p) {
        super(p);
    }

    @Override
    public List<Object> getValues() {
        calculateFlowLayout();

        List<Object> fields = new ArrayList<>();

        fields.add(simplifiedVectorDirectionPerPath);
        fields.add(flowLayoutPerPath);
        fields.add(combinationError);
        fields.add(letterAccuracyError);
        fields.add(flowLayout);
        fields.add(outDegMultiplicated);
        fields.add(numberOfSplitsAndJoins);
        fields.add(numberOfBoundaryEvents);
        fields.add(problematicBoundaryEvent);
        fields.add(hasSwimlaneChange);
        fields.add(numberOfFlowNodes);
        fields.add(error);
        fields.add(portionOfFlowNodesAnalyzed);
        fields.add(analyzingTime);

        return fields;
    }

    public void calculateFlowLayout() {
        if (flowLayoutPerPath.stream().filter(direction ->
                direction.equals("Other")).count()
                > flowLayoutPerPath.size() * (2. / 3.)) {
            flowLayout = "Other";
        } else {
            List<String> flowLayoutPerPathNoOther = flowLayoutPerPath.stream().filter(direction -> !direction.equals("Other")).collect(Collectors.toList());
            Map<String, Long> layoutOccurences = flowLayoutPerPathNoOther.stream()
                    .collect(Collectors.groupingBy(Function.identity(),
                            Collectors.counting()));
            int numberOfPaths = flowLayoutPerPathNoOther.size();
            if (!setDominatingLayout(layoutOccurences, numberOfPaths)) {
                layoutOccurences = flowLayoutPerPathNoOther.stream()
                        .collect(Collectors.groupingBy(
                                this::getBaselayoutAndOrientation,
                                Collectors.counting()));
                if (!setDominatingLayout(layoutOccurences, numberOfPaths)) {
                    layoutOccurences = flowLayoutPerPathNoOther.stream()
                            .collect(Collectors.groupingBy(
                                    this::getBaselayout, Collectors.counting()));
                    if (!setDominatingLayout(layoutOccurences, numberOfPaths)) {
                        flowLayout = "Other";
                    }
                }
            }
        }
    }

    private String getBaselayoutAndOrientation(String flowLayout) {
        return flowLayout.substring(0,
                flowLayout.indexOf("-", flowLayout.indexOf("-") + 1) != -1
                        ? flowLayout.indexOf("-", flowLayout.indexOf("-") + 1)
                        : flowLayout.length());
    }

    private String getBaselayout(String flowLayout) {
        return flowLayout.substring(0, flowLayout.contains("-")
                ? flowLayout.indexOf("-")
                : flowLayout.length());
    }


    private boolean setDominatingLayout(Map<String, Long> pathLayouts, int numberOfPaths) {
        pathLayouts.entrySet().stream().max(Map.Entry.comparingByValue())
                .ifPresent(entry -> {
                    if (entry.getValue() >= numberOfPaths * (2. / 3.)) {
                        flowLayout = entry.getKey();
                    }
                });
        return flowLayout != null;
    }

    public void addSimplifiedVectorDirectionPerPath(String pathDirections) {
        simplifiedVectorDirectionPerPath.add(pathDirections);
    }

    public void addFlowLayoutPerPath(String direction) {
        flowLayoutPerPath.add(direction);
    }

    public void setCombinationError(double combinationError) {
        this.combinationError = combinationError;
    }

    public void setLetterAccuracyError(double letterAccuracyError) {
        this.letterAccuracyError = letterAccuracyError;
    }

    public List<String> getFlowLayoutPerPath() {
        return flowLayoutPerPath;
    }

    public String getFlowLayout() {
        calculateFlowLayout();
        return flowLayout;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public void setAnalyzingTime(long time) {
        analyzingTime = time;
    }

    public void setNumberOfSplitsAndJoins(int numberOfSplitsAndJoins) {
        this.numberOfSplitsAndJoins = numberOfSplitsAndJoins;
    }

    public void setPortionOfFlowNodesAnalyzed(double portionOfFlowNodesAnalyzed) {
        this.portionOfFlowNodesAnalyzed = portionOfFlowNodesAnalyzed;
    }

    public void setOutDegMultiplicated(long outDegMultiplicated) {
        this.outDegMultiplicated = outDegMultiplicated;
    }

    public void setNumberOfBoundaryEvents(int numberOfBoundaryEvents) {
        this.numberOfBoundaryEvents = numberOfBoundaryEvents;
    }

    public void setProblematicBoundaryEvent(boolean problematicBoundaryEvent) {
        this.problematicBoundaryEvent = problematicBoundaryEvent;
    }

    public void setHasSwimlaneChange(boolean hasSwimlaneChange) {
        this.hasSwimlaneChange = hasSwimlaneChange;
    }

    public void setNumberOfFlowNodes(int numberOfFlowNodes) {
        this.numberOfFlowNodes = numberOfFlowNodes;
    }
}
