package controllers;

import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONObject;
import server.Main;

import javax.ws.rs.CookieParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

@Path("user/")
public class User {

    @POST
    @Path("login")
    public String loginUser(@FormDataParam("username") String username, @FormDataParam("password") String password) {
        System.out.println("Invoked loginUser() on path user/login");
        try {
            PreparedStatement ps1 = Main.db.prepareStatement("SELECT Password FROM Users WHERE Username = ?");
            ps1.setString(1, username);
            ResultSet loginResults = ps1.executeQuery();
            if (loginResults.next() == true) {
                String correctPassword = loginResults.getString(1);
                if (password.equals(correctPassword)) {
                    String token = UUID.randomUUID().toString();
                    PreparedStatement ps2 = Main.db.prepareStatement("UPDATE Users SET Token = ? WHERE Username = ?");
                    ps2.setString(1, token);
                    ps2.setString(2, username);
                    ps2.executeUpdate();
                    JSONObject userDetails = new JSONObject();
                    userDetails.put("username", username);
                    userDetails.put("token", token);
                    return userDetails.toString();
                } else {
                    return "{\"Error\": \"Incorrect password!\"}";
                }
            } else {
                return "{\"Error\": \"Username and password are incorrect.\"}";
            }
        } catch (Exception exception) {
            System.out.println("Database error during /user/login: " + exception.getMessage());
            return "{\"Error\": \"Server side error!\"}";
        }
    }


    public static boolean validToken(String token) {        // this method MUST be called before any data is returned to the browser
// token is taken from the Cookie sent back automatically with every HTTP request
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT UserID FROM Users WHERE Token = ?");
            ps.setString(1, token);
            ResultSet logoutResults = ps.executeQuery();
            return logoutResults.next();   //logoutResults.next() will be true if there is a record in the ResultSet
        } catch (Exception exception) {
            System.out.println("Database error" + exception.getMessage());
            return false;
        }
    }


}
