package it.unibas.iqmeter.model;

public class EffortGraphNode {

    public static final String TYPE_NODE_SOURCE_SCHEMA = "S";
    public static final String TYPE_NODE_TARGET_SCHEMA = "T";
    public static final String TYPE_NODE_METADATA_SCHEMA = "M";
    public static final String TYPE_NODE_FUNCTION = "F";
    public static final String TYPE_NODE_ANNOTATION = "A";
    public static final String TYPE_NODE_ANNOTATION_SCRIPT = "AS";
    public static final String TYPE_NODE_BOX = "B";
    private String type;
    private String label;
    private String nodeId;
    private String position;

    public EffortGraphNode(String type, String label, String nodeId) {
        this.type = type;
        this.label = label;
        this.nodeId = nodeId;
    }

    public EffortGraphNode(String type, String label, String nodeId, String position) {
        this.type = type;
        this.label = label;
        this.nodeId = nodeId;
        this.position = position;
    }

    public String getPosition() {
        return position;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String toString() {
        return "[" + type + "] " + label;
    }

    public boolean equals(EffortGraphNode e) {
        return nodeId.equals(e.nodeId);
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
