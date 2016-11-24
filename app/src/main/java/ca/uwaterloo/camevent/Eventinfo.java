package ca.uwaterloo.camevent;

/**
 * Created by Steve on 2016/10/24.
 */
public class Eventinfo {
    private String EventTitle;
    private String EventLocation;
    private String  EventDesc;
    private String  EventLink;
    private String  fromDate;
    private String EventLatitude;
    private String EventLongitude;

    public Eventinfo() {
    }

    public  Eventinfo(String eventTitle,String eventLocationName,String eventLatitude,String eventLongitude,String eventDescriptionRow,String eventLink,String eventDate) {
        this.EventTitle=eventTitle;
        this.EventLocation=eventLocationName;
        this.EventLatitude=eventLatitude;
        this.EventLongitude=eventLongitude;
        this.EventDesc=eventDescriptionRow;
        this.EventLink=eventLink;
        this.fromDate=eventDate;
    }



    public String getEventDescriptionRow() {
        return EventDesc;
    }

    public void setEventDescriptionRow(String eventDescriptionRow) {
        EventDesc = eventDescriptionRow;
    }

    public String getEventTitle() {
        return EventTitle;
    }

    public void setEventTitle(String eventTitle) {
        EventTitle = eventTitle;
    }

    public String getEventLocationName() {
        return EventLocation;
    }

    public void setEventLocationName(String eventLocationName) {
        EventLocation = eventLocationName;
    }

    public String getEventLatitude() {
        return EventLatitude;
    }

    public void setEventLatitude(String eventLatitude) {
        EventLatitude = eventLatitude;
    }

    public String getEventLongitude() {
        return EventLongitude;
    }

    public void setEventLongitude(String eventLongitude) {
        EventLongitude = eventLongitude;
    }

    public String getEventLink() {
        return EventLink;
    }

    public void setEventLink(String eventLink) {
        EventLink = eventLink;
    }

    public String getEventDate() {
        return fromDate;
    }

    public void setEventDate(String eventDate) {
        fromDate = eventDate;
    }


}


