package Agents;

import java.util.ArrayList;


import MessagesModel.AgentsModel;
import MessagesModel.ZoneModel;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import model.CaseModel;
import model.MySudoku;
import utils.parser.JsonParser; 

public class Interface extends Agent {
	
	public String PREFIX_MESSAGE = ""; 
	private int mAgentsAvailable = 0;
	private ArrayList<AID> mAgentsAID;
	private ArrayList<String> mMessagesToSend;
	private MySudoku mSudoku;
	private ArrayList<CaseModel> mSudokuCases;
	private int mReponse;
	private AID SimulationAID;

	@Override
	protected void setup() {
		super.setup();
		PREFIX_MESSAGE =  Interface.this.getAID().getName() + " : ";
		mAgentsAID = new ArrayList<AID>();
		mMessagesToSend = new ArrayList<String>();
		mReponse = 0;
		mSudoku = new MySudoku();
		mSudokuCases = new ArrayList<>();
		SimulationAID = new AID();
		for(int i = 0; i< 9; i++)//ligne
		{
			for(int j = 0; j <9; j++)//colonne
			{
				mSudokuCases.add(mSudoku.getmSudoku().getCase(i, j));
			}
		}
		mSudoku.printSudoku();
		System.out.println(PREFIX_MESSAGE + "Bonjour");
		this.addBehaviour(new Step());
		
	}
	
	private class Step extends Behaviour{
		
		private boolean mIsDone = false;

		@Override
		public void action() {
			MessageTemplate tMessageTemplate=MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
			ACLMessage tMessage=receive(tMessageTemplate);
			if(tMessage==null){
				block();
			}else{
				System.out.println(PREFIX_MESSAGE + "Message reçu : " + tMessage.getContent());
				if(tMessage.getContent().contains(Constantes.MESSAGE_AGENT_USABLE))
				{
					SimulationAID = tMessage.getSender();
					AgentsModel mAgentsModel = JsonParser.unserialize(tMessage.getContent(), AgentsModel.class);
					
					AID tAID = new AID(mAgentsModel.getmAID());
					mAgentsAID.add(tAID);
					mAgentsAvailable++;
					System.out.println(PREFIX_MESSAGE + "Ajout de l'agent : " + mAgentsModel.getmAID());
					
				}
				if(mAgentsAvailable == Constantes.AGENTS_TO_REGISTER)
				{
					mIsDone = true;
					mAgentsAvailable = 0;
					Interface.this.addBehaviour(new Repartition());
				}
			}	
		}

		@Override
		public boolean done() {
			return mIsDone;
		}
		//todo add behaviour verification of lines.
		//todo add behaviour end of simulation
	}
	
	private class Repartition extends OneShotBehaviour{

		@Override
		public void action() {
			// récupérer composant sudoku : ligne
			ArrayList<CaseModel>tCasesArrayList = new ArrayList();
			for(int i = 0; i < 9; i++)
			{
				for(int j = 0; j < 9; j++){
					tCasesArrayList.add(mSudoku.getmSudoku().getLine(i).get(j));
				}
				ZoneModel tZoneModel = new ZoneModel(
						tCasesArrayList,
						"Ligne",
						Constantes.MESSAGE_AGENT_9CASES,
						i
						);
				
				String tMessage = JsonParser.serialize(tZoneModel);
				mMessagesToSend.add(tMessage);
				tCasesArrayList.clear();
			}
			
			for(int i = 0; i < 9; i++)
			{
				for(int j = 0; j < 9; j++){
					tCasesArrayList.add(mSudoku.getmSudoku().getColumn(i).get(j));
				}
				ZoneModel tZoneModel = new ZoneModel(
						tCasesArrayList,
						"Colonne" + i,
						Constantes.MESSAGE_AGENT_9CASES,
						i
						);
				String tMessage = JsonParser.serialize(tZoneModel);
				mMessagesToSend.add(tMessage);
				tCasesArrayList.clear();
			}
			for(int i = 0; i < 9; i++) //todo récupérer les 9 cases d'une zone, puis les mettre dans arraylist pour les envoyer.
			{
				for(int j = 0; j < 9; j++){
					tCasesArrayList.add(mSudoku.getmSudoku().getColumn(i).get(j));
				}
				ZoneModel tZoneModel = new ZoneModel(// todo comprendre comment fonctionne area
						tCasesArrayList,
						"Zone" + i,
						Constantes.MESSAGE_AGENT_9CASES,
						i
						);
				String tMessage = JsonParser.serialize(tZoneModel);
				mMessagesToSend.add(tMessage);
				tCasesArrayList.clear();
			}
			Interface.this.addBehaviour(new Sender());
		}
	}
	
