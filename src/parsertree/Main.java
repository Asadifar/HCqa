//  tokenize,2- pos tagging 3- parse tree 4- dependency parser 

package parsertree;
//java imports



//NER


import edu.stanford.nlp.ie.AbstractSequenceClassifier;


//
//import Jwi.Main;
import TAGmeWebService.tagmewebservice;

import java.util.Collection; 
import java.io.StringReader;
import java.util.List; 
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.jena.rdf.model.Model;

//stanford imports 
 

import static parsertree.Sentence.processSentense;
import static parsertree.TriplesGraph.processTriples;
import static parsertree.TriplesGraph.triplesGeneration;




public class Main { 
 public static Model model;
   
  

  
   //--------------------Functions for each lines in our input file------------
   public static void ReadInputFile() throws Exception { 
    
     //defines model for lexicalized parser
     String Input = "../Armin/sampleText/S2352179114200056.txt";
     String line = null;
    
     try {
                FileReader fileReader = new FileReader(Input);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                while((line = bufferedReader.readLine()) != null){ 
                  processSentense(line); 
                  triplesGeneration(line);
                } 
                bufferedReader.close();    
            }
    catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" +Input + "'");                
    }
    catch(IOException ex) {
            System.out.println(
                "Error reading file '" + Input + "'");                  
        }
    }
   
   public static void main(String[] args) throws Exception { 
        ReadInputFile();
   } 
}
  