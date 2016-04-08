package Agents;

import java.util.ArrayList;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import utils.parser.JsonParser;
import Agents.Constantes;
import MessagesModel.AgentsModel;


public class Simulation extends Agent{
	
	public String PREFIX_MESSAGE = ""; 

	private int mAgentsRegistered;
	private ArrayList<String> mMessagesToSend;
	private boolean mIsDone;
	private AID mInterfaceAID;
	private ArrayList<AID> mAgentsAID;
	
	@Override
	protected void setup() {
		super.setup();
		PREFIX_MESSAGE =  Simulation.this.getAID().getName() + " : ";
		mAgentsRegistered = 0;
		mIsDone = false;
		mMessagesToSend = new ArrayList<String>();
		mAgentsAID = new ArrayList<AID>();
		mInterfaceAID = new AID(Constantes.INTERFACE_NAME, AID.ISLOCALNAME);
		this.addBehaviour(new Souscription());
		this.addBehaviour(new SimulationEnder());
		System.out.println(PREFIX_MESSAGE + "Bonjour");
	}
	
	private class Souscription extends Behaviour{ //attends d'avoir tous les agents

		@Override
		public void action() {
			MessageTemplate tMessageTemplate=MessageTemplate.MatchPerformative(ACLMessage.INFORM);
			ACLMessage tMessage=receive(tMessageTemplate);
			if(tMessage==null){
				block();
			}else{
				System.out.println(PREFIX_MESSAGE + "Message reçu : " + tMessage.getContent());
				if(tMessage.getContent().contains(Constantes.MESSAGE_AGENT_READY))
				{
					AID tSenderAID= tMessage.getSender();
					mAgentsAID.add(tSenderAID);
					mAgentsRegistered ++;
					int tAgentNumber = mAgentsRegistered;
					
					AgentsModel mAgentModel = new AgentsModel(tSenderAID.getName(), tAgentNumber-1, Constantes.MESSAGE_AGENT_USABLE);
					String tMessageToSend = JsonParser.serialize(mAgentModel);/*tAgentNumber + tSenderAID.toString() + Constantes.MESSAGE_AGENT_USABLE;*/
					mMessagesToSend.add(tMessageToSend);
					System.out.println(PREFIX_MESSAGE + "Message Créé : " + tMessageToSend);
				}
				if(mAgentsRegistered == Constantes.AGENTS_TO_REGISTER)
				{
					mIsDone = true;	
					mAgentsRegistered = 0;
					System.out.println(PREFIX_MESSAGE + "Souscription terminée");
					Simulation.this.addBehaviour(new MessageSender());
				}
			}
		}

		@Override
		public boolean done() {
			return mIsDone;
		}
	}
	
	private class MessageSender extends OneShotBehaviour{

		@Override
		public void action() {
			System.out.println(PREFIX_MESSAGE + "Envoi d'un groupe de messages");
			for(String tItem : mMessagesToSend)
			{
				ACLMessage tMessage=new ACLMessage(ACLMessage.REQUEST);
			 	tMessage.addReceiver(mInterfaceAID);
				tMessage.setContent(tItem);
				send(tMessage);
				System.out.println(PREFIX_MESSAGE +"Message envoyé : " + tMessage.getContent());
			}
			mMessagesToSend.clear();
			Simulation.this.addBehaviour(new MessageMaker(Simulation.this, Constantes.TIMEOUT));
		}
	}
	
	private class MessageMaker extends WakerBehaviour{

		public MessageMaker(Agent a, long timeout) {
			super(a, timeout);
			System.out.println(PREFIX_MESSAGE + "Création d'un nouveau groupe de messages dans : " + timeout + " ms");
		}
		
		@Override
		public void onWake()
		{
			for(AID mItem : mAgentsAID)
			{
				mAgentsRegistered++;
				int tAgentNumber = mAgentsRegistered-1;
				AgentsModel mAgentModel = new AgentsModel(mItem.getName(), tAgentNumber, Constantes.MESSAGE_AGENT_USABLE);
				String tMessageToSend = JsonParser.serialize(mAgentModel);/*tAgentNumber + tSenderAID.toString() + Constantes.MESSAGE_AGENT_USABLE;*/
				mMessagesToSend.add(tMessageToSend);
				System.out.println(PREFIX_MESSAGE + "Message Créé : " + tMessageToSend);
			}
			mAgentsRegistered = 0;
			Simulation.this.addBehaviour(new MessageSender());
			
		}
	}
	
	private class SimulationEnder extends Behaviour{

		@Override
		public void action() {
			MessageTemplate tMessageTemplate = MessageTemplate.and(
					MessageTemplate.MatchConversationId(String.valueOf(Constantes.EnderID)),MessageTemplate.MatchPerformative(ACLMessage.INFORM));
			ACLMessage tMessage=receive(tMessageTemplate);
			if(tMessage==null){
				block();
			}else{
				System.out.println(PREFIX_MESSAGE + "Message reçu : " + tMessage.getContent());
				if(tMessage.getContent().contains(Constantes.MESSAGE_AGENT_READY))
				{
					//todo EndSimulation
					System.out.println(PREFIX_MESSAGE + "Fin de la simulation");
				}
			}	
		}

		@Override
		public boolean done() {
			// TODO Auto-generated method stub
			return false;
		}
	}

}
