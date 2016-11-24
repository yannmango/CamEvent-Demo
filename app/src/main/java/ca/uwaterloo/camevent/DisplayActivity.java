package ca.uwaterloo.camevent;


import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class DisplayActivity extends BaseActivity{


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private static final String TAG = "PostDetailActivity";
    private GoogleApiClient client;
    private String strTitle;
    //private String strDes;
    private String strDate;
    private String strLoc;
    private String strLink;
    public static final String EXTRA_POST_KEY = "post_key";

    private DatabaseReference mPostReference;
    private String mPostKey;

    TextView title;
    TextView link;
    TextView location;
    TextView time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        title = (TextView) findViewById(R.id.title);
        link = (TextView) findViewById(R.id.link);
        location = (TextView) findViewById(R.id.location);
        time = (TextView) findViewById(R.id.time);

        mPostKey = getIntent().getStringExtra(EXTRA_POST_KEY);

        // Initialize Database
        mPostReference = FirebaseDatabase.getInstance().getReference()
                .child("posts").child(mPostKey);
    }
    @Override
    public void onStart() {
        super.onStart();

        // Add value event listener to the post
        // [START post_value_event_listener]
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Post post = dataSnapshot.getValue(Post.class);
                // [START_EXCLUDE]
                time.setText(post.date);
                title.setText(post.title);
                link.setText(post.body);
                location.setText(post.loc);
                // [END_EXCLUDE]
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                Toast.makeText(DisplayActivity.this, "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        };
        mPostReference.addValueEventListener(postListener);
        // [END post_value_event_listener]

    }

}





