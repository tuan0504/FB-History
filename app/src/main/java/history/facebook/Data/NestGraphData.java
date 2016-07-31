package history.facebook.Data;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Tuan on 7/30/2016.
 */
public class NestGraphData {
    private String created_time;
    private String id;

    private String name;
    private String message;
    private String story;

    private Calendar calendar = null;

    private final String FORMAT = "yyyy-MM-dd";

    public String getCreatedTime() {
        if(created_time != null && created_time.length() > FORMAT.length()) {
            return created_time.substring(0, FORMAT.length());
        } else  {
            return created_time;
        }
    }

    public Calendar getDateTime(){
        try {
            if(calendar == null) {
                SimpleDateFormat format = new SimpleDateFormat(FORMAT);
                Date date = format.parse(created_time);
                calendar = Calendar.getInstance();
                calendar.setTime(date);
            }
            return calendar;
        } catch (Exception e) {
        }

        return  Calendar.getInstance();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public String getStory() {
        return story;
    }

    public String getTitle() {
        if(story != null && !story.isEmpty()) {
            return  story.trim();
        } else if(name != null && !name.isEmpty()) {
            return  name.trim();
        } else if(message != null && !message.isEmpty()) {
            return  message.trim();
        }
        return  "";
    }
}
