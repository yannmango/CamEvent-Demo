package ca.uwaterloo.camevent;

import java.util.ArrayList;

import Core.APIResult;
import Core.JSONDownloader;
import Core.UWOpenDataAPI;
import Events.Event;
import Events.EventsParser;

/**
 * Created by sound on 2016/10/27.
 */
public class Initialize implements JSONDownloader.onDownloadListener {

    private EventsParser parser = new EventsParser();
    private ArrayList<Event> events=null;

    public Initialize(){
        parser.setParseType(EventsParser.ParseType.EVENTS);

        String apiKey = "5f59ce30b239a9e2e1f2e3d5774c562b"; // store your key in strings.xml

        // Step 2
        String url = UWOpenDataAPI.buildURL(parser.getEndPoint(), apiKey);

        JSONDownloader downloader = new JSONDownloader(url);
        downloader.setOnDownloadListener(this);
        downloader.start(); // starts download in seperate thread
    }
    public void onDownloadFail(String givenURL, int index) {
        // this method is called if the download fails (No internet connection, timeout, bad url, missing permission etc).
        //Log.i(LOGCAT_TAG, "Download failed.. url = " + givenURL);
    }

    @Override
    public void onDownloadComplete(APIResult apiResult) {
        // Step 4
        // parseJSON() will do different types of parsing depending on what ParseType you give it.
        // Each Parser has their own ParseTypes
        //parser.setParseType(EventsParser.ParseType.EVENTS_SITE_ID);
        parser.setAPIResult(apiResult);
        parser.parseJSON();
        // Step 5
        //events = parser.getEvents();
    }
    public ArrayList<Event> getEvents(){
        events = parser.getEvents();
        return events;
    }
}
