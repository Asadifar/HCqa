//  tokenize,2- pos tagging 3- parse tree 4- dependency parser 

package parsertree;
//java imports



//NER

import edu.stanford.nlp.ie.crf.*;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import org.apache.jena.util.FileManager;

//
import Jwi.Main;
import TAGmeWebService.tagmewebservice;
import static TAGmeWebService.tagmewebservice.NumTagMeLinks;
import static TAGmeWebService.tagmewebservice.WikiLinksInformationArray;

import java.util.Collection; 
import dbpediaspotlightwebservice.dbpediaSpot;
import java.io.StringReader;

import java.util.List; 



//import static org.apache.commons.io.IOUtils.closeQuietly; 
//stanford imports 
 
import edu.stanford.nlp.process.CoreLabelTokenFactory; 
import edu.stanford.nlp.process.DocumentPreprocessor; 
import edu.stanford.nlp.ling.CoreLabel;   
import edu.stanford.nlp.ling.HasWord;   
import edu.stanford.nlp.trees.*; 
import edu.stanford.nlp.parser.lexparser.LexicalizedParser; 
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.trees.Tree;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.jena.rdf.model.Model;

public class Parsertree { 
 public static Model model;
   //------------------minimals
   
   public static String [][] Minimaltriples = new String [40][3];   //stores triples
   public static int [] MinimaltriplesState = new int [40];   //stores triples
   public static String [] MinimaltriplesVerbPrep = new String [40]; 
   public static int [][] MinimaltriplesPosition = new int [40][5];  
   public static String [][] MinimaltripleCondition=new String [40][3];  
   public static int MinimalEndNum;
//-----------------
   static String [] verbs=new String[20];
   public static String [][] triples = new String [40][3];   //stores triples
   public static String [][] triplesVerbPrep = new String [40][3]; 
   public static String [][] tripleCondition=new String [40][3];  
//-----------------------------------------------------------
   public static String [] subjectFillers=new String [7];
   public static int subjectFillersNumber;
   public static int [] subjectFillersPositions=new int [7];
   public static String [] objectFillers=new String [7];
   public static String [] objectFillersPrep=new String [7];
   public static int [] objectFillersPositions=new int [7];
   public static int [] directObjectFillersPositions=new int [7];
   public static int objectFillersNumber;
   //-------------------------------------------------------------
   public static String  IndirectObject;
   
    public static int  haveIndirectObject;
   public static String  DirectObject;
    public static int  haveDirectObject;
    public static int  RightLimitOfWord, LeftLimitOfWord;
   public static String [] typeTriples = new String[40]; 
    public static String [] MinimaltypeTriples = new String[40]; 
   static String [][] triplesRoles = new String [40][3];
   public static int [][] triplesPosition = new int [40][5];
   
   static int TwoNounTriplesEndNum;
    public static int [] phraseTagmeLinksRecognizer=new int [150]; 
   static String [] EntityWord=new String [150];
   public static String [] POSword=new String [150];
   public static String [] POStagword=new String [150];
    public static int [] POSwordPositionStart=new int[150];
    public static int [] POSwordPositionEnd=new int[150];
    public static int [] POSwordNamedEntityState=new int[150];
    public static String [] POSwordNamedEntityURI=new String[150];
     
   static  int verbsPosition[]=new int[40];
   static String verbsRoles[]=new String[40];
   public static int EndNum;
   public static int wordsNumber; 
   static int row=0; 
   static int CounterVerbs;
   