	private class Sender extends OneShotBehaviour
	{

		@Override
		public void action() {
			System.out.println(PREFIX_MESSAGE + "Envoi d'un groupe de messages");
			int index = 0;
			for(String tItem : mMessagesToSend)
			{ 
				ACLMessage tMessage=new ACLMessage(ACLMessage.REQUEST);
				AID tAID = new AID();
				tAID = mAgentsAID.get(index);
			 	tMessage.addReceiver(tAID);
				tMessage.setContent(tItem);
				send(tMessage);
				System.out.println(PREFIX_MESSAGE +"Message envoyé : " + tMessage.getContent());
				index++;
			}
			mMessagesToSend.clear();
			Interface.this.addBehaviour(new Waiter());
			Interface.this.addBehaviour(new Step());
		}
	}
	
	private class Waiter extends Behaviour
	{
		private boolean mIsDone = false;
		@Override
		public void action() {
			MessageTemplate tMessageTemplate=MessageTemplate.MatchPerformative(ACLMessage.INFORM);
			ACLMessage tMessage=receive(tMessageTemplate);
			if(tMessage==null){
				block();
			}else{
				System.out.println(PREFIX_MESSAGE + "Message reçu : " + tMessage.getContent());
				if(tMessage.getContent().contains(Constantes.MESSAGE_AGENT_REPONSE))
				{
					ZoneModel tZoneModel = JsonParser.unserialize(tMessage.getContent(), ZoneModel.class); //On gère les réponse 1 par 1 par ordre d'arrivée.
					HandleAnswer(tZoneModel);
					mReponse++;
				
				}
				if(mReponse == Constantes.AGENTS_TO_REGISTER)
				{
					mIsDone = true;	
					mReponse = 0;
					System.out.println(PREFIX_MESSAGE + "Réponse terminée");
					if(CheckIfResolved())
					{
						System.out.println(PREFIX_MESSAGE + "Fin de la simulation");
						mSudoku.printSudoku();
					}
				}
			}	
			
		}

		@Override
		public boolean done() {
			// TODO Auto-generated method stub
			return mIsDone;
		}
		
		private void HandleAnswer(ZoneModel sZoneModel)
		{
			if(sZoneModel.getmZone().contains("Ligne"))
			{
					for(int j = 0; j <9; j++)//colonne
					{
						mSudokuCases.get(sZoneModel.getmNumber()*9+j).mergePossible(sZoneModel.getmCases().get(j).getPossibleList());
					}

			}
			if(sZoneModel.getmZone().contains("Colonne"))
			{
				
					for(int j = 0; j <81; j+=9)//colonne : de 9 en 9
					{
						mSudokuCases.get(sZoneModel.getmNumber()+j).mergePossible(sZoneModel.getmCases().get(j/9).getPossibleList()); //on récupère par colonee : numéro de la colonne puis +9
					}
				
			}
			if(sZoneModel.getmZone().contains("Zone"))
			{
				//todo faire la meme chose que pour les autres mais avec area
			}
		}
		
		private boolean CheckIfResolved() //check if endend, if yes stop simulation, if no step.
		{
			if (mSudoku.getmSudoku().isCompleted()) //oob here, i completed doesnt work.
			{
				ACLMessage tMessage=new ACLMessage(ACLMessage.REQUEST);
				tMessage.setConversationId(String.valueOf(Constantes.EnderID));
			 	tMessage.addReceiver(SimulationAID);
				tMessage.setContent(Constantes.MESSAGE_AGENT_END);
				return true;
			}
			return false;
		}
	}
	
	
	
	
	

}
