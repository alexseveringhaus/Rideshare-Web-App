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

@WebServlet ("/ride-handler")
public class RideServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public RideServlet() {
		super();
	}
	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		PrintWriter pw = null;
		try {
			//prepare for JSON response
			resp.setContentType("application/json");	
			pw = resp.getWriter();
			
			//get request parameters
			double pickup_latitude = Double.parseDouble(req.getParameter("pickup_lat"));
			double pickup_longitude = Double.parseDouble(req.getParameter("pickup_long"));
			String terminal = req.getParameter("terminal");
			String start_time = req.getParameter("start_time");
			String user1 = req.getParameter("username1");
			String user2 = req.getParameter("username2");
			
			//get user ID
			DBUserHandler dbUser = new DBUserHandler();
			int user1ID = dbUser.getUserID(user1);
			int user2ID = dbUser.getUserID(user2);
			
			//creating the ride
			Ride ride = new Ride(pickup_latitude, pickup_longitude, terminal, start_time);
			DBRideHandler dbRide = new DBRideHandler();
			boolean success = dbRide.createRide(ride, user1ID, user2ID);
			
			//write response
			pw.write("{error:" + (success ? "false":"true") + 
					", message:" + (success ? "Successfully made request":"Error occurred while making request") + 
					"}");
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
