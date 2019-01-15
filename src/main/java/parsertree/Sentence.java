//  This class process the Input line(question or a sentence)
//  The class of sentence  was made from a sequence of words.
/*
    --------------------variables------------------- 

            wordPre[]: an array of words before name entity recognition
            word[]: an array of words after name entity recognition
            quotations[]= an array for quotations
            verbs[]: recognized verbs   
            NamedEntity[]: an array of named entities
            CounterVerbs: the number of verbs
            quotationCounter: the number of quotation   
            wordsNumber: the number of words 
            
     --------------------Functions-------------------
ReadInputFile: read the Input file and each step gets one line to process and generate triples     
processSentense : processes each line    
*/

package parsertree;

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
import static parsertree.Word.SearchForAdvmodOfVerb;
import static parsertree.Word.numberOfIndex;
import static parsertree.Word.relIsIndirectObject;

public class Sentence {
public  static Word[] wordPre=new Word[150];
public static Word[] word=new Word[150];
static Quotation[] quotations=new Quotation[10]; 
static Verb[] verbs=new Verb[40]; 
static NamedEntity[] namedEntity=new NamedEntity[150]; 
static int CounterVerbs;
public static int quotationCounter;   
public static int wordsNumber;
static int row=0; 
static public String line;
private final TokenizerFactory<CoreLabel> tokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), "invertible=true");
static LexicalizedParser parser = LexicalizedParser.loadModel("dependencyviewer-0.1.1/edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
public static List<TypedDependency> tdl1;
public  static List<TypedDependency> tdl;
public static List<TypedDependency> processSentense(String line) throws Exception{  
    Initialization();
    prestopRemover(line);
    line=stopRemover(line);
    CounterVerbs=0;
    Tokenize1Pre(line);
    NamedEntityRecognition.main(line);
    String replacedSent=RecognizeLinkedWordPositions();
    replacedSent=replaceSentenceConsideringTagmeLinks(line);
    PrintDependencies(tdl); 
    List<TypedDependency> tdl=ParseLine(replacedSent);
    CounterVerbs=0;
    PrintDependencies(tdl);    
    Tokenize1(replacedSent,tdl);
    return tdl;
    } 
public Sentence(String line){//parses the line
     tdl=ParseLine(line);
}
public static void Initialization(){
                   quotationCounter=0;
                   wordsNumber=0; 
                   CounterVerbs=0; 
                   for(int i=0;i<150;i++){
                       word[i]=new Word("","",0,0);
                       wordPre[i]=new Word("","",0,0);
                    }
                   for(int i=0;i<10;i++)
                     quotations[i]=new Quotation();
                   for(int j=0;j<40;j++)
                      verbs[j]=new Verb("",0,"");
   }

public Tree pars(String line) {// creates the parser tree               
        List<CoreLabel> tokens = tokenize(line);
        Tree tree = parser.apply(tokens);
        return tree;
    }//parse the line
public static List<TypedDependency> ParseLine(String line) {
    Tree parse;   
    TokenizerFactory<CoreLabel> tokenizerFactory =  
    PTBTokenizer.factory(new CoreLabelTokenFactory(), ""); 
    List<CoreLabel> rawWords2 =  tokenizerFactory.getTokenizer(new StringReader(line)).tokenize(); 
    parse = parser.apply(rawWords2); 
    TreebankLanguagePack tlp = new PennTreebankLanguagePack(); 
    GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory(); 
    GrammaticalStructure gs = gsf.newGrammaticalStructure(parse); 
    List<TypedDependency> tdl = gs.typedDependenciesCCprocessed(); 
    return tdl;
   }

public static void PrintDependencies(List<TypedDependency> tdl){//print dependencies between words in input line
   
    int index=0;
    for (TypedDependency thelp : tdl) { 
            System.out.print(tdl.get(index).reln());
            System.out.print('('+tdl.get(index).gov().value()+','+tdl.get(index).dep().value()+')');
    System.out.println();
            index++;
    }
    System.out.println();
  }
public static void PositionOfWords(String line){//finds the position of words in input line
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

public static void Tokenize1Pre(String line){ /*tockenize before named entity recognition*/
    Sentence parser = new Sentence(line);
    Tree tree = parser.pars(line);  
    List<Tree> leaves = tree.getLeaves();
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
              else if(
                      w.contentEquals("!")|| w.contentEquals(";")|| w.contentEquals("\\")||
                      w.contentEquals(".")|| w.contentEquals("?")|| w.contentEquals(":")||
                      w.contentEquals("-")|| w.contentEquals("n't")|| w.contentEquals("'s")||
                      w.contentEquals("'m")|| w.contentEquals("'re")|| w.contentEquals("'ve")|| w.contentEquals("'d")
                      )
                     ws= wordPre[k-1].POSwordEnd+1;
              else if(w.contentEquals("``")){
                    doubleQutation++;    
                    if(doubleQutation==1)
                         ws= wordPre[k-1].POSwordEnd+2;
                    else{
                         ws= wordPre[k-1].POSwordEnd+1;
                         doubleQutation=0;
                     }
              }
              else if(w.contentEquals("`")){
                    singleQutation++;    
                    if(singleQutation==1)
                         ws= wordPre[k-1].POSwordEnd+2;
                    else{
                         ws= wordPre[k-1].POSwordEnd+1;
                     singleQutation=0;
                    }
              }
          else
               ws= wordPre[k-1].POSwordEnd+2;
          }   
          else
             ws= 1;  
        we= w.length()+ws-1;
        wt= parent.label().value();
        if(
            (parent.label().value().equals("VB"))||(parent.label().value().equals("VBN"))||
            (parent.label().value().equals("VBP"))||(parent.label().value().equals("VBD"))||
            (parent.label().value().equals("VBZ"))
          ){   
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
                if(sw==0){   
                       verbs[CounterVerbs].verbs=leaf.label().value();
                       verbs[CounterVerbs].verbsRoles=parent.label().value();
                       verbs[CounterVerbs].verbsPosition=k;
                       CounterVerbs++;
                }
             }  
         }
        System.out.println(w+","+wt+","+ws+","+we+",");
        wordPre[k].POSword= w;
        wordPre[k].POStagword= wt;
        wordPre[k].POSwordStart=ws;
        wordPre[k].POSwordEnd=we;
      
        k++;
    }
   wordsNumber=k; 
   }
