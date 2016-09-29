import java.util.List;
import org.sql2o.*;
import java.util.ArrayList;

public class User {
  private int id;
  private String name;

  public User(String _name) {
    name = _name;
    this.save();
  }

  // Getters/Setters
  public int getId() {
    return id;
  }
  public String getName() {
    return name;
  }

  public void setName(String _name) {
    name = _name;
    this.update();
  }

  // Search Functions
  public static List<User> all() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM users";
      List<User> allUsers = con.createQuery(sql)
      .executeAndFetch(User.class);
      return allUsers;
    }
  }

  public static List<Post> allPostsByUser(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM posts WHERE user_id = :user_id";
      List<Post> allPosts = con.createQuery(sql)
        .addParameter("user_id", id)
        .executeAndFetch(Post.class);
        return allPosts;
    }
  }

  public static List<Topic> allTopicsByUser(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM topics WHERE user_id = :user_id";
      List<Topic> allTopics = con.createQuery(sql)
        .addParameter("user_id", id)
        .executeAndFetch(Topic.class);
        return allTopics;
    }
  }

  public List<Topic> allTopicsByTag(String _tagName) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT topics.* FROM topics JOIN topics_tags ON (topics.id = topics_tags.topic_id) JOIN tags ON (tags.id = topics_tags.tag_id) WHERE tags.name = :name";
      List<Topic> allTopicsWithTag = con.createQuery(sql)
        .addParameter("name", _tagName)
        .executeAndFetch(Topic.class);
        return allTopicsWithTag;
    }
  }

  // Create Functions


  public void createNewTopic(String _title, String _message) {
    Topic newTopic = new Topic(id, _title, _message);
  }

  public void createNewPost(int _topic_id, String _message) {
    Post newPost = new Post(_topic_id, id, _message);
  }

  // Database Functions
  public static User find(int _id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM users where id=:id";
      User user = con.createQuery(sql)
        .addParameter("id", _id)
        .executeAndFetchFirst(User.class);
      return user;
    }
  }

  public void save() {
     try(Connection con = DB.sql2o.open()) {
       String sql = "INSERT INTO users (name) VALUES (:name)";
       id = (int) con.createQuery(sql, true)
         .addParameter("name", name)
         .executeUpdate()
         .getKey();
     }
   }

   public void update() {
     try(Connection con = DB.sql2o.open()) {
       String sql = "UPDATE users SET name = :name WHERE id = :id";
       con.createQuery(sql)
         .addParameter("name", name)
         .addParameter("id", id)
         .executeUpdate();
     }
   }

   public void delete() {
     try(Connection con = DB.sql2o.open()) {
       String sql2 = "DELETE FROM users WHERE id = :id";
       con.createQuery(sql2)
         .addParameter("id", id)
         .executeUpdate();
     }
   }

  // Overrides
  @Override
  public boolean equals(Object otherUser){
    if (!(otherUser instanceof User))
      return false;
    User newUser = (User) otherUser;
    return id == newUser.getId();
  }
}
