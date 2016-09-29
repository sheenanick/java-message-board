import java.util.List;
import org.sql2o.*;
import java.util.ArrayList;

public class Topic {
  private int id;
  private int user_id;
  private String title;
  private String message;
  private Timestamp time_created;

  public Topic(int _user_id, String _title, String _message) {
    user_id = _user_id;
    title = _title;
    message = _message;
    this.save();
  }

  // Getters/Setters
  public int getId() {
    return id;
  }
  public int getUserId() {
    return user_id;
  }
  public String getTitle() {
    return title;
  }
  public String getMessage() {
    return message;
  }
  public Timestamp getTimeCreated() {
    return time_created;
  }

  public void setTitle(String _title) {
    title = _title;
    this.update();
  }
  public void setMessage(String _message) {
    message = _message;
    this.update();
  }

  // Add Functions
  
  // Database Functions
  public void save() {
     try(Connection con = DB.sql2o.open()) {
       String sql = "INSERT INTO topics (user_id, title, message, time_created) VALUES (:user_id, :title, :message, now())";
       id = (int) con.createQuery(sql, true)
         .addParameter("user_id", user_id)
         .addParameter("title", title)
         .addParameter("message", message)
         .executeUpdate()
         .getKey();
     }
   }

   public void update() {
     try(Connection con = DB.sql2o.open()) {
       String sql = "UPDATE topics SET user_id, title, message, time_created = :user_id, :title, :message, now() WHERE id = :id";
       con.createQuery(sql)
         .addParameter("user_id", user_id)
         .addParameter("title", title)
         .addParameter("message", message)
         .executeUpdate();
     }
   }

   public void delete() {
     try(Connection con = DB.sql2o.open()) {
       String sql2 = "DELETE FROM topics WHERE id = :id";
       con.createQuery(sql2)
         .addParameter("id", id)
         .executeUpdate();
     }
   }
}
