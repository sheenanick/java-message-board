import java.util.List;
import org.sql2o.*;
import java.util.ArrayList;
import java.sql.Timestamp;

public class User {
  private int id;
  private String name;
  private Timestamp date_registered;

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
  public Timestamp getDate() {
    return date_registered;
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

  public List<Topic> allTopicsByTag(String _tagName) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT topics.* FROM topics JOIN topics_tags ON (topics.id = topics_tags.topic_id) JOIN tags ON (tags.id = topics_tags.tag_id) WHERE tags.name = :name";
      List<Topic> allTopicsWithTag = con.createQuery(sql)
        .addParameter("name", _tagName)
        .executeAndFetch(Topic.class);
        return allTopicsWithTag;
    }
  }

  // Add Functions
  public void addTagToTopic(int _topic_id, String _tagName) {
    boolean check = this.createNewTag(_tagName);

    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM tags WHERE name = :name";
      Tag receivedTag = con.createQuery(sql)
        .addParameter("name", _tagName)
        .executeAndFetchFirst(Tag.class);
      String sql = "INSERT INTO topics_tags (tag_id, topic_id) VALUES (:tag_id, :topic_id)";
      con.createQuery(sql)
        .addParameter("topic_id", _topic_id)
        .addParameter("tag_id", receivedTag.getId())
        .executeUpdate();
    }
  }

  // Create Functions
  public boolean createNewTag(String _name) {
    // Search for tag of same name. If tag name exists, return false. Else create new tag and return true.
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM tags WHERE name = :name";
      Tag receivedTag = con.createQuery(sql)
        .addParameter("name", _name)
        .executeAndFetchFirst(Tag.class);
      if(receivedTag == null) {
        Tag newTag = new Tag(_name);
        return true;
      } else {
        return false;
      }
    }
  }

  public void createNewTopic(String _title, String _message) {
    Topic newTopic = new Topic(id, _title, _message);
  }

  public void createNewPost(int _topic_id, String _message) {
    Post newPost = new Post(_topic_id, id, _message);
  }

  // Database Functions
  public void save() {
     try(Connection con = DB.sql2o.open()) {
       String sql = "INSERT INTO users (name, date_registered) VALUES (:name, now())";
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
