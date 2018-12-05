/*
 *In this code we transfer all of pages in apecial wikipedia category to sql table 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsertree;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.Scanner;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import parsertree.NamedEntity;
import parsertree.Sentence;
//import getWikiPages.DBClass;
/**
 *
 * @author WIN8
 */
public class NamedEntityRecognition {

    /** 
     * @param args the command line arguments
     */
public static NamedEntity[] namedEntity=new NamedEntity[150];   
String  phrase="";
//public static String [][] WikiLinksInformationArray= new String [50][5];  //Phrase,phraseStart,phraseEnd,WikiLinkPhrase,number of words
    public static int NumTagMeLinks;
    public static final String SpecialExport_URL = "https://tagme.d4science.org/tagme/tag?";
    public static Document OtherCategories(String text)throws IOException {
                String searchCategory="";
                String FirstCategory="";
                String[] subStr;//----   /*	
               //  String text="Which buildings in art deco style did Shreve, Lamb and Harmon design?";
                 String Query=SpecialExport_URL + "lang=en" +
                    "&gcube-token=" +  URLEncoder.encode("7358841c-821c-4bac-8a9b-4ff7ea704363-843339462", "utf-8")
                   + "&text=" + URLEncoder.encode(text, "utf-8");
                 Document doc =Jsoup.connect(Query).validateTLSCertificates(false).ignoreContentType(true).get();
                 return doc;
                              
                
     }        
     public static int  phraseIsContainSomeSpecialWords(String phrase,String Start,String End){
     if(phrase.matches("I")||phrase.matches("you")||phrase.matches("she")||phrase.matches("he")||
             phrase.matches("we")||phrase.matches("they")||phrase.matches("number")||phrase.matches("numbers")||
             phrase.matches("hundreds")||phrase.matches("milions")
             ||phrase.contains("I ")||phrase.contains("you ")||phrase.contains("he ")||phrase.contains("she ")||phrase.contains("they ")
             )
          return 1;
    return 0;
      }
    public static int  phraseIsContainWhWord(String phrase,String Start,String End){
    if (phrase.contains("Who")||phrase.contains("Where")||phrase.contains("When")||phrase.contains("How many")||phrase.contains("Which")||phrase.contains("What")||phrase.contains("How old")||
            phrase.contains("who")||phrase.contains("where")||phrase.contains("when")||phrase.contains("how many")||phrase.contains("which")||phrase.contains("what")||phrase.contains("how old"))
           return 1;
    return 0;
    }
     public static int  phraseIsCapital(String phrase,String Start,String End){
      
         if(phrase.startsWith("A")||phrase.startsWith("B")||phrase.startsWith("C")
           ||phrase.startsWith("A")||phrase.startsWith("A")||phrase.startsWith("C")
           ||phrase.startsWith("D")||phrase.startsWith("E")||phrase.startsWith("F")
           ||phrase.startsWith("G")||phrase.startsWith("H")||phrase.startsWith("I")
           ||phrase.startsWith("J")||phrase.startsWith("K")||phrase.startsWith("L")
           ||phrase.startsWith("M")||phrase.startsWith("N")||phrase.startsWith("O")
           ||phrase.startsWith("P")||phrase.startsWith("Q")||phrase.startsWith("R")
           ||phrase.startsWith("S")||phrase.startsWith("T")||phrase.startsWith("U")
           ||phrase.startsWith("V")||phrase.startsWith("W")||phrase.startsWith("X")
           ||phrase.startsWith("Y")||phrase.startsWith("Z"))
               if(   !(Integer.parseInt(Start)==0 &  !Sentence.wordPre[1].POStagword.matches("NNP")))
             return 1;
   return 0;
    }
   public static int  phraseEndsWithPrep(String phrase,String Start,String End){
       if(phrase.endsWith(" in")||phrase.endsWith(" at")||phrase.endsWith(" of")||phrase.endsWith(" under")||
          phrase.endsWith(" on")||phrase.endsWith(" by")||phrase.endsWith(" against")||phrase.endsWith(" during")||     
          phrase.endsWith(" with")||phrase.endsWith(" to")||phrase.endsWith(" from")||phrase.endsWith(" during")
               )
         return 1;
         return 0;
    }
   
