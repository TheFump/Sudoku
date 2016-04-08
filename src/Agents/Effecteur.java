package Agents;



import MessagesModel.ZoneModel;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import utils.parser.JsonParser;

public class Effecteur extends Agent {
	
	public String PREFIX_MESSAGE =  "";
	
	@Override
	protected void setup() {
		super.setup();
		PREFIX_MESSAGE =  Effecteur.this.getAID().getName() + " : ";
		System.out.println(PREFIX_MESSAGE + "Bonjour");
		this.addBehaviour(new Subscriber());
		
	}
	
	private class Subscriber extends OneShotBehaviour{

		@Override
		public void action() {
			
			ACLMessage tMessage=new ACLMessage(ACLMessage.INFORM);
		 	tMessage.addReceiver(new AID(Constantes.SIMULATION_NAME, AID.ISLOCALNAME));
			tMessage.setContent(Constantes.MESSAGE_AGENT_READY);
			send(tMessage);
			System.out.println(PREFIX_MESSAGE +"Message envoyé : " + tMessage.getContent());
			Effecteur.this.addBehaviour(new Doer());
		}
	}
	
	private class Doer extends Behaviour{
		
		private boolean mIsDone = false;

		@Override 
		public void action() {
			MessageTemplate tMessageTemplate=MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
			ACLMessage tMessage=receive(tMessageTemplate);
			if(tMessage==null){
				block();
			}else{
				System.out.println(PREFIX_MESSAGE + "Message reçu : " + tMessage.getContent());
				if(tMessage.getContent().contains(Constantes.MESSAGE_AGENT_9CASES))
				{
					ZoneModel tZoneModel = JsonParser.unserialize(tMessage.getContent(), ZoneModel.class);
					//todo
					//résoudre les 9 cases
					//répondre
				}
			}	
		}

		@Override
		public boolean done() {
			return mIsDone;
		}
		
	}
	
	//todo add behaviour for handling line completion
	
}
