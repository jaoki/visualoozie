package visualoozie.api.action;

public class WorkflowNode {
    public enum NodeType{
        START
        , KILL
        , DECISION
        , FORK
        , JOIN
        , END
        , ACTION

    }
    private String name;
    private NodeType type;
    private String[] to;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public NodeType getType() { return type; }
    public void setType(NodeType type) { this.type = type; }

    public String[] getTo() { return to; }
    public void setTo(String[] to) { this.to = to; }

}
