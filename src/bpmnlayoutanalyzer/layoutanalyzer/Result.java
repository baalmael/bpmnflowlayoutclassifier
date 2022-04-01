package bpmnlayoutanalyzer.layoutanalyzer;


import bpmnlayoutanalyzer.layoutanalyzer.bpmnmodel.BpmnProcess;

import java.util.List;

/**
 * From BPMN-Layout-Analyzer (edited by Elias Baalmann)
 */
public abstract class Result {

	public Result(BpmnProcess p) {
		this(p.getFilename(), p.getDiagramIndex());
	}
	
	public Result(String filename, int digramIndex) {
		super();
		this.filename = filename;
		this.digramIndex = digramIndex;
	}
	
	public final String filename;
	public final int digramIndex;

	public abstract List<Object> getValues();

}
