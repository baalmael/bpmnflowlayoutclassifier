package bpmnlayoutanalyzer.flowlayout;


import bpmnlayoutanalyzer.layoutanalyzer.bpmnmodel.*;

import java.util.*;
import java.util.stream.Collectors;

import static bpmnlayoutanalyzer.flowlayout.Constants.*;


public class LayoutDirectionAnalyzer {

    private static final String[] VALUES = {STRAIGHT_E_CLEAN_REGEX, STRAIGHT_E_LENDRIGHT_REGEX, STRAIGHT_E_LSTARTLEFT_REGEX, STRAIGHT_E_LENDLEFT_REGEX, STRAIGHT_E_LSTARTRIGHT_REGEX, STRAIGHT_E_LLSTARTLEFT_REGEX, STRAIGHT_E_LLSTARTRIGHT_REGEX, STRAIGHT_E_U_REGEX, STRAIGHT_E_N_REGEX, STRAIGHT_E_Z_REGEX, STRAIGHT_E_S_REGEX, STRAIGHT_S_CLEAN_REGEX, STRAIGHT_S_LENDRIGHT_REGEX, STRAIGHT_S_LSTARTLEFT_REGEX, STRAIGHT_S_LENDLEFT_REGEX, STRAIGHT_S_LSTARTRIGHT_REGEX, STRAIGHT_S_LLSTARTLEFT_REGEX, STRAIGHT_S_LLSTARTRIGHT_REGEX, STRAIGHT_S_U_REGEX, STRAIGHT_S_N_REGEX, STRAIGHT_S_Z_REGEX, STRAIGHT_S_S_REGEX, STRAIGHT_W_CLEAN_REGEX, STRAIGHT_W_LENDRIGHT_REGEX, STRAIGHT_W_LSTARTLEFT_REGEX, STRAIGHT_W_LENDLEFT_REGEX, STRAIGHT_W_LSTARTRIGHT_REGEX, STRAIGHT_W_LLSTARTLEFT_REGEX, STRAIGHT_W_LLSTARTRIGHT_REGEX, STRAIGHT_W_U_REGEX, STRAIGHT_W_N_REGEX, STRAIGHT_W_Z_REGEX, STRAIGHT_W_S_REGEX, STRAIGHT_N_CLEAN_REGEX, STRAIGHT_N_LENDRIGHT_REGEX, STRAIGHT_N_LSTARTLEFT_REGEX, STRAIGHT_N_LENDLEFT_REGEX, STRAIGHT_N_LSTARTRIGHT_REGEX, STRAIGHT_N_LLSTARTLEFT_REGEX, STRAIGHT_N_LLSTARTRIGHT_REGEX, STRAIGHT_N_U_REGEX, STRAIGHT_N_N_REGEX, STRAIGHT_N_Z_REGEX, STRAIGHT_N_S_REGEX, L_SE_CLEAN_REGEX, L_SE_LENDRIGHT_REGEX, L_SE_LSTARTLEFT_REGEX, L_SE_LENDLEFT_REGEX, L_SE_LSTARTRIGHT_REGEX, L_SE_LLSTARTLEFT_REGEX, L_SE_LLSTARTRIGHT_REGEX, L_SE_U_REGEX, L_SE_N_REGEX, L_SE_UNCLEAN_REGEX, L_WS_CLEAN_REGEX, L_WS_LENDRIGHT_REGEX, L_WS_LSTARTLEFT_REGEX, L_WS_LENDLEFT_REGEX, L_WS_LSTARTRIGHT_REGEX, L_WS_LLSTARTLEFT_REGEX, L_WS_LLSTARTRIGHT_REGEX, L_WS_U_REGEX, L_WS_N_REGEX, L_WS_UNCLEAN_REGEX, L_NW_CLEAN_REGEX, L_NW_LENDRIGHT_REGEX, L_NW_LSTARTLEFT_REGEX, L_NW_LENDLEFT_REGEX, L_NW_LSTARTRIGHT_REGEX, L_NW_LLSTARTLEFT_REGEX, L_NW_LLSTARTRIGHT_REGEX, L_NW_U_REGEX, L_NW_N_REGEX, L_NW_UNCLEAN_REGEX, L_EN_CLEAN_REGEX, L_EN_LENDRIGHT_REGEX, L_EN_LSTARTLEFT_REGEX, L_EN_LENDLEFT_REGEX, L_EN_LSTARTRIGHT_REGEX, L_EN_LLSTARTLEFT_REGEX, L_EN_LLSTARTRIGHT_REGEX, L_EN_U_REGEX, L_EN_N_REGEX, L_EN_UNCLEAN_REGEX, L_SW_CLEAN_REGEX, L_SW_LENDRIGHT_REGEX, L_SW_LSTARTLEFT_REGEX, L_SW_LENDLEFT_REGEX, L_SW_LSTARTRIGHT_REGEX, L_SW_LLSTARTLEFT_REGEX, L_SW_LLSTARTRIGHT_REGEX, L_SW_U_REGEX, L_SW_N_REGEX, L_SW_UNCLEAN_REGEX, L_WN_CLEAN_REGEX, L_WN_LENDRIGHT_REGEX, L_WN_LSTARTLEFT_REGEX, L_WN_LENDLEFT_REGEX, L_WN_LSTARTRIGHT_REGEX, L_WN_LLSTARTLEFT_REGEX, L_WN_LLSTARTRIGHT_REGEX, L_WN_U_REGEX, L_WN_N_REGEX, L_WN_UNCLEAN_REGEX, L_NE_CLEAN_REGEX, L_NE_LENDRIGHT_REGEX, L_NE_LSTARTLEFT_REGEX, L_NE_LENDLEFT_REGEX, L_NE_LSTARTRIGHT_REGEX, L_NE_LLSTARTLEFT_REGEX, L_NE_LLSTARTRIGHT_REGEX, L_NE_U_REGEX, L_NE_N_REGEX, L_NE_UNCLEAN_REGEX, L_ES_CLEAN_REGEX, L_ES_LENDRIGHT_REGEX, L_ES_LSTARTLEFT_REGEX, L_ES_LENDLEFT_REGEX, L_ES_LSTARTRIGHT_REGEX, L_ES_LLSTARTLEFT_REGEX, L_ES_LLSTARTRIGHT_REGEX, L_ES_U_REGEX, L_ES_N_REGEX, L_ES_UNCLEAN_REGEX, SNAKE_ES_CLEAN_REGEX, SNAKE_ES_STARTTOSIDE_REGEX, SNAKE_WS_CLEAN_REGEX, SNAKE_WS_STARTTOSIDE_REGEX, SNAKE_WN_CLEAN_REGEX, SNAKE_WN_STARTTOSIDE_REGEX, SNAKE_EN_CLEAN_REGEX, SNAKE_EN_STARTTOSIDE_REGEX, SNAKE_SE_CLEAN_REGEX, SNAKE_SE_STARTTOSIDE_REGEX, SNAKE_NE_CLEAN_REGEX, SNAKE_NE_STARTTOSIDE_REGEX, SNAKE_NW_CLEAN_REGEX, SNAKE_NW_STARTTOSIDE_REGEX, SNAKE_SW_CLEAN_REGEX, SNAKE_SW_STARTTOSIDE_REGEX, MULTILINE_ES_CLEAN_REGEX, MULTILINE_ES_STARTTOSIDE_REGEX, MULTILINE_ES_ENDTOSIDE_REGEX, MULTILINE_ES_STARTANDENDTOSIDE_REGEX, MULTILINE_WN_CLEAN_REGEX, MULTILINE_WN_STARTTOSIDE_REGEX, MULTILINE_WN_ENDTOSIDE_REGEX, MULTILINE_WN_STARTANDENDTOSIDE_REGEX, MULTILINE_EN_CLEAN_REGEX, MULTILINE_EN_STARTTOSIDE_REGEX, MULTILINE_EN_ENDTOSIDE_REGEX, MULTILINE_EN_STARTANDENDTOSIDE_REGEX, MULTILINE_WS_CLEAN_REGEX, MULTILINE_WS_STARTTOSIDE_REGEX, MULTILINE_WS_ENDTOSIDE_REGEX, MULTILINE_WS_STARTANDENDTOSIDE_REGEX, MULTILINE_SE_CLEAN_REGEX, MULTILINE_SE_STARTTOSIDE_REGEX, MULTILINE_SE_ENDTOSIDE_REGEX, MULTILINE_SE_STARTANDENDTOSIDE_REGEX, MULTILINE_NW_CLEAN_REGEX, MULTILINE_NW_STARTTOSIDE_REGEX, MULTILINE_NW_ENDTOSIDE_REGEX, MULTILINE_NW_STARTANDENDTOSIDE_REGEX, MULTILINE_NE_CLEAN_REGEX, MULTILINE_NE_STARTTOSIDE_REGEX, MULTILINE_NE_ENDTOSIDE_REGEX, MULTILINE_NE_STARTANDENDTOSIDE_REGEX, MULTILINE_SW_CLEAN_REGEX, MULTILINE_SW_STARTTOSIDE_REGEX, MULTILINE_SW_ENDTOSIDE_REGEX, MULTILINE_SL_STARTANDENDTOSIDE_REGEX, U_SE_REGEX, U_WS_REGEX, U_NW_REGEX, U_EN_REGEX, U_SW_REGEX, U_WN_REGEX, U_NE_REGEX, U_ES_REGEX, STAIRS_SE_REGEX, STAIRS_SW_REGEX, STAIRS_NW_REGEX, STAIRS_NE_REGEX, Z_ES_CLEAN_REGEX, Z_ES_LENDRIGHT_REGEX, Z_ES_LSTARTLEFT_REGEX, Z_ES_LENDLEFT_REGEX, Z_ES_LSTARTRIGHT_REGEX, Z_ES_LLSTARTLEFT_REGEX, Z_ES_LLSTARTRIGHT_REGEX, Z_ES_U_REGEX, Z_ES_N_REGEX, Z_ES_UNCLEAN_REGEX, Z_EN_CLEAN_REGEX, Z_EN_LENDRIGHT_REGEX, Z_EN_LSTARTLEFT_REGEX, Z_EN_LENDLEFT_REGEX, Z_EN_LSTARTRIGHT_REGEX, Z_EN_LLSTARTLEFT_REGEX, Z_EN_LLSTARTRIGHT_REGEX, Z_EN_U_REGEX, Z_EN_N_REGEX, Z_EN_UNCLEAN_REGEX, Z_WS_CLEAN_REGEX, Z_WS_LENDRIGHT_REGEX, Z_WS_LSTARTLEFT_REGEX, Z_WS_LENDLEFT_REGEX, Z_WS_LSTARTRIGHT_REGEX, Z_WS_LLSTARTLEFT_REGEX, Z_WS_LLSTARTRIGHT_REGEX, Z_WS_U_REGEX, Z_WS_N_REGEX, Z_WS_UNCLEAN_REGEX, Z_WN_CLEAN_REGEX, Z_WN_LENDRIGHT_REGEX, Z_WN_LSTARTLEFT_REGEX, Z_WN_LENDLEFT_REGEX, Z_WN_LSTARTRIGHT_REGEX, Z_WN_LLSTARTLEFT_REGEX, Z_WN_LLSTARTRIGHT_REGEX, Z_WN_U_REGEX, Z_WN_N_REGEX, Z_WN_UNCLEAN_REGEX, Z_SE_CLEAN_REGEX, Z_SE_LENDRIGHT_REGEX, Z_SE_LSTARTLEFT_REGEX, Z_SE_LENDLEFT_REGEX, Z_SE_LSTARTRIGHT_REGEX, Z_SE_LLSTARTLEFT_REGEX, Z_SE_LLSTARTRIGHT_REGEX, Z_SE_U_REGEX, Z_SE_N_REGEX, Z_SE_UNCLEAN_REGEX, Z_SW_CLEAN_REGEX, Z_SW_LENDRIGHT_REGEX, Z_SW_LSTARTLEFT_REGEX, Z_SW_LENDLEFT_REGEX, Z_SW_LSTARTRIGHT_REGEX, Z_SW_LLSTARTLEFT_REGEX, Z_SW_LLSTARTRIGHT_REGEX, Z_SW_U_REGEX, Z_SW_N_REGEX, Z_SW_UNCLEAN_REGEX, Z_NE_CLEAN_REGEX, Z_NE_LENDRIGHT_REGEX, Z_NE_LSTARTLEFT_REGEX, Z_NE_LENDLEFT_REGEX, Z_NE_LSTARTRIGHT_REGEX, Z_NE_LLSTARTLEFT_REGEX, Z_NE_LLSTARTRIGHT_REGEX, Z_NE_U_REGEX, Z_NE_N_REGEX, Z_NE_UNCLEAN_REGEX, Z_NW_CLEAN_REGEX, Z_NW_LENDRIGHT_REGEX, Z_NW_LSTARTLEFT_REGEX, Z_NW_LENDLEFT_REGEX, Z_NW_LSTARTRIGHT_REGEX, Z_NW_LLSTARTLEFT_REGEX, Z_NW_LLSTARTRIGHT_REGEX, Z_NW_U_REGEX, Z_NW_N_REGEX, Z_NW_UNCLEAN_REGEX, STRAIGHT_E_UNCLEAN_REGEX, STRAIGHT_S_UNCLEAN_REGEX, STRAIGHT_W_UNCLEAN_REGEX, STRAIGHT_N_UNCLEAN_REGEX};
    private static final String[] KEYS = {"Straight-E-Clean", "Straight-E-LEndRight", "Straight-E-LStartLeft", "Straight-E-LEndLeft", "Straight-E-LStartRight", "Straight-E-LLStartLeft", "Straight-E-LLStartRight", "Straight-E-U", "Straight-E-N", "Straight-E-Z", "Straight-E-S", "Straight-S-Clean", "Straight-S-LEndRight", "Straight-S-LStartLeft", "Straight-S-LEndLeft", "Straight-S-LStartRight", "Straight-S-LLStartLeft", "Straight-S-LLStartRight", "Straight-S-U", "Straight-S-N", "Straight-S-Z", "Straight-S-S", "Straight-W-Clean", "Straight-W-LEndRight", "Straight-W-LStartLeft", "Straight-W-LEndLeft", "Straight-W-LStartRight", "Straight-W-LLStartLeft", "Straight-W-LLStartRight", "Straight-W-U", "Straight-W-N", "Straight-W-Z", "Straight-W-S", "Straight-N-Clean", "Straight-N-LEndRight", "Straight-N-LStartLeft", "Straight-N-LEndLeft", "Straight-N-LStartRight", "Straight-N-LLStartLeft", "Straight-N-LLStartRight", "Straight-N-U", "Straight-N-N", "Straight-N-Z", "Straight-N-S", "L-SE-Clean", "L-SE-LEndRight", "L-SE-LStartLeft", "L-SE-LEndLeft", "L-SE-LStartRight", "L-SE-LLStartLeft", "L-SE-LLStartRight", "L-SE-U", "L-SE-N", "L-SE-Unclean", "L-WS-Clean", "L-WS-LEndRight", "L-WS-LStartLeft", "L-WS-LEndLeft", "L-WS-LStartRight", "L-WS-LLStartLeft", "L-WS-LLStartRight", "L-WS-U", "L-WS-N", "L-WS-Unclean", "L-NW-Clean", "L-NW-LEndRight", "L-NW-LStartLeft", "L-NW-LEndLeft", "L-NW-LStartRight", "L-NW-LLStartLeft", "L-NW-LLStartRight", "L-NW-U", "L-NW-N", "L-NW-Unclean", "L-EN-Clean", "L-EN-LEndRight", "L-EN-LStartLeft", "L-EN-LEndLeft", "L-EN-LStartRight", "L-EN-LLStartLeft", "L-EN-LLStartRight", "L-EN-U", "L-EN-N", "L-EN-Unclean", "L-SW-Clean", "L-SW-LEndRight", "L-SW-LStartLeft", "L-SW-LEndLeft", "L-SW-LStartRight", "L-SW-LLStartLeft", "L-SW-LLStartRight", "L-SW-U", "L-SW-N", "L-SW-Unclean", "L-WN-Clean", "L-WN-LEndRight", "L-WN-LStartLeft", "L-WN-LEndLeft", "L-WN-LStartRight", "L-WN-LLStartLeft", "L-WN-LLStartRight", "L-WN-U", "L-WN-N", "L-WN-Unclean", "L-NE-Clean", "L-NE-LEndRight", "L-NE-LStartLeft", "L-NE-LEndLeft", "L-NE-LStartRight", "L-NE-LLStartLeft", "L-NE-LLStartRight", "L-NE-U", "L-NE-N", "L-NE-Unclean", "L-ES-Clean", "L-ES-LEndRight", "L-ES-LStartLeft", "L-ES-LEndLeft", "L-ES-LStartRight", "L-ES-LLStartLeft", "L-ES-LLStartRight", "L-ES-U", "L-ES-N", "L-ES-Unclean", "Snake-ES-Clean", "Snake-ES-StartToSide", "Snake-WS-Clean", "Snake-WS-StartToSide", "Snake-WN-Clean", "Snake-WN-StartToSide", "Snake-EN-Clean", "Snake-EN-StartToSide", "Snake-SE-Clean", "Snake-SE-StartToSide", "Snake-NE-Clean", "Snake-NE-StartToSide", "Snake-NW-Clean", "Snake-NW-StartToSide", "Snake-SW-Clean", "Snake-SW-StartToSide", "Multiline-ES-Clean", "Multiline-ES-StartToSide", "Multiline-ES-Endtoside", "Multiline-ES-StartandEndtoside", "Multiline-WN-Clean", "Multiline-WN-StartToSide", "Multiline-WN-Endtoside", "Multiline-WN-StartandEndtoside", "Multiline-EN-Clean", "Multiline-EN-StartToSide", "Multiline-EN-Endtoside", "Multiline-EN-StartandEndtoside", "Multiline-WS-Clean", "Multiline-WS-StartToSide", "Multiline-WS-Endtoside", "Multiline-WS-StartandEndtoside", "Multiline-SE-Clean", "Multiline-SE-StartToSide", "Multiline-SE-Endtoside", "Multiline-SE-StartandEndtoside", "Multiline-NW-Clean", "Multiline-NW-StartToSide", "Multiline-NW-Endtoside", "Multiline-NW-StartandEndtoside", "Multiline-NE-Clean", "Multiline-NE-StartToSide", "Multiline-NE-Endtoside", "Multiline-NE-StartandEndtoside", "Multiline-SW-Clean", "Multiline-SW-StartToSide", "Multiline-SW-Endtoside", "Multiline-SL-StartandEndtoside", "U-SE", "U-WS", "U-NW", "U-EN", "U-SW", "U-WN", "U-NE", "U-ES", "Stairs-SE", "Stairs-SW", "Stairs-NW", "Stairs-NE", "Z-ES-Clean", "Z-ES-LEndRight", "Z-ES-LStartLeft", "Z-ES-LEndLeft", "Z-ES-LStartRight", "Z-ES-LLStartLeft", "Z-ES-LLStartRight", "Z-ES-U", "Z-ES-N", "Z-ES-Unclean", "Z-EN-Clean", "Z-EN-LEndRight", "Z-EN-LStartLeft", "Z-EN-LEndLeft", "Z-EN-LStartRight", "Z-EN-LLStartLeft", "Z-EN-LLStartRight", "Z-EN-U", "Z-EN-N", "Z-EN-Unclean", "Z-WS-Clean", "Z-WS-LEndRight", "Z-WS-LStartLeft", "Z-WS-LEndLeft", "Z-WS-LStartRight", "Z-WS-LLStartLeft", "Z-WS-LLStartRight", "Z-WS-U", "Z-WS-N", "Z-WS-Unclean", "Z-WN-Clean", "Z-WN-LEndRight", "Z-WN-LStartLeft", "Z-WN-LEndLeft", "Z-WN-LStartRight", "Z-WN-LLStartLeft", "Z-WN-LLStartRight", "Z-WN-U", "Z-WN-N", "Z-WN-Unclean", "Z-SE-Clean", "Z-SE-LEndRight", "Z-SE-LStartLeft", "Z-SE-LEndLeft", "Z-SE-LStartRight", "Z-SE-LLStartLeft", "Z-SE-LLStartRight", "Z-SE-U", "Z-SE-N", "Z-SE-Unclean", "Z-SW-Clean", "Z-SW-LEndRight", "Z-SW-LStartLeft", "Z-SW-LEndLeft", "Z-SW-LStartRight", "Z-SW-LLStartLeft", "Z-SW-LLStartRight", "Z-SW-U", "Z-SW-N", "Z-SW-Unclean", "Z-NE-Clean", "Z-NE-LEndRight", "Z-NE-LStartLeft", "Z-NE-LEndLeft", "Z-NE-LStartRight", "Z-NE-LLStartLeft", "Z-NE-LLStartRight", "Z-NE-U", "Z-NE-N", "Z-NE-Unclean", "Z-NW-Clean", "Z-NW-LEndRight", "Z-NW-LStartLeft", "Z-NW-LEndLeft", "Z-NW-LStartRight", "Z-NW-LLStartLeft", "Z-NW-LLStartRight", "Z-NW-U", "Z-NW-N", "Z-NW-Unclean", "Straight-E-Unclean", "Straight-S-Unclean", "Straight-W-Unclean", "Straight-N-Unclean"};

