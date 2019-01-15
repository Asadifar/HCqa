//  This class reads the input file containing a question or a sentence in each line. 
/*
     --------------------Functions-------------------
ReadInputFile: read the Input file and each step gets one line to process and generate triples     
processSentense : processes each line    
*/
package parsertree;

 
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.apache.jena.rdf.model.Model;
import static parsertree.Sentence.processSentense;
import static parsertree.TriplesGraph.triplesGeneration;

public class Main { 
public static Model model;

public static String line = null;
public static void ReadInputFile() throws Exception { 
    
     String Input = "./Input/in.txt";
     try {
            FileReader fileReader = new FileReader(Input);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
                while((line = bufferedReader.readLine()) != null){ 
                  parsertree.Sentence.processSentense(line); 
                  triplesGeneration(line);
                } 
            bufferedReader.close();    
       }
     catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" +Input + "'");                
        }
     catch(IOException ex) {
            System.out.println(
                "Named entity recognizer does not respond");                  
        } 
}
   
public static void main(String[] args) throws Exception { 
        ReadInputFile();
   } 
}
  