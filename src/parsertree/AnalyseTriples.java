/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsertree;


import static parsertree.Parsertree.triples;
import static parsertree.Parsertree.POSword;

import static parsertree.Parsertree.TwoNounTriplesEndNum;


import java.io.StringWriter;
import java.io.StringReader;
import java.io.PrintWriter;
import java.util.List; 
 
import edu.stanford.nlp.process.CoreLabelTokenFactory; 
import edu.stanford.nlp.ling.CoreLabel;   
import edu.stanford.nlp.trees.*; 
import edu.stanford.nlp.parser.lexparser.LexicalizedParser; 
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.trees.Tree;
import java.io.IOException;
import java.util.Arrays;
import static parsertree.Parsertree.sent2;
import static parsertree.Parsertree.wordsNumber;

/**
 *
 * @author WIN8
 */
public class AnalyseTriples {
     public final LexicalizedParser parser = LexicalizedParser.loadModel(Parsertree.PCG_MODEL);
     public static String[] popStrArray=new String [50];
     public static int counterPopArray;
     public static void main(String[] args){
     //Parsertree.main(args);
     Tree parse;char character; 
     String[] stack=new String [100];
     String[] subStr;
     int length;
     String[] tempStrArr=new String[10];
     
     String[] popNumArray=new String [50];
     int i,j,k;
     String StrTemp;
     counterPopArray=1;
     System.out.println();
     System.out.println("***********       Next section is the output of AnalyseTriples.java        ******");
     System.out.println();
  /*   for(j=0;j<=TwoNounTriplesEndNum;j++)
     {
       System.out.print(triples[j][0]+'*'+triples[j][1]+'*'+triples[j][2]); 
       System.out.println();
       
    }*/ 
//-----------------------------------prints tockenz and then  parse tree----------------------------------
    
    TokenizerFactory<CoreLabel> tokenizerFactory =  
    PTBTokenizer.factory(new CoreLabelTokenFactory(), ""); 
    List<CoreLabel> rawWords2 =  tokenizerFactory.getTokenizer(new StringReader(sent2)).tokenize(); 
     LexicalizedParser lp;
       //defines model for lexicalized parser
       lp = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");  
    parse = lp.apply(rawWords2); 
    //parse.pennPrint();
  
         //-----------------------------------------dependencies or parse tree-------------------
   //TreePrint tp = new TreePrint("wordsAndTags,penn,typedDependenciesCollapsed"); //for instance amod(currencies-14,own-12) structures
     TreePrint tp = new TreePrint("penn");// extraxt parse tree like structure 
    StringWriter stringWriter = new StringWriter(); 
    PrintWriter pw = new PrintWriter(stringWriter); 
    tp.printTree(parse,pw); 
    
    String output =  stringWriter.toString(); 
    //Tree t;
    
    TreeReader tr = null; 
        try { 
            tr = new PennTreeReader(new StringReader(output), new LabeledScoredTreeFactory()); 
            System.out.println(tr.readTree()); 
            
             } 
        catch (IOException e) { 
            throw new IllegalStateException(e); 
        } 
        finally { 
           // closeQuietly(tr); 
        } 
        
    System.out.println(output);
    //------------Reading Tree and constructing the tree-------------------
    
   // ArrayList<Tree> pronouns = new ArrayList<Tree>();
    StrTemp=parse.getNodeNumber(1).toString()+"*";
    i=1;k=0;stack[k]="(";
    k++;
    //subStr[1]=parse.getNodeNumber(2).toString();
    
    subStr = StrTemp.split("ROOT");
    //j=2;
   /* while(!subStr[1].endsWith("*"))
            {j++;
             subStr[1]=subStr[1]+POSword[i]+subStr[j];
             }*/
    
   while(i<wordsNumber)
     {  
        if ( i!=wordsNumber-1)
        { subStr = subStr[1].split(POSword[i]);
             j=1;
           while(!subStr[1].endsWith("*"))
            {j++;
             subStr[1]=subStr[1]+POSword[i]+subStr[j];
             }
          length=subStr[0].length();
        }    
        else
        {subStr[0]=subStr[1];
         length= subStr[0].length()-7;
        }         

        for(j=0;j<length;j++)
              {
                  char ch=subStr[0].charAt(j);
                  if(ch=='(')//push
                     {String temp=Character.toString(ch);
                       stack[k]=temp;
                       k++;
                     }
                  else
                    if(ch==')')//pop
                     {  k--;
                        String popStr=")";
                        while(!stack[k].equals("("))
                        {
                            popStr=stack[k]+" "+popStr;
                            k--;
                        }
                         popStr="("+" "+popStr;
                         //stack[k]=popStr;
                         
                         stack[k]=Integer.toString(counterPopArray);
                         popStrArray[counterPopArray]=popStr;
                         popNumArray[counterPopArray]=Integer.toString(counterPopArray);
                         
                         counterPopArray++;
                         k++;
                      }  
                 //  System.out.print(subStr[1]);
               }
       if ( i!=wordsNumber-1)
       stack[k]=POSword[i]+'/'+i;
       else
       {   //counterPopArray--;
           popStrArray[counterPopArray]="( "+stack[2]+" "+ (counterPopArray-1)+" )";
           System.out.print(Arrays.toString(popStrArray)); }  
       k++;
       i++;
     }
     
    }

}



