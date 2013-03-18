package controllers;


import models.Follows;
import models.User;


import org.codehaus.jackson.JsonNode;
import play.libs.Json;
import play.mvc.Http.Request;
import play.mvc.*;

import play.mvc.Result;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import providers.MyUsernamePasswordAuthProvider;
import providers.MyUsernamePasswordAuthUser;

import java.util.List;

public class FollowREST extends Controller {

    // TODO review this two

	@Restrict(@Group(Application.USER_ROLE))
	public static Result follow() {
		final User follower = Application.getLocalUser(session());
        String followedName =  request().body().asFormUrlEncoded().get("name")[0];
        Follows res = Follows.follow(follower, User.findByName(followedName));
        return res != null ? ok("") : notFound("Could not follow");
	}

    @Restrict(@Group(Application.USER_ROLE))
    public static Result unfollow() {
        final User follower = Application.getLocalUser(session());
        String followedName =  request().body().asFormUrlEncoded().get("name")[0];
        Follows res = Follows.unfollow(follower, User.findByName(followedName));
        return res != null ? ok("") : notFound("Could not unfollow");
    }


    // GET(ALL)
    @Restrict(@Group(Application.USER_ROLE))
    public static Result getAllFollows() {
        List<Follows> res = Follows.all();
        return ok(Json.toJson(res));
    }

    // GET
    @Restrict(@Group(Application.USER_ROLE))
    public static Result getFollows(String userId) {
        Follows res = Follows.findById(userId);
        if (res == null)
            res = Follows.create(userId);
        return ok(Json.toJson(res));
    }

    // POST
    @Restrict(@Group(Application.USER_ROLE))
    public static Result addFollows() {
        JsonNode json = request().body().asJson();
        if(json == null) {
            return badRequest("Expecting Json data");
        } else {
            String userId = json.findPath("id").getTextValue();
            Follows follows = Follows.create(userId);
            List<String> listFollows = json.findValuesAsText("follow");
            follows.update(listFollows);
            return ok("");
        }
    }

    // PUT
    @Restrict(@Group(Application.USER_ROLE))
    public static Result updateFollows(String userId) {
        JsonNode json = request().body().asJson();
        if(json == null) {
            return badRequest("Expecting Json data");
        } else {
            Follows follows = Follows.findById(userId);
            if (follows == null)
                return  badRequest("Invalid Index");
            List<String> listFollows = json.findValuesAsText("follows");
            follows.update(listFollows);
            return ok("");
        }
    }

    // DELETE
    @Restrict(@Group(Application.USER_ROLE))
    public static Result deleteFollows(String userId) {
        Follows follows = Follows.findById("userId");
        if (follows != null) {
            Follows res = follows.delete();
            if (res != null)
                return ok("");
        }
        return null;
    }

}
