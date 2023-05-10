package project;

import java.util.ArrayList;
import java.util.List;

public class MoveTree {
    private MoveTreeNode root;
    private MoveTreeNode current;

    public MoveTree() {
        root = new MoveTreeNode(null);
        current = root;
    }

    public Move getMove() {
        return current.getMove();
    }

    public void addMove(Move move) {
        MoveTreeNode node = new MoveTreeNode(move);
        current.addChild(node);
        current = node;
    }

    public void traverseUp(int steps) {
        for (int i = 0; i < steps; i++) {
            current.getMove().undoMove();
            current = current.getParent();
        }
    }

    public void traverseDown(int index) {
        List<MoveTreeNode> children = current.getChildren();
        if (index >= 0 && index < children.size()) {
            current = children.get(index);
            current.getMove().doMove();
        }
    }

    public void addAlternateMove(Move move) {
        List<MoveTreeNode> children = current.getParent().getChildren();
        int index = children.indexOf(current);
        if (index >= 0) {
            for (int i = index + 1; i < children.size(); i++) {
                if (children.get(i).getMove().equals(move)) {
                    current = children.get(i);
                    return;
                }
            }
            MoveTreeNode node = new MoveTreeNode(move);
            current.getParent().addChild(node);
            current = node;
        }
    }

    private static class MoveTreeNode {
        private final Move move;
        private final MoveTreeNode parent;
        private final List<MoveTreeNode> children;

        public MoveTreeNode(Move move) {
            this.move = move;
            this.parent = null;
            this.children = new ArrayList<>();
        }

        public void addChild(MoveTreeNode child) {
            children.add(child);
        }

        public MoveTreeNode getParent() {
            return parent;
        }

        public List<MoveTreeNode> getChildren() {
            return children;
        }

        public Move getMove() {
            return move;
        }
    }
}