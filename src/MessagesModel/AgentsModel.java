package MessagesModel;

import jade.core.AID;

public class AgentsModel {

	private String mAID;
	private int mNumber;
	private String mMessage;
	
	public AgentsModel(String mAID, int mNumber, String mMessage) {
		super();
		this.mAID = mAID;
		this.mNumber = mNumber;
		this.mMessage = mMessage;
	}
	public String getmAID() {
		return mAID;
	}
	public void setmAID(String mAID) {
		this.mAID = mAID;
	}
	public int getmNumber() {
		return mNumber;
	}
	public void setmNumber(int mNumber) {
		this.mNumber = mNumber;
	}
	public String getmMessage() {
		return mMessage;
	}
	public void setmMessage(String mMessage) {
		this.mMessage = mMessage;
	}
	
}
