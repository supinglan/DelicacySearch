package team.g3.delicacysearch.model;

import java.util.List;

public class KnowledgeGraph {
    private String rootId;
    private List<Node> nodes;
    private List<Line> lines;

    public KnowledgeGraph(String root, List<Node> nodes, List<Line> lines) {
        this.rootId = root;
        this.nodes = nodes;
        this.lines = lines;
    }

    public String getRootId() {
        return rootId;
    }

    public void setRootId(String root) {
        this.rootId = root;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public List<Line> getLines() {
        return lines;
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
    }

}
