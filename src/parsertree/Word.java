/*




 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsertree;

import edu.stanford.nlp.trees.TypedDependency;
import java.util.List;
import static parsertree.Sentence.govIsMatchedWhithVerbs;


/**
 *
 * @author mywin
 */
public class Word {
    

      String  POSword;
      String POStagword;
      int  POSwordStart;
      int  POSwordEnd;
      //int POSwordNamedEntityState;
      String POSwordNamedEntityURI;
      int phraseTagmeLinksRecognizer;
   public Word(String w,String tw,int ws,int we){
     POSword=w;
     POStagword=tw;
     POSwordStart=ws;
     POSwordEnd=we;
     POSwordNamedEntityURI="";
     phraseTagmeLinksRecognizer=0;
   
   }



 
   
   
 
 public static int SearchForAdvmodOfVerb(String verb,int CounterVerbs,List<TypedDependency> tdl ){
       int index2=0;
        String[] str1;int gov2Index,dep2Index,k=0;
       String rel2,gov2,dep2;   
       dep2="";
       rel2="";
                         for (TypedDependency t2 : tdl){
                            
                            str1=tdl.get(index2).dep().toString().split("/");
                            dep2=str1[0];
                            //dep2Index=tdl.get(index2).dep().index();
                            str1=tdl.get(index2).reln().toString().split("/");
                            rel2=str1[0];
                            str1=tdl.get(index2).gov().toString().split("/");
                            //gov2Index=tdl.get(index2).gov().index();
                            gov2=str1[0]; 
                            if((gov2.matches(verb)& rel2.matches("advmod")))
                                if(Sentence.verbs[CounterVerbs].verbs!=""){
                                    Sentence.verbs[CounterVerbs].verbs=  Sentence.verbs[CounterVerbs].verbs+" "+dep2;  
                                    CounterVerbs++;
                                }
                                else{
                                 Sentence.verbs[CounterVerbs].verbs=verb; 
                                  Sentence.verbs[CounterVerbs].verbs=  Sentence.verbs[CounterVerbs].verbs+" "+dep2;  
                                  CounterVerbs++;
                                }  
                            index2++;  
                            }//end for
       
       return CounterVerbs;
      
   
   
   }
 public static String relIsIndirectObject(String rel){
     String prep="";
     String[] str=rel.split("nmod");
     
      if(    rel.equals("nmod:in")//which revolutionary was born in Mvezo?(revolutionary,born,Mvezo)(nsubjpass,VBN,nmod:in)
            ||rel.equals("nmod:at")                                                   //Which recipients of the Victoria Cross fought in the Battle of Arnhem? (recipients*fought*Battle NNS*VBD*nmod:in)
           ||rel.equals("nmod:near") 
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
            ||rel.equals("nmod:over")
            ||rel.equals("nmod:like")
             ||str.length>0){
                if(rel.contains("nmod:"))
                prep= rel.split("nmod:")[1];
                return prep;
      
              }
      return prep;
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
   
   ///////noun functions
   public static int IsNoun(int i){
       if(i>1)
       if(Sentence.word[i].POStagword.equals("VBG")& (Sentence.word[i-1].POStagword.equals("IN")) )
    
           return 1;
        if(i>2)
       if(Sentence.word[i].POStagword.equals("VBG")& (Sentence.word[i-2].POStagword.equals("IN")) )
    
           return 1;
       if(
                 (
                 Sentence.word[i].POStagword.equals("NN")||Sentence.word[i].POStagword.equals("NNS")||
                 Sentence.word[i].POStagword.equals("NNP")||Sentence.word[i].POStagword.equals("JJ")||
                 Sentence.word[i].POStagword.equals("JJS")||
                 Sentence.word[i].POStagword.equals("PRP$")||Sentence.word[i].POStagword.equals("NNPS")||
                 Sentence.word[i].POStagword.equals("PRP")
                 )
                 
            ){
             return 1;
         }              
        return 0;
         }
   public static int BeCapableOfMemberOfNounRelation(int i){
       if(i>1)
       if(Sentence.word[i].POStagword.equals("VBG")& (Sentence.word[i-1].POStagword.equals("IN")) )
    
           return 1;
        if(i>2)
       if(Sentence.word[i].POStagword.equals("VBG")& (Sentence.word[i-2].POStagword.equals("IN")) )
    
           return 1;
       if(
                 (
                 Sentence.word[i].POStagword.equals("NN")||Sentence.word[i].POStagword.equals("NNS")||
                 Sentence.word[i].POStagword.equals("NNP")||Sentence.word[i].POStagword.equals("JJ")||
                 Sentence.word[i].POStagword.equals("JJS")||
                 Sentence.word[i].POStagword.equals("NNPS")
                 //Sentence.word[i].POStagword[i].equals("PRP")
                 )
                 
            ){
             return 1;
         }              
        return 0;
         }
   
     public static int GovOrDepAreValid(int govIndex,int depIndex){
    
      if(       Sentence.word[govIndex].POSword.matches("That")||  Sentence.word[govIndex].POSword.matches("that")
              || Sentence.word[govIndex].POSword.matches("This")|| Sentence.word[govIndex].POSword.matches("this")
              || Sentence.word[govIndex].POSword.matches("These")|| Sentence.word[govIndex].POSword.matches("these")
              || Sentence.word[govIndex].POSword.matches("All")|| Sentence.word[govIndex].POSword.matches("all")
              || Sentence.word[govIndex].POSword.matches("Some")|| Sentence.word[govIndex].POSword.matches("some")
              || Sentence.word[govIndex].POSword.matches("Someone")|| Sentence.word[govIndex].POSword.matches("someone")
              || Sentence.word[govIndex].POSword.matches("Something")|| Sentence.word[govIndex].POSword.matches("something")
              || Sentence.word[govIndex].POSword.matches("Somebody")|| Sentence.word[govIndex].POSword.matches("somebody")
              || Sentence.word[govIndex].POSword.matches("Nobody")|| Sentence.word[govIndex].POSword.matches("nobody")
              || Sentence.word[govIndex].POSword.matches("Anybody")|| Sentence.word[govIndex].POSword.matches("anybody")
              || Sentence.word[govIndex].POSword.matches("Anything")|| Sentence.word[govIndex].POSword.matches("anything")
              || Sentence.word[govIndex].POSword.matches("Nothing")|| Sentence.word[govIndex].POSword.matches("nothing")
              || Sentence.word[govIndex].POSword.matches("Everything")|| Sentence.word[govIndex].POSword.matches("everything")
              || Sentence.word[govIndex].POSword.matches("Everyone")|| Sentence.word[govIndex].POSword.matches("everyone")
              || Sentence.word[govIndex].POSword.matches("Hundreds")|| Sentence.word[govIndex].POSword.matches("hundreds")
              || Sentence.word[govIndex].POSword.matches("Number")|| Sentence.word[govIndex].POSword.matches("number")
              || Sentence.word[govIndex].POSword.matches("Milion")|| Sentence.word[govIndex].POSword.matches("milion")
              || Sentence.word[govIndex].POSword.matches("Little")|| Sentence.word[govIndex].POSword.matches("little")
              || Sentence.word[govIndex].POSword.matches("More")|| Sentence.word[govIndex].POSword.matches("more")
              || Sentence.word[govIndex].POSword.matches("Few")|| Sentence.word[govIndex].POSword.matches("few")
              || Sentence.word[govIndex].POSword.matches("A few")|| Sentence.word[govIndex].POSword.matches("a few")
              || Sentence.word[govIndex].POSword.matches("A bit")|| Sentence.word[govIndex].POSword.matches("a bit")
              || Sentence.word[govIndex].POSword.matches("Bit")|| Sentence.word[govIndex].POSword.matches("bit")
              || Sentence.word[govIndex].POSword.matches("Much")|| Sentence.word[govIndex].POSword.matches("much")
              || Sentence.word[govIndex].POSword.matches("Many")|| Sentence.word[govIndex].POSword.matches("many")
              || Sentence.word[govIndex].POSword.matches("Lot")|| Sentence.word[govIndex].POSword.matches("lot")
              || Sentence.word[govIndex].POSword.matches("Lots")|| Sentence.word[govIndex].POSword.matches("lots")
              || Sentence.word[govIndex].POSword.matches("Most")|| Sentence.word[govIndex].POSword.matches("most")
              || Sentence.word[govIndex].POSword.matches("Amount")|| Sentence.word[govIndex].POSword.matches("amount")
              || Sentence.word[govIndex].POSword.matches("Possibility")|| Sentence.word[govIndex].POSword.matches("possibility")
              || Sentence.word[govIndex].POSword.matches("Possibility")|| Sentence.word[govIndex].POSword.matches("possibility")
              || Sentence.word[govIndex].POSword.matches("Before")|| Sentence.word[govIndex].POSword.matches("before")
              || Sentence.word[govIndex].POSword.matches("Ago")|| Sentence.word[govIndex].POSword.matches("ago")
              || Sentence.word[govIndex].POSword.matches("Up")|| Sentence.word[govIndex].POSword.matches("up")
              || Sentence.word[govIndex].POSword.matches("Down")|| Sentence.word[govIndex].POSword.matches("down")
              
              || Sentence.word[depIndex].POSword.matches("Us")|| Sentence.word[depIndex].POSword.matches("us")
              || Sentence.word[depIndex].POSword.matches("Our")|| Sentence.word[depIndex].POSword.matches("our")
              || Sentence.word[depIndex].POSword.matches("Ours")|| Sentence.word[depIndex].POSword.matches("ours")
              || Sentence.word[depIndex].POSword.matches("Your")|| Sentence.word[depIndex].POSword.matches("your")
              || Sentence.word[depIndex].POSword.matches("Yours")|| Sentence.word[depIndex].POSword.matches("yours")
              || Sentence.word[depIndex].POSword.matches("Their")|| Sentence.word[depIndex].POSword.matches("their")
              || Sentence.word[depIndex].POSword.matches("Theirs")|| Sentence.word[depIndex].POSword.matches("theirs")
              || Sentence.word[depIndex].POSword.matches("You")|| Sentence.word[depIndex].POSword.matches("you")
              || Sentence.word[depIndex].POSword.matches("Me")|| Sentence.word[depIndex].POSword.matches("me")
              || Sentence.word[depIndex].POSword.matches("You")|| Sentence.word[depIndex].POSword.matches("you")
              || Sentence.word[depIndex].POSword.matches("Him")|| Sentence.word[depIndex].POSword.matches("him")
              || Sentence.word[depIndex].POSword.matches("Her")|| Sentence.word[depIndex].POSword.matches("her")
              || Sentence.word[depIndex].POSword.matches("It")|| Sentence.word[depIndex].POSword.matches("it")
              || Sentence.word[depIndex].POSword.matches("Them")|| Sentence.word[depIndex].POSword.matches("them")
              || Sentence.word[depIndex].POSword.matches("Up")|| Sentence.word[depIndex].POSword.matches("up")
              || Sentence.word[depIndex].POSword.matches("Down")|| Sentence.word[depIndex].POSword.matches("down")
              || Sentence.word[depIndex].POSword.matches("Someone")|| Sentence.word[depIndex].POSword.matches("someone")
              || Sentence.word[depIndex].POSword.matches("Something")|| Sentence.word[depIndex].POSword.matches("something")
              || Sentence.word[depIndex].POSword.matches("Somebody")|| Sentence.word[depIndex].POSword.matches("somebody")
              || Sentence.word[depIndex].POSword.matches("Nobody")|| Sentence.word[depIndex].POSword.matches("nobody")
              || Sentence.word[depIndex].POSword.matches("Anybody")|| Sentence.word[depIndex].POSword.matches("anybody")
              || Sentence.word[depIndex].POSword.matches("Anything")|| Sentence.word[depIndex].POSword.matches("anything")
              || Sentence.word[depIndex].POSword.matches("Nothing")|| Sentence.word[depIndex].POSword.matches("nothing")
              || Sentence.word[depIndex].POSword.matches("Everything")|| Sentence.word[depIndex].POSword.matches("everything")
              || Sentence.word[depIndex].POSword.matches("Everyone")|| Sentence.word[depIndex].POSword.matches("everyone")
              || Sentence.word[depIndex].POSword.matches("Which")|| Sentence.word[depIndex].POSword.matches("which")
              || Sentence.word[depIndex].POSword.matches("When")|| Sentence.word[depIndex].POSword.matches("when")
              || Sentence.word[depIndex].POSword.matches("Where")|| Sentence.word[depIndex].POSword.matches("where")
              || Sentence.word[depIndex].POSword.matches("What")|| Sentence.word[depIndex].POSword.matches("what")
              || Sentence.word[depIndex].POSword.matches("Whose")|| Sentence.word[depIndex].POSword.matches("whose")
              )
          return 0;
      else
          return 1;
    }
     public static int  IsAQuotationPhrase(String rel,int govIndex,int depIndex){
                for(int i=0;i<Sentence.quotationCounter;i++){
                   if(Sentence.quotations[i][2].quotation.matches(rel) & Sentence.quotations[i][3].quotation.matches( Sentence.word[govIndex].POSword) & Sentence.quotations[i][4].quotation.matches( Sentence.word[depIndex].POSword) & Sentence.quotations[i][5].quotation=="0"){
                   return 1;
                   
                   }
                   
                }
        return 0;
    }
     public static int IsCapital(int i){
   if(Sentence.wordPre[i].POSword.startsWith("A")||Sentence.wordPre[i].POSword.startsWith("B")||Sentence.wordPre[i].POSword.startsWith("C")
           ||Sentence.wordPre[i].POSword.startsWith("A")||Sentence.wordPre[i].POSword.startsWith("A")||Sentence.wordPre[i].POSword.startsWith("C")
           ||Sentence.wordPre[i].POSword.startsWith("D")||Sentence.wordPre[i].POSword.startsWith("E")||Sentence.wordPre[i].POSword.startsWith("F")
           ||Sentence.wordPre[i].POSword.startsWith("G")||Sentence.wordPre[i].POSword.startsWith("H")||Sentence.wordPre[i].POSword.startsWith("I")
           ||Sentence.wordPre[i].POSword.startsWith("J")||Sentence.wordPre[i].POSword.startsWith("K")||Sentence.wordPre[i].POSword.startsWith("L")
           ||Sentence.wordPre[i].POSword.startsWith("M")||Sentence.wordPre[i].POSword.startsWith("N")||Sentence.wordPre[i].POSword.startsWith("O")
           ||Sentence.wordPre[i].POSword.startsWith("P")||Sentence.wordPre[i].POSword.startsWith("Q")||Sentence.wordPre[i].POSword.startsWith("R")
           ||Sentence.wordPre[i].POSword.startsWith("S")||Sentence.wordPre[i].POSword.startsWith("T")||Sentence.wordPre[i].POSword.startsWith("U")
           ||Sentence.wordPre[i].POSword.startsWith("V")||Sentence.wordPre[i].POSword.startsWith("W")||Sentence.wordPre[i].POSword.startsWith("X")
           ||Sentence.wordPre[i].POSword.startsWith("Y")||Sentence.wordPre[i].POSword.startsWith("Z"))return 1;
   return 0;
   }
    public static int govIsSomeSpecialWords(String word){
     if(word.matches("number") ||word.matches("numbers")||word.matches("hundreds")||word.matches("milions"))
        return 1;
         return 0;
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
   
   public static int IsPossessiveAdjective(String dep2){//Possessive adjectives - my, your, his, her, its, our, your, their - modify the noun following it in order to show possession.
   if(dep2.matches("my")||dep2.matches("your")||dep2.matches("his")||dep2.matches("his")||dep2.matches("her")||dep2.matches("its")||dep2.matches("our")||dep2.matches("your")||dep2.matches("their"))
    return 1;
   return 0;
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
  
  
   
   
   
   public static int BeCapableOfMemberOfNounRelationSecondArguman(int i){
      
       if(
                 (
                 Sentence.word[i].POStagword.equals("NN")||Sentence.word[i].POStagword.equals("NNS")||
                 Sentence.word[i].POStagword.equals("NNP")||
                 Sentence.word[i].POStagword.equals("NNPS")
                 //Sentence.word[i].POStagword[i].equals("PRP")
                 )
                 
            ){
             return 1;
         }              
        return 0;
         }
   
   public static int NPprepNp(int pos){
       if(Sentence.word[pos+1].POStagword.equals("IN"))
          return pos+1;
   return 0;
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
         case 11: return "eleven";
         case 12: return "twelve"; 
         case 13: return "thirteen";
         case 14: return "flourteen";
         case 15: return "fifteen";


          }
       return "";
   }
  public static int IsGenitivePossSubject(int i){
      int counter=i;
       while((Sentence.word[counter].POStagword.equals("NN")||
                        IsNoun(counter)==1||
                        Sentence.word[counter].POStagword.equals("NNS")||Sentence.word[counter].POStagword.equals("CD")||Sentence.word[counter].POStagword.equals("IN")||
                       Sentence.word[counter].POStagword.equals("NNP")||Sentence.word[counter].POStagword.equals("JJ")||Sentence.word[counter].POStagword.equals("JJS")||Sentence.word[counter].POStagword.equals("PRP$")
                     // ||Sentence.word[counter].POStagword.equals("POS")
                        //||Sentence.word[counter].POStagword.equals('"')
                      //||Sentence.word[counter].POStagword.equals("CC")
                         ||Sentence.word[counter].POStagword.equals("NNPS")
                          ||Sentence.word[counter].POStagword.equals("PRP")
                       // ||Sentence.word[counter].POStagword.equals("DT")
               //         ||Sentence.word[counter].POStagword.equals("RB")
               )&(counter!=Sentence.wordsNumber))
                counter++;
        if(Sentence.word[counter].POSword.matches("'s")){counter++; return counter; }
   return 0;}
  public static int FindRerenceOfThat(int index){
    int index1=0,k,dep1Index,gov1Index;
    String[] str1; String dep1,rel1,gov1;
     List<TypedDependency> tdl=null;
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
  
  

  
}