    private static final LinkedHashMap<String, String> REGEXES = new LinkedHashMap<>();

    static {
        for (int i = 0; i < KEYS.length; i++) {
            REGEXES.put(KEYS[i], VALUES[i]);
        }
    }


    private static final String[] HEADERS;

    private final List<LayoutDirectionResult> results = new ArrayList<>();

    static {
        List<String> headers = new ArrayList<>(Arrays.asList(
                "SimplifiedVectorDirectionPerPath",
                "LayoutDirectionPerPath",
                "CombinationError",
                "LetterAccuracyError",
                "LayoutDirection",
                "OutDegMultiplicated",
                "NumberOfSplitsAndJoins",
                "NumberOfBoundaryEvents",
                "ProblematicBoundaryEvent",
                "HasSwimlaneChange",
                "NumberOfFlowNodes",
                "Error",
                "PortionOfFlowNodesAnalyzed",
                "AnalyzingTime"
        ));

        HEADERS = headers.toArray(new String[0]);
    }

    public void analyze(BpmnProcess p) {
        long startTime = System.nanoTime();
        LayoutDirectionResult result = new LayoutDirectionResult(p);
        List<Lane> lanes = p.getLanes();
        double combinationErrorSum = 0.0;
        double letterAccuracyErrorSum = 0.0;
        List<FlowNode> startFlowNodes = p.getStartFlowNodes();
        List<Trace> traces = new ArrayList<>();
        for (FlowNode startNode : startFlowNodes) {
            traces.addAll(getNonLoopingTracesToEnd(startNode));
        }

        result = setMetrics(p, traces, result);
        if (result == null) return;

        for (Trace trace : traces) {
            if (trace.hasCompleteLayoutData()) {
                VectorChain vectorChain = traceToVectorChain(trace, lanes, result);
                if (vectorChain.getVectors().size() != 0) {
                    vectorChain.simplify();
                    String pathDirections = vectorChain.getDiscreteVectorDirections();
                    result.addSimplifiedVectorDirectionPerPath(pathDirections);

                    determinePathLayout(vectorChain, pathDirections, result);


                    combinationErrorSum += vectorChain.getCombinationError();
                    letterAccuracyErrorSum += vectorChain.letterAccuracyError();
                }
            } else {
                result = new LayoutDirectionResult(p);
                result.setError(Error.NO_LAYOUT_DATA);
                results.add(result);
                return;
            }
        }
        if (result.getLayoutDirectionPerPath().size() == 0) {
            result = new LayoutDirectionResult(p);
            result.setError(Error.NO_START_ENDNODE_PAIR);
            results.add(result);
            return;
        }
        result.setCombinationError(combinationErrorSum / result.getLayoutDirectionPerPath().size());
        result.setLetterAccuracyError(letterAccuracyErrorSum / result.getLayoutDirectionPerPath().size());
        result.setAnalyzingTime(System.nanoTime() - startTime);
        result.calculateLayoutDirection();
        results.add(result);
    }