public static void Tokenize1(String line,List<TypedDependency> tdl){//repeated tokenize  after named entity  recognition
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
         if(
             wordPre[k].POSword.contentEquals("\\")||wordPre[k].POSword.contentEquals(".")||
             wordPre[k].POSword.contentEquals("?")
           )
             wordPre[k].POSwordStart= wordPre[k-1].POSwordEnd+1;
         else
             wordPre[k].POSwordStart= wordPre[k-1].POSwordEnd+2;
             wordPre[k].POSwordEnd= wordPre[k].POSword.length()+wordPre[k].POSwordStart-1;
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
     k++;
    }
wordsNumber=k; 
   }
private List<CoreLabel> tokenize(String line) { //tockenize the line
        Tokenizer<CoreLabel> tokenizer =tokenizerFactory.getTokenizer(new StringReader(line));    
        return tokenizer.tokenize();
}

public static void prestopRemover(String line){
       Tokenize1Pre(line);
       RecognizeQuotationPositions();
       fullUpWithAllBetweenQuotation();
       RecognizeQuotationPhrases(line);
       RecognizeIsThereAVerbInQuotation(line); 
}
public static String stopRemover(String line){
     line=line.replaceAll("\"", "");
     line = line.replaceAll("[{}]","");
     line=line.replaceAll("\''", "");
     line=line.replaceAll("``", "");
     line=line.replaceAll("\\*","");
     line=line.replaceAll("^","");
     line=line.replaceAll("  "," ");
     line=line.replace("?","");
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
 return counterEquals;
   }
