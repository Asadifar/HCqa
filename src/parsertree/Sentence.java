/*
 The class of sentence  was made from a sequence of words. 
variables:
            wordPre[]: an array of words before name entity recognition
            word[]: an array of words after name entity recognition
            quotations= an array for quotations
            verbs: recognized verbs of sentence  
            NamedEntity
            CounterVerbs;
            quotationCounter;   
            wordsNumber; 
            CounterVerbs;
 */
package parsertree;

//import static TAGmeWebService.tagmewebservice.WikiLinksInformationArray;


import edu.stanford.nlp.parser.lexparser.LexicalizedParser; 
import edu.stanford.nlp.process.CoreLabelTokenFactory; 

import edu.stanford.nlp.ling.CoreLabel;   

import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;
import java.io.StringReader;
import java.util.List;
import static parsertree.NamedEntityRecognition.NumTagMeLinks;
import static parsertree.Word.PrintDependencies;
import static parsertree.Word.SearchForAdvmodOfVerb;
import static parsertree.Word.numberOfIndex;
import static parsertree.Word.relIsIndirectObject;

public class Sentence {
  
   
public static Word[] wordPre=new Word[150];
static Word[] word=new Word[150];
static Quotation[][] quotations=new Quotation[5][7]; 
static Verb[] verbs=new Verb[40]; 
static NamedEntity[] namedEntity=new NamedEntity[150]; 
static int CounterVerbs;
public static int quotationCounter;   
public static int wordsNumber;


static int row=0; 
static public String line;
private final TokenizerFactory<CoreLabel> tokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), "invertible=true");
static LexicalizedParser parser = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
public static List<TypedDependency> tdl;
public Sentence(String line){
     tdl=ParseLine(line);
     
   
   }
public Tree pars(String line) {                
        List<CoreLabel> tokens = tokenize(line);
//        LexicalizedParser parser = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
        Tree tree = parser.apply(tokens);
        return tree;
    }