   public static int phraseIsAVerb(String phrase,String Start,String End){
     int phraseStart=0;
     int phraseEnd=0;
     phraseStart=Integer.parseInt(Start);
     phraseEnd=Integer.parseInt(End);
     
    
      for(int i=1;i<Sentence.wordsNumber;i++){
        if(Sentence.wordPre[i].POSword.matches(phrase) & (Sentence.wordPre[i].POSwordStart>=phraseStart) & (Sentence.wordPre[i].POSwordEnd<=phraseEnd)
                &
                (Sentence.wordPre[i].POStagword.matches("VB")||Sentence.wordPre[i].POStagword.matches("VBG")||Sentence.wordPre[i].POStagword.matches("VBN")
                ||Sentence.wordPre[i].POStagword.matches("VBP")
                ||Sentence.wordPre[i].POStagword.matches("VBD")||Sentence.wordPre[i].POStagword.matches("VBZ")
                )
                ) {
                 return 1;            
               }
        }
        return 0;
     }
    public static int AreTwoOverlappedLinksType1(int NumTagMeLinks,String currentPhraseStart,String currentPhraseEnd){//common end or overlapped in middle so second link should be neglected
        int curLinkStart=Integer.parseInt(currentPhraseStart);
        int curLinkEnd=Integer.parseInt(currentPhraseEnd);
        if(NumTagMeLinks>=1){
        int previousLinkStart=Integer.parseInt(namedEntity [NumTagMeLinks-1].phraseStart);
        int previusLinkEnd=Integer.parseInt(namedEntity [NumTagMeLinks-1].phraseEnd);
        if (curLinkEnd==previusLinkEnd ||curLinkStart<previusLinkEnd)
                   return 1;
        }
    return 0;
    }    
    public static int AreTwoOverlappedLinksType2(int NumTagMeLinks,String currentPhraseStart,String currentPhraseEnd){//common start so first link should be neglected
        int curLinkStart=Integer.parseInt(currentPhraseStart);
        int curLinkEnd=Integer.parseInt(currentPhraseEnd);
        if(NumTagMeLinks>=1){
        int previousLinkStart=Integer.parseInt(namedEntity[NumTagMeLinks-1].phraseStart);
        int previusLinkEnd=Integer.parseInt(namedEntity[NumTagMeLinks-1].phraseEnd);
        if (curLinkStart==previousLinkStart)
                   return 1;
        }
    return 0;
    }
public static int phraseIsContainedNumber(String phrase,String Start,String End){
     int phraseStart=0;
     int phraseEnd=0;
     phraseStart=Integer.parseInt(Start);
     phraseEnd=Integer.parseInt(End);
     
      String[] str=phrase.split(" ");
      
        for(int i=1;i<Sentence.wordsNumber;i++){
          if((str[0].contains(Sentence.wordPre[i].POSword)) & (Sentence.wordPre[i].POSwordStart>=phraseStart) & (Sentence.wordPre[i].POSwordEnd<=phraseEnd)
                &
                (Sentence.wordPre[i].POStagword.matches("CD")
                )
                ) 
                 return 1;            
               
         }
     
        return 0;
     }
    public static void Initialization(){
                  
        for(int i=0;i<16;i++)
                     namedEntity[i]=new NamedEntity("","","","","");          
        NumTagMeLinks=0;
                  
                  
   }
      public static void docComposition(Document doc){
      String mid1="";
      String phraseEnd="";
      String wikiLinkPhrase="";
      String phraseStart="";
      int numberOfWordsInPhrase=0;
      String phrase;
      String str =doc.text();
      System.out.print(doc);
             
      String[] content=str.split("\"spot\":\"");
             for(int i=1;i<content.length;i++){
              String node=content[i];
              String[] mid=node.split("\",\"start\":");
              phrase=mid[0];
              phraseStart=mid[1].split(",")[0];
              numberOfWordsInPhrase=phrase.split(" ").length;
              if(phrase.contains(","))
              numberOfWordsInPhrase=numberOfWordsInPhrase+phrase.split(",").length-1;
              phraseEnd=mid[1].split("\"end\":")[1].split(",")[0];
              wikiLinkPhrase=mid[1].split("\"end\":")[1].split("\"title\":\"")[1].split("\"")[0];
                  
                   
             if(phrase.endsWith(" ")){
                      phrase=phrase.substring(0, phrase.length() - 1);
                      phraseEnd=Integer.toString(Integer.parseInt(phraseEnd)-1);
                  }
              if(phrase.endsWith(" 's")){
                 phrase=phrase.replace(" 's", "");
                 phraseEnd=Integer.toString(Integer.parseInt(phraseEnd)-3);
                 numberOfWordsInPhrase--;
              }   
              if (phraseIsContainedNumber(phrase,phraseStart,phraseEnd)==0 & phraseIsAVerb(phrase,phraseStart,phraseEnd)==0 & phraseIsContainWhWord(phrase,phraseStart,phraseEnd)==0
                     & phraseEndsWithPrep(phrase,phraseStart,phraseEnd)==0 & phraseIsContainSomeSpecialWords(phrase,phraseStart,phraseEnd)==0
                      //& phraseIsCapital(phrase,phraseStart,phraseEnd)==1
                      ){
                if(AreTwoOverlappedLinksType2(NumTagMeLinks,phraseStart,phraseEnd)==0 & AreTwoOverlappedLinksType1(NumTagMeLinks,phraseStart,phraseEnd)==0) {
                   namedEntity[NumTagMeLinks]=new NamedEntity(phrase,phraseStart,phraseEnd,wikiLinkPhrase,String.valueOf(numberOfWordsInPhrase));
                   NumTagMeLinks++;
                }
                else if(AreTwoOverlappedLinksType2(NumTagMeLinks,phraseStart,phraseEnd)==1){
                   NumTagMeLinks--;      
                   namedEntity[NumTagMeLinks]=new NamedEntity(phrase,phraseStart,phraseEnd,wikiLinkPhrase,String.valueOf(numberOfWordsInPhrase));
                   NumTagMeLinks++;  
               }
              }
             }
         
             for(int i=0;i<NumTagMeLinks;i++){
           System.out.println(i+" "+namedEntity[i].phraseStart+"  "+namedEntity[i].phraseEnd+" "+namedEntity[i].phrase+" "+namedEntity[i].wikiLinkPhrase);
     }
      
      }
    public static void Correcttion(String text){
       
       for(int i=0;i<NumTagMeLinks;i++){
          // System.out.println(i+" "+NamedEntityRecognition.namedEntity[i].phraseStart+"  "+NamedEntityRecognition.namedEntity[i].phraseEnd+" "+NamedEntityRecognition.namedEntity[i].phrase+" "+NamedEntityRecognition.namedEntity[i].wikiLinkPhrase);
             
         for(int j=1;j<Sentence.wordsNumber;j++){   
           if(Sentence.wordPre[j].POSwordStart>=Integer.parseInt(namedEntity[i].phraseStart)&
              Sentence.wordPre[j].POSwordStart<=Integer.parseInt(namedEntity[i].phraseEnd) &
              Sentence.wordPre[j].POSwordEnd>Integer.parseInt(namedEntity[i].phraseEnd)
                   ){ namedEntity[i].phrase="";
                    for(int k=Integer.parseInt(namedEntity[i].phraseStart);k<=Sentence.wordPre[j].POSwordEnd-1;k++){
                        namedEntity[i].phrase=namedEntity[i].phrase+text.charAt(k);
                    }
                    
                    namedEntity[i].phraseEnd=Integer.toString(Sentence.wordPre[j].POSwordEnd);
           
                     }
         }
       }
     }
    public static void main(String text) throws IOException {
	      Initialization(); 
              
            //  namedEntity[NumTagMeLinks]
              Document doc= OtherCategories(text);
              docComposition(doc);
           
              Correcttion(text);
              System.out.print(doc);
    }
   
}



