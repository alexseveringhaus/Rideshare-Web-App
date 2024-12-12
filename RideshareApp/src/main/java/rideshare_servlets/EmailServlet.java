package rideshare_servlets;

import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rideshare_ridehandling.DBRideHandler;
import rideshare_ridehandling.Ride;
//INCLUDE DBUSERHANDLER
import rideshare_userhandling.DBUserHandler;


@WebServlet ("/EmailServlet")
public class EmailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        resp.addHeader("Access-Control-Allow-Origin", "*");
		PrintWriter pw = null;
		try {
			//prepare for JSON response
			resp.setContentType("application/json");	
			pw = resp.getWriter();
			
			//get request parameters
			int userID = Integer.parseInt(req.getParameter("user_id"));
			
			//get user ID
			DBUserHandler dbUser = new DBUserHandler();
			String email = dbUser.getUserByUID(userID);

            //write response
			pw.println("{");
		    pw.println("\"email\":" + "\"" + email + "\"");
		    pw.println("}");
			
		}
		catch(Exception e) {
			System.out.println("Error occurred in ride request servlet: " + e.getMessage());
		}
		finally {
			if(pw != null) {
				pw.flush();
				pw.close();
			}
		}
	}
}
