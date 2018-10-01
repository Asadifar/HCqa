/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbpediaspotlightwebservice;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import parsertree.Parsertree;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.methods.GetMethod;
import org.dbpedia.spotlight.exceptions.AnnotationException;
import org.dbpedia.spotlight.model.DBpediaResource;
import org.dbpedia.spotlight.model.Text;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static parsertree.Parsertree.POSwordNamedEntityState;
import static parsertree.Parsertree.POSwordNamedEntityURI;

public class db extends AnnotationClient {
 //private static String  API_URL    = "http://spotlight.sztaki.hu:2222/";
 private static String  API_URL    = "http://model.dbpedia-spotlight.org/";
    private static  double  CONFIDENCE = 0.0;
   private static  int     SUPPORT    = 0;
    //private final static String API_URL = "http://jodaiber.dyndns.org:2222/";
   
   // private static  String  powered_by ="non";
   // private static  String  spotter ="CoOccurrenceBasedSelector";//"LingPipeSpotter"=Annotate all spots 
                                                //AtLeastOneNounSelector"=No verbs and adjs.    
                                                //"CoOccurrenceBasedSelector" =No 'common words'
                                                //"NESpotter"=Only Per.,Org.,Loc.
    //private static String  disambiguator ="Default";//Default ;Occurrences=Occurrence-centric;Document=Document-centric
    //private static String  showScores ="yes";

@SuppressWarnings("static-access")
public void configiration(double CONFIDENCE,int SUPPORT)
//, String powered_by,String spotter,String disambiguator,String showScores)
{
    this.CONFIDENCE=CONFIDENCE;
    this.SUPPORT=SUPPORT;
   // this.powered_by=powered_by;
    //this.spotter=spotter;
    //this.disambiguator=disambiguator;
    //showScores=showScores;

}
    public List<DBpediaResource> extract(Text text) throws AnnotationException {
       // LOG.info("Querying API.");
        String spotlightResponse;
        try {
            String Query=API_URL + "en/annotate?" +
                    "confidence=" + CONFIDENCE
                 + "&support=" + SUPPORT
                 // + "&spotter=" + spotter
                 // + "&disambiguator=" + disambiguator
                 // + "&showScores=" + showScores
                 // + "&powered_by=" + powered_by
                  + "&text=" + URLEncoder.encode(text.text(), "utf-8");
            //LOG.info(Query);
            //Query="http://model.dbpedia-spotlight.org/en/annotate?text=President%20Obama%20called%20Wednesday%20on%20Congress%20to%20extend%20a%20tax%20break%20for%20students%20included%20in%20last%20year%27s%20economic%20stimulus%20package,%20arguing%20that%20the%20policy%20provides%20more%20generous%20assistance.&confidence=0.2&support=20&types=Person,Organisation";
            GetMethod getMethod = new GetMethod(Query);
            getMethod.addRequestHeader(new Header("Accept", "application/json"));
            spotlightResponse = request(getMethod);

        } catch (UnsupportedEncodingException e) {
            throw new AnnotationException("Could not encode text.", e);
        }
        assert     spotlightResponse != null;
        JSONObject resultJSON         = null;
        JSONArray  entities           = null;

        try {                   
            resultJSON = new JSONObject(spotlightResponse);
            entities = resultJSON.getJSONArray("Resources");

        } catch (JSONException e) {
            //throw new AnnotationException("Received invalid response from DBpedia Spotlight API.");
        }

        LinkedList<DBpediaResource> resources = new LinkedList<DBpediaResource>();
        if(entities!=null){ 
            String namedEntityWordPositionstr=" ";
            String namedEntityWord=" ";
   
            for(int i = 0; i < entities.length(); i++) {
            //try {
                JSONObject entity = null;
                try {
                    entity = entities.getJSONObject(i);
                } catch (JSONException ex) {
                    Logger.getLogger(db.class.getName()).log(Level.SEVERE, null, ex);
                }
               
                try {
                    namedEntityWord=entity.getString("@surfaceForm");
                } catch (JSONException ex) {
                    Logger.getLogger(db.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    namedEntityWordPositionstr=entity.getString("@offset");
                } catch (JSONException ex) {
                    Logger.getLogger(db.class.getName()).log(Level.SEVERE, null, ex);
                }
                int namedEntityWordPosition;
                try {
                    namedEntityWordPosition = Integer.parseInt(entity.getString("@offset"));
                    for(int b=1;b<=Parsertree.wordsNumber-1;b++){
                        if(Parsertree.POSwordPositionStart[b]==namedEntityWordPosition){
                            String[] str=namedEntityWord.split(" ");
                            POSwordNamedEntityState[b]=str.length;
                            POSwordNamedEntityURI[b]=entity.getString("@URI");
                            break;
                       }
                    }
                
                } catch (JSONException ex) {
                    Logger.getLogger(db.class.getName()).log(Level.SEVERE, null, ex);
                }
            
                try {
                    resources.add(
                            new DBpediaResource(entity.getString("@URI"),
                                    Integer.parseInt(entity.getString("@support"))));
                    //  } catch (JSONException e) {
                    //((Object) LOG).error("JSON exception "+e);
                    // }
                } catch (JSONException ex) {
                    Logger.getLogger(db.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
        }
            return resources;
    }

}