    private void determinePathLayout(VectorChain vectorChain, String discreteVectorDirections, LayoutDirectionResult result) {
        ArrayList<String> possiblePathLayouts = new ArrayList<>();
        matchDirections(discreteVectorDirections, possiblePathLayouts);
        if (possiblePathLayouts.size() == 0) {
            result.addLayoutDirectionPerPath("Other");
        } else if (possiblePathLayouts.size() == 1) {
            result.addLayoutDirectionPerPath(possiblePathLayouts.get(0));
        } else {
            if (possiblePathLayouts.stream().filter(s -> s.contains("Multiline")).count() == possiblePathLayouts.size()) {
                // only Multilines found
                discreteVectorDirections = multilineReplacement(discreteVectorDirections, vectorChain);

                possiblePathLayouts = new ArrayList<>();
                matchDirections(discreteVectorDirections, possiblePathLayouts);
            } else if (possiblePathLayouts.stream().filter(s -> s.contains("Snake")).count() == possiblePathLayouts.size()) {
                // only Snake found
                String newPathDirections = snakeReplacement(discreteVectorDirections, vectorChain);

                possiblePathLayouts = new ArrayList<>();
                matchDirections(newPathDirections, possiblePathLayouts);
            }
            if (possiblePathLayouts.size() == 0) {
                result.addLayoutDirectionPerPath("Other");
            } else {
                result.addLayoutDirectionPerPath(possiblePathLayouts.get(0));
            }
        }
    }

