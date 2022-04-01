package bpmnlayoutanalyzer.layoutanalyzer.bpmnmodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * From BPMN-Layout-Analyzer (edited by Elias Baalmann)
 */
public class WayPointList {

	private final List<WayPoint> waypoints = new ArrayList<>();
	
	public WayPointList() {
	}
	
	public WayPointList(List<WayPoint> waypoints) {
		this.waypoints.addAll(waypoints);
	}

	public void add(WayPoint wp) {
		waypoints.add(wp);
	}
	
	public List<WayPoint> getWaypoints() {
		return Collections.unmodifiableList(waypoints);
	}
}
