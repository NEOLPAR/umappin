package models;

import com.google.code.morphia.annotations.Reference;

import controllers.MorphiaObject;
import org.bson.types.ObjectId;
import org.codehaus.jackson.node.ObjectNode;

import play.libs.Json;

import java.util.ArrayList;
import java.util.List;

/**
 * User: a.digangi
 * Date: 23/02/13
 * Time: 22.44
 */
public class Message extends Item{

	public String message;

	public ObjectId writerId;
	
	@Reference
	public ObjectId replyToMsg; // ID of the message this message is replying to

	public static List<Message> all() {
		if (MorphiaObject.datastore != null) {
			return MorphiaObject.datastore.find(Message.class).asList();
		} else {
			return new ArrayList<Message>();
		}
	}

	public static Message findById(ObjectId id) {
		return MorphiaObject.datastore.get(Message.class, id);
	}
	
	public static Message findById(String id) {
		return MorphiaObject.datastore.get(Message.class, new ObjectId(id));
	}
	
	/** Parses a message list and prepares it for exporting to JSON
	 * @param msgs Message list
	 * @return List of ObjectNodes ready for use in toJson
	 */
	public static List<ObjectNode> messagesToObjectNodes (List<Message> msgs){
	List<ObjectNode> messages = new ArrayList<ObjectNode>();
		for(Message message : msgs){
			messages.add(messageToObjectNode(message));
		}
		return messages;
	}
	
	/** Parses a message and prepares it for exporting to JSON
	 * @param message A message
	 * @return ObjectNode ready for use in toJson
	 */
	public static ObjectNode messageToObjectNode (Message message){
		
		User user = User.findById(message.writerId, User.class);
		if (user == null){
			return null;
		}
		
		ObjectNode userNode = Json.newObject();
		userNode.put("id", user.id.toString());
		userNode.put("name", user.name);
		userNode.put("profilePicture", user.profilePicture != null ? user.profilePicture.toString() : null);
		
		ObjectNode messageNode = Json.newObject();
		messageNode.put("id", message.id.toString());
		messageNode.put("user", userNode);
		messageNode.put("message", message.message.toString());
		messageNode.put("timeStamp", message.id.getTime());
		return messageNode;
	}
}