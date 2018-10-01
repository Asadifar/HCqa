/*
 *In this code we transfer all of pages in apecial wikipedia category to sql table 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TAGmeWebService;
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
import static parsertree.Parsertree.POStagword;
import static parsertree.Parsertree.POSword;
import static parsertree.Parsertree.POSwordPositionEnd;
import static parsertree.Parsertree.POSwordPositionStart;
import static parsertree.Parsertree.wordsNumber;
//import getWikiPages.DBClass;
/**
 *
 * @author WIN8
 */
public class tagmewebservice {

    /**
     * @param args the command line arguments
     */
    public static String [][] WikiLinksInformationArray= new String [10][5];  //Phrase,phraseStart,phraseEnd,WikiLinkPhrase,number of words
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
    public static int  phraseIsContainWhWord(String phrase,String Start,String End){
    if (phrase.contains("Who")||phrase.contains("Where")||phrase.contains("When")||phrase.contains("How many")||phrase.contains("Which")||phrase.contains("What")||phrase.contains("How old")||
            phrase.contains("who")||phrase.contains("where")||phrase.contains("when")||phrase.contains("how many")||phrase.contains("which")||phrase.contains("what")||phrase.contains("how old"))
           return 1;
    return 0;
    }
    public static int phraseIsAVerb(String phrase,String Start,String End){
     int phraseStart=0;
     int phraseEnd=0;
     phraseStart=Integer.parseInt(Start);
     phraseEnd=Integer.parseInt(End);
     
    
      for(int i=1;i<wordsNumber;i++){
        if(POSword[i].matches(phrase) & (POSwordPositionStart[i]>=phraseStart) & (POSwordPositionEnd[i]<=phraseEnd)
                &
                (POStagword[i].matches("VB")||POStagword[i].matches("VBG")||POStagword[i].matches("VBN")
                ||POStagword[i].matches("VBP")
                ||POStagword[i].matches("VBD")||POStagword[i].matches("VBZ")
                )
                ) {
                 return 1;            
               }
        }
        return 0;
     }
    public static int phraseIsContainedNumber(String phrase,String Start,String End){
     int phraseStart=0;
     int phraseEnd=0;
     phraseStart=Integer.parseInt(Start);
     phraseEnd=Integer.parseInt(End);
     
    
      for(int i=1;i<wordsNumber;i++){
        if((phrase.contains(POSword[i])) & (POSwordPositionStart[i]>=phraseStart) & (POSwordPositionEnd[i]<=phraseEnd)
                &
                (POStagword[i].matches("CD")
                )
                ) {
                 return 1;            
               }
        }
        return 0;
     }
    public static void Initialization(){
                  
                   NumTagMeLinks=0;
                   for(int i=0;i<10;i++){
                      WikiLinksInformationArray[i][0]="";
                      WikiLinksInformationArray[i][1]="";
                      WikiLinksInformationArray[i][2]="";
                      WikiLinksInformationArray[i][3]="";
                     
                   }
                  
   }
    public static void docComposition(Document doc){
     String str =doc.text();
             String[] content=str.split("\"id\"");
             for(int i=1;i<content.length;i++){
              String node=content[i];
              String[] mid=node.split("\",\"start\":");
              String phrase=mid[1].split(",\"spot\":\"")[1].split("\"}")[0];
              int numberOfWordsInPhrase=phrase.split(" ").length;
              if(phrase.contains(","))
                 numberOfWordsInPhrase=numberOfWordsInPhrase+phrase.split(",").length-1;
              String phraseStart=mid[1].split(",\"link_probability\":")[0];
              String phraseEnd=mid[1].split(",\"spot\":\"")[0].split("\"end\":")[1];
              String wikiLinkPhrase=mid[0].split("\"title\":\"")[1];
               if (phraseIsContainedNumber(phrase,phraseStart,phraseEnd)==0 & phraseIsAVerb(phrase,phraseStart,phraseEnd)==0 & phraseIsContainWhWord(phrase,phraseStart,phraseEnd)==0){
                   WikiLinksInformationArray[NumTagMeLinks][0]=phrase;
                   WikiLinksInformationArray[NumTagMeLinks][1]=phraseStart;
                   WikiLinksInformationArray[NumTagMeLinks][2]=phraseEnd;
                   WikiLinksInformationArray[NumTagMeLinks][3]=wikiLinkPhrase;
                   WikiLinksInformationArray[NumTagMeLinks][4]=String.valueOf(numberOfWordsInPhrase);
                   System.out.println(" ");
                   System.out.print(phrase);
                    System.out.print(" ");
                     System.out.println(wikiLinkPhrase);
                      
                   NumTagMeLinks++;
                   
                }
              }
    } 
    public static void main(String text) throws IOException {
	      Initialization();     
              Document doc= OtherCategories(text);
              docComposition(doc);
              System.out.print(doc);
    }
   
}



