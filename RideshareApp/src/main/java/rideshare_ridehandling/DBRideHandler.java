package rideshare_ridehandling;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBRideHandler extends DBConnector {
	//mysql date format
	private static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	//constructors
	public DBRideHandler(String db, String user, String pass) {
		super(db, user, pass);
	}
	public DBRideHandler() {
		super();
	}
	
	//main matching method that returns a map with keys: "matches" and "close_matches", each associated with a list
	//of ride requests
	public Map<String, List<RideRequest>> findMatches(RideRequest req) {
		Map<String, List<RideRequest>> result = Collections.synchronizedMap(new HashMap<String, List<RideRequest>>());
		
		//results lists of exact and close matches
		List<RideRequest> exactMatches = new ArrayList<RideRequest>();
		List<RideRequest> closeMatches = new ArrayList<RideRequest>();
		
		//getting common information to work with (pickup lat/long and start time)
		double lat = req.getPickupLat();
		double lon = req.getPickupLong();
		LocalDateTime startTime = req.getStartTime();
		
		//statements + result sets to close later
		PreparedStatement eSt = null;
		ResultSet eResults = null;
		PreparedStatement cSt = null;
		ResultSet cResults = null;
		
		try {
			//finding exact matches
			//calculation uses the haversine formula: https://en.wikipedia.org/wiki/Haversine_formula
			//only approximate, also partly because Earth's radius is not constant. Average radius is assumed to be 3,959 mi (https://simple.wikipedia.org/wiki/Earth_radius)
			eSt = conn.prepareStatement("SELECT ride_id, user_id, pickup_latitude, pickup_longitude, terminal, start_time," + 
					"3959 * 2 * ASIN(SQRT(0.5 * (1 - COS((pickup_latitude - ?) * PI()/180) + " +
					"COS(pickup_latitude * PI()/180) * COS(? * PI()/180) * " + 
					"(1 - COS((pickup_longitude - ?) * PI()/180))))) AS distance " +
					"FROM RideRequests HAVING distance < 2 AND terminal=? AND start_time BETWEEN ? AND ? " + 
					"ORDER BY distance ASC");
			eSt.setDouble(1, lat);
			eSt.setDouble(2, lat);
			eSt.setDouble(3, lon);
			eSt.setString(4, req.getTerminal());
			LocalDateTime beforeRange = startTime.minusMinutes(15);
			LocalDateTime afterRange = startTime.plusMinutes(15);
			eSt.setTimestamp(5, Timestamp.valueOf(beforeRange));
			eSt.setTimestamp(6, Timestamp.valueOf(afterRange));
			
			
			//execute query and create results for each of them
			eResults = eSt.executeQuery();
			while(eResults.next()) {
				int ride_id = eResults.getInt(1);
				int user_id = eResults.getInt(2);
				double pickup_lat = eResults.getDouble(3);
				double pickup_long = eResults.getDouble(4);
				String terminal = eResults.getString(5);
				String dateStr = eResults.getString(6);
				exactMatches.add(new RideRequest(ride_id, user_id, pickup_lat, pickup_long, terminal, dateStr));
			}
			
			//finding close matches - within 1 hour of start time and 2 miles of pickup
			cSt = conn.prepareStatement("SELECT ride_id, user_id, pickup_latitude, pickup_longitude, terminal, start_time, " + 
					"3959 * 2 * ASIN(SQRT(0.5 * (1 - COS((pickup_latitude - ?) * PI()/180) + " +
					"COS(pickup_latitude * PI()/180) * COS(? * PI()/180) * " + 
					"(1 - COS((pickup_longitude - ?) * PI()/180))))) AS distance " +
					"FROM RideRequests HAVING distance < 2 AND start_time BETWEEN ? AND ? " +
					"AND NOT(terminal=? AND start_time BETWEEN ? AND ?) " + //making sure exact matches aren't included twice
					"ORDER BY distance ASC");
			cSt.setDouble(1, lat);
			cSt.setDouble(2, lat);
			cSt.setDouble(3, lon);
			LocalDateTime cBefore = startTime.minusMinutes(60);
			LocalDateTime cAfter = startTime.plusMinutes(60);
			cSt.setTimestamp(4, Timestamp.valueOf(cBefore));
			cSt.setTimestamp(5, Timestamp.valueOf(cAfter));
			cSt.setString(6, req.getTerminal());
			cSt.setTimestamp(7, Timestamp.valueOf(beforeRange));
			cSt.setTimestamp(8, Timestamp.valueOf(afterRange));
					
			//execute query and create results for each of them
			cResults = cSt.executeQuery();
			while(cResults.next()) {
				int ride_id = cResults.getInt(1);
				int user_id = cResults.getInt(2);
				double pickup_lat = cResults.getDouble(3);
				double pickup_long = cResults.getDouble(4);
				String terminal = cResults.getString(5);
				String dateStr = cResults.getString(6);
				closeMatches.add(new RideRequest(ride_id, user_id, pickup_lat, pickup_long, terminal, dateStr));
			}
		}
		catch(Exception e) {
			System.out.println("Error while finding matches: " + e.getMessage());
		}
		finally {
			cleanUp(eResults);
			cleanUp(eSt);
			cleanUp(cResults);
			cleanUp(cSt);
			
			result.put("matches", exactMatches);
			result.put("close_matches", closeMatches);
		}
		return result;
	}
	
	//creates a ride request and stores it in the database
	//returns true for success, false for error
	public boolean createRideRequest(RideRequest req) {
		PreparedStatement iSt = null;
		try {
			iSt = conn.prepareStatement("INSERT INTO RideRequests (user_id, pickup_latitude, pickup_longitude, terminal, start_time) VALUES (?, ?, ?, ?, ?)");
			iSt.setInt(1, req.getUserId());
			iSt.setDouble(2, req.getPickupLat());
			iSt.setDouble(3, req.getPickupLong());
			iSt.setString(4, req.getTerminal());
			iSt.setTimestamp(5, Timestamp.valueOf(req.getStartTime()));
			return iSt.executeUpdate() > 0;
		}
		catch(Exception e) {
			System.out.println("Error while inserting ride request: " + e.getMessage());
		}
		finally {
			cleanUp(iSt);
		}
		return false;
	}
	
	//create ride function to be called when a ride is confirmed between two users
	public boolean createRide(Ride req, int user1, int user2) {
		PreparedStatement iSt = null;
		PreparedStatement iSt2 = null;
		ResultSet r = null;
		try {
			//begin a transaction first so that if any insertion fails, we won't have rides partially inserted
			conn.createStatement().execute("START TRANSACTION");
			
			//insert rides into database
			iSt = conn.prepareStatement("INSERT INTO Rides (pickup_latitude, pickup_longitude, terminal, start_time) VALUES (?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			iSt.setDouble(1, req.getPickupLat());
			iSt.setDouble(2, req.getPickupLong());
			iSt.setString(3, req.getTerminal());
			iSt.setTimestamp(4, Timestamp.valueOf(req.getStartTime()));
			boolean iSuccess = iSt.executeUpdate() > 0;
			
			//get newly-inserted ride id
			r = iSt.getGeneratedKeys();
			r.next();
			int rideId = r.getInt(1);
			
			//also insert the appropriate entries into the user/rides table
			iSt2 = conn.prepareStatement("INSERT INTO UserRides (ride_id, user_id) VALUES (?, ?)");
			iSt2.setInt(1, rideId);
			iSt2.setInt(2, user1);
			boolean user1Success = iSt2.executeUpdate() > 0;
			iSt2.setInt(2, user2);
			boolean user2Success = iSt2.executeUpdate() > 0;
			boolean result = iSuccess && user1Success && user2Success;
			
			//commit and return success status
			conn.createStatement().execute("COMMIT");
			return result;
		}
		catch(Exception e) {
			System.out.println("Error while inserting ride: " + e.getMessage());
			try {
				conn.createStatement().execute("ROLLBACK");
			}
			catch(Exception e2) {
				System.out.println("Error while rolling back");
			}
		}
		finally {
			cleanUp(r);
			cleanUp(iSt);
			cleanUp(iSt2);
		}
		return false;
	}
	
	//delete ride function, returning the number of affected rows
	public int deleteRideById(int id) {
		PreparedStatement iSt = null;
		PreparedStatement iSt2 = null;
		try {
			//begin a transaction first 
			conn.createStatement().execute("START TRANSACTION");
			
			//removing from the list of confirmed rides between users first
			iSt = conn.prepareStatement("DELETE FROM UserRides WHERE ride_id=?");
			iSt.setInt(1, id);
			int numRows1 = iSt.executeUpdate();
			
			//then deleting information from the rides table
			iSt2 = conn.prepareStatement("DELETE FROM Rides WHERE ride_id=?");
			iSt2.setInt(1, id);
			int numRows2 = iSt2.executeUpdate();
			int result = numRows1 + numRows2;
			
			//commit and then return result
			conn.createStatement().execute("COMMIT");
			return result;
		}
		catch(Exception e1) {
			System.out.println("Error while deleting ride: " + e1.getMessage());
			try {
				conn.createStatement().execute("ROLLBACK");
			}
			catch(Exception e2) {
				System.out.println("Error while rolling back");
			}
		}
		finally {
			cleanUp(iSt);
		}
		return 0;
	}
	
	//testing
	public static void main(String[] args) {
		DBRideHandler db = new DBRideHandler();
		//db.createRideRequest(new RideRequest(1, 34.021719, -118.286844, "A", "2024-11-20 09:30:00"));
		//db.createRideRequest(new RideRequest(1, 34.026043, -118.285222, "B", "2024-11-20 09:00:00"));
		//db.createRideRequest(new RideRequest(1, 34.031628, -118.283672, "A", "2024-11-20 08:45:00"));
		//db.createRideRequest(new RideRequest(1, 34.035892, -118.299591, "A", "2024-11-20 09:25:00"));
		//db.createRideRequest(new RideRequest(1, 34.057580, -118.299250, "A", "2024-11-20 09:30:00")); //out of range
		//db.createRideRequest(new RideRequest(1, 34.045944, -118.294770, "A", "2024-11-20 09:27:00")); //just barely within range
		//db.createRide(new Ride(34.022061, -118.292976, "C", "2024-11-20 10:00:00"), 1, 1);
		RideRequest testReq = new RideRequest(34.022061, -118.292976, "A", "2024-11-20 09:30:00");
		Map<String, List<RideRequest>> result = db.findMatches(testReq);
		System.out.println(result);
		db.close();
	}
}
