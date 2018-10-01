/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbpediaspotlightwebservice;

/**
 *
 * @author WIN8
 */
import static breeze.serialization.StringSerialization$class.encoding;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import java.net.HttpURLConnection;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.dbpedia.spotlight.exceptions.AnnotationException;
import org.dbpedia.spotlight.model.DBpediaResource;
import org.dbpedia.spotlight.model.Text;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.ws.rs.HttpMethod;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.dbpedia.extraction.util.IOUtils;

/**
 * @author pablomendes
 */
public abstract class AnnotationClient {

    //public Logger LOG = Logger.getLogger(this.getClass());
    private List<String> RES = new ArrayList<String>();

    // Create an instance of HttpClient.
    private static HttpClient client = new HttpClient();
    public List<String> getResu(){
        return RES;     
    }

    public String request(GetMethod getMethod) throws AnnotationException {
        String response = null ;
        
        
        // Provide custom retry handler is necessary
        ( getMethod).getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler(10, false));
       //Socket webSocket = new Socket(domain, 80);
        //webSocket.connect(new InetSocketAddress(domain, 80), 10);
        try {
            // Execute the method.
            int statusCode = client.executeMethod((org.apache.commons.httpclient.HttpMethod) getMethod);
            if (statusCode != HttpStatus.SC_OK) {
               // LOG.error("Method failed: " + ((HttpMethodBase) method).getStatusLine());
            }

            // Read the response body.
            //InputStream response; //TODO Going to buffer response body of large or unknown size. Using getResponseBodyAsStream instead is recommended.

            // Deal with the response.
            // Use caution: ensure correct character encoding and is not binary data
           
            InputStream responseStream = ((HttpMethodBase) getMethod).getResponseBodyAsStream();
           // response = new String(responseBody);
         response = new BufferedReader(new InputStreamReader(responseStream))
  .lines().collect(Collectors.joining("\n"));
         
        } catch (HttpException e) {
           // LOG.error("Fatal protocol violation: " + e.getMessage());
            throw new AnnotationException("Protocol error executing HTTP request.",e);
        } catch (IOException e) {
            //((Object) LOG).error("Fatal transport error: " + e.getMessage());
            //((Object) LOG).error(((HttpMethodBase) method).getQueryString());
            throw new AnnotationException("Transport error executing HTTP request.",e);
        } finally {
            // Release the connection.
            ((HttpMethodBase) getMethod).releaseConnection();
        }
        return response;

    }

    protected static String readFileAsString(String filePath) throws java.io.IOException{
        return readFileAsString(new File(filePath));
    }

    protected static String readFileAsString(File file) throws IOException {
        byte[] buffer = new byte[(int) file.length()];
        @SuppressWarnings("resource")
        BufferedInputStream f = new BufferedInputStream(new FileInputStream(file));
        f.read(buffer);
        return new String(buffer);
    }

    static abstract class LineParser {

        public abstract String parse(String s) throws ParseException;

        static class ManualDatasetLineParser extends LineParser {
            public String parse(String s) throws ParseException {
                return s.trim();
            }
        }

        static class OccTSVLineParser extends LineParser {
            public String parse(String s) throws ParseException {
                String result = s;
                try {
                    result = s.trim().split("\t")[3];
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new ParseException(e.getMessage(), 3);
                }
                return result; 
            }
        }
    }

    public void saveExtractedEntitiesSet(String Question, LineParser parser, int restartFrom) throws Exception {
        String text = Question;
        int i=0;
        //int correct =0 ; int error = 0;int sum = 0;

        for (String snippet: text.split("\n")) {
            String s = parser.parse(snippet);
            if (s!= null && !s.equals("")) {
                i++;

                if (i<restartFrom) continue;

                List<DBpediaResource> entities = new ArrayList<DBpediaResource>();

                try {
                    entities = extract(new Text(snippet.replaceAll("\\s+"," ")));
                   // System.out.println(entities.get(0).getFullUri());

                } catch (AnnotationException e) {
                   // error++;
                    //LOG.error(e);
                    e.printStackTrace();
                }
                for (DBpediaResource e: entities) {
                    RES.add(e.uri());
                }
            }
        }
    }


 private static String  API_URL    = " http://model.dbpedia-spotlight.org/en/annotate?";
    private static  double  CONFIDENCE = 0.0;
   private static  int     SUPPORT    = 0;
    public List<DBpediaResource> extract(Text text) throws AnnotationException {
       // LOG.info("Querying API.");
       

       String spotlightResponse;
       int timeoutConnection = 10 * 1000;
HttpConnectionParams.setConnectionTimeout((HttpParams) client.getParams(),
                timeoutConnection);
       String Query=API_URL +
               // "confidence=" + CONFIDENCE
               //+ "&support=" + SUPPORT+
               "text=" +
               "&confidence=" + CONFIDENCE
               + "&support=" + SUPPORT+
               "types=Person,Organisation";
       Query="http://model.dbpedia-spotlight.org/en/annotate?text=President%20Obama%20called%20Wednesday%20on%20Congress%20to%20extend%20a%20tax%20break%20for%20students%20included%20in%20last%20year%27s%20economic%20stimulus%20package,%20arguing%20that%20the%20policy%20provides%20more%20generous%20assistance.&confidence=0.2&support=20&types=Person,Organisation";
       GetMethod getMethod = new GetMethod(Query);
       getMethod.addRequestHeader(new Header("Accept", "application/json"));
       spotlightResponse = request(getMethod);
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
        if(entities!=null) 
        for(int i = 0; i < entities.length(); i++) {
            try {
                JSONObject entity = entities.getJSONObject(i);
                resources.add(
                        new DBpediaResource(entity.getString("@URI"),
                        Integer.parseInt(entity.getString("@support"))));
            } catch (JSONException e) {
                //((Object) LOG).error("JSON exception "+e);
            }
        }
        return resources;
    }

    public void evaluate(String Question) throws Exception {
        evaluateManual(Question,0);
    }

    public void evaluateManual(String Question, int restartFrom) throws Exception {
         saveExtractedEntitiesSet(Question,new LineParser.ManualDatasetLineParser(), restartFrom);
    }
}