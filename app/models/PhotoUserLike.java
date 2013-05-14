package models;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.query.Query;
import controllers.MorphiaObject;
import org.bson.types.ObjectId;
import play.data.validation.Constraints;

import java.util.List;

@Entity("Photo_User_Like")
public class PhotoUserLike {

    public static final String USER_ID = "user_id";
    public static final String PHOTO_ID = "photo_id";

    //max number of photoUserLike returned in a query
    public static final int MAX_RESULTS_RETURNED = 20;

    @Id
    private ObjectId id;

    @Constraints.Required
    @Embedded(USER_ID)
    private ObjectId userId;

    @Constraints.Required
    @Embedded(PHOTO_ID)
    private ObjectId photoId;

    @Embedded("photo_is_beautiful")
    private boolean photoIsBeautiful = false;

    @Embedded("photo_is_useful")
    private boolean photoIsUseful = false;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getUserId() {
        return userId;
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }

    public ObjectId getPhotoId() {
        return photoId;
    }

    public void setPhotoId(ObjectId photoId) {
        this.photoId = photoId;
    }

    public Boolean getPhotoIsBeautiful() {
        return photoIsBeautiful;
    }

    public void setPhotoIsBeautiful(Boolean photoIsBeautiful) {
        this.photoIsBeautiful = photoIsBeautiful;
    }

    public Boolean getPhotoIsUseful() {
        return photoIsUseful;
    }

    public void setPhotoIsUseful(Boolean photoIsUseful) {
        this.photoIsUseful = photoIsUseful;
    }

    public static List<PhotoUserLike> getFromPhotoAndUser(ObjectId photoId, ObjectId userId, int offset, int limit) {

        Query q = MorphiaObject.datastore.createQuery(PhotoUserLike.class);
        //Query q = MorphiaObject.datastore.createQuery(PhotoUserLike.class).filter("USER_ID = ", userId).filter("photo_id", photoId);

        if(photoId != null){
            q.field(PHOTO_ID).equal(photoId);
        }
        if(userId != null){
            q.field(USER_ID).equal(userId);
        }

        //paginate the query
        if(offset < 0){
            offset = 0;
        }
        if (limit <= 0 || limit > MAX_RESULTS_RETURNED){
            limit = MAX_RESULTS_RETURNED;
        }
        q.offset(offset).limit(limit);

        //cast needed here, surprisingly doesn't compile otherwise (?!)
        //PhotoUserLike photoUserLike = (PhotoUserLike) q.get();
        List<PhotoUserLike> photoUserLikes = q.asList();

        return photoUserLikes;
    }

    public ObjectId save() {
        MorphiaObject.datastore.save(this);
        return this.getId();
    }
}
