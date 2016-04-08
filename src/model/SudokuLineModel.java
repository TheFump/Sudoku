package model;

import java.util.ArrayList;

public class SudokuLineModel extends ArrayList<CaseModel>{
	private static final int MAX_LINE_SIZE=9;
	
	/**
	 * Check if the line is corrected and completed
	 * @return true if completed, else otherwise
	 */
	public boolean isCompleted(){
		ArrayList<Integer> tResultModel = SudokuManager.createResultModel();
		boolean tRightSize=this.size()==MAX_LINE_SIZE;
		boolean tCorrected=false;
		if(tRightSize){
			for(int tIndex=1; tIndex <= this.size(); tIndex++){
				if(tResultModel.contains(this.get(tIndex).getValue())){
					tResultModel.remove(this.get(tIndex).getValue());
				}
			}
			if(tResultModel.size()==0){
				tCorrected=true;
			}
		}
		return tCorrected;
	}
	

}
