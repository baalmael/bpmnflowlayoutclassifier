package bpmnlayoutanalyzer.layoutanalyzer.output;

/**
 * From BPMN-Layout-Analyzer
 */
public class CsvWriterOptions {

    private String fieldSeparator = ",";
    private String fieldSeparatorReplacement = ";";
    private String recordSeparator = "\n";
    private String recordSeparatorReplacement = "\\n";
    private boolean enforceSlashesAsPathSeparator = false;

    public String getFieldSeparator() {
        return fieldSeparator;
    }

    public void setFieldSeparator(String fieldSeparator) {
        this.fieldSeparator = fieldSeparator;
    }

    public String getFieldSeparatorReplacement() {
        return fieldSeparatorReplacement;
    }

    public void setFieldSeparatorReplacement(String fieldSeparatorReplacement) {
        this.fieldSeparatorReplacement = fieldSeparatorReplacement;
    }

    public String getRecordSeparatorReplacement() {
        return recordSeparatorReplacement;
    }

    public void setRecordSeparatorReplacement(String recordSeparatorReplacement) {
        this.recordSeparatorReplacement = recordSeparatorReplacement;
    }

    public String getRecordSeparator() {
        return recordSeparator;
    }

    public void setRecordSeparator(String recordSeparator) {
        this.recordSeparator = recordSeparator;
    }

    public boolean shallEnforceSlashesAsPathSeparator() {
        return enforceSlashesAsPathSeparator;
    }

    public void setEnforceSlashesAsPathSeparator(boolean enforceSlashesAsPathSeparator) {
        this.enforceSlashesAsPathSeparator = enforceSlashesAsPathSeparator;
    }
}
