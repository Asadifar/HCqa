/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsertree;

import edu.stanford.nlp.trees.TypedDependency;
import java.util.List;

/**
 *
 * @author mywin
 */
public class Verb {
    
     String  verbs;
   int verbsPosition;
    String verbsRoles;
public Verb(String v,int vP,String vR){
verbs=v;
verbsPosition=vP;
verbsRoles=vR;
}
  public static int NotCopularVerb(String str){
       //--------------------------------if verb is am, is , was,.....---------------------------------
   String[] str1; String dep1,rel1,gov1; 
    int sw1=1;
   if((str.equals("was")||str.equals("were")||
        str.equals("am")||str.equals("is")||
             str.equals("are")||str.equals("had")||
             str.equals("has")||str.equals("have")
             ||str.equals("did")||str.equals("does")
             )) {
        
             for(int j=0;j<Sentence.CounterVerbs;j++){
              int index1=0;
              List<TypedDependency> tdl=null;
   
              for (TypedDependency t1 : tdl){
                   //may be verb is copular verb such as did.....serve.verb did should be deleated
                     str1=tdl.get(index1).dep().toString().split("/");
                     dep1=str1[0];
                     str1=tdl.get(index1).reln().toString().split("/");
                     rel1=str1[0];
                     str1=tdl.get(index1).gov().toString().split("/");
                     gov1=str1[0];
                    if (dep1.equals(Sentence.verbs[j].verbs)& gov1.equals(str) & rel1.equals("aux"))
                    { index1++;sw1=1;return sw1;}
                    else
                    index1++;
                }
             }
       
       
       sw1=0; }
   return sw1;//0 not main verb,1 main verb
   }
   public static int BeCopularMainVerb(String str,int j ){
   //--------------------------------if verb is am, is , was,.....---------------------------------
   String[] str1; String dep1,rel1,gov1; 
    int sw1=1;
   if((str.equals("was")||str.equals("were")||
        str.equals("am")||str.equals("is")||
             str.equals("are")||str.equals("had")||
             str.equals("has")||str.equals("have")
             ||str.equals("did")||str.equals("does")
             ) & (!Sentence.word[Sentence.verbs[j].verbsPosition+1].POStagword.equals("VBN"))& 
              !(Sentence.word[Sentence.verbs[j].verbsPosition+1].POStagword.equals("VB"))&
              !(Sentence.word[Sentence.verbs[j].verbsPosition+1].POStagword.equals("VBP"))&
              !(Sentence.word[Sentence.verbs[j].verbsPosition+1].POStagword.equals("VBD"))&
              !(Sentence.word[Sentence.verbs[j].verbsPosition+1].POStagword.equals("VBZ"))
              ){  
              
              int index1=0;
             List<TypedDependency> tdl=null;
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
              
             
             for(j=0;j<Sentence.CounterVerbs;j++){
                  index1=0;
              for (TypedDependency t1 : tdl){
                   //may be verb is copular verb such as did.....serve.verb did should be deleated
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
   //**********
  
    
}