    private void matchDirections(String pathDirections, ArrayList<String> directions) {
        for (Map.Entry<String, String> entry : REGEXES.entrySet()) {
            if (!(entry.getKey().contains("-Unclean") && directions.size() != 0)) {
                if (pathDirections.matches(entry.getValue())) {
                    directions.add(entry.getKey());
                }
            }
        }
    }

    private VectorChain traceToVectorChain(Trace trace, List<Lane> lanes, LayoutDirectionResult result) {
        List<FlowNode> flowNodes = trace.getFlowNodes();
        VectorChain vectorChain = new VectorChain();
        FlowNode prevNode = flowNodes.get(0);
        for (int i = 1; i < flowNodes.size(); i++) {
            FlowNode thisNode = flowNodes.get(i);
            if (!thisNode.getType().equals("boundaryEvent")) {
                // does not go into BoundaryEvent
                Vector v = calculateFlowVector(prevNode, thisNode, lanes, vectorChain.lastVector(), trace, i, result);
                if (v.getLength() > 5) {
                    // Only add Vector if it is not really short to prevent backwards Vectors from othogonal Splits etc.
                    vectorChain.addVector(v);
                }
            }
            prevNode = thisNode;
        }
        return vectorChain;
    }

    private LayoutDirectionResult setMetrics(BpmnProcess p, List<Trace> traces, LayoutDirectionResult result) {
        result.setNumberOfFlowNodes(p.getFlowNodes().size());

        long outDegMultiplicated = 1;
        int numberOfSplitsAndJoins = 0;
        int numberOfBoundaryEvents = 0;
        for (FlowNode flowNode : p.getFlowNodes()) {
            if (flowNode.getOutgoingSequenceFlows().size() > 1) {
                outDegMultiplicated += flowNode.getOutgoingSequenceFlows().size() - 1;
                numberOfSplitsAndJoins++;
            }
            if (flowNode.getIncomingSequenceFlows().size() > 1) {
                numberOfSplitsAndJoins++;
            }
            if (flowNode.getAttachedTo() != null) {
                numberOfBoundaryEvents++;
            }
        }
        result.setOutDegMultiplicated(outDegMultiplicated);
        result.setNumberOfSplitsAndJoins(numberOfSplitsAndJoins);
        result.setNumberOfBoundaryEvents(numberOfBoundaryEvents);

        if (p.getStartFlowNodes().size() == 0) {
            result = new LayoutDirectionResult(p);
            result.setError(Error.NO_CLEAR_STARTNODE);
            results.add(result);
            return null;
        }

        if (p.getEndFlowNodes().size() == 0) {
            result = new LayoutDirectionResult(p);
            result.setError(Error.NO_CLEAR_ENDNODE);
            results.add(result);
            return null;
        }

        if (p.getSequenceFlows().size() == 0) {
            result = new LayoutDirectionResult(p);
            result.setError(Error.NO_SEQUENCE_FLOW);
            results.add(result);
            return null;
        }

        for (SequenceFlow sequenceFlow : p.getSequenceFlows()) {
            if (!sequenceFlow.hasLayoutData() || sequenceFlow.getWayPoints().getWaypoints() == null || sequenceFlow.getWayPoints().getWaypoints().size() == 0) {
                result = new LayoutDirectionResult(p);
                result.setError(Error.NO_LAYOUT_DATA);
                results.add(result);
                return null;
            }
            List<WayPoint> waypoints = sequenceFlow.getWayPoints().getWaypoints();
            FlowNode source = sequenceFlow.getSource();
            FlowNode target = sequenceFlow.getTarget();
            if (!source.hasLayoutData() || !target.hasLayoutData()) {
                result = new LayoutDirectionResult(p);
                result.setError(Error.NO_LAYOUT_DATA);
                results.add(result);
                return null;
            }
            if (isNotClose(source.getBounds(), new Vector(waypoints.get(0))) || isNotClose(target.getBounds(), new Vector(waypoints.get(waypoints.size() - 1)))) {
                result = new LayoutDirectionResult(p);
                result.setError(Error.NOT_ATTACHED_SEQUENCEFLOW);
                results.add(result);
                return null;
            }
        }

        Set<FlowNode> touchedNodes = new HashSet<>();
        for (Trace trace : traces) {
            touchedNodes.addAll(trace.getFlowNodes());
        }
        int notTouchedNodes = 0;
        for (FlowNode node : p.getFlowNodes()) {
            if (!touchedNodes.contains(node)) {
                notTouchedNodes++;
            }
        }
        result.setPortionOfFlowNodesAnalyzed((p.getFlowNodes().size() - notTouchedNodes) / (double) p.getFlowNodes().size());
        return result;
    }

