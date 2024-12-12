package rideshare_servlets;

import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rideshare_ridehandling.DBRideHandler;
import rideshare_ridehandling.RideRequest;
//IMPORT DBUSERHANDLER
import rideshare_userhandling.DBUserHandler;

@WebServlet ("/ride-req-handler")
public class RideReqServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	public RideReqServlet() {
		super();
	}
	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		PrintWriter pw = null;
		try {
			//prepare for JSON response
			resp.setContentType("application/json");	
			pw = resp.getWriter();
			resp.addHeader("Access-Control-Allow-Origin", "*");
			//get request parameters
			double pickup_latitude = Double.parseDouble(req.getParameter("pickup_lat"));
			double pickup_longitude = Double.parseDouble(req.getParameter("pickup_long"));
			String terminal = req.getParameter("terminal");
			String start_time = req.getParameter("start_time");
			String username = req.getParameter("username");
			
			//get user ID
			DBUserHandler dbUser = new DBUserHandler();
			int userID = dbUser.getUserID(username);
			//System.out.print(userID);
			
			//creating the ride request
			RideRequest rReq = new RideRequest(userID, pickup_latitude, pickup_longitude, terminal, start_time);
			DBRideHandler dbRide = new DBRideHandler();
			boolean success = dbRide.createRideRequest(rReq);
			
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
