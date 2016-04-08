package model;

import java.util.ArrayList;

/**
 * Model for an area of the sudoku.
 * @author Anais
 *
 */
public class AreaModel {

	/**
	 * CONSTANTS
	 */
	private static final int MAX_CASE=9;
	private static final int MAX_CASE_PER_LINE=3;
	
	/**
	 * ATTRIBUTES AND METHODS
	 */
	
	private ArrayList<CaseModel> mCaseList;
	
	public AreaModel(){
		mCaseList=new ArrayList<>();
		//Set the 9 cases of an area
		for(int tIndex=0;tIndex<MAX_CASE;tIndex++){
			mCaseList.add(new CaseModel());
		}
	}
	
	/**
	 * Getters and setters
	 */
	
	public ArrayList<CaseModel> getCaseList(){
		return mCaseList;
	}
	
	/**
	 * Return a case of the area
	 * @param sX : the line 
	 * @param sY : the column
	 * @return
	 */
	public CaseModel getCase(int sLine, int sColumn){
		return mCaseList.get(sLine*MAX_CASE_PER_LINE+sColumn);
	}
	
	/**
	 * Check if the area is corrected and completed
	 * @return true if completed, else otherwise
	 */
	public boolean isCompleted(){
		ArrayList<Integer> tResultModel = SudokuManager.createResultModel();
		boolean tRightSize=this.mCaseList.size()==MAX_CASE;
		boolean tCorrected=false;
		if(tRightSize){
			for(int tIndex=1; tIndex <= this.mCaseList.size(); tIndex++){
				if(tResultModel.contains(this.mCaseList.get(tIndex).getValue())){
					tResultModel.remove(this.mCaseList.get(tIndex).getValue());
				}
			}
			if(tResultModel.size()==0){
				tCorrected=true;
			}
		}
		return tCorrected;
	}

	
	
}
