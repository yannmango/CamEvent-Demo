package ca.uwaterloo.camevent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class EventDBTest extends AppCompatActivity {

    private static final String TAG = "EventDBTest";
    private TextView tvCondition;
    private Button btnSunny;
    private Button btnFoggy;
    private String condition;

    private Firebase mFireRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_dbtest);
        Firebase.setAndroidContext(this);
        tvCondition = (TextView)findViewById(R.id.text_condition);

        mFireRef = new Firebase("https://camevent-380cb.firebaseio.com/condition");

        mFireRef.addValueEventListener(
                new com.firebase.client.ValueEventListener() {
                    @Override
                    public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                        condition = dataSnapshot.getValue(String.class);
                        tvCondition.setText(condition);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                }
        );

        btnSunny = (Button)findViewById(R.id.button_sunny);
        btnFoggy = (Button)findViewById(R.id.button_foggy);
        btnSunny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFireRef.setValue("Sunny");
            }
        });
        btnFoggy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFireRef.setValue("Foggy");
            }
        });

    }
}

