/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbpediaspotlightwebservice;
import parsertree.Parsertree;
/**
 *
 * @author WIN8
 */
import com.google.appengine.api.datastore.Text;
import java.util.List;
import org.dbpedia.spotlight.exceptions.AnnotationException;
import org.dbpedia.spotlight.model.DBpediaResource;

/**
 *
 * @author WIN8
 */
public class dbpediaSpot {
    public static void main(String sentence) throws Exception {
       // String question = "What is the winning chances of BJP in New Delhi elections?";
      //  String question = " President Obama called Wednesday on Congress to extend a tax break for students included in last year s economic stimulus package, arguing that the policy provides more generous assistance.";
         String question = "country Chinese-speaking";
        db c = new db ();
        c.configiration(0.35,2);
        //, 0, "non", "AtLeastOneNounSelector", "Default", "yes");
        c.evaluate(sentence);
        System.out.println("resource : "+c.getResu());
    
    }
}
