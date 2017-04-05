package fatdunky;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.repackaged.com.google.gson.Gson;
import com.google.appengine.repackaged.com.google.gson.GsonBuilder;


/**
 * Servlet implementation class FibonacciServlet
 */
public class TokenServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(TokenServlet.class.getName());
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TokenServlet() {
        super();
    }
   
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String retVal  = "73f072e6-0189-4a00-b478-c31586b61189";
		Gson gson = new GsonBuilder().create();

		/* Just return our token*/
		
		response.setContentType("application/json");
		log.info("Got request: returning: "+retVal);
		response.getWriter().print(gson.toJson(retVal));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
