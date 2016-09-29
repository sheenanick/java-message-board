import java.util.List;
import org.sql2o.*;
import java.util.ArrayList;
import java.sql.Timestamp;

public class Post {
  private int id;
  private int topic_id;
  private int user_id;
  private String message;

  public Post(int _topic_id, int _user_id, String _message) {
    topic_id = _topic_id;
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

  public void setMessage(String _message) {
    message = _message;
    this.update();
  }

  // Database Functions
  public static Post find(int _id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM posts where id=:id";
      Post post = con.createQuery(sql)
        .addParameter("id", _id)
        .executeAndFetchFirst(Post.class);
      return post;
    }
  }


  public void save() {
     try(Connection con = DB.sql2o.open()) {
       String sql = "INSERT INTO posts (topic_id, user_id, message) VALUES (:topic_id, :user_id, :message)";
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
       String sql = "UPDATE posts SET topic_id = :topic_id, user_id = :user_id, message = :message WHERE id = :id";
       con.createQuery(sql)
         .addParameter("topic_id", topic_id)
         .addParameter("user_id", user_id)
         .addParameter("message", message)
         .addParameter("id", id)
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

   public static List<Post> allPostsByTopic(int _topic_id) {
     try(Connection con = DB.sql2o.open()) {
       String sql = "SELECT * FROM posts WHERE topic_id = :topic_id";
       List<Post> allPostsWithTopic = con.createQuery(sql)
         .addParameter("topic_id", _topic_id)
         .executeAndFetch(Post.class);
         return allPostsWithTopic;
     }
   }
}
