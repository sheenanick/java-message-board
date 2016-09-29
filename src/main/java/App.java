import java.util.Map;
import java.util.HashMap;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;
import java.util.List;
import org.sql2o.*;
import java.util.ArrayList;

public class App {
  public static void main(String[] args) {
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    get("/", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      String catcher = request.session().attribute("user_id");

      if(catcher == null){
        catcher = "";
        request.session().attribute("user_id", catcher);
      }

      model.put("user", catcher);
      model.put("users", User.all());
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/users/:id/signin", (request, response) ->  {
      Map<String, Object> model = new HashMap<String, Object>();

      request.session().attribute("user_id", request.queryParams("user_id"));
      User user = User.find(Integer.parseInt(request.queryParams("user_id")));

      model.put("users", User.all());
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/users", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("users", User.all());
      model.put("template", "templates/users.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/users", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      String name = request.queryParams("name");
      User newUser = new User(name);

      model.put("users", User.all());
      model.put("template", "templates/users.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/users/:id/delete", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      User user = User.find(Integer.parseInt(request.params(":id")));
      user.delete();
      model.put("users", User.all());
      model.put("template", "templates/users.vtl");
      return new ModelAndView(model, layout);

    }, new VelocityTemplateEngine());

    get("/users/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      User newUser = User.find(Integer.parseInt(request.params(":id")));
      model.put("topics", User.allTopicsByUser(newUser.getId()));
      model.put("user", newUser);
      model.put("template", "templates/user.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/tags", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("tags", Tag.all());
      model.put("template", "templates/tags.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/tags", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      String name = request.queryParams("name");
      Tag.createNewTag(name);

      model.put("tags", Tag.all());
      model.put("template", "templates/tags.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/topics", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();

      model.put("topics", Topic.all());
      model.put("template", "templates/topics.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/topics", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      String title = request.queryParams("title");
      String message = request.queryParams("message");
      int userId = Integer.parseInt(request.session().attribute("user_id"));
      Topic newTopic = new Topic(userId, title, message);

      model.put("topics", Topic.all());
      model.put("template", "templates/topics.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/topics/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Topic newTopic = Topic.find(Integer.parseInt(request.params(":id")));
      List<Post> topicPosts = Post.allPostsByTopic(newTopic.getId());
      int userId = Integer.parseInt(request.session().attribute("user_id"));
      model.put("user", userId);
      model.put("topic", newTopic);
      model.put("posts", topicPosts);
      model.put("template", "templates/topic.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/topics/:id/posts", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      String message = request.queryParams("message");
      int userId = Integer.parseInt(request.session().attribute("user_id"));
      int topic_id = Integer.parseInt(request.params(":id"));
      Post newPost = new Post(topic_id, userId, message);
      Topic newTopic = Topic.find(Integer.parseInt(request.params(":id")));
      List<Post> topicPosts = Post.allPostsByTopic(newTopic.getId());
      model.put("user", userId);
      model.put("topic", newTopic);
      model.put("posts", topicPosts);
      model.put("template", "templates/topic.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/topics/:id/delete", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      int userId = Integer.parseInt(request.session().attribute("user_id"));
      Topic newTopic = Topic.find(Integer.parseInt(request.params(":id")));
      newTopic.delete();
      model.put("topics", Topic.all());
      model.put("template", "templates/topics.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/topics/:topicid/posts/:id/delete", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Post newPost = Post.find(Integer.parseInt(request.params(":id")));
      newPost.delete();
      model.put("topics", Topic.all());
      model.put("template", "templates/topics.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/topics/:topicid/posts/:id/edit", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Post newPost = Post.find(Integer.parseInt(request.params(":id")));
      model.put("post", newPost);
      model.put("template", "templates/editPost.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/topics/:topicid/posts/:id/edit", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      int topicId = Integer.parseInt(request.params(":topicid"));
      int postId = Integer.parseInt(request.params(":id"));
      Post newPost = Post.find(postId);
      String message = request.queryParams("message");
      newPost.setMessage(message);
      response.redirect("/topics/" + topicId);
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

  }
}
