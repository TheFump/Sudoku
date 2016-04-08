package model;

public class MySudoku {

	public SudokuModel mSudoku;
	private SudokuManager mManager;

	public MySudoku() {
		super();
		mSudoku = new SudokuModel();
		mManager = new SudokuManager();
		mSudoku = mManager.generateSudoku();
	}

	public SudokuModel getmSudoku() {
		return mSudoku;
	}

	public void setmSudoku(SudokuModel mSudoku) {
		this.mSudoku = mSudoku;
	}
	
	public void printSudoku()
	{
		for(int i = 0; i< 9; i++)//ligne
		{
			for(int j = 0; j <9; j++)//colonne
			{
				System.out.print(this.getmSudoku().getCase(i, j));
			}
			System.out.println("");
		}
	}
	
	
}
