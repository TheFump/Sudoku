package model;

import java.util.ArrayList;

import sun.rmi.transport.tcp.TCPConnection;

public class SudokuModel {
	/**
	 * CONSTANTS
	 */
	private static final int MAX_AREA = 9;
	private static final int MAX_AREA_PER_LINE_AND_COLUMN = 3;
	private static final int MAX_CASE_PER_AREA_LINE_AND_COLUMN = 3;
	
	/**
	 * ATTRIBUTES AND METHODS
	 */
	
	private ArrayList<AreaModel> mAreaList;
	
	public SudokuModel(){
		mAreaList=new ArrayList<>();
		//Set all the area
		for(int tIndex=0; tIndex< MAX_AREA; tIndex++){
			mAreaList.add(new AreaModel());
		}
	}
	
	/**
	 * Getters and setters
	 */
	
	public ArrayList<AreaModel> getAreaList(){
		return mAreaList;
	}
	
	/**
	 * Return an area from the sudoku
	 * @param sX : the line
	 * @param sY : the column
	 * @return
	 */
	public AreaModel getArea(int sLine, int sColumn){
		return mAreaList.get(sLine*MAX_AREA_PER_LINE_AND_COLUMN+sColumn);
	}
	
	/**
	 * Return a line from the index.
	 * @param sIndex
	 * @return
	 */
	public SudokuLineModel getLine(int sIndex){
		SudokuLineModel tResult = new SudokuLineModel();
		
		//Get the 3 areas concerned
		int tX=sIndex / MAX_AREA_PER_LINE_AND_COLUMN;
		int tLineInArea= sIndex % MAX_AREA_PER_LINE_AND_COLUMN;
		ArrayList<AreaModel> tAreaLine = new ArrayList<>();
		for(int tIndex=0; tIndex<MAX_AREA_PER_LINE_AND_COLUMN; tIndex++){
			tAreaLine.add(getArea(tX,tIndex));
		}
		
		//Get cases in each area
		for(AreaModel tArea : tAreaLine){
			for(int tCaseIndex=0; tCaseIndex < MAX_CASE_PER_AREA_LINE_AND_COLUMN; tCaseIndex++){
				tResult.add(tArea.getCase(tLineInArea, tCaseIndex));
			}
		}
		
		//return the line
		return tResult;
	}
	
	/**
	 * Return a column from the index
	 * @param sIndex
	 * @return
	 */
	public SudokuColumnModel getColumn(int sIndex){
		SudokuColumnModel tResult = new SudokuColumnModel();
		
		//Get the 3 areas concerned
		int tY=sIndex / MAX_AREA_PER_LINE_AND_COLUMN;
		int tColumnInArea = sIndex % MAX_AREA_PER_LINE_AND_COLUMN;
		ArrayList<AreaModel> tAreaColumn = new ArrayList<>();
		for(int tIndex = 0; tIndex < MAX_AREA_PER_LINE_AND_COLUMN; tIndex++){
			tAreaColumn.add(getArea(tIndex, tY));
		}
		
		//Get cases in each area
		for(AreaModel tArea : tAreaColumn){
			for(int tCaseIndex=0; tCaseIndex < MAX_CASE_PER_AREA_LINE_AND_COLUMN; tCaseIndex++){
				tResult.add(tArea.getCase(tCaseIndex, tColumnInArea));
			}
		}
		
		
		//return the line
		return tResult;
	}
	
	/**
	 * Return a specific case in the sudoku
	 * @param sLine
	 * @param sColumn
	 * @return
	 */
	public CaseModel getCase(int sLine, int sColumn){
		CaseModel tResult=null;
		
		int tAreaLine = sLine / MAX_AREA_PER_LINE_AND_COLUMN;
		int tAreaColumn = sColumn / MAX_AREA_PER_LINE_AND_COLUMN;
		int tLineInArea = sLine % MAX_AREA_PER_LINE_AND_COLUMN;
		int tColumnInArea = sLine % MAX_AREA_PER_LINE_AND_COLUMN;
		//Get the area then the case from this area
		AreaModel tArea = getArea(tAreaLine, tAreaColumn);
		tResult=tArea.getCase(tLineInArea, tColumnInArea);
		
		//return the result
		return tResult;
	}
	
	/**
	 * Check if the sudoku is corrected and completed
	 * @return true if completed, else otherwise
	 */
	public boolean isCompleted(){
		boolean tCorrected=true;
		for(int tIndex=0; tIndex<MAX_AREA; tIndex++){
			tCorrected=tCorrected && this.mAreaList.get(tIndex).isCompleted() && this.getLine(tIndex).isCompleted() && this.getColumn(tIndex).isCompleted();
		}
		return tCorrected;
	}
	
}
