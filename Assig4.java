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
	
	public int counter = 0;
	public int rows;
	public int cols;
	public int currSolCount = 0;
	public int [] pathRows;
	public int [] pathCols;

	public static void main(String [] args){
		new Assig4(args[0]);
	}
	
	public Assig4(String fileName){
		Scanner fReader = null;
		File fName;
		
		try{
			fName = new File(fileName);
            fReader = new Scanner(fName);
		}
		catch(Exception e){
            System.out.println(e);
		}
		
		// Parse input file to create 2-d grid of characters
		String [] dims = (fReader.nextLine()).split(" ");
		rows = Integer.parseInt(dims[0]);
		cols = Integer.parseInt(dims[1]);
		pathRows = new int[rows*cols];
		pathCols = new int[rows*cols];
		
		String [] startPoints = (fReader.nextLine()).split(" ");
		int rowStart = Integer.parseInt(startPoints[0]);
		int colStart = Integer.parseInt(startPoints[1]);
		
		int [][] theMaze = new int[rows][cols];

		for (int i = 0; i < rows; i++)
		{
			//String rowString = fReader.nextLine();
			String [] rowString = (fReader.nextLine()).split(" ");
			for (int j = 0; j < rowString.length; j++)
			{
				theMaze[i][j] = Integer.parseInt(rowString[j]);
			}
		}

		// Show user the grid
		System.out.println("-----------------------------------------");
		System.out.println("\nThe new board is:");
		
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < cols; j++)
			{
				System.out.print(theMaze[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println("\nSearching for solutions starting at (" + rowStart + "," + colStart + ")");
		
		//This ends the initial set up of the maze and display
		
		
		
		//while (!(word.equals(""))) This while loop WILL be used to find every solution?
		//{
			int x = rowStart, y = colStart;
		
			boolean found = false;
			for (int r = 0; (r < rows && !found); r++)
			{
				for (int c = 0; (c < cols && !found); c++)
				{
				// Start search for each position at index 0 of the word
					found = find2(r, c, 0, theMaze);
					if (found)
					{
						x = r;  // store starting indices of solution
						y = c;
					}
				}
			}

			if (found)
			{
				System.out.println("Solution Found with " + currSolCount + " segments");
				System.out.print("Path:");
				for(int i = 0; i < currSolCount; i++){
					System.out.print(" (" + pathRows[i] + "," + pathCols[i] + ")");
				}
				System.out.println();
				System.out.println();
				
				
				for (int i = 0; i < rows; i++)
				{
					for (int j = 0; j < cols; j++)
					{
						System.out.print(theMaze[i][j] + " ");
						//theMaze[i][j] = Character.toLowerCase(theMaze[i][j]); //Reset maze
					}
					System.out.println();
				}
			}
			else
			{
				System.out.println("No path from this point leads to the number 2");
			}
			
		//}
		
		
	}
	
	public boolean find2(int r, int c, int loc, int [][] bo)
	{
		//System.out.println("findWord: " + r + ":" + c + " " + word + ": " + loc); // trace code
		
		// Check boundary conditions
		if (r >= bo.length || r < 0 || c >= bo[0].length || c < 0)
			return false;
		else if (bo[r][c] != 0 && bo[r][c] != 2)  // char does not match
			return false;
		else  	// current character matches
		{
			int holder = bo[r][c];
			bo[r][c] = 8;  // Change to -1 so it leaves a path
			counter++;
			pathRows[loc] = r;
			pathCols[loc] = c;
				
			boolean answer;
			if (holder == 2){		//Looks like we found ol' #2
				answer = true;		
				currSolCount = loc + 1;
				bo[r][c] = holder;
			}				
				
			else	// Still have more letters to match, so recurse.
			{		// Try all four directions if necessary.
				answer = find2(r, c+1, loc+1, bo);  // Right
				if (!answer)
					answer = find2(r+1, c, loc+1, bo);  // Down
				if (!answer)
					answer = find2(r, c-1, loc+1, bo);  // Left
				if (!answer)
					answer = find2(r-1, c, loc+1, bo);  // Up

				if (!answer)
					bo[r][c] = 0; //Backtrack
			}
			return answer;
		}
	}
	
	
	
}