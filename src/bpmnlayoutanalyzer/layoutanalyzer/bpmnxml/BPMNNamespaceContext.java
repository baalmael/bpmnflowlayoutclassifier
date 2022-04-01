package bpmnlayoutanalyzer.layoutanalyzer.bpmnxml;

import javax.xml.namespace.NamespaceContext;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * From BPMN-Layout-Analyzer (edited by Elias Baalmann)
 */
public class BPMNNamespaceContext implements NamespaceContext {

    public static final NamespaceContext DEFAULT = new BPMNNamespaceContext();

    private final Map<String, String> namespaceByUri = new HashMap<>();
    private final Map<String, String> namespaceByPrefix = new HashMap<>();

    public BPMNNamespaceContext() {
        addNamespace("http://www.omg.org/spec/BPMN/20100524/MODEL", "bpmn");
        addNamespace("http://www.omg.org/spec/BPMN/20100524/DI", "bpmndi");
        addNamespace("http://www.omg.org/spec/DD/20100524/DI", "di");
        addNamespace("http://www.omg.org/spec/DD/20100524/DC", "dc");
    }

    private void addNamespace(String uri, String prefix) {
        namespaceByUri.put(uri, prefix);
        namespaceByPrefix.put(prefix, uri);
    }

    public String getNamespaceURI(String prefix) {
        return namespaceByPrefix.get(prefix);
    }

    public String getPrefix(String uri) {
        return namespaceByUri.get(uri);
    }

    @Override
    public Iterator<String> getPrefixes(String namespaceURI) {
        return null;
    }


}
