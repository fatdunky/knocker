package fatdunky;

import java.io.IOException;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.repackaged.com.google.gson.Gson;
import com.google.appengine.repackaged.com.google.gson.GsonBuilder;
import com.google.appengine.repackaged.com.google.gson.JsonObject;


/**
 * Servlet implementation class ReverseWordsServlet
 */
public class ReverseWordsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(ReverseWordsServlet.class.getName());
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReverseWordsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Gson gson = new GsonBuilder().create();
		StringBuilder retValBuild  =  new StringBuilder();
		String retVal;
		try {
			String parameterName  = "sentence";
			String word = request.getParameter(parameterName);
			log.info("Got scentce="+word);
			
			/* Get our scentence from request, break it into words, reverse words and add to output*/
			
			//Make sure to specify tokenizer treats spaces as tokens aswell.
			StringTokenizer tok = new StringTokenizer(word, " ", true);
	
			while (tok.hasMoreTokens()) {
				StringBuilder temp = new StringBuilder(tok.nextToken()).reverse();
				log.info("Adding word to return"+temp.toString());
				retValBuild.append(temp);
			}
				
			retVal = gson.toJson(retValBuild.toString());		
			response.setContentType("application/json");
			
		} catch (Exception e) {
			log.severe("Caught exception: "+e.toString());
			JsonObject obj = new JsonObject();
			obj.addProperty("message", "The request is invalid.");
			retVal = gson.toJson(obj);
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
		log.info("returning"+retVal);
		response.getWriter().print(retVal);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
