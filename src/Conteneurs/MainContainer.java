package Conteneurs;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;

public class MainContainer {

	public static final String MAIN_PROPERTIES_FILE="MainProperties";
	
	public static void main(String[] args) {
		Runtime tRuntime=Runtime.instance();
		Profile tProfile = null;
		try{
			tProfile = new ProfileImpl(MAIN_PROPERTIES_FILE);
			AgentContainer tAgentContainer= tRuntime.createMainContainer(tProfile);
		}catch(Exception tException){
			tException.printStackTrace();
		}
	}

}