private List<CoreLabel> tokenize(String line) {
        Tokenizer<CoreLabel> tokenizer =tokenizerFactory.getTokenizer(new StringReader(line));    
        return tokenizer.tokenize();
}
public static void PositionOfWords(String line){
        wordPre[1].POSwordStart=0;
        int wordNum=1;
        for(int i=0;i<=line.length()-1;i++){
        char c=line.charAt(i);
        if(line.charAt(i)==' '||line.charAt(i)=='?'||line.charAt(i)=='.'||line.charAt(i)==','||line.charAt(i)=='\''){
          wordPre[wordNum].POSwordEnd=i-1;
          wordNum++;
          if(i+1<line.length()-1) wordPre[wordNum].POSwordStart=i+1;
          if(line.charAt(i)==','||line.charAt(i)=='\''||line.charAt(i)=='.'){
             wordPre[wordNum].POSwordStart=i;
          }
        }
         
       }
       wordPre[wordNum].POSwordEnd=wordPre[wordNum].POSwordStart;
}
public static void Tokenize1Pre(String line){ /*prints tockenz and then  parse tree*/
    Sentence parser = new Sentence(line);
    Tree tree = parser.pars(line);  
    List<Tree> leaves = tree.getLeaves();
    //wordPre[0].POSwordStart=-1;         
    //wordPre[0].POSwordEnd=-1;         
    int i=0;int sw=0;
    int ws=0,we=0;
    String w="",wt="";
    int k=1;//full array of pos tag 
    int doubleQutation=0; 
    int singleQutation=0;
    String st;
    for (Tree leaf : leaves) { 
         Tree parent = leaf.parent(tree);
         System.out.print(leaf.label().value() + "-" + parent.label().value());
      
         w= leaf.label().value();
          st=w;
          if(k>1){
              if(wordPre[k-1].POSword.contentEquals("-")||wordPre[k-1].POSword.contentEquals("$"))
                ws= wordPre[k-1].POSwordEnd+1;
               //-----------------------'s----------------
              else if(
                      //POSwordPre[k].contentEquals(",")||
                      w.contentEquals("!")|| w.contentEquals(";")|| w.contentEquals("\\")
                  || w.contentEquals(".")|| w.contentEquals("?")|| w.contentEquals(":")//||POSwordPre[k].contentEquals("'")
                  || w.contentEquals("-")
                      //||POSwordPre[k].contentEquals("'s")
                      || w.contentEquals("n't"))
                     ws= wordPre[k-1].POSwordEnd+1;
              
               //-----------------------double qutation----------------
              else if(w.contentEquals("``")){
                  doubleQutation++;    
                  if(doubleQutation==1)
                    ws= wordPre[k-1].POSwordEnd+2;
                  else{
                    ws=  wordPre[k-1].POSwordEnd+1;
                     doubleQutation=0;
                  }
              }
              //-----------------------single qutation----------------
            else if(w.contentEquals("`")){
                  singleQutation++;    
                  if(singleQutation==1)
                    ws= wordPre[k-1].POSwordEnd+2;
                  else{
                    ws=  wordPre[k-1].POSwordEnd+1;
                     singleQutation=0;
                  }
             }
          //------------------------end single qutation-----------------------------------
          else
                    
                  ws= wordPre[k-1].POSwordEnd+2;
          //------------------------------end k>1--------------
          }   
          else
             ws= 1;  
          //-----------------------------------------------------------------
          
          we= w.length()+ws-1;
         //--------------------end position of Word------
          
         wt= parent.label().value();
        
          
//^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^finding verbs in sentense(VB,VBD,VBN,VBP,VBZ)
         if((parent.label().value().equals("VB"))||
                 (parent.label().value().equals("VBN"))|(parent.label().value().equals("VBP"))
                 ||(parent.label().value().equals("VBD"))
                 ||(parent.label().value().equals("VBZ"))){   
                       verbs[CounterVerbs].verbs=leaf.label().value();
                       verbs[CounterVerbs].verbsRoles=parent.label().value();
                       verbs[CounterVerbs].verbsPosition=k;
                       CounterVerbs++;
         }
         else{
              
              if(parent.label().value().equals("VBG")){
                  sw=0;
                  if(k>1)
                       if(wordPre[k-1].POStagword.matches("IN"))
                          sw=1;
                    if(k>2)
                      if(wordPre[k-2].POStagword.matches("IN"))  
                          sw=1;
                
              if(sw==0)
              {   
                 verbs[CounterVerbs].verbs=leaf.label().value();
                       verbs[CounterVerbs].verbsRoles=parent.label().value();
                       verbs[CounterVerbs].verbsPosition=k;
                 CounterVerbs++;
              }
           }  
         }
        System.out.println(w+","+wt+","+ws+","+we+",");
        wordPre[k]=new Word(w,wt,ws,we); 
         k++;
         //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    }
   wordsNumber=k; 
   }
 public static void RecognizeQuotationPositions(){
   for(int i=1;i<wordsNumber;i++){
     if(wordPre[i].POSword.matches("``"))
         quotations[quotationCounter][0].quotation=Integer.toString(i);
     else if(wordPre[i].POSword.matches("''")){
         quotations[quotationCounter][1].quotation=Integer.toString(i);
         quotationCounter++;
     }
   }
   }

public static int AreRelatedTogether(int current,int counter){
        int index2=0;
        String[] str1;int gov2Index,dep2Index,k=0;
       String rel2,gov2,dep2;   
       dep2="";
       rel2="";
            List<TypedDependency> tdl=null;

                         for (TypedDependency t2 : tdl){
                            
                            str1=tdl.get(index2).dep().toString().split("/");
                            dep2=str1[0];
                            //dep2Index=tdl.get(index2).dep().index();
                            str1=tdl.get(index2).reln().toString().split("/");
                            //rel2=str1[0];
                            str1=tdl.get(index2).gov().toString().split("/");
                            //gov2Index=tdl.get(index2).gov().index();
                            gov2=str1[0]; 
                            if((gov2.matches(word[current].POSword)& dep2.matches(word[counter].POSword)) || (dep2.matches(word[current].POSword)& gov2.matches(word[counter].POSword)))
                                    
                               return 1;
                            index2++;  
                            }//end for
       
       return 0;
   }
   
public static void prestopRemover(String line){
       Tokenize1Pre(line);
       RecognizeQuotationPositions();
       fullUpWithAllBetweenQuotation();
       RecognizeQuotationPhrases(line);
       RecognizeIsThereAVerbInQuotation(line); 
}
    
public static void RecognizeQuotationPhrases(String line){
   String[] line1;int dep1Index,gov2Index,dep2Index,gov1Index,k=0;
       String rel1,dep1,gov1,rel2,gov2,dep2;   
       dep2="";
       rel2="";
       int sequenceNum=-1;
       dep2Index=0;
       int index1=0;
       int step=1;   
       List<TypedDependency> tdl0=ParseLine(line);
       for (TypedDependency t1 : tdl0){
                     line1=tdl0.get(index1).dep().toString().split("/");
                     dep1Index=tdl0.get(index1).dep().index();
                     dep1=line1[0];
                     gov1Index=tdl0.get(index1).gov().index();
                     line1=tdl0.get(index1).reln().toString().split("/");
                     rel1=line1[0];
                     line1=tdl0.get(index1).gov().toString().split("/");
                     gov1=line1[0];
                     if(!gov1.matches("ROOT"))
                     if(!relIsIndirectObject(rel1).matches("")& govIsMatchedWhithVerbs(gov1)==0){
                          for(int j=0;j<quotationCounter;j++){
                             if(dep1Index>=Integer.parseInt(quotations[j][0].quotation) & dep1Index<=Integer.parseInt(quotations[j][1].quotation)
                                & gov1Index>=Integer.parseInt(quotations[j][0].quotation)&  gov1Index<=Integer.parseInt(quotations[j][1].quotation) ){
                                   quotations[j][2].quotation=rel1;
                                   quotations[j][3].quotation=gov1;
                                   quotations[j][4].quotation=dep1;
                             }
                          }                  
                     }
                    index1++;
                   
                }
   }
public static void RecognizeIsThereAVerbInQuotation(String line){
         String[] line1;int dep1Index,gov2Index,dep2Index,gov1Index,k=0;
       String rel1,dep1,gov1,rel2,gov2,dep2;   
       dep2="";
       rel2="";
       dep2Index=0;
       int index1=0;
       List<TypedDependency> tdl0=ParseLine(line);
       for (TypedDependency t1 : tdl0){
                     line1=tdl0.get(index1).dep().toString().split("/");
                     dep1Index=tdl0.get(index1).dep().index();
                     dep1=line1[0];
                     gov1Index=tdl0.get(index1).gov().index();
                     line1=tdl0.get(index1).reln().toString().split("/");
                     rel1=line1[0];
                     line1=tdl0.get(index1).gov().toString().split("/");
                     gov1=line1[0];
                     if(!gov1.matches("ROOT"))
                     if(govIsMatchedWhithVerbs(gov1)==1){
                          for(int j=0;j<quotationCounter;j++){
                             if(dep1Index>=Integer.parseInt(quotations[j][0].quotation) & dep1Index<=Integer.parseInt(quotations[j][1].quotation)
                                & gov1Index>=Integer.parseInt(quotations[j][0].quotation)&  gov1Index<=Integer.parseInt(quotations[j][1].quotation) ){
                                   quotations[j][5].quotation="1";
                                                         
                             }
                          }                  
                     }
                    index1++;
                   
                }
        
    }   
public static void  fullUpWithAllBetweenQuotation(){
     for(int i=0;i<quotationCounter;i++){
        quotations[i][6].quotation="";
         for(int j=Integer.parseInt( quotations[i][0].quotation);j<=Integer.parseInt( quotations[i][1].quotation);j++)
          quotations[i][6].quotation= quotations[i][6].quotation+" "+wordPre[j].POSword;
     
     }
    } 
public static List<TypedDependency> ParseLine(String line) {
   //List<TypedDependency> tdl;
    // public final static String PCG_MODEL = "./dependencyviewer-0.1.1/edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz";        
   //LexicalizedParser lp; 
   //private final LexicalizedParser parser = LexicalizedParser.loadModel(PCG_MODEL);
    //LexicalizedParser lp = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
   //  List<TypedDependency> tdl;
    Tree parse;   
    TokenizerFactory<CoreLabel> tokenizerFactory =  
    PTBTokenizer.factory(new CoreLabelTokenFactory(), ""); 
    List<CoreLabel> rawWords2 =  tokenizerFactory.getTokenizer(new StringReader(line)).tokenize(); 
    parse = parser.apply(rawWords2); 
    //parse.pennPrint();
    TreebankLanguagePack tlp = new PennTreebankLanguagePack(); 
    GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory(); 
    GrammaticalStructure gs = gsf.newGrammaticalStructure(parse); 
    List<TypedDependency> tdl = gs.typedDependenciesCCprocessed(); 
    return tdl;
   }

public static void Tokenize1(String line,List<TypedDependency> tdl){
    //-----------------------------------prints tockenz and then  parse tree----------------------------------
    Sentence parser = new Sentence(line);
    Tree tree = parser.pars(line);  
    List<Tree> leaves = tree.getLeaves();
    
    wordPre[0].POSwordStart=-1;
    wordPre[0].POSwordEnd=-1;
   
   int i=0;int sw=0;
   int k=1;//full array of pos tag 
  
    for (Tree leaf : leaves) { 
         Tree parent = leaf.parent(tree);
         System.out.print(leaf.label().value() + "-" + parent.label().value());
          word[k].POSword= leaf.label().value();
          word[k].POStagword= parent.label().value();
        
           //---------------------------------------position of Word-------
          if(
                 // POSwordPre[k].contentEquals(",")||//in reverb an space is existed before comma but in nyt no so in reverb ve should comment this line
                  wordPre[k].POSword.contentEquals("\\")
                  ||wordPre[k].POSword.contentEquals(".")||wordPre[k].POSword.contentEquals("?"))
          wordPre[k].POSwordStart= wordPre[k-1].POSwordEnd+1;
          else
          wordPre[k].POSwordStart= wordPre[k-1].POSwordEnd+2;
          wordPre[k].POSwordEnd= wordPre[k].POSword.length()+wordPre[k].POSwordStart-1;
         //--------------------------
//^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^finding verbs in sentense(VB,VBD,VBN,VBP,VBZ)
         if((parent.label().value().equals("VB"))||
                 (parent.label().value().equals("VBN"))|(parent.label().value().equals("VBP"))
                 ||(parent.label().value().equals("VBD"))
                 ||(parent.label().value().equals("VBZ"))){   
                       verbs[CounterVerbs].verbs=leaf.label().value();
                       CounterVerbs=SearchForAdvmodOfVerb(verbs[CounterVerbs].verbs,CounterVerbs,tdl);

                       verbs[CounterVerbs].verbsRoles=parent.label().value();
                       verbs[CounterVerbs].verbsPosition=k;
                       CounterVerbs++;
         }
         else{
              
              if(parent.label().value().equals("VBG")){
                  sw=0;
                  if(k>1)
                    if(word[k-1].POStagword.matches("IN"))
                       sw=1;
                 if(k>2)
                    if(word[k-2].POStagword.matches("IN"))  
                       sw=1;
              if(sw==0)
              {   
                 verbs[CounterVerbs].verbs=leaf.label().value();
                 CounterVerbs=SearchForAdvmodOfVerb(verbs[CounterVerbs].verbs,CounterVerbs,tdl);

                 verbs[CounterVerbs].verbsRoles=parent.label().value();
                 verbs[CounterVerbs].verbsPosition=k;
                 CounterVerbs++;
              }
              }
         }
         
       //  CounterVerbs=SearchForAdvmodOfVerb(verbs[CounterVerbs],CounterVerbs,tdl);
         
      k++;
         //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    }
    
   wordsNumber=k; 
   }
   
  public static String stopRemover(String line){
     line=line.replaceAll("\"", "");
    // line=line.replaceAll("[()]", "");
    // line=line.replaceAll(";", "");
     line = line.replaceAll("[{}]","");
     line=line.replaceAll("\''", "");
     line=line.replaceAll("``", "");
    
    // line=line.replaceAll("[\\[\\](){}]","");
     line=line.replaceAll("\\*","");
     line=line.replaceAll("^","");
    // line=line.replaceAll(":","");
     line=line.replaceAll("  "," ");
     return line;
   }  

public static String RecognizeLinkedWordPositions(){
   int WordsPosInreplacedSentence=1;
   int i;
    for(int j=1;j<wordsNumber;j++){
       for( i=0;i<NamedEntityRecognition.NumTagMeLinks;i++){
           if(wordPre[j].POSwordStart>=Integer.parseInt(NamedEntityRecognition.namedEntity[i].phraseStart)+1 &wordPre[j].POSwordEnd<=Integer.parseInt(NamedEntityRecognition.namedEntity[i].phraseEnd) ){
             // if(i!=NumTagMeLinks)
               //   if( (POSwordPositionStart[j]<Integer.parseInt(NamedEntityRecognition.namedEntity[i+1].phraseStart))){
               //Integer.parseInt(NamedEntityRecognition.namedEntity[i].numberOfWords)
             //  if(phraseTagmeLinksRecognizer[WordsPosInreplacedSentence-1]==1 & POStagword[j-1].matches("NNP")&POStagword[j].matches("NNP"))
               wordPre[WordsPosInreplacedSentence].phraseTagmeLinksRecognizer=1;
               j=j+Integer.parseInt(NamedEntityRecognition.namedEntity[i].numberOfWords)-1;
               
               break;
              //    }
           }
           else{
               wordPre[WordsPosInreplacedSentence].phraseTagmeLinksRecognizer=0;
               
           }
          System.out.println( wordPre[WordsPosInreplacedSentence].phraseTagmeLinksRecognizer);
           
     }
      if(wordPre[WordsPosInreplacedSentence].phraseTagmeLinksRecognizer==0){ 
        word[WordsPosInreplacedSentence].POSwordStart=word[WordsPosInreplacedSentence-1].POSwordEnd+1;
        word[WordsPosInreplacedSentence].POSwordEnd=word[WordsPosInreplacedSentence].POSwordStart;
      
      }else{
         if(Integer.parseInt(NamedEntityRecognition.namedEntity[i].numberOfWords)>1){
           WordsPosInreplacedSentence++;
           word[WordsPosInreplacedSentence].POSwordStart=word[WordsPosInreplacedSentence-2].POSwordEnd+1; 
           word[WordsPosInreplacedSentence].POSwordEnd=word[WordsPosInreplacedSentence].POSwordStart+Integer.parseInt(NamedEntityRecognition.namedEntity[i].numberOfWords)-1;
        
         }
         else{
           
             
         word[WordsPosInreplacedSentence].POSwordStart=word[WordsPosInreplacedSentence-1].POSwordEnd+1; 
         word[WordsPosInreplacedSentence].POSwordEnd=word[WordsPosInreplacedSentence].POSwordStart+Integer.parseInt(NamedEntityRecognition.namedEntity[i].numberOfWords)-1;
         }
      }
         System.out.println(word[WordsPosInreplacedSentence].POSwordStart+"   "+word[WordsPosInreplacedSentence].POSwordEnd);
         WordsPosInreplacedSentence++;
    }   
     
   return "";
   }
 public static String replaceSentenceConsideringTagmeLinks(String line){
      
       String replacedSent="";int sw=0;int start=0;int end=0;
       int firstToken=0;int endToken=0;int startToken=0;int i;
       for(i=0;i<NumTagMeLinks;i++){
              startToken=Integer.parseInt(NamedEntityRecognition.namedEntity[i].phraseStart);
              endToken=Integer.parseInt(NamedEntityRecognition.namedEntity[i].phraseEnd);
              if(i>0)
              if(Integer.parseInt(NamedEntityRecognition.namedEntity[i].phraseEnd)==Integer.parseInt(NamedEntityRecognition.namedEntity[i-1].phraseEnd)||Integer.parseInt(NamedEntityRecognition.namedEntity[i].phraseStart)<Integer.parseInt(NamedEntityRecognition.namedEntity[i-1].phraseEnd))//for solving overlapped links-common end
                sw=1;
              else 
                sw=0;
              
              else if(i!=NumTagMeLinks-1)
              if(Integer.parseInt(NamedEntityRecognition.namedEntity[i].phraseStart)==Integer.parseInt(NamedEntityRecognition.namedEntity[i+1].phraseStart))//for solving overlapped links-common start
                sw=2;
              else
                sw=0;
              
              
              
              
              for(int j=firstToken;j<startToken;j++){
                 replacedSent=replacedSent+line.charAt(j);
              }
              
              if(sw==1)
                  for(int j=Integer.parseInt(NamedEntityRecognition.namedEntity[i-1].phraseEnd);j<Integer.parseInt(NamedEntityRecognition.namedEntity[i].phraseEnd);j++){
                 replacedSent=replacedSent+line.charAt(j);
              }
              if(sw==0)
              if(!NamedEntityRecognition.namedEntity[i].numberOfWords.matches("1")){
                replacedSent=replacedSent+"the T1"+numberOfIndex(i); 
              }
              else 
                 replacedSent=replacedSent+NamedEntityRecognition.namedEntity[i].phrase;
                 firstToken= endToken;
          
      }
     if(line.length()>=endToken)
     for(int j=endToken;j<line.length();j++)
        replacedSent=replacedSent+line.charAt(j);
   System.out.print(replacedSent);
   replacedSent=replacedSent.replaceAll("the the", "the");
   System.out.print(replacedSent);
   return replacedSent;
   }
   public static String preprocess(String line){
   line=line.replace("'s","  ");   
   return line;
   }
   public static int govIsMatchedWhithVerbs(String rel){
       for(int j=0;j<Sentence.CounterVerbs;j++){
         if(rel.matches(Sentence.verbs[j].verbs))
             return 1;
        }
       return 0;
   }   
     public static int NotDependencyBetwweenToVerbs(String dep,String str){
        //-------------------------------
         int counterEquals;
              counterEquals=1; 
              for(int b=0;b<CounterVerbs;b++)
                  if(dep.matches(verbs[b].verbs)){ 
                      counterEquals=0;
                      break;
                  }
              
          //----------------------------------    
   return counterEquals;
   }
   public static int VerbToVerb(int verbNum){
       if(verbs[verbNum].verbsPosition>2)   
         if (word[verbs[verbNum].verbsPosition-1].POSword.matches("to")|| word[verbs[verbNum].verbsPosition-2].POSword.matches("to"))
             return 1;
          else return 0;
       else return 0;
   }
  public static List<TypedDependency> processSentense(String line) throws Exception{  
       Initialization();
       prestopRemover(line);
       line=stopRemover(line);
       Tokenize1Pre(line);
      //PositionOfWords(line,lp);
      //String newSent=preprocess(line);
      NamedEntityRecognition.main(line);
      String replacedSent=RecognizeLinkedWordPositions();
      replacedSent=replaceSentenceConsideringTagmeLinks(line);
      //replacedSent=RecognizeLinkedWordPositions(replacedSent);
      //String replacedSent=line;
      List<TypedDependency> tdl=ParseLine(replacedSent);
      CounterVerbs=0;
      PrintDependencies(tdl);    
      //dbpediaSpot.main(replacedSent);
      Tokenize1(replacedSent,tdl);
      //PositionOfWords(replacedSent,lp);
      
      
    return tdl;
     } 
public static void Initialization(){
                   
                   quotationCounter=0;
                   wordsNumber=0; 
                   CounterVerbs=0; 
                   
                   for(int i=0;i<150;i++){
                     word[i]=new Word("","",0,0);
                     wordPre[i]=new Word("","",0,0);
                     //namedEntity[i].phraseTagmeLinksRecognizer=0;
                    }
                   for(int j=0;j<4;j++)
                   for(int i=0;i<5;i++){
                   quotations[i][j]=new Quotation();
                   }
                   for(int j=0;j<40;j++){
                      verbs[j]=new Verb("",0,"");
                   }
                    
                   
   }
  

}
