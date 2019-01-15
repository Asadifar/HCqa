/*
 * Named entity class
 */
package parsertree;


public class NamedEntity {
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
