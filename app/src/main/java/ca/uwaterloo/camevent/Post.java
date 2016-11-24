package ca.uwaterloo.camevent;

/**
 * Created by mactang on 2016-11-18.
 */

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Post {

    public String uid;
    public String author;
    public String title;
    public String body;
    public int starCount = 0;
    public Map<String, Boolean> stars = new HashMap<>();
    public String loc;
    public String date;

    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Post(String uid, String author, String title, String body, String loc, String date) {
        this.uid = uid;
        this.author = author;
        this.title = title;
        this.body = body;
        this.loc = loc;
        this.date = date;
    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("author", author);
        result.put("title", title);
        result.put("body", body);
        result.put("starCount", starCount);
        result.put("stars", stars);
        result.put("loc", loc);
        result.put("date", date);

        return result;
    }
    // [END post_to_map]
}
