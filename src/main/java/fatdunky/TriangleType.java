package fatdunky;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.repackaged.com.google.gson.Gson;
import com.google.appengine.repackaged.com.google.gson.GsonBuilder;
import com.google.appengine.repackaged.com.google.gson.JsonObject;



/**
 * Servlet implementation class FibonacciServlet
 */
public class TriangleType extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(TriangleType.class.getName());
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TriangleType() {
        super();
    }
    public boolean is_right (double side1, double side2, double side3) {
        if (((side1*side1) == ((side2*side2) + (side3*side3))) ||
            ((side2*side2) == ((side1*side1) + (side3*side3))) ||
            ((side3*side3) == ((side1*side1) + (side2*side2))))
           return true;
        else
           return false;
     }

     // Method to test for a scalene triangle.
     public boolean is_scalene (double side1, double side2, double side3) {
        if ((side1 != side2) && (side1 != side3) && (side2 != side3))
           return true;
        else
           return false;
     }

     // Method to test for an isosceles triangle.
     public boolean is_isosceles (double side1, double side2, double side3) {
        if (((side1 == side2) && (side1 != side3)) ||
            ((side1 == side3) && (side1 != side2)) ||
            ((side2 == side3) && (side2 != side1)))
           return true;
        else
           return false;
     }

     // Method to test for an equilateral triangle.
     public boolean is_equilateral (double side1, double side2, double side3) {
        if ((side1 == side2) && (side1 == side3))
           return true;
        else
           return false;
     }
    
    
    protected String triangleType(double side1, double side2, double side3) {
    	String retVal = "Error";
    	/* Check for non triangles and negatives and 0s (as the input is lengths)*/
    	if((side1 + side2 > side3) && (side1 > 0 && side2 > 0 && side3 >0))
         {
    		
             if (is_equilateral(side1,side2,side3))
             {
            	 retVal = "Equilateral";
             } 
             else if (is_isosceles(side1,side2,side3))
             {
            	 retVal = "Isosceles";
             }
             else if (is_scalene(side1,side2,side3))
             {
            	 retVal = "Scalene";
             }              
             else if (is_right(side1,side2,side3))
             {
            	 retVal = "Right";
             }        
             else
             {
            	 retVal = "Error";
             }//else
         }
  
         else
         {
        	 retVal = "Error";
         }//else
    	return retVal;
  
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Gson gson = new GsonBuilder().create();

		String parameterNameSide1  = "a";
		String parameterNameSide2  = "b";
		String parameterNameSide3  = "c";
		
		String side1String = request.getParameter(parameterNameSide1);
		String side2String = request.getParameter(parameterNameSide2);
		String side3String = request.getParameter(parameterNameSide3);
		String retVal = "";
		
		log.info("Got request Side1="+side1String+", Side2="+side2String+" ,Side3="+side3String);
		
		try {
			/*Get values from request, run throught triangle function, return result */
			double side1 = Double.parseDouble(side1String);
			double side2 = Double.parseDouble(side2String);
			double side3 = Double.parseDouble(side3String);
			
			log.info("Converted to double Side1="+side1+", Side2="+side2+" ,Side3="+side3);
			retVal = this.triangleType(side1, side2, side3);
	
			log.info("triangleType returned: "+retVal);
			response.setContentType("application/json");
			response.getWriter().print(gson.toJson(retVal));
			
		} catch (Exception nfe) {
			log.severe("Caught Exception: "+nfe.toString());
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.setContentType("application/json");
			JsonObject obj = new JsonObject();
			obj.addProperty("message", "The request is invalid.");
			retVal = gson.toJson(obj);
			
			log.severe("Returning: "+retVal);
			response.getWriter().print(retVal);
		}
		
		

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
