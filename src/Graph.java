/**
 * SJSU Spring 2018 CS 146
 * Programming Project Maze
 * @author Ziqi Yuan, Dung Pham
 * @version 1.0
 */



import java.util.*;
import java.util.Stack;

public class Graph {
	private int dimension; // dimension of maze
	private Random myRand; // random number generator
	private Vertex vertexList[]; // store each Vertex
	private Vertex adjList[][]; // 2D representation

	private int adjMatrixSize; // size of the maze 2D[][]. Just dimension
								// actually
	private int totalVertices; // total vertices in maze

	public Graph(int dimension) {//<---------------------------------- No change made here
		int index = 0;
		this.dimension = dimension;
		this.vertexList = new Vertex[dimension * dimension];
		this.myRand = new Random();
		this.totalVertices = dimension * dimension;
		this.adjList = new Vertex[dimension][dimension];
		this.adjMatrixSize = adjList.length - 1;
		// generate 2D matrix. each cell is each vertex element
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				// assign value for each cell
				adjList[i][j] = new Vertex(i, j);
				vertexList[index] = new Vertex(i, j);
				index++;
			}
		}
		generateMaze();

	}

	/**
	 * random generator
	 *
	 * @return number from 0-1
	 */
	public double myRandom() { //<---------------------------------- No change made here
		return myRand.nextDouble();
	}

	/**
	 * generate the maze using DFS algorithm start from [0][0] by using DFS
	 * algorithm
	 */
	public void generateMaze() {//<-------------------------------------One change made in this method

		// Creates stack
		Stack<Vertex> cellStack = new Stack<Vertex>();
		//start from the beginning vertex
		Vertex currentCell = adjList[0][0];
		int visitedCell = 1;

		int totalCell = vertexList.length;

		while (visitedCell < totalCell) {
			// list of neighbors if all walls intact
			List<Vertex> neighbors = findAllNeighbors(currentCell);

			Vertex randVertex = currentCell;

			// continues go to next till end
			if (neighbors.size() > 0) {
				cellStack.push(currentCell);
				// random value will be from 0 - 3 since neighbors size is at most 4
				int randomValue = (int) (myRandom() * neighbors.size());
				currentCell = neighbors.get(randomValue);
				// knock down random cell and current cell
				removeWall(randVertex, currentCell); //<------------------------------------Parameters swapped by Ziqi Yuan
				//update new current cell = random cell
				currentCell.adjList.add(randVertex);
				randVertex.adjList.add(currentCell);
				// currentVertex = selectedVertex;
				visitedCell++;
			} else {
				currentCell = cellStack.pop();
			}
		}
		adjList[0][0].NORTH = false;
		adjList[adjMatrixSize][adjMatrixSize].SOUTH = false;
	}

	/**
	 * find all neighbors check if this cell has walls intact add to the list of
	 * neighbors
	 *
	 * @param currentCell
	 * @return array list typed vertex
	 */
	public List<Vertex> findAllNeighbors(Vertex current) {//<---------------------No change made in this method

		List<Vertex> neighbors = new ArrayList<Vertex>();
		// check right/down side <-------------------------------------After "/" is how I understand it
		if ((current.width < adjMatrixSize) && checkWallsIntact(adjList[current.width + 1][current.height])) {
			neighbors.add(adjList[current.width + 1][current.height]);
		}
		// check left/up side <----------------------------------------After "/" is how I understand it
		if ((current.width > 0) && checkWallsIntact(adjList[current.width - 1][current.height])) {
			neighbors.add(adjList[current.width - 1][current.height]);
		}
		// check up/right <-------------------------------------After "/" is how I understand it
		if ((current.height < adjMatrixSize) && checkWallsIntact(adjList[current.width][current.height + 1])) {
			neighbors.add(adjList[current.width][current.height + 1]);
		}
		// check down/left <-----------------------------------After "/" is how I understand it
		if ((current.height > 0) && checkWallsIntact(adjList[current.width][current.height - 1])) {
			neighbors.add(adjList[current.width][current.height - 1]);
		}
		return neighbors;

	}

	/**
	 * check walls intact
	 *
	 * @param v
	 * @return if current cell is covered by 4 walls
	 *
	 */
	public boolean checkWallsIntact(Vertex v) {//<-------------------No change made in this method
		return ((v.EAST == true) && (v.WEST == true) && (v.NORTH == true) && (v.SOUTH == true));
	}

	/**
	 * knock down walls
	 *
	 * @param current
	 * @param randVertex
	 */
	public void removeWall(Vertex current, Vertex randVertex) {// -----------------> Multiple changes made, surrounded by /* */

		// knock down current' east/down side <-----------------------------------After "/" is how I understand it
		if ((current.width < adjMatrixSize) && adjList[current.width + 1][current.height].equals(randVertex)) {
			/*current.EAST = false;
			randVertex.WEST = false;*/
			current.SOUTH=false;
			randVertex.NORTH=false;

		}
		// knock down current' west/up side<-----------------------------------After "/" is how I understand it
		if ((current.width > 0) && adjList[current.width - 1][current.height].equals(randVertex)) {
			/*current.WEST = false;
			randVertex.EAST = false;*/
			current.NORTH=false;
			randVertex.SOUTH=false;

		}
		// knock down current' north/left side<-----------------------------------After "/" is how I understand it
		if ((current.height > 0) && adjList[current.width][current.height - 1].equals(randVertex)) {
			/*current.NORTH = false;
			randVertex.SOUTH = false;*/
			current.WEST=false;
			randVertex.EAST=false;

		}
		// knock down current' south/right side<-----------------------------------After "/" is how I understand it
		if ((current.height < adjMatrixSize) && adjList[current.width][current.height + 1].equals(randVertex)) {
			/*current.SOUTH = false;
			randVertex.NORTH = false;*/
			current.EAST=false;
			randVertex.WEST=false;

		}
		//Following line of code is added by Ziqi
		current.children.add(randVertex); //Make the randVertex the child of current
		randVertex.parent=current;//Make the current the parent of randVertex
	}

	/**
	 * print maze
	 *
	 */
	public void printMaze() {//<--------------------I swapped the row and column identifier
		System.out.print("+ +");
		for (int i = 0; i < adjMatrixSize; i++) {
			System.out.print("-+");
		}
		System.out.println("");

		/*for (int i = 0; i <= adjMatrixSize; i++) {
			System.out.print("|");
			for (int j = 0; j < adjMatrixSize; j++) {
				if (adjList[j][i].EAST)
					System.out.print(" |");
				else
					System.out.print("  ");
			}
			System.out.print(" |");
			System.out.print("\n+");

			for (int k = 0; k <= adjMatrixSize; k++) {
				if (adjList[k][i].SOUTH)
					System.out.print("-+");
				else
					System.out.print(" +");
			}
			System.out.print("\n");
		}*/

		for (int i = 0; i <= adjMatrixSize; i++) {//<---------------------Replaced by this
			System.out.print("|");
			for (int j = 0; j < adjMatrixSize; j++) {
				if (adjList[i][j].EAST)
					System.out.print(" |");
				else
					System.out.print("  ");
			}
			System.out.print(" |");
			System.out.print("\n+");

			for (int k = 0; k <= adjMatrixSize; k++) {
				if (adjList[i][k].SOUTH)
					System.out.print("-+");
				else
					System.out.print(" +");
			}
			System.out.print("\n");
		}
		System.out.println();
		}



	/**
	 * Solve maze using BFS to to check the way it runs
	 * start from [0][0] till [size-1][size-1]
	 * Author:Richard Pham
	 */
	/*public void solveBFS(){
		// Create a queue for BFS to store all the vertex when enqueue
		LinkedList <Vertex> queue = new LinkedList<Vertex>();
		Queue<Vertex> queue1=new LinkedList<Vertex>();
		//check neighbor if visited
		boolean visited[] = new boolean[totalVertices];
		visited[0] = true;
		visited[totalVertices -1] = true;
		//current vertex start from 0
		Vertex current = vertexList[0];
		//enqueue first vertex.
		queue.add(current);
		while((queue.size()!= 0) && (queue.size() < totalVertices)){
			queue.remove(current);
			for(Vertex vertex : current.adjList)
			{
				queue.add(vertex);
				current.previous = current;
			}
		}
	}*/

	/**
	 * Solve maze using BFS technique
	 * It prints the order of rooms visited and final path
	 * Author:Ziqi Yuan
	 */
	public void solveBFS() {
		//Build a queue
		Queue<Vertex> queue=new LinkedList<Vertex>();
		//Build a int[][] to record the order the vertex is visited
		int[][] orderV=new int[dimension][dimension];
		int order=0;//Initialize order to zero
		Vertex currentV=adjList[0][0];// Start with the starting vertex
		orderV[currentV.width][currentV.height]=order;
		//Breath First Search begins
		queue.add(currentV);
		Vertex v;
		boolean stop=false;
		boolean empty=false;
		int i=0;
		order++;
		while(!empty || !stop ) {

		    while(i<currentV.children.size() && !stop) {

			v=currentV.children.get(i);
			if(v==adjList[dimension-1][dimension-1]) {
				stop=true;
			}
			 orderV[v.width][v.height]=order;
		    queue.add(v);
		    order++;

		       i++;
		    }
		    i=0;
		queue.remove();
		if(queue.peek()!=null)
		{currentV=queue.peek();}
		else {empty=true;}
		}

	//Print the order of rooms visited
		int row=0;
		System.out.print("+ +");

		for (int a = 0; a < adjMatrixSize; a++) {
			System.out.print("-+");
		}
		System.out.println("");//first line printed
		while(row<dimension) {
			System.out.print("|");
		for(int j=0;j<dimension;j++) {

			 if(orderV[row][j]==0 && (row!=0 || j!=0)) {
				 if(adjList[row][j].EAST==true) {
			        System.out.print(" |");}
			     else {System.out.print("  ");}}
			 else if(orderV[row][j]>=10) {
				 if(adjList[row][j].EAST==true) {
					 System.out.print(orderV[row][j]%10+"|");
				 }
				 else {System.out.print(orderV[row][j]%10+" ");}
			 }
			 else {
				 if(adjList[row][j].EAST==true) {
				 System.out.print(orderV[row][j]+"|");}
				 else {
					 System.out.print(+orderV[row][j]%10+" ");
				 }
			 }
		}

			System.out.println();

			//Print next line
		for(int j=0;j<dimension;j++) {
			if(adjList[row][j].SOUTH==true) {
				System.out.print("+-");
			}
			else {
				System.out.print("+ ");
			}

		}
		    System.out.print("+");
		    System.out.println();
			row++;
		}

		System.out.println();

	// Find the shortest path
    // Start from the finishing vertex, and find the complete shortest path
		//by finding the parent Vertex all the way back to the starting Vertex
	    Vertex current=adjList[dimension-1][dimension-1];
	    boolean STOP=false;
	    while(!STOP) {
	    	if(current==adjList[0][0]) {
	    		STOP=true;
	    	}
	    	current.inPath=true;
	    	current=current.parent;

	    }

	//Print the shortest path
	    int Row=0;
		System.out.print("+ +");

		for (int a = 0; a < adjMatrixSize; a++) {
			System.out.print("-+");
		}
		System.out.println("");//first line printed

		while(Row<dimension) {
			System.out.print("|");
		for(int j=0;j<dimension;j++) {

			 if(adjList[Row][j].inPath==true) {
				 if(adjList[Row][j].EAST==true) {
			        System.out.print("#|");}
			     else {System.out.print("# ");}}
	   else {
		   if(adjList[Row][j].EAST==true) {
		        System.out.print(" |");}
		     else {System.out.print("  ");}

			 }
		}

			System.out.println();

			//Print next line
		for(int j=0;j<dimension;j++) {
			if(adjList[Row][j].SOUTH==true) {
				System.out.print("+-");
			}
			else {
				System.out.print("+ ");
			}

		}
		    System.out.print("+");
		    System.out.println();
			Row++;
		}



	}//solveBFS() ends


	/**
	 * Solve maze using DFS to to check the way it runs
	 */
	public void solveDFS(){

	}


	/**
	 * Solve the maze
	 */
//	public void solveMaze(){
//		System.out.print("+ +");
//		for (int i = 0; i < adjMatrixSize ; i++)
//		{
//			System.out.print("-+");
//		}
//		System.out.println("");
//		for (int i = 0; i <= adjMatrixSize; i++)
//		{
//			System.out.print("|#");
//			for (int j = 0; j < adjMatrixSize ; j++)
//			{
//				if(adjList[j][i].EAST)
//					System.out.print("#|");
//				else
//					System.out.print("#");
//			}
//			System.out.print(" |");
//			System.out.print("\n+");
//		}
//	}

}
