import java.util.List;
import org.sql2o.*;
import java.util.ArrayList;


public class Topic {
  private int id;
  private int user_id;
  private String title;
  private String message;
  // private Timestamp time_created;

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
  // public Timestamp getTimeCreated() {
  //   return time_created;
  // }

  public void setTitle(String _title) {
    title = _title;
    this.update();
  }
  public void setMessage(String _message) {
    message = _message;
    this.update();
  }

  // Add Functions
  public void addTagToTopic(int _topic_id, String _tagName) {
    boolean check = Tag.createNewTag(_tagName);

    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM tags WHERE name = :name";
      Tag receivedTag = con.createQuery(sql)
        .addParameter("name", _tagName)
        .executeAndFetchFirst(Tag.class);
      String sql2 = "INSERT INTO topics_tags (tag_id, topic_id) VALUES (:tag_id, :topic_id)";
      con.createQuery(sql2)
        .addParameter("topic_id", _topic_id)
        .addParameter("tag_id", receivedTag.getId())
        .executeUpdate();
    }
  }

  // Database Functions
  public void save() {
     try(Connection con = DB.sql2o.open()) {
       String sql = "INSERT INTO topics (user_id, title, message) VALUES (:user_id, :title, :message)";
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
       String sql = "UPDATE topics SET user_id = :user_id, title = :title, message = :message WHERE id = :id";
       con.createQuery(sql)
         .addParameter("user_id", user_id)
         .addParameter("title", title)
         .addParameter("message", message)
         .addParameter("id", id)
         .executeUpdate();
     }
   }

   public void delete() {
     try(Connection con = DB.sql2o.open()) {
       String sql = "DELETE FROM topics_tags WHERE topic_id = :id";
       con.createQuery(sql)
         .addParameter("id", id)
         .executeUpdate();
       String sql2 = "DELETE FROM topics WHERE id = :id";
       con.createQuery(sql2)
         .addParameter("id", id)
         .executeUpdate();
     }
   }

   public static List<Topic> all() {
     try(Connection con = DB.sql2o.open()) {
       String sql = "SELECT * FROM topics";
       List<Topic> allTopics = con.createQuery(sql)
       .executeAndFetch(Topic.class);
       return allTopics;
     }
   }

   public static Topic find(int _id) {
     try(Connection con = DB.sql2o.open()) {
       String sql = "SELECT * FROM topics where id=:id";
       Topic topic = con.createQuery(sql)
         .addParameter("id", _id)
         .executeAndFetchFirst(Topic.class);
       return topic;
     }
   }
}
