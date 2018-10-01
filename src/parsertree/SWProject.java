package parsertree;


import edu.stanford.nlp.ie.crf.*;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;

import edu.stanford.nlp.util.Triple;
import java.util.LinkedList;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.util.FileManager;

public class SWProject {

    public static Model model;
    
    public static int main(String fileContents,AbstractSequenceClassifier classifier) {
        int i=0;
        // String serializedClassifier = "F:\\Question-answer\\implementation\\myprog\\stanfordLib\\classifiers\\english.all.3class.distsim.crf.ser.gz";
        //AbstractSequenceClassifier classifier = CRFClassifier.getClassifierNoExceptions(serializedClassifier);
        //model = FileManager.get().loadModel("F:\\Question-answer\\implementation\\myprog\\stanfordLib\\SUMO.owl-master\\SUMO.owl");
        LinkedList<String> entities = new LinkedList<>();
        
        try{
            //String fileContents = "Obama";
            // String fileContents= IOUtils.slurpFile("F:\\Question-answer\\implementation\\MyActionsInHQA\\1-question-Analysis\\Armin\\sampleText\\S2352179114200056.txt");
            List<Triple<String, Integer, Integer>> list = classifier.classifyToCharacterOffsets(fileContents);
            i=list.size();
            /*for (Triple<String, Integer, Integer> item : list) {
                String term = fileContents.substring(item.second(), item.third());
              System.out.println(item.first() + ": " + term);
                //DoQuery("US");
            }*/
        } catch(Exception e){
            e.printStackTrace();
        }
return i;
    }
    
 /*   public static void DoQuery(String obj){
      /*  String queryString = 
        		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
        		"SELECT ?name WHERE { " +
        		"    'file:/D:/University/Courses/M.Sc/Semantic%20Web/tools/http-__www.adampease.org_OP_SUMO.owl#Christine_Guldbrandsen' ?first ?second . " +
        		"}";
        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        try {
            ResultSet results = qexec.execSelect();
            while ( results.hasNext() ) {
                QuerySolution soln = results.nextSolution();
                Literal name = soln.getLiteral("name");
                System.out.println(name);
            }
        } finally {
            qexec.close();
        }*/
   // }
    
}
