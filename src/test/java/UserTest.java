import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class UserTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void addTagToTopic_addsToJoinTable() {
    User testUser = new User("justin");
    
    testUser.addTagToTopic()
  }


}
