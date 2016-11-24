package ca.uwaterloo.camevent;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class AboutActivity extends Activity {
    private int theme = 0;
    public final static int CREATE_DIALOG  = -1;
    public final static int Red_Theme  = 0;
    public final static int Brown_Theme  = 1;
    public final static int BlueGrey_Theme=2;
    public final static int Default_Theme=3;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        position = getIntent().getIntExtra("position", -1);

        switch(position)
        {
            /*case CREATE_DIALOG:
                createDialog();
                break;*/
            case Red_Theme:
                setTheme(R.style.RedTheme);
                break;
            case Brown_Theme:
                setTheme(R.style.BrownTheme);
                break;
            case BlueGrey_Theme:
                setTheme(R.style.BlueGreyTheme);
                break;
            case Default_Theme:
                setTheme(R.style.AppTheme);

        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.nav_back) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);


    }
}

