package ca.uwaterloo.camevent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;

import Core.APIResult;
import Core.JSONDownloader;
import Core.UWOpenDataAPI;
import Events.Event;
import Events.EventTime;
import Events.EventsParser;

public class WaterlooOD_test extends AppCompatActivity implements JSONDownloader.onDownloadListener {

    String apiKey = null;
    final String LOGCAT_TAG = "My Activity";
    EventsParser parser = new EventsParser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waterloo_od_test);
        //...
        // Step 1, do not redownload on screen rotation

        if(savedInstanceState == null) {

            parser.setParseType(EventsParser.ParseType.EVENTS);

            String apiKey = getString(R.string.api_key); // store your key in strings.xml

            // Step 2
            String url = UWOpenDataAPI.buildURL(parser.getEndPoint("institute-for-quantum-computing","1938"), apiKey);

            // Step 3
            JSONDownloader downloader = new JSONDownloader(url);
            downloader.setOnDownloadListener(this);
            downloader.start(); // starts download in seperate thread

       }
    }

   // ...

    @Override
    public void onDownloadFail(String givenURL, int index) {
        // this method is called if the download fails (No internet connection, timeout, bad url, missing permission etc).
        Log.i(LOGCAT_TAG, "Download failed.. url = " + givenURL);
    }

    @Override
    public void onDownloadComplete(APIResult apiResult) {
        // Step 4
        // parseJSON() will do different types of parsing depending on what ParseType you give it.
        // Each Parser has their own ParseTypes
        //Log.d(LOGCAT_TAG,String.valueOf(parser.getParseType()));
        parser.setParseType(EventsParser.ParseType.EVENTS_SITE_ID);
        //Log.d(LOGCAT_TAG,String.valueOf(parser.getParseType()));
        parser.setAPIResult(apiResult);
        parser.parseJSON();



        // Step 5
        //ArrayList<Event> events = parser.getEvents();
        Event events= parser.getSpecificEvent();
       // EventTime eventTime=parser.getSpecificEvent();
        Log.d(LOGCAT_TAG, events.getEventTitle());
        Log.d(LOGCAT_TAG, events.getEventDescriptionRaw());
        Log.d(LOGCAT_TAG, events.getLocationName());
        Log.d(LOGCAT_TAG, String.valueOf(events.getLongitude()));
        Log.d(LOGCAT_TAG, String.valueOf(events.getLatitude()));
        Log.d(LOGCAT_TAG, events.getLink());
        ArrayList<EventTime> eventTimes= events.getTimes();
        Log.d(LOGCAT_TAG, eventTimes.get(0).getStartDate());
        //double a=43.471174;
        //System.out.println(a);
        //EventDBHandler eventDB= new EventDBHandler(this);
        //Eventinfo eventinfo=new Eventinfo(events.getEventTitle(),events.getLocationName(),String.valueOf(events.getLatitude()),String.valueOf(events.getLongitude()),events.getEventDescriptionRaw(),events.getLink(),eventTimes.get(0).getStartDate());
        //eventDB.addEventinfo(eventinfo);
        //Eventinfo eventinfo1= eventDB.getEvent(1);
        //System.out.println(eventinfo1.getEventDescriptionRow());
    }
}
