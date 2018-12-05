/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsertree;

/**
 *
 * @author mywin
 */
public class NamedEntity {
    //int phraseTagmeLinksRecognizer; 
   //static String EntityWord;
     String phrase;//0
     String phraseStart;//1
     String phraseEnd;//2
     String wikiLinkPhrase;//3
     String numberOfWords;//4
    public NamedEntity(String ph,String phS,String phE,String phWiki,String phNum){
                   phrase=ph;
                   phraseStart=phS;
                  phraseEnd=phE;
                   wikiLinkPhrase=phWiki;
                   numberOfWords=phNum;
    }    
}
