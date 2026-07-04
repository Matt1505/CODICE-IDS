package client.MacroGestioneCondivisioni;

import java.util.List;

import client.GeneralClasses.Entities.ContenutoEntity;
import client.MacroGestioneProfilo.HomePageBoundary;

public class SendFileControl {

private HomePageBoundary hb;


    public SendFileControl(HomePageBoundary hb){

        this.hb=hb;

    }


    public void abilitaSelezione(){
    
    this.hb.enableSendButton();
    int numberOfCards = this.hb.getNumberOfCards();
        for(int i=0; i<numberOfCards; i++){
           this.hb.abilitaContentCheckBox(i);
        }
    
    }

    public void createResumePageBound(List<ContenutoEntity> elementi){

        //per domani: fai questa implementazione, considerando che deve essere simile agli inoltri di register
        //e login.
        
    }
    
}
