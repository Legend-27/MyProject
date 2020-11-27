package controllers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import server.Main;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Path("quiz/")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)

public class Quiz {
    @GET
    @Path("list")
    public String quizList() {
        System.out.println("Invoked quiz.quizList()");
        JSONArray response = new JSONArray();
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT QuizID, QuizName FROM Quizzes");
            ResultSet results = ps.executeQuery();
            while (results.next()==true) {
                JSONObject row = new JSONObject();
                row.put("QuizID", results.getInt(1));
                row.put("QuizName", results.getString(2));
                response.add(row);
            }
            return response.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to list items.  Error code xx.\"}";
        }
    }



    @GET
    @Path("listQuestions/{quizId}")
    public String questionList(@PathParam("quizId") Integer quizId) {
        System.out.println("Invoked Questions.QuestionList()");
        JSONArray response = new JSONArray();
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT QuestionId, QuestionText FROM Questions WHERE QuizId = ?");
            ps.setInt(1,quizId);
            ResultSet results = ps.executeQuery();
            while (results.next()==true) {
                JSONObject row = new JSONObject();
                row.put("QuestionId", results.getInt(1));
                row.put("QuestionText", results.getString(2));
                response.add(row);
            }
            return response.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to list items.  Error code xx.\"}";
        }
    }
}
