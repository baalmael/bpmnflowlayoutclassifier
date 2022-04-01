package bpmnlayoutanalyzer.flowlayout;

import bpmnlayoutanalyzer.layoutanalyzer.Result;
import bpmnlayoutanalyzer.layoutanalyzer.bpmnmodel.BpmnProcess;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LayoutDirectionResult extends Result {

    private List<String> simplifiedVectorDirectionPerPath = new ArrayList<>();
    private List<String> layoutDirectionPerPath = new ArrayList<>();
    private double combinationError = 0.0;
    private double letterAccuracyError = 0.0;
    private String layoutDirection = null;
    private long outDegMultiplicated;
    private int numberOfSplitsAndJoins;
    private int numberOfBoundaryEvents;
    private boolean problematicBoundaryEvent = false;
    private boolean hasSwimlaneChange = false;
    private int numberOfFlowNodes;
    private Error error = Error.NO_ERROR;
    private double portionOfFlowNodesAnalyzed;
    private long analyzingTime;

    public LayoutDirectionResult(BpmnProcess p) {
        super(p);
    }

    @Override
    public List<Object> getValues() {
        calculateLayoutDirection();

        List<Object> fields = new ArrayList<>();

        fields.add(simplifiedVectorDirectionPerPath);
        fields.add(layoutDirectionPerPath);
        fields.add(combinationError);
        fields.add(letterAccuracyError);
        fields.add(layoutDirection);
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

    public long diaglayoutTime = 0;


    public void calculateLayoutDirection() {
        long timer = System.nanoTime();
//        layoutDirection = null;
        if (layoutDirectionPerPath.stream().filter(direction -> direction.equals("Other")).count() > layoutDirectionPerPath.size() * (2. / 3.)) {
            layoutDirection = "Other";
        } else {
            List<String> layoutDirectionPerPathNoOther = layoutDirectionPerPath.stream().filter(direction -> !direction.equals("Other")).collect(Collectors.toList());
            Map<String, Long> layoutOccurences = layoutDirectionPerPathNoOther.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
            int numberOfPaths = layoutDirectionPerPathNoOther.size();
            if (!setDominatingLayout(layoutOccurences, numberOfPaths)) {
                layoutOccurences = layoutDirectionPerPathNoOther.stream().collect(Collectors.groupingBy(s -> s.substring(0, s.indexOf("-", s.indexOf("-") + 1) != -1 ? s.indexOf("-", s.indexOf("-") + 1) : s.length()), Collectors.counting()));
                if (!setDominatingLayout(layoutOccurences, numberOfPaths)) {
                    layoutOccurences = layoutDirectionPerPathNoOther.stream().collect(Collectors.groupingBy(s -> s.substring(0, s.contains("-") ? s.indexOf("-") : s.length()), Collectors.counting()));
                    if (!setDominatingLayout(layoutOccurences, numberOfPaths)) {
                        layoutDirection = "Other";
                    }
                }
            }
        }
        diaglayoutTime = System.nanoTime() - timer;
    }


    private boolean setDominatingLayout(Map<String, Long> pathLayouts, int numberOfPaths) {
        pathLayouts.entrySet().stream().max(Map.Entry.comparingByValue())
                .ifPresent(entry -> {
                    if (entry.getValue() >= numberOfPaths * (2. / 3.)) {
                        layoutDirection = entry.getKey();
                    }
                });
        return layoutDirection != null;
    }

    public void addSimplifiedVectorDirectionPerPath(String pathDirections) {
        simplifiedVectorDirectionPerPath.add(pathDirections);
    }

    public void addLayoutDirectionPerPath(String direction) {
        layoutDirectionPerPath.add(direction);
    }

    public void setCombinationError(double combinationError) {
        this.combinationError = combinationError;
    }

    public void setLetterAccuracyError(double letterAccuracyError) {
        this.letterAccuracyError = letterAccuracyError;
    }

    public List<String> getLayoutDirectionPerPath() {
        return layoutDirectionPerPath;
    }

    public String getLayoutDirection() {
        calculateLayoutDirection();
        return layoutDirection;
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
