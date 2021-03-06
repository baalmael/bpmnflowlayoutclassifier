package bpmnlayoutanalyzer.layoutanalyzer.output;

import bpmnlayoutanalyzer.layoutanalyzer.Result;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * From BPMN-Layout-Analyzer (edited by Elias Baalmann)
 */
public class CsvResultWriter implements AutoCloseable {

    private final CsvWriterOptions options;
    private final FileWriter writer;

    public CsvResultWriter(String filename) throws IOException {
        this(filename, new CsvWriterOptions());
    }

    public CsvResultWriter(String filename, CsvWriterOptions options) throws IOException {
        this.options = options;
        this.writer = new FileWriter(filename);
    }


    public void writeHeader(String... headers) throws IOException {
        String[] values = new String[headers.length + 4];

        values[0] = "Filename";
        values[1] = "DiagramIndex";
        System.arraycopy(headers, 0, values, 2, headers.length);

        writeRecord(values);
    }

    public void writeRecords(List<? extends Result> results) throws IOException {
        for (Result r : results) {
            writeRecord(r);
        }
    }

    public void writeRecord(Result r) throws IOException {
        List<Object> valuesFromResult = r.getValues();
        String[] allValues = new String[valuesFromResult.size() + 4];

        allValues[0] = sanitize(r.filename);
        if (allValues[0] != null && options.shallEnforceSlashesAsPathSeparator()) {
            allValues[0] = allValues[0].replaceAll("\\\\", "/");
        }
        allValues[1] = Integer.toString(r.digramIndex);
        for (int i = 0; i < valuesFromResult.size(); i++) {
            if (valuesFromResult.get(i) != null) {
                allValues[i + 2] = sanitize(valuesFromResult.get(i).toString());
            } else {
                allValues[i + 2] = "";
            }
        }

        writeRecord(allValues);
    }

    private String sanitize(String string) {
        if (string == null)
            return null;

        return string
                .replace(options.getFieldSeparator(), options.getFieldSeparatorReplacement())
                .replace(options.getRecordSeparator(), options.getRecordSeparatorReplacement())
                ;
    }

    private void writeRecord(String[] values) throws IOException {
        writer.write(String.join(options.getFieldSeparator(), values));
        writer.write(options.getRecordSeparator());
    }

    @Override
    public void close() throws IOException {
        writer.close();
    }

}