    private String snakeReplacement(String pathDirections, VectorChain vectorChain) {
        StringBuilder result = new StringBuilder("" + pathDirections.charAt(0));
        for (int i = 1; i < pathDirections.length(); i++) {
            String character = "" + pathDirections.charAt(i);
            String prevCharacter = "" + pathDirections.charAt(i - 1);
            Vector centerOfVector = vectorChain.getVectors().get(i).getCenterPosition();
            Vector centerOfPrevVector = vectorChain.getVectors().get(i - 1).getCenterPosition();

            if (((prevCharacter.equals(E_M_L) || prevCharacter.equals(E_M_S)) && (character.equals(W_M_L) || character.equals(W_M_S))) || ((prevCharacter.equals(W_M_L) || prevCharacter.equals(W_M_S)) && (character.equals(E_M_L) || character.equals(E_M_S)))) {
                // horizontal
                if (centerOfVector.getY() > centerOfPrevVector.getY()) {
                    result.append(S_NM_S);
                } else {
                    result.append(N_NM_S);
                }
            }
            if (((prevCharacter.equals(S_M_L) || prevCharacter.equals(S_M_S)) && (character.equals(N_M_L) || character.equals(N_M_S))) || ((prevCharacter.equals(N_M_L) || prevCharacter.equals(N_M_S)) && (character.equals(S_M_L) || character.equals(S_M_S)))) {
                // vertical
                if (centerOfVector.getX() > centerOfPrevVector.getX()) {
                    result.append(E_NM_S);
                } else {
                    result.append(W_NM_S);
                }
            }
            result.append(character);
        }
        return result.toString();
    }

