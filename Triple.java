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
    public static String subject;//triples
    public static String predicate;//triples
    public static String object;//triples
                           // elements positions
    public static int subjectStart;//triplePosition
    public static int subjectEnd;//triplePosition
    public static int predicateStart;//triplePosition
    public static int predicateEnd;//triplePosition
    public static int objectStart;//triplePosition
    public static int objectEnd;//triplePosition
                           //elements conditions
    public static String typeCondition;//tripleCondition
    public static String sortTypeCondition;//tripleCondition
    public static String elementCondition;//tripleCondition
    
                           //elements roles
    public static String  subjectRole;//tripleRoles
    public static String predicateRole;//tripleRoles
    public static String  objectRole;//tripleRoles
                            
    public static String  predicatePrep ; //triplesVerbPrep
     public static String tripleType ;
    public static int  tripleState; 
     
     //public static int triplesState ; 
     
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
     
     
    
    
    public static int CompSuper=0;
   
}
