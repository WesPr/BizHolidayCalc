import java.time.LocalDate;
import java.time.LocalTime;

public class DayTimeRange {

    //Variables
    private LocalDate date;
    private boolean isClosed;
    private LocalTime startTime;
    private LocalTime endTime;
    private DayOfWeekEnum day;

    /**
     * Constructor
     * @param date String to store date.
     * @param isClosed boolean to determine if business is open or closed on date.
     * @param startTime business hours open time.
     * @param endTime business hours close time.
     */
    public DayTimeRange(String date, boolean isClosed, String startTime, String endTime) {
        this.date = LocalDate.parse(date);
        this.isClosed = isClosed;
        if(startTime == null || endTime == null){
            this.startTime = null;
            this.endTime = null;
        }else{
            this.startTime = LocalTime.parse(startTime);
            this.endTime = LocalTime.parse(endTime);
        }
    }

    /**
     * Constructor
     * @param day String to store day.
     * @param isClosed boolean to determine if business is open or closed on day.
     * @param startTime business hours open time.
     * @param endTime business hours close time.
     */
    public DayTimeRange(DayOfWeekEnum day, boolean isClosed, String startTime, String endTime) {
        this.day = day;
        this.isClosed = isClosed;
        if(startTime == null || endTime == null){
            this.startTime = null;
            this.endTime = null;
        }else{
            this.startTime = LocalTime.parse(startTime);
            this.endTime = LocalTime.parse(endTime);
        }
    }

    /**
     * Getter
     * @return the date.
     */
    public LocalDate getDate() {return date;}

    /**
     * Getter
     * @return the date/days open time.
     */
    public LocalTime getStartTime() {return startTime;}

    /**
     * Getter
     * @return the date/days close time.
     */
    public LocalTime getEndTime() {return endTime;}

    public boolean isClosed() {
        return isClosed;
    }
}