    private String multilineReplacement(String pathDirections, VectorChain vectorChain) {
        StringBuilder result = new StringBuilder("" + pathDirections.charAt(0));
        for (int i = 1; i < pathDirections.length(); i++) {
            String character = "" + pathDirections.charAt(i);
            Vector centerOfVector = vectorChain.getVectors().get(i).getCenterPosition();
            Vector centerOfPrevVector = vectorChain.getVectors().get(i - 1).getCenterPosition();

            switch (character) {
                case N_NM_L:
                    if (centerOfVector.getX() > centerOfPrevVector.getX()) {
                        result.append(NNE_NM_L);
                    } else {
                        result.append(NNW_NM_L);
                    }
                    break;
                case E_NM_L:
                    if (centerOfVector.getY() > centerOfPrevVector.getY()) {
                        result.append(SEE_NM_L);
                    } else {
                        result.append(NEE_NM_L);
                    }
                    break;
                case S_NM_L:
                    if (centerOfVector.getX() > centerOfPrevVector.getX()) {
                        result.append(SSE_NM_L);
                    } else {
                        result.append(SSW_NM_L);
                    }
                    break;
                case W_NM_L:
                    if (centerOfVector.getY() > centerOfPrevVector.getY()) {
                        result.append(SWW_NM_L);
                    } else {
                        result.append(NWW_NM_L);
                    }
                    break;
                default:
                    result.append(character);
                    break;
            }
        }
        return result.toString();
    }

