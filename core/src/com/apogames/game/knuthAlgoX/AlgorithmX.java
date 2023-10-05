package com.apogames.game.knuthAlgoX;

import com.apogames.help.Helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class AlgorithmX {

    public static void main(String[] args) {
        //long start = System.nanoTime();
        AlgorithmX algorithmXSolve = new AlgorithmX();
        algorithmXSolve.run(5, 5);
    }

    private ColumnNode root = null;
    public ArrayList<Node> solution = new ArrayList<>();
    public ArrayList<byte[][]> allSolutions = new ArrayList<>();

    private int maxSolutions = 0;

    private byte[][] matrix;

    private int xSize;
    private int ySize;

    private int pieces;

    private int[] valueUntil;

    public void run(int xSize, int ySize) {
        this.matrix = createMatrix(xSize, ySize);

        this.run(xSize, ySize, this.pieces, this.matrix);
    }

    public byte[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(byte[][] matrix) {
        this.matrix = matrix;
    }

    public ColumnNode getRoot() {
        return root;
    }

    public ArrayList<Node> getSolution() {
        return solution;
    }

    public int getxSize() {
        return xSize;
    }

    public int getySize() {
        return ySize;
    }

    public ArrayList<byte[][]> run(int xSize, int ySize, int pieces, byte[][] matrix) {
        return run(xSize, ySize, pieces, matrix, -1);
    }

    public ArrayList<byte[][]> run(int xSize, int ySize, int pieces, byte[][] matrix, int maxSolutions) {
        this.xSize = xSize;
        this.ySize = ySize;
        this.pieces = pieces;

        this.matrix = matrix;
        this.valueUntil = getTileUntil(this.pieces);

        this.maxSolutions = maxSolutions;

        this.allSolutions.clear();
        createDoubleLinkedLists(matrix);
        search(0);
        System.out.println(this.allSolutions.size());
        return new ArrayList<>(this.allSolutions);
    }

    private byte[][] createMatrix(int xSize, int ySize) {
        MyPuzzleADayBinary myPuzzleADayBinary = new MyPuzzleADayBinary();

        byte[][] matrix = myPuzzleADayBinary.getMatrix(MyPuzzleADayBinary.GOAL);
        this.pieces = myPuzzleADayBinary.getAllTiles().size();
        return matrix;
    }

    private ColumnNode createDoubleLinkedLists(byte[][] matrix)
    {
        root = new ColumnNode(-1, 0); // the root is used as an entry-way to the linked list i.e. we access the list through the root
        // create the column heads
        ColumnNode curColumn = root;
        for(int col = 0; col < matrix[0].length; col++) // getting the column heads from the sparse matrix and filling in the information about the
        // constraints. We iterate for all the column heads, thus going through all the items in the first row of the sparse matrix
        {
            // We create the ColumnID that will store the information. We will later map this ID to the current curColumn
            curColumn.right = new ColumnNode(col, 0);
            curColumn.right.left = curColumn;
            curColumn = (ColumnNode)curColumn.right;
            curColumn.head = curColumn;
        }
        curColumn.right = root; // making the list circular i.e. the right-most ColumnHead is linked to the root
        root.left = curColumn;

        // Once all the ColumnHeads are set, we iterate over the entire matrix
        // Iterate over all the rows
        for(int row = 0; row < matrix.length; row++) {
            // iterator over all the columns
            curColumn = (ColumnNode)root.right;
            Node lastCreatedElement = null;
            Node firstElement = null;
            for(int col = 0; col < matrix[row].length; col++) {
                if(matrix[row][col] == 1)  {
                    Node colElement = curColumn;
                    while(colElement.down != null)
                    {
                        colElement = colElement.down;
                    }
                    colElement.down = new Node(col, row);
                    if(firstElement == null) {
                        firstElement = colElement.down;
                    }
                    colElement.down.up = colElement;
                    colElement.down.left = lastCreatedElement;
                    colElement.down.head = curColumn;
                    if(lastCreatedElement != null)
                    {
                        colElement.down.left.right = colElement.down;
                    }
                    lastCreatedElement = colElement.down;
                    curColumn.size++;
                }
                curColumn = (ColumnNode)curColumn.right;
            }
            if(lastCreatedElement != null)
            {
                lastCreatedElement.right = firstElement;
                firstElement.left = lastCreatedElement;
            }
        }
        curColumn = (ColumnNode)root.right;
        for(int i = 0; i < matrix[0].length; i++)
        {
            Node colElement = curColumn;
            while(colElement.down != null)
            {
                colElement = colElement.down;
            }
            colElement.down = curColumn;
            curColumn.up = colElement;
            curColumn = (ColumnNode)curColumn.right;
        }
        return root;
    }

    private void search(int k)
    {
        if (this.allSolutions.size() >= this.maxSolutions && this.maxSolutions > 0) {
            //System.out.println("Argh");
            return;
        }
        if(root.right == root) { // || (root.right.x >= 2 * (xSize * ySize))) {// && isSolved())) {// || this.solution.size() == 9) {
            showSolution();
            return;
        }
        ColumnNode c = choose(); // we choose a column to cover
        cover(c);
        Node r = c.down;
        //System.out.println("r = "+r.x+" "+r.y);
        //showSolution();
        //System.out.println();

        while(r != c)
        {
            if(k < solution.size())
            {
                solution.remove(k); // if we had to enter this loop again
            }
            solution.add(k,r); // the solution is added

            Node j = r.right;
            while(j != r) {
                cover(j.head);
                j = j.right;
            }
            search(k+1); //recursively search

            Node r2 = (Node)solution.get(k);
            Node j2 = r2.left;
            while(j2 != r2) {
                uncover(j2.head);
                j2 = j2.left;
            }
            r = r.down;
        }
        //System.out.println("uncover ");
        uncover(c);
    }

    private void showSolution() {
        byte[][] solutionArray = new byte[ySize][xSize];
        for (Node node : this.solution) {
            if (node.getY() < this.matrix.length - 1) {
                byte value = getValueForSolution(this.valueUntil, node);
                for (int matrixY = 0; matrixY < this.matrix[0].length - this.pieces; matrixY += 1) {
                    if (matrixY < xSize * ySize && this.matrix[node.getY()][matrixY] == 1) {
                        int x = matrixY % xSize;
                        int y = matrixY / xSize;
                        if (value > 7) {
                            value = 7;
                        }
                        solutionArray[y][x] = value;
                    }
                }
            }
        }

        for (int i = this.allSolutions.size() - 1; i >= 0; i--) {
            if (Helper.equal(this.allSolutions.get(i), solutionArray)) {
                return;
            }
        }

//        for (byte[] row : solutionArray) {
//            System.out.println(Arrays.toString(row));
//        }
//        System.out.println();

        this.allSolutions.add(solutionArray);
    }

    private byte getValueForSolution(int[] valueUntil, Node node) {
        for (int i = 0; i < valueUntil.length; i++) {
            if (node.getY() < valueUntil[i]) {
                return (byte)(i+1);
            }
        }
        return 0;
    }

    public int[] getTileUntil(int pieces) {
        int[] valueUntil = new int[pieces];

        int start = 0;
        int index = 0;
        for (int piece = this.matrix[0].length - pieces; piece < this.matrix[0].length; piece++) {
            for (int y = start; y < this.matrix.length; y++) {
                if (this.matrix[y][piece] != 1) {
                    break;
                } else {
                    start += 1;
                }
            }
            valueUntil[index] = start;
            index += 1;
        }

        return valueUntil;
    }

    private ColumnNode choose() {
        ColumnNode rightOfRoot = (ColumnNode)root.right;
        ColumnNode smallest = rightOfRoot;
        while(rightOfRoot.right != root) {
            rightOfRoot = (ColumnNode)rightOfRoot.right;
            int x = rightOfRoot.x;
            if ((/*x < 2 * xSize * ySize && */rightOfRoot.size < smallest.size)) {// || (rightOfRoot.size > 0 && rightOfRoot.size < smallest.size)){
                smallest = rightOfRoot;
            }
        }
        return smallest;
    }

    // covers the column; used as a helper method for the search method. Pseudo code by Jonathan Chu (credited above)
    private void cover(Node column) {
        column.right.left = column.left;
        column.left.right = column.right;

        Node curRow = column.down;
        while(curRow != column) {
            Node curNode = curRow.right;
            while(curNode != curRow) {
                curNode.down.up = curNode.up;
                curNode.up.down = curNode.down;
                curNode.head.size--;
                curNode = curNode.right;
            }
            curRow = curRow.down;
        }
    }

    private void uncover(Node column) {
        Node curRow = column.up;
        while(curRow != column) {
            Node curNode = curRow.left;
            while(curNode != curRow) {
                curNode.head.size++;
                curNode.down.up = curNode;
                curNode.up.down = curNode;
                curNode = curNode.left;
            }
            curRow = curRow.up;
        }
        column.right.left = column;
        column.left.right = column;
    }

}
