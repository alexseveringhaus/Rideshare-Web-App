package rideshare_servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rideshare_userhandling.DBUserHandler;
import rideshare_userhandling.DBUserHandler.User;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        response.addHeader("Access-Control-Allow-Origin", "*");
        
        if (username == null || password == null) {
            response.getWriter().println("Username or password not provided.");
            return;
        }

        try {
           DBUserHandler dbUH = new DBUserHandler();
           if (dbUH.getSingleUserByUsername(username) == null) {
        	   response.getWriter().println("Username does not exist.");
	       }
           else {
        	   User user = dbUH.getSingleUserByUsername(username);
        	   if (user.getPassword().equals(password)) {
        		   response.getWriter().println("Login successful.");
        	   }
        	   else {
        		   response.getWriter().println("Incorrect password.");
        	   }
           }
        } catch (SQLException sqle) {
            response.getWriter().println("Database error: " + sqle.getMessage());
        }
    }

}