public static int VerbToVerb(int verbNum){
       if(verbs[verbNum].verbsPosition>2)   
         if (word[verbs[verbNum].verbsPosition-1].POSword.matches("to")|| word[verbs[verbNum].verbsPosition-2].POSword.matches("to"))
             return 1;
          else return 0;
       else return 0;
   }
public static int NotCopularVerb(String str){ //--------------------------------if verb is am, is , was,.....---------------------------------
    String[] str1; String dep1,rel1,gov1; 
    int sw1=1;
    if(
            str.equals("was")||str.equals("were")||
            str.equals("am")||str.equals("is")||
            str.equals("are")||str.equals("had")||
            str.equals("has")||str.equals("have")||
            str.equals("did")||str.equals("does")
       ) {
           for(int j=0;j<Sentence.CounterVerbs;j++){
           int index1=0;
           for (TypedDependency t1 : tdl){ //may be verb is copular verb such as did.....serve.verb did should be deleated
                     str1=tdl.get(index1).dep().toString().split("/");
                     dep1=str1[0];
                     str1=tdl.get(index1).reln().toString().split("/");
                     rel1=str1[0];
                     str1=tdl.get(index1).gov().toString().split("/");
                     gov1=str1[0];
                     if (dep1.equals(Sentence.verbs[j].verbs)& gov1.equals(str) & rel1.equals("aux")){
                      index1++;sw1=1;return sw1;
                     }
                    else
                    index1++;
                }
             }      
       sw1=0; }
   return sw1;//0 not main verb,1 main verb
   }
public static int BeCopularMainVerb(String str,int j ){ //--------------------------------if verb is am, is , was,.....---------------------------------
    String[] str1; String dep1,rel1,gov1; 
    int sw1=1;
    if(
            (
              str.equals("was")||str.equals("were")||str.equals("am")||str.equals("is")||str.equals("are")||
              str.equals("had")||str.equals("has")||str.equals("have")||str.equals("did")||str.equals("does")
            ) 
            & 
            (
              !(Sentence.word[Sentence.verbs[j].verbsPosition+1].POStagword.equals("VBN"))& 
              !(Sentence.word[Sentence.verbs[j].verbsPosition+1].POStagword.equals("VB"))&
              !(Sentence.word[Sentence.verbs[j].verbsPosition+1].POStagword.equals("VBP"))&
              !(Sentence.word[Sentence.verbs[j].verbsPosition+1].POStagword.equals("VBD"))&
              !(Sentence.word[Sentence.verbs[j].verbsPosition+1].POStagword.equals("VBZ"))
            )
     ){
        int index1=0;
        for (TypedDependency t1 : tdl){
                     str1=tdl.get(index1).dep().toString().split("/");
                     dep1=str1[0];
                     str1=tdl.get(index1).reln().toString().split("/");
                     rel1=str1[0];
                    if (dep1.equals(str)& ((rel1.equals("ccomp"))||rel1.equals("auxpass")||(rel1.equals("aux"))))
                    { index1++;sw1=0;return sw1;}
                    else
                    index1++;
                }
              for(j=0;j<Sentence.CounterVerbs;j++){
                  index1=0;
              for (TypedDependency t1 : tdl){
                     str1=tdl.get(index1).dep().toString().split("/");
                     dep1=str1[0];
                     str1=tdl.get(index1).reln().toString().split("/");
                     rel1=str1[0];
                     str1=tdl.get(index1).gov().toString().split("/");
                     gov1=str1[0];
                     if (dep1.matches(Sentence.verbs[j].verbs)& gov1.matches(str) & rel1.matches("aux"))
                    { index1++;sw1=0;return sw1;}
                    if (gov1.matches(Sentence.verbs[j].verbs)& dep1.matches(str) & (rel1.matches("aux")||rel1.matches("auxpass")))
                    { index1++;sw1=0;return sw1;}
                    else
                    index1++;
                }
                }
             }
            else sw1=0;
   return sw1;//0 not main verb,1 main verb
   }
