import java.util.List;
import org.sql2o.*;
import java.util.ArrayList;
import java.sql.Timestamp;

public class Tag {
  private int id;
  private String name;

  public Tag(String _name) {
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

  // Create Functions
  public static boolean createNewTag(String _name) {
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

  // Database Functions
  public void save() {
     try(Connection con = DB.sql2o.open()) {
       String sql = "INSERT INTO tags (name) VALUES (:name)";
       id = (int) con.createQuery(sql, true)
         .addParameter("name", name)
         .executeUpdate()
         .getKey();
     }
   }

   public void update() {
     try(Connection con = DB.sql2o.open()) {
       String sql = "UPDATE tags SET name = :name WHERE id = :id";
       con.createQuery(sql)
         .addParameter("name", name)
         .addParameter("id", id)
         .executeUpdate();
     }
   }

   public void delete() {
     try(Connection con = DB.sql2o.open()) {
       String sql = "DELETE FROM topics_tags WHERE tag_id = :id";
       con.createQuery(sql)
         .addParameter("id", id)
         .executeUpdate();
       String sql2 = "DELETE FROM tags WHERE id = :id";
       con.createQuery(sql2)
         .addParameter("id", id)
         .executeUpdate();
     }
   }

   public static List<Tag> all() {
     try(Connection con = DB.sql2o.open()) {
       String sql = "SELECT * FROM tags";
       List<Tag> allTags = con.createQuery(sql)
       .executeAndFetch(Tag.class);
       return allTags;
     }
   }
 }
