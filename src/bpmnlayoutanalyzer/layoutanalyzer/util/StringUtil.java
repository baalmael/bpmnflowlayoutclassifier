package bpmnlayoutanalyzer.layoutanalyzer.util;

/**
 * From BPMN-Layout-Analyzer (edited by Elias Baalmann)
 */
public class StringUtil {

	public static boolean isNotEmpty(String s) {
		return s != null && !"".equals(s);
	}

	public static String toFirstUpper(String s) {
		if(s == null) {
			return null;
		} else if("".equals(s)) {
			return "";
		} else if(s.length() == 1) {
			return s.toUpperCase();
		} else {
			return s.substring(0, 1).toUpperCase() + s.substring(1);
		}
		
		
	}
	
	private StringUtil() {
	}
	
	
	
}
