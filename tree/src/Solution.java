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
    long multi = 1;
    long M = 1000000007;


    public int getResult() {
        return (int)multi;
    }

    public void visitNode(TreeNode node) {
        if (node.getColor() == Color.RED)
        {
            multi = multi * node.getValue() % M;
        }
    }

    public void visitLeaf(TreeLeaf leaf) {
        if (leaf.getColor() == Color.RED) {
            multi = multi * leaf.getValue() % M;
        }
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
        ArrayList<Integer> values = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();
        HashMap<Integer, ArrayList<Integer>> map = new HashMap<>();
        Scanner scan = new Scanner(System.in);
        int size = scan.nextInt();
        for (int i = 0; i < size; i++) {
            values.add(scan.nextInt());
        }
        for (int i = 0; i < size; i++) {
            colors.add(scan.nextInt());
        }
        for (int i = 0; i < size - 1; i++){
            int key1 = scan.nextInt();
            int key2 = scan.nextInt();

            if (map.containsKey(key1)){
                map.get(key1).add(key2);
            }
            else {
                map.put(key1, new ArrayList<>());
                map.get(key1).add(key2);
            }
            if (map.containsKey(key2)){
                map.get(key2).add(key1);
            }
            else {
                map.put(key2, new ArrayList<>());
                map.get(key2).add(key1);
            }
        }
        TreeNode tree = new TreeNode(values.get(0).intValue(), Color.values()[colors.get(0).intValue()],0);
        ArrayList<TreeNode> roots = new ArrayList<>();
        ArrayList<Integer> nums = new ArrayList<>();
        roots.add(tree);
        nums.add(1);
        while (!roots.isEmpty()){
            map.get(nums.get(0)).forEach(x -> {
                if (map.get(x).size() > 1){
                    TreeNode child = new TreeNode(values.get(x - 1).intValue(),
                            Color.values()[colors.get(x - 1).intValue()],roots.get(0).getDepth() + 1);
                    roots.get(0).addChild(child);
                    roots.add(child);
                    nums.add(x);
                    map.get(x).remove(nums.get(0));
                }
                else {
                    TreeLeaf child = new TreeLeaf(values.get(x - 1).intValue(),
                            Color.values()[colors.get(x - 1).intValue()],roots.get(0).getDepth() + 1);
                    roots.get(0).addChild(child);
                }
            });
            roots.remove(0);
            nums.remove(0);
        }
        return tree;
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
