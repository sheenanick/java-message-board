import java.util.List;
import org.sql2o.*;
import java.util.ArrayList;

public class Post {
  private int id;
  private int topic_id;
  private int user_id;
  private String message;
  private Timestamp time_created;

  public Post(int _topic_id, int _user_id, String _message) {
    topic_id = _topic_id
    user_id = _user_id;
    message = _message;
    this.save();
  }

  // Getters/Setters
  public int getId() {
    return id;
  }

  public int getTopicId() {
    return topic_id;
  }

  public int getUserId() {
    return user_id;
  }

  public String getMessage() {
    return message;
  }

  public Timestamp getTimeCreated() {
    return time_created;
  }

  public void setMessage(String _message) {
    message = _message;
    this.update();
  }

  // Database Functions
  public void save() {
     try(Connection con = DB.sql2o.open()) {
       String sql = "INSERT INTO posts (topic_id, user_id, message, time_created) VALUES (:topic_id, :user_id, :message, now())";
       id = (int) con.createQuery(sql, true)
         .addParameter("topic_id", topic_id)
         .addParameter("user_id", user_id)
         .addParameter("message", message)
         .executeUpdate()
         .getKey();
     }
   }

   public void update() {
     try(Connection con = DB.sql2o.open()) {
       String sql = "UPDATE posts SET topic_id, user_id, message, time_created = :user_id, :message, now() WHERE id = :id";
       con.createQuery(sql)
         .addParameter("topic_id", topic_id)
         .addParameter("user_id", user_id)
         .addParameter("message", message)
         .executeUpdate();
     }
   }

   public void delete() {
     try(Connection con = DB.sql2o.open()) {
       String sql2 = "DELETE FROM posts WHERE id = :id";
       con.createQuery(sql2)
         .addParameter("id", id)
         .executeUpdate();
     }
   }
}
