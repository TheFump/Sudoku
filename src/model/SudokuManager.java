package model;

import java.util.ArrayList;
import java.util.Random;

public class SudokuManager {
	private static final int MAX_LINE_OR_COLUMN_SIZE=9;
	private static final int AREA_NUMBER = 9;
	private static final int MIN_RANDOM = 9;
	private static final int MAX_RANDOM = 81;
	private static final int LINE_OR_COLUMN_NUMBER_PER_AREA=3;
	
	
	public static SudokuModel generateSudoku(){
		//TODO generate sudoku
		SudokuModel tSudoku = new SudokuModel();
		
		//Generate the number of fixed cases
		Random tRandomGenerator = new Random();
		int tNumberFixedCases = MIN_RANDOM + tRandomGenerator.nextInt(MAX_RANDOM); 
		
		//the first nineth should be in all the area
		int tAreaIndex=0;
		while(tAreaIndex<AREA_NUMBER){
			AreaModel tArea=tSudoku.getAreaList().get(tAreaIndex);
			int tCaseLine=tRandomGenerator.nextInt(LINE_OR_COLUMN_NUMBER_PER_AREA);
			int tCaseColumn=tRandomGenerator.nextInt(LINE_OR_COLUMN_NUMBER_PER_AREA);
			if(tArea.getCase(tCaseLine, tCaseColumn).getValue()==0){
				//Generate a random number
				int tRandom=1+tRandomGenerator.nextInt(MAX_LINE_OR_COLUMN_SIZE);
				//TODO check if correct case
				if(true){
					tArea.getCase(tCaseLine, tCaseColumn).setValue(tRandom, true);
					tAreaIndex++;
					tNumberFixedCases--;
				}
			}
		}
		
		while(tNumberFixedCases>0){
			//Choose an area
			int tRandomArea=tRandomGenerator.nextInt(AREA_NUMBER);
			AreaModel tArea=tSudoku.getAreaList().get(tRandomArea);
			
			//Choose a case
			int tCaseLine=tRandomGenerator.nextInt(LINE_OR_COLUMN_NUMBER_PER_AREA);
			int tCaseColumn=tRandomGenerator.nextInt(LINE_OR_COLUMN_NUMBER_PER_AREA);
			
			//Check if it's the right place to put the number
			if(tArea.getCase(tCaseLine, tCaseColumn).getValue()==0){
				//Generate a random number
				int tRandom=1+tRandomGenerator.nextInt(MAX_LINE_OR_COLUMN_SIZE);
				//TODO check if correct case
				if(true){
					tArea.getCase(tCaseLine, tCaseColumn).setValue(tRandom, true);
					tNumberFixedCases--;
				}
			}
			
		}
		
		return tSudoku;
	}
	
	/**
	 * Create an ArrayList containing all numbers between 1 and 9
	 */
	public static ArrayList<Integer> createResultModel(){
		ArrayList<Integer> tResult= new ArrayList<>();
		for(int tIndex=1; tIndex<=MAX_LINE_OR_COLUMN_SIZE; tIndex++){
			tResult.add(tIndex);
		}
		return tResult;
	}
}
