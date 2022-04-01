package bpmnlayoutanalyzer;

import bpmnlayoutanalyzer.flowlayout.FlowLayoutAnalyzer;
import bpmnlayoutanalyzer.layoutanalyzer.bpmnmodel.BpmnProcess;
import bpmnlayoutanalyzer.layoutanalyzer.bpmnxml.BpmnLayoutSetter;
import bpmnlayoutanalyzer.layoutanalyzer.bpmnxml.BpmnReader;
import bpmnlayoutanalyzer.layoutanalyzer.output.CsvResultWriter;
import bpmnlayoutanalyzer.layoutanalyzer.output.CsvWriterOptions;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

/**
 * From BPMN-Layout-Analyzer (edited by Elias Baalmann)
 */
public class BpmnLayoutAnalyzer {


    private final FlowLayoutAnalyzer analyzer = new FlowLayoutAnalyzer();
    private long timeSpentInNanosOnAnalyzer = 0L;

    private final BpmnLayoutSetter bpmnLayoutSetter = new BpmnLayoutSetter();

    public void analyze(File bpmnModelFile) throws SAXException, IOException {
        try {
            BpmnProcess process = new BpmnReader().readProcess(bpmnModelFile.getPath());

            int diagramCount = bpmnLayoutSetter.getDiagramCount(process);
            for (int i = 0; i < diagramCount; i++) {
                analyzeDiagram(process, i);
            }
        } catch (Exception e) {
            System.err.print(bpmnModelFile.getAbsolutePath() + ": ");
            e.printStackTrace();
            throw e;
        }
    }

    private void analyzeDiagram(BpmnProcess process, int diagramIndex) {
        bpmnLayoutSetter.setLayoutData(process, diagramIndex);
        long start = System.nanoTime();
        analyzer.analyze(process);
        timeSpentInNanosOnAnalyzer = System.nanoTime() - start;
    }

    public void writeReport(String baseName, CsvWriterOptions options) throws IOException {
        try (CsvResultWriter out = new CsvResultWriter(baseName + "." + analyzer.getShortName() + ".csv", options)) {
            out.writeHeader(analyzer.getHeaders());
            out.writeRecords(analyzer.getResults());
        }
        System.out.println("Overall analyzing time: " + timeSpentInNanosOnAnalyzer + "ns");
    }

}
