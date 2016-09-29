import java.util.List;
import org.sql2o.*;
import java.util.ArrayList;

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
       String sql2 = "DELETE FROM tags WHERE id = :id";
       con.createQuery(sql2)
         .addParameter("id", id)
         .executeUpdate();
     }
   }
 }
