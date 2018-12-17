/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsertree;

import edu.stanford.nlp.trees.TypedDependency;
import java.util.List;
import static parsertree.Main.line;
import static parsertree.Sentence.tdl;

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
  
    
}