   //static public   String sent2 = "Which writers had influenced the philosopher that refused a Nobel Prize?";
   static public   String sent2;
   public final static String PCG_MODEL = "F:\\Question-answer\\implementation\\software\\dependencyviewer-0.1.1\\edu\\stanford\\nlp\\models\\lexparser\\englishPCFG.ser.gz";        
   private final TokenizerFactory<CoreLabel> tokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), "invertible=true");
   private final LexicalizedParser parser = LexicalizedParser.loadModel(PCG_MODEL);
   
   //&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
   
   public Tree pars(String str) {                
        List<CoreLabel> tokens = tokenize(str);
        Tree tree = parser.apply(tokens);
        return tree;
    }
   private List<CoreLabel> tokenize(String str) {
        Tokenizer<CoreLabel> tokenizer =tokenizerFactory.getTokenizer(new StringReader(str));    
        return tokenizer.tokenize();
    }
   
   //Internal needed finctions
    public static void PositionOfWords(String str,LexicalizedParser lp){
    POSwordPositionStart[1]=0;
    int wordNum=1;
    
    for(int i=0;i<=str.length()-1;i++){
        char c=str.charAt(i);
        if(str.charAt(i)==' '||str.charAt(i)=='?'||str.charAt(i)=='.'||str.charAt(i)==','||str.charAt(i)=='\''){
          POSwordPositionEnd[wordNum]=i-1;
          wordNum++;
          if(i+1<str.length()-1)POSwordPositionStart[wordNum]=i+1;
          if(str.charAt(i)==','||str.charAt(i)=='\''||str.charAt(i)=='.'){
             POSwordPositionStart[wordNum]=i;
             //POSwordPositionEnd[wordNum]=i;
             
          }
        }
         
    }
    }
   public static void Tokenize1(String str,LexicalizedParser lp){
    //-----------------------------------prints tockenz and then  parse tree----------------------------------
    Parsertree parser = new Parsertree();
    Tree tree = parser.pars(str);  
    List<Tree> leaves = tree.getLeaves();
    //String verbs[]=new String[10];
    
   
   int i=0;
   int k=1;//full array of pos tag 
    
    for (Tree leaf : leaves) 
    { 
         Tree parent = leaf.parent(tree);
         System.out.print(leaf.label().value() + "-" + parent.label().value());
          POSword[k]= leaf.label().value();
          //POSwordSize[k]=leaf.size();
          
          POStagword[k]= parent.label().value();
        
          
//^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^finding verbs in sentense(VB,VBD,VBN,VBP,VBZ)
         if((parent.label().value().equals("VB"))||(parent.label().value().equals("VBG"))||
                 (parent.label().value().equals("VBN"))|(parent.label().value().equals("VBP"))
                 ||(parent.label().value().equals("VBD"))
                 ||(parent.label().value().equals("VBZ")))
         { 
             verbs[CounterVerbs]=leaf.label().value();
             verbsRoles[CounterVerbs]=parent.label().value();
             verbsPosition[CounterVerbs]=k;
             
         CounterVerbs++;}
         k++;
         //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    }
   wordsNumber=k; }
   public static void demoDP(LexicalizedParser lp, String filename) { 
    // This option shows loading and sentence-segment and tokenizing 
    // a file using DocumentPreprocessor 
        
    TreebankLanguagePack tlp = new PennTreebankLanguagePack(); 
    GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory(); 
    // You could also create a tokenier here (as below) and pass it 
    // to DocumentPreprocessor 
    for (List<HasWord> sentence : new DocumentPreprocessor(filename)) { 
      Tree parse = lp.apply(sentence); 
      parse.pennPrint(); 
    //  System.out.println(); 
       
      GrammaticalStructure gs = gsf.newGrammaticalStructure(parse); 
      Collection tdl = gs.typedDependenciesCCprocessed(true); 
    //  System.out.println(tdl); 
      System.out.println(); 
    } 
  } 
   public static void extendToLeftCopular(int i,int triplenum,int counter,String str,LexicalizedParser lp){
               
              while(
                      !(POStagword[counter].equals("NN")
                      ||POStagword[counter].equals("NNS")
                      ||POStagword[counter].equals("NNP")
                      ||POStagword[counter].equals("JJ")
                      ||POStagword[counter].equals("POS")
                      ||POStagword[counter].equals("CD")
                      ||POStagword[counter].equals("PRP$")
                      ||POStagword[counter].equals("PRP")
                    //  ||POStagword[counter].equals("VBG")
                      ||POStagword[counter].equals("DT")
                       //||POStagword[counter].equals('"')
                       ||POStagword[counter].equals("CC")
                      ||POStagword[counter].equals("NNPS")
                      ))
              if (counter!=1)counter--;else break;
              triples[triplenum][i]=POSword[counter];
              
              if(i==0)
              { triplesPosition[triplenum][0]=counter;
                  triplesPosition[triplenum][1]=counter;}
               if(i==2)
               {  triplesPosition[triplenum][4]=counter;
                triplesPosition[triplenum][3]=counter;}
              if (counter!=1) counter--;
              while(
                      (POStagword[counter].equals("NN")||
                     // POStagword[counter].equals("VBG")||
                      POStagword[counter].equals("NNS")||POStagword[counter].equals("CD")||POStagword[counter].equals("IN")||
                       POStagword[counter].equals("NNP")||POStagword[counter].equals("JJ")||POStagword[counter].equals("JJS")||POStagword[counter].equals("PRP$")
                     ||POStagword[counter].equals("POS")||POStagword[counter].equals("DT")
                      //||POStagword[counter].equals('"')
                       ||POStagword[counter].equals("CC")
                      ||POStagword[counter].equals("NNPS")
                        ||POStagword[counter].equals("PRP")
                      )&(counter!=1))
                 {
                     triples[triplenum][i]=POSword[counter]+" "+triples[triplenum][i];
                    if(i==0)
                        triplesPosition[triplenum][0]=counter;
                    if(i==2)
                        triplesPosition[triplenum][3]=counter;
                     
                     if (counter!=1) counter--;else break;
                 }
   }
   public static void extendToLeftCopularMinimal(int i,int triplenum,int counter,String str,LexicalizedParser lp){
               
              while(
                      !(POStagword[counter].equals("NN")
                      ||POStagword[counter].equals("NNS")
                      ||POStagword[counter].equals("NNP")
                      ||POStagword[counter].equals("JJ")
                      ||POStagword[counter].equals("POS")
                      ||POStagword[counter].equals("PRP$")
                      ||POStagword[counter].equals("PRP")
                      //||POStagword[counter].equals("VBG")
                      ||POStagword[counter].equals("NNPS")
                      ||POStagword[counter].equals("CD")
                      ))
              if (counter!=1)counter--;else break;
              Minimaltriples[triplenum][i]=POSword[counter];
              
              if(i==0)
              { MinimaltriplesPosition[triplenum][0]=counter;
                  MinimaltriplesPosition[triplenum][1]=counter;}
               if(i==2)
               {  MinimaltriplesPosition[triplenum][4]=counter;
                MinimaltriplesPosition[triplenum][3]=counter;}
              if (counter!=1) counter--;
              while(
                      (POStagword[counter].equals("NN")||
                      //POStagword[counter].equals("VBG")||
                      POStagword[counter].equals("NNS")||POStagword[counter].equals("CD")||//POStagword[counter].equals("IN")||
                       POStagword[counter].equals("NNP")||POStagword[counter].equals("JJ")||POStagword[counter].equals("JJS")||POStagword[counter].equals("PRP$")
                     ||POStagword[counter].equals("POS")
                      //||POStagword[counter].equals('"')
                  
                      ||POStagword[counter].equals("NNPS")
                        ||POStagword[counter].equals("PRP")
                      )&(counter!=1))
                 {
                     Minimaltriples[triplenum][i]=POSword[counter]+" "+Minimaltriples[triplenum][i];
                    if(i==0)
                        MinimaltriplesPosition[triplenum][0]=counter;
                    if(i==2)
                         MinimaltriplesPosition[triplenum][3]=counter;
                     
                     if (counter!=1) counter--;else break;
                 }
   }
   public static void extendToRightCopular(int i,int triplenum,int counter,String str,LexicalizedParser lp){
              //counter= verbsPosition[wordnum]+1;
              while(!(POStagword[counter].equals("NN"))&!(POStagword[counter].equals("NNS"))&!
                      (POStagword[counter].equals("CD"))&!
                      //(POStagword[counter].equals("VBG"))&!
                      (POStagword[counter].equals("NNP"))&!(POStagword[counter].equals("JJ"))&!
                      POStagword[counter].equals("POS")
                      &!POStagword[counter].equals("IN")
                      &!POStagword[counter].equals("CC")
                      &!POStagword[counter].equals("NNPS")
                      &!POStagword[counter].equals("PRP")
                      &!POStagword[counter].equals("DT")
                      &!POStagword[counter].equals("RBR") )
                    if(counter!=wordsNumber)counter++;else break;
              
              triples[triplenum][i]=POSword[counter];
              if(i==0)
              { triplesPosition[triplenum][1]=counter;
                  triplesPosition[triplenum][0]=counter;
              }   
               if(i==2)
               {  triplesPosition[triplenum][4]=counter;
               triplesPosition[triplenum][3]=counter;
               }
              //triplesPosition[triplenum][i]=counter;
                  if(counter!=wordsNumber)counter++;
              while(
                     (POStagword[counter].equals("NN")||POStagword[counter].equals("NNS")||POStagword[counter].equals("CD")||POStagword[counter].equals("IN")||
                       POStagword[counter].equals("NNP")||POStagword[counter].equals("JJ")||POStagword[counter].equals("JJS")||
                      //POStagword[counter].equals("VBG")||
                      POStagword[counter].equals("PRP$")
                      ||POStagword[counter].equals("POS")
                      ||POStagword[counter].equals("IN")
                      ||POStagword[counter].equals("RBR")
                      ||POStagword[counter].equals("CC")
                      ||POStagword[counter].equals("NNPS")
                      ||POStagword[counter].equals("PRP")
                      ||POStagword[counter].equals("DT"))&(counter!=wordsNumber))
                 {
                     triples[triplenum][i]=triples[triplenum][i]+" "+POSword[counter];
                       if(i==0)
                        triplesPosition[triplenum][1]=counter;
                    if(i==2)
                        triplesPosition[triplenum][4]=counter;
                     //triplesPosition[triplenum][i]=counter;
                     // triplesPosition[triplenum][i]=counter;
                     if(counter!=wordsNumber)counter++;else break;
                 }
   }
   public static void extendToRightCopularMinimal(int i,int triplenum,int counter,String str,LexicalizedParser lp){
              //counter= verbsPosition[wordnum]+1;
              while(!(POStagword[counter].equals("NN"))&!(POStagword[counter].equals("NNS"))&!
                     (POStagword[counter].equals("CD"))&!
                      //(POStagword[counter].equals("VBG"))&!
                      (POStagword[counter].equals("NNP"))&!(POStagword[counter].equals("JJ"))&!
                      POStagword[counter].equals("POS")
                      //&!POStagword[counter].equals('"')
                      //&!POStagword[counter].equals("CC")
                      &!POStagword[counter].equals("NNPS")
                      &!POStagword[counter].equals("PRP")
                     // &!POStagword[counter].equals("DT")
                      )
                    if(counter!=wordsNumber)counter++;else break;
              
               Minimaltriples[triplenum][i]=POSword[counter];
              if(i==0)
              {    MinimaltriplesPosition[triplenum][1]=counter;
                   MinimaltriplesPosition[triplenum][0]=counter;
              }   
               if(i==2)
               {   MinimaltriplesPosition[triplenum][4]=counter;
                   MinimaltriplesPosition[triplenum][3]=counter;
               }
              //triplesPosition[triplenum][i]=counter;
                  if(counter!=wordsNumber)counter++;
              while(
                     (POStagword[counter].equals("NN")||POStagword[counter].equals("NNS")||POStagword[counter].equals("CD")
                      //||POStagword[counter].equals("IN")
                      ||POStagword[counter].equals("NNP")||POStagword[counter].equals("JJ")||POStagword[counter].equals("JJS")
                      //||POStagword[counter].equals("VBG")
                      ||POStagword[counter].equals("PRP$")
                      ||POStagword[counter].equals("POS")
                      //||POStagword[counter].equals('"')
                    // ||POStagword[counter].equals("CC")
                      ||POStagword[counter].equals("NNPS")
                      ||POStagword[counter].equals("PRP")
                      //||POStagword[counter].equals("DT")
                      )&(counter!=wordsNumber))
                 {
                      Minimaltriples[triplenum][i]= Minimaltriples[triplenum][i]+" "+POSword[counter];
                       if(i==0)
                         MinimaltriplesPosition[triplenum][1]=counter;
                    if(i==2)
                         MinimaltriplesPosition[triplenum][4]=counter;
                     //triplesPosition[triplenum][i]=counter;
                     // triplesPosition[triplenum][i]=counter;
                     if(counter!=wordsNumber)counter++;else break;
                 }
   }
   public static void extendToNounGroup(int i,int triplenum,String str,LexicalizedParser lp){
   
   //we must extend the object that is only one word to a noun group 
            int counter=0;            
                    if(i==0)
                        counter=triplesPosition[triplenum][0]+1;
                    if(i==2)
                        counter=triplesPosition[triplenum][4]+1;
               
                int sw1=0;
               if(counter<wordsNumber)
                while((POStagword[counter].equals("NN")||
                        //POStagword[counter].equals("VBG")||
                        POStagword[counter].equals("NNS")||POStagword[counter].equals("CD")||POStagword[counter].equals("IN")||
                       POStagword[counter].equals("NNP")||POStagword[counter].equals("JJ")||POStagword[counter].equals("JJS")||POStagword[counter].equals("PRP$")
                      ||POStagword[counter].equals("POS")
                        //||POStagword[counter].equals('"')
                      //||POStagword[counter].equals("CC")
                         ||POStagword[counter].equals("NNPS")
                          ||POStagword[counter].equals("PRP")
                        ||POStagword[counter].equals("DT")
                        ||POStagword[counter].equals("RB"))&(counter!=wordsNumber))
               {
                
                      sw1=1;
                     triples[triplenum][i]=triples[triplenum][i]+" "+POSword[counter];
                      if(i==0)
                        triplesPosition[triplenum][1]=counter;
                    if(i==2)
                        triplesPosition[triplenum][4]=counter;
                      
                  if(counter!=wordsNumber-1)counter++;else break;
                 }
              
              
              //----------------------------------------------------------
              //we must extend the object that is one word to noun group 
            //  if (sw1==0){    
             if(i==0)
                        counter=triplesPosition[triplenum][0]-1;
                    if(i==2)
                        counter=triplesPosition[triplenum][3]-1;  
            
            //counter= counter-2;
                if(counter!=0){
               while((POStagword[counter].equals("NN")||
                       //POStagword[counter].equals("VBG")||
                       POStagword[counter].equals("NNS")||POStagword[counter].equals("IN")||POStagword[counter].equals("CD")||
                       POStagword[counter].equals("NNP")||POStagword[counter].equals("JJ")||POStagword[counter].equals("JJS")||POStagword[counter].equals("PRP$")
                       ||POStagword[counter].equals("POS")
                       //||POStagword[counter].equals('"')
                       //||POStagword[counter].equals("CC")
                       ||POStagword[counter].equals("NNPS")
                         ||POStagword[counter].equals("PRP")
                       ||POStagword[counter].equals("DT")
                       ||POStagword[counter].equals("RB"))&(counter!=wordsNumber))
                
              {
                     triples[triplenum][i]=POSword[counter]+" "+triples[triplenum][i];
                  //shayad
                    if(i==0)
                        triplesPosition[triplenum][0]=counter;
                    if(i==2)
                        triplesPosition[triplenum][3]=counter;   
                  // triplesPosition[triplenum][i]--; 
                  if(counter!=1)counter--;else break;
                 }
                }
             // }
              
              //----------------------------------------------------------
              
   
   }
   public static void extendToNounGroupMinimal(int i,int triplenum,String str,LexicalizedParser lp){
   
   //we must extend the object that is only one word to a noun group 
            int counter=0;            
                    if(i==0)
                        counter= MinimaltriplesPosition[triplenum][0]+1;
                    if(i==1)
                        counter= MinimaltriplesPosition[triplenum][2]+1;
               
                    if(i==2)
                        counter= MinimaltriplesPosition[triplenum][4]+1;
               
                int sw1=0;
               if(counter<wordsNumber)
                while((POStagword[counter].equals("NN")||
                        //POStagword[counter].equals("VBG")||
                        POStagword[counter].equals("NNS")
                        ||POStagword[counter].equals("CD")
                        //||POStagword[counter].equals("IN")
                         ||POStagword[counter].equals("NNP")||POStagword[counter].equals("JJ")||POStagword[counter].equals("JJS")||POStagword[counter].equals("PRP$")
                        // ||POStagword[counter].equals("POS")
                         //||POStagword[counter].equals('"')
                         //||POStagword[counter].equals("CC")
                         ||POStagword[counter].equals("NNPS")
                         ||POStagword[counter].equals("PRP")
                         // ||POStagword[counter].equals("DT")
                         ||POStagword[counter].equals("RB"))&(counter!=wordsNumber))
               {
                
                      sw1=1;
                      Minimaltriples[triplenum][i]= Minimaltriples[triplenum][i]+" "+POSword[counter];
                      if(i==0)
                         MinimaltriplesPosition[triplenum][1]=counter;
                       if(i==1)
                         MinimaltriplesPosition[triplenum][2]=counter;
                      if(i==2)
                         MinimaltriplesPosition[triplenum][4]=counter;
                      
                  if(counter!=wordsNumber-1)counter++;else break;
                 }
              
              
              //----------------------------------------------------------
              //we must extend the object that is one word to noun group 
            //  if (sw1==0){    
             if(i==0)
                        counter= MinimaltriplesPosition[triplenum][0]-1;
             if(i==1)
                         counter=MinimaltriplesPosition[triplenum][2]-1;      
             if(i==2)
                        counter= MinimaltriplesPosition[triplenum][3]-1;  
            
            //counter= counter-2;
                if(counter!=0){
               while((POStagword[counter].equals("NN")
                       //||POStagword[counter].equals("VBG")
                       ||POStagword[counter].equals("NNS")
                       //||POStagword[counter].equals("IN")
                       ||POStagword[counter].equals("CD")
                       ||POStagword[counter].equals("NNP")||POStagword[counter].equals("JJ")
                       ||POStagword[counter].equals("JJS")||POStagword[counter].equals("PRP$")
                       //||POStagword[counter].equals("POS")
                       //||POStagword[counter].equals('"')
                       //||POStagword[counter].equals("CC")
                       ||POStagword[counter].equals("NNPS")
                       ||POStagword[counter].equals("PRP")
                       //||POStagword[counter].equals("DT")
                       ||POStagword[counter].equals("RB"))&(counter!=wordsNumber))
                
              {
                      Minimaltriples[triplenum][i]=POSword[counter]+" "+ Minimaltriples[triplenum][i];
                  //shayad
                    if(i==0)
                         MinimaltriplesPosition[triplenum][0]=counter;
                    if(i==2)
                         MinimaltriplesPosition[triplenum][3]=counter;   
                  // triplesPosition[triplenum][i]--; 
                  if(counter!=1)counter--;else break;
                 }
                }
             // }
              
              //----------------------------------------------------------
              
   
   }
   public static String extendToNounGroupForAWordDirection(int phrasePosition,String Word,String str,LexicalizedParser lp,int direction){
   //direction   left=1   right=2   left and right=3
   //we must extend the object that is only one word to a noun group 
               Word="";        
              if(direction==2 || direction==3) { 
                  int counter=phrasePosition;
                  RightLimitOfWord=phrasePosition;
                  int sw1=0;
                  if(counter<wordsNumber)
                  while((POStagword[counter].equals("NN")||
                          //POStagword[counter].equals("VBG")||
                          POStagword[counter].equals("NNS")||POStagword[counter].equals("CD")||POStagword[counter].equals("IN")||
                          POStagword[counter].equals("NNP")||POStagword[counter].equals("JJ")||POStagword[counter].equals("JJS")||POStagword[counter].equals("PRP$")
                        ||POStagword[counter].equals("POS")
                          //||POStagword[counter].equals('"')
                        ||POStagword[counter].equals("CC")
                        ||POStagword[counter].equals("NNPS")
                            ||POStagword[counter].equals("PRP")
                        ||POStagword[counter].equals("DT")||POStagword[counter].equals("RB"))&(counter!=wordsNumber)){
                         sw1=1;
                         Word=Word+" "+POSword[counter];
                         RightLimitOfWord=counter;
                         if(counter!=wordsNumber-1)counter++;else break;
                    }
              
                 }
              //----------------------------------------------------------
              //we must extend the object that is one word to noun group 
              if(direction==1 || direction==3) {
                                      
                int counter=phrasePosition;
                if(direction==3)
                   counter--; 
                  LeftLimitOfWord=phrasePosition;
                 // counter= counter-1;
                  if(counter!=0){
                  while((POStagword[counter].equals("NN")||
                          //POStagword[counter].equals("VBG")||
                          POStagword[counter].equals("NNS")||POStagword[counter].equals("IN")||POStagword[counter].equals("CD")||
                       POStagword[counter].equals("NNP")||POStagword[counter].equals("JJ")||POStagword[counter].equals("JJS")||POStagword[counter].equals("PRP$")
                       ||POStagword[counter].equals("POS")
                       //||POStagword[counter].equals('"')
                       ||POStagword[counter].equals("CC")
                            ||POStagword[counter].equals("PRP")
                       ||POStagword[counter].equals("DT")||POStagword[counter].equals("RB"))&(counter!=wordsNumber))
                
                  {
                     Word=POSword[counter]+" "+Word;
                     LeftLimitOfWord=counter;
                     if(counter!=1)counter--;else break;
                  }
                }
              }
              
              //----------------------------------------------------------
              
   
   return Word;
   }
   public static String extendToNounGroupForAWordDirectionMinimal(int phrasePosition,String Word,String str,LexicalizedParser lp,int direction){
   //direction   left=0   right=2   left and right=3
   //we must extend the object that is only one word to a noun group 
               Word="";        
              if(direction==2 || direction==3) { 
                  int counter=phrasePosition;
                  RightLimitOfWord=phrasePosition;
                  int sw1=0;
                  if(counter<wordsNumber)
                  while((POStagword[counter].equals("NN")
                          //||POStagword[counter].equals("VBG")
                          ||POStagword[counter].equals("NNS")
                         ||POStagword[counter].equals("CD")
                          //||POStagword[counter].equals("IN")
                          ||POStagword[counter].equals("NNP")||POStagword[counter].equals("JJ")
                          ||POStagword[counter].equals("JJS")||POStagword[counter].equals("PRP$")
                          ||POStagword[counter].equals("POS")
                          //||POStagword[counter].equals('"')
                          //||POStagword[counter].equals("CC")
                          ||POStagword[counter].equals("NNPS")
                          ||POStagword[counter].equals("PRP")
                          //||POStagword[counter].equals("DT")
                          ||POStagword[counter].equals("RB")
                          )
                          &
                          (counter!=wordsNumber)
                          ){
                         sw1=1;
                         Word=Word+" "+POSword[counter];
                         RightLimitOfWord=counter;
                         if(counter!=wordsNumber-1)counter++;else break;
                    }
              
                 }
              //----------------------------------------------------------
              //we must extend the object that is one word to noun group 
              if(direction==1 || direction==3) {
                                      
                int counter=phrasePosition;
                if(direction==3)
                   counter--; 
                  LeftLimitOfWord=phrasePosition;
                 // counter= counter-1;
                  if(counter!=0){
                  while((POStagword[counter].equals("NN")
                          //||POStagword[counter].equals("VBG")
                          ||POStagword[counter].equals("NNS")
                         // ||POStagword[counter].equals("IN")
                          ||POStagword[counter].equals("CD")
                          ||POStagword[counter].equals("NNP")||POStagword[counter].equals("JJ")
                          ||POStagword[counter].equals("JJS")||POStagword[counter].equals("PRP$")
                          ||POStagword[counter].equals("POS")
                          //||POStagword[counter].equals('"')
                          //||POStagword[counter].equals("CC")
                            ||POStagword[counter].equals("PRP")
                          //||POStagword[counter].equals("DT")
                          ||POStagword[counter].equals("RB"))&(counter!=wordsNumber))
                
                  {
                     Word=POSword[counter]+" "+Word;
                     LeftLimitOfWord=counter;
                     if(counter!=1)counter--;else break;
                  }
                }
              }
              
              //----------------------------------------------------------
              
   
   return Word;
   }
   public static void PrintDependencies(List<TypedDependency> tdl){
   
       //..................................prints dependency relations............................................. 
    int index=0;
    for (TypedDependency thelp : tdl) { 
            System.out.print(tdl.get(index).reln());
            System.out.print('('+tdl.get(index).gov().value()+','+tdl.get(index).dep().value()+')');
    System.out.println();
            index++;
    }
    System.out.println();
    
   }
   public static List<TypedDependency> ParseLine(String line,LexicalizedParser lp) {
   //List<TypedDependency> tdl;
    Tree parse;   
    TokenizerFactory<CoreLabel> tokenizerFactory =  
    PTBTokenizer.factory(new CoreLabelTokenFactory(), ""); 
    List<CoreLabel> rawWords2 =  tokenizerFactory.getTokenizer(new StringReader(line)).tokenize(); 
    parse = lp.apply(rawWords2); 
    //parse.pennPrint();
    TreebankLanguagePack tlp = new PennTreebankLanguagePack(); 
    GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory(); 
    GrammaticalStructure gs = gsf.newGrammaticalStructure(parse); 
    List<TypedDependency> tdl = gs.typedDependenciesCCprocessed(); 
    return tdl;
   }
   ///////noun functions
   public static int IsNoun(int i){
         if(
                 (
                 POStagword[i].equals("NN")||POStagword[i].equals("VBG")||POStagword[i].equals("NNS")||
                 POStagword[i].equals("NNP")||POStagword[i].equals("JJ")||POStagword[i].equals("JJS")||
                 POStagword[i].equals("PRP$")||POStagword[i].equals("NNPS")||
                 POStagword[i].equals("PRP")
                 )
                 
            ){
             return 1;
         }              
        return 0;
         }
   public static int CommaBetweenTwoNoun(int i,int objectPosition,String Object,String line,LexicalizedParser lp){
    int sw=0;
    EndNum=i;
                  if(objectPosition!=1)   
                   if(POSword[objectPosition-1].matches(",")){
                         i++;
                         EndNum=i;
                         triples[i][0]=triples[i-1][0];triplesPosition[i][0]=triplesPosition[i-1][0];triplesPosition[i][1]=triplesPosition[i-1][1];
                         triples[i][1]=triples[i-1][1];triplesPosition[i][1]=triplesPosition[i-1][1];
                         typeTriples[i]=typeTriples[i-1];
                         String str1=extendToNounGroupForAWordDirection(objectPosition-2,POSword[objectPosition-2],line,lp,1);
 
                         triples[i][2]=str1;triplesPosition[i][4]=objectPosition-2;triplesPosition[i][3]=LeftLimitOfWord;
                     }
                     if(objectPosition!=wordsNumber) 
                     if(POSword[objectPosition+1].matches(",")){
                         sw=1;                       
// i++;
                       //  triples[i][0]=triples[i-1][0];triplesPosition[i][0]=triplesPosition[i-1][0];triplesPosition[i][1]=triplesPosition[i-1][1];
                        // triples[i][1]=triples[i-1][1];triplesPosition[i][1]=triplesPosition[i-1][1];
                        String str1=extendToNounGroupForAWordDirection(objectPosition+2,POSword[objectPosition+2],line,lp,2);
                        triples[i][2]=triples[i][2]+" "+str1;triplesPosition[i][3]=objectPosition+2;triplesPosition[i][3]=objectPosition+4;triplesPosition[i][4]=RightLimitOfWord;
                        
                         return sw;
                      }
                           
                                         
                   
return sw;   
} 
   public static int CommaBetweenTwoNounMinimal(int i,int objectPosition,String Object,String line,LexicalizedParser lp){
    int sw=0;
    MinimalEndNum=i;
                  if(objectPosition!=1)   
                   if(POSword[objectPosition-1].matches(",")){
                         i++;
                         MinimalEndNum=i;
                         Minimaltriples[i][0]=Minimaltriples[i-1][0];MinimaltriplesPosition[i][0]=MinimaltriplesPosition[i-1][0];MinimaltriplesPosition[i][1]=MinimaltriplesPosition[i-1][1];
                         Minimaltriples[i][1]=Minimaltriples[i-1][1];MinimaltriplesPosition[i][1]=MinimaltriplesPosition[i-1][1];
                        MinimaltypeTriples[i]=MinimaltypeTriples[i-1];
                         String str1=extendToNounGroupForAWordDirectionMinimal(objectPosition-2,POSword[objectPosition-2],line,lp,1);
 
                         Minimaltriples[i][2]=str1;MinimaltriplesPosition[i][4]=objectPosition-2;MinimaltriplesPosition[i][3]=LeftLimitOfWord;
                     }
                     if(objectPosition!=wordsNumber) 
                     if(POSword[objectPosition+1].matches(",")){
                         sw=1;                       
// i++;
                       //  triples[i][0]=triples[i-1][0];triplesPosition[i][0]=triplesPosition[i-1][0];triplesPosition[i][1]=triplesPosition[i-1][1];
                        // triples[i][1]=triples[i-1][1];triplesPosition[i][1]=triplesPosition[i-1][1];
                        String str1=extendToNounGroupForAWordDirectionMinimal(objectPosition+2,POSword[objectPosition+2],line,lp,2);
                        Minimaltriples[i][2]=Minimaltriples[i][2]+" "+str1;MinimaltriplesPosition[i][3]=objectPosition+2;MinimaltriplesPosition[i][3]=objectPosition+4;MinimaltriplesPosition[i][4]=RightLimitOfWord;
                        
                         return sw;
                      }
                           
                                         
                   
return sw; 
}
   public static int NPprepNp(int pos){
       if(POStagword[pos+1].equals("IN"))
          return pos+1;
   return 0;
   }
   public static void convertConnectedWords(int i,int SubjObj,int MinMax, String conectedWord,int ConnectedWordPosition,String sentence,List<TypedDependency> tdl){
   //-----------------------------connected words------------------------------------
    String subj="";
   if (conectedWord.matches("that")||conectedWord.matches("who")
           ||conectedWord.matches("which")||conectedWord.matches("whom")
           ||conectedWord.matches("where")){
                           int RefrenceIndex=FindRerenceOfThat(ConnectedWordPosition-1,tdl);
                           if(RefrenceIndex!=0){
                             //subj=extendToNounGroupForAWordDirection(RefrenceIndex,POSword[RefrenceIndex],sentence,lp,1);
                              if (MinMax==1){
                                   triples[i][SubjObj]=subj;
                                   if(SubjObj==0){
                                       triplesPosition[i][0]=LeftLimitOfWord;
                                       triplesPosition[i][1]=RightLimitOfWord;
                                    }else{
                                         triplesPosition[i][3]=LeftLimitOfWord;
                                         triplesPosition[i][4]=RightLimitOfWord;
                                          }
                                       
                                }
                              else{
                                  Minimaltriples[i][SubjObj]=subj;
                                   if(SubjObj==0){
                                       MinimaltriplesPosition[i][0]=LeftLimitOfWord;
                                       MinimaltriplesPosition[i][1]=RightLimitOfWord;
                                    }else{
                                         MinimaltriplesPosition[i][3]=LeftLimitOfWord;
                                         MinimaltriplesPosition[i][4]=RightLimitOfWord;
                                          }
                                   
                                  
                                }
                        
                        }
                          else{
                         
                               if (MinMax==0){
                                             Minimaltriples[i][SubjObj]=POSword[ConnectedWordPosition-1];
                                             if(SubjObj==0){
                                                 MinimaltriplesPosition[i][0]=ConnectedWordPosition-1;
                                                 MinimaltriplesPosition[i][1]=ConnectedWordPosition-1;
                                             }
                                              else {
                                                MinimaltriplesPosition[i][3]=ConnectedWordPosition-1;
                                                MinimaltriplesPosition[i][4]=ConnectedWordPosition-1;
                                             }
                               }
                               if (MinMax==1){
                                             triples[i][SubjObj]=POSword[ConnectedWordPosition-1];
                                             if(SubjObj==0){
                                                 triplesPosition[i][0]=ConnectedWordPosition-1;
                                                 triplesPosition[i][1]=ConnectedWordPosition-1;
                                             }
                                             else{
                                                 triplesPosition[i][3]=ConnectedWordPosition-1;
                                                 triplesPosition[i][4]=ConnectedWordPosition-1;
                                             }
                                }
                            }       
    
   }
   }
   public static void FullNullObject(String line){
       String[] str;int counter;
       for(int j=0;j<EndNum;j++){
      
           if(triples[j][0]!="" & triples[j][2]==""){
            triples[j][2]="";
               counter= triplesPosition[j][2]+1; 
               if(!POStagword[counter].equals(","))
             while(counter!=wordsNumber & !POStagword[counter].equals("''")){                
                       triples[j][2]=triples[j][2]+" "+POSword[counter];
                       if(counter!=wordsNumber-1)counter++;else break;
                 }
            }
        }
   }
   public static void FullNullObjectMinimal(String line){
       String[] str;int counter;
       for(int j=0;j<MinimalEndNum;j++){
      
           if(Minimaltriples[j][0]!="" & Minimaltriples[j][2]==""){
            Minimaltriples[j][2]="";
               counter= MinimaltriplesPosition[j][2]+1; 
               if(!POStagword[counter].equals(","))
             while(counter!=wordsNumber & !POStagword[counter].equals("''")){                
                       Minimaltriples[j][2]=Minimaltriples[j][2]+" "+POSword[counter];
                       if(counter!=wordsNumber-1)counter++;else break;
                 }
            }
        }
   }
   //////verb functions
   public static int NotDependencyBetwweenToVerbs(String dep,String str){
        //-------------------------------
         int counterEquals;
              counterEquals=1; 
              for(int b=0;b<CounterVerbs;b++)
                  if(dep.matches(verbs[b])){ 
                      counterEquals=0;
                      break;
                  }
              
          //----------------------------------    
   return counterEquals;
   }
   public static int VerbToVerb(int verbNum){
       if(verbsPosition[verbNum]>2)   
         if (POSword[verbsPosition[verbNum]-1].matches("to")|| POSword[verbsPosition[verbNum]-2].matches("to"))
             return 1;
          else return 0;
       else return 0;
   }
   
   public static int NotCopularVerb(String str,List<TypedDependency> tdl){
       //--------------------------------if verb is am, is , was,.....---------------------------------
   String[] str1; String dep1,rel1,gov1; 
    int sw1=1;
   if((str.equals("was")||str.equals("were")||
        str.equals("am")||str.equals("is")||
             str.equals("are")||str.equals("had")||
             str.equals("has")||str.equals("have")
             ||str.equals("did")||str.equals("does")
             )) {
        
             for(int j=0;j<CounterVerbs;j++){
              int index1=0;
                 for (TypedDependency t1 : tdl){
                   //may be verb is copular verb such as did.....serve.verb did should be deleated
                     str1=tdl.get(index1).dep().toString().split("/");
                     dep1=str1[0];
                     str1=tdl.get(index1).reln().toString().split("/");
                     rel1=str1[0];
                     str1=tdl.get(index1).gov().toString().split("/");
                     gov1=str1[0];
                    if (dep1.equals(verbs[j])& gov1.equals(str) & rel1.equals("aux"))
                    { index1++;sw1=1;return sw1;}
                    else
                    index1++;
                }
             }
       
       
       sw1=0; }
   return sw1;//0 not main verb,1 main verb
   }
   public static int BeCopularMainVerb(String str,int j,List<TypedDependency> tdl ){
   //--------------------------------if verb is am, is , was,.....---------------------------------
   String[] str1; String dep1,rel1,gov1; 
    int sw1=1;
   if((str.equals("was")||str.equals("were")||
        str.equals("am")||str.equals("is")||
             str.equals("are")||str.equals("had")||
             str.equals("has")||str.equals("have")
             ||str.equals("did")||str.equals("does")
             ) & (!POStagword[verbsPosition[j]+1].equals("VBN"))& 
              !(POStagword[verbsPosition[j]+1].equals("VB"))&
              !(POStagword[verbsPosition[j]+1].equals("VBP"))&
              !(POStagword[verbsPosition[j]+1].equals("VBD"))&
              !(POStagword[verbsPosition[j]+1].equals("VBZ"))
              ){  
              
              int index1=0;
             
              for (TypedDependency t1 : tdl){
                   //may be verb is copular verb such as did.....serve.verb did should be deleated
                     str1=tdl.get(index1).dep().toString().split("/");
                     dep1=str1[0];
                     str1=tdl.get(index1).reln().toString().split("/");
                     rel1=str1[0];
                    if (dep1.equals(str)& ((rel1.equals("ccomp"))||rel1.equals("auxpass")||(rel1.equals("aux"))))
                    { index1++;sw1=0;return sw1;}
                    else
                    index1++;
                }
              
             
             for(j=0;j<CounterVerbs;j++){
                  index1=0;
              for (TypedDependency t1 : tdl){
                   //may be verb is copular verb such as did.....serve.verb did should be deleated
                     str1=tdl.get(index1).dep().toString().split("/");
                     dep1=str1[0];
                     str1=tdl.get(index1).reln().toString().split("/");
                     rel1=str1[0];
                     str1=tdl.get(index1).gov().toString().split("/");
                     gov1=str1[0];
                    if (dep1.matches(verbs[j])& gov1.matches(str) & rel1.matches("aux"))
                    { index1++;sw1=0;return sw1;}
                    if (gov1.matches(verbs[j])& dep1.matches(str) & (rel1.matches("aux")||rel1.matches("auxpass")))
                    { index1++;sw1=0;return sw1;}
                    else
                    index1++;
                }
             }
             }
     
           else sw1=0;
   return sw1;//0 not main verb,1 main verb
   }
   //**********
   public static int AddNounPhraseTriplePatternP1(int firstNoun,int LengthOfFirstNoun,int secondNoun,String str,LexicalizedParser lp){
    MinimalEndNum++;
    int i=MinimalEndNum;
    
    Minimaltriples[i][0]=POSword[secondNoun]; MinimaltriplesPosition[i][0]=POSwordPositionStart[secondNoun];MinimaltriplesPosition[i][1]=POSwordPositionStart[secondNoun];//extendToNounGroup(0,i,sentence,lp);
    Minimaltriples[i][1]="has a relation with"; MinimaltriplesPosition[i][2]=0;
    MinimaltypeTriples[i]="Noun phrase relation(p1): ";
    for(int k=1;k<=LengthOfFirstNoun;k++){
    Minimaltriples[i][2]=Minimaltriples[i][2]+POSword[firstNoun+k-1];}
    MinimaltriplesPosition[i][3]=firstNoun;MinimaltriplesPosition[i][4]=firstNoun+LengthOfFirstNoun-1;
   Minimaltriples[i][2]=extendToNounGroupForAWordDirectionMinimal(MinimaltriplesPosition[i][3],Minimaltriples[i][2],str,lp,1);
    return 0;}
   
   public static int AddNounPhraseTriplePatternP2(int firstNoun,int LengthOfFirstNoun,int secondNoun){
    MinimalEndNum++;
    int i=MinimalEndNum;
    MinimaltypeTriples[i]="Noun phrase relation(p2):";
    Minimaltriples[i][0]=POSword[firstNoun]; MinimaltriplesPosition[i][0]=POSwordPositionStart[firstNoun];MinimaltriplesPosition[i][1]=POSwordPositionStart[firstNoun];//extendToNounGroup(0,i,sentence,lp);
    Minimaltriples[i][1]=POSword[secondNoun]; MinimaltriplesPosition[i][2]=POSwordPositionStart[secondNoun];
    Minimaltriples[i][2]="?o";
    MinimaltriplesPosition[i][3]=0;MinimaltriplesPosition[i][4]=0;
    
   return 0;}
   public static int AddNounPhraseTriplePatternP3(int firstNoun,int LengthOfFirstNoun,int secondNoun){
    MinimalEndNum++;
    int i=MinimalEndNum;
    MinimaltypeTriples[i]="Noun phrase relation(p3):";
    Minimaltriples[i][0]="?s1";MinimaltriplesPosition[i][0]=0;MinimaltriplesPosition[i][1]=0;//extendToNounGroup(0,i,sentence,lp);
    Minimaltriples[i][1]="?p1"; MinimaltriplesPosition[i][2]=0;
    Minimaltriples[i][2]=POSword[firstNoun];
    MinimaltriplesPosition[i][3]=POSwordPositionStart[firstNoun];MinimaltriplesPosition[i][4]=POSwordPositionStart[firstNoun];
    MinimalEndNum++;
    i++;
    MinimaltypeTriples[i]="Noun phrase relation(p3):";
    Minimaltriples[i][0]="?s1";MinimaltriplesPosition[i][0]=0;MinimaltriplesPosition[i][1]=0;//extendToNounGroup(0,i,sentence,lp);
    Minimaltriples[i][1]="?p2"; MinimaltriplesPosition[i][2]=0;
    Minimaltriples[i][2]=POSword[secondNoun];
    MinimaltriplesPosition[i][3]=POSwordPositionStart[secondNoun];MinimaltriplesPosition[i][4]=POSwordPositionStart[secondNoun];
    
   return 0;}
   public static int AddNotCopularVerbRelationTriple(String str,int j,List<TypedDependency> tdl,LexicalizedParser lp,int i,String sentence){
     
     //Initialization
     for(int f=0;f<=6;f++){subjectFillers[f]="";subjectFillersPositions[f]=0;objectFillers[f]="";objectFillersPositions[f]=0;directObjectFillersPositions[f]=0;}
     subjectFillersNumber=0;objectFillersNumber=0;
     IndirectObject="";
     EndNum=i;
     //----------------------------------
     int index=0,depIndex,govIndex;String gov,dep,rel,govTag;String[] strTemp; 
     for (TypedDependency t : tdl){ 
         gov=tdl.get(index).gov().value();
         dep=tdl.get(index).dep().value();
         depIndex=tdl.get(index).dep().index();
         govTag=tdl.get(index).gov().tag();
         govIndex=tdl.get(index).gov().index();
         rel=tdl.get(index).reln().toString();
         if(gov.matches(str)||(dep.matches(str)& rel.matches("acl")))
           
             if(!dep.matches(str)){
             if(NotDependencyBetwweenToVerbs(dep,str)==1){
              
               FullUpSubjectFillers(gov,dep,rel,EndNum,depIndex,lp,govTag,govIndex,sentence,tdl);
               FullUpObjectFillers(gov,dep,rel,EndNum,depIndex,lp,govTag,govIndex,sentence,str);
             
           }}
             else{
              FullUpSubjectFillers(gov,dep,rel,EndNum,depIndex,lp,govTag,govIndex,sentence,tdl);
               FullUpObjectFillers(gov,dep,rel,EndNum,depIndex,lp,govTag,govIndex,sentence,str);
             }
         index++;
        }
       i=makeTriplesFromFillers(i,str,j,sentence,lp,tdl);
   return i;
   }  
   public static int AddNotCopularVerbRelationTripleMinimal(String str,int j,List<TypedDependency> tdl,LexicalizedParser lp,int i,String sentence){
     
     //Initialization
   //  for(int f=0;f<=6;f++){subjectFillers[f]="";subjectFillersPositions[f]=0;objectFillers[f]="";objectFillersPositions[f]=0;directObjectFillersPositions[f]=0;}
   //  subjectFillersNumber=0;objectFillersNumber=0;
   //  IndirectObject="";
    // EndNum=i;
     MinimalEndNum=i;
     //----------------------------------
    /* int index=0,depIndex,govIndex;String gov,dep,rel,govTag;String[] strTemp; 
     for (TypedDependency t : tdl){ 
         gov=tdl.get(index).gov().value();
         dep=tdl.get(index).dep().value();
         depIndex=tdl.get(index).dep().index();
         govTag=tdl.get(index).gov().tag();
         govIndex=tdl.get(index).gov().index();
         rel=tdl.get(index).reln().toString();
         if(gov.matches(str)||(dep.matches(str)& rel.matches("acl")))
             if(!dep.matches(str)){
             if(NotDependencyBetwweenToVerbs(dep,str)==1){
              
               FullUpSubjectFillers(gov,dep,rel,MinimalEndNum,depIndex,lp,govTag,govIndex,sentence);
               FullUpObjectFillers(gov,dep,rel,MinimalEndNum,depIndex,lp,govTag,govIndex,sentence,str);
             
           }}
         else{
               FullUpSubjectFillers(gov,dep,rel,MinimalEndNum,depIndex,lp,govTag,govIndex,sentence);
               FullUpObjectFillers(gov,dep,rel,MinimalEndNum,depIndex,lp,govTag,govIndex,sentence,str);
             }
         index++;
        }*/
       i=makeTriplesFromFillersMinimal(i,str,j,sentence,lp,tdl);
   return i;
   }  
   public static int IsGenitivePossSubject(int i){
      int counter=i;
       while((POStagword[counter].equals("NN")||
                        //POStagword[counter].equals("VBG")||
                        POStagword[counter].equals("NNS")||POStagword[counter].equals("CD")||POStagword[counter].equals("IN")||
                       POStagword[counter].equals("NNP")||POStagword[counter].equals("JJ")||POStagword[counter].equals("JJS")||POStagword[counter].equals("PRP$")
                     // ||POStagword[counter].equals("POS")
                        //||POStagword[counter].equals('"')
                      //||POStagword[counter].equals("CC")
                         ||POStagword[counter].equals("NNPS")
                          ||POStagword[counter].equals("PRP")
                       // ||POStagword[counter].equals("DT")
               //         ||POStagword[counter].equals("RB")
               )&(counter!=wordsNumber))
                counter++;
        if(POSword[counter].matches("'s")){counter++; return counter; }
   return 0;}
   
   public static void FullUpSubjectFillers(String gov,String dep,String rel,int i,int depIndex,LexicalizedParser lp,String govTag,int govIndex,String sentence,List<TypedDependency> tdl){
     
       if (rel.equals("nsubj")||rel.equals("nsubjpass")){
         
           subjectFillers[subjectFillersNumber]=dep; 
           subjectFillersPositions[subjectFillersNumber]=depIndex;
           if(IsGenitivePossSubject(depIndex)!=0)
           {
             subjectFillers[subjectFillersNumber]=POSword[IsGenitivePossSubject(depIndex)];
             subjectFillersPositions[subjectFillersNumber]=IsGenitivePossSubject(depIndex);
           }
          
           subjectFillersNumber++;
         }else if(rel.equals("acl")){
          subjectFillers[subjectFillersNumber]=gov;
           subjectFillersPositions[subjectFillersNumber]=govIndex;
           subjectFillersNumber++;
         }
       int index=FindRerenceOfThat(subjectFillersPositions[subjectFillersNumber-1],tdl);
       if(index!=0){
           subjectFillers[subjectFillersNumber-1]=POSword[index]; 
           subjectFillersPositions[subjectFillersNumber-1]=index;
       }
   }
   public static int FullUpObjectFillers(String gov,String dep,String rel,int i,int depIndex,LexicalizedParser lp,String govTag,int govIndex,String sentence,String verb){
   
      //------------------------------------full up object-----------------------------------------------------
     
      if( rel.equals("dobj")||       
           //How many Golden Globe awards did the daughter of Henry Fonda win? (nsubj*VB*dobj)
            rel.equals("advcl")//In which town was the man convicted of killing Martin Luther King born? (nsubj,VB,advcl)
            ||rel.equals("nmod:in")//which revolutionary was born in Mvezo?(revolutionary,born,Mvezo)(nsubjpass,VBN,nmod:in)
             ||rel.equals("nmod:at")                                                   //Which recipients of the Victoria Cross fought in the Battle of Arnhem? (recipients*fought*Battle NNS*VBD*nmod:in)
            ||rel.equals("nmod:of")
            ||rel.equals("nmod:from")
            ||rel.equals("nmod:under")//Under which king did the British prime minister that had a reputation as a playboy serve?(minister*serve*king  nsubj*VB*nmod:under)
            ||rel.equals("nmod:tmod")//Who succeeded the pope that reigned only 33 days? (pope*reigned*days  nsubj*VBD*nmod:tmod)
            ||rel.equals("nmod:on")//On which island did the national poet of Greece die?(poet*die*island  NN*VB*nmod:on
            ||rel.equals("nmod:by")//Which building owned by the Bank of America was featured in the TV series MegaStructures?
            ||rel.equals("nmod:against")//Who was vice president under the president who approved the use of atomic weapons against Japan during World War II?"
            ||rel.equals("nmod:during")//Who was vice president under the president who approved the use of atomic weapons against Japan during World War II?"
            ||rel.equals("advmod")//Where did the first human in space die?(human,die,Where)(nsubj,VB,advmod)
            ||rel.equals("xcomp")//Which actress starring in the TV series Friends owns the production company Coquette Productions? xcomp(owns,Productions)
            ||rel.equals("nmod:with")//Which street basketball player was diagnosed with Sarcoidosis?
            ||rel.equals("nmod:to")
            ||rel.equals("nmod:agent")) { //the man has been killed by the police.(man killed by police)        
           objectFillers[objectFillersNumber]=dep;
           objectFillersPositions[objectFillersNumber]=depIndex;
           if(rel.contains("nmod:"))
           objectFillersPrep[objectFillersNumber]=rel.split("nmod:")[1];
          
           if(rel.equals("dobj"))directObjectFillersPositions[objectFillersNumber]=1;
            objectFillersNumber++;
         }
          return 0;
  }
   public static int makeTriplesFromFillers(int i,String str,int verbNum,String sentence, LexicalizedParser lp,List<TypedDependency> tdl){
    int numberOfDirectObjects=0; String object="";
    for(int sf=0;sf<subjectFillersNumber;sf++)
        for(int of=0;of<objectFillersNumber;of++){
            if(objectFillersNumber>1){
                if(directObjectFillersPositions[of]==1){
                        
                        numberOfDirectObjects++;
                        triples[i][0]=subjectFillers[sf]; triplesPosition[i][0]=subjectFillersPositions[sf];triplesPosition[i][1]=subjectFillersPositions[sf];convertConnectedWords(i,0,1,triples[i][0],triplesPosition[i][0],sentence, tdl);extendToNounGroup(0,i,sentence,lp);
                        triples[i][1]=str; triplesPosition[i][2]=verbsPosition[verbNum];
                        triples[i][2]=objectFillers[of]; triplesPosition[i][3]=objectFillersPositions[of];triplesPosition[i][4]=objectFillersPositions[of];convertConnectedWords(i,2,1,triples[i][2],triplesPosition[i][2],sentence,tdl);extendToNounGroup(2,i,sentence,lp);
                        typeTriples[i]="Verbal relation: ";
                        if(POSword[1].equals("How") & POSword[2].equals("many") & triples[i][2].contains(POSword[3])){
                           triples[i][1]=triples[i][2];
                           triples[i][2]="N";
                           typeTriples[i]="How many relation: ";
                        }
                        i=CommaBetweenTwoNoun(i,triplesPosition[i][3],triples[i][2],sentence,lp);
                         // i++; triples[i][1]=str;triplesPosition[i][1]=triplesPosition[i-1][1];sw=1;
                        i=EndNum;
                        if(!triples[i][0].matches(triples[i][2]))
                        i++;
                }
                else { 
                    if(objectFillersNumber==2 & numberOfDirectObjects==1){
                    object=extendToNounGroupForAWordDirection(objectFillersPositions[of],objectFillers[of],sentence,lp,3);
                    }
                    IndirectObject=IndirectObject+" "+object;
                    }
            }else{
                        triples[i][0]=subjectFillers[sf]; triplesPosition[i][0]=subjectFillersPositions[sf]; triplesPosition[i][1]=subjectFillersPositions[sf];convertConnectedWords(i,0,1,triples[i][0],triplesPosition[i][0],sentence,tdl);extendToNounGroup(0,i,sentence,lp);
                        triples[i][1]=str; triplesPosition[i][2]=verbsPosition[verbNum];
                        triples[i][2]=objectFillers[of]; triplesPosition[i][3]=objectFillersPositions[of];triplesPosition[i][4]=objectFillersPositions[of];convertConnectedWords(i,2,1,triples[i][2],triplesPosition[i][2],sentence,tdl);extendToNounGroup(2,i,sentence,lp);
                       typeTriples[i]="Verbal relation: ";
                        if(POSword[1].equals("How") & POSword[2].equals("many") & triples[i][2].contains(POSword[3])){
                           triples[i][1]=triples[i][2].split("many ")[1];
                           triples[i][2]="N";
                             typeTriples[i]="How many relation: ";
                        }
                        i=CommaBetweenTwoNoun(i,triplesPosition[i][3],triples[i][2],sentence,lp);
                         // i++; triples[i][1]=str;triplesPosition[i][1]=triplesPosition[i-1][1];sw=1;
                        i=EndNum;
                        if(!triples[i][0].matches(triples[i][2]))
                        i++;
            
            
            }
                
    }   
    if(subjectFillersNumber==0 & objectFillersNumber>0){
          for(int of=0;of<objectFillersNumber;of++){
           IndirectObject=IndirectObject+" "+objectFillers[of];
          }
        triples[i][0]="";  
        triples[i][1]=str; triplesPosition[i][2]=verbsPosition[verbNum];
        triples[i][2]=IndirectObject;
        typeTriples[i]="Verbal relation: ";
     }     
     else if(numberOfDirectObjects==0 & objectFillersNumber>1)
     {
        triples[i][0]=subjectFillers[0];  triplesPosition[i][0]=subjectFillersPositions[0]; triplesPosition[i][1]=subjectFillersPositions[0];extendToNounGroup(0,i,sentence,lp);
        triples[i][1]=str; triplesPosition[i][2]=verbsPosition[verbNum];
         typeTriples[i]="Verbal relation: ";
        //        triples[i][2]=triples[sf][2]+" "+IndirectObject; triplesPosition[i][3]=triplesPosition[sf][3];triplesPosition[i][4]=triplesPosition[sf][4];
                i++;
     }
     else if (!IndirectObject.matches("")){
         int of=i;
         String predicate=triples[of-1][1];
         for(int sf=of-1;sf>0;sf--)
                 if( triples[sf][1]==predicate){
                     triples[i][0]=triples[sf][0]; triplesPosition[i][0]=triplesPosition[sf][0];triplesPosition[i][1]=triplesPosition[sf][1];
                     triples[i][1]=triples[sf][1]; triplesPosition[i][2]=triplesPosition[sf][1];
                     triples[i][2]=triples[sf][2]+" "+IndirectObject; triplesPosition[i][3]=triplesPosition[sf][3];triplesPosition[i][4]=triplesPosition[sf][4];
                     typeTriples[i]="Verbal relation: ";
                     if(!triples[i][0].matches(triples[i][2]))
                        i++;
                }
                 else break;
     }

     if(objectFillersNumber==0)
         for(int sf=0;sf<subjectFillersNumber;sf++){
         triples[i][0]=subjectFillers[sf]; triplesPosition[i][0]=subjectFillersPositions[sf];triplesPosition[i][1]=subjectFillersPositions[sf];extendToNounGroup(0,i,sentence,lp);
         triples[i][1]=str; triplesPosition[i][2]=verbsPosition[verbNum];
         typeTriples[i]="Verbal relation: ";
         i++;
         }


     return i;
     }
    //***********
   public static int makeTriplesFromFillersMinimal(int i,String str,int verbNum,String sentence, LexicalizedParser lp,List<TypedDependency> tdl ){
     int numberOfDirectObjects=0;


        for(int sf=0;sf<subjectFillersNumber;sf++)
         for(int of=0;of<objectFillersNumber;of++){
             if(objectFillersNumber>1){
//                 if(directObjectFillersPositions[of]==1){
//                         numberOfDirectObjects++;
                         Minimaltriples[i][0]=subjectFillers[sf]; MinimaltriplesPosition[i][0]=subjectFillersPositions[sf];MinimaltriplesPosition[i][1]=subjectFillersPositions[sf];convertConnectedWords(i,0,0,Minimaltriples[i][0],MinimaltriplesPosition[i][0],sentence,tdl);extendToNounGroupMinimal(0,i,sentence,lp);
                         Minimaltriples[i][1]=str; MinimaltriplesPosition[i][2]=verbsPosition[verbNum];
                         Minimaltriples[i][2]=objectFillers[of]; MinimaltriplesPosition[i][3]=objectFillersPositions[of];MinimaltriplesPosition[i][4]=objectFillersPositions[of];convertConnectedWords(i,2,0,Minimaltriples[i][2],MinimaltriplesPosition[i][2],sentence,tdl);
                         MinimaltriplesVerbPrep[i]=objectFillersPrep[of];
                         if(!POStagword[objectFillersPositions[of]].matches("WRB"))
                           extendToNounGroupMinimal(2,i,sentence,lp);
                         MinimaltypeTriples[i]="Verbal relation: "; 
                        if(POSword[1].equals("How") & POSword[2].equals("many") & Minimaltriples[i][2].contains(POSword[3])){
                           Minimaltriples[i][1]=Minimaltriples[i][2].split("many ")[1];
                           Minimaltriples[i][2]="N";
                        }
                        i=CommaBetweenTwoNounMinimal(i,MinimaltriplesPosition[i][3],Minimaltriples[i][2],sentence,lp);
                         // i++; triples[i][1]=str;triplesPosition[i][1]=triplesPosition[i-1][1];sw=1;
                        i=MinimalEndNum;
                        if(!Minimaltriples[i][0].matches(Minimaltriples[i][2]))
                           i++;
//                }
//                else { 
//                    if(objectFillersNumber==2 & numberOfDirectObjects==1)
//                    objectFillers[of]=extendToNounGroupForAWordDirectionMinimal(objectFillersPositions[of],objectFillers[of],sentence,lp,3);
                    
//                    IndirectObject=IndirectObject+" "+objectFillers[of];
////                    }
            }else{
                        Minimaltriples[i][0]=subjectFillers[sf]; MinimaltriplesPosition[i][0]=subjectFillersPositions[sf]; MinimaltriplesPosition[i][1]=subjectFillersPositions[sf];convertConnectedWords(i,0,0,Minimaltriples[i][0],MinimaltriplesPosition[i][0],sentence,tdl);extendToNounGroupMinimal(0,i,sentence,lp);
                        Minimaltriples[i][1]=str; MinimaltriplesPosition[i][2]=verbsPosition[verbNum];
                        Minimaltriples[i][2]=objectFillers[of];MinimaltriplesPosition[i][3]=objectFillersPositions[of];MinimaltriplesPosition[i][4]=objectFillersPositions[of];if(!POStagword[objectFillersPositions[of]].matches("WRB"))convertConnectedWords(i,2,0,Minimaltriples[i][2],MinimaltriplesPosition[i][2],sentence,tdl);extendToNounGroupMinimal(2,i,sentence,lp);
                         MinimaltriplesVerbPrep[i]=objectFillersPrep[of];
                        MinimaltypeTriples[i]="Verbal relation: ";
                        if(POSword[1].equals("How") & POSword[2].equals("many") & Minimaltriples[i][2].contains(POSword[3])){
                           Minimaltriples[i][1]=Minimaltriples[i][2].split("many ")[1];
                           Minimaltriples[i][2]="N";
                           MinimaltypeTriples[i]="How many relation: ";
                        }
                        i=CommaBetweenTwoNounMinimal(i,MinimaltriplesPosition[i][3],Minimaltriples[i][2],sentence,lp);
                         // i++; triples[i][1]=str;triplesPosition[i][1]=triplesPosition[i-1][1];sw=1;
                        i=MinimalEndNum;
                        if(!Minimaltriples[i][0].matches(Minimaltriples[i][2])) 
                        i++;
            
            
            }
                
    }   
    if(subjectFillersNumber==0 & objectFillersNumber>0){
         for(int of=0;of<objectFillersNumber;of++){
         //  IndirectObject=IndirectObject+" "+objectFillers[of];
         // }
       Minimaltriples[i][0]="";  
       Minimaltriples[i][1]=str; MinimaltriplesPosition[i][2]=verbsPosition[verbNum];
       Minimaltriples[i][2]=objectFillers[of];
        MinimaltriplesVerbPrep[i]=objectFillersPrep[of];
       MinimaltypeTriples[i]="verbal relation: ";
        
        i++;
    }  
    }     
 //   else if(numberOfDirectObjects==0 & objectFillersNumber>1)
//    {
//       Minimaltriples[i][0]=subjectFillers[0];  MinimaltriplesPosition[i][0]=subjectFillersPositions[0]; MinimaltriplesPosition[i][1]=subjectFillersPositions[0];extendToNounGroupMinimal(0,i,sentence,lp);
//       Minimaltriples[i][1]=str; MinimaltriplesPosition[i][2]=verbsPosition[verbNum];
//       MinimaltypeTriples[i]="verbal relation: ";       
//        triples[i][2]=triples[sf][2]+" "+IndirectObject; triplesPosition[i][3]=triplesPosition[sf][3];triplesPosition[i][4]=triplesPosition[sf][4];
//               i++;
//    }
/*    else if (!IndirectObject.matches("")){
        int of=i;
        String predicate=Minimaltriples[of-1][1];
        for(int sf=of-1;sf>0;sf--)
                if( Minimaltriples[sf][1]==predicate){
                    Minimaltriples[i][0]=Minimaltriples[sf][0]; MinimaltriplesPosition[i][0]=MinimaltriplesPosition[sf][0];MinimaltriplesPosition[i][1]=MinimaltriplesPosition[sf][1];
                    Minimaltriples[i][1]=Minimaltriples[sf][1]; MinimaltriplesPosition[i][2]=MinimaltriplesPosition[sf][1];
                    Minimaltriples[i][2]=Minimaltriples[sf][2]+" "+IndirectObject; MinimaltriplesPosition[i][3]=MinimaltriplesPosition[sf][3];MinimaltriplesPosition[i][4]=MinimaltriplesPosition[sf][4];
                     MinimaltypeTriples[i]="verbal relation: ";
                    i++;
               }
                else break;
    }*/
        
    if(objectFillersNumber==0)
        for(int sf=0;sf<subjectFillersNumber;sf++){
        Minimaltriples[i][0]=subjectFillers[sf]; MinimaltriplesPosition[i][0]=subjectFillersPositions[sf];MinimaltriplesPosition[i][1]=subjectFillersPositions[sf];convertConnectedWords(i,0,0,Minimaltriples[i][0],MinimaltriplesPosition[i][0],sentence,tdl);extendToNounGroupMinimal(0,i,sentence,lp);
        Minimaltriples[i][1]=str; MinimaltriplesPosition[i][2]=verbsPosition[verbNum];
         MinimaltypeTriples[i]="verbal relation: ";
        i++;
        }
        
            
    return i;
    }
  public static int FindRerenceOfThat(int index,List<TypedDependency> tdl){
    int index1=0,k,dep1Index,gov1Index;
    String[] str1; String dep1,rel1,gov1;
                        for (TypedDependency t2 : tdl){
                            k=0;
                            str1=tdl.get(index1).dep().toString().split("/");
                            dep1=str1[0];
                            dep1Index=tdl.get(index1).dep().index();
                            str1=tdl.get(index1).reln().toString().split("/");
                            rel1=str1[0];
                            str1=tdl.get(index1).gov().toString().split("/");
                            gov1Index=tdl.get(index1).gov().index();
                            gov1=str1[0]; 
                            if((dep1.matches("that")||dep1.matches("where")||dep1.matches("which")||dep1.matches("who")
                                    ||dep1.matches("whom")||dep1.matches("That")||dep1.matches("Where")||dep1.matches("Which")||dep1.matches("Who")
                                    ||dep1.matches("Whom")
                                    ) & 
                                    (dep1Index==index & rel1.matches("ref")))
                                  return gov1Index;
                            if((gov1.matches("that")||gov1.matches("where")||gov1.matches("which")||gov1.matches("who")
                                    ||gov1.matches("whom")||gov1.matches("That")||gov1.matches("Where")||gov1.matches("Which")||gov1.matches("Who")
                                    ||gov1.matches("Whom")
                                    ) & 
                                    (gov1Index==index & rel1.matches("nmod:of")))
                                  return dep1Index;
                            
                            index1++;      
                            }//end for
      
      return 0;
  }
   public static int AddCopularRelationTriple(String str,int j,List<TypedDependency> tdl,LexicalizedParser lp ,int i ,String sentence){
        int counter;String subj="";
        triples[i][1]=verbs[j];triplesRoles[i][1]=verbsRoles[i];//typeTriples[i]=2; 
        triplesPosition[i][2]=verbsPosition[j];
        counter= verbsPosition[j]-1;
        //Find out the subject
        if(POSword[verbsPosition[j]-1].matches("that")||POSword[verbsPosition[j]-1].matches("which")||
           POSword[verbsPosition[j]-1].matches("where")||POSword[verbsPosition[j]-1].matches("whom")||
           POSword[verbsPosition[j]-1].matches("who")){ 
            int RefrenceIndex=FindRerenceOfThat(counter,tdl);
            if(RefrenceIndex!=0){
                                  subj=extendToNounGroupForAWordDirection(RefrenceIndex,POSword[RefrenceIndex],sentence,lp,1);
                                  triples[i][0]=subj;
                                  triplesPosition[i][0]=LeftLimitOfWord;
                                  triplesPosition[i][1]=RightLimitOfWord;
            }
            else
            extendToLeftCopular(0,i,counter,sentence,lp);
            }
            else
            extendToLeftCopular(0,i,counter,sentence,lp);  
        
       
        
        //Find out the object
        triplesPosition[i][3]=verbsPosition[j]+1;
        triplesPosition[i][4]=verbsPosition[j]+1;
        typeTriples[i]="Copular verb relation: ";
                        extendToRightCopular(2,i,counter,sentence,lp);
                        int sw=1;
                        //if(NPprepNp())
                        while(sw==1) 
                          sw=CommaBetweenTwoNoun(i,triplesPosition[i][4],triples[i][2],sentence,lp);
                        i=EndNum;
        return i;
   }
   public static int AddCopularRelationTripleMinimal(String str,int j,List<TypedDependency> tdl,LexicalizedParser lp ,int i ,String sentence){
       int counter;String subj;int sw1=0;int sw=0;
       int index1=0,k,dep1Index=0,gov1Index;
       String[] str1; String dep1="";String rel1,gov1;
       Minimaltriples[i][1]=verbs[j];triplesRoles[i][1]=verbsRoles[i];//typeTriples[i]=2; 
       MinimaltriplesPosition[i][2]=verbsPosition[j];
       counter= verbsPosition[j]-1;
       if(POSword[verbsPosition[j]-1].matches("that")||POSword[verbsPosition[j]-1].matches("which")||
          POSword[verbsPosition[j]-1].matches("where")||POSword[verbsPosition[j]-1].matches("whom")||
          POSword[verbsPosition[j]-1].matches("who")){
           int RefrenceIndex=FindRerenceOfThat(counter,tdl);
           if(RefrenceIndex!=0){
              subj=extendToNounGroupForAWordDirection(RefrenceIndex,POSword[RefrenceIndex],sentence,lp,1);
              Minimaltriples[i][0]=subj;
              MinimaltriplesPosition[i][0]=LeftLimitOfWord;
              MinimaltriplesPosition[i][1]=RightLimitOfWord;
            }
            else
            extendToLeftCopularMinimal(0,i,counter,sentence,lp);
        }
        else
        extendToLeftCopularMinimal(0,i,counter,sentence,lp);  
        if(MinimaltriplesPosition[i][0]-1>3){
        if(POStagword[MinimaltriplesPosition[i][0]-1].matches("IN")){
        counter= MinimaltriplesPosition[i][2]-2;    
           extendToLeftCopularMinimal(0,i,counter,sentence,lp);
        
        }
        else if(POStagword[MinimaltriplesPosition[i][0]-2].matches("IN")){
           counter= MinimaltriplesPosition[i][2]-3;    
           extendToLeftCopularMinimal(0,i,counter,sentence,lp);
        
        }        
        }   
        
       
       
       counter= verbsPosition[j]+1;
                        MinimaltriplesPosition[i][3]=verbsPosition[j]+1;
                        MinimaltriplesPosition[i][4]=verbsPosition[j]+1;
                        MinimaltypeTriples[i]="Copular verb relation: ";
                        extendToRightCopularMinimal(2,i,counter,sentence,lp);
                        sw=1;
                        if(NPprepNp(MinimaltriplesPosition[i][4])!=0){ 
                          i++;
                         
                          Minimaltriples[i][1]=Minimaltriples[i-1][2];MinimaltriplesPosition[i][2]=MinimaltriplesPosition[i-1][3];
                          MinimaltypeTriples[i]="verbal relation(NP-VPc-prep-NP): ";
                            
                            for (TypedDependency t2 : tdl){
                            k=0;
                            str1=tdl.get(index1).dep().toString().split("/");
                            dep1=str1[0];
                            dep1Index=tdl.get(index1).dep().index();
                            str1=tdl.get(index1).reln().toString().split("/");
                            rel1=str1[0];
                            str1=tdl.get(index1).gov().toString().split("/");
                            gov1Index=tdl.get(index1).gov().index();
                            gov1=str1[0]; 
                            if((gov1.matches(Minimaltriples[i][1]) & gov1Index==MinimaltriplesPosition[i][2] & (rel1.matches("nsubj")|| rel1.matches("nsubjpass"))                   
                                    )){
                            sw1=1; 
                            break;
                            }
                               
                            index1++;      
                            }//end for
                            if(sw1==1){
                                Minimaltriples[i][0]=dep1;
                                MinimaltriplesPosition[i][0]=dep1Index;
                                MinimaltriplesPosition[i][1]=dep1Index;
                            }else{
                          Minimaltriples[i][0]=Minimaltriples[i-1][0];MinimaltriplesPosition[i][0]=MinimaltriplesPosition[i-1][0];MinimaltriplesPosition[i][1]=MinimaltriplesPosition[i-1][1];
                            }
                          extendToRightCopularMinimal(2,i,NPprepNp(MinimaltriplesPosition[i-1][4]),sentence,lp); 
                          MinimalEndNum=i; 
                        
                        }
                        while(sw==1) 
                          sw=CommaBetweenTwoNounMinimal(i,MinimaltriplesPosition[i][4],Minimaltriples[i][2],sentence,lp);
                        i=MinimalEndNum;
        return i;
   }
   public static void TriplesReview(String line,List<TypedDependency> tdl ){
                  // SubjObjNull();
                  FullNullObject(line);
                  FullNullObjectMinimal(line);
                   //CommaBetweenTwoNoun(line);//developed a filing system that enables judges, lawyers 
                   //VerbT0Verb(line,tdl);
    }
   //relatin extraction functions
    public static void IndirectVerbal(LexicalizedParser lp,List<TypedDependency> tdl,String sentence ) throws IOException{
           String[] str1;int dep1Index,gov1Index,gov2Index,dep2Index,k=0;
       String rel1,dep1,gov1,rel2,gov2,dep2,Subject,predicate;   
       dep2="";
       dep2Index=0;
       int index1=0;
       int step=1;
       int predicateIndex=0;
       AbstractSequenceClassifier classifier = null;
                 for (TypedDependency t1 : tdl){
                     str1=tdl.get(index1).dep().toString().split("/");
                     dep1Index=tdl.get(index1).dep().index();
                     dep1=str1[0];
                     
                     str1=tdl.get(index1).reln().toString().split("/");
                     rel1=str1[0];
                     str1=tdl.get(index1).gov().toString().split("/");
                     gov1=str1[0];
                     gov1Index=tdl.get(index1).gov().index();
                     predicateIndex=gov1Index;
                     predicate=gov1;
                     if((!relIsIndirectObject(rel1).matches("") || rel1.matches("dobj"))& govIsMatchedWhithVerbs(gov1)==1)
                     {
                     int sw=1;
                     while(sw==1){
                        int index2=0;
                        for (TypedDependency t2 : tdl){
                            k=0;
                            str1=tdl.get(index2).dep().toString().split("/");
                            dep2=str1[0];
                            dep2Index=tdl.get(index2).dep().index();
                            str1=tdl.get(index2).reln().toString().split("/");
                            rel2=str1[0];
                            str1=tdl.get(index2).gov().toString().split("/");
                            gov2Index=tdl.get(index2).gov().index();
                            gov2=str1[0]; 
                            if(gov2.matches(dep1) & (gov2Index==dep1Index)& (rel2.matches("nmod:in")||rel2.matches("nmod:at")||rel2.matches("nmod:on"))){
                                  k=1;
                                  MakeIndirectVerbalRelation(predicate,gov1Index,dep2,dep2Index,relIsIndirectObject(rel2),sentence,lp);
                                  break;
                            }//end if
                            index2++;      
                            }//end for
                        if(k==1)
                        {dep1=dep2;
                         dep1Index=dep2Index;
                        }
                        else sw=0;    
                     }
                     }//end if(IsProbable......)
                     index1++;
                }
    }
   public static void MakeIndirectVerbalRelation(String predicate,int predicateIndex,String dep2,int dep2Index,String prep,String sentence,LexicalizedParser lp){
    int j=0;String subject="";
    int subjectStart=0,subjectEnd=0;
    
    for(j=0;j<MinimalEndNum+1;j++){
                  if(Minimaltriples[j][1].matches(predicate) & Minimaltriples[j][2].matches(dep2))
                    break;
                  else if(Minimaltriples[j][1].matches(predicate) & !Minimaltriples[j][2].matches(dep2)){
                    subject=Minimaltriples[j][0];
                    subjectStart=MinimaltriplesPosition[j][0];
                    subjectEnd=MinimaltriplesPosition[j][1];
                  }
    }
    if(j==MinimalEndNum+1){
     MinimalEndNum++;   
        Minimaltriples[j][0]=subject;MinimaltriplesPosition[j][0]=subjectStart;MinimaltriplesPosition[j][1]=subjectEnd;
        Minimaltriples[j][1]=predicate;MinimaltriplesPosition[j][2]=predicateIndex;
        Minimaltriples[j][2]=dep2;MinimaltriplesPosition[j][3]=MinimaltriplesPosition[j][4]=dep2Index;extendToNounGroupMinimal(2,j,sentence,lp);
        MinimaltriplesVerbPrep[j]=prep;
        MinimaltypeTriples[j]="Indirect Verbal relation: ";
    }
        
   }
   public static void GenitiveRelations(LexicalizedParser lp,List<TypedDependency> tdl,String sentence ) throws IOException{
        
       String[] str1;int dep1Index,gov2Index,dep2Index,k=0;
       String rel1,dep1,gov1,rel2,gov2,dep2;   
       dep2="";
       rel2="";
       dep2Index=0;
       int index1=0;
       int step=1;
       AbstractSequenceClassifier classifier = null;
                 for (TypedDependency t1 : tdl){
                   //may be verb is copular verb such as did.....serve.verb did should be deleated
                     str1=tdl.get(index1).dep().toString().split("/");
                     dep1Index=tdl.get(index1).dep().index();
                     dep1=str1[0];
                     
                     str1=tdl.get(index1).reln().toString().split("/");
                     rel1=str1[0];
                     str1=tdl.get(index1).gov().toString().split("/");
                     gov1=str1[0];
                     
                     if(IsProbableGenitiveType(gov1,rel1,dep1)==1 )
                     {
                     int sw=1;
                     while(sw==1){
                        int index2=0;
                        for (TypedDependency t2 : tdl){
                            k=0;
                            str1=tdl.get(index2).dep().toString().split("/");
                            dep2=str1[0];
                            dep2Index=tdl.get(index2).dep().index();
                            str1=tdl.get(index2).reln().toString().split("/");
                            rel2=str1[0];
                            str1=tdl.get(index2).gov().toString().split("/");
                            gov2Index=tdl.get(index2).gov().index();
                            gov2=str1[0]; 
                            if(
                                    (
                                    (gov2.matches(dep1) & (gov2Index==dep1Index))& (!relIsIndirectObject(rel2).matches("")||rel2.matches("dep"))& IsPossessiveAdjective(dep2)==0 )||
                                    (rel1.matches("cop")&!relIsIndirectObject(rel2).matches("") &gov2.matches(gov1))
                                    
                                    ){
                                  k=1;
                                  /*if(step==1){
                                  String serializedClassifier = "F:\\Question-answer\\implementation\\myprog\\stanfordLib\\classifiers\\english.all.3class.distsim.crf.ser.gz";
                                  classifier = CRFClassifier.getClassifierNoExceptions(serializedClassifier);
                                  model = FileManager.get().loadModel("F:\\Question-answer\\implementation\\myprog\\stanfordLib\\SUMO.owl-master\\SUMO.owl");
                                  }*/
                               //   if(phraseTagmeLinksRecognizer[dep1Index]!=1){
                                      step++;
                                      MakeGenitiveRelation(gov2Index,dep2Index,rel2,sentence,lp);
                                      break;
                                //  }
                            }//end if
                            
                            index2++;  
                            
                            }//end for
                        if(k==1)
                        {dep1=dep2;
                        rel1=rel2;
                         dep1Index=dep2Index;
                        }
                        else sw=0;    
                     }
                     }//end if(IsProbable......)
                     index1++;
                }
   }
   public static void MakeGenitiveRelation(int gov2Index,int dep2Index,String rel2,String str,LexicalizedParser lp) throws IOException{
    
    MinimalEndNum++;
    int i=MinimalEndNum;
    
    
      // String Class=Main.main(POSword[gov2Index]);
              //if(!Class.matches("Null")){
             //missed predicates
              if(rel2.matches("nmod:in")||rel2.matches("nmod:at")||rel2.matches("nmod:on")||rel2.matches("nmod:by")){
                  Minimaltriples[i][0]=POSword[gov2Index]; MinimaltriplesPosition[i][0]=gov2Index;MinimaltriplesPosition[i][1]=gov2Index;extendToNounGroupMinimal(0,i,str,lp);
                  Minimaltriples[i][1]="has a relation with"; MinimaltriplesPosition[i][2]=0;
                  Minimaltriples[i][2]=POSword[dep2Index]; MinimaltriplesPosition[i][3]=dep2Index;MinimaltriplesPosition[i][4]=dep2Index;extendToNounGroupMinimal(2,i,str,lp);
                  MinimaltypeTriples[i]="Genitive relation(missed predicate): ";
              }
              //missed subjects
              else if(rel2.matches("nmod:of")||rel2.matches("nmod:poss")){
                  Minimaltriples[i][0]="?subject "; MinimaltriplesPosition[i][0]= MinimaltriplesPosition[i][1]=0;
                  Minimaltriples[i][1]=POSword[gov2Index]; MinimaltriplesPosition[i][2]=gov2Index;extendToNounGroupMinimal(1,i,str,lp);
                  Minimaltriples[i][2]=POSword[dep2Index]; MinimaltriplesPosition[i][3]=dep2Index;MinimaltriplesPosition[i][4]=dep2Index;extendToNounGroupMinimal(2,i,str,lp);
                  MinimaltypeTriples[i]="Genitive relation(missed subject): ";
              }
              else if(rel2.matches("dep")){
                  Minimaltriples[i][0]="?subject "; MinimaltriplesPosition[i][0]= MinimaltriplesPosition[i][1]=0;
                  Minimaltriples[i][2]=POSword[gov2Index];MinimaltriplesPosition[i][3]=gov2Index;MinimaltriplesPosition[i][4]=gov2Index;extendToNounGroupMinimal(2,i,str,lp);
                  Minimaltriples[i][1]=POSword[dep2Index]; MinimaltriplesPosition[i][2]=dep2Index;extendToNounGroupMinimal(1,i,str,lp);
                  MinimaltypeTriples[i]="Genitive relation(missed subject): ";
              }
        MinimalEndNum=i; 
   }
    public static int depIsMatchedCopVerbs(String dep1){
       if(dep1.matches("Are")||dep1.matches("are")||dep1.matches("is")||dep1.matches("Is")||dep1.matches("Was")||dep1.matches("was")
               ||dep1.matches("Were")||dep1.matches("were"))
       return 1;
       return 0;
    }
   public static int IsProbableGenitiveType(String gov,String rel,String dep1){
          if(
                           //Genitive type1                             
                        (
                            // (gov.matches("Who")||gov.matches("Where")||gov.matches("What")||gov.matches("Which"))
                             //&
                             (rel.matches("nsubj")||rel.matches("nsubjpass"))
                        )
                        ||
                           //Genitive type2,type3,type4
                        (
                             (govIsMatchedWhithVerbs(gov)==1)
                             &
                             (rel.matches("nsubj") ||rel.matches("nsubjpass")||rel.matches("dobj")||!relIsIndirectObject(rel).matches(""))
                        )
                         ||
                           //Genitive type2,type3,type4
                        (
                             (depIsMatchedCopVerbs(dep1)==1)
                             &
                             (rel.matches("cop"))
                        )
                        
                        
                          
           ){
              return 1;
            }//end if
return 0;
   }
   public static int govIsMatchedWhithVerbs(String rel){
       for(int j=0;j<CounterVerbs;j++){
         if(rel.matches(verbs[j]))
             return 1;
        }
       return 0;
   }
   public static int IsPossessiveAdjective(String dep2){//Possessive adjectives - my, your, his, her, its, our, your, their - modify the noun following it in order to show possession.
   if(dep2.matches("my")||dep2.matches("your")||dep2.matches("his")||dep2.matches("his")||dep2.matches("her")||dep2.matches("its")||dep2.matches("our")||dep2.matches("your")||dep2.matches("their"))
    return 1;
   return 0;
   }
   public static String relIsIndirectObject(String rel){
     String prep="";
      if(    rel.equals("nmod:in")//which revolutionary was born in Mvezo?(revolutionary,born,Mvezo)(nsubjpass,VBN,nmod:in)
            ||rel.equals("nmod:at")                                                   //Which recipients of the Victoria Cross fought in the Battle of Arnhem? (recipients*fought*Battle NNS*VBD*nmod:in)
            ||rel.equals("nmod:of")
            ||rel.equals("nmod:under")//Under which king did the British prime minister that had a reputation as a playboy serve?(minister*serve*king  nsubj*VB*nmod:under)
            ||rel.equals("nmod:tmod")//Who succeeded the pope that reigned only 33 days? (pope*reigned*days  nsubj*VBD*nmod:tmod)
            ||rel.equals("nmod:on")//On which island did the national poet of Greece die?(poet*die*island  NN*VB*nmod:on
            ||rel.equals("nmod:by")//Which building owned by the Bank of America was featured in the TV series MegaStructures?
            ||rel.equals("nmod:against")//Who was vice president under the president who approved the use of atomic weapons against Japan during World War II?"
            ||rel.equals("nmod:during")//Who was vice president under the president who approved the use of atomic weapons against Japan during World War II?"
            ||rel.equals("advmod")//Where did the first human in space die?(human,die,Where)(nsubj,VB,advmod)
            ||rel.equals("xcomp")//Which actress starring in the TV series Friends owns the production company Coquette Productions? xcomp(owns,Productions)
            ||rel.equals("nmod:with")//Which street basketball player was diagnosed with Sarcoidosis?
            ||rel.equals("nmod:to")
            ||rel.equals("nmod:from")
            ||rel.equals("nmod:agent") //the man has been killed by the police.(man killed by police)        
            ||rel.equals("nmod:poss")
             ){
                if(rel.contains("nmod:"))
                prep= rel.split("nmod:")[1];
                return prep;
      
              }
      return prep;
  }
  public static String ComparativeAdjectiveIsQuantityAdjective(String adjective){
    if(adjective.matches("sheaper")) return "cheap cheaper cheapest price lower";
    if(adjective.matches("colder")) return "old colder coldest teppreature lower";
    if(adjective.matches("deeper")) return "deep deeper deepest depth higher";
    if(adjective.matches("fatter")) return "fat fatter fattest weight higher";
    if(adjective.matches("higher")) return "high higher highest height higher";
    if(adjective.matches("hotter")) return "hot hotter hottest tepreature higher";
    if(adjective.matches("longer")) return "long longer longest length higher";
    if(adjective.matches("elder")) return "old elder eldest age higher";
    if(adjective.matches("shorter")) return "short shorter shortest length lower";
    if(adjective.matches("taller")) return "tall taller tallest length higher";
    if(adjective.matches("warmer")) return "warm warmer warmest tempreature higher";
    if(adjective.matches("younger")) return "young younger youngest age lower";
 return "";
  }
  public static String SuperlativeAdjectiveIsQuantityAdjective(String adjective){
    if(adjective.matches("cheapest")) return "cheap cheaper cheapest price lower";
    if(adjective.matches("coldest")) return "old colder coldest teppreature lower";
    if(adjective.matches("deepest")) return "deep deeper deepest depth higher";
    if(adjective.matches("fattest")) return "fat fatter fattest weight higher";
    if(adjective.matches("highest")) return "high higher highest height higher";
    if(adjective.matches("hottest")) return "hot hotter hottest tepreature higher";
    if(adjective.matches("longest")) return "long longer longest length higher";
    if(adjective.matches("eldest")) return "old elder eldest age higher";
    if(adjective.matches("shortest")) return "short shorter shortest length lower";
    if(adjective.matches("tallest")) return "tall taller tallest length higher";
    if(adjective.matches("warmest")) return "warm warmer warmest tempreature higher";
    if(adjective.matches("youngest")) return "young younger youngest age lower";
 return "";
  }
   public static void SuperlativeRelations (LexicalizedParser lp,List<TypedDependency> tdl,String sentence ){
        String predicate="";
        String operator="";
       for(int i=1;i<wordsNumber;i++){
          if(POStagword[i].matches("JJS")||POStagword[i].matches("RBS")){
                 String Adjective=SuperlativeAdjectiveIsQuantityAdjective(POSword[i]);
                   if(!Adjective.matches("")){//is quantity adjective
                        String str[]=Adjective.split(" ");
                        predicate=str[3];
                        operator=str[4];
                        MinimalEndNum++;
                        Minimaltriples[MinimalEndNum][0]=POSword[i+1];MinimaltriplesPosition[MinimalEndNum][0]=i+1;MinimaltriplesPosition[MinimalEndNum][1]=i+1;
                        Minimaltriples[MinimalEndNum][1]=predicate;//triples[i][1]+' '+POSword[verbsPosition[j]+1];
                        MinimaltypeTriples[MinimalEndNum]="quantity Superlative relation:  ";
                        Minimaltriples[MinimalEndNum][2]="n1";
                        MinimaltripleCondition[MinimalEndNum][0]="order by";
                        if(operator.matches("higher"))
                        MinimaltripleCondition[MinimalEndNum][1]="ASC";
                        else
                        MinimaltripleCondition[MinimalEndNum][1]="DESC";    
                        MinimaltripleCondition[MinimalEndNum][2]="n1";
                   }
          }
       }
   
    }
  public static void ComparativeRelations (LexicalizedParser lp,List<TypedDependency> tdl,String sentence ){
    int j;
    String predicate="";
    String operator="";
    for(j=0;j<CounterVerbs;j++){
      if(POStagword[verbsPosition[j]+1].matches("RBR")||POStagword[verbsPosition[j]+1].matches("JJR")){
        String Adjective=ComparativeAdjectiveIsQuantityAdjective(POSword[verbsPosition[j]+1]);
        if(!Adjective.matches("")){//is quantity adjective
           String str[]=Adjective.split(" ");
           predicate=str[3];
           operator=str[4];
            for(int i=0;i<=MinimalEndNum;i++){
            if(Minimaltriples[i][1].matches(verbs[j])){
                MinimalEndNum++;
                Minimaltriples[MinimalEndNum][0]=Minimaltriples[i][0];MinimaltriplesPosition[MinimalEndNum][0]=MinimaltriplesPosition[i][0];MinimaltriplesPosition[MinimalEndNum][1]=MinimaltriplesPosition[i][1];
                Minimaltriples[MinimalEndNum][1]=predicate;//triples[i][1]+' '+POSword[verbsPosition[j]+1];
                MinimaltypeTriples[MinimalEndNum]="quantity Comparative relation:  ";
                MinimaltriplesState[i]=0;
                if(POStagword[verbsPosition[j]+3].matches("CD")){
                    Minimaltriples[MinimalEndNum][2]="n1";//POSword[verbsPosition[j]+3];
                    //triplesPosition[EndNum][3]=verbsPosition[j]+3;
                   // triplesPosition[EndNum][4]=verbsPosition[j]+3;
                   //extendToNounGroup(2,EndNum,sentence,lp);
                  MinimaltripleCondition[MinimalEndNum][0]="n1";
                  MinimaltripleCondition[MinimalEndNum][1]=operator;
                  MinimaltripleCondition[MinimalEndNum][2]=POSword[verbsPosition[j]+3];
                }else{//is not a number
                     Minimaltriples[MinimalEndNum][2]="n1";
                     MinimalEndNum++;
                     Minimaltriples[MinimalEndNum][0]=POSword[verbsPosition[j]+3];MinimaltriplesPosition[MinimalEndNum][0]=verbsPosition[j]+3;MinimaltriplesPosition[MinimalEndNum][1]=verbsPosition[j]+3; extendToNounGroupMinimal(0,MinimalEndNum,sentence,lp);
                     Minimaltriples[MinimalEndNum][1]=predicate;//triples[i][1]+' '+POSword[verbsPosition[j]+1];
                     Minimaltriples[MinimalEndNum][2]="n2";
                     MinimaltypeTriples[MinimalEndNum]="quantity Comparative relation:  ";
                     MinimaltripleCondition[MinimalEndNum][0]="n1";
                     MinimaltripleCondition[MinimalEndNum][1]=operator;
                     MinimaltripleCondition[MinimalEndNum][2]="n2";
                }
          
          
                  
        }
        }
        
        
        
       //--------------
           
           
           
           
        }
        else{//is quality Adjective
            
            
      
        
        for(int i=0;i<=EndNum;i++){
        if(triples[i][1].matches(verbs[j])){
          EndNum++;
          triples[EndNum][0]=triples[i][0];triplesPosition[EndNum][0]=triplesPosition[i][0];triplesPosition[EndNum][1]=triplesPosition[i][1];
          triples[EndNum][1]=triples[i][1]+' '+POSword[verbsPosition[j]+1];
          typeTriples[EndNum]="Comparative relation:  ";
          if(POStagword[verbsPosition[j]+3].matches("CD")){
              triples[EndNum][2]=POSword[verbsPosition[j]+3];
              triplesPosition[EndNum][3]=verbsPosition[j]+3;
               triplesPosition[EndNum][4]=verbsPosition[j]+3;
              extendToNounGroup(2,EndNum,sentence,lp);
          }
          
          else
          {    triples[EndNum][2]=POSword[verbsPosition[j]+3];
               triplesPosition[EndNum][3]=verbsPosition[j]+3;
               triplesPosition[EndNum][4]=verbsPosition[j]+3;
               extendToNounGroup(2,EndNum,sentence,lp);
          }
          
                  
        }
        }
        
        
        for(int i=0;i<=MinimalEndNum;i++){
        if(Minimaltriples[i][1].matches(verbs[j])){
          MinimalEndNum++;
          Minimaltriples[MinimalEndNum][0]=Minimaltriples[i][0];MinimaltriplesPosition[MinimalEndNum][0]=MinimaltriplesPosition[i][0];MinimaltriplesPosition[MinimalEndNum][1]=MinimaltriplesPosition[i][1];
          Minimaltriples[MinimalEndNum][1]=Minimaltriples[i][1]+' '+POSword[verbsPosition[j]+1];
          MinimaltypeTriples[MinimalEndNum]="Comparative relation:  ";
          if(POStagword[verbsPosition[j]+3].matches("CD")){
              Minimaltriples[MinimalEndNum][2]=POSword[verbsPosition[j]+3];
              MinimaltriplesPosition[MinimalEndNum][3]=verbsPosition[j]+3;
              MinimaltriplesPosition[MinimalEndNum][4]=verbsPosition[j]+3;
              extendToNounGroupMinimal(2,MinimalEndNum,sentence,lp);
          }
          else
          {    Minimaltriples[MinimalEndNum][2]=POSword[verbsPosition[j]+3];
               MinimaltriplesPosition[MinimalEndNum][3]=verbsPosition[j]+3;
               MinimaltriplesPosition[MinimalEndNum][4]=verbsPosition[j]+3;
               extendToNounGroupMinimal(2,MinimalEndNum,sentence,lp);
          }
          
                  
        }
        }
       //--------------
        
      
      }//ens else is quantity Adjective
    }//counter verbs
  }
  }  //
  public static void VerbBasedRelations(LexicalizedParser lp,List<TypedDependency> tdl,String sentence ){
      //***********************************searches for subject- verb- object triples*******************************************
      int i,Minimali,j,counter;
      i=0;Minimali=0;
      for(j=0;j<CounterVerbs;j++){
         int index=0;
         int k=BeCopularMainVerb(verbs[j],j,tdl);
         if(BeCopularMainVerb(verbs[j],j,tdl)==1){ 
             i=AddCopularRelationTriple(verbs[j],j,tdl,lp,i,sentence);i++;
             Minimali=AddCopularRelationTripleMinimal(verbs[j],j,tdl,lp,Minimali,sentence);Minimali++;
         }
         else if (NotCopularVerb(verbs[j],tdl)==1 & VerbToVerb(j)==0){
            i=AddNotCopularVerbRelationTriple(verbs[j],j,tdl,lp,i,sentence);i++;
            Minimali=AddNotCopularVerbRelationTripleMinimal(verbs[j],j,tdl,lp,Minimali,sentence);Minimali++;     
         }
      }
      EndNum=i;
      MinimalEndNum=Minimali;
    }
   public static void NounPhraseRelations(LexicalizedParser lp,List<TypedDependency> tdl,String str) throws IOException{
       
       for(int i=1;i<=wordsNumber-2;i++){
        
         //dbpedia spotlight
         /*
         if(!POSwordNamedEntityURI[i].matches("")){
              if(POSwordNamedEntityURI[POSwordNamedEntityState[i]+i].matches("")& IsNoun(POSwordNamedEntityState[i]+i)==1){
                   String Class=Main.main(POSword[POSwordNamedEntityState[i]+i]);
                  if(!Class.matches("Null"))
                     AddNounPhraseTriplePatternP1(i,POSwordNamedEntityState[i],POSwordNamedEntityState[i]+i,str,lp);
                  else
                     AddNounPhraseTriplePatternP2(i,POSwordNamedEntityState[i],POSwordNamedEntityState[i]+i);
                   }
               else if(!POSwordNamedEntityURI[POSwordNamedEntityState[i]+i].matches("")& IsNoun(POSwordNamedEntityState[i]+i)==1){
                     AddNounPhraseTriplePatternP3(i,POSwordNamedEntityState[i],POSwordNamedEntityState[i]+i);
               }
               }
         */
         //TAGme
         if(phraseTagmeLinksRecognizer[i]==1){
              if(
                      //phraseTagmeLinksRecognizer[i+1]==0 & 
                   IsNoun(i+1)==1){
                //   String Class=Main.main(POSword[i+1]);
                  // if(!Class.matches("Null"))
                      AddNounPhraseTriplePatternP1(i,1,i+1,str,lp);
                 // else
                     AddNounPhraseTriplePatternP2(i,1,i+1);
                  // }
               //else if(phraseTagmeLinksRecognizer[i+1]==1 & IsNoun(i+1)==1){
                     AddNounPhraseTriplePatternP3(i,1,i+1);
              }
               }
         
       }
   }
     
//main functions   1-ReadInputFile    2-Initialization  3- ProcessLine  4- WriteTriplesInOutput   
   public static void ReadInputFile() throws Exception { 
    LexicalizedParser lp;
       //defines model for lexicalized parser
     lp = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
     String Input = "../Armin/sampleText/S2352179114200056.txt";
     int rowLine=11;
     List<TypedDependency> tdl;
     String line = null;
    
     try {
                FileReader fileReader = new FileReader(Input);
                BufferedReader bufferedReader = 
                new BufferedReader(fileReader);
                while((line = bufferedReader.readLine()) != null){ 
                  Initialization();
                  tdl=ProcessLine(lp,line); 
                  TriplesReview(line,tdl);
                          
                  convertion();
                  WriteTriplesInOutput(rowLine,line);
                  rowLine++;
                } 
                bufferedReader.close();    
            }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" +Input + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" + Input + "'");                  
        }
    }
   public static String RecognizeLinkedWordPositions(String sentence){
   int WordsPosInreplacedSentence=1;
    for(int j=1;j<wordsNumber;j++){
       for(int i=0;i<NumTagMeLinks;i++){
           if(POSwordPositionStart[j]>=Integer.parseInt(WikiLinksInformationArray[i][1]) &POSwordPositionEnd[j]<=Integer.parseInt(WikiLinksInformationArray[i][2])-1){
               //Integer.parseInt(WikiLinksInformationArray[i][4])
               phraseTagmeLinksRecognizer[WordsPosInreplacedSentence]=1;
               j=j+Integer.parseInt(WikiLinksInformationArray[i][4])-1;
               break;
           }
           else{
               phraseTagmeLinksRecognizer[WordsPosInreplacedSentence]=0;
               
           }
           
           
     }
       WordsPosInreplacedSentence++;
    }   
       
   return "";
   }
   public static String numberOfIndex(int i){
      switch(i) {
         case 0:return "zero";
         case 1: return "one"; 
         case 2: return "two";
         case 3: return "three";
         case 4: return "four";
         case 5: return "five";
         case 6: return "six";
         case 7: return "seven";
         case 8: return "eight";
         case 9: return "nine";
         case 10: return "ten";
          }
       return "";
   }
  
           
   public static String replaceSentenceConsideringTagmeLinks(String sent2){
       String replacedSent="";int sw=0;int start=0;int end=0;
       int firstToken=0;int endToken=0;int startToken=0;int i;
       for(i=0;i<NumTagMeLinks;i++){
          
              startToken=Integer.parseInt(WikiLinksInformationArray[i][1]);
              endToken=Integer.parseInt(WikiLinksInformationArray[i][2]);
              if(i>0)
              if(Integer.parseInt(WikiLinksInformationArray[i][2])==Integer.parseInt(WikiLinksInformationArray[i-1][2])||Integer.parseInt(WikiLinksInformationArray[i][1])<Integer.parseInt(WikiLinksInformationArray[i-1][2]))//for solving overlapped links-common end
                sw=1;
              else 
                sw=0;
              
              else if(i!=NumTagMeLinks-1)
              if(Integer.parseInt(WikiLinksInformationArray[i][1])==Integer.parseInt(WikiLinksInformationArray[i+1][1]))//for solving overlapped links-common start
                sw=2;
              else
                sw=0;
              
              
              
              
              for(int j=firstToken;j<startToken;j++){
                 replacedSent=replacedSent+sent2.charAt(j);
              }
              
              if(sw==1)
                  for(int j=Integer.parseInt(WikiLinksInformationArray[i-1][2]);j<Integer.parseInt(WikiLinksInformationArray[i][2]);j++){
                 replacedSent=replacedSent+sent2.charAt(j);
              }
              if(sw==0)
              if(!WikiLinksInformationArray[i][4].matches("1")){
                replacedSent=replacedSent+"t"+numberOfIndex(i); 
              }
              else 
                 replacedSent=replacedSent+WikiLinksInformationArray[i][0];
                 firstToken= endToken;
          
      }
     if(sent2.length()>=endToken)
     for(int j=endToken;j<sent2.length();j++)
        replacedSent=replacedSent+sent2.charAt(j);
   System.out.print(replacedSent);
   return replacedSent;
   }
   public static String preprocess(String sent2){
   sent2=sent2.replace("'s","  ");   
   return sent2;
   }
   public static void Initialization(){
                   EndNum=0;
                   MinimalEndNum=0;
                   wordsNumber=0; 
                   CounterVerbs=0; 
                   for(int i=0;i<40;i++){
                      triples[i][0]="";
                      triples[i][1]="";
                      triples[i][2]="";
                      Minimaltriples[i][0]="";
                      Minimaltriples[i][1]="";
                      Minimaltriples[i][2]="";
                      MinimaltriplesPosition[i][0]=1;
                      MinimaltriplesPosition[i][1]=1;
                      MinimaltriplesPosition[i][2]=1;
                      MinimaltriplesPosition[i][3]=1;
                      MinimaltriplesPosition[i][4]=1;  
                      triplesPosition[i][0]=1;
                      triplesPosition[i][1]=1;
                      triplesPosition[i][2]=1;
                      triplesPosition[i][3]=1;
                      triplesPosition[i][4]=1;
                      MinimaltriplesVerbPrep[i]="";
                      MinimaltripleCondition[i][0]="";
                      MinimaltripleCondition[i][1]="";
                      MinimaltripleCondition[i][2]="";
                      MinimaltriplesState[i]=1;
                   }
                    for(int i=0;i<7;i++){
                        objectFillersPrep[i]=""; 
                    }
                                       
                   for(int i=1;i<150;i++){
                     POSwordPositionStart[i]=0;
                     POSwordPositionEnd[i]=0;
                     POSwordNamedEntityState[i]=0;
                     POSwordNamedEntityURI[i]="";
                    phraseTagmeLinksRecognizer[i]=0;
                   }
   }
   
   public static List<TypedDependency> ProcessLine(LexicalizedParser lp,String sent2) throws Exception{  
     
      Tokenize1(sent2,lp);
      PositionOfWords(sent2,lp);
      String newSent=preprocess(sent2);
      tagmewebservice.main(newSent);
      String replacedSent=RecognizeLinkedWordPositions(sent2);
      replacedSent=replaceSentenceConsideringTagmeLinks(sent2);
     // replacedSent=RecognizeLinkedWordPositions(replacedSent);
      //String replacedSent=sent2;
      List<TypedDependency> tdl=ParseLine(replacedSent,lp);
      CounterVerbs=0;
      PrintDependencies(tdl);    
      dbpediaSpot.main(replacedSent);
      Tokenize1(replacedSent,lp);
      PositionOfWords(replacedSent,lp);
      VerbBasedRelations(lp,tdl,replacedSent);
      NounPhraseRelations(lp,tdl,replacedSent);
      GenitiveRelations(lp,tdl,replacedSent);
      IndirectVerbal(lp,tdl,replacedSent);
      ComparativeRelations(lp,tdl,replacedSent);
      SuperlativeRelations(lp,tdl,replacedSent);
    return tdl;
     } 
     public static void convertion(){ 
         for(int j=0;j<=EndNum;j++){
                if(triples[j][0].matches("Where")||triples[j][0].matches("where"))
                      triples[j][0]="place";
                else
                if(triples[j][0].matches("Who")||triples[j][0].matches("who"))
                      triples[j][0]="person"; 
                else
                if(triples[j][0].matches("When")||triples[j][0].matches("When"))
                      triples[j][0]="time"; 
                else
                if(triples[j][0].matches("What")||triples[j][0].matches("what"))
                      triples[j][0]="thing";
                
                                     
                if(triples[j][2].matches("Where")||triples[j][2].matches("where"))
                      triples[j][2]="place";
                else
                if(triples[j][2].matches("Who")||triples[j][2].matches("who"))
                      triples[j][2]="person"; 
                else
                if(triples[j][2].matches("When")||triples[j][2].matches("when"))
                      triples[j][2]="time"; 
                else
                if(triples[j][2].matches("What")||triples[j][2].matches("what"))
                      triples[j][2]="thing";          
        }

              for(int j=0;j<=MinimalEndNum;j++){
                if(Minimaltriples[j][0].matches("where")||Minimaltriples[j][0].matches("Where"))
                      Minimaltriples[j][0]="place";
                else
                if(Minimaltriples[j][0].matches("who")||Minimaltriples[j][0].matches("Who"))
                      Minimaltriples[j][0]="person"; 
                else
                if(Minimaltriples[j][0].matches("when")||Minimaltriples[j][0].matches("When"))
                      Minimaltriples[j][0]="time"; 
                else
                if(Minimaltriples[j][0].matches("what")||Minimaltriples[j][0].matches("What"))
                      Minimaltriples[j][0]="thing";
                
                
                if(Minimaltriples[j][2].matches("where")||Minimaltriples[j][2].matches("Where"))
                      Minimaltriples[j][2]="place";
                else
                if(Minimaltriples[j][2].matches("who")||Minimaltriples[j][2].matches("Who"))
                      Minimaltriples[j][2]="person"; 
                else
                if(Minimaltriples[j][2].matches("when")||Minimaltriples[j][2].matches("When"))
                      Minimaltriples[j][2]="time"; 
                else
                if(Minimaltriples[j][2].matches("what")||Minimaltriples[j][2].matches("What"))
                      Minimaltriples[j][2]="thing"; 
                  
        }
     }
    public static void  replaceTiWithPhrases(int i){
   
       triples[i][0] = triples[i][0].replace("tzero", WikiLinksInformationArray[0][0]);
        triples[i][0] = triples[i][0].replace("tone", WikiLinksInformationArray[1][0]);
        triples[i][0] = triples[i][0].replace("ttwo", WikiLinksInformationArray[2][0]);
        triples[i][0] = triples[i][0].replace("tthree", WikiLinksInformationArray[3][0]);
        triples[i][0] = triples[i][0].replace("tfour", WikiLinksInformationArray[4][0]);
        triples[i][0] = triples[i][0].replace("tfive", WikiLinksInformationArray[5][0]);
        triples[i][0] = triples[i][0].replace("tsix", WikiLinksInformationArray[6][0]);
        triples[i][0] = triples[i][0].replace("tseven", WikiLinksInformationArray[7][0]);
        triples[i][0] = triples[i][0].replace("teight", WikiLinksInformationArray[8][0]);
        triples[i][0] = triples[i][0].replace("tnine", WikiLinksInformationArray[9][0]);
       
        triples[i][1] = triples[i][1].replace("tzero", WikiLinksInformationArray[0][0]);
        triples[i][1] = triples[i][1].replace("tone", WikiLinksInformationArray[1][0]);
        triples[i][1] = triples[i][1].replace("ttwo", WikiLinksInformationArray[2][0]);
        triples[i][1] = triples[i][1].replace("tthree", WikiLinksInformationArray[3][0]);
        triples[i][1] = triples[i][1].replace("tfour", WikiLinksInformationArray[4][0]);
        triples[i][1] = triples[i][1].replace("tfive", WikiLinksInformationArray[5][0]);
        triples[i][1] = triples[i][1].replace("tsix", WikiLinksInformationArray[6][0]);
        triples[i][1] = triples[i][1].replace("tseven", WikiLinksInformationArray[7][0]);
        triples[i][1] = triples[i][1].replace("teight", WikiLinksInformationArray[8][0]);
        triples[i][1] = triples[i][1].replace("tnine", WikiLinksInformationArray[9][0]);
       
        triples[i][2] = triples[i][2].replace("tzero", WikiLinksInformationArray[0][0]);
        triples[i][2] = triples[i][2].replace("tone", WikiLinksInformationArray[1][0]);
        triples[i][2] = triples[i][2].replace("ttwo", WikiLinksInformationArray[2][0]);
        triples[i][2] = triples[i][2].replace("tthree", WikiLinksInformationArray[3][0]);
        triples[i][2] = triples[i][2].replace("tfour", WikiLinksInformationArray[4][0]);
        triples[i][2] = triples[i][2].replace("tfive", WikiLinksInformationArray[5][0]);
        triples[i][2] = triples[i][2].replace("tsix", WikiLinksInformationArray[6][0]);
        triples[i][2] = triples[i][2].replace("tseven", WikiLinksInformationArray[7][0]);
        triples[i][2] = triples[i][2].replace("teight", WikiLinksInformationArray[8][0]);
        triples[i][2] = triples[i][2].replace("tnine", WikiLinksInformationArray[9][0]);
       
    
    }
    public static void  replaceTiWithPrhasesMinimal(int i){
   
        Minimaltriples[i][0] = Minimaltriples[i][0].replace("tzero", WikiLinksInformationArray[0][0]);
        Minimaltriples[i][0] = Minimaltriples[i][0].replace("tone", WikiLinksInformationArray[1][0]);
        Minimaltriples[i][0] = Minimaltriples[i][0].replace("ttwo", WikiLinksInformationArray[2][0]);
        Minimaltriples[i][0] = Minimaltriples[i][0].replace("tthree", WikiLinksInformationArray[3][0]);
        Minimaltriples[i][0] = Minimaltriples[i][0].replace("tfour", WikiLinksInformationArray[4][0]);
        Minimaltriples[i][0] = Minimaltriples[i][0].replace("tfive", WikiLinksInformationArray[5][0]);
        Minimaltriples[i][0] = Minimaltriples[i][0].replace("tsix", WikiLinksInformationArray[6][0]);
        Minimaltriples[i][0] = Minimaltriples[i][0].replace("tseven", WikiLinksInformationArray[7][0]);
        Minimaltriples[i][0] = Minimaltriples[i][0].replace("teight", WikiLinksInformationArray[8][0]);
        Minimaltriples[i][0] = Minimaltriples[i][0].replace("tnine", WikiLinksInformationArray[9][0]);
       
        Minimaltriples[i][1] = Minimaltriples[i][1].replace("tzero", WikiLinksInformationArray[0][0]);
        Minimaltriples[i][1] = Minimaltriples[i][1].replace("tone", WikiLinksInformationArray[1][0]);
        Minimaltriples[i][1] = Minimaltriples[i][1].replace("ttwo", WikiLinksInformationArray[2][0]);
        Minimaltriples[i][1] = Minimaltriples[i][1].replace("tthree", WikiLinksInformationArray[3][0]);
        Minimaltriples[i][1] = Minimaltriples[i][1].replace("tfour", WikiLinksInformationArray[4][0]);
        Minimaltriples[i][1] = Minimaltriples[i][1].replace("tfive", WikiLinksInformationArray[5][0]);
        Minimaltriples[i][1] = Minimaltriples[i][1].replace("tsix", WikiLinksInformationArray[6][0]);
        Minimaltriples[i][1] = Minimaltriples[i][1].replace("tseven", WikiLinksInformationArray[7][0]);
        Minimaltriples[i][1] = Minimaltriples[i][1].replace("teight", WikiLinksInformationArray[8][0]);
        Minimaltriples[i][1] = Minimaltriples[i][1].replace("tnine", WikiLinksInformationArray[9][0]);
       
        Minimaltriples[i][2] = Minimaltriples[i][2].replace("tzero", WikiLinksInformationArray[0][0]);
        Minimaltriples[i][2] = Minimaltriples[i][2].replace("tone", WikiLinksInformationArray[1][0]);
        Minimaltriples[i][2] = Minimaltriples[i][2].replace("ttwo", WikiLinksInformationArray[2][0]);
        Minimaltriples[i][2] = Minimaltriples[i][2].replace("tthree", WikiLinksInformationArray[3][0]);
        Minimaltriples[i][2] = Minimaltriples[i][2].replace("tfour", WikiLinksInformationArray[4][0]);
        Minimaltriples[i][2] = Minimaltriples[i][2].replace("tfive", WikiLinksInformationArray[5][0]);
        Minimaltriples[i][2] = Minimaltriples[i][2].replace("tsix", WikiLinksInformationArray[6][0]);
        Minimaltriples[i][2] = Minimaltriples[i][2].replace("tseven", WikiLinksInformationArray[7][0]);
        Minimaltriples[i][2] = Minimaltriples[i][2].replace("teight", WikiLinksInformationArray[8][0]);
        Minimaltriples[i][2] = Minimaltriples[i][2].replace("tnine", WikiLinksInformationArray[9][0]);
    }
   public static void WriteTriplesInOutput(int row,String line){
      String Output = "../Armin/sampleText/S2352179114200056Out.txt";

        try {
             FileWriter fileWriter = new FileWriter(Output,true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
             bufferedWriter.newLine();
             bufferedWriter.write("*******************************************************************");
             bufferedWriter.write(Integer.toString(row));
             bufferedWriter.write("******************************************************************");
             bufferedWriter.newLine();
             bufferedWriter.write(line); 
             bufferedWriter.newLine();
             bufferedWriter.newLine();
            
             bufferedWriter.write("______________________________________________________Maximal Relations______________________________________________________");
             bufferedWriter.newLine();
             bufferedWriter.newLine();
             System.out.println();        
             System.out.println("-------------------Maximal Relations--------------------");
             System.out.println();
            for(int j=0;j<=EndNum;j++){
                replaceTiWithPhrases(j);  
                if(triples[j][0]!="" && triples[j][2]!=""){
                      bufferedWriter.write(typeTriples[j]);bufferedWriter.write("------>");
                      bufferedWriter.write(triples[j][0]); 
                      bufferedWriter.write("*");
                      bufferedWriter.write(triples[j][1]);
                      bufferedWriter.write("*");
                      bufferedWriter.write(triples[j][2]);
                      bufferedWriter.newLine();
                  
                      System.out.print(typeTriples[j]);System.out.print("------>");
                      System.out.print(triples[j][0]+'*'+triples[j][1]+'*'+triples[j][2]); 
                      System.out.println();
                    }
                    else if (triples[j][0]!="" && triples[j][2]==""){
                         bufferedWriter.write(typeTriples[j]);bufferedWriter.write("------>");
                         bufferedWriter.write(triples[j][0]); 
                         bufferedWriter.write("*");
                         bufferedWriter.write(triples[j][1]);
                         bufferedWriter.write("*");
                         bufferedWriter.write("---");
                         bufferedWriter.newLine();
                        
                         System.out.print(typeTriples[j]);System.out.print("------>");
                         System.out.print(triples[j][0]+'*'+triples[j][1]+'*'+triples[j][2]); 
                         System.out.println();
                        }
                 
                  
           }
            bufferedWriter.newLine();
            bufferedWriter.write("______________________________________________________Minimal Relations______________________________________________________");
            bufferedWriter.newLine();
            bufferedWriter.newLine();
            System.out.println();
            System.out.println("--------------------Minimal Relations--------------------");
            System.out.println();
            for(int j=0;j<=MinimalEndNum;j++){
                replaceTiWithPrhasesMinimal(j); 
                if(MinimaltriplesState[j]!=0){
                if(Minimaltriples[j][0]!="" && Minimaltriples[j][2]!=""){
                  bufferedWriter.write(MinimaltypeTriples[j]);bufferedWriter.write("------>"); 
                  bufferedWriter.write(Minimaltriples[j][0]); 
                  bufferedWriter.write("*");
                  bufferedWriter.write(Minimaltriples[j][1]);
                  bufferedWriter.write(" ");
                  bufferedWriter.write(MinimaltriplesVerbPrep[j]);
                  bufferedWriter.write("*");
                  bufferedWriter.write(Minimaltriples[j][2]);
                  bufferedWriter.newLine();              
                  
                  System.out.print(MinimaltypeTriples[j]);System.out.print("------>");
                  System.out.print(Minimaltriples[j][0]+'*'+Minimaltriples[j][1]+' '+MinimaltriplesVerbPrep[j]+'*'+Minimaltriples[j][2]); 
                  System.out.print("         ");
                  if(!MinimaltripleCondition[j][0].matches("")){
                        bufferedWriter.write("                       ");
                        bufferedWriter.write(MinimaltripleCondition[j][0]+'*'+MinimaltripleCondition[j][1]+'*'+MinimaltripleCondition[j][2]);
                        System.out.print("                      ");
                        System.out.print(MinimaltripleCondition[j][0]+'*'+MinimaltripleCondition[j][1]+'*'+MinimaltripleCondition[j][2]); 
                     }
                     bufferedWriter.newLine();
                     System.out.println();
                     }else if (Minimaltriples[j][0]!="" && Minimaltriples[j][2]==""){
                         bufferedWriter.write(MinimaltypeTriples[j]);bufferedWriter.write("------>"); 
                         bufferedWriter.write(Minimaltriples[j][0]); 
                         bufferedWriter.write("*");
                         bufferedWriter.write(Minimaltriples[j][1]);
                         bufferedWriter.write("*");
                         bufferedWriter.write("---");
                         bufferedWriter.newLine();
                         System.out.print(MinimaltypeTriples[j]);System.out.print("------");
                         System.out.print(Minimaltriples[j][0]+'*'+Minimaltriples[j][1]+'*'+Minimaltriples[j][2]); 
                         System.out.println();
                    }
                }       
           }
          bufferedWriter.close();
        }
        catch(IOException ex) {
            System.out.println("Error writing to file '"+ Output + "'");
            }
        
   }
     
   //main function
   public static void main(String[] args) throws Exception { 
        ReadInputFile();
        
  } 
}
  