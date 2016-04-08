package model;

import java.util.ArrayList;

/**
 * Represents a sudoku case, the value or the possible values for the case.
 * @author Anais
 *
 */
public class CaseModel {
	private boolean mIsFixed;
	private int mValue;
	private ArrayList<Integer> mPossibleList;
	
	/**
	 * Constructor
	 */
	public CaseModel(){
		mValue=0;
		mIsFixed=false;
		mPossibleList=new ArrayList<>();
	}
	
	/**
	 * Setter for the value
	 * @param sValue
	 */
	public void setValue(int sValue){
		mValue=sValue;
	}
	
	/**
	 * Setter for the value, allow setting a fixed value (to generate the sudoku, for example)
	 * @param sValue
	 * @param sFixed
	 */
	public void setValue(int sValue, boolean sFixed){
		mValue=sValue;
		mIsFixed=sFixed;
	}
	
	/**
	 * Get the list of all possible values for the case
	 * @return
	 */
	public ArrayList<Integer> getPossibleList(){
		return mPossibleList;
	}
	
	/**
	 * Merge an already existing possible list with another one to limit the values. If one value left, it is set as the right value for this case.
	 * @param sNewPossible
	 */
	public void mergePossible(ArrayList<Integer> sNewPossible){
		ArrayList<Integer> tResult=new ArrayList<>();
		for(int tValue : sNewPossible){
			if(mPossibleList.contains(tValue)){
				tResult.add(tValue);
			}
		}
		if(tResult.size()==1){
			mValue=tResult.get(0);
			mPossibleList.clear();
		}else{
			mPossibleList=tResult;
		}
		
	}
	
	/**
	 * Set the case as fixed or not.
	 * @param sFixed
	 */
	public void setIsFixed(boolean sFixed){
		mIsFixed=sFixed;
	}
	
	/**
	 * Return if the case is a fixed one or not
	 * @return
	 */
	public boolean isFixed(){
		return mIsFixed;
	}
	
	/**
	 * Return the value of the case
	 * @return
	 */
	public int getValue(){
		return mValue;
	}
	
	
}