    private boolean isNotClose(Bounds bounds, Vector point) {
        double threshold = 0.15; // 10% extended to each side
        return !(bounds.getX() - threshold * bounds.getWidth() <= point.getX()) || !(bounds.getX() + bounds.getWidth() + threshold * bounds.getWidth() >= point.getX())
                || !(bounds.getY() - threshold * bounds.getHeight() <= point.getY()) || !(bounds.getY() + bounds.getHeight() + threshold * bounds.getHeight() >= point.getY());
    }

    private Vector calculateFlowVector(FlowNode prevNode, FlowNode thisNode, List<Lane> lanes, Vector
            prevFlowVector, Trace trace, int indexOfThisNode, LayoutDirectionResult result) {
        Vector v = new Vector(prevNode.getBounds().getCenter(), thisNode.getBounds().getCenter(), false); // Idee ist, dass die Rückkante von Multiline mit nichts kombiniert werden kann
        v.setCenterPosition(prevNode.getBounds().getCenter().plus(v.divide(2.0)));

        if (prevNode.getType().equals("boundaryEvent")) {
            // comes out of BoundaryEvent
            setDimensionToZero(prevNode.isTopOrBottomBoundaryEvent(), v);
        }
        if (prevNode.getOutgoingSequenceFlows().size() > 1) {
            // comes out of Split
            if (prevFlowVector != null) {
                // the split node has an incoming SF
                setDimensionToZero(prevFlowVector.isHorizontal(), v); // only considers the prev SF from the Trace because that one is already adjusted for BoundaryEvent etc.
            } else {
                double averageDirection = averageDirection(
                        sequenceFlowsToElementConnectionVectors(prevNode.getOutgoingSequenceFlows().stream().filter(sf -> !trace.getFlowNodes().subList(0, indexOfThisNode - 1).contains(sf.getTarget())).collect(Collectors.toList())));
                setDimensionToZero(isHorizontal(averageDirection), v);
            }
        }
        if (thisNode.getIncomingSequenceFlows().size() > 1) {
            // goes into Join
            double averageDirection = 0;
            if (thisNode.getOutgoingSequenceFlows().size() != 0) {
                if (thisNode.getOutgoingSequenceFlows().size() > 1) {
                    // Join is also split
                    averageDirection = averageDirection(sequenceFlowsToElementConnectionVectors(thisNode.getIncomingSequenceFlows().stream().filter(sf -> !trace.getFlowNodes().subList(indexOfThisNode, trace.getFlowNodes().size()).contains(sf.getSource())).collect(Collectors.toList())));
                } else {
                    // the Join Node has an outgoing SF
                    boolean problemResolved = false;
                    for (int i = 1; i < trace.getFlowNodes().size() - indexOfThisNode; i++) {
                        FlowNode futureNode = trace.getFlowNodes().get(indexOfThisNode + i);
                        if (futureNode.getIncomingSequenceFlows().size() <= 1) {
                            // future Node is not a Join
                            if (futureNode.getIncomingSequenceFlows().size() != 0) {
                                // future Node is has incoming SFs. (is not a Boundary Event)
                                SequenceFlow sequenceFlow = futureNode.getIncomingSequenceFlows().get(0);
                                Vector vector = new Vector(sequenceFlow.getSource().getBounds().getCenter(), sequenceFlow.getTarget().getBounds().getCenter(), false);

                                Lane prevLane = getLane(trace.getFlowNodes().get(indexOfThisNode + i - 1), lanes);
                                Lane nextLane = getLane(futureNode, lanes);
                                if (prevLane != nextLane) {
                                    // Lane Change
                                    if (prevLane != null && nextLane != null && prevLane.hasLayoutData() && nextLane.hasLayoutData()) {
                                        setDimensionToZero(Math.abs(prevLane.getBounds().getX() - nextLane.getBounds().getX()) < Math.abs(prevLane.getBounds().getY() - nextLane.getBounds().getY()), vector);
                                    }
                                }
                                averageDirection = vector.getDirection();
                                problemResolved = true;
                            }
                        }
                    }
                    if (!problemResolved) {
//                    System.out.println("PROBLEM UNSOLVED");
                        averageDirection = averageDirection(sequenceFlowsToElementConnectionVectors(thisNode.getOutgoingSequenceFlows()));
                    }
                }

            } else {
                // the Join Node has no outgoing SF
                averageDirection = averageDirection(sequenceFlowsToElementConnectionVectors(thisNode.getIncomingSequenceFlows()));
            }
            setDimensionToZero(isHorizontal(averageDirection), v);
        }
        Lane prevLane = getLane(prevNode, lanes);
        Lane thisLane = getLane(thisNode, lanes);
        if (thisLane != prevLane) {
            // Lane Change
            if (prevLane != null && thisLane != null && prevLane.hasLayoutData() && thisLane.hasLayoutData()) {
                setDimensionToZero(Math.abs(prevLane.getBounds().getX() - thisLane.getBounds().getX()) < Math.abs(prevLane.getBounds().getY() - thisLane.getBounds().getY()), v);
            }
            result.setHasSwimlaneChange(true);
        }

        if (indexOfThisNode >= 2 && trace.getFlowNodes().get(indexOfThisNode - 2).getType().equals("boundaryEvent")) {
            if (v.isHorizontal() && prevFlowVector.getX() == 0 || !v.isHorizontal() && prevFlowVector.getY() == 0) {
                result.setProblematicBoundaryEvent(true);
            }
        }

        return v;
    }

