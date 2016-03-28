//Tyler Protivnak
//File Created 3/13/2016
//University of Pittsburgh - CS445
//Prof: John Ramirez, T/Th 9:30-10:45
//TA: Karin Cox, Tues 4-5
//
//Assig 4 file
//This is the main program for the assignment that deals with recursion and backtracking

import java.io.*;
import java.util.*;

public class Assig4{
	
	public long globalCount = 0;//Total number of calls
	public int rows;
	public int cols;
	public int currSolCount = 0;//Length of current solution path
	public int solCount = 0;//number of solutions
	public ArrayList<Integer> pathRows = new ArrayList<Integer>();//Path of solution
	public ArrayList<Integer> pathCols = new ArrayList<Integer>();
	public int x,y;
	public boolean globalTrue = false;//Test if there is an answer
	public char [][] theMaze;//Actual board that will be tested
	public int rowStart;
	public int colStart;
	
	//Group of shortest parts
	public int shortestPath;
	public ArrayList<Integer> shortRows = new ArrayList<Integer>();//Shortest path
	public ArrayList<Integer> shortCols = new ArrayList<Integer>();
	public char [][] theShortest;

	public static void main(String [] args){//command line example = java Assig4 data4-1.txt
		String fileName = args[0];
		Scanner fReader = null;
		File fName;
		try{//Try out the file
			fName = new File(fileName);
			fReader = new Scanner(fName);
		}
		catch(Exception e){
			System.out.println(e);
		}
		while(true){
			try{
				// Parse input file to create 2-d grid of characters
				String [] dims = (fReader.nextLine()).split(" ");
				int rs = Integer.parseInt(dims[0]);
				int cs = Integer.parseInt(dims[1]);
				int sPath = rs*cs;
				//Get start points
				String [] startPoints = (fReader.nextLine()).split(" ");
				int rStart = Integer.parseInt(startPoints[0]);
				int cStart = Integer.parseInt(startPoints[1]);
				char [][] tMaze = new char[rs][cs];
				
				for (int i = 0; i < rs; i++){
					String [] rowString = (fReader.nextLine()).split(" ");
					for (int j = 0; j < rowString.length; j++){
						tMaze[i][j] = rowString[j].charAt(0);
					}
				}
				System.out.println("\n\n");
				new Assig4(rs, cs, sPath, rStart, cStart, tMaze); //Create new object with a different matrix
			}
			catch(Exception e){
				break;
			}
		}	
	}
	
	public Assig4(int rs, int cs, int sPath, int rStart, int cStart, char [][] tMaze){
		rows = rs;
		cols = cs;
		rowStart = rStart;
		colStart = cStart;
		shortestPath = sPath;
		theMaze = tMaze;
		theShortest = new char[rows][cols];

		// Show user the grid
		System.out.println("-----------------------------------------");
		System.out.println("\nThe new board is:");
		for (int i = 0; i < rows; i++){
			for (int j = 0; j < cols; j++){
				System.out.print(theMaze[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println("\nSearching for solutions starting at (" + rowStart + "," + colStart + ")");		
		//This ends the initial set up of the maze and display

		find2(rowStart, colStart, 0, theMaze);//Start recurrsion
		if(!globalTrue){
			System.out.println("No path from this point leads to the number 2");
			System.out.println("A total of " + globalCount + " recursive calls were made");
		}
		else{
			System.out.println("\n\nThere were a total of " + solCount + " solutions found");
			System.out.println("A total of " + globalCount + " recursive calls were made");
			System.out.println("The shortest solution had " + shortestPath + " segments");
			
			for (int i = 0; i < rows; i++){//copy maze
				for (int j = 0; j < cols; j++){
					theShortest[i][j] = theMaze[i][j];
				}
			}
			
			for (int i = 0; i < shortestPath-1; i++){//Set the shortest maze
				theShortest[shortRows.get(i)][shortCols.get(i)] = 'x';
			}
			
			for (int i = 0; i < rows; i++){//Print maze
				for (int j = 0; j < cols; j++){
					System.out.print(theShortest[i][j] + " ");
				}
				System.out.println();
			}
			
			System.out.print("Path:");//Print that locations
			for(int i = 0; i < shortestPath; i++){
				System.out.print(" (" + shortRows.get(i) + "," + shortCols.get(i) + ")");
			}
		}
	}
	
	public void find2(int r, int c, int loc, char [][] bo){//Since I knew we were testing all possible routes, this is void and doesn't look for one answer
		globalCount++;
		char holder = bo[r][c];
		bo[r][c] = 'x';  // Change to x so it leaves a path
		pathRows.add(new Integer(r));//Add locations to path
		pathCols.add(new Integer(c));
			
		if (holder == '2'){		//Looks like we found ye ol' #2
			currSolCount = loc + 1;
			solCount++;
			bo[r][c] = '2';
			printSol(bo);
			
			if(shortestPath>loc){
				shortestPath = loc + 1;
				shortCols.clear();
				shortRows.clear();
				for(int i = 0; i<pathCols.size();i++){
					shortCols.add(new Integer(pathCols.get(i)));//Set path of shortest solution
					shortRows.add(new Integer(pathRows.get(i)));
				}
			}
		}				
		else{		// Try all four directions if necessary.
			if (!(c+1 >= bo[0].length) && !(bo[r][c+1] == '1') && !(bo[r][c+1] == 'x'))
				find2(r, c+1, loc+1, bo);  // Right
			if (!(r+1 >= bo.length) && !(bo[r+1][c] == '1') && !(bo[r+1][c] == 'x'))
				find2(r+1, c, loc+1, bo);  // Down
			if (!(c-1 < 0) && !(bo[r][c-1] == '1')  && !(bo[r][c-1] == 'x'))
				find2(r, c-1, loc+1, bo);  // Left
			if (!(r-1 < 0) && !(bo[r-1][c] == '1') && !(bo[r-1][c] == 'x'))
				find2(r-1, c, loc+1, bo);  // Up
			bo[r][c] = '0'; //Backtrack
		}
		pathRows.remove(pathRows.size()-1);//Delete item off of path since it is no longer used
		pathCols.remove(pathCols.size()-1);
	}
	
	public void printSol(char [][] theMaze){//Basic helper method to print method
		System.out.println("\nSolution Found with " + currSolCount + " segments");
		for (int i = 0; i < rows; i++){
			for (int j = 0; j < cols; j++){
				System.out.print(theMaze[i][j] + " ");
			}
			System.out.println();
		}
		System.out.print("Path:");
		for(int i = 0; i < currSolCount; i++){
			System.out.print(" (" + pathRows.get(i) + "," + pathCols.get(i) + ")");
		}
		System.out.println();
		globalTrue = true;
	}
}