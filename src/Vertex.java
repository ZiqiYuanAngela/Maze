/**
 * SJSU Spring 2018 CS 146
 * Programming Project Maze
 * @author Ziqi Yuan, Dung Pham
 * @version 1.0
 */







import java.util.ArrayList;
import java.util.LinkedList;
//Code below is from Richard Pham
public class Vertex {

	LinkedList<Vertex> adjList;
	String data;
	int flag;
	Vertex previous;
	int[] walls;
	int width;
	int height;
	//4 walls
	boolean NORTH = true;
    boolean SOUTH = true;
    boolean WEST = true;
    boolean EAST = true;
    ArrayList<Vertex> children;
    Vertex parent;
    boolean inPath;


    public Vertex(int width, int height){
    	children=new ArrayList<Vertex>();
    	this.width = width;
    	this.height= height;
    	walls = new int[4];
    	adjList = new LinkedList<Vertex>();
    	this.previous = null;
    	this.flag = -1;
    	parent=null;
    	inPath=false;
    }



}
