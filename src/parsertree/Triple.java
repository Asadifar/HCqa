/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsertree;

/**
 *
 * @author mywin
 */
public class Triple {
                           //elements
    public  String subject;//triples
    public  String predicate;//triples
    public  String object;//triples
                           // elements positions
    public  int subjectStart;//triplePosition
    public  int subjectEnd;//triplePosition
    public  int predicateStart;//triplePosition
    public  int predicateEnd;//triplePosition
    public  int objectStart;//triplePosition
    public  int objectEnd;//triplePosition
                           //elements conditions
    public  String typeCondition;//tripleCondition
    public  String sortTypeCondition;//tripleCondition
    public  String elementCondition;//tripleCondition
    
                           //elements roles
    public  String  subjectRole;//tripleRoles
    public  String predicateRole;//tripleRoles
    public  String  objectRole;//tripleRoles
                            
    public  String  predicatePrep ; //triplesVerbPrep
     public  String tripleType ;
    public  int  tripleState; 
     
     //public  int triplesState ; 
     
   public Triple(String s,String o,String p){
     subject=s;
     object=o;
     predicate=p;
     subjectStart=1;//triplePosition
    subjectEnd=1;//triplePosition
    predicateStart=1;//triplePosition
    predicateEnd=1;//triplePosition
    objectStart=1;//triplePosition
    objectEnd=1;//triplePosition
    typeCondition="";//tripleCondition
    sortTypeCondition="";//tripleCondition
    elementCondition="";//tripleCondition
    
                           //elements roles
    subjectRole="";//tripleRoles
    predicateRole="";//tripleRoles
    objectRole="";//tripleRoles
                            
    predicatePrep="" ; //triplesVerbPrep
    tripleType="" ;
    tripleState=1; 
     
   }  
     
     
    
    
    public  int CompSuper=0;
   
}
