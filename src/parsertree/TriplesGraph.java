/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsertree;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.trees.TypedDependency;
import java.io.IOException;
import Jwi.Main;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.List;
import static parsertree.Sentence.AreRelatedTogether;
import static parsertree.Sentence.NotDependencyBetwweenToVerbs;
import static parsertree.Sentence.VerbToVerb;
import static parsertree.Sentence.govIsMatchedWhithVerbs;
import static parsertree.Sentence.tdl;
import static parsertree.Sentence.verbs;
import static parsertree.Sentence.wordsNumber;
import static parsertree.Verb.BeCopularMainVerb;
import static parsertree.Verb.NotCopularVerb;
import static parsertree.Word.BeCapableOfMemberOfNounRelation;
import static parsertree.Word.BeCapableOfMemberOfNounRelationSecondArguman;
import static parsertree.Word.ComparativeAdjectiveIsQuantityAdjective;
import static parsertree.Word.FindRerenceOfThat;
import static parsertree.Word.IsAQuotationPhrase;
import static parsertree.Word.IsCapital;
import static parsertree.Word.IsGenitivePossSubject;
import static parsertree.Word.IsNoun;
import static parsertree.Word.IsPossessiveAdjective;
import static parsertree.Word.IsProbableGenitiveType;
import static parsertree.Word.SuperlativeAdjectiveIsQuantityAdjective;
import static parsertree.Word.govIsSomeSpecialWords;
import static parsertree.Word.relIsIndirectObject;

/**
 *
 * @author mywin
 */
public class TriplesGraph {
    public static int EndNum;
    public static int minimalEndNum;
    static Triple[] triple=new Triple[60];
     static Triple[] minimalTriple=new Triple[60];
     //static Sentence sentence=new Sentence();
        public static int  RightLimitOfWord, LeftLimitOfWord;
       public static String  IndirectObject;
    public static int  haveIndirectObject;
    public static String  DirectObject;
    public static int  haveDirectObject;
         public static int [] prepSequence=new int[15];// what is it?

     public static String [] subjectFillers=new String [15];
    public static int subjectFillersNumber;
    public static int [] subjectFillersPositions=new int [15];
    public static String [] objectFillers=new String [15];
    public static String [] objectFillersPrep=new String [15];
    public static int [] objectFillersPositions=new int [15];
    public static int [] directObjectFillersPositions=new int [15];
    public static int objectFillersNumber;
    public static void convertion(){ 
         for(int j=0;j<=EndNum;j++){
                if(triple[j].subject.matches("Where")||triple[j].subject.matches("where"))
                      triple[j].subject="place";
                else
                if(triple[j].subject.matches("Who")||triple[j].subject.matches("who"))
                      triple[j].subject="person"; 
                else
                if(triple[j].subject.matches("When")||triple[j].subject.matches("When"))
                      triple[j].subject="time"; 
                else
                if(triple[j].subject.matches("What")||triple[j].subject.matches("what"))
                      triple[j].subject="thing";
                
                                     
                if(triple[j].object.matches("Where")||triple[j].object.matches("where"))
                      triple[j].object="place";
                else
                if(triple[j].object.matches("Who")||triple[j].object.matches("who"))
                      triple[j].object="person"; 
                else
                if(triple[j].object.matches("When")||triple[j].object.matches("when"))
                      triple[j].object="time"; 
                else
                if(triple[j].object.matches("What")||triple[j].object.matches("what"))
                      triple[j].object="thing";          
        }

              for(int j=0;j<=minimalEndNum;j++){
                if(minimalTriple[j].subject.matches("where")||minimalTriple[j].subject.matches("Where"))
                      minimalTriple[j].subject="place";
                else
                if(minimalTriple[j].subject.matches("who")||minimalTriple[j].subject.matches("Who"))
                      minimalTriple[j].subject="person"; 
                else
                if(minimalTriple[j].subject.matches("when")||minimalTriple[j].subject.matches("When"))
                      minimalTriple[j].subject="time"; 
                else
                if(minimalTriple[j].subject.matches("what")||minimalTriple[j].subject.matches("What"))
                      minimalTriple[j].subject="thing";
                
                
                if(minimalTriple[j].object.matches("where")||minimalTriple[j].object.matches("Where"))
                      minimalTriple[j].object="place";
                else
                if(minimalTriple[j].object.matches("who")||minimalTriple[j].object.matches("Who"))
                      minimalTriple[j].object="person"; 
                else
                if(minimalTriple[j].object.matches("when")||minimalTriple[j].object.matches("When"))
                      minimalTriple[j].object="time"; 
                else
                if(minimalTriple[j].object.matches("what")||minimalTriple[j].object.matches("What"))
                      minimalTriple[j].object="thing"; 
                  
        }
     }
    public static void TriplesReview(){
                  // SubjObjNull();
                  FullNullObject();
                  FullNullObjectMinimal();
                  
                   //CommaBetweenTwoNoun(line);//developed a filing system that enables judges, lawyers 
                   //VerbT0Verb(line);
    }
   public static void GenetiveFunction1(String sentence ) throws IOException{
       String[] str1;int dep1Index,gov2Index,dep2Index,k=0;
       String rel1,dep1,gov1,rel2,gov2,dep2;   
       int Startnum=minimalEndNum;
       int Endnum=0;
       dep2="";
       rel2="";
       int sequenceNum=-1;
       dep2Index=0;
       int index1=0;
       int step=1;
       //List<TypedDependency> tdl=null;;
       for (TypedDependency t1 : tdl){
                     str1=tdl.get(index1).dep().toString().split("/");
                     dep1Index=tdl.get(index1).dep().index();
                     dep1=str1[0];
                     
                     str1=tdl.get(index1).reln().toString().split("/");
                     rel1=str1[0];
                     str1=tdl.get(index1).gov().toString().split("/");
                     gov1=str1[0];
                     
                     if(IsProbableGenitiveType(gov1,rel1,dep1)==1 )
                     {sequenceNum=-1;
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
                                    (gov2.matches(dep1) & (gov2Index==dep1Index))& govIsSomeSpecialWords(gov2)==0&(!relIsIndirectObject(rel2).matches("")||rel2.matches("dep"))& IsPossessiveAdjective(dep2)==0 )||
                                    (rel1.matches("cop")&!relIsIndirectObject(rel2).matches("") &gov2.matches(gov1) )
                                    
                                    ){
                                  k=1;
                                  /*if(step==1){
                                  String serializedClassifier = "F:\\Question-answer\\implementation\\myprog\\stanfordLib\\classifiers\\english.all.3class.distsim.crf.ser.gz";
                                  classifier = CRFClassifier.getClassifierNoExceptions(serializedClassifier);
                                  model = FileManager.get().loadModel("F:\\Question-answer\\implementation\\myprog\\stanfordLib\\SUMO.owl-master\\SUMO.owl");
                                  }*/
                               //   if(phraseTagmeLinksRecognizer[dep1Index]!=1){
                                      step++;
                                      int first=minimalEndNum;
                                     
                                      
                                      MakeGenitiveRelation(gov2Index,dep2Index,rel2);
                                      if(minimalEndNum>first){
                                      sequenceNum=sequenceNum+2;
                                      minimalTriple[minimalEndNum-1].tripleType="Genitive relation:Function1";
                                      prepSequence[sequenceNum]=gov2Index;
                                      prepSequence[sequenceNum+1]=dep2Index;
                                      
                                      break;
                                      }
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
                     if(sequenceNum>0)
                     if(IsCapital(Sentence.word[prepSequence[sequenceNum+1]].POSwordStart)==0)
                         minimalEndNum=Startnum;  
                     else 
                         Startnum=minimalEndNum;
                     }//end if(IsProbable......)
                     index1++;
                   
                }
}
    public static void GenetiveFunction3(String sentence ){
    String[] str1;int dep1Index,gov2Index,dep2Index,k=0;
       String rel1,dep1,gov1,rel2,gov2,dep2;   
       int Startnum=minimalEndNum;
       int Endnum=0;
       dep2="";
       rel2="";
       int sequenceNum=-1;
       dep2Index=0;
       int index1=0;
       int step=1;
                index1=0;   
                //List<TypedDependency> tdl=null;;
                 for (TypedDependency t2 : tdl){
                            sequenceNum=-1;       
                            k=0;
                            str1=tdl.get(index1).dep().toString().split("/");
                            dep1=str1[0];
                            dep1Index=tdl.get(index1).dep().index();
                            str1=tdl.get(index1).reln().toString().split("/");
                            rel1=str1[0];
                            str1=tdl.get(index1).gov().toString().split("/");
                            int gov1Index=tdl.get(index1).gov().index();
                            gov1=str1[0]; 
                            if(rel1.matches("nmod:poss")){
                            int j=0; 
                            for(j=0;j<minimalEndNum;j++)
                             {
                              if(minimalTriple[j].subject.contains(dep1)& minimalTriple[j].object.contains(gov1) )
                               break;
                             }
                             if(j==minimalEndNum){
                                      sequenceNum=sequenceNum+2;
                                      prepSequence[sequenceNum]=gov1Index;
                                      prepSequence[sequenceNum+1]=dep1Index;
                                      if(IsCapital(Sentence.word[prepSequence[sequenceNum+1]].POSwordStart)==1){
                    
                               minimalTriple[minimalEndNum].subject=dep1;minimalTriple[minimalEndNum].subjectStart=dep1Index; minimalTriple[minimalEndNum].subjectEnd=dep1Index;extendToNounGroupMinimal(0,minimalEndNum);
                              minimalTriple[minimalEndNum].predicate="has";minimalTriple[minimalEndNum].predicateStart=0;
                              minimalTriple[minimalEndNum].object=gov1;minimalTriple[minimalEndNum].objectStart=gov1Index;minimalTriple[minimalEndNum].objectEnd=gov1Index; extendToNounGroupMinimal(2,minimalEndNum);
                              minimalTriple[minimalEndNum].tripleType="Genitive relation: Poss,Function3";
                              minimalEndNum++;
                                      }
                                      }        
                            
                            }
                            if(!relIsIndirectObject(rel1).matches("")){
                            int j=0; 
                            for(j=0;j<minimalEndNum;j++)
                             {
                              if(minimalTriple[j].subject.contains(dep1)& minimalTriple[j].object.contains(gov1) )
                               break;
                             }
                             if(j==minimalEndNum){
                                      sequenceNum=sequenceNum+2;
                                      prepSequence[sequenceNum]=gov1Index;
                                      prepSequence[sequenceNum+1]=dep1Index;
                                      if(IsCapital(Sentence.word[prepSequence[sequenceNum+1]].POSwordStart)==1){
                    
                               minimalTriple[minimalEndNum].subject="?s";minimalTriple[minimalEndNum].subjectStart=0; minimalTriple[minimalEndNum].subjectEnd=0;
                              minimalTriple[minimalEndNum].predicate=gov1;;minimalTriple[minimalEndNum].predicateStart=gov1Index;extendToNounGroupMinimal(1,minimalEndNum);
                              minimalTriple[minimalEndNum].object=dep1;minimalTriple[minimalEndNum].objectStart=dep1Index;minimalTriple[minimalEndNum].objectEnd=dep1Index; extendToNounGroupMinimal(2,minimalEndNum);
                              minimalTriple[minimalEndNum].tripleType="Genitive relation:(missedsubject),other nmods: Function3 ";
                              minimalEndNum++;
                                      }
                                      }        
                            
                            }                               
                            index1++;      
                            }//end for
                 minimalEndNum--;
    } 
      
  

    public static void SuperlativeRelations (){
        String predicate="";
        String operator="";
       for(int i=1;i<wordsNumber;i++){
          if(Sentence.word[i].POStagword.matches("JJS")||Sentence.word[i].POStagword.matches("RBS")){
                 String Adjective=SuperlativeAdjectiveIsQuantityAdjective(Sentence.word[i].POSword);
                   if(!Adjective.matches("")){//is quantity adjective
                        String str[]=Adjective.split(" ");
                        predicate=str[3];
                        operator=str[4];
                        minimalEndNum++;
                        int counter=i+1;
                        while(Sentence.word[counter].POStagword.matches("RB"))
                            counter++;
                        minimalTriple[minimalEndNum].subject=Sentence.word[counter].POSword;minimalTriple[minimalEndNum].subjectStart=counter;minimalTriple[minimalEndNum].subjectEnd=counter;
                         minimalTriple[minimalEndNum].subject=extendToNounGroupForAWordDirectionMinimal(counter,Sentence.word[counter].POSword,2);
                        minimalTriple[minimalEndNum].predicate=predicate;//triple[i].predicate+' '+Sentence.word[[verbs[j].verbsPosition+1]].POSword;
                        minimalTriple[minimalEndNum].tripleType="quantity Superlative relation:  ";
                        //CompSuper=1;
                        minimalTriple[minimalEndNum].object="n1";
                        minimalTriple[minimalEndNum].typeCondition="order by";
                        if(operator.matches("higher"))
                        minimalTriple[minimalEndNum].sortTypeCondition="ASC";
                        else
                        minimalTriple[minimalEndNum].sortTypeCondition="DESC";    
                        minimalTriple[minimalEndNum].elementCondition="n1";
                  
                        minimalEndNum++;

                   }
                   else{minimalEndNum++;
                        int counter=i+1;
                        while(Sentence.word[counter].POStagword.matches("RB"))
                            counter++;
                        minimalTriple[minimalEndNum].subject=Sentence.word[counter].POSword;minimalTriple[minimalEndNum].subjectStart=counter;minimalTriple[minimalEndNum].subjectEnd=counter;
                        minimalTriple[minimalEndNum].subject=extendToNounGroupForAWordDirectionMinimal(counter,Sentence.word[counter].POSword,2);
                        minimalTriple[minimalEndNum].predicate="is";//triple[i].predicate+' '+Sentence.word[[verbs[j].verbsPosition+1]].POSword;
                        minimalTriple[minimalEndNum].tripleType="qualify Superlative relation:  ";
                        //CompSuper=1;
                        minimalTriple[minimalEndNum].object=Sentence.word[i].POSword;
                        minimalEndNum++;
                   }//is not quantity adjective
          }
       }
   
    }
  public static void ComparativeRelations (){
    int j;
    String predicate="";
    String operator="";
    for(j=0;j<Sentence.CounterVerbs;j++){
       if(verbs[Sentence.CounterVerbs].verbsPosition+1<wordsNumber)
        if(Sentence.wordPre[Sentence.word[verbs[j].verbsPosition+1].POSwordStart].POStagword.matches("RBR")||Sentence.wordPre[Sentence.word[verbs[j].verbsPosition+1].POSwordStart].POStagword.matches("JJR")){
        String Adjective=ComparativeAdjectiveIsQuantityAdjective(Sentence.word[verbs[j].verbsPosition+1].POSword);
        if(!Adjective.matches("")){//is quantity adjective
           String str[]=Adjective.split(" ");
           predicate=str[3];
           operator=str[4];
            for(int i=0;i<=minimalEndNum;i++){
            if(minimalTriple[i].predicate.matches(verbs[j].verbs)){
                      
                minimalEndNum++;
                minimalTriple[minimalEndNum].subject=minimalTriple[i].subject;minimalTriple[minimalEndNum].subjectStart=minimalTriple[i].subjectStart;minimalTriple[minimalEndNum].subjectEnd=minimalTriple[i].subjectEnd;
                minimalTriple[minimalEndNum].predicate=predicate;//triple[i].predicate+' '+Sentence.word[[verbs[j].verbsPosition+1]].POSword;
                minimalTriple[minimalEndNum].tripleType="quantity Comparative relation:  ";
                //CompSuper=1;
                minimalTriple[i].tripleState=0;
                if(verbs[j].verbsPosition+3<wordsNumber){
                if(Sentence.word[verbs[j].verbsPosition+3].POStagword.matches("CD")){
                    minimalTriple[minimalEndNum].object="n1";//POSword[verbs[j].verbsPosition+3];
                    //triplesPosition[EndNum][3]=verbs[j].verbsPosition+3;
                   // triplesPosition[EndNum][4]=verbs[j].verbsPosition+3;
                   //extendToNounGroup(2,EndNum,sentence);
                    minimalTriple[minimalEndNum].typeCondition="n1";
                    minimalTriple[minimalEndNum].sortTypeCondition=operator;
                    minimalTriple[minimalEndNum].elementCondition=Sentence.word[verbs[j].verbsPosition+3].POSword;
                   
                }else{//is not a number
                     minimalTriple[minimalEndNum].object="n1";
                     minimalEndNum++;
                     minimalTriple[minimalEndNum].subject=Sentence.word[verbs[j].verbsPosition+3].POSword;minimalTriple[minimalEndNum].subjectStart=verbs[j].verbsPosition+3;minimalTriple[minimalEndNum].subjectEnd=verbs[j].verbsPosition+3; extendToNounGroupMinimal(0,minimalEndNum);
                     minimalTriple[minimalEndNum].predicate=predicate;//triple[i].predicate+' '+Sentence.word[[verbs[j].verbsPosition+1]].POSword;
                     minimalTriple[minimalEndNum].object="n2";
                     minimalTriple[minimalEndNum].tripleType="quantity Comparative relation:  ";
                     //CompSuper=1;
                     minimalTriple[minimalEndNum].typeCondition="n1";
                     minimalTriple[minimalEndNum].sortTypeCondition=operator;
                     minimalTriple[minimalEndNum].elementCondition="n2";
                }
              }          
          
              
        }
        }
        
        
        
       //--------------
           
           
           
           
        }
        else{//is quality Adjective
            
            
      
        
        for(int i=0;i<=minimalEndNum;i++){
        if(minimalTriple[i].predicate.matches(verbs[j].verbs)){
          
      
        minimalTriple[minimalEndNum].subject=minimalTriple[i].subject;minimalTriple[minimalEndNum].subjectStart=minimalTriple[i].subjectStart;minimalTriple[minimalEndNum].subjectEnd=minimalTriple[i].subjectEnd;
          int sw=0;
          //CompSuper=1;
          minimalTriple[minimalEndNum].tripleType="quality Comparative relation:  ";
          if(verbs[j].verbsPosition+2<wordsNumber){
              if(Sentence.word[verbs[j].verbsPosition+1].POSword.matches("than")){
               minimalTriple[minimalEndNum].predicate=minimalTriple[i].predicate+' '+Sentence.word[verbs[j].verbsPosition+1].POSword;   
               minimalTriple[minimalEndNum].object=Sentence.word[verbs[j].verbsPosition+2].POSword;
              minimalTriple[minimalEndNum].objectStart=verbs[j].verbsPosition+2;
              minimalTriple[minimalEndNum].objectEnd=verbs[j].verbsPosition+2;
               extendToNounGroupMinimal(2,minimalEndNum);
               minimalEndNum++;
               sw=1;
              }
             }
              if(sw==0){
               minimalTriple[minimalEndNum].predicate=minimalTriple[i].predicate;   
               minimalTriple[minimalEndNum].object=Sentence.word[verbs[j].verbsPosition+1].POSword;
              minimalTriple[minimalEndNum].objectStart=verbs[j].verbsPosition+1;
              minimalTriple[minimalEndNum].objectEnd=verbs[j].verbsPosition+1;
               extendToNounGroupMinimal(2,minimalEndNum);
               minimalEndNum++;
              }
              break;
         }
                  
        }
             
        }
        }
        
        /*
        for(int i=0;i<=minimalEndNum;i++){
        if(minimalTriple[i].predicate.matches(verbs[j])){
          minimalEndNum++;
          minimalTriple[minimalEndNum].subject=minimalTriple[i].subject;minimalTriple[minimalEndNum].subjectStart=minimalTriple[i].subjectStart;minimalTriple[minimalEndNum].subjectEnd=minimalTriple[i].subjectEnd;
          minimalTriple[minimalEndNum].predicate=minimalTriple[i].predicate+' '+Sentence.word[[verbs[j].verbsPosition+1]].POSword;
          minimalTriple[minimalEndNum].tripleType="Comparative relation:  ";
          if(verbs[j].verbsPosition+3<wordsNumber)
          if(POStagword[verbs[j].verbsPosition+3].matches("CD")){
              minimalTriple[minimalEndNum].object=POSword[verbs[j].verbsPosition+3];
             minimalTriple[minimalEndNum].objectStart=verbs[j].verbsPosition+3;
             minimalTriple[minimalEndNum].objectEnd=verbs[j].verbsPosition+3;
              extendToNounGroupMinimal(2,minimalEndNum,sentence);
          }
          else
          {    minimalTriple[minimalEndNum].object=POSword[verbs[j].verbsPosition+3];
              minimalTriple[minimalEndNum].objectStart=verbs[j].verbsPosition+3;
              minimalTriple[minimalEndNum].objectEnd=verbs[j].verbsPosition+3;
               extendToNounGroupMinimal(2,minimalEndNum,sentence);
          }
          
                  
        }
        }*/
       //--------------
        
      
      }//ens else is quantity Adjective
    }//counter verbs
  
    //
    public static void extendToLeftCopular(int i,int triplenum,int counter){
               
              while(
                      !(Sentence.word[counter].POStagword.equals("NN")
                      ||Sentence.word[counter].POStagword.equals("NNS")
                      ||Sentence.word[counter].POStagword.equals("NNP")
                      ||Sentence.word[counter].POStagword.equals("JJ")
                      ||Sentence.word[counter].POStagword.equals("POS")
                      ||Sentence.word[counter].POStagword.equals("CD")
                      ||Sentence.word[counter].POStagword.equals("PRP$")
                      ||Sentence.word[counter].POStagword.equals("PRP")
                      || IsNoun(counter)==1
                      ||Sentence.word[counter].POStagword.equals("DT")
                       //||Sentence.word[counter].POStagword.equals('"')
                       ||Sentence.word[counter].POStagword.equals("CC")
                      ||Sentence.word[counter].POStagword.equals("NNPS")
                      ))
              if (counter!=1)counter--;else break;
              
              if(i==0)
              {   triple[triplenum].subject=Sentence.word[counter].POSword;
                  triple[triplenum].subjectStart=counter;
                  triple[triplenum].subjectEnd=counter;}
               if(i==2)
               {  triple[triplenum].object=Sentence.word[counter].POSword;
                  triple[triplenum].objectEnd=counter;
                  triple[triplenum].objectStart=counter;}
              if (counter!=1) counter--;
             /* while(
                      (Sentence.word[counter].POStagword.equals("NN")||
                     // Sentence.word[counter].POStagword.equals("VBG")||
                      Sentence.word[counter].POStagword.equals("NNS")||Sentence.word[counter].POStagword.equals("CD")||Sentence.word[counter].POStagword.equals("IN")||
                       Sentence.word[counter].POStagword.equals("NNP")||Sentence.word[counter].POStagword.equals("JJ")||Sentence.word[counter].POStagword.equals("JJS")||Sentence.word[counter].POStagword.equals("PRP$")
                     ||Sentence.word[counter].POStagword.equals("POS")||Sentence.word[counter].POStagword.equals("DT")
                      //||Sentence.word[counter].POStagword.equals('"')
                       ||Sentence.word[counter].POStagword.equals("CC")
                      ||Sentence.word[counter].POStagword.equals("NNPS")
                        ||Sentence.word[counter].POStagword.equals("PRP")
                      )&(counter!=1))*/
             while(govIsMatchedWhithVerbs(Sentence.word[counter].POSword)==0 & counter!=1& !Sentence.word[counter].POSword.matches(","))
                 {
                    if(i==0)
                       triple[triplenum].subject=Sentence.word[counter].POSword+" "+triple[triplenum].subject;
                       triple[triplenum].subjectStart=counter;
                    if(i==2)
                       triple[triplenum].object=Sentence.word[counter].POSword+" "+triple[triplenum].object;
                       triple[triplenum].objectStart=counter;
                     
                     if (counter!=1) counter--;else break;
                 }
   }
   public static void extendToLeftCopularMinimal(int i,int triplenum,int counter){
               
              while(
                      !(Sentence.word[counter].POStagword.equals("NN")
                      ||Sentence.word[counter].POStagword.equals("NNS")
                      ||Sentence.word[counter].POStagword.equals("NNP")
                      ||Sentence.word[counter].POStagword.equals("JJ")
                      ||Sentence.word[counter].POStagword.equals("POS")
                      ||Sentence.word[counter].POStagword.equals("PRP$")
                      ||Sentence.word[counter].POStagword.equals("PRP")
                      //||Sentence.word[counter].POStagword.equals("VBG")
                      ||Sentence.word[counter].POStagword.equals("NNPS")
                      ||Sentence.word[counter].POStagword.equals("CD")
                      ))
              if (counter!=1)counter--;else break;
              if(i==0)
              {   minimalTriple[triplenum].subject=Sentence.word[counter].POSword;
                  minimalTriple[triplenum].subjectStart=counter;
                  minimalTriple[triplenum].subjectEnd=counter;}
               if(i==2)
               {  minimalTriple[triplenum].object=Sentence.word[counter].POSword; 
                  minimalTriple[triplenum].objectEnd=counter;
                  minimalTriple[triplenum].objectStart=counter;}
              if (counter!=1) counter--;
              while(
                      (Sentence.word[counter].POStagword.equals("NN")||
                      IsNoun(counter)==1||
                      Sentence.word[counter].POStagword.equals("NNS")||Sentence.word[counter].POStagword.equals("CD")||//Sentence.word[counter].POStagword.equals("IN")||
                       Sentence.word[counter].POStagword.equals("NNP")||Sentence.word[counter].POStagword.equals("JJ")||Sentence.word[counter].POStagword.equals("JJS")||Sentence.word[counter].POStagword.equals("PRP$")
                     ||Sentence.word[counter].POStagword.equals("POS")
                      //||Sentence.word[counter].POStagword.equals('"')
                  
                      ||Sentence.word[counter].POStagword.equals("NNPS")
                        ||Sentence.word[counter].POStagword.equals("PRP")
                      )&(counter!=1))
                 {
                    if(i==0) minimalTriple[triplenum].subject=Sentence.word[counter].POSword+" "+minimalTriple[triplenum].subject;
                    else     minimalTriple[triplenum].object=Sentence.word[counter].POSword+" "+minimalTriple[triplenum].object;
                        
                    if(i==0)
                        minimalTriple[triplenum].subjectStart=counter;
                    if(i==2)
                         minimalTriple[triplenum].subjectEnd=counter;
                     
                     if (counter!=1) counter--;else break;
                 }
   }
   public static void extendToRightCopular(int i,int triplenum,int counter){
              //counter= verbsPosition[wordnum]+1;
              while(!(Sentence.word[counter].POStagword.equals("NN"))&!(Sentence.word[counter].POStagword.equals("NNS"))&!
                      (Sentence.word[counter].POStagword.equals("CD"))&!
                      (IsNoun(counter)==1)
                      &!(Sentence.word[counter].POStagword.equals("NNP"))&!(Sentence.word[counter].POStagword.equals("JJ"))&!
                      Sentence.word[counter].POStagword.equals("POS")
                      &!Sentence.word[counter].POStagword.equals("IN")
                      &!Sentence.word[counter].POStagword.equals("CC")
                      &!Sentence.word[counter].POStagword.equals("NNPS")
                      &!Sentence.word[counter].POStagword.equals("PRP")
                      &!Sentence.word[counter].POStagword.equals("DT")
                      &!Sentence.word[counter].POStagword.equals("RBR") )
                    if(counter!=wordsNumber)counter++;else break;
              
              
              if(i==0)
                  
              {   
                  triple[triplenum].subject=Sentence.word[counter].POSword;
                  triple[triplenum].subjectEnd=counter;
                  triple[triplenum].subjectStart=counter;
              }   
               if(i==2)
               {  triple[triplenum].object=Sentence.word[counter].POSword;
                  triple[triplenum].objectEnd=counter;
                  triple[triplenum].objectStart=counter;
               }
              //triplesPosition[triplenum][i]=counter;
                  if(counter!=wordsNumber)counter++;
             /* while(  
                     (Sentence.word[counter].POStagword.equals("NN")||Sentence.word[counter].POStagword.equals("NNS")||Sentence.word[counter].POStagword.equals("CD")||Sentence.word[counter].POStagword.equals("IN")||
                       Sentence.word[counter].POStagword.equals("NNP")||Sentence.word[counter].POStagword.equals("JJ")||Sentence.word[counter].POStagword.equals("JJS")||
                      //Sentence.word[counter].POStagword.equals("VBG")||
                      Sentence.word[counter].POStagword.equals("PRP$")
                      ||Sentence.word[counter].POStagword.equals("POS")
                      ||Sentence.word[counter].POStagword.equals("IN")
                      ||Sentence.word[counter].POStagword.equals("RBR")
                      ||Sentence.word[counter].POStagword.equals("CC")
                      ||Sentence.word[counter].POStagword.equals("NNPS")
                      ||Sentence.word[counter].POStagword.equals("PRP")
                      ||Sentence.word[counter].POStagword.equals("DT"))&(counter!=wordsNumber))*/
             while(counter!=wordsNumber)
                     
                 { 
                     if(govIsMatchedWhithVerbs(Sentence.word[counter].POSword)==0 & !Sentence.word[counter].POSword.matches(",") ){
                       if(i==0)
                       {
                        triple[triplenum].subjectEnd=counter;
                        triple[triplenum].subject=triple[triplenum].subject+" "+Sentence.word[counter].POSword;
                       }
                        if(i==2)
                        {
                        triple[triplenum].objectEnd=counter;
                        triple[triplenum].object=triple[triplenum].object+" "+Sentence.word[counter].POSword;

                        }                     
//triplesPosition[triplenum][i]=counter;
                     // triplesPosition[triplenum][i]=counter;
                     if(counter!=wordsNumber)counter++;else break;
                     }else
                         break;
                 }
   }
   public static void extendToRightCopularMinimal(int i,int triplenum,int counter){
              //counter= verbsPosition[wordnum]+1;
              while(!(Sentence.word[counter].POStagword.equals("NN"))&!(Sentence.word[counter].POStagword.equals("NNS"))&!
                     (Sentence.word[counter].POStagword.equals("CD"))&!
                      (IsNoun(counter)==1)&!
                      (Sentence.word[counter].POStagword.equals("NNP"))&!(Sentence.word[counter].POStagword.equals("JJ"))&!
                      Sentence.word[counter].POStagword.equals("POS")
                      //&!Sentence.word[counter].POStagword.equals('"')
                      //&!Sentence.word[counter].POStagword.equals("CC")
                      &!Sentence.word[counter].POStagword.equals("NNPS")
                      &!Sentence.word[counter].POStagword.equals("PRP")
                     // &!Sentence.word[counter].POStagword.equals("DT")
                      )
                    if(counter!=wordsNumber)counter++;else break;
              
              if(i==0)
              {   minimalTriple[triplenum].subject=Sentence.word[counter].POSword; 
                  minimalTriple[triplenum].subjectEnd=counter;
                   minimalTriple[triplenum].subjectStart=counter;
              }   
               if(i==2)
               {   
                  minimalTriple[triplenum].object=Sentence.word[counter].POSword; 
                  minimalTriple[triplenum].objectEnd=counter;
                   minimalTriple[triplenum].objectStart=counter;
               }
              //triplesPosition[triplenum][i]=counter;
                  if(counter!=wordsNumber)counter++;
              while(
                     (Sentence.word[counter].POStagword.equals("NN")||Sentence.word[counter].POStagword.equals("NNS")||Sentence.word[counter].POStagword.equals("CD")
                      //||Sentence.word[counter].POStagword.equals("IN")
                      ||Sentence.word[counter].POStagword.equals("NNP")||Sentence.word[counter].POStagword.equals("JJ")||Sentence.word[counter].POStagword.equals("JJS")
                      ||IsNoun(counter)==1
                      ||Sentence.word[counter].POStagword.equals("PRP$")
                      ||Sentence.word[counter].POStagword.equals("POS")
                      //||Sentence.word[counter].POStagword.equals('"')
                    
                      ||Sentence.word[counter].POStagword.equals("NNPS")
                      ||Sentence.word[counter].POStagword.equals("PRP")
                      //||Sentence.word[counter].POStagword.equals("DT")
                      )&(counter!=wordsNumber))
                 {    if(i==0)
                       {
                           minimalTriple[triplenum].subjectEnd=counter;
                           minimalTriple[triplenum].subject= minimalTriple[triplenum].subject+" "+Sentence.word[counter].POSword;
                       }
                    if(i==2)
                       {
                           minimalTriple[triplenum].objectEnd=counter;
                           minimalTriple[triplenum].object= minimalTriple[triplenum].object+" "+Sentence.word[counter].POSword;
                       } 
                     if(counter!=wordsNumber)counter++;else break;
                 }
   }
   
    
   public static void convertConnectedWords(int i,int SubjObj,int MinMax, String conectedWord,int ConnectedWordPosition){
   //-----------------------------connected words------------------------------------
    String subj="";
   if (conectedWord.matches("that")||conectedWord.matches("who")
           ||conectedWord.matches("which")||conectedWord.matches("whom")
           ||conectedWord.matches("where")){
            //List<TypedDependency> tdl=null;;              
            int RefrenceIndex=FindRerenceOfThat(ConnectedWordPosition-1);
                           if(RefrenceIndex!=0){
                             //subj=extendToNounGroupForAWordDirection(RefrenceIndex,POSword[RefrenceIndex],sentence,1);
                              if (MinMax==1){
                                   if(SubjObj==0){
                                       triple[i].subject=subj;
                                       triple[i].subjectStart=LeftLimitOfWord;
                                       triple[i].subjectEnd=RightLimitOfWord;
                                    }else{
                                         triple[i].objectStart=LeftLimitOfWord;
                                         triple[i].objectEnd=RightLimitOfWord;
                                         triple[i].object=subj;
                                           }
                                       
                                }
                              else{
                                  
                                   if(SubjObj==0){
                                       minimalTriple[i].subject=subj;
                                       minimalTriple[i].subjectStart=LeftLimitOfWord;
                                       minimalTriple[i].subjectEnd=RightLimitOfWord;
                                    }else{
                                         minimalTriple[i].object=subj;
                                           minimalTriple[i].objectStart=LeftLimitOfWord;
                                         minimalTriple[i].objectEnd=RightLimitOfWord;
                                          }
                                   
                                  
                                }
                        
                        }
                          else{
                         
                               if (MinMax==0){
                                            if(SubjObj==0) 
                                                minimalTriple[i].subject=Sentence.word[ConnectedWordPosition-1].POSword;
                                            else
                                                minimalTriple[i].object=Sentence.word[ConnectedWordPosition-1].POSword;
                                             if(SubjObj==0){
                                                 minimalTriple[i].subjectStart=ConnectedWordPosition-1;
                                                 minimalTriple[i].subjectEnd=ConnectedWordPosition-1;
                                             }
                                              else {
                                                minimalTriple[i].objectStart=ConnectedWordPosition-1;
                                                minimalTriple[i].objectEnd=ConnectedWordPosition-1;
                                             }
                               }
                               if (MinMax==1){
                                                       
                                             if(SubjObj==0){
                                                 triple[i].subject=Sentence.word[ConnectedWordPosition-1].POSword;
                                                 triple[i].subjectStart=ConnectedWordPosition-1;
                                                 triple[i].subjectEnd=ConnectedWordPosition-1;
                                             }
                                             else{
                                                 triple[i].object=Sentence.word[ConnectedWordPosition-1].POSword;
                                                 triple[i].objectStart=ConnectedWordPosition-1;
                                                 triple[i].objectEnd=ConnectedWordPosition-1;
                                             }
                                }
                            }       
    
   }
   }
   
   public static void FullNullObject(){
       String[] str;int counter;
       for(int j=0;j<EndNum;j++){
      
           if(triple[j].subject!="" & triple[j].object==""){
            triple[j].object="";
               counter= triple[j].predicateStart+1; 
               if(!Sentence.word[counter].POStagword.equals(","))
             while(counter!=wordsNumber & !Sentence.word[counter].POStagword.equals("''")& !Sentence.word[counter].POStagword.equals(",")){                
                       triple[j].object=triple[j].object+" "+Sentence.word[counter].POSword;
                       if(counter!=wordsNumber-1)counter++;else break;
                 }
            }
        }
   }
   public static void FullNullObjectMinimal(){
       String[] str;int counter;
       for(int j=0;j<minimalEndNum;j++){
      
           if(minimalTriple[j].subject!="" & minimalTriple[j].object==""){
            minimalTriple[j].object="";
               counter= minimalTriple[j].predicateStart+1; 
               if(!Sentence.word[counter].POStagword.equals(","))
             while(counter!=wordsNumber & !Sentence.word[counter].POStagword.equals("''")&!Sentence.word[counter].POStagword.equals(",")){                
                       minimalTriple[j].object=minimalTriple[j].object+" "+Sentence.word[counter].POSword;
                       if(counter!=wordsNumber-1)counter++;else break;
                 }
            }
        }
   }
   //////verb functions
   
    public static void CommaRelation() throws Exception{
       String[] str1;int dep1Index,gov1Index,j,i,k=0;
       String rel1,dep1,gov1;   
       int index1=0;
       ////List<TypedDependency> tdl=null;;   
       for (TypedDependency t1 : tdl){
                     str1=tdl.get(index1).dep().toString().split("/");
                     dep1Index=tdl.get(index1).dep().index();
                     dep1=str1[0];
                     gov1Index=tdl.get(index1).gov().index();
                     str1=tdl.get(index1).reln().toString().split("/");
                     rel1=str1[0];
                     str1=tdl.get(index1).gov().toString().split("/");
                     gov1=str1[0];
                    
                     if(rel1.matches("appos")){
                     /*-------------second approach to increase the accuracy of the Comma relations------------------
                      */
                     
                         for( i=0; i<EndNum;i++)
                           if(triple[i].subject.contains(dep1)) break;
                         for( j=0; j<minimalEndNum;j++)
                           if(minimalTriple[j].object.contains(dep1)) break;
                         if(i==EndNum & j==minimalEndNum){
                           //  */
                      //-------------- end second approach to increase the accuracy of the Comma relations------------------  
                            minimalTriple[minimalEndNum].subject=gov1; minimalTriple[minimalEndNum].subjectStart=gov1Index;minimalTriple[minimalEndNum].subjectEnd=gov1Index;extendToNounGroupMinimal(0,minimalEndNum);
                            minimalTriple[minimalEndNum].predicate="Is";minimalTriple[minimalEndNum].predicateStart=0;
                            minimalTriple[minimalEndNum].object=dep1;minimalTriple[minimalEndNum].objectStart=dep1Index;minimalTriple[minimalEndNum].objectEnd=dep1Index;extendToNounGroupMinimal(2,minimalEndNum);
                            minimalTriple[minimalEndNum].tripleType="Comma relation:   ";
                            minimalEndNum++;  
                            //CompSuper=1;
                         
                        /*//*/ } 
                         }
                     
                     index1++;
                    }
}

    public static void GenetiveFunction2() {
       String[] str1;int dep1Index,gov2Index,dep2Index,gov1Index,j,k=0;
       String rel1,dep1,gov1,rel2,gov2,dep2;   
       int Startnum=minimalEndNum;
       int Endnum=0;
       dep2="";
       rel2="";
       int sequenceNum=-1;
       dep2Index=0;
       int index1=0;
       int step=1;   
       ////List<TypedDependency> tdl=null;;
       for (TypedDependency t1 : tdl){
                     str1=tdl.get(index1).dep().toString().split("/");
                     dep1Index=tdl.get(index1).dep().index();
                     dep1=str1[0];
                     gov1Index=tdl.get(index1).gov().index();
                     str1=tdl.get(index1).reln().toString().split("/");
                     rel1=str1[0];
                     str1=tdl.get(index1).gov().toString().split("/");
                     gov1=str1[0];
                     if(!gov1.matches("ROOT"))
                     if(!relIsIndirectObject(rel1).matches("")& govIsMatchedWhithVerbs(gov1)==0){
                       
                      for(j=0;j<=minimalEndNum;j++)
                      {
                      if((minimalTriple[j].subject.matches(gov1)& minimalTriple[j].object.matches(dep1) )||
                           (minimalTriple[j].predicate.matches(gov1)& minimalTriple[j].object.matches(dep1) )   
                              ) 
                          break;
                      }
                             if(j>minimalEndNum){
                      
                      int first=minimalEndNum;
                      MakeGenitiveRelation(gov1Index,dep1Index,rel1);
                      if(minimalEndNum>first){
                                      sequenceNum=-1;
                                      sequenceNum=sequenceNum+2; 
                                      if(rel1.matches("nmod:to"))
                                          rel1="nmod:"+gov1+" to";
                                      //minimalTriple[minimalEndNum-1].tripleType="Genitive relation:Function2 "+rel1;
                                      minimalTriple[minimalEndNum-1].tripleType=rel1;
                                      prepSequence[sequenceNum]=gov1Index;
                                      prepSequence[sequenceNum+1]=dep1Index;
                             
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
                                    (gov2.matches(dep1) & (gov2Index==dep1Index))& govIsSomeSpecialWords(gov2)==0&(!relIsIndirectObject(rel2).matches("")||rel2.matches("dep"))& IsPossessiveAdjective(dep2)==0 )||
                                    (rel1.matches("cop")&!relIsIndirectObject(rel2).matches("") &gov2.matches(gov1) )
                                    
                                    ){
                                  k=1;
                                       
                            for(j=0;j<minimalEndNum;j++)
                             {
                              if(minimalTriple[j].subject.contains(dep1)& minimalTriple[j].object.contains(gov1) )
                               break;
                             }
                             if(j==minimalEndNum){                                
                                      
                                      first=minimalEndNum;
                                      MakeGenitiveRelation(gov2Index,dep2Index,rel2);
                                      if(minimalEndNum>first)
                                      {
                                      sequenceNum=sequenceNum+2;    
                                      //minimalTriple[minimalEndNum-1].tripleType="Genitive relation:Function2 "+rel2;
                                      minimalTriple[minimalEndNum-1].tripleType=rel2;
                                      prepSequence[sequenceNum]=gov2Index;
                                      prepSequence[sequenceNum+1]=dep2Index;
                                      }
                                      
                                      break;
                                //  }
                            }
                            
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
                      }
                      }
                     }
                     if(sequenceNum>0)
                     if(IsCapital(Sentence.word[prepSequence[sequenceNum+1]].POSwordStart)==0)
                         {
                           //minimalEndNum=Startnum;
                           for(int counter=Startnum+1;counter<minimalEndNum;counter++){
                              minimalTriple[counter].tripleType=minimalTriple[counter].tripleType;
                              //minimalTriple[counter].tripleType=minimalTriple[counter].tripleType+"Not capital object";
                           }
                         }  
                     else 
                         Startnum=minimalEndNum;
                     sequenceNum=-1;
                   //  }//end if(IsProbable......)
                     index1++;
                   
                }
}
       
      public static void IndirectVerbal() {
           String[] str1;int dep1Index,gov1Index,gov2Index,dep2Index,k=0;
       String rel1,dep1,gov1,rel2,gov2,dep2,Subject,predicate;   
       dep2="";
       dep2Index=0;
       int index1=0;
       int step=1;
       int predicateIndex=0;
       AbstractSequenceClassifier classifier = null;
       ////List<TypedDependency> tdl=null;;
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
                         //   if(gov2.matches(dep1) & (gov2Index==dep1Index)& !relIsIndirectObject(rel2).matches("")){
                           
                         k=1;
                                  MakeIndirectVerbalRelation(predicate,gov1Index,dep2,dep2Index,relIsIndirectObject(rel2));
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
    public static void IndirectVerbalNew( ){
          String[] str1;int dep1Index,gov2Index,dep2Index,gov1Index,j,k=0;
       String rel1,dep1,gov1,rel2,gov2,dep2;   
       int Startnum=minimalEndNum;
       int Endnum=0;
       dep2="";
       rel2="";
       int sequenceNum=-1;
       dep2Index=0;
       int index1=0;
       int step=1;  
      // List<TypedDependency> tdl=null;
       for (TypedDependency t1 : tdl){
                     str1=tdl.get(index1).dep().toString().split("/");
                     dep1Index=tdl.get(index1).dep().index();
                     dep1=str1[0];
                     gov1Index=tdl.get(index1).gov().index();
                     str1=tdl.get(index1).reln().toString().split("/");
                     rel1=str1[0];
                     str1=tdl.get(index1).gov().toString().split("/");
                     gov1=str1[0];
                     if(!gov1.matches("ROOT"))
                     if(!relIsIndirectObject(rel1).matches("")&
                             //govIsMatchedWhithVerbs(POSword[POSwordStart[gov1Index]])==0  &
                          IsCapital(Sentence.word[dep1Index].POSwordStart)==1 & IsCapital(Sentence.word[gov1Index].POSwordStart)==1){
                       
                        for(j=0;j<=minimalEndNum;j++)
                         {
                           if((minimalTriple[j].subject.matches(gov1)& minimalTriple[j].object.matches(dep1) )||
                             (minimalTriple[j].predicate.matches(gov1)& minimalTriple[j].object.matches(dep1) )   
                              ) 
                          break;
                        }
                        if(j>minimalEndNum)
                             
                               MakeIndirectVerbalRelationNew(gov1,gov1Index,dep1,dep1Index,relIsIndirectObject(rel1));
                      }
                     index1++;
                    }
    }
   public static void MakeIndirectVerbalRelation(String predicate,int predicateIndex,String dep2,int dep2Index,String prep){
    int j=0;String subject="";
    int subjectStart=0,subjectEnd=0;
    
    for(j=0;j<minimalEndNum+1;j++){
                  if(minimalTriple[j].predicate.matches(predicate) & minimalTriple[j].object.matches(dep2))
                    break;
                  else if(minimalTriple[j].predicate.matches(predicate) & !minimalTriple[j].object.matches(dep2)){
                    subject=minimalTriple[j].subject;
                    subjectStart=minimalTriple[j].subjectStart;
                    subjectEnd=minimalTriple[j].subjectEnd;
                  }
    }
    if(j==minimalEndNum+1){
     minimalEndNum++;   
        minimalTriple[j].subject=subject;minimalTriple[j].subjectStart=subjectStart;minimalTriple[j].subjectEnd=subjectEnd;
        minimalTriple[j].predicate=predicate;minimalTriple[j].predicateStart=predicateIndex;
        minimalTriple[j].object=dep2;minimalTriple[j].objectStart=minimalTriple[j].objectEnd=dep2Index;extendToNounGroupMinimal(2,j);
        minimalTriple[j].predicatePrep=prep;
        minimalTriple[j].tripleType="Indirect Verbal relation: ";
    }
        
   }
   public static void MakeIndirectVerbalRelationNew(String gov1,int gov1Index,String dep1,int dep1Index,String prep){
    int j=0;String subject="";
    int subjectStart=0,subjectEnd=0;
    
    for(j=0;j<minimalEndNum+1;j++){
                  if(minimalTriple[j].object.matches(gov1))
                    break;
                  
    }
    
    if(j<minimalEndNum+1){
        minimalEndNum++;   
        minimalTriple[minimalEndNum].subject=minimalTriple[j].subject;minimalTriple[minimalEndNum].subjectStart=minimalTriple[j].subjectStart;minimalTriple[minimalEndNum].subjectEnd=minimalTriple[j].subjectEnd;
        minimalTriple[minimalEndNum].predicate=minimalTriple[j].predicate;minimalTriple[minimalEndNum].predicateStart=minimalTriple[j].predicateStart;
        minimalTriple[minimalEndNum].object=dep1;minimalTriple[minimalEndNum].objectStart=minimalTriple[minimalEndNum].objectEnd=dep1Index;extendToNounGroupMinimal(2,minimalEndNum);
        minimalTriple[minimalEndNum].predicatePrep=prep;
        minimalTriple[minimalEndNum].tripleType="Indirect Verbal relation: ";
    }
      minimalEndNum++;     
   }
    public static void GenitiveRelations(){
        
     //  GenetiveFunction1(tdl,sentence);
       GenetiveFunction2();
 }
    public static void VerbBasedRelations( ){
      
      //***********************************searches for subject- verb- object triples*******************************************
      int i,Minimali,j,counter;
      i=0;Minimali=0;
      for(j=0;j<Sentence.CounterVerbs;j++){
         int index=0;
         int k=BeCopularMainVerb(verbs[j].verbs,j);
         if(BeCopularMainVerb(verbs[j].verbs,j)==1 ){
             
                    i=AddCopularRelationTriple(verbs[j].verbs,j,i);i++;
                    Minimali=AddCopularRelationTripleMinimal(verbs[j].verbs,j,Minimali);
                 
         }
         
         else if (NotCopularVerb(verbs[j].verbs)==1 & VerbToVerb(j)==0){
              if(j>0){
                 if(NotCopularVerb(verbs[j-1].verbs)==0 || verbs[j].verbsPosition!=verbs[j-1].verbsPosition+1){
                    i=AddNotCopularVerbRelationTriple(verbs[j].verbs,j,i);//i++;
                    Minimali=AddNotCopularVerbRelationTripleMinimal(verbs[j].verbs,j,Minimali);    
                 }
                 else
                     Sentence.word[verbs[j].verbsPosition].POStagword="NN";
              }else{
                     i=AddNotCopularVerbRelationTriple(verbs[j].verbs,j,i);i++;
                    Minimali=AddNotCopularVerbRelationTripleMinimal(verbs[j].verbs,j,Minimali);Minimali++;     
                   }
                  
             }
      }
      EndNum=i;
      minimalEndNum=Minimali;
    }
   public static void NounPhraseRelations() throws IOException{
       int [] Adjective=new int [10] ;
       int ntNum=0;
       for(int i=1;i<wordsNumber;i++){
        
         //dbpedia spotlight
         /*
         if(!POSwordNamedEntityURI[i].matches("")){
              if(POSwordNamedEntityURI[POSwordNamedEntityState[i]+i].matches("")& IsNoun(POSwordNamedEntityState[i]+i)==1){
                   String Class=Main.main(POSword[POSwordNamedEntityState[i]+i]);
                  if(!Class.matches("Null"))
                     AddNounPhraseTriplePatternP1(i,POSwordNamedEntityState[i],POSwordNamedEntityState[i]+i,str);
                  else
                     AddNounPhraseTriplePatternP2(i,POSwordNamedEntityState[i],POSwordNamedEntityState[i]+i);
                   }
               else if(!POSwordNamedEntityURI[POSwordNamedEntityState[i]+i].matches("")& IsNoun(POSwordNamedEntityState[i]+i)==1){
                     AddNounPhraseTriplePatternP3(i,POSwordNamedEntityState[i],POSwordNamedEntityState[i]+i);
               }
               }
         */
         //TAGme
         int sw=1;
         if(i<wordsNumber-1)
             if(/*phraseTagmeLinksRecognizer[i]==1 & phraseTagmeLinksRecognizer[i+1]==1
                & */BeCapableOfMemberOfNounRelation(i)==1 & BeCapableOfMemberOfNounRelationSecondArguman(i+1)==1  ){
                        if(i>1) 
                         if(Sentence.word[i-1].POStagword.matches("TO")) 
                             sw=0;
                        if((IsCapital(Sentence.word[i].POSwordStart)==1 & IsCapital(Sentence.word[i+1].POSwordStart)==0)&
                                !(Sentence.word[i].POStagword.matches("NNP")&(Sentence.word[i+1].POStagword.matches("NNP")))& sw==1){
                           String Class=Main.main(Sentence.word[Sentence.word[i+1].POSwordStart].POSword);
                           if(!Class.matches("Null"))
                              AddNounPhraseTriplePatternP1(i,1,i+1);
                           else 
                             // AddNounPhraseTriplePatternP2(i,1,i+1);
                              AddNounPhraseTriplePatternP1(i,1,i+1);
                        }
                        else if(IsCapital(Sentence.word[i].POSwordStart)==0 & IsCapital(Sentence.word[i+1].POSwordStart)==0 & sw==1  ){
                           if( !(Sentence.wordPre[Sentence.word[i].POSwordStart].POStagword.matches("NNP")& Sentence.wordPre[Sentence.word[i+1].POSwordStart].POStagword.matches("NNP")))
                             if((Sentence.wordPre[Sentence.word[i].POSwordStart].POStagword.matches("JJ")& Sentence.wordPre[Sentence.word[i+1].POSwordStart].POStagword.matches("JJ"))){
                                 int NumAdjective=0;
                                 int counter=i;
                                 while(Sentence.word[counter].POStagword.matches("JJ")){
                                      Adjective[NumAdjective]=counter;
                                      if(counter<wordsNumber){
                                          NumAdjective++;  
                                          counter++;
                                      }
                                 }
                                 if(Sentence.word[counter].POStagword.matches("NN")||Sentence.word[counter].POStagword.matches("NNP")||Sentence.word[counter].POStagword.matches("NNPS")||Sentence.word[counter].POStagword.matches("NNS"))
                                     ntNum++;
                                     for(int j=0;j<NumAdjective;j++){
                                         AddNounPhraseTriplePatternP1(Adjective[j],1,counter);
                                         //AddNounPhraseTriplePatternP3(Adjective[j],1,counter,ntNum);   
                                 }
                                i=counter-1; 
                             }  
                             else{
                                  ntNum++;
                                  AddNounPhraseTriplePatternP1(i,1,i+1);
                                 // AddNounPhraseTriplePatternP3(i,1,i+1,ntNum); 
                             }
                        }
               }
             else if (Sentence.word[i].POStagword.matches("JJ")){
                                 int NumAdjective=0;
                                 int counter=i;
                                 while(Sentence.word[counter].POStagword.matches("JJ")){
                                      Adjective[NumAdjective]=counter;
                                      if(counter<wordsNumber){
                                          NumAdjective++;  
                                          counter++;
                                      }
                                 }
                                 if(Sentence.word[counter].POStagword.matches("NN")||Sentence.word[counter].POStagword.matches("NNP")||Sentence.word[counter].POStagword.matches("NNPS")||Sentence.word[counter].POStagword.matches("NNS")){
                                     ntNum++;
                                     for(int j=0;j<NumAdjective;j++){
                                        //AddNounPhraseTriplePatternP3(Adjective[j],1,counter,ntNum);   
                                         AddNounPhraseTriplePatternP1(Adjective[j],1,counter);
                                         
                                     }
                                 }
                                i=counter-1; 
              //-------------------------------------
             }
         
       }
   }
 public static int AddNounPhraseTriplePatternP1(int firstNoun,int LengthOfFirstNoun,int secondNoun){
    minimalEndNum++;
    int i=minimalEndNum-1;
    
    minimalTriple[i].subject=Sentence.word[secondNoun].POSword; minimalTriple[i].subjectStart=Sentence.word[secondNoun].POSwordStart;minimalTriple[i].subjectEnd=Sentence.word[secondNoun].POSwordStart;//extendToNounGroup(0,i,sentence);
    minimalTriple[i].predicate="has a relation with"; minimalTriple[i].predicateStart=0;
    minimalTriple[i].tripleType="Noun phrase relation(p1): ";
    for(int k=1;k<=LengthOfFirstNoun;k++){
    minimalTriple[i].object=minimalTriple[i].object+Sentence.word[firstNoun+k-1].POSword;}
    minimalTriple[i].objectStart=firstNoun;minimalTriple[i].objectEnd=firstNoun+LengthOfFirstNoun-1;
   //minimalTriple[i].object=extendToNounGroupForAWordDirectionNounPhrase(minimalTriple[i].objectStart,minimalTriple[i].object,str,1);
    return 0;}
   
   public static int AddNounPhraseTriplePatternP2(int firstNoun,int LengthOfFirstNoun,int secondNoun){
    minimalEndNum++;
    int i=minimalEndNum-1;
    minimalTriple[i].tripleType="Noun phrase relation(p2):";
    minimalTriple[i].subject=Sentence.word[firstNoun].POSword; minimalTriple[i].subjectStart=Sentence.word[firstNoun].POSwordStart;minimalTriple[i].subjectEnd=Sentence.word[firstNoun].POSwordStart;//extendToNounGroup(0,i,sentence);
    minimalTriple[i].predicate=Sentence.word[secondNoun].POSword; minimalTriple[i].predicateStart=Sentence.word[secondNoun].POSwordStart;
    minimalTriple[i].object="?o";
    minimalTriple[i].objectStart=0;minimalTriple[i].objectEnd=0;
    
   return 0;}
   public static int AddNounPhraseTriplePatternP3(int firstNoun,int LengthOfFirstNoun,int secondNoun,int ntNum){
    minimalEndNum++;int j;
    int i=minimalEndNum-1;
    for(j=0;j<minimalEndNum;j++){
        if(minimalTriple[j].object.matches(Sentence.word[firstNoun].POSword))
              break;
    }
    if(j==minimalEndNum){
    minimalTriple[i].tripleType="Noun phrase relation(p3):";
    minimalTriple[i].subject="?s"+ntNum;minimalTriple[i].subjectStart=0;minimalTriple[i].subjectEnd=0;//extendToNounGroup(0,i,sentence);
    minimalTriple[i].predicate="?p1"; minimalTriple[i].predicateStart=0;
    minimalTriple[i].object=Sentence.word[firstNoun].POSword;
    minimalTriple[i].objectStart=Sentence.word[firstNoun].POSwordStart;minimalTriple[i].objectEnd=Sentence.word[firstNoun].POSwordStart;
    minimalEndNum++;
    i++;}
    for(j=0;j<minimalEndNum;j++){
        if(minimalTriple[j].object.matches(Sentence.word[secondNoun].POSword))
              break;
    }
    if(j==minimalEndNum){
    minimalTriple[i].tripleType="Noun phrase relation(p3):";
    minimalTriple[i].subject="?s"+ntNum;minimalTriple[i].subjectStart=0;minimalTriple[i].subjectEnd=0;//extendToNounGroup(0,i,sentence);
    minimalTriple[i].predicate="?p2"; minimalTriple[i].predicateStart=0;
    minimalTriple[i].object=Sentence.word[secondNoun].POSword;
    minimalTriple[i].objectStart=Sentence.word[secondNoun].POSwordStart;minimalTriple[i].objectEnd=Sentence.word[secondNoun].POSwordStart;
    }
   return 0;}
   public static int AddNotCopularVerbRelationTriple(String str,int j,int i){
     
     //Initialization
     for(int f=0;f<=14;f++){subjectFillers[f]="";subjectFillersPositions[f]=0;objectFillers[f]="";objectFillersPositions[f]=0;directObjectFillersPositions[f]=0;}
     subjectFillersNumber=0;objectFillersNumber=0;
     IndirectObject="";
     EndNum=i;
     String[] str1;
     //----------------------------------
     int index=0,depIndex,govIndex;String gov,dep,rel,govTag;String[] strTemp; 
     //List<TypedDependency> tdl=null;;
     for (TypedDependency t : tdl){ 
         gov=tdl.get(index).gov().value();
         dep=tdl.get(index).dep().value();
         depIndex=tdl.get(index).dep().index();
         govTag=tdl.get(index).gov().tag();
         govIndex=tdl.get(index).gov().index();
         rel=tdl.get(index).reln().toString();
         str1=str.split(" ");
         str=str1[0];
         if(gov.matches(str)||(dep.matches(str)& rel.matches("acl")))
           
             if(!dep.matches(str)){
             if(NotDependencyBetwweenToVerbs(dep,str)==1){
              
               FullUpSubjectFillers(gov,dep,rel,EndNum,depIndex,govTag,govIndex);
               FullUpObjectFillers(gov,dep,rel,EndNum,depIndex,govTag,govIndex);
             
           }}
             else{
              FullUpSubjectFillers(gov,dep,rel,EndNum,depIndex,govTag,govIndex);
               FullUpObjectFillers(gov,dep,rel,EndNum,depIndex,govTag,govIndex);
             }
         index++;
        }
       i=makeTriplesFromFillers1(i,str,j);
   return i;
   }  
   public static int AddNotCopularVerbRelationTripleMinimal(String str,int j,int i){
     
     //Initialization
   //  for(int f=0;f<=6;f++){subjectFillers[f]="";subjectFillersPositions[f]=0;objectFillers[f]="";objectFillersPositions[f]=0;directObjectFillersPositions[f]=0;}
   //  subjectFillersNumber=0;objectFillersNumber=0;
   //  IndirectObject="";
    // EndNum=i;
     minimalEndNum=i;
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
              
               FullUpSubjectFillers(gov,dep,rel,minimalEndNum,depIndex,govTag,govIndex,sentence);
               FullUpObjectFillers(gov,dep,rel,minimalEndNum,depIndex,govTag,govIndex,sentence,str);
             
           }}
         else{
               FullUpSubjectFillers(gov,dep,rel,minimalEndNum,depIndex,govTag,govIndex,sentence);
               FullUpObjectFillers(gov,dep,rel,minimalEndNum,depIndex,govTag,govIndex,sentence,str);
             }
         index++;
        }*/
       i=makeTriplesFromFillersMinimal(i,str,j);
   return i;
   }  
  public static void FullUpSubjectFillers(String gov,String dep,String rel,int i,int depIndex,String govTag,int govIndex){
     
       if (rel.equals("nsubj")||rel.equals("nsubjpass")){
         
           subjectFillers[subjectFillersNumber]=dep; 
           subjectFillersPositions[subjectFillersNumber]=depIndex;
           if(IsGenitivePossSubject(depIndex)!=0)
           {
             subjectFillers[subjectFillersNumber]=Sentence.word[IsGenitivePossSubject(depIndex)].POSword;
             subjectFillersPositions[subjectFillersNumber]=IsGenitivePossSubject(depIndex);
           }
          
           subjectFillersNumber++;
         }else if(rel.equals("acl")){
          subjectFillers[subjectFillersNumber]=gov;
           subjectFillersPositions[subjectFillersNumber]=govIndex;
           subjectFillersNumber++;
         }
       if(subjectFillersNumber!=0){
       int index=FindRerenceOfThat(subjectFillersPositions[subjectFillersNumber-1]);
       if(index!=0){
           subjectFillers[subjectFillersNumber-1]=Sentence.word[index].POSword; 
           subjectFillersPositions[subjectFillersNumber-1]=index;
       }
       }
   }
   public static int FullUpObjectFillers(String gov,String dep,String rel,int i,int depIndex,String govTag,int govIndex){
   
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
            //||rel.equals("advmod")//Where did the first human in space die?(human,die,Where)(nsubj,VB,advmod)
            ||rel.equals("xcomp")//Which actress starring in the TV series Friends owns the production company Coquette Productions? xcomp(owns,Productions)
            ||rel.equals("nmod:with")//Which street basketball player was diagnosed with Sarcoidosis?
            ||rel.equals("nmod:to")
            ||rel.equals("nmod:over")
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
   public static int makeTriplesFromFillers(int i,String str,int verbNum,String sentence){
    int numberOfDirectObjects=0; String object="";
    for(int sf=0;sf<subjectFillersNumber;sf++)
        for(int of=0;of<objectFillersNumber;of++){
            if(objectFillersNumber>1){
                if(directObjectFillersPositions[of]==1){
                        
                        numberOfDirectObjects++;
                        triple[i].subject=subjectFillers[sf]; triple[i].subjectStart=subjectFillersPositions[sf];triple[i].subjectEnd=subjectFillersPositions[sf];convertConnectedWords(i,0,1,triple[i].subject,triple[i].subjectStart);extendToNounGroup(0,i);
                        triple[i].predicate=str; triple[i].predicateStart=verbs[verbNum].verbsPosition;
                        triple[i].object=objectFillers[of]; triple[i].objectStart=objectFillersPositions[of];triple[i].objectEnd=objectFillersPositions[of];convertConnectedWords(i,2,1,triple[i].subject,triple[i].predicateStart);extendToNounGroup(2,i);
                        triple[i].tripleType="Verbal relation: ";
                        if(Sentence.word[1].POSword.equals("How") & Sentence.word[2].POSword.equals("many") & triple[i].object.contains(Sentence.word[3].POSword)){
                           triple[i].predicate=triple[i].subject;
                           triple[i].object="N";
                           triple[i].tripleType="How many relation: ";
                        }
                        i=CommaBetweenTwoNoun(i,triple[i].objectStart,triple[i].subject);
                         // i++; triple[i].predicate=str;triple[i].subjectEnd=triplesPosition[i-1][1];sw=1;
                        i=EndNum;
                        if(!triple[i].subject.matches(triple[i].subject))
                        i++;
                }
                else { 
                    if(objectFillersNumber==2 & numberOfDirectObjects==1){
                    object=extendToNounGroupForAWordDirection(objectFillersPositions[of],objectFillers[of],3);
                    }
                    IndirectObject=IndirectObject+" "+object;
                    }
            }else{
                        triple[i].subject=subjectFillers[sf]; triple[i].subjectStart=subjectFillersPositions[sf]; triple[i].subjectEnd=subjectFillersPositions[sf];convertConnectedWords(i,0,1,triple[i].subject,triple[i].subjectStart);extendToNounGroup(0,i);
                        triple[i].predicate=str; triple[i].predicateStart=verbs[verbNum].verbsPosition;
                        triple[i].object=objectFillers[of]; triple[i].objectStart=objectFillersPositions[of];triple[i].objectEnd=objectFillersPositions[of];convertConnectedWords(i,2,1,triple[i].subject,triple[i].predicateStart);extendToNounGroup(2,i);
                        triple[i].tripleType="Verbal relation: ";
                        if(Sentence.word[1].POSword.equals("How") & Sentence.word[2].POSword.equals("many") & triple[i].object.contains(Sentence.word[3].POSword)){
                           triple[i].predicate=triple[i].subject.split("many ")[1];
                           triple[i].object="N";
                           triple[i].tripleType="How many relation: ";
                        }
                        i=CommaBetweenTwoNoun(i,triple[i].objectStart,triple[i].subject);
                         // i++; triple[i].predicate=str;triple[i].subjectEnd=triplesPosition[i-1][1];sw=1;
                        i=EndNum;
                        if(!triple[i].subject.matches(triple[i].subject))
                        i++;
            
            
            }
                
    }   
    if(subjectFillersNumber==0 & objectFillersNumber>0){
          for(int of=0;of<objectFillersNumber;of++){
           IndirectObject=IndirectObject+" "+objectFillers[of];
          }
        triple[i].subject="";  
        triple[i].predicate=str; triple[i].predicateStart=verbs[verbNum].verbsPosition;
        triple[i].object=IndirectObject;
        triple[i].tripleType="Verbal relation: ";
     }     
     else if(numberOfDirectObjects==0 & objectFillersNumber>1)
     {
        triple[i].subject=subjectFillers[0];  triple[i].subjectStart=subjectFillersPositions[0]; triple[i].subjectEnd=subjectFillersPositions[0];extendToNounGroup(0,i);
        triple[i].predicate=str; triple[i].predicateStart=verbs[verbNum].verbsPosition;
         triple[i].tripleType="Verbal relation: ";
        //        triple[i].object=triples[sf][2]+" "+IndirectObject; triple[i].objectStart=triplesPosition[sf][3];triple[i].objectEnd=triple[sf].objectEnd;
                i++;
     }
     else if (!IndirectObject.matches("")){
         int of=i;
         String predicate=triple[of-1].subject;
         for(int sf=of-1;sf>0;sf--)
                 if( triple[sf].subject==predicate){
                     triple[i].subject=triple[sf].subject; triple[i].subjectStart=triple[sf].subjectStart;triple[i].subjectEnd=triple[sf].subjectEnd;
                     triple[i].predicate=triple[sf].predicate; triple[i].predicateStart=triple[sf].subjectEnd;
                     triple[i].object=triple[sf].object+" "+IndirectObject; triple[i].objectStart=triple[sf].objectStart;triple[i].objectEnd=triple[sf].objectEnd;
                     triple[i].tripleType="Verbal relation: ";
                     if(!triple[i].subject.matches(triple[i].subject))
                        i++;
                }
                 else break;
     }

     if(objectFillersNumber==0)
         for(int sf=0;sf<subjectFillersNumber;sf++){
         triple[i].subject=subjectFillers[sf]; triple[i].subjectStart=subjectFillersPositions[sf];triple[i].subjectEnd=subjectFillersPositions[sf];extendToNounGroup(0,i);
         triple[i].predicate=str; triple[i].predicateStart=verbs[verbNum].verbsPosition;
         triple[i].tripleType="Verbal relation: ";
         i++;
         }


     return i;
     }
    //***********
   public static int makeTriplesFromFillersMinimal(int i,String str,int verbNum){
     int numberOfDirectObjects=0;


        for(int sf=0;sf<subjectFillersNumber;sf++)
         for(int of=0;of<objectFillersNumber;of++){
             if(objectFillersNumber>1){
//                 if(directObjectFillersPositions[of]==1){
//                         numberOfDirectObjects++;
                         minimalTriple[i].subject=subjectFillers[sf]; minimalTriple[i].subjectStart=subjectFillersPositions[sf];minimalTriple[i].subjectEnd=subjectFillersPositions[sf];convertConnectedWords(i,0,0,minimalTriple[i].subject,minimalTriple[i].subjectStart);extendToNounGroupMinimal(0,i);
                         minimalTriple[i].predicate=str; minimalTriple[i].predicateStart=verbs[verbNum].verbsPosition;
                         minimalTriple[i].object=objectFillers[of]; minimalTriple[i].objectStart=objectFillersPositions[of];minimalTriple[i].objectEnd=objectFillersPositions[of];convertConnectedWords(i,2,0,minimalTriple[i].object,minimalTriple[i].predicateStart);
                         minimalTriple[i].predicatePrep=objectFillersPrep[of];
                         
                         if(!Sentence.word[objectFillersPositions[of]].POStagword.matches("WRB"))
                           extendToNounGroupMinimal(2,i);
                         minimalTriple[i].tripleType="Verbal relation: "; 
                        if(Sentence.word[1].POSword.equals("How") & Sentence.word[2].POSword.equals("many") & minimalTriple[i].object.contains(Sentence.word[3].POSword)){
                           minimalTriple[i].predicate=minimalTriple[i].object.split("many ")[1];
                           minimalTriple[i].object="N";
                        }
                        i=CommaBetweenTwoNounMinimal(i,minimalTriple[i].objectStart,minimalTriple[i].object);
                         // i++; triple[i].predicate=str;triple[i].subjectEnd=triplesPosition[i-1][1];sw=1;
                         minimalEndNum++;
                         i=minimalEndNum;
                        if(!minimalTriple[i].subject.matches(minimalTriple[i].object))
                           i++;
//                }
//                else { 
//                    if(objectFillersNumber==2 & numberOfDirectObjects==1)
//                    objectFillers[of]=extendToNounGroupForAWordDirectionMinimal(objectFillersPositions[of],objectFillers[of],sentence,3);
                    
//                    IndirectObject=IndirectObject+" "+objectFillers[of];
////                    }
            }else{
                        minimalTriple[i].subject=subjectFillers[sf]; minimalTriple[i].subjectStart=subjectFillersPositions[sf]; minimalTriple[i].subjectEnd=subjectFillersPositions[sf];convertConnectedWords(i,0,0,minimalTriple[i].subject,minimalTriple[i].subjectStart);extendToNounGroupMinimal(0,i);
                        minimalTriple[i].predicate=str; minimalTriple[i].predicateStart=verbs[verbNum].verbsPosition;
                        minimalTriple[i].object=objectFillers[of];minimalTriple[i].objectStart=objectFillersPositions[of];minimalTriple[i].objectEnd=objectFillersPositions[of];if(!Sentence.word[objectFillersPositions[of]].POStagword.matches("WRB"))convertConnectedWords(i,2,0,minimalTriple[i].object,minimalTriple[i].predicateStart);extendToNounGroupMinimal(2,i);
                         minimalTriple[i].predicatePrep=objectFillersPrep[of];
                        minimalTriple[i].tripleType="Verbal relation: ";
                        if(Sentence.word[1].POSword.equals("How") & Sentence.word[2].POSword.equals("many") & minimalTriple[i].object.contains(Sentence.word[3].POSword)){
                           minimalTriple[i].predicate=minimalTriple[i].object.split("many ")[1];
                           minimalTriple[i].object="N";
                           minimalTriple[i].tripleType="How many relation: ";
                        }
                        i=CommaBetweenTwoNounMinimal(i,minimalTriple[i].objectStart,minimalTriple[i].object);
                         // i++; triple[i].predicate=str;triple[i].subjectEnd=triplesPosition[i-1][1];sw=1;
                        minimalEndNum++;
                        i=minimalEndNum;
                        if(!minimalTriple[i].subject.matches(minimalTriple[i].object)) 
                        i++;
            
            
            }
                
    }   
    if(subjectFillersNumber==0 & objectFillersNumber>0){
         for(int of=0;of<objectFillersNumber;of++){
         //  IndirectObject=IndirectObject+" "+objectFillers[of];
         // }
       minimalTriple[i].subject="";  
       minimalTriple[i].predicate=str; minimalTriple[i].predicateStart=verbs[verbNum].verbsPosition;
       minimalTriple[i].object=objectFillers[of];
        minimalTriple[i].predicatePrep=objectFillersPrep[of];
       minimalTriple[i].tripleType="verbal relation: ";
        
        i++;
    }  
    }     
 //   else if(numberOfDirectObjects==0 & objectFillersNumber>1)
//    {
//       minimalTriple[i].subject=subjectFillers[0];  minimalTriple[i].subjectStart=subjectFillersPositions[0]; minimalTriple[i].subjectEnd=subjectFillersPositions[0];extendToNounGroupMinimal(0,i,sentence);
//       minimalTriple[i].predicate=str; minimalTriple[i].predicateStart=verbs[verbNum].verbsPosition;
//       minimalTriple[i].tripleType="verbal relation: ";       
//        triple[i].object=triples[sf][2]+" "+IndirectObject; triple[i].objectStart=triplesPosition[sf][3];triple[i].objectEnd=triplesPosition[sf][4];
//               i++;
//    }
/*    else if (!IndirectObject.matches("")){
        int of=i;
        String predicate=Minimaltriples[of-1][1];
        for(int sf=of-1;sf>0;sf--)
                if( Minimaltriples[sf][1]==predicate){
                    minimalTriple[i].subject=Minimaltriples[sf][0]; minimalTriple[i].subjectStart=MinimaltriplesPosition[sf][0];minimalTriple[i].subjectEnd=MinimaltriplesPosition[sf][1];
                    minimalTriple[i].predicate=Minimaltriples[sf][1]; minimalTriple[i].predicateStart=MinimaltriplesPosition[sf][1];
                    minimalTriple[i].object=Minimaltriples[sf][2]+" "+IndirectObject; minimalTriple[i].objectStart=MinimaltriplesPosition[sf][3];minimalTriple[i].objectEnd=MinimaltriplesPosition[sf][4];
                     minimalTriple[i].tripleType="verbal relation: ";
                    i++;
               }
                else break;
    }*/
        
    if(objectFillersNumber==0)
        for(int sf=0;sf<subjectFillersNumber;sf++){
        minimalTriple[i].subject=subjectFillers[sf]; minimalTriple[i].subjectStart=subjectFillersPositions[sf];minimalTriple[i].subjectEnd=subjectFillersPositions[sf];convertConnectedWords(i,0,0,minimalTriple[i].subject,minimalTriple[i].subjectStart);extendToNounGroupMinimal(0,i);
        minimalTriple[i].predicate=str; minimalTriple[i].predicateStart=verbs[verbNum].verbsPosition;
         minimalTriple[i].tripleType="verbal relation: ";
        i++;
        }
        
            
    return i;
    }
    public static int makeTriplesFromFillers1(int i,String str,int verbNum){
     int numberOfDirectObjects=0;


        for(int sf=0;sf<subjectFillersNumber;sf++)
         for(int of=0;of<objectFillersNumber;of++){
             if(objectFillersNumber>1){
//                 if(directObjectFillersPositions[of]==1){
//                         numberOfDirectObjects++;
                         triple[i].subject=subjectFillers[sf];triple[i].subjectStart=subjectFillersPositions[sf];triple[i].subjectEnd=subjectFillersPositions[sf];convertConnectedWords(i,0,0,triple[i].subject,triple[i].subjectStart);extendToNounGroup(0,i);
                        triple[i].predicate=str;triple[i].predicateStart=verbs[verbNum].verbsPosition;
                        triple[i].object=objectFillers[of];triple[i].objectStart=objectFillersPositions[of];triple[i].objectEnd=objectFillersPositions[of];convertConnectedWords(i,2,0,triple[i].subject,triple[i].predicateStart);
                        // triple[i].predicatePrep=objectFillersPrep[of];
                         
                         if(!Sentence.word[objectFillersPositions[of]].POStagword.matches("WRB"))
                           extendToNounGroup(2,i);
                         triple[i].tripleType="Maximum Verbal relation: "; 
                        if(Sentence.word[1].POSword.equals("How") & Sentence.word[2].POSword.equals("many") & triple[i].object.contains(Sentence.word[3].POSword)){
                           triple[i].predicate=triple[i].subject.split("many ")[1];
                           triple[i].object="N";
                        }
                        i=CommaBetweenTwoNoun(i,triple[i].objectStart,triple[i].subject);
                         // i++; triple[i].predicate=str;triple[i].subjectEnd=triplesPosition[i-1][1];sw=1;
                         EndNum++;
                         i=EndNum;
                        if(!triple[i].subject.matches(triple[i].subject))
                           i++;
//                }
//                else { 
//                    if(objectFillersNumber==2 & numberOfDirectObjects==1)
//                    objectFillers[of]=extendToNounGroupForAWordDirection(objectFillersPositions[of],objectFillers[of],sentence,3);
                    
//                    IndirectObject=IndirectObject+" "+objectFillers[of];
////                    }
            }else{
                        triple[i].subject=subjectFillers[sf]; triple[i].subjectStart=subjectFillersPositions[sf]; triple[i].subjectEnd=subjectFillersPositions[sf];convertConnectedWords(i,0,0,triple[i].subject,triple[i].subjectStart);extendToNounGroup(0,i);
                        triple[i].predicate=str; triple[i].predicateStart=verbs[verbNum].verbsPosition;
                        triple[i].object=objectFillers[of];triple[i].objectStart=objectFillersPositions[of];triple[i].objectEnd=objectFillersPositions[of];if(!Sentence.word[objectFillersPositions[of]].POStagword.matches("WRB"))convertConnectedWords(i,2,0,triple[i].subject,triple[i].predicateStart);extendToNounGroup(2,i);
                         triple[i].predicatePrep=objectFillersPrep[of];
                        triple[i].tripleType="Maximum Verbal relation: ";
                        if(Sentence.word[1].POSword.equals("How") & Sentence.word[2].POSword.equals("many") & triple[i].object.contains(Sentence.word[3].POSword)){
                           triple[i].predicate=triple[i].subject.split("many ")[1];
                           triple[i].object="N";
                           triple[i].tripleType="How many relation: ";
                        }
                        i=CommaBetweenTwoNoun(i,triple[i].objectStart,triple[i].subject);
                         // i++; triple[i].predicate=str;triple[i].subjectEnd=triplesPosition[i-1][1];sw=1;
                        EndNum++;
                        i=EndNum;
                        if(!triple[i].subject.matches(triple[i].subject)) 
                        i++;
            
            
            }
                
    }   
    if(subjectFillersNumber==0 & objectFillersNumber>0){
         for(int of=0;of<objectFillersNumber;of++){
         //  IndirectObject=IndirectObject+" "+objectFillers[of];
         // }
       triple[i].subject="";  
       triple[i].predicate=str; triple[i].predicateStart=verbs[verbNum].verbsPosition;
       triple[i].object=objectFillers[of];
        triple[i].predicatePrep=objectFillersPrep[of];
       triple[i].tripleType="Maximum verbal relation: ";
        
        i++;
    }  
    }     
 //   else if(numberOfDirectObjects==0 & objectFillersNumber>1)
//    {
//       triple[i].subject=subjectFillers[0];  triple[i].subjectStart=subjectFillersPositions[0]; triple[i].subjectEnd=subjectFillersPositions[0];extendToNounGroup(0,i,sentence);
//       triple[i].predicate=str; triple[i].predicateStart=verbs[verbNum].verbsPosition;
//       triple[i].tripleType="verbal relation: ";       
//        triple[i].object=triples[sf][2]+" "+IndirectObject; triple[i].objectStart=triplesPosition[sf][3];triple[i].objectEnd=triplesPosition[sf][4];
//               i++;
//    }
/*    else if (!IndirectObject.matches("")){
        int of=i;
        String predicate=triples[of-1][1];
        for(int sf=of-1;sf>0;sf--)
                if( triples[sf][1]==predicate){
                    triple[i].subject=triples[sf][0]; triple[i].subjectStart=triplesPosition[sf][0];triple[i].subjectEnd=triplesPosition[sf][1];
                    triple[i].predicate=triples[sf][1]; triple[i].predicateStart=triplesPosition[sf][1];
                    triple[i].object=triples[sf][2]+" "+IndirectObject; triple[i].objectStart=triplesPosition[sf][3];triple[i].objectEnd=triplesPosition[sf][4];
                     triple[i].tripleType="verbal relation: ";
                    i++;
               }
                else break;
    }*/
        
    if(objectFillersNumber==0)
        for(int sf=0;sf<subjectFillersNumber;sf++){
        triple[i].subject=subjectFillers[sf]; triple[i].subjectStart=subjectFillersPositions[sf];triple[i].subjectEnd=subjectFillersPositions[sf];convertConnectedWords(i,0,0,triple[i].subject,triple[i].subjectStart);extendToNounGroup(0,i);
        triple[i].predicate=str; triple[i].predicateStart=verbs[verbNum].verbsPosition;
         triple[i].tripleType="verbal relation: ";
        i++;
        }
        
            
    return i;
    }
    
   public static int AddCopularRelationTriple(String str,int j,int i){
        int counter;String subj="";
        triple[i].predicate=verbs[j].verbs;
        triple[i].predicateRole=verbs[i].verbsRoles;//triple[i].tripleType=2; 
        triple[i].predicateStart=verbs[j].verbsPosition;
        counter= verbs[j].verbsPosition-1;
        //Find out the subject
        if(Sentence.word[verbs[j].verbsPosition-1].POSword.matches("that")||Sentence.word[verbs[j].verbsPosition-1].POSword.matches("which")||
           Sentence.word[verbs[j].verbsPosition-1].POSword.matches("where")||Sentence.word[verbs[j].verbsPosition-1].POSword.matches("whom")||
           Sentence.word[verbs[j].verbsPosition-1].POSword.matches("who")){ 
            int RefrenceIndex=FindRerenceOfThat(counter);
            if(RefrenceIndex!=0){
                                  subj=extendToNounGroupForAWordDirection(RefrenceIndex,Sentence.word[RefrenceIndex].POSword,1);
                                  triple[i].subject=subj;
                                  triple[i].subjectStart=LeftLimitOfWord;
                                  triple[i].subjectEnd=RightLimitOfWord;
            }
            else
            extendToLeftCopular(0,i,counter);
            }
            else
            extendToLeftCopular(0,i,counter);  
        
       
        
        //Find out the object
        triple[i].objectStart=verbs[j].verbsPosition+1;
        triple[i].objectEnd=verbs[j].verbsPosition+1;
        triple[i].tripleType="Copular verb relation: ";
        counter= verbs[j].verbsPosition+1;
        extendToRightCopular(2,i,counter);
                        int sw=1;
                        //if(NPprepNp())
                        while(sw==1) 
                          sw=CommaBetweenTwoNoun(i,triple[i].objectEnd,triple[i].subject);
                        i=EndNum;
        return i;
   }
   public static int AddCopularRelationTripleMinimal(String str,int j,int i ){
       int counter;String subj;int sw1=0;int sw=0;
       int index1=0,k,dep1Index=0,gov1Index;
       String[] str1; String dep1="";String rel1,gov1;
       minimalTriple[i].predicate=verbs[j].verbs;triple[i].predicateRole=verbs[i].verbsRoles;//triple[i].tripleType=2; 
       minimalTriple[i].predicateStart=verbs[j].verbsPosition;
       counter= verbs[j].verbsPosition-1;
       if(Sentence.word[verbs[j].verbsPosition-1].POSword.matches("that")||Sentence.word[verbs[j].verbsPosition-1].POSword.matches("which")||
          Sentence.word[verbs[j].verbsPosition-1].POSword.matches("where")||Sentence.word[verbs[j].verbsPosition-1].POSword.matches("whom")||
          Sentence.word[verbs[j].verbsPosition-1].POSword.matches("who")){
           int RefrenceIndex=FindRerenceOfThat(counter);
           if(RefrenceIndex!=0){
              subj=extendToNounGroupForAWordDirectionMinimal(RefrenceIndex,Sentence.word[RefrenceIndex].POSword,1);
              minimalTriple[i].subject=subj;
              minimalTriple[i].subjectStart=LeftLimitOfWord;
              minimalTriple[i].subjectEnd=RightLimitOfWord;
            }
            else
            extendToLeftCopularMinimal(0,i,counter);
        }
        else
        extendToLeftCopularMinimal(0,i,counter);  
        if(minimalTriple[i].subjectStart-1>3){
        if(Sentence.word[minimalTriple[i].subjectStart-1].POStagword.matches("IN")){
        counter= minimalTriple[i].predicateStart-2;    
           extendToLeftCopularMinimal(0,i,counter);
        
        }
        else if(Sentence.word[minimalTriple[i].subjectStart-2].POStagword.matches("IN")){
           counter= minimalTriple[i].predicateStart-3;    
           extendToLeftCopularMinimal(0,i,counter);
        
        }        
        }   
        
       
       
       counter= verbs[j].verbsPosition+1;
                        minimalTriple[i].objectStart=verbs[j].verbsPosition+1;
                        minimalTriple[i].objectEnd=verbs[j].verbsPosition+1;
                        minimalTriple[i].tripleType="Copular verb relation: ";
                        extendToRightCopularMinimal(2,i,counter);
                        String Mintriple=minimalTriple[i].object;
                        sw=1;
                        //List<TypedDependency> tdl=null;;
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
                            if((gov1.matches(Mintriple) & gov1Index==minimalTriple[i].objectStart & (!relIsIndirectObject(rel1).matches(""))                   
                                    )){
                            if(sw==0){
                              i++;
                              minimalTriple[i].subject=minimalTriple[i-1].subject;minimalTriple[i].subjectStart=minimalTriple[i-1].subjectStart; minimalTriple[i].subjectEnd=minimalTriple[i-1].subjectEnd;
                              minimalTriple[i].predicate=minimalTriple[i-1].predicate;minimalTriple[i].predicateStart=minimalTriple[i-1].objectStart;
                              minimalTriple[i].object=Mintriple;minimalTriple[i].objectStart=minimalTriple[i-1].objectStart; minimalTriple[i].subjectEnd=minimalTriple[i-1].subjectEnd; // extendToNounGroupMinimal(2,i,str);
                            }        
                            minimalTriple[i].tripleType="verbal relation(NP-VPc-NP-prep-NP): ";
                            str1=rel1.split("nmod:");
                            minimalTriple[i].object=minimalTriple[i].object+" "+str1[1]+" "+extendToNounGroupForAWordDirectionMinimal(dep1Index,dep1,1);
                            minimalTriple[i].objectEnd=dep1Index;
                            sw=0;
                            }
                               
                            index1++;      
                            }//end for
                       /* if(NPprepNp(minimalTriple[i].objectEnd)!=0){ 
                          i++;
                         
                          minimalTriple[i].predicate=minimalTriple[i-1].predicate;minimalTriple[i].predicateStart=minimalTriple[i-1].predicateStart;
                          minimalTriple[i].tripleType="verbal relation(NP-VPc-NP-prep-NP): ";
                            
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
                            if((gov1.matches(minimalTriple[i].predicate) & gov1Index==minimalTriple[i].predicateStart & (rel1.matches("nsubj")|| rel1.matches("nsubjpass"))                   
                                    )){
                            sw1=1; 
                            break;
                            }
                               
                            index1++;      
                            }//end for
                            if(sw1==1){
                                minimalTriple[i].subject=dep1;
                                minimalTriple[i].subjectStart=dep1Index;
                                minimalTriple[i].subjectEnd=dep1Index;
                            }else{
                          minimalTriple[i].subject=minimalTriple[i-1].subject;minimalTriple[i].subjectStart=minimalTriple[i-1].subjectStart;minimalTriple[i].subjectEnd=minimalTriple[i-1].subjectEnd;
                            }
                          extendToRightCopularMinimal(2,i,NPprepNp(Minimaltriple[i-1].objectEnd),sentence); 
                          minimalEndNum=i; 
                        
                        }
                        while(sw==1) 
                          sw=CommaBetweenTwoNounMinimal(i,minimalTriple[i].objectEnd,minimalTriple[i].object,sentence);
                      */
                        i=minimalEndNum+1;
        return i;
   }
   
   //relatin extraction functions
  
         public static void  replaceTiWithPhrases(int i){
   
       triple[i].subject = triple[i].subject.replace("T1zero", NamedEntityRecognition.namedEntity[0].phrase);
        triple[i].subject = triple[i].subject.replace("T1one", NamedEntityRecognition.namedEntity[1].phrase);
        triple[i].subject = triple[i].subject.replace("T1two", NamedEntityRecognition.namedEntity[2].phrase);
        triple[i].subject = triple[i].subject.replace("T1three", NamedEntityRecognition.namedEntity[3].phrase);
        triple[i].subject = triple[i].subject.replace("T1four", NamedEntityRecognition.namedEntity[4].phrase);
        triple[i].subject = triple[i].subject.replace("T1five", NamedEntityRecognition.namedEntity[5].phrase);
        triple[i].subject = triple[i].subject.replace("T1six", NamedEntityRecognition.namedEntity[6].phrase);
        triple[i].subject = triple[i].subject.replace("T1seven", NamedEntityRecognition.namedEntity[7].phrase);
        triple[i].subject = triple[i].subject.replace("T1eight", NamedEntityRecognition.namedEntity[8].phrase);
        triple[i].subject = triple[i].subject.replace("T1nine", NamedEntityRecognition.namedEntity[9].phrase);
        triple[i].subject = triple[i].subject.replace("T1ten", NamedEntityRecognition.namedEntity[10].phrase);
        triple[i].subject = triple[i].subject.replace("T1eleven", NamedEntityRecognition.namedEntity[11].phrase);
        triple[i].subject = triple[i].subject.replace("T1twelve", NamedEntityRecognition.namedEntity[12].phrase);
        triple[i].subject = triple[i].subject.replace("T1thirteen", NamedEntityRecognition.namedEntity[13].phrase);
        triple[i].subject = triple[i].subject.replace("T1flourteen", NamedEntityRecognition.namedEntity[14].phrase);
        triple[i].subject = triple[i].subject.replace("T1fifteen", NamedEntityRecognition.namedEntity[15].phrase);
       
        triple[i].predicate = triple[i].predicate.replace("T1zero", NamedEntityRecognition.namedEntity[0].phrase);
        triple[i].predicate = triple[i].predicate.replace("T1one", NamedEntityRecognition.namedEntity[1].phrase);
        triple[i].predicate = triple[i].predicate.replace("T1two", NamedEntityRecognition.namedEntity[2].phrase);
        triple[i].predicate = triple[i].predicate.replace("T1three", NamedEntityRecognition.namedEntity[3].phrase);
        triple[i].predicate = triple[i].predicate.replace("T1four", NamedEntityRecognition.namedEntity[4].phrase);
        triple[i].predicate = triple[i].predicate.replace("T1five", NamedEntityRecognition.namedEntity[5].phrase);
        triple[i].predicate = triple[i].predicate.replace("T1six", NamedEntityRecognition.namedEntity[6].phrase);
        triple[i].predicate = triple[i].predicate.replace("T1seven", NamedEntityRecognition.namedEntity[7].phrase);
        triple[i].predicate = triple[i].predicate.replace("T1eight", NamedEntityRecognition.namedEntity[8].phrase);
        triple[i].predicate = triple[i].predicate.replace("T1nine", NamedEntityRecognition.namedEntity[9].phrase);
        triple[i].predicate = triple[i].predicate.replace("T1ten", NamedEntityRecognition.namedEntity[10].phrase);
        triple[i].predicate = triple[i].predicate.replace("T1eleven", NamedEntityRecognition.namedEntity[11].phrase);
        triple[i].predicate = triple[i].predicate.replace("T1twelve", NamedEntityRecognition.namedEntity[12].phrase);
        triple[i].predicate = triple[i].predicate.replace("T1thirteen", NamedEntityRecognition.namedEntity[13].phrase);
        triple[i].predicate = triple[i].predicate.replace("T1flourteen", NamedEntityRecognition.namedEntity[14].phrase);
        triple[i].predicate = triple[i].predicate.replace("T1fifteen", NamedEntityRecognition.namedEntity[15].phrase);
       
        triple[i].object = triple[i].object.replace("T1zero", NamedEntityRecognition.namedEntity[0].phrase);
        triple[i].object = triple[i].object.replace("T1one", NamedEntityRecognition.namedEntity[1].phrase);
        triple[i].object = triple[i].object.replace("T1two", NamedEntityRecognition.namedEntity[2].phrase);
        triple[i].object = triple[i].object.replace("T1three", NamedEntityRecognition.namedEntity[3].phrase);
        triple[i].object = triple[i].object.replace("T1four", NamedEntityRecognition.namedEntity[4].phrase);
        triple[i].object = triple[i].object.replace("T1five", NamedEntityRecognition.namedEntity[5].phrase);
        triple[i].object = triple[i].object.replace("T1six", NamedEntityRecognition.namedEntity[6].phrase);
        triple[i].object = triple[i].object.replace("T1seven", NamedEntityRecognition.namedEntity[7].phrase);
        triple[i].object = triple[i].object.replace("T1eight", NamedEntityRecognition.namedEntity[8].phrase);
        triple[i].object = triple[i].object.replace("T1nine", NamedEntityRecognition.namedEntity[9].phrase);
        triple[i].object = triple[i].object.replace("T1ten", NamedEntityRecognition.namedEntity[10].phrase);
        triple[i].object = triple[i].object.replace("T1eleven", NamedEntityRecognition.namedEntity[11].phrase);
        triple[i].object = triple[i].object.replace("T1twelve", NamedEntityRecognition.namedEntity[12].phrase);
        triple[i].object = triple[i].object.replace("T1thirteen", NamedEntityRecognition.namedEntity[13].phrase);
        triple[i].object = triple[i].object.replace("T1flourteen", NamedEntityRecognition.namedEntity[14].phrase);
        triple[i].object = triple[i].object.replace("T1fifteen", NamedEntityRecognition.namedEntity[15].phrase);
       
    }
    public static void  replaceTiWithPrhasesMinimal(int i){
   
        minimalTriple[i].subject = minimalTriple[i].subject.replace("T1zero", NamedEntityRecognition.namedEntity[0].phrase);
        minimalTriple[i].subject = minimalTriple[i].subject.replace("T1one", NamedEntityRecognition.namedEntity[1].phrase);
        minimalTriple[i].subject = minimalTriple[i].subject.replace("T1two", NamedEntityRecognition.namedEntity[2].phrase);
        minimalTriple[i].subject = minimalTriple[i].subject.replace("T1three", NamedEntityRecognition.namedEntity[3].phrase);
        minimalTriple[i].subject = minimalTriple[i].subject.replace("T1four", NamedEntityRecognition.namedEntity[4].phrase);
        minimalTriple[i].subject = minimalTriple[i].subject.replace("T1five", NamedEntityRecognition.namedEntity[5].phrase);
        minimalTriple[i].subject = minimalTriple[i].subject.replace("T1six", NamedEntityRecognition.namedEntity[6].phrase);
        minimalTriple[i].subject = minimalTriple[i].subject.replace("T1seven", NamedEntityRecognition.namedEntity[7].phrase);
        minimalTriple[i].subject = minimalTriple[i].subject.replace("T1eight", NamedEntityRecognition.namedEntity[8].phrase);
        minimalTriple[i].subject = minimalTriple[i].subject.replace("T1nine", NamedEntityRecognition.namedEntity[9].phrase);
        minimalTriple[i].subject = minimalTriple[i].subject.replace("T1ten", NamedEntityRecognition.namedEntity[10].phrase);
        minimalTriple[i].subject = minimalTriple[i].subject.replace("T1eleven", NamedEntityRecognition.namedEntity[11].phrase);
        minimalTriple[i].subject = minimalTriple[i].subject.replace("T1twelve", NamedEntityRecognition.namedEntity[12].phrase);
        minimalTriple[i].subject = minimalTriple[i].subject.replace("T1thirteen", NamedEntityRecognition.namedEntity[13].phrase);
        minimalTriple[i].subject = minimalTriple[i].subject.replace("T1flourteen", NamedEntityRecognition.namedEntity[14].phrase);
        minimalTriple[i].subject = minimalTriple[i].subject.replace("T1fifteen", NamedEntityRecognition.namedEntity[15].phrase);
       
        minimalTriple[i].predicate = minimalTriple[i].predicate.replace("T1zero", NamedEntityRecognition.namedEntity[0].phrase);
        minimalTriple[i].predicate = minimalTriple[i].predicate.replace("T1one", NamedEntityRecognition.namedEntity[1].phrase);
        minimalTriple[i].predicate = minimalTriple[i].predicate.replace("T1two", NamedEntityRecognition.namedEntity[2].phrase);
        minimalTriple[i].predicate = minimalTriple[i].predicate.replace("T1three", NamedEntityRecognition.namedEntity[3].phrase);
        minimalTriple[i].predicate = minimalTriple[i].predicate.replace("T1four", NamedEntityRecognition.namedEntity[4].phrase);
        minimalTriple[i].predicate = minimalTriple[i].predicate.replace("T1five", NamedEntityRecognition.namedEntity[5].phrase);
        minimalTriple[i].predicate = minimalTriple[i].predicate.replace("T1six", NamedEntityRecognition.namedEntity[6].phrase);
        minimalTriple[i].predicate = minimalTriple[i].predicate.replace("T1seven", NamedEntityRecognition.namedEntity[7].phrase);
        minimalTriple[i].predicate = minimalTriple[i].predicate.replace("T1eight", NamedEntityRecognition.namedEntity[8].phrase);
        minimalTriple[i].predicate = minimalTriple[i].predicate.replace("T1nine", NamedEntityRecognition.namedEntity[9].phrase);
        minimalTriple[i].predicate = minimalTriple[i].predicate.replace("T1ten", NamedEntityRecognition.namedEntity[10].phrase);
        minimalTriple[i].predicate = minimalTriple[i].predicate.replace("T1eleven", NamedEntityRecognition.namedEntity[11].phrase);
        minimalTriple[i].predicate = minimalTriple[i].predicate.replace("T1twelve", NamedEntityRecognition.namedEntity[12].phrase);
        minimalTriple[i].predicate = minimalTriple[i].predicate.replace("T1thirteen", NamedEntityRecognition.namedEntity[13].phrase);
        minimalTriple[i].predicate = minimalTriple[i].predicate.replace("T1flourteen", NamedEntityRecognition.namedEntity[14].phrase);
        minimalTriple[i].predicate = minimalTriple[i].predicate.replace("T1fifteen", NamedEntityRecognition.namedEntity[15].phrase);

        
        minimalTriple[i].object = minimalTriple[i].object.replace("T1zero", NamedEntityRecognition.namedEntity[0].phrase);
        minimalTriple[i].object = minimalTriple[i].object.replace("T1one", NamedEntityRecognition.namedEntity[1].phrase);
        minimalTriple[i].object = minimalTriple[i].object.replace("T1two", NamedEntityRecognition.namedEntity[2].phrase);
        minimalTriple[i].object = minimalTriple[i].object.replace("T1three", NamedEntityRecognition.namedEntity[3].phrase);
        minimalTriple[i].object = minimalTriple[i].object.replace("T1four", NamedEntityRecognition.namedEntity[4].phrase);
        minimalTriple[i].object = minimalTriple[i].object.replace("T1five", NamedEntityRecognition.namedEntity[5].phrase);
        minimalTriple[i].object = minimalTriple[i].object.replace("T1six", NamedEntityRecognition.namedEntity[6].phrase);
        minimalTriple[i].object = minimalTriple[i].object.replace("T1seven", NamedEntityRecognition.namedEntity[7].phrase);
        minimalTriple[i].object = minimalTriple[i].object.replace("T1eight", NamedEntityRecognition.namedEntity[8].phrase);
        minimalTriple[i].object = minimalTriple[i].object.replace("T1nine", NamedEntityRecognition.namedEntity[9].phrase);
        minimalTriple[i].object = minimalTriple[i].object.replace("T1ten", NamedEntityRecognition.namedEntity[10].phrase);
        minimalTriple[i].object = minimalTriple[i].object.replace("T1eleven", NamedEntityRecognition.namedEntity[11].phrase);
        minimalTriple[i].object = minimalTriple[i].object.replace("T1twelve", NamedEntityRecognition.namedEntity[12].phrase);
        minimalTriple[i].object = minimalTriple[i].object.replace("T1thirteen", NamedEntityRecognition.namedEntity[13].phrase);
        minimalTriple[i].object = minimalTriple[i].object.replace("T1flourteen", NamedEntityRecognition.namedEntity[14].phrase);
        minimalTriple[i].object = minimalTriple[i].object.replace("T1fifteen", NamedEntityRecognition.namedEntity[15].phrase);
   
    }
   public static void WriteTriplesInOutput(int row){
      String Output = "../Armin/sampleText/S2352179114200056Out.txt";

        try {
             FileWriter fileWriter = new FileWriter(Output,true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
//            if(minimalEndNum>0 /*& //CompSuper==1*/){
             bufferedWriter.newLine();
             ////bufferedWriter.write("---------------------------------- ");
 //            bufferedWriter.newLine(); 
//            bufferedWriter.newLine();
           ////  bufferedWriter.write(Integer.toString(row));
           ////  bufferedWriter.newLine();
             //bufferedWriter.write(line); 
//            }
//             bufferedWriter.write("  ");
//             bufferedWriter.write("******************************************************************");
//             bufferedWriter.newLine();
 //            bufferedWriter.write(line); 
//            bufferedWriter.newLine();
//             bufferedWriter.newLine();
            
//             bufferedWriter.write("______________________________________________________Maximal Relations______________________________________________________");
//             bufferedWriter.newLine();
//             bufferedWriter.newLine();
//             System.out.println();        
         
             System.out.println("-------------------Maximal Relations--------------------");
             System.out.println();
          //  if(//CompSuper==1) 
       /*     for(int j=0;j<=EndNum;j++){
        //    replaceTiWithPhrases(j);  
                if((triple[j].subject!="" && triple[j].object!="")|| (!triple[j].subject.matches(triple[j].object))){
                   ////   bufferedWriter.write(typeTriples[j]);bufferedWriter.write("------>");
                   
                   bufferedWriter.write(triple[j].subject); 
                      bufferedWriter.write("*");
                      bufferedWriter.write(triple[j].predicate);
                      bufferedWriter.write("*");
                      bufferedWriter.write(triple[j].object);
                      bufferedWriter.newLine();
                  
                      System.out.print(typeTriples[j]);System.out.print("------>");
                      System.out.print(triple[j].subject+'*'+triple[j].predicate+'*'+triple[j].object); 
                      System.out.println();
                    }
                    else if (triple[j].subject!="" && triple[j].object==""){
                    ////     bufferedWriter.write(typeTriples[j]);bufferedWriter.write("------>");
                         bufferedWriter.write(triple[j].subject); 
                         bufferedWriter.write("*");
                         bufferedWriter.write(triple[j].predicate);
                         bufferedWriter.write("*");
                        //// bufferedWriter.write("---");
                         bufferedWriter.newLine();
                        
                         System.out.print(typeTriples[j]);System.out.print("------>");
                         System.out.print(triple[j].subject+'*'+triple[j].predicate+'*'+triple[j].object); 
                         System.out.println();
                        }
                 
                  
           }
       */     
    /*        
            //---------------------------
              for(int j=0;j<EndNum;j++){
                  replaceTiWithPhrases(j); 
                //if(triplesState[j]!=0){
               // if((triple[j].subject!="" && triple[j].object!="")&(!triple[j].subject.contains(triple[j].object))){
                 if(( triple[j].object!="")&(!triple[j].subject.contains(triple[j].object))
                         &(!triple[j].predicate.matches(triple[j].object))){
                 
                  bufferedWriter.newLine(); 
                  bufferedWriter.write(typeTriples[j]);bufferedWriter.write("------>"); 
                  bufferedWriter.write(triple[j].subject); 
                  bufferedWriter.write("*");
                  bufferedWriter.write(triple[j].predicate);
                  bufferedWriter.write(" ");
                  bufferedWriter.write(triplesVerbPrep[j]);
                  bufferedWriter.write("*");
                  bufferedWriter.write(triple[j].object);
                               
                  
                  System.out.print(typeTriples[j]);System.out.print("------>");
                  System.out.print(triple[j].subject+'*'+triple[j].predicate+' '+triplesVerbPrep[j]+'*'+triple[j].object); 
                  System.out.print("         ");
                  if(!tripleCondition[j][0].matches("")){
                        bufferedWriter.write("                       ");
                        bufferedWriter.write(tripleCondition[j][0]+'*'+tripleCondition[j][1]+'*'+tripleCondition[j][2]);
                        System.out.print("                      ");
                        System.out.print(tripleCondition[j][0]+'*'+tripleCondition[j][1]+'*'+tripleCondition[j][2]); 
                     }
        //             bufferedWriter.newLine();
                     System.out.println();
                     }else if (triple[j].subject!="" && triple[j].object==""){
                         bufferedWriter.write(typeTriples[j]);bufferedWriter.write("------>"); 
                         bufferedWriter.write(triple[j].subject); 
                         bufferedWriter.write("*");
                         bufferedWriter.write(triple[j].predicate);
                         bufferedWriter.write("*");
                         bufferedWriter.write("---");
//                         bufferedWriter.newLine();
                         System.out.print(typeTriples[j]);System.out.print("------");
                         System.out.print(triple[j].subject+'*'+triple[j].predicate+'*'+triple[j].object); 
                         System.out.println();
                    }
               // }       
           }
  */          
      //      bufferedWriter.newLine();
//            bufferedWriter.write("______________________________________________________Minimal Relations______________________________________________________");
//            bufferedWriter.newLine();
//            bufferedWriter.newLine();
//            System.out.println();
//            System.out.println("--------------------Minimal Relations--------------------");
//            System.out.println();
         //if(//CompSuper== CompSuper=1;1)   
        //  if(//CompSuper==1)      
         for(int j=0;j<minimalEndNum;j++){
              replaceTiWithPrhasesMinimal(j); 
                //if(triple[j].tripleState!=0){
               // if((Minimaltriple[j].subject!="" && minimalTriple[j].object!="")&(!minimalTriple[j].subject.contains(minimalTriple[j].object))){
                 if(( minimalTriple[j].object!="")&(!minimalTriple[j].subject.contains(minimalTriple[j].object))
                         &(!minimalTriple[j].predicate.matches(minimalTriple[j].object))){
                 
                  bufferedWriter.newLine(); 
                 //// bufferedWriter.write(minimalTriple[j].tripleType);bufferedWriter.write("------>"); 
                  bufferedWriter.write(minimalTriple[j].subject); 
                  bufferedWriter.write("*");
                  bufferedWriter.write(minimalTriple[j].predicate);
                  bufferedWriter.write(" ");
                  bufferedWriter.write(minimalTriple[j].predicatePrep);
                  bufferedWriter.write("*");
                  bufferedWriter.write(minimalTriple[j].object);
                               
                  
                  System.out.print(minimalTriple[j].tripleType);System.out.print("------>");
                  System.out.print(minimalTriple[j].subject+'*'+minimalTriple[j].predicate+' '+minimalTriple[j].predicatePrep+'*'+minimalTriple[j].object); 
                  System.out.print("         ");
                  if(!minimalTriple[j].typeCondition.matches("")){
                        bufferedWriter.write("                       ");
                        bufferedWriter.write(minimalTriple[j].typeCondition+'*'+minimalTriple[j].sortTypeCondition+'*'+minimalTriple[j].elementCondition);
                        System.out.print("                      ");
                        System.out.print(minimalTriple[j].typeCondition+'*'+minimalTriple[j].sortTypeCondition+'*'+minimalTriple[j].elementCondition);
                     }
        //             bufferedWriter.newLine();
                     System.out.println();
                     }else if (minimalTriple[j].subject!="" && minimalTriple[j].object==""){
                       ////  bufferedWriter.write(minimalTriple[j].tripleType);bufferedWriter.write("------>"); 
                         bufferedWriter.write(minimalTriple[j].subject); 
                         bufferedWriter.write("*");
                         bufferedWriter.write(minimalTriple[j].predicate);
                         bufferedWriter.write("*");
                         bufferedWriter.write("---");
//                         bufferedWriter.newLine();
                         System.out.print(minimalTriple[j].tripleType);System.out.print("------");
                         System.out.print(minimalTriple[j].subject+'*'+minimalTriple[j].predicate+'*'+minimalTriple[j].object); 
                         System.out.println();
                    }
                //}       
           }
        //}     
          bufferedWriter.close();
        }
        catch(IOException ex) {
            System.out.println("Error writing to file '"+ Output + "'");
            }
        
   }
     
   public static void triplesGeneration(String line) throws Exception {
       initialization(line);
       VerbBasedRelations();
       CommaRelation();
     // NounPhraseRelations(tdl,replacedSent);
      GenitiveRelations();
      IndirectVerbal();
      IndirectVerbalNew();
      ComparativeRelations();
      SuperlativeRelations();
     processTriples();
   }
      public static void MakeGenitiveRelation(int gov2Index,int dep2Index,String rel2) {
    
    int j=0;
    if(/*GovOrDepAreValid(gov2Index,dep2Index)==1 &*/ !Sentence.word[gov2Index].POStagword.matches("CD") & IsAQuotationPhrase(rel2,gov2Index,dep2Index)==0 & (relIsNModTo(rel2,gov2Index,dep2Index)==0 ||relIsNModTo(rel2,gov2Index,dep2Index)==2)){
    for(j=0;j<minimalEndNum;j++){
        if((minimalTriple[j].subjectStart<=gov2Index && minimalTriple[j].subjectEnd>=gov2Index && minimalTriple[j].objectStart<=dep2Index && minimalTriple[j].objectEnd>=dep2Index )||
           (minimalTriple[j].subjectEnd==gov2Index && minimalTriple[j].objectStart<=dep2Index && minimalTriple[j].objectEnd>=dep2Index )
              )
                    break;
    }
    if(j==minimalEndNum){
// String Class=Main.main(POSword[gov2Index]);
              //if(!Class.matches("Null")){
             //missed predicates
            //minimalEndNum++;
           int i=minimalEndNum;  
            if(rel2.matches("dep")||rel2.matches("nmod:of")||rel2.matches("nmod:poss")||rel2.matches("nmod:for")||rel2.matches("nmod:in")||rel2.matches("nmod:at")||rel2.matches("nmod:on")||rel2.matches("nmod:by")||rel2.matches("nmod:near")||rel2.matches("nmod:from")
                    ||rel2.matches("nmod:with")||rel2.matches("nmod:under")||rel2.matches("nmod:against")||rel2.matches("nmod:during")
                    ||rel2.matches("nmod:to")||rel2.matches("nmod:agent")||rel2.matches("nmod:like")||rel2.contains("nmod:")){
                    minimalTriple[i].subject=Sentence.word[gov2Index].POSword; minimalTriple[i].subjectStart=gov2Index;minimalTriple[i].subjectEnd=gov2Index;extendToNounGroupMinimal(0,i);
                    minimalTriple[i].predicate="has a relation with"; minimalTriple[i].predicateStart=0;
                    minimalTriple[i].object=Sentence.word[dep2Index].POSword; minimalTriple[i].objectStart=dep2Index;minimalTriple[i].objectEnd=dep2Index;extendToNounGroupMinimal(2,i);
                  //minimalTriple[i].tripleType="Genitive relation(missed predicate):Function1 ";
                    minimalEndNum++;  
                    if(rel2.matches("nmod:to")){
                         String[] temp=minimalTriple[i].subject.split(" ");
                         minimalTriple[i].subject="";
                         for(int k=0;k<temp.length-1;k++)
                             minimalTriple[i].subject=minimalTriple[i].subject+" "+temp[k];
                    }
                  
            }
              //missed subjects
/*              else if(rel2.matches("nmod:of")||rel2.matches("nmod:poss")||rel2.matches("nmod:for")){
                  minimalTriple[i].subject="?subject "; minimalTriple[i].subjectStart= minimalTriple[i].subjectEnd=0;
                  minimalTriple[i].predicate=POSword[gov2Index]; minimalTriple[i].predicateStart=gov2Index;extendToNounGroupMinimal(1,i,str);
                  minimalTriple[i].object=POSword[dep2Index]; minimalTriple[i].objectStart=dep2Index;minimalTriple[i].objectEnd=dep2Index;extendToNounGroupMinimal(2,i,str);
                  //minimalTriple[i].tripleType="Genitive relation(missed subject):Function1 ";
                  
                  minimalEndNum++;
                  
              }  
              else if(rel2.matches("dep")){
                  minimalTriple[i].subject="?subject "; minimalTriple[i].subjectStart= minimalTriple[i].subjectEnd=0;
                  minimalTriple[i].object=POSword[gov2Index];minimalTriple[i].objectStart=gov2Index;minimalTriple[i].objectEnd=gov2Index;extendToNounGroupMinimal(2,i,str);
                  minimalTriple[i].predicate=POSword[dep2Index]; minimalTriple[i].predicateStart=dep2Index;extendToNounGroupMinimal(1,i,str);
                  //minimalTriple[i].tripleType="Genitive relation(missed subject):Function1 ";
                   minimalEndNum++;
              }
*/        
    }   
    }
   }
    public static int  relIsNModTo(String rel,int govIndex,int depIndex){
//0 : is not nmod:to, 
//1: is nmod:to and without similar, near  and .... which should be removed
//2: is nmod:to and with similar, near, adjacent and ...
        int i=depIndex-1;       
        if(rel.matches("nmod:to"))
                if(Sentence.word[govIndex].POSword.matches("adjacent")|| Sentence.word[govIndex].POSword.matches("near")||Sentence.word[govIndex].POSword.matches("similar"))
                     return 2;
                else      
                     return 1; //  which should be removed
                
       else 
           return 0; 
    }
    
    public static void processTriples(){
        int rowLine=41;

        TriplesReview();
        convertion();
        WriteTriplesInOutput(rowLine);
     
   
   }
    public static void initialization(String line){
                   //CompSuper=0;            
                   minimalEndNum=0;
                   Sentence sentence=new Sentence(line);
                   for(int i=0;i<60;i++){
                      
                     triple[i]=new Triple("","","");
                     minimalTriple[i]=new Triple("","","");
                     //namedEntity[i].phraseTagmeLinksRecognizer=0;
                    }
                      
                   
                    for(int i=0;i<15;i++){
                        objectFillersPrep[i]=""; 
                    }
                                       
                  
                  
                   
                   
   }
    public static int CommaBetweenTwoNoun(int i,int objectPosition,String Object){
    int sw=0;
    EndNum=i;
                  if(objectPosition!=1)   
                   if(Sentence.word[objectPosition-1].POSword.matches(",")){
                         i++;
                         EndNum=i;
                         triple[i].subject=triple[i-1].subject;triple[i].subjectStart=triple[i-1].subjectStart;triple[i].subjectEnd=triple[i-1].subjectEnd;
                         triple[i].predicate=triple[i-1].predicate;triple[i].subjectEnd=triple[i-1].subjectEnd;
                         triple[i].tripleType=triple[i-1].tripleType;
                         String str1=extendToNounGroupForAWordDirection(objectPosition-2,Sentence.word[objectPosition-2].POSword,1);
 
                         triple[i].object=str1;triple[i].objectEnd=objectPosition-2;triple[i].objectStart=LeftLimitOfWord;
                     }
                    /* if(objectPosition!=wordsNumber-1) 
                   if(POSword[objectPosition+1].matches(",")){
                         sw=1;                       
                         String str1=extendToNounGroupForAWordDirection(objectPosition+2,POSword[objectPosition+2],line,2);
                        triple[i].object=triple[i].subject+" "+str1;triple[i].objectStart=objectPosition+2;triple[i].objectStart=objectPosition+4;triple[i].objectEnd=RightLimitOfWord;
                        
                         return sw;
                      }*/
                           
                                         
                   
return sw;   
} 
   public static int CommaBetweenTwoNounMinimal(int i,int objectPosition,String Object){
    int sw=0;
    minimalEndNum=i;
                  if(objectPosition!=1)   
                   if(Sentence.word[objectPosition-1].POSword.matches(",")){
                         i++;
                         minimalEndNum=i;
                         minimalTriple[i].subject=minimalTriple[i-1].subject;minimalTriple[i].subjectStart=minimalTriple[i-1].subjectStart;minimalTriple[i].subjectEnd=minimalTriple[i-1].subjectStart;
                         minimalTriple[i].predicate=minimalTriple[i-1].predicate;minimalTriple[i].subjectEnd=minimalTriple[i-1].subjectEnd;
                        minimalTriple[i].tripleType=minimalTriple[i-1].tripleType;
                         String str1=extendToNounGroupForAWordDirectionMinimal(objectPosition-2,Sentence.word[objectPosition-2].POSword,1);
 
                         minimalTriple[i].object=str1;minimalTriple[i].objectEnd=objectPosition-2;minimalTriple[i].objectStart=LeftLimitOfWord;
                     }
                     if(objectPosition!=wordsNumber) 
                     if(Sentence.word[objectPosition+1].POSword.matches(",")){
                         sw=1;                       
// i++;
                       //  triple[i].subject=triple[i-1].subject;triple[i].subjectStart=triplesPosition[i-1][0];triple[i].subjectEnd=triplesPosition[i-1][1];
                        // triple[i].predicate=triple[i-1].predicate;triple[i].subjectEnd=triplesPosition[i-1][1];
                        String str1=extendToNounGroupForAWordDirectionMinimal(objectPosition+2,Sentence.word[objectPosition+2].POSword,2);
                        minimalTriple[i].object=minimalTriple[i].object+" "+str1;minimalTriple[i].objectStart=objectPosition+2;minimalTriple[i].objectStart=objectPosition+4;minimalTriple[i].objectEnd=RightLimitOfWord;
                        
                         return sw;
                      }
                           
                                         
                   
return sw; 
}
  public static void extendToNounGroup(int i,int triplenum){
   
   //we must extend the object that is only one Word to a noun group 
            int counter=0;            
                    if(i==0)
                        counter=triple[triplenum].subjectStart+1;
                    if(i==2)
                        counter=triple[triplenum].objectEnd+1;
               
                int sw1=0;
               if(counter<wordsNumber)
                while((Sentence.word[counter].POStagword.equals("NN")||
                        IsNoun(counter)==1||
                        Sentence.word[counter].POStagword.equals("NNS")||Sentence.word[counter].POStagword.equals("CD")||Sentence.word[counter].POStagword.equals("IN")||
                       Sentence.word[counter].POStagword.equals("NNP")||Sentence.word[counter].POStagword.equals("JJ")||Sentence.word[counter].POStagword.equals("JJS")||Sentence.word[counter].POStagword.equals("PRP$")
                      ||Sentence.word[counter].POStagword.equals("POS")
                        //||Sentence.word[counter].POStagword.equals('"')
                      ||Sentence.word[counter].POStagword.equals("TO")
                         ||Sentence.word[counter].POStagword.equals("NNPS")
                          ||Sentence.word[counter].POStagword.equals("PRP")
                        ||Sentence.word[counter].POStagword.equals("DT")
                        ||Sentence.word[counter].POStagword.equals("RB"))&(counter!=wordsNumber))
               {
                
                      sw1=1;
                     
                     if(i==0)
                        triple[triplenum].subject=triple[triplenum].subject+" "+Sentence.word[counter].POSword;     
                        triple[triplenum].subjectEnd=counter;
                    if(i==2)
                        triple[triplenum].objectEnd=counter;
                        triple[triplenum].object=triple[triplenum].object+" "+Sentence.word[counter].POSword;
                  if(counter!=wordsNumber-1)counter++;else break;
                 }
              
              
              //----------------------------------------------------------
              //we must extend the object that is one Word to noun group 
            //  if (sw1==0){    
             if(i==0)
                        counter=triple[triplenum].subjectStart-1;
                    if(i==2)
                        counter=triple[triplenum].objectStart-1;  
            
            //counter= counter-2;
                if(counter!=0){
               while((Sentence.word[counter].POStagword.equals("NN")||
                       IsNoun(counter)==1||
                       Sentence.word[counter].POStagword.equals("NNS")||Sentence.word[counter].POStagword.equals("IN")||Sentence.word[counter].POStagword.equals("CD")||
                       Sentence.word[counter].POStagword.equals("NNP")||Sentence.word[counter].POStagword.equals("JJ")||Sentence.word[counter].POStagword.equals("JJS")||Sentence.word[counter].POStagword.equals("PRP$")
                       ||Sentence.word[counter].POStagword.equals("POS")
                       //||Sentence.word[counter].POStagword.equals('"')
                       ||Sentence.word[counter].POStagword.equals("TO")
                       ||Sentence.word[counter].POStagword.equals("NNPS")
                         ||Sentence.word[counter].POStagword.equals("PRP")
                       ||Sentence.word[counter].POStagword.equals("DT")
                       ||Sentence.word[counter].POStagword.equals("RB"))&(counter!=wordsNumber))
                
              {
                  if(i==0)   
                  triple[triplenum].subject=Sentence.word[counter].POSword+" "+triple[triplenum].subject;
                  else if(i==2)
                     triple[triplenum].object=Sentence.word[counter].POSword+" "+triple[triplenum].object;  
                  //shayad
                    if(i==0)
                        triple[triplenum].subjectStart=counter;
                    if(i==2)
                        triple[triplenum].objectStart=counter;   
                  // triplesPosition[triplenum][i]--; 
                  if(counter!=1)counter--;else break;
                 }
                }
             // }
              
              //----------------------------------------------------------
              
   
   }
   public static void extendToNounGroupMinimal(int i,int minimalEndNum ){
   
   //we must extend the object that is only one Word to a noun group 
            int counter=0;          
            
            
                if(i==0)
                        counter= minimalTriple[minimalEndNum].subjectStart+1;
                if(i==1)
                        counter= minimalTriple[minimalEndNum].predicateStart+1;
               
                if(i==2)
                        counter= minimalTriple[minimalEndNum].objectEnd+1;
               
                int sw1=0;
                int current=counter-1;
                if(counter<wordsNumber)
                while((Sentence.word[counter].POStagword.equals("NN")||
                        //!Sentence.word[counter].POStagword.equals(",")||
                        Sentence.word[counter].POStagword.equals("NNS")
                        ||Sentence.word[counter].POStagword.equals("CD")
                        //||Sentence.word[counter].POStagword.equals("IN")
                         ||Sentence.word[counter].POStagword.equals("NNP")||
                        Sentence.word[counter].POStagword.equals("JJ")||
                        Sentence.word[counter].POStagword.equals("JJS")||
                        Sentence.word[counter].POStagword.equals("PRP$")
                        // ||Sentence.word[counter].POStagword.equals("POS")
                         //||Sentence.word[counter].POStagword.equals('"')
                         //||Sentence.word[counter].POStagword.equals("CC")
                         ||Sentence.word[counter].POStagword.equals("NNPS")
                         ||Sentence.word[counter].POStagword.equals("PRP")
                          ||Sentence.word[counter].POStagword.equals("DT")
                         //||Sentence.word[counter].POStagword.equals("RB")
                        )&(counter!=wordsNumber)){
                
                      sw1=1;
                    if( AreRelatedTogether(current,counter)==1){
                         if(i==0)
                        minimalTriple[minimalEndNum].subject= minimalTriple[minimalEndNum].subject+" "+Sentence.word[counter].POSword;
                        else
                        minimalTriple[minimalEndNum].object= minimalTriple[minimalEndNum].object+" "+Sentence.word[counter].POSword;

                         if(i==0)
                            minimalTriple[minimalEndNum].subjectEnd=counter;
                        if(i==1)
                            minimalTriple[minimalEndNum].predicateStart=counter;
                        if(i==2)
                            minimalTriple[minimalEndNum].objectEnd=counter;
                        if(counter!=wordsNumber-1)counter++;else break;
                    }
                    else
                        break;
                    }
              
              
              //----------------------------------------------------------
              //we must extend the object that is one Word to noun group 
            //  if (sw1==0){    
             if(i==0)
                        counter= minimalTriple[minimalEndNum].subjectStart-1;
             if(i==1)
                         counter=minimalTriple[minimalEndNum].predicateStart-1;      
             if(i==2)
                        counter= minimalTriple[minimalEndNum].objectStart-1;  
            
            current=counter+1;
             if(counter!=0){
               while((Sentence.word[counter].POStagword.equals("NN")
                       //||!Sentence.word[counter].POStagword.equals(",")
                       ||Sentence.word[counter].POStagword.equals("NNS")
                       //||Sentence.word[counter].POStagword.equals("IN")
                       ||Sentence.word[counter].POStagword.equals("CD")
                       ||Sentence.word[counter].POStagword.equals("NNP")||Sentence.word[counter].POStagword.equals("JJ")
                       ||Sentence.word[counter].POStagword.equals("JJS")||Sentence.word[counter].POStagword.equals("PRP$")
                       //||Sentence.word[counter].POStagword.equals("POS")
                       //||Sentence.word[counter].POStagword.equals('"')
                       //||Sentence.word[counter].POStagword.equals("CC")
                       ||Sentence.word[counter].POStagword.equals("NNPS")
                       ||Sentence.word[counter].POStagword.equals("PRP")
                       ||Sentence.word[counter].POStagword.equals("DT")
                       //||Sentence.word[counter].POStagword.equals("RB")
                       )&(counter!=wordsNumber)){
                
                    if( AreRelatedTogether(current,counter)==1){
                        if(i==0)  
                        minimalTriple[minimalEndNum].subject=Sentence.word[counter].POSword+" "+ minimalTriple[minimalEndNum].subject;
                        else
                        minimalTriple[minimalEndNum].object=Sentence.word[counter].POSword+" "+ minimalTriple[minimalEndNum].object;    
                          if(i==0)
                                    minimalTriple[minimalEndNum].subjectStart=counter;
                          if(i==2)
                                    minimalTriple[minimalEndNum].objectStart=counter;   
                          if(counter!=1)counter--;else break;
                    }
                    else
                        break;
                     
                   
                 }
                }
             // }
              
              //----------------------------------------------------------
              
   
   }
   public static String extendToNounGroupForAWordDirection(int phrasePosition,String Word,int direction){
   //direction   left=1   right=2   left and right=3
   //we must extend the object that is only one Word to a noun group 
               Word="";        
              if(direction==2 || direction==3) { 
                  int counter=phrasePosition;
                  RightLimitOfWord=phrasePosition;
                  int sw1=0;
                  if(counter<wordsNumber)
                  while((Sentence.word[counter].POStagword.equals("NN")||
                         IsNoun(counter)==1||
                          Sentence.word[counter].POStagword.equals("NNS")||Sentence.word[counter].POStagword.equals("CD")||Sentence.word[counter].POStagword.equals("IN")||
                          Sentence.word[counter].POStagword.equals("NNP")||Sentence.word[counter].POStagword.equals("JJ")||Sentence.word[counter].POStagword.equals("JJS")||Sentence.word[counter].POStagword.equals("PRP$")
                        ||Sentence.word[counter].POStagword.equals("POS")
                          //||Sentence.word[counter].POStagword.equals('"')
                        ||Sentence.word[counter].POStagword.equals("CC")
                        ||Sentence.word[counter].POStagword.equals("NNPS")
                            ||Sentence.word[counter].POStagword.equals("PRP")
                        ||Sentence.word[counter].POStagword.equals("DT")||Sentence.word[counter].POStagword.equals("RB"))&(counter!=wordsNumber)){
                         sw1=1;
                         Word=Word+" "+Sentence.word[counter].POSword;
                         RightLimitOfWord=counter;
                         if(counter!=wordsNumber-1)counter++;else break;
                    }
              
                 }
              //----------------------------------------------------------
              //we must extend the object that is one Word to noun group 
              if(direction==1 || direction==3) {
                                      
                int counter=phrasePosition;
                if(direction==3)
                   counter--; 
                  LeftLimitOfWord=phrasePosition;
                 // counter= counter-1;
                  if(counter!=0){
                  while((Sentence.word[counter].POStagword.equals("NN")||
                         IsNoun(counter)==1||
                          Sentence.word[counter].POStagword.equals("NNS")||Sentence.word[counter].POStagword.equals("IN")||Sentence.word[counter].POStagword.equals("CD")||
                       Sentence.word[counter].POStagword.equals("NNP")||Sentence.word[counter].POStagword.equals("JJ")||Sentence.word[counter].POStagword.equals("JJS")||Sentence.word[counter].POStagword.equals("PRP$")
                       ||Sentence.word[counter].POStagword.equals("POS")
                       //||Sentence.word[counter].POStagword.equals('"')
                       ||Sentence.word[counter].POStagword.equals("CC")
                            ||Sentence.word[counter].POStagword.equals("PRP")
                       ||Sentence.word[counter].POStagword.equals("DT")||Sentence.word[counter].POStagword.equals("RB"))&(counter!=wordsNumber))
                
                  {
                     Word=Sentence.word[counter].POSword+" "+Word;
                     LeftLimitOfWord=counter;
                     if(counter!=1)counter--;else break;
                  }
                }
              }
              
              //----------------------------------------------------------
              
   
   return Word;
   }
   public static String extendToNounGroupForAWordDirectionMinimal(int phrasePosition,String Word,int direction){
   //direction   left=0   right=2   left and right=3
   //we must extend the object that is only one Word to a noun group 
               Word="";        
              if(direction==2 || direction==3) { 
                  int counter=phrasePosition;
                  RightLimitOfWord=phrasePosition;
                  int sw1=0;
                  if(counter<wordsNumber)
                  while((Sentence.word[counter].POStagword.equals("NN")||
                         IsNoun(counter)==1
                          ||Sentence.word[counter].POStagword.equals("NNS")
                         ||Sentence.word[counter].POStagword.equals("CD")
                          //||Sentence.word[counter].POStagword.equals("IN")
                          ||Sentence.word[counter].POStagword.equals("NNP")||Sentence.word[counter].POStagword.equals("JJ")
                          ||Sentence.word[counter].POStagword.equals("JJS")||Sentence.word[counter].POStagword.equals("PRP$")
                          ||Sentence.word[counter].POStagword.equals("POS")
                          //||Sentence.word[counter].POStagword.equals('"')
                          //||Sentence.word[counter].POStagword.equals("CC")
                          ||Sentence.word[counter].POStagword.equals("NNPS")
                          ||Sentence.word[counter].POStagword.equals("PRP")
                          //||Sentence.word[counter].POStagword.equals("DT")
                          ||Sentence.word[counter].POStagword.equals("RB")
                          )
                          &
                          (counter!=wordsNumber)
                          ){
                         sw1=1;
                         Word=Word+" "+Sentence.word[counter].POSword;
                         RightLimitOfWord=counter;
                         if(counter!=wordsNumber-1)counter++;else break;
                    }
              
                 }
              //----------------------------------------------------------
              //we must extend the object that is one Word to noun group 
              if(direction==1 || direction==3) {
                                      
                int counter=phrasePosition;
                if(direction==3)
                   counter--; 
                  LeftLimitOfWord=phrasePosition;
                 // counter= counter-1;
                  if(counter!=0){
                  while((Sentence.word[counter].POStagword.equals("NN")||
                         IsNoun(counter)==1
                          ||Sentence.word[counter].POStagword.equals("NNS")
                         // ||Sentence.word[counter].POStagword.equals("IN")
                          ||Sentence.word[counter].POStagword.equals("CD")
                          ||Sentence.word[counter].POStagword.equals("NNP")||Sentence.word[counter].POStagword.equals("JJ")
                          ||Sentence.word[counter].POStagword.equals("JJS")||Sentence.word[counter].POStagword.equals("PRP$")
                          ||Sentence.word[counter].POStagword.equals("POS")
                          //||Sentence.word[counter].POStagword.equals('"')
                          //||Sentence.word[counter].POStagword.equals("CC")
                            ||Sentence.word[counter].POStagword.equals("PRP")
                          //||Sentence.word[counter].POStagword.equals("DT")
                          ||Sentence.word[counter].POStagword.equals("RB"))&(counter!=wordsNumber))
                
                  {
                     Word=Sentence.word[counter].POSword+" "+Word;
                     LeftLimitOfWord=counter;
                     if(counter!=1)counter--;else break;
                  }
                }
              }
              
              //----------------------------------------------------------
              
   
   return Word;
   }
    
  
   
   
   
   
   public static String extendToNounGroupForAWordDirectionNounPhrase(int phrasePosition,String Word,String str,int direction){
   //direction   left=0   right=2   left and right=3
   //we must extend the object that is only one Word to a noun group 
               Word="";        
              if(direction==2 || direction==3) { 
                  int counter=phrasePosition;
                  RightLimitOfWord=phrasePosition;
                  int sw1=0;
                  if(counter<wordsNumber)
                  while((Sentence.word[counter].POStagword.equals("NN")
                          //||IsNoun(counter)==1
                          ||Sentence.word[counter].POStagword.equals("NNS")
                         ||Sentence.word[counter].POStagword.equals("CD")
                          //||Sentence.word[counter].POStagword.equals("IN")
                          ||Sentence.word[counter].POStagword.equals("NNP")
                          ||Sentence.word[counter].POStagword.equals("JJ")
                          ||Sentence.word[counter].POStagword.equals("JJS")
                          ||Sentence.word[counter].POStagword.equals("PRP$")
                          ||Sentence.word[counter].POStagword.equals("POS")
                          //||Sentence.word[counter].POStagword.equals('"')
                          //||Sentence.word[counter].POStagword.equals("CC")
                          ||Sentence.word[counter].POStagword.equals("NNPS")
                          ||Sentence.word[counter].POStagword.equals("PRP")
                          //||Sentence.word[counter].POStagword.equals("DT")
                         // ||Sentence.word[counter].POStagword.equals("RB")
                          )
                          &
                          (counter!=wordsNumber)
                          ){
                         sw1=1;
                         Word=Word+" "+Sentence.word[counter].POSword;
                         RightLimitOfWord=counter;
                         if(counter!=wordsNumber-1)counter++;else break;
                    }
              
                 }
              //----------------------------------------------------------
              //we must extend the object that is one Word to noun group 
              if(direction==1 || direction==3) {
                                      
                int counter=phrasePosition;
                if(direction==3)
                   counter--; 
                  LeftLimitOfWord=phrasePosition;
                 // counter= counter-1;
                  if(counter!=0){
                  while((Sentence.word[counter].POStagword.equals("NN")
                        // IsNoun(counter)==1
                          ||Sentence.word[counter].POStagword.equals("NNS")
                         // ||Sentence.word[counter].POStagword.equals("IN")
                          ||Sentence.word[counter].POStagword.equals("CD")
                          ||Sentence.word[counter].POStagword.equals("NNP")||Sentence.word[counter].POStagword.equals("JJ")
                          ||Sentence.word[counter].POStagword.equals("JJS")||Sentence.word[counter].POStagword.equals("PRP$")
                          ||Sentence.word[counter].POStagword.equals("POS")
                          //||Sentence.word[counter].POStagword.equals('"')
                          //||Sentence.word[counter].POStagword.equals("CC")
                            ||Sentence.word[counter].POStagword.equals("PRP")
                          //||Sentence.word[counter].POStagword.equals("DT")
                          //||Sentence.word[counter].POStagword.equals("RB")
                          )&(counter!=wordsNumber))
                
                  {
                     Word=Sentence.word[counter].POSword+" "+Word;
                     LeftLimitOfWord=counter;
                     if(counter!=1)counter--;else break;
                  }
                }
              }
              
              //----------------------------------------------------------
              
   
   return Word;
   }
}
