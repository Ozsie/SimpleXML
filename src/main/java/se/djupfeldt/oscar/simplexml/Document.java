package se.djupfeldt.oscar.simplexml;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ozsie on 14/07/16.
 */
public class Document {
    private Node root;

    public Document(Node root) {
        this.root = root;
    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    public List<Node> getNodesByName(String name) {
        return getNodesByName(name, root);
    }

    public List<Node> getNodesByName(String name, Node startingPoint) {
        List<Node> nodes = new ArrayList<>();
        for (Object obj : startingPoint.getChildren()) {
            if (obj instanceof Node) {
                Node node = (Node) obj;
                if (node.getName().equalsIgnoreCase(name)) {
                    nodes.add(node);
                }
                nodes.addAll(getNodesByName(name, node));
            }
        }
        return nodes;
    }

    @Override
    public String toString() {
        return root.toString();
    }

    public String toFormattedString() {
        return root.toFormattedString();
    }
}
