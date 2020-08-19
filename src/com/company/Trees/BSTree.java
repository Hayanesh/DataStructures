package com.company.Trees;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
* Properties
* ===========
* left node < root > right node
*           8
*        /    \
*       3      10
*     /  \      \
*    1   6      14
*       / \     /
*      4   7   13
*
* InOrder : 1 -> 3 -> 4 -> 6 -> 7 -> 8 -> 10 -> 13 -> 14 (Sorted Ascending)
* PreOrder : 8 -> 3 -> 1 -> 6 -> 4 -> 7 -> 10 -> 14 -> 13
* PostOrder : 1 -> 4 -> 7 -> 6 -> 3 -> 13 -> 14 -> 10 -> 8
*
* */
public class BSTree {
    private class Node {
        int value;
        Node left, right;

        public Node(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
    private Node root;

    public void insert(int value){
        root = this.insert(root, value);
    }

    public int height(){
        return height(root);
    }

    public boolean isPresent(int value){
        return isPresent(root, value);
    }

    /**
     * Left -> Root -> Right
     */
    public void printInOrder(){
        List<Node> nodes = inOrder(root, new ArrayList<>());
        displayNodes(nodes);
    }

    /**
     * Root -> Left -> Right
     */
    public void printPreOrder(){
        List<Node> nodes = preOrder(root, new ArrayList<>());
        displayNodes(nodes);
    }

    /**
     * Left -> Right -> Root
     */
    public void printPostOrder(){
        List<Node> nodes = postOrder(root, new ArrayList<>());
        displayNodes(nodes);
    }

    public List<Integer> levelOrder(){
        List<Node> nodes = new ArrayList<>();
        for(int i = 0; i <= height(root); i++){
            getNodesAt(root, i, nodes);
        }
        displayNodes(nodes);
        return nodes.stream()
                .map(Node::getValue)
                .collect(Collectors.toList());
    }

    public void print(){
        //TODO
    }

    public void printNodesAt(int distance){
        if(height(root) < distance)
            System.out.println("Distance is greater than the height of the tree");
        List<Node> nodes = getNodesAt(root, distance, new ArrayList<>());
        displayNodes(nodes);
    }

    public void findAndDelete(int value){
        root = this.findAndDelete(root, value);
    }

    /** =<Private>= **/

    private Predicate<Node> isLeaf = node -> node.left == null && node.right == null;

    private Node insert(Node node, int value){
        if(node == null)
            return new Node(value);

        if(value < node.value)
            node.left = insert(node.left, value);
        else
            node.right = insert(node.right, value);

        return node;
    }

    private List<Node> inOrder(Node node, List<Node> sortedNodes){
        if(node == null)
            return null;

        this.inOrder(node.left, sortedNodes);
        sortedNodes.add(node);
        this.inOrder(node.right, sortedNodes);

        return sortedNodes;
    }

    private List<Node> preOrder(Node node, List<Node> sortedNodes){
        if(node == null)
            return null;

         sortedNodes.add(node);
         this.preOrder(node.left,sortedNodes);
         this.preOrder(node.right, sortedNodes);
         return sortedNodes;
    }

    private List<Node> postOrder(Node node, List<Node> sortedNodes){
        if(node == null)
            return null;

        this.postOrder(node.left, sortedNodes);
        this.postOrder(node.right, sortedNodes);
        sortedNodes.add(node);
        return sortedNodes;
    }

    private int height(Node node){
        if(node == null)
            return -1;

        if(isLeaf.test(node))
            return 0;

        return 1 + Math.max(height(node.left), height(node.right));
    }

    private List<Node> getNodesAt(Node node, int distance, List<Node> nodes){
        if(node == null)
            return null;

        if(distance == 0)
            nodes.add(node);

        getNodesAt(node.left, distance - 1, nodes);
        getNodesAt(node.right, distance -1 , nodes);

        return nodes;
    }

    private void displayNodes(List<Node> nodes){
        System.out.println(
                nodes.stream()
                        .map(n -> Integer.toString(n.getValue()))
                        .collect(Collectors.joining(" -> "))
        );
    }

    private boolean isPresent(Node node, int value){
        if(isLeaf.test(node))
            return false;

        if(node.value == value)
            return true;

        return (value < node.value) ?
                isPresent(node.left, value) :
                isPresent(node.right, value);
    }

    //TODO if node == root this won't work
    private Node findAndDelete(Node node, int value){
        if(node == null) return node;

        if(value < node.value){
            node.left = findAndDelete(node.left, value);
        }
        else if(value > node.value) {
            node.right = findAndDelete(node.right, value);
        }
        else{
            if(isLeaf.test(node))
                return null;

            return Optional.ofNullable(getMaxNeighbour(node.right))
                    .orElse(node.left);
        }
        return node;
    }


    private void bubbleUp(Node node, int value){
        if (node.left != null && node.left.getValue() == value)
            node.left = getMaxNeighbour(node.left);
        else
            node.right = getMaxNeighbour(node.right);
    }

    private Node getMaxNeighbour(Node node){
        if(node == null || (node.left == null && node.right == null))
            return null;

        // Inorder (L, Root, R)
        return Stream.of(node.left, node, node.right)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    public static final boolean DEBUG = true;

    public static void main(String[] args) {
        BSTree tree = new BSTree();
        List<Integer> numbers;
        Scanner sc = new Scanner(System.in);
        if(DEBUG == true){
            numbers = Arrays.asList(8 , 3, 10, 1, 6, 4, 7, 14, 13);
            numbers.forEach(tree::insert);
            System.out.println("LevelOrder");
            tree.levelOrder();
            tree.findAndDelete(8);
            System.out.println("LevelOrder");
            tree.levelOrder();
        }else {
            System.out.println("Enter the node values...");
            String input = sc.nextLine();
            numbers = input.transform(str -> Arrays.asList(str.split(" ")))
                    .stream()
                    .peek(System.out::println)
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            numbers.forEach(tree::insert);
            System.out.println("InOrder");
            tree.printInOrder();

            System.out.println("PreOrder");
            tree.printPreOrder();

            System.out.println("PostOrder");
            tree.printPostOrder();

            System.out.println("LevelOrder");
            tree.levelOrder();

            System.out.println("Height of the tree : " + tree.height());

            System.out.println("Enter the element to search for...");
            int value = sc.nextInt();
            System.out.println(tree.isPresent(value) ? "Present" : "Not present");

            System.out.println("Enter the distance...");
            int distance = sc.nextInt();
            tree.printNodesAt(distance);

            System.out.println("Enter the node to be deleted");
            tree.findAndDelete(sc.nextInt());
        }
    }
}