public static int AreRelatedTogether(int current,int counter){
        int index2=0;
        String[] str1;
        int gov2Index,dep2Index,k=0;
        String rel2,gov2,dep2;   
        dep2="";rel2="";
        for (TypedDependency t2 : tdl){
                                        str1=tdl.get(index2).dep().toString().split("/");
                                        dep2=str1[0];
                                        str1=tdl.get(index2).reln().toString().split("/");
                                        str1=tdl.get(index2).gov().toString().split("/");
                                        gov2=str1[0]; 
                                        if(
                                                (gov2.matches(word[current].POSword)& dep2.matches(word[counter].POSword))
                                                ||
                                                (dep2.matches(word[current].POSword)& gov2.matches(word[counter].POSword))
                                          )
                                    
                               return 1;
                               index2++;  
                            }//end for
       
       return 0;
   }

public static int FindRerenceOfThat(int index){
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

public static int IsAQuotationPhrase(String rel,int govIndex,int depIndex){
                for(int i=0;i<Sentence.quotationCounter;i++){
                   if(Sentence.quotations[i].rel.matches(rel) & Sentence.quotations[i].gov.matches( Sentence.word[govIndex].POSword) & Sentence.quotations[i].dep.matches( Sentence.word[depIndex].POSword) & Sentence.quotations[i].sw.matches("0")){
                   return 1;
                }
                }
    return 0;
}
public static void RecognizeQuotationPositions(){
   for(int i=1;i<wordsNumber;i++){
     if(wordPre[i].POSword.matches("``"))
         quotations[quotationCounter].startPos=i;
     else if(wordPre[i].POSword.matches("''")){
         quotations[quotationCounter].endPos=i;
         quotationCounter++;
     }
   }
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
                             if(dep1Index>=quotations[j].startPos & dep1Index<=quotations[j].endPos
                                & gov1Index>quotations[j].startPos &  gov1Index<=quotations[j].endPos ){
                                   quotations[j].rel=rel1;
                                   quotations[j].gov=gov1;
                                   quotations[j].dep=dep1;
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
                             if(dep1Index>=quotations[j].startPos & dep1Index<=quotations[j].endPos
                              & gov1Index>=quotations[j].startPos & gov1Index<=quotations[j].endPos ){
                                   quotations[j].sw="1";
                                                         
                             }
                          }                  
                     }
                    index1++;
                   
                }
        
    }   
public static void fullUpWithAllBetweenQuotation(){
     for(int i=0;i<quotationCounter;i++){
        quotations[i].quotation="";
         for(int j=quotations[i].startPos;j<=quotations[i].endPos;j++)
          quotations[i].quotation= quotations[i].quotation+" "+wordPre[j].POSword;
     
     }
    }

public static String RecognizeLinkedWordPositions(){
   int WordsPosInreplacedSentence=1;
   int i;
    for(int j=1;j<wordsNumber;j++){
       for( i=0;i<NamedEntityRecognition.NumTagMeLinks;i++){
           if(wordPre[j].POSwordStart>=Integer.parseInt(NamedEntityRecognition.namedEntity[i].phraseStart)+1 &wordPre[j].POSwordEnd<=Integer.parseInt(NamedEntityRecognition.namedEntity[i].phraseEnd) ){
               wordPre[WordsPosInreplacedSentence].phraseTagmeLinksRecognizer=1;
               j=j+Integer.parseInt(NamedEntityRecognition.namedEntity[i].numberOfWords)-1;
               break;
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
             for(int j=firstToken;j<startToken;j++)
                 replacedSent=replacedSent+line.charAt(j);
             if(sw==1)
                  for(int j=Integer.parseInt(NamedEntityRecognition.namedEntity[i-1].phraseEnd);j<Integer.parseInt(NamedEntityRecognition.namedEntity[i].phraseEnd);j++){
                     replacedSent=replacedSent+line.charAt(j);
              }
              if(sw==0)
              if(!NamedEntityRecognition.namedEntity[i].numberOfWords.matches("1")){
                replacedSent=replacedSent+"T1"+numberOfIndex(i); 
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

}
