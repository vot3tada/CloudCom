import java.util.ArrayList;
import java.util.*;
import java.util.Scanner;

enum Color {
    RED, GREEN
}

abstract class Tree {

    private int value;
    private Color color;
    private int depth;

    public Tree(int value, Color color, int depth) {
        this.value = value;
        this.color = color;
        this.depth = depth;
    }

    public int getValue() {
        return value;
    }

    public Color getColor() {
        return color;
    }

    public int getDepth() {
        return depth;
    }

    public abstract void accept(TreeVis visitor);
}

class TreeNode extends Tree {

    private ArrayList<Tree> children = new ArrayList<>();

    public TreeNode(int value, Color color, int depth) {
        super(value, color, depth);
    }

    public void accept(TreeVis visitor) {
        visitor.visitNode(this);

        for (Tree child : children) {
            child.accept(visitor);
        }
    }

    public void addChild(Tree child) {
        children.add(child);
    }
}

class TreeLeaf extends Tree {

    public TreeLeaf(int value, Color color, int depth) {
        super(value, color, depth);
    }

    public void accept(TreeVis visitor) {
        visitor.visitLeaf(this);
    }
}

abstract class TreeVis {
    public abstract int getResult();

    public abstract void visitNode(TreeNode node);

    public abstract void visitLeaf(TreeLeaf leaf);

}

class SumInLeavesVisitor extends TreeVis {
    int sum = 0;

    public int getResult() {
        return sum;
    }

    public void visitNode(TreeNode node) {
        return;
    }

    public void visitLeaf(TreeLeaf leaf) {
        sum += leaf.getValue();
    }
}

class ProductOfRedNodesVisitor extends TreeVis {
    int multi = 1;


    public int getResult() {
        return multi;
    }

    public void visitNode(TreeNode node) {
        if (node.getColor() == Color.RED) multi *= node.getValue();
    }

    public void visitLeaf(TreeLeaf leaf) {
        if (leaf.getColor() == Color.RED) multi *= leaf.getValue();
    }
}

class FancyVisitor extends TreeVis {
    int sumNodes = 0;
    int sumLeafs = 0;

    public int getResult() {
        return Math.abs(sumNodes - sumLeafs);
    }

    public void visitNode(TreeNode node) {
        if (node.getDepth() % 2 == 0) sumNodes += node.getValue();
    }

    public void visitLeaf(TreeLeaf leaf) {
        if (leaf.getColor() == Color.GREEN) sumLeafs += leaf.getValue();
    }
}

public class Solution {

    public static Tree solve() {
        //read the tree from STDIN and return its root as a return value of this function
        ArrayList<Integer> values = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();
        ArrayList<String[]> connections = new ArrayList<>();
        ArrayList<Tree> nodes = new ArrayList<>();
        Scanner scan = new Scanner(System.in);
        int size = scan.nextInt();
        for (int i = 0; i < size; i++) {
            values.add(scan.nextInt());
        }
        for (int i = 0; i < size; i++) {
            colors.add(scan.nextInt());
        }
        scan.nextLine();
        for (int i = 0; i < size - 1; i++) {
            connections.add(scan.nextLine().split(" "));
        }
        for (int i = 0; i < size; i++) {
            int depth = 0;
            int check = i + 1;
            Optional<String[]> filtered;
            while (true) {
                int Finalcheck = check;
                filtered = connections.stream()
                        .filter(con -> Integer.parseInt(con[1]) == Finalcheck)
                        .findFirst();
                if (filtered.isEmpty()) break;
                check = Integer.parseInt(filtered.get()[0]);
                depth += 1;
            }
            int Finalcheck = i + 1;
            if (connections.stream()
                    .mapToInt(con -> Integer.parseInt(con[0]))
                    .filter(con -> con == Finalcheck)
                    .findFirst()
                    .isPresent()) {
                nodes.add(new TreeNode(values.get(i).intValue(),
                        Color.values()[colors.get(i).intValue()], depth));
            } else {
                nodes.add(new TreeLeaf(values.get(i).intValue(),
                        Color.values()[colors.get(i).intValue()], depth));
            }
        }
        connections.forEach(con -> {
            ((TreeNode) nodes.get(Integer.parseInt(con[0]) - 1)).addChild(nodes.get(Integer.parseInt(con[1]) - 1));
        });
        for (int i = 1; i <= size; i++){
            System.out.println(i + " " + nodes.get(i - 1).getValue() + " " + nodes.get(i - 1).getClass() + " " + nodes.get(i - 1).getDepth());
        }
        System.out.println(nodes.stream().filter(node -> node.getClass() == TreeLeaf.class).mapToInt(x -> x.getValue()).sum());
        return nodes.get(0);
    }


    public static void main(String[] args) {
        Tree root = solve();
        SumInLeavesVisitor vis1 = new SumInLeavesVisitor();
        ProductOfRedNodesVisitor vis2 = new ProductOfRedNodesVisitor();
        FancyVisitor vis3 = new FancyVisitor();

        root.accept(vis1);
        root.accept(vis2);
        root.accept(vis3);

        int res1 = vis1.getResult();
        int res2 = vis2.getResult();
        int res3 = vis3.getResult();

        System.out.println(res1);
        System.out.println(res2);
        System.out.println(res3);
    }
}