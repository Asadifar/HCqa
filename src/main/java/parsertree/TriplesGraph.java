/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsertree;
import edu.stanford.nlp.trees.TypedDependency;
import java.io.IOException;
//import Jwi.Main;
//import static com.aliasi.util.Iterators.list;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import static java.util.Collections.list;
import java.util.Iterator;
import java.util.List;
import static parsertree.Sentence.AreRelatedTogether;
import static parsertree.Sentence.NotDependencyBetwweenToVerbs;
import static parsertree.Sentence.VerbToVerb;
import static parsertree.Sentence.govIsMatchedWhithVerbs;
import static parsertree.Sentence.tdl;
import static parsertree.Sentence.verbs;
import static parsertree.Sentence.wordsNumber;
import static parsertree.Sentence.BeCopularMainVerb;
import static parsertree.Sentence.NotCopularVerb;
import static parsertree.Word.BeCapableOfMemberOfNounRelation;
import static parsertree.Word.BeCapableOfMemberOfNounRelationSecondArguman;
import static parsertree.Word.ComparativeAdjectiveIsQuantityAdjective;
import static parsertree.Sentence.FindRerenceOfThat;
import static parsertree.Sentence.IsAQuotationPhrase;
import static parsertree.Word.IsCapital;
import static parsertree.Word.IsGenitivePossSubject;
import static parsertree.Word.IsNoun;
import static parsertree.Word.IsPossessiveAdjective;
import static parsertree.Word.SuperlativeAdjectiveIsQuantityAdjective;
import static parsertree.Word.govIsSomeSpecialWords;
import static parsertree.Word.relIsIndirectObject;

public class TriplesGraph {
public static ArrayList<Triple> tripleList = new ArrayList<Triple>();
public static int EndNum;
public static int minimalEndNum;
static public Triple[] triple=new Triple[60];
public static int  RightLimitOfWord, LeftLimitOfWord;
public static String  IndirectObject;
public static int  haveIndirectObject;
public static String  DirectObject;
public static int  haveDirectObject;
public static int [] prepSequence=new int[15];
public static String [] subjectFillers=new String [15];
public static int [] subjectFillersPositions=new int [15];
public static int subjectFillersNumber;
public static String [] objectFillers=new String [15];
public static String [] objectFillersPrep=new String [15];
public static int [] objectFillersPositions=new int [15];
public static int [] directObjectFillersPositions=new int [15];
public static int objectFillersNumber;

public static void initialization(String line){
                   minimalEndNum=0;
                   //List<Triple> tripleList = new ArrayList<Object>();
            tripleList.clear();

                   for(int i=0;i<60;i++){
                     triple[i]=new Triple("","","");
                    // triplei=new Triple("","","");
                    }
                    for(int i=0;i<15;i++)
                        objectFillersPrep[i]=""; 
}
public static void triplesGeneration(String line) throws Exception {
       initialization(line);
       VerbBasedRelations();
       CommaRelation();
       NounPhraseRelations();
       GenitiveRelations();
       IndirectVerbal();
       ComparativeRelations();
       SuperlativeRelations();
       processTriples();
   }

public static void convertConnectedWords(int i,int SubjObj,int MinMax, String conectedWord,int ConnectedWordPosition){
   String subj="";
   Triple triplei=tripleList.get(i);
   if (conectedWord.matches("that")||conectedWord.matches("who")
           ||conectedWord.matches("which")||conectedWord.matches("whom")
           ||conectedWord.matches("where")){
            int RefrenceIndex=FindRerenceOfThat(ConnectedWordPosition-1);
                           if(RefrenceIndex!=0){
                              if (MinMax==1){
                                   if(SubjObj==0){
                                       //triple[i]=maximalTripleList.get(i);
                                       triple[i].subject=subj;
                                       triple[i].subjectStart=LeftLimitOfWord;
                                       triple[i].subjectEnd=RightLimitOfWord;
                                     //  maximalTripleList.add(i,triple[i]); 
                                    }else{
                                     //    triple[i]=maximalTripleList.get(i);
                                         triple[i].objectStart=LeftLimitOfWord;
                                         triple[i].objectEnd=RightLimitOfWord;
                                         triple[i].object=subj;
                                     //    maximalTripleList.add(i,triple[i]); 

                                           }
                                       
                                }
                              else{
                                  
                                   if(SubjObj==0){
                                       
                                       triplei=tripleList.get(i);
                                       triplei.subject=subj;
                                       triplei.subjectStart=LeftLimitOfWord;
                                       triplei.subjectEnd=RightLimitOfWord;
                                       tripleList.add(i,triplei);
                                    }else{
                                         triplei=tripleList.get(i);
                                         triplei.object=subj;
                                         triplei.objectStart=LeftLimitOfWord;
                                         triplei.objectEnd=RightLimitOfWord;
                                         tripleList.add(i,triplei);

                                          }
                                   
                                  
                                }
                        
                        }
                          else{
                         
                               if (MinMax==0){
                                            triplei=tripleList.get(i);
                                      
                                            if(SubjObj==0) 
                                                triplei.subject=Sentence.word[ConnectedWordPosition-1].POSword;
                                            else
                                                triplei.object=Sentence.word[ConnectedWordPosition-1].POSword;
                                             if(SubjObj==0){
                                                 triplei.subjectStart=ConnectedWordPosition-1;
                                                 triplei.subjectEnd=ConnectedWordPosition-1;
                                             }
                                              else {
                                                triplei.objectStart=ConnectedWordPosition-1;
                                                triplei.objectEnd=ConnectedWordPosition-1;
                                             }
                               tripleList.add(i,triplei);
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
public static void FullNullObjectMinimal(){
       String[] str;int counter;
       Triple triplej=new Triple("","","");
       for(int j=0;j<minimalEndNum-1;j++){
            triplej=tripleList.get(j); 
           if(triplej.subject!="" & triplej.object==""){
            triplej.object="";
               counter= triplej.predicateStart+1; 
               if(!Sentence.word[counter].POStagword.equals(","))
             while(counter!=wordsNumber & !Sentence.word[counter].POStagword.equals("''")&!Sentence.word[counter].POStagword.equals(",")){                
                       triplej.object=triplej.object+" "+Sentence.word[counter].POSword;
                       if(counter!=wordsNumber-1)counter++;else break;
                 }
            }
           tripleList.set(j,triplej);
        }
   }

public static void IndirectVerbal( ){
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
       Triple triplej=new Triple("","","");
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
                            IsCapital(Sentence.word[dep1Index].POSwordStart)==1 & IsCapital(Sentence.word[gov1Index].POSwordStart)==1){
                            for(j=0;j<minimalEndNum;j++){
                                triplej=tripleList.get(j);
                                if(
                                        (triplej.subject.contains(gov1)& triplej.object.contains(dep1) )||
                                        (triplej.predicate.contains(gov1)&triplej.object.contains(dep1) )   
                                  ) 
                                  break;
                            }
                            if(j>minimalEndNum)
                                     MakeIndirectVerbalRelation(gov1,gov1Index,dep1,dep1Index,relIsIndirectObject(rel1));
                            else
                                                                   MakeIndirectVerbalRelation(gov1,gov1Index,dep1,dep1Index,relIsIndirectObject(rel1));
  
                     }  
                     index1++;
                    }
    }
public static void MakeIndirectVerbalRelation(String gov1,int gov1Index,String dep1,int dep1Index,String prep){
    int j=0;String subject="";
    int subjectStart=0,subjectEnd=0;
    Triple triplej=new Triple("","","");
    for(j=0;j<minimalEndNum;j++){
        
         triplej=tripleList.get(j);
                  if(triplej.object.contains(gov1))
                    break;
                  
    }
if(j<minimalEndNum+1){
        Triple newtriple=new Triple("","","");
        minimalEndNum++;   
        newtriple.subject=triplej.subject;newtriple.subjectStart=triplej.subjectStart;newtriple.subjectEnd=triplej.subjectEnd;
        newtriple.predicate=triplej.predicate;newtriple.predicateStart=triplej.predicateStart;
        newtriple.object=dep1;newtriple.objectStart=newtriple.objectEnd=dep1Index;
        tripleList.add(minimalEndNum-1,newtriple);
        extendToNounGroupMinimal(2,minimalEndNum-1);
        newtriple=tripleList.get(minimalEndNum-1);
        newtriple.predicatePrep=prep;
        newtriple.tripleType="Indirect Verbal relation: ";
        tripleList.set(minimalEndNum-1,newtriple);
    }
      minimalEndNum++;     
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
                        Triple newTriple=new Triple("","","");
                        int counter=i+1;
                        while(Sentence.word[counter].POStagword.matches("RB"))
                            counter++;
                        newTriple.subject=Sentence.word[counter].POSword;newTriple.subjectStart=counter;newTriple.subjectEnd=counter;
                         newTriple.subject=extendToNounGroupForAWordDirectionMinimal(counter,Sentence.word[counter].POSword,2);
                        newTriple.predicate=predicate;//triple[i].predicate+' '+Sentence.word[[verbs[j].verbsPosition+1]].POSword;
                        newTriple.tripleType="quantity Superlative relation:  ";
                        newTriple.object="n1";
                        newTriple.typeCondition="order by";
                        if(operator.matches("higher"))
                        newTriple.sortTypeCondition="ASC";
                        else
                        newTriple.sortTypeCondition="DESC";    
                        newTriple.elementCondition="n1";
                        tripleList.add(minimalEndNum,newTriple);
                        minimalEndNum++;
                   }
                   else{minimalEndNum++;
                        Triple newTriple=new Triple("","","");
                        int counter=i+1;
                        while(Sentence.word[counter].POStagword.matches("RB"))
                            counter++;
                        newTriple.subject=Sentence.word[counter].POSword;newTriple.subjectStart=counter;newTriple.subjectEnd=counter;
                        newTriple.subject=extendToNounGroupForAWordDirectionMinimal(counter,Sentence.word[counter].POSword,2);
                        newTriple.predicate="is";//triple[i].predicate+' '+Sentence.word[[verbs[j].verbsPosition+1]].POSword;
                        newTriple.tripleType="qualify Superlative relation:  ";
                        //CompSuper=1;
                        newTriple.object=Sentence.word[i].POSword;
                        tripleList.add(minimalEndNum,newTriple);
                        minimalEndNum++;
                        
                   }//is not quantity adjective
          }
       }
  }
public static void ComparativeRelations (){
    int j;
    String predicate="";
    String operator="";
    Triple newTriple= new Triple("","","");
    for(j=0;j<Sentence.CounterVerbs;j++){
       if(verbs[Sentence.CounterVerbs].verbsPosition+1<wordsNumber)
        if(Sentence.wordPre[Sentence.word[verbs[j].verbsPosition+1].POSwordStart].POStagword.matches("RBR")||Sentence.wordPre[Sentence.word[verbs[j].verbsPosition+1].POSwordStart].POStagword.matches("JJR")){
        String Adjective=ComparativeAdjectiveIsQuantityAdjective(Sentence.word[verbs[j].verbsPosition+1].POSword);
        if(!Adjective.matches("")){//is quantity adjective
           String str[]=Adjective.split(" ");
           predicate=str[3];
           operator=str[4];
            for(int i=0;i<=minimalEndNum;i++){
             Triple triplei= new Triple("","","");
             if(triplei.predicate.matches(verbs[j].verbs)){
                minimalEndNum++;
                newTriple.subject=triplei.subject;newTriple.subjectStart=triplei.subjectStart;newTriple.subjectEnd=triplei.subjectEnd;
                newTriple.predicate=predicate;//triple[i].predicate+' '+Sentence.word[[verbs[j].verbsPosition+1]].POSword;
                newTriple.tripleType="quantity Comparative relation:  ";
                triplei.tripleState=0;
                if(verbs[j].verbsPosition+3<wordsNumber){
                if(Sentence.word[verbs[j].verbsPosition+3].POStagword.matches("CD")){
                    newTriple.object="n1";//POSword[verbs[j].verbsPosition+3];
                    newTriple.typeCondition="n1";
                    newTriple.sortTypeCondition=operator;
                    newTriple.elementCondition=Sentence.word[verbs[j].verbsPosition+3].POSword;
                     tripleList.add(minimalEndNum,newTriple);
                }else{//is not a number
                     newTriple.object="n1";
                     minimalEndNum++;
                     newTriple.subject=Sentence.word[verbs[j].verbsPosition+3].POSword;newTriple.subjectStart=verbs[j].verbsPosition+3;newTriple.subjectEnd=verbs[j].verbsPosition+3; extendToNounGroupMinimal(0,minimalEndNum);
                     newTriple.predicate=predicate;//triple[i].predicate+' '+Sentence.word[[verbs[j].verbsPosition+1]].POSword;
                     newTriple.object="n2";
                     newTriple.tripleType="quantity Comparative relation:  ";
                     newTriple.typeCondition="n1";
                     newTriple.sortTypeCondition=operator;
                     newTriple.elementCondition="n2";
                     tripleList.add(minimalEndNum,newTriple);
                }
              }
                tripleList.add(minimalEndNum,newTriple);
                
            }
        }
            
        }
        else{//is quality Adjective
           for(int i=0;i<=minimalEndNum;i++){
             Triple triplei=new Triple("","",""); 
             triplei=tripleList.get(i);
           if(triplei.predicate.matches(verbs[j].verbs)){
             newTriple.subject=triplei.subject;newTriple.subjectStart=triplei.subjectStart;newTriple.subjectEnd=triplei.subjectEnd;
             int sw=0;
             newTriple.tripleType="quality Comparative relation:  ";
             if(verbs[j].verbsPosition+2<wordsNumber){
              if(Sentence.word[verbs[j].verbsPosition+1].POSword.matches("than")){
                newTriple.predicate=triplei.predicate+' '+Sentence.word[verbs[j].verbsPosition+1].POSword;   
                 newTriple.object=Sentence.word[verbs[j].verbsPosition+2].POSword;
                 newTriple.objectStart=verbs[j].verbsPosition+2;
                 newTriple.objectEnd=verbs[j].verbsPosition+2;
                 extendToNounGroupMinimal(2,minimalEndNum);
                 tripleList.add(minimalEndNum,newTriple);
                 minimalEndNum++;
                sw=1;
              }
             }
              if(sw==0){
                  
              newTriple.predicate=triplei.predicate;   
               newTriple.object=Sentence.word[verbs[j].verbsPosition+1].POSword;
               newTriple.objectStart=verbs[j].verbsPosition+1;
               newTriple.objectEnd=verbs[j].verbsPosition+1;
               extendToNounGroupMinimal(2,minimalEndNum);
                tripleList.set(minimalEndNum,newTriple);
               minimalEndNum++;
              }
              break;
           }
                  
           }
             
        }
        }
     }//ens else is quantity Adjective
    }//counter verbs
public static void CommaRelation() throws Exception{
       String[] str1;int dep1Index,gov1Index,j,i,k=0;
       String rel1,dep1,gov1;   
       Triple newTriple=new Triple("","","");
       Triple triplej=new Triple("","","");
              
       int index1=0;
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
                     /*-------------second approach to increase the accuracy of the Comma relations-- */                   
                         for( i=0; i<EndNum;i++)
                           if(triple[i].subject.contains(dep1)) break;
                         for( j=0; j<minimalEndNum;j++)
                         { triplej=tripleList.get(j);
                           if(triplej.object.contains(dep1)) break;
                         }  
                         if(i==EndNum & j==minimalEndNum){
                     /*-------------- end second approach to increase the accuracy of the Comma relations----------*/ 
                            newTriple.subject=gov1; newTriple.subjectStart=gov1Index;newTriple.subjectEnd=gov1Index;extendToNounGroupMinimal(0,minimalEndNum);
                            newTriple.predicate="Is";newTriple.predicateStart=0;
                           newTriple.object=dep1;newTriple.objectStart=dep1Index;newTriple.objectEnd=dep1Index;extendToNounGroupMinimal(2,minimalEndNum);
                            newTriple.tripleType="Comma relation:   ";
                            tripleList.add(minimalEndNum,newTriple);
                            minimalEndNum++;  
                         
                         } 
                         }
                     
                     index1++;
                    }
}
public static void GenitiveRelations(){
        
       GenetiveFunction2();
 }
public static void VerbBasedRelations( ){
 int i,Minimali,j,counter;
      i=0;Minimali=0;
      for(j=0;j<Sentence.CounterVerbs;j++){
         int index=0;
         int k=BeCopularMainVerb(verbs[j].verbs,j);
         if(BeCopularMainVerb(verbs[j].verbs,j)==1 ){
                   // i=AddCopularRelationTriple(verbs[j].verbs,j,i);i++;
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
                    Minimali=AddNotCopularVerbRelationTripleMinimal(verbs[j].verbs,j,Minimali);//Minimali++;     
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
         int sw=1;
         if(i<wordsNumber-1)
             if(BeCapableOfMemberOfNounRelation(i)==1 & BeCapableOfMemberOfNounRelationSecondArguman(i+1)==1  ){
                        if(i>1) 
                         if(Sentence.word[i-1].POStagword.matches("TO")) 
                             sw=0;
                        if((IsCapital(Sentence.word[i].POSwordStart)==1 & IsCapital(Sentence.word[i+1].POSwordStart)==0)&
                                !(Sentence.word[i].POStagword.matches("NNP")&(Sentence.word[i+1].POStagword.matches("NNP")))& sw==1){
                          // String Class=Main.main(Sentence.word[Sentence.word[i+1].POSwordStart].POSword);
                          // if(!Class.matches("Null"))
                          //    AddNounPhraseTriplePatternP1(i,1,i+1);
                          // else 
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
                                 }
                                i=counter-1; 
                             }  
                             else{
                                  ntNum++;
                                  AddNounPhraseTriplePatternP1(i,1,i+1);
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
             }
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
       Triple temp=new Triple("","","");
       Triple newTriple=new Triple("","","");
       Triple triplej=new Triple("","","");
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
                     if(
                             (!relIsIndirectObject(rel1).matches("")&
                             govIsMatchedWhithVerbs(gov1)==0) &
                             (IsCapital(Sentence.word[dep1Index].POSwordStart)==0 || 
                             IsCapital(Sentence.word[gov1Index].POSwordStart)==0)
                             
                             ){
 
                      for(j=0;j<minimalEndNum;j++)
                      {
                          triplej=tripleList.get(j);
                      if((triplej.subject.matches(gov1)& triplej.object.matches(dep1) )||
                           (triplej.predicate.matches(gov1)& triplej.object.matches(dep1) )   
                              ) 
                          break;
                      }
                             if(j>=minimalEndNum){
                      
                      int first=minimalEndNum;
                      MakeGenitiveRelation(gov1Index,dep1Index,rel1);
                      if(minimalEndNum>first){
                                      sequenceNum=-1;
                                      sequenceNum=sequenceNum+2; 
                                      if(rel1.matches("nmod:to"))
                                          rel1="nmod:"+gov1+" to";
                                      temp=tripleList.get(minimalEndNum-1);
                                      temp.tripleType=rel1;
                                      tripleList.set(minimalEndNum-1,temp);
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
                             { triplej=tripleList.get(j);
                              if(triplej.subject.contains(dep1)& triplej.object.contains(gov1) )
                               break;
                             }
                             if(j==minimalEndNum){                                
                                      
                                      first=minimalEndNum;
                                      MakeGenitiveRelation(gov2Index,dep2Index,rel2);
                                      if(minimalEndNum>first)
                                      {
                                      sequenceNum=sequenceNum+2; 
                                      temp=tripleList.get(minimalEndNum-1);
                                      temp.tripleType=rel2;
                                      tripleList.set(minimalEndNum-1,temp);
                                      prepSequence[sequenceNum]=gov2Index;
                                      prepSequence[sequenceNum+1]=dep2Index;
                                      }
                                      break;
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
                             for(int counter=Startnum+1;counter<minimalEndNum;counter++){
                                 temp=tripleList.get(counter);
                                 temp.tripleType=temp.tripleType;
                                 tripleList.set(counter,temp);
                           }
                         }  
                     else 
                         Startnum=minimalEndNum;
                     sequenceNum=-1;
                     index1++;
                   
                }
}

public static int AddNounPhraseTriplePatternP1(int firstNoun,int LengthOfFirstNoun,int secondNoun){
    minimalEndNum++;
     
       Triple triplei=new Triple("","","");
    int i=minimalEndNum-1;
    
        triplei.subject=Sentence.word[secondNoun].POSword; triplei.subjectStart=Sentence.word[secondNoun].POSwordStart;triplei.subjectEnd=Sentence.word[secondNoun].POSwordStart;//extendToNounGroup(0,i,sentence);
    triplei.predicate="has a relation with"; triplei.predicateStart=0;
    triplei.tripleType="Noun phrase relation(p1): ";
    for(int k=1;k<=LengthOfFirstNoun;k++){
    triplei.object=triplei.object+Sentence.word[firstNoun+k-1].POSword;}
    
    triplei.objectStart=firstNoun;triplei.objectEnd=firstNoun+LengthOfFirstNoun-1;
    tripleList.add(i,triplei);
    return 0;}

public static int AddNotCopularVerbRelationTriple(String str,int j,int i){
    
    for(int f=0;f<=14;f++){subjectFillers[f]="";subjectFillersPositions[f]=0;objectFillers[f]="";objectFillersPositions[f]=0;directObjectFillersPositions[f]=0;}
     subjectFillersNumber=0;objectFillersNumber=0;
     IndirectObject="";
     EndNum=i;
     String[] str1;
     int index=0,depIndex,govIndex;String gov,dep,rel,govTag;String[] strTemp; 
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
      // i=makeTriplesFromFillers1(i,str,j);
   return i;
   }  
public static int AddNotCopularVerbRelationTripleMinimal(String str,int j,int i){
     minimalEndNum=i;
     i=makeTriplesFromFillersMinimal(i,str,j);
   return i;
}  

public static void FullUpSubjectFillers(String gov,String dep,String rel,int i,int depIndex,String govTag,int govIndex){
    
    if (rel.equals("nsubj")||rel.equals("nsubjpass")||rel.equals("nsubj:xsubj")){
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
         if( rel.equals("dobj")||//How many Golden Globe awards did the daughter of Henry Fonda win? (nsubj*VB*dobj)
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
            ||rel.equals("nmod:agent")
             ||rel.equals("advmod") ) { //the man has been killed by the police.(man killed by police)        
           objectFillers[objectFillersNumber]=dep;
           objectFillersPositions[objectFillersNumber]=depIndex;
           if(rel.contains("nmod:"))
           objectFillersPrep[objectFillersNumber]=rel.split("nmod:")[1];
           if(rel.equals("dobj"))directObjectFillersPositions[objectFillersNumber]=1;
            objectFillersNumber++;
         }
          return 0;
  }

public static int makeTriplesFromFillersMinimal(int i,String str,int verbNum){
    Triple triplei=new Triple("","",""); 
    //if(i==0) i=1;
    int numberOfDirectObjects=0;
      for(int sf=0;sf<subjectFillersNumber;sf++)
         for(int of=0;of<objectFillersNumber;of++){
             if(objectFillersNumber>1){
                         triplei=new Triple("","","");
                         triplei.subject=subjectFillers[sf];
                         triplei.subjectStart=subjectFillersPositions[sf];
                         triplei.subjectEnd=subjectFillersPositions[sf];
                         tripleList.add(i,triplei);
                         convertConnectedWords(i,0,0,triplei.subject,triplei.subjectStart);
                         extendToNounGroupMinimal(0,i);
                         
                         triplei=tripleList.get(i);
                         triplei.predicate=str;
                         triplei.predicateStart=verbs[verbNum].verbsPosition;
                         
                         triplei.object=objectFillers[of];
                         triplei.objectStart=objectFillersPositions[of];
                         triplei.objectEnd=objectFillersPositions[of];
                          tripleList.set(i,triplei);
                         convertConnectedWords(i,2,0,triplei.object,triplei.predicateStart);
                         triplei.predicatePrep=objectFillersPrep[of];
                         tripleList.set(i,triplei);
                         if(!Sentence.word[objectFillersPositions[of]].POStagword.matches("WRB"))
                           extendToNounGroupMinimal(2,i);
                         
                         triplei.tripleType="Verbal relation: "; 
                         
                        if(Sentence.word[1].POSword.equals("How") & Sentence.word[2].POSword.equals("many") & triplei.object.contains(Sentence.word[3].POSword)){
                           
                            triplei.predicate=triplei.object.split("many ")[1];
                           triplei.object="N";
                        }
                        tripleList.set(i,triplei);
                        i=CommaBetweenTwoNounMinimal(i,triplei.objectStart,triplei.object);
                         minimalEndNum++;
                         i=minimalEndNum;
                         
                        
            }else{     
                        
                        triplei.subject=subjectFillers[sf];
                        triplei.subjectStart=subjectFillersPositions[sf];
                        triplei.subjectEnd=subjectFillersPositions[sf];
                        tripleList.add(i,triplei);
                        convertConnectedWords(i,0,0,triplei.subject,triplei.subjectStart);
                        
                        extendToNounGroupMinimal(0,i);
                        triplei=tripleList.get(i);
                        triplei.predicate=str; triplei.predicateStart=verbs[verbNum].verbsPosition;
                        triplei.object=objectFillers[of];triplei.objectStart=objectFillersPositions[of];triplei.objectEnd=objectFillersPositions[of];if(!Sentence.word[objectFillersPositions[of]].POStagword.matches("WRB"))convertConnectedWords(i,2,0,triplei.object,triplei.predicateStart);
                        tripleList.set(i,triplei);
                        extendToNounGroupMinimal(2,i);
                        triplei=tripleList.get(i);
                        triplei.predicatePrep=objectFillersPrep[of];
                        triplei.tripleType="Verbal relation: ";
                        if(Sentence.word[1].POSword.equals("How") & Sentence.word[2].POSword.equals("many") & triplei.object.contains(Sentence.word[3].POSword)&triplei.object.contains("many")){
                           triplei.predicate=triplei.object.split("many ")[1];
                           triplei.object="N";
                           triplei.tripleType="How many relation: ";
                        }
                         tripleList.set(i,triplei);
                        i=CommaBetweenTwoNounMinimal(i,triplei.objectStart,triplei.object);
                         // i++; triple[i].predicate=str;triple[i].subjectEnd=triplesPosition[i-1][1];sw=1;
                        minimalEndNum++;
                        i=minimalEndNum;
                        //triplei=tripleList.get(i);
                       // if(!triplei.subject.matches(triplei.object)) 
                       // i++;
             }
            }   
    if(subjectFillersNumber==0 & objectFillersNumber>0){
         for(int of=0;of<objectFillersNumber;of++){
         //  IndirectObject=IndirectObject+" "+objectFillers[of];
         // }
      triplei= tripleList.get(i);
       triplei.subject="";  
       triplei.predicate=str; triplei.predicateStart=verbs[verbNum].verbsPosition;
       triplei.object=objectFillers[of];
        triplei.predicatePrep=objectFillersPrep[of];
       triplei.tripleType="verbal relation: ";
         tripleList.set(i,triplei);
        i++;
    }  
    }     
    if(objectFillersNumber==0)
        for(int sf=0;sf<subjectFillersNumber;sf++){
        // triplei= tripleList.get(i);
    
        triplei.subject=subjectFillers[sf]; 
        triplei.subjectStart=subjectFillersPositions[sf];
        triplei.subjectEnd=subjectFillersPositions[sf];
         tripleList.add(i,triplei);
        convertConnectedWords(i,0,0,triplei.subject,triplei.subjectStart);extendToNounGroupMinimal(0,i);
        triplei= tripleList.get(i);
        triplei.predicate=str; triplei.predicateStart=verbs[verbNum].verbsPosition;
         triplei.tripleType="verbal relation: ";
         tripleList.set(i,triplei);
        i++;
        }
  return i;
    }

public static int AddCopularRelationTripleMinimal(String str,int j,int i ){
       int counter;String subj;int sw1=0;int sw=0;
       Triple triplei=new Triple("","","");
        Triple tripleBeforeI=new Triple("","","");
       int index1=0,k,dep1Index=0,gov1Index;
       String[] str1; String dep1="";String rel1,gov1;
       triplei=new Triple("","","");
       
       triplei.predicate=verbs[j].verbs;triple[i].predicateRole=verbs[i].verbsRoles;//triple[i].tripleType=2; 
       triplei.predicateStart=verbs[j].verbsPosition;
       tripleList.add(triplei);
       counter= verbs[j].verbsPosition-1;
       if(Sentence.word[verbs[j].verbsPosition-1].POSword.matches("that")||Sentence.word[verbs[j].verbsPosition-1].POSword.matches("which")||
          Sentence.word[verbs[j].verbsPosition-1].POSword.matches("where")||Sentence.word[verbs[j].verbsPosition-1].POSword.matches("whom")||
          Sentence.word[verbs[j].verbsPosition-1].POSword.matches("who")){
           int RefrenceIndex=FindRerenceOfThat(counter);
           if(RefrenceIndex!=0){
              subj=extendToNounGroupForAWordDirectionMinimal(RefrenceIndex,Sentence.word[RefrenceIndex].POSword,1);
              triplei=tripleList.get(i);
              triplei.subject=subj;
              triplei.subjectStart=LeftLimitOfWord;
              triplei.subjectEnd=RightLimitOfWord;
              tripleList.set(i,triplei);
 
           }
            else
            extendToLeftCopularMinimal(0,i,counter);
        }
        else
        extendToLeftCopularMinimal(0,i,counter);
        triplei=tripleList.get(i);
        if(triplei.subjectStart-1>3){
        if(Sentence.word[triplei.subjectStart-1].POStagword.matches("IN")){
        counter= triplei.predicateStart-2;    
           extendToLeftCopularMinimal(0,i,counter);
        
        }
        else if(Sentence.word[triplei.subjectStart-2].POStagword.matches("IN")){
           counter= triplei.predicateStart-3;    
           extendToLeftCopularMinimal(0,i,counter);
        
        }        
        }  counter= verbs[j].verbsPosition+1;
                        triplei=tripleList.get(i);
                        triplei.objectStart=verbs[j].verbsPosition+1;
                        triplei.objectEnd=verbs[j].verbsPosition+1;
                        triplei.tripleType="Copular verb relation: ";
                        tripleList.set(i,triplei);
                        extendToRightCopularMinimal(2,i,counter);
                        String Mintriple=triplei.object;
                        sw=1;
                        
                         triplei=tripleList.get(i);
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
                            if((gov1.matches(Mintriple) & gov1Index==triplei.objectStart & (!relIsIndirectObject(rel1).matches(""))                   
                                    )){
                            if(sw==0){
                              i++;
                              triplei=tripleList.get(i);
                              tripleBeforeI=tripleList.get(i-1);
                              triplei.subject=tripleBeforeI.subject;triplei.subjectStart=tripleBeforeI.subjectStart; triplei.subjectEnd=tripleBeforeI.subjectEnd;
                              triplei.predicate=tripleBeforeI.predicate;triplei.predicateStart=tripleBeforeI.objectStart;
                              triplei.object=Mintriple;triplei.objectStart=tripleBeforeI.objectStart; triplei.subjectEnd=tripleBeforeI.subjectEnd; // extendToNounGroupMinimal(2,i,str);
                              tripleList.set(i,triplei);
                            }        
                            triplei=tripleList.get(i);
                            triplei.tripleType="verbal relation(NP-VPc-NP-prep-NP): ";
                            str1=rel1.split("nmod:");
                            triplei.object=triplei.object+" "+str1[1]+" "+extendToNounGroupForAWordDirectionMinimal(dep1Index,dep1,1);
                            triplei.objectEnd=dep1Index;
                            sw=0;
                            tripleList.set(i,triplei);
                            }
                               
                            index1++;      
                            }//end for
                         i=minimalEndNum;
        return i;
   }

public static void MakeGenitiveRelation(int gov2Index,int dep2Index,String rel2) {
    Triple triplej=new Triple("","","");
    Triple triplei=new Triple("","","");

    int j=0;
    if(!Sentence.word[gov2Index].POStagword.matches("CD") & IsAQuotationPhrase(rel2,gov2Index,dep2Index)==0 & (relIsNModTo(rel2,gov2Index,dep2Index)==0 ||relIsNModTo(rel2,gov2Index,dep2Index)==2)){
    for(j=0;j<minimalEndNum;j++){
        triplej=tripleList.get(j);
        if((triplej.subjectStart<=gov2Index && triplej.subjectEnd>=gov2Index && triplej.objectStart<=dep2Index && triplej.objectEnd>=dep2Index )||
           (triplej.subjectEnd==gov2Index && triplej.objectStart<=dep2Index && triplej.objectEnd>=dep2Index )
              )
                    break;
    }
    if(j==minimalEndNum){

           int i=minimalEndNum;  
            if(rel2.matches("dep")||rel2.matches("nmod:of")||rel2.matches("nmod:poss")||rel2.matches("nmod:for")||rel2.matches("nmod:in")||rel2.matches("nmod:at")||rel2.matches("nmod:on")||rel2.matches("nmod:by")||rel2.matches("nmod:near")||rel2.matches("nmod:from")
                    ||rel2.matches("nmod:with")||rel2.matches("nmod:under")||rel2.matches("nmod:against")||rel2.matches("nmod:during")
                    ||rel2.matches("nmod:to")||rel2.matches("nmod:agent")||rel2.matches("nmod:like")||rel2.contains("nmod:")){
                    triplei.subject=Sentence.word[gov2Index].POSword; 
                    triplei.subjectStart=gov2Index;
                    triplei.subjectEnd=gov2Index;
                    tripleList.add(i,triplei);
                    extendToNounGroupMinimal(0,i);
                    triplei.predicate="has a relation with"; triplei.predicateStart=0;
                    triplei.object=Sentence.word[dep2Index].POSword; triplei.objectStart=dep2Index;triplei.objectEnd=dep2Index;extendToNounGroupMinimal(2,i);
                    minimalEndNum++;  
                    if(rel2.matches("nmod:to")){
                         String[] temp=triplei.subject.split(" ");
                         triplei.subject="";
                         for(int k=0;k<temp.length-1;k++)
                             triplei.subject=triplei.subject+" "+temp[k];
                    }
                    tripleList.set(i,triplei);
                  
            }
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

public static int CommaBetweenTwoNounMinimal(int i,int objectPosition,String Object){
    int sw=0;
    Triple triplei=tripleList.get(i);
    Triple tripleOneBeforeI=new Triple("","","");
    minimalEndNum=i;
                  if(objectPosition!=1)   
                   if(Sentence.word[objectPosition-1].POSword.matches(",")){
                         i++;
                         minimalEndNum=i;
                         triplei=tripleList.get(i);
                         tripleOneBeforeI=tripleList.get(i-1);
                        // triplei=tripleList.get(i);
                         triplei.subject=tripleOneBeforeI.subject;triplei.subjectStart=tripleOneBeforeI.subjectStart;triplei.subjectEnd=tripleOneBeforeI.subjectStart;
                         triplei.predicate=tripleOneBeforeI.predicate;triplei.subjectEnd=tripleOneBeforeI.subjectEnd;
                        triplei.tripleType=tripleOneBeforeI.tripleType;
                        tripleList.add(i,triplei);
                        String str1=extendToNounGroupForAWordDirectionMinimal(objectPosition-2,Sentence.word[objectPosition-2].POSword,1);
                         triplei=tripleList.get(i);
                         triplei.object=str1;triplei.objectEnd=objectPosition-2;triplei.objectStart=LeftLimitOfWord;
                         tripleList.set(i,triplei);
                  
                   }
                     if(objectPosition!=wordsNumber) 
                     if(Sentence.word[objectPosition+1].POSword.matches(",")){
                         sw=1;       
                         tripleList.set(i,triplei);
                         String str1=extendToNounGroupForAWordDirectionMinimal(objectPosition+2,Sentence.word[objectPosition+2].POSword,2);
                         triplei=tripleList.get(i);
                         triplei.object=triplei.object+" "+str1;triplei.objectStart=objectPosition+2;triplei.objectStart=objectPosition+4;triplei.objectEnd=RightLimitOfWord;
                        return sw;
                      }
                     tripleList.set(i,triplei);
  return sw; 
}

public static void extendToNounGroupMinimal(int i,int minimalEndNum ){
    Triple newTriple=new Triple("","","");
    newTriple=tripleList.get(minimalEndNum);
    int counter=0;          
      if(i==0)
                        counter= newTriple.subjectStart+1;
                if(i==1)
                        counter= newTriple.predicateStart+1;
               
                if(i==2)
                        counter= newTriple.objectEnd+1;
               
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
                        newTriple.subject= newTriple.subject+" "+Sentence.word[counter].POSword;
                        else
                        newTriple.object= newTriple.object+" "+Sentence.word[counter].POSword;

                         if(i==0)
                            newTriple.subjectEnd=counter;
                        if(i==1)
                            newTriple.predicateStart=counter;
                        if(i==2)
                            newTriple.objectEnd=counter;
                        if(counter!=wordsNumber-1)counter++;else break;
                    }
                    else
                        break;
                    }  
             if(i==0)
                        counter= newTriple.subjectStart-1;
             if(i==1)
                         counter=newTriple.predicateStart-1;      
             if(i==2)
                        counter= newTriple.objectStart-1;  
            
            current=counter+1;
             if(counter!=0){
               while((Sentence.word[counter].POStagword.equals("NN")
                       ||Sentence.word[counter].POStagword.equals("NNS")
                       ||Sentence.word[counter].POStagword.equals("CD")
                       ||Sentence.word[counter].POStagword.equals("NNP")||Sentence.word[counter].POStagword.equals("JJ")
                       ||Sentence.word[counter].POStagword.equals("JJS")||Sentence.word[counter].POStagword.equals("PRP$")
                       ||Sentence.word[counter].POStagword.equals("NNPS")
                       ||Sentence.word[counter].POStagword.equals("PRP")
                       ||Sentence.word[counter].POStagword.equals("DT")
                       )&(counter!=wordsNumber)){
                        if( AreRelatedTogether(current,counter)==1){
                        if(i==0)  
                        newTriple.subject=Sentence.word[counter].POSword+" "+ newTriple.subject;
                        else
                        newTriple.object=Sentence.word[counter].POSword+" "+ newTriple.object;    
                          if(i==0)
                                    newTriple.subjectStart=counter;
                          if(i==2)
                                    newTriple.objectStart=counter;   
                          if(counter!=1)counter--;else break;
                    }
                    else
                        break;
                     
                 }
                }
tripleList.set(minimalEndNum,newTriple);             
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
public static void extendToLeftCopularMinimal(int i,int triplenum,int counter){
               
    Triple newTriple =new Triple("","","");
    while(
                      !(Sentence.word[counter].POStagword.equals("NN")
                      ||Sentence.word[counter].POStagword.equals("NNS")
                      ||Sentence.word[counter].POStagword.equals("NNP")
                      ||Sentence.word[counter].POStagword.equals("JJ")
                      ||Sentence.word[counter].POStagword.equals("POS")
                      ||Sentence.word[counter].POStagword.equals("PRP$")
                      ||Sentence.word[counter].POStagword.equals("PRP")
                      ||Sentence.word[counter].POStagword.equals("NNPS")
                      ||Sentence.word[counter].POStagword.equals("CD")
                      ))
              if (counter!=1)counter--;else break;
              if(i==0)
                  
              {   newTriple=tripleList.get(triplenum);
                  newTriple.subject=Sentence.word[counter].POSword;
                  newTriple.subjectStart=counter;
                  newTriple.subjectEnd=counter;}
               if(i==2)
               {  newTriple=tripleList.get(triplenum);
                  newTriple.object=Sentence.word[counter].POSword; 
                  newTriple.objectEnd=counter;
                  newTriple.objectStart=counter;}
              if (counter!=1) counter--;
              while(
                      (Sentence.word[counter].POStagword.equals("NN")||
                      IsNoun(counter)==1||
                      Sentence.word[counter].POStagword.equals("NNS")||Sentence.word[counter].POStagword.equals("CD")||
                       Sentence.word[counter].POStagword.equals("NNP")||Sentence.word[counter].POStagword.equals("JJ")||Sentence.word[counter].POStagword.equals("JJS")||Sentence.word[counter].POStagword.equals("PRP$")
                     ||Sentence.word[counter].POStagword.equals("POS")
                     ||Sentence.word[counter].POStagword.equals("NNPS")
                        ||Sentence.word[counter].POStagword.equals("PRP")
                      )&(counter!=1))
                 { 
                    if(i==0) newTriple.subject=Sentence.word[counter].POSword+" "+newTriple.subject;
                    else    newTriple.object=Sentence.word[counter].POSword+" "+newTriple.object;
                        
                    if(i==0)
                        newTriple.subjectStart=counter;
                    if(i==2)
                        newTriple.subjectEnd=counter;
                     
                     if (counter!=1) counter--;else break;
                 }
            tripleList.set(triplenum,newTriple);  
   }
public static void extendToRightCopularMinimal(int i,int triplenum,int counter){
Triple newTriple=new Triple("","","");          
    while(!(Sentence.word[counter].POStagword.equals("NN"))&!(Sentence.word[counter].POStagword.equals("NNS"))&!
                     (Sentence.word[counter].POStagword.equals("CD"))&!
                      (IsNoun(counter)==1)&!
                      (Sentence.word[counter].POStagword.equals("NNP"))&!(Sentence.word[counter].POStagword.equals("JJ"))&!
                      Sentence.word[counter].POStagword.equals("POS")
                     &!Sentence.word[counter].POStagword.equals("NNPS")
                      &!Sentence.word[counter].POStagword.equals("PRP")
                      )
                    if(counter!=wordsNumber)counter++;else break;
              newTriple=tripleList.get(triplenum);
              if(i==0)
              {   newTriple.subject=Sentence.word[counter].POSword; 
                  newTriple.subjectEnd=counter;
                  newTriple.subjectStart=counter;
              }   
               if(i==2)
               {   
                 newTriple.object=Sentence.word[counter].POSword; 
                 newTriple.objectEnd=counter;
                 newTriple.objectStart=counter;
               }
                  if(counter!=wordsNumber)counter++;
              while(
                     (Sentence.word[counter].POStagword.equals("NN")||Sentence.word[counter].POStagword.equals("NNS")||Sentence.word[counter].POStagword.equals("CD")
                      ||Sentence.word[counter].POStagword.equals("NNP")||Sentence.word[counter].POStagword.equals("JJ")||Sentence.word[counter].POStagword.equals("JJS")
                      ||IsNoun(counter)==1
                      ||Sentence.word[counter].POStagword.equals("PRP$")
                      ||Sentence.word[counter].POStagword.equals("POS")
                      ||Sentence.word[counter].POStagword.equals("NNPS")
                      ||Sentence.word[counter].POStagword.equals("PRP")
                      )&(counter!=wordsNumber))
                 {    if(i==0)
                       {
                          newTriple.subjectEnd=counter;
                          newTriple.subject= newTriple.subject+" "+Sentence.word[counter].POSword;
                       }
                    if(i==2)
                       {
                           newTriple.objectEnd=counter;
                           newTriple.object= newTriple.object+" "+Sentence.word[counter].POSword;
                       } 
                     if(counter!=wordsNumber)counter++;else break;
                 }
              tripleList.set(triplenum,newTriple);
   }

public static void replaceTiWithPrhasesMinimal(int i){
   Triple triplei=new Triple("","","");
   triplei=tripleList.get(i);
        triplei.subject = triplei.subject.replace("T1zero", NamedEntityRecognition.namedEntity[0].phrase);
        triplei.subject = triplei.subject.replace("T1one", NamedEntityRecognition.namedEntity[1].phrase);
        triplei.subject = triplei.subject.replace("T1two", NamedEntityRecognition.namedEntity[2].phrase);
        triplei.subject = triplei.subject.replace("T1three", NamedEntityRecognition.namedEntity[3].phrase);
        triplei.subject = triplei.subject.replace("T1four", NamedEntityRecognition.namedEntity[4].phrase);
        triplei.subject = triplei.subject.replace("T1five", NamedEntityRecognition.namedEntity[5].phrase);
        triplei.subject = triplei.subject.replace("T1six", NamedEntityRecognition.namedEntity[6].phrase);
        triplei.subject = triplei.subject.replace("T1seven", NamedEntityRecognition.namedEntity[7].phrase);
        triplei.subject = triplei.subject.replace("T1eight", NamedEntityRecognition.namedEntity[8].phrase);
        triplei.subject = triplei.subject.replace("T1nine", NamedEntityRecognition.namedEntity[9].phrase);
        triplei.subject = triplei.subject.replace("T1ten", NamedEntityRecognition.namedEntity[10].phrase);
        triplei.subject = triplei.subject.replace("T1eleven", NamedEntityRecognition.namedEntity[11].phrase);
        triplei.subject = triplei.subject.replace("T1twelve", NamedEntityRecognition.namedEntity[12].phrase);
        triplei.subject = triplei.subject.replace("T1thirteen", NamedEntityRecognition.namedEntity[13].phrase);
        triplei.subject = triplei.subject.replace("T1flourteen", NamedEntityRecognition.namedEntity[14].phrase);
        triplei.subject = triplei.subject.replace("T1fifteen", NamedEntityRecognition.namedEntity[15].phrase);
       
        triplei.predicate = triplei.predicate.replace("T1zero", NamedEntityRecognition.namedEntity[0].phrase);
        triplei.predicate = triplei.predicate.replace("T1one", NamedEntityRecognition.namedEntity[1].phrase);
        triplei.predicate = triplei.predicate.replace("T1two", NamedEntityRecognition.namedEntity[2].phrase);
        triplei.predicate = triplei.predicate.replace("T1three", NamedEntityRecognition.namedEntity[3].phrase);
        triplei.predicate = triplei.predicate.replace("T1four", NamedEntityRecognition.namedEntity[4].phrase);
        triplei.predicate = triplei.predicate.replace("T1five", NamedEntityRecognition.namedEntity[5].phrase);
        triplei.predicate = triplei.predicate.replace("T1six", NamedEntityRecognition.namedEntity[6].phrase);
        triplei.predicate = triplei.predicate.replace("T1seven", NamedEntityRecognition.namedEntity[7].phrase);
        triplei.predicate = triplei.predicate.replace("T1eight", NamedEntityRecognition.namedEntity[8].phrase);
        triplei.predicate = triplei.predicate.replace("T1nine", NamedEntityRecognition.namedEntity[9].phrase);
        triplei.predicate = triplei.predicate.replace("T1ten", NamedEntityRecognition.namedEntity[10].phrase);
        triplei.predicate = triplei.predicate.replace("T1eleven", NamedEntityRecognition.namedEntity[11].phrase);
        triplei.predicate = triplei.predicate.replace("T1twelve", NamedEntityRecognition.namedEntity[12].phrase);
        triplei.predicate = triplei.predicate.replace("T1thirteen", NamedEntityRecognition.namedEntity[13].phrase);
        triplei.predicate = triplei.predicate.replace("T1flourteen", NamedEntityRecognition.namedEntity[14].phrase);
        triplei.predicate = triplei.predicate.replace("T1fifteen", NamedEntityRecognition.namedEntity[15].phrase);

        
        triplei.object = triplei.object.replace("T1zero", NamedEntityRecognition.namedEntity[0].phrase);
        triplei.object = triplei.object.replace("T1one", NamedEntityRecognition.namedEntity[1].phrase);
        triplei.object = triplei.object.replace("T1two", NamedEntityRecognition.namedEntity[2].phrase);
        triplei.object = triplei.object.replace("T1three", NamedEntityRecognition.namedEntity[3].phrase);
        triplei.object = triplei.object.replace("T1four", NamedEntityRecognition.namedEntity[4].phrase);
        triplei.object = triplei.object.replace("T1five", NamedEntityRecognition.namedEntity[5].phrase);
        triplei.object = triplei.object.replace("T1six", NamedEntityRecognition.namedEntity[6].phrase);
        triplei.object = triplei.object.replace("T1seven", NamedEntityRecognition.namedEntity[7].phrase);
        triplei.object = triplei.object.replace("T1eight", NamedEntityRecognition.namedEntity[8].phrase);
        triplei.object = triplei.object.replace("T1nine", NamedEntityRecognition.namedEntity[9].phrase);
        triplei.object = triplei.object.replace("T1ten", NamedEntityRecognition.namedEntity[10].phrase);
        triplei.object = triplei.object.replace("T1eleven", NamedEntityRecognition.namedEntity[11].phrase);
        triplei.object = triplei.object.replace("T1twelve", NamedEntityRecognition.namedEntity[12].phrase);
        triplei.object = triplei.object.replace("T1thirteen", NamedEntityRecognition.namedEntity[13].phrase);
        triplei.object = triplei.object.replace("T1flourteen", NamedEntityRecognition.namedEntity[14].phrase);
        triplei.object = triplei.object.replace("T1fifteen", NamedEntityRecognition.namedEntity[15].phrase);
   tripleList.set(i,triplei);
    }

public static void processTriples(){
        int rowLine=41;

        TriplesReview();
        convertion();
        WriteTriplesInOutput(rowLine);
     
   
   }
public static void WriteTriplesInOutput(int row){
      String Output = "./Input/S2352179114200056Out.txt";
      Triple triplej= new Triple("","","");
        try {
             FileWriter fileWriter = new FileWriter(Output,true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
             bufferedWriter.newLine();
             System.out.println("-------------------Minimal Relations--------------------");
             System.out.println();
         for(int j=0;j<minimalEndNum;j++){
              replaceTiWithPrhasesMinimal(j); 
                //if(triple[j].tripleState!=0){
               // if((Minimaltriple[j].subject!="" && triplej.object!="")&(!triplej.subject.contains(triplej.object))){
               triplej=tripleList.get(j);
               if(( triplej.object!="")&(!triplej.subject.contains(triplej.object))
                         &(!triplej.predicate.matches(triplej.object))){
                 
                  bufferedWriter.newLine(); 
                 //// bufferedWriter.write(triplej.tripleType);bufferedWriter.write("------>"); 
                  bufferedWriter.write(triplej.subject); 
                  bufferedWriter.write("*");
                  bufferedWriter.write(triplej.predicate);
                  bufferedWriter.write(" ");
                  bufferedWriter.write(triplej.predicatePrep);
                  bufferedWriter.write("*");
                  bufferedWriter.write(triplej.object);
                               
                  
                  System.out.print(triplej.tripleType);System.out.print("------>");
                  System.out.print(triplej.subject+'*'+triplej.predicate+' '+triplej.predicatePrep+'*'+triplej.object); 
                  System.out.print("         ");
                  if(!triplej.typeCondition.matches("")){
                        bufferedWriter.write("                       ");
                        bufferedWriter.write(triplej.typeCondition+'*'+triplej.sortTypeCondition+'*'+triplej.elementCondition);
                        System.out.print("                      ");
                        System.out.print(triplej.typeCondition+'*'+triplej.sortTypeCondition+'*'+triplej.elementCondition);
                     }
                     System.out.println();
                     }else if (triplej.subject!="" && triplej.object==""){
                         bufferedWriter.write(triplej.subject); 
                         bufferedWriter.write("*");
                         bufferedWriter.write(triplej.predicate);
                         bufferedWriter.write("*");
                         bufferedWriter.write("---");
                         System.out.print(triplej.tripleType);System.out.print("------");
                         System.out.print(triplej.subject+'*'+triplej.predicate+'*'+triplej.object); 
                         System.out.println();
                    }
           }
        //}     
          bufferedWriter.close();
        }
        catch(IOException ex) {
            System.out.println("Error writing to file '"+ Output + "'");
            }
   }
public static void convertion(){ 
    Triple triplej=new Triple("","","");
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

              for(int j=0;j<minimalEndNum;j++){
                if(triplej.subject.matches("where")||triplej.subject.matches("Where"))
                      triplej.subject="place";
                else
                if(triplej.subject.matches("who")||triplej.subject.matches("Who"))
                      triplej.subject="person"; 
                else
                if(triplej.subject.matches("when")||triplej.subject.matches("When"))
                      triplej.subject="time"; 
                else
                if(triplej.subject.matches("what")||triplej.subject.matches("What"))
                      triplej.subject="thing";
                if(triplej.object.matches("where")||triplej.object.matches("Where"))
                      triplej.object="place";
                else
                if(triplej.object.matches("who")||triplej.object.matches("Who"))
                      triplej.object="person"; 
                else
                if(triplej.object.matches("when")||triplej.object.matches("When"))
                      triplej.object="time"; 
                else
                if(triplej.object.matches("what")||triplej.object.matches("What"))
                      triplej.object="thing"; 
                  
        }
     }
public static void TriplesReview(){
                //  FullNullObject();
                  FullNullObjectMinimal();
    }
}
