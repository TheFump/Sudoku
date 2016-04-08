package MessagesModel;

import java.util.ArrayList;

import model.CaseModel;

public class ZoneModel {

	
	private ArrayList<CaseModel> mCases;
	private String mZone;
	private int mNumber;
	private String mMessage;
	
	public ZoneModel(ArrayList<CaseModel> Cases,String Zone, String Message, int Number) {
		super();
		this.setmCases(Cases);
		this.mMessage = Message;
		this.mZone = Zone;
		this.setmNumber(Number);
	}
	
	public ZoneModel(String Zone, String Message, int Number) {
		super();
		mCases = new ArrayList<>();
		this.mMessage = Message;
		this.mZone = Zone;
		this.setmNumber(Number);
	}

	public String getmZone() {
		return mZone;
	}
	public void setmZone(String mZone) {
		this.mZone = mZone;
	}
	public String getmMessage() {
		return mMessage;
	}
	public void setmMessage(String mMessage) {
		this.mMessage = mMessage;
	}

	public ArrayList<CaseModel> getmCases() {
		return mCases;
	}

	public void setmCases(ArrayList<CaseModel> mCases) {
		this.mCases = mCases;
	}

	public int getmNumber() {
		return mNumber;
	}

	public void setmNumber(int mNumber) {
		this.mNumber = mNumber;
	}
	
}