    private List<Vector> sequenceFlowsToElementConnectionVectors(List<SequenceFlow> sequenceFlows) {
        return sequenceFlows.stream()
                .map(sequenceFlow -> {
                    FlowNode source = sequenceFlow.getSource();
                    FlowNode target = sequenceFlow.getTarget();
                    return new Vector(source.getBounds().getCenter(), target.getBounds().getCenter());
                }).collect(Collectors.toList());
    }

    /**
     * @param dimension true -> sets Y to zero, false -> sets X to zero
     */
    private void setDimensionToZero(boolean dimension, Vector v) {
        if (dimension) {
            v.setY(0);
        } else {
            v.setX(0);
        }
    }

    private boolean isHorizontal(double averageDirection) {
        return new Vector(Math.cos(averageDirection), Math.sin(averageDirection)).isHorizontal();
    }

    private double averageDirection(List<Vector> vectors) {
        return vectors.stream().mapToDouble(Vector::getDirection)
                .average().orElse(Double.NaN);
    }


    private Lane getLane(FlowNode node, List<Lane> lanes) {
        ArrayList<Lane> resultLanes = new ArrayList<>();
        if (node.hasLayoutData()) {
            for (Lane lane : lanes) {
                if (lane.hasLayoutData()) {
                    if (lane.contains(node.getBounds())) {
                        resultLanes.add(lane);
                    }
                }
            }
        }
        if (resultLanes.size() == 0) return null;
        Lane smallestLane = resultLanes.get(0);
        double smallestLaneSize = smallestLane.getBounds().getArea();
        for (Lane resultLane : resultLanes) {
            if (resultLane.getBounds().getArea() < smallestLaneSize) {
                smallestLane = resultLane;
                smallestLaneSize = smallestLane.getBounds().getArea();
            }
        }
        return smallestLane;
    }

    public List<Trace> getNonLoopingTracesToEnd(FlowNode fn) {
        Trace currentTrace = new Trace();
        currentTrace.addFlowNodeToTrace(fn);
        final List<Trace> result = new ArrayList<>();
        getNonLoopingTracesToEnd(currentTrace, result);
        return result;
    }

    private void getNonLoopingTracesToEnd(Trace currentTrace, List<Trace> traces) {
        FlowNode current = currentTrace.last();
        for (FlowNode following : current.getOutgoingSequenceFlows().stream().map(SequenceFlow::getTarget)
                .collect(Collectors.toList())) {
            if (!currentTrace.contains(following)) {
                Trace newTrace = new Trace(currentTrace);
                newTrace.addFlowNodeToTrace(following);
                getNonLoopingTracesToEnd(newTrace, traces);
            }
        }

        for (FlowNode boundaryEvent : current.getBoundaryEvents()) {
            if (!currentTrace.contains(boundaryEvent)) {
                Trace newTrace = new Trace(currentTrace);
                newTrace.addFlowNodeToTrace(boundaryEvent);
                getNonLoopingTracesToEnd(newTrace, traces);
            }
        }

        if (current.getOutgoingSequenceFlows().size() == 0 && current.getBoundaryEvents().size() == 0) {
            traces.add(currentTrace);
        }
    }

    public String getShortName() {
        return "layoutdirection";
    }

    public List<LayoutDirectionResult> getResults() {
        return results;
    }


    public String[] getHeaders() {
        return HEADERS;
    }
}
