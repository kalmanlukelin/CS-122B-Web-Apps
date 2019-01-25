import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.annotation.Resource;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;


/**
 * This class is declared as LoginServlet in web annotation, 
 * which is mapped to the URL pattern /api/login
 */
@WebServlet(name = "LoginServlet", urlPatterns = "/api/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	String username = request.getParameter("username");
        String password = request.getParameter("password");

        /**
         * This example only allows username/password to be anteater/123456
         * In real world projects, you should talk to the database to verify username/password
         */ 
        
        boolean passwordMatch = false;
        
        try {
        	Connection dbcon = dataSource.getConnection();
            // Declare our statement
            Statement statement = dbcon.createStatement();
            
            // Query database to get top 20 movies list.
            String query = "SELECT c.password FROM `customers` c WHERE c.email =" + "'"+ username+"'";
            
            // Perform the query
            ResultSet rs = statement.executeQuery(query);

            JsonArray jsonArray = new JsonArray();
            while (rs.next()) {
            	String _password = rs.getString("password");
            	if(password.equals(_password)) {
            		// getSession() : if exist, get the id, if not generate another one
                    String sessionId = ((HttpServletRequest) request).getSession().getId(); 
                    Long lastAccessTime = ((HttpServletRequest) request).getSession().getLastAccessedTime();
                    request.getSession().setAttribute("user", new User(username));

                    JsonObject responseJsonObject = new JsonObject();
                    responseJsonObject.addProperty("status", "success");
                    responseJsonObject.addProperty("message", "success");

                    response.getWriter().write(responseJsonObject.toString());
            	}
            }
            
            
            // write JSON string to output
            // set response status to 200 (OK)
            //response.setStatus(200);

            rs.close();
            statement.close();
            dbcon.close();
        } catch (Exception e) {
        	
            JsonObject responseJsonObject = new JsonObject();
            responseJsonObject.addProperty("status", "fail");
            if (!username.equals("anteater")) {
                responseJsonObject.addProperty("message", "user " + username + " doesn't exist");
            } else {
                responseJsonObject.addProperty("message", "incorrect password");
            }
            response.getWriter().write(responseJsonObject.toString());

        }

//        //if (username.equals("anteater") && password.equals("123456")) {
//        if (passwordMatch) {
//            // Login succeeds
//            // Set this user into current session
//            String sessionId = ((HttpServletRequest) request).getSession().getId();
//            Long lastAccessTime = ((HttpServletRequest) request).getSession().getLastAccessedTime();
//            request.getSession().setAttribute("user", new User(username));
//
//            JsonObject responseJsonObject = new JsonObject();
//            responseJsonObject.addProperty("status", "success");
//            responseJsonObject.addProperty("message", "success");
//
//            response.getWriter().write(responseJsonObject.toString());
//        } else {
//            // Login fails
//            JsonObject responseJsonObject = new JsonObject();
//            responseJsonObject.addProperty("status", "fail");
//            if (!username.equals("anteater")) {
//                responseJsonObject.addProperty("message", "user " + username + " doesn't exist");
//            } else {
//                responseJsonObject.addProperty("message", "incorrect password");
//            }
//            response.getWriter().write(responseJsonObject.toString());
//        }
    }
}
