package bpmnlayoutanalyzer.layoutanalyzer.bpmnxml;

/**
 * From BPMN-Layout-Analyzer (edited by Elias Baalmann)
 */
public class InvalidBpmnXml extends RuntimeException {

	public InvalidBpmnXml(String msg, Throwable e) {
		super(msg, e);
	}

	public InvalidBpmnXml(String msg) {
		super(msg);
	}

}
