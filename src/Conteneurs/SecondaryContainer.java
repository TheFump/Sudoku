package Conteneurs;
import Agents.Constantes;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

public class SecondaryContainer {

	public static final String SECONDARY_PROPERTIES_FILE="SecondaryProperties";
	
	
	
	public static void main(String[] args) {
		Runtime tRuntime=Runtime.instance();
		ProfileImpl tProfile = null;
		try{
			tProfile=new ProfileImpl(SECONDARY_PROPERTIES_FILE);
			ContainerController tContainerController = tRuntime.createAgentContainer(tProfile);
			AgentController tRootControllerSimulation = tContainerController.createNewAgent(Constantes.SIMULATION_NAME,"Agents.Simulation",new Object[]{});
			tRootControllerSimulation.start();
			AgentController tRootControllerInterface = tContainerController.createNewAgent(Constantes.INTERFACE_NAME,"Agents.Interface",new Object[]{});
			tRootControllerInterface.start();
			Thread.sleep(2000);
			for(int i = 0; i < Constantes.AGENTS_TO_REGISTER; i++)
			{
				AgentController tRootControllerEffecteur = tContainerController.createNewAgent(Constantes.EFFECTEUR_NAME + (i+1),"Agents.Effecteur",new Object[]{});
				tRootControllerEffecteur.start();
			}
		}catch(Exception tException){
			tException.printStackTrace();
		}

	}

}
