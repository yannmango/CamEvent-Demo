package ca.uwaterloo.camevent;

/**
 * Created by mactang on 2016-11-17.
 */
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;

import java.util.ArrayList;
import java.util.Arrays;

public class Favourite extends Fragment {
    private DatabaseReference mDatabase;
    // [END define_database_reference]
    private static final String TAG = "FavouriteFragment";

    Spinner spinnerBuilding;
    private FirebaseRecyclerAdapter<Post, PostViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    String loc;

    public Favourite() {}

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_sub_page02, container, false);

        // [START create_database_reference]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END create_database_reference]

        mRecycler = (RecyclerView) rootView.findViewById(R.id.messages_list);
        mRecycler.setHasFixedSize(true);
        spinnerBuilding = (Spinner) rootView.findViewById(R.id.spinnerBuilding);
        showSpinner();

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        spinnerBuilding.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v,
                                       int position, long id) {
                // On selecting a spinner item
                loc = adapter.getItemAtPosition(position).toString();
                // Showing selected spinner item
//                Toast.makeText(getContext(),
//                        "Selected Country : " + loc, Toast.LENGTH_SHORT).show();
                // Set up Layout Manager, reverse layout
                mManager = new LinearLayoutManager(getActivity());
                mManager.setReverseLayout(true);
                mManager.setStackFromEnd(true);
                mRecycler.setLayoutManager(mManager);

                // Set up FirebaseRecyclerAdapter with the Query
                Query postsQuery = getQuery(mDatabase);
                mAdapter = new FirebaseRecyclerAdapter<Post, PostViewHolder>(Post.class, R.layout.item_post,
                        PostViewHolder.class, postsQuery) {
                    @Override
                    protected void populateViewHolder(final PostViewHolder viewHolder, final Post model, final int position) {
                        final DatabaseReference postRef = getRef(position);

                        // Set click listener for the whole post view
                        final String postKey = postRef.getKey();
                        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Launch PostDetailActivity
                                Intent intent = new Intent(getActivity(), DisplayActivity.class);
                                intent.putExtra(DisplayActivity.EXTRA_POST_KEY, postKey);
                                startActivity(intent);
                            }
                        });

                        // Determine if the current user has liked this post and set UI accordingly
                        if (model.stars.containsKey(getUid())) {
                            viewHolder.starView.setImageResource(R.drawable.ic_toggle_star_24);
                        } else {
                            viewHolder.starView.setImageResource(R.drawable.ic_toggle_star_outline_24);
                        }

                        // Bind Post to ViewHolder, setting OnClickListener for the star button
                        viewHolder.bindToPost(model, new View.OnClickListener() {
                            @Override
                            public void onClick(View starView) {
                                // Need to write to both places the post is stored
                                DatabaseReference globalPostRef = mDatabase.child("posts").child(postRef.getKey());
                                DatabaseReference userPostRef = mDatabase.child("user-posts").child(model.uid).child(postRef.getKey());

                                // Run two transactions
                                onStarClicked(globalPostRef);
                                onStarClicked(userPostRef);
                            }
                        });
                    }
                };
                mRecycler.setAdapter(mAdapter);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

    }

    // [START post_stars_transaction]
    private void onStarClicked(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Post p = mutableData.getValue(Post.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }

                if (p.stars.containsKey(getUid())) {
                    // Unstar the post and remove self from stars
                    p.starCount = p.starCount - 1;
                    p.stars.remove(getUid());
                } else {
                    // Star the post and add self to stars
                    p.starCount = p.starCount + 1;
                    p.stars.put(getUid(), true);
                }

                // Set value and report transaction success
                mutableData.setValue(p);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d(TAG, "postTransaction:onComplete:" + databaseError);
            }
        });
    }
    // [END post_stars_transaction]

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.cleanup();
        }
    }
    private void showSpinner(){
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterBuilding = ArrayAdapter.createFromResource(getContext(),
                R.array.spinnerBuilding, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterBuilding.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerBuilding.setAdapter(adapterBuilding);

    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public Query getQuery(DatabaseReference databaseReference){

        loc = spinnerBuilding.getSelectedItem().toString();
        Query myFavourite = databaseReference.child("posts").orderByChild("loc")
                .equalTo(loc);
        return myFavourite;
    }

}


