package Agents;



import java.util.ArrayList;

import MessagesModel.ZoneModel;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import model.CaseModel;
import utils.parser.JsonParser;

public class Effecteur extends Agent {
	
	public String PREFIX_MESSAGE =  "";
	private ArrayList<CaseModel> mCellules;
	
	@Override
	protected void setup() {
		super.setup();
		PREFIX_MESSAGE =  Effecteur.this.getAID().getName() + " : ";
		System.out.println(PREFIX_MESSAGE + "Bonjour");
		mCellules = new ArrayList<CaseModel>();
		this.addBehaviour(new Subscriber());
		
	}
	
	private class Subscriber extends OneShotBehaviour{

		@Override
		public void action() {
			
			ACLMessage tMessage=new ACLMessage(ACLMessage.INFORM);
		 	tMessage.addReceiver(new AID(Constantes.SIMULATION_NAME, AID.ISLOCALNAME));
			tMessage.setContent(Constantes.MESSAGE_AGENT_READY);
			send(tMessage); //ici erreur console ?
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
					mCellules = tZoneModel.getmCases();
					//Si une valeur possible, mettre dans case
					for(CaseModel tCase : mCellules)
					{
						if(tCase.getValue() == 0 && tCase.getPossibleList().size() == 1){
							tCase.setValue(tCase.getPossibleList().get(0));
						}
					}
					//si une valeur n'est possible que dans une cellule, 
					// on set la valeur dans cette cellule
					for (int i=1; i<10; ++i){
						CaseModel tCase = new CaseModel();
						tCase.setValue(0);
						for (CaseModel c : mCellules){
							if (c.getValue() == i){
								tCase.setValue(0);;
								break;
							}
							else if (c.getValue() == 0 && c.getPossibleList().contains(i)) {
								if (tCase.getValue() == 0){
									tCase= c;
								}
								else {
									tCase.setValue(0);
									break;
								}
							}
						}
						if (tCase.getValue() != 0){
							tCase.setValue(i);
						}
					}
						
						//Si une valuer déterminée, retirer des possibles
					for(CaseModel tCase : mCellules)
					{
						if(tCase.getValue() != 0){
							for(CaseModel c : mCellules)
							{
								int index = 0;
								for(int i : c.getPossibleList())
								{
									if(i == tCase.getValue())
									{
										c.getPossibleList().remove(index);
										continue;
									}
									index++;
								}
							}
						}
					}
						
						
						// si deux valeurs ne sont possiles que dans deux cellules, 
						// alors on les retire des valeurs possibles des autres
						// cellules
						
						//todo
						
						//Réponse au l'interface :
					tZoneModel.setmCases(mCellules);
					tZoneModel.setmMessage(Constantes.MESSAGE_AGENT_REPONSE);
					ACLMessage tReply = tMessage.createReply();
					tReply.setPerformative(ACLMessage.INFORM);
					tReply.setContent(JsonParser.serialize(tZoneModel));
					send(tReply);
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
