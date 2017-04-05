package fatdunky;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.logging.Logger;

import com.google.appengine.repackaged.com.google.gson.GsonBuilder;
import com.google.appengine.repackaged.com.google.gson.JsonObject;
import com.google.appengine.repackaged.com.google.gson.Gson;


import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class FibonacciServlet
 */
public class FibonacciServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(FibonacciServlet.class.getName());
	private static HashMap<Integer,Double> preCalFibs =  new HashMap<Integer,Double>();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FibonacciServlet() {
        super();
       
    }
    @Override
    public void init(ServletConfig sc) throws ServletException
	{
    	 super.init(sc);
    	 
    	 /*Pre load as many number as is reasonable. This will use up memory but will give speed */
    	 
    	 for (int number = -1000;number < -1;number++) {
    		 preCalFibs.put(number, fib_print(number));
          	 log.finest("Loaded n="+number+", fib"+preCalFibs.get(number));
    	 }
    	 /* We have some special cases in the middle, This could be put in our fib function, but this also works */
    	 preCalFibs.put(-1,1.0);
         preCalFibs.put(0,0.0);
         preCalFibs.put(1,1.0);
	     for (int number = 2;number < 1000;number++) {
	    	preCalFibs.put(number, fib_print(number));
	    	log.finest("Loaded n="+number+", fib"+preCalFibs.get(number));
	     }
        
	}
    private static BigDecimal isqrt(BigDecimal x, MathContext mc) {
        BigDecimal guess = new BigDecimal(1/Math.sqrt(x.doubleValue()));
        BigDecimal three = new BigDecimal(3);
        BigDecimal half = new BigDecimal(0.5);
        BigDecimal oldguess;
        do{ oldguess = guess;
            guess = half.multiply(guess.multiply(
                      three.subtract(x.multiply(guess.pow(2)))), mc);
        } while (oldguess.compareTo(guess) != 0);
        
        return guess;
    }

    private static BigInteger fib(int n) {
       MathContext mc = new MathContext(n/2, RoundingMode.HALF_DOWN);
       BigDecimal is5 = isqrt(new BigDecimal("5"), mc);
       BigDecimal one = BigDecimal.ONE;
       BigDecimal half = new BigDecimal(0.5);
       BigDecimal phi = half.multiply(one.add(one.divide(is5, mc)));
       return phi.pow(n, mc).multiply(is5).toBigInteger();
    }


    private static BigInteger f(int n) {
       if(n<0)
          return (n%2==0) ? fib(-n).negate() : fib(-n);
       else
          return fib(n);
    }

    private double fib_print(int n) {
    	return f(n).doubleValue();
    } 


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Gson gson = new GsonBuilder().create();
		String parameterName  = "n", retVal ="";
		
		String number = request.getParameter(parameterName);
		log.info("request recieved n ="+number);

		/* Get number from request. Check it doesnt exceed max or min values. Check if we
		 * have allready calculated its position. If we haven't calculate it and store result.
		 * Then return precalc'd or calc'd value
		 * */
		
		try {
			int n = Integer.parseInt(number);
			log.info("Converted n to int="+n);
			double fib =0.0;
			if (n < Short.MIN_VALUE || n > Short.MAX_VALUE) {
				/* Input guard, large values make us time out and lead to fib numbers greater then we can handle*/
				throw new Exception("Value out of range");
			}
			
			if (preCalFibs.containsKey(n)) {
				fib = preCalFibs.get(n);
			} else {
				log.info("Calculating position="+n);
				fib = fib_print(n);
				/* We dont want the hash getting to big as it will chew up memory. 
				 * However, the input guard should limit the Hash to around 65k (entries)
				 * */			
				preCalFibs.put(n, fib);
				
			}	
			log.info("fib retured"+fib);
			retVal = String.format("%.0f", fib);
		
			response.setContentType("application/json");
			log.info("response="+retVal);
			
			response.getWriter().print(retVal);
		} catch (Exception nfe) {
			log.severe("Caught exception"+nfe.toString());
			JsonObject obj = new JsonObject();
			obj.addProperty("message", "The request is invalid.");
			retVal = gson.toJson(obj);
			
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.setContentType("application/json");
			
			log.severe("response 400:"+retVal);
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
