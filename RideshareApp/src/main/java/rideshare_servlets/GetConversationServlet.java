
package rideshare_servlets;

import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import rideshare_messaging.DBMessenger;
import rideshare_messaging.Conversation;

@WebServlet("/get-conversations")
public class GetConversationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public GetConversationServlet() {
		super();
	}
	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		PrintWriter pw = null;
		try {
			//prepare for JSON response
			resp.setContentType("application/json");	
			pw = resp.getWriter();
			resp.addHeader("Access-Control-Allow-Origin", "*");
			
			//get user ID
			int userID = Integer.parseInt(req.getParameter("sender"));
			
			//make database connection and get response
			DBMessenger dbh = new DBMessenger();
			List<Conversation> results = dbh.retrieveConversations(userID); 
			
			//turn that into JSON to respond
			Gson gson = new Gson();
			String responseStr = gson.toJson(results);
			pw.write(responseStr);
		}
		catch(Exception e) {
			System.out.println("Error occurred in message getting servlet: " + e.getMessage());
		}
		finally {
			if(pw != null) {
				pw.flush();
				pw.close();
			}
		}
	}
}

