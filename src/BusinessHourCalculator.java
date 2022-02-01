import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class BusinessHourCalculator {

    //Variables
    private final Set<DayTimeRange> specificDates;
    private final Map<Integer, DayTimeRange> WEEKDAYS;
    private static final String DATE_FORMATTER= "EEE MMM dd HH:mm:ss yyyy";

    /**
     * Constructor
     * @param defaultOpeningTime the open time.
     * @param defaultClosingTime the close time.
     */
    public BusinessHourCalculator(String defaultOpeningTime, String defaultClosingTime){
        this.WEEKDAYS = new HashMap<>();
        this.specificDates = new HashSet<>();
        //Initialise map with default values
        for (DayOfWeekEnum day : DayOfWeekEnum.values()) {
            WEEKDAYS.put(day.getValue(), new DayTimeRange(day, false, defaultOpeningTime,
                    defaultClosingTime));
        }
    }

    /**
     * Getter
     * @return the date.
     */
    public Set<DayTimeRange> getSpecificDates() {
        return specificDates;
    }

    /**
     * Getter
     * @return the days.
     */
    public Map<Integer, DayTimeRange> getWEEKDAYS() {
        return WEEKDAYS;
    }

    /**
     * Overloaded method
     * @param dayOfWeek enum.
     * @param dayOpeningTime the open time.
     * @param dayClosingTime the close time.
     */
    public void setOpeningHours(DayOfWeekEnum dayOfWeek, String dayOpeningTime, String dayClosingTime){
        WEEKDAYS.put(dayOfWeek.getValue(), new DayTimeRange(dayOfWeek, false, dayOpeningTime,
                dayClosingTime));
    }

    /**
     * Overloaded method
     * @param specificDateTime String.
     * @param dayOpeningTime the open time.
     * @param dayClosingTime the close time.
     */
    public void setOpeningHours(String specificDateTime, String dayOpeningTime, String dayClosingTime){
        specificDates.add(new DayTimeRange(specificDateTime, false, dayOpeningTime, 
                dayClosingTime));
    }

    /**
     * Overloaded method
     * @param days Enum Varargs.
     */
    public void setClosed(DayOfWeekEnum... days){
        for (DayOfWeekEnum day:
             days) {
            DayTimeRange closed = new DayTimeRange(day, true, null,
                    null);
            WEEKDAYS.put(day.getValue(),closed);
        }
    }

    /**
     * Overloaded method
     * @param days String Varargs.
     */
    public void setClosed(String... days){
        for (String date:
             days) {
            specificDates.add(new DayTimeRange(date,true,null,null));
        }
    }

    /**
     * Method
     * @param remainingSeconds time it takes to complete job.
     * @param dropOffTime time that item was dropped off.
     * @return the date that item will be completed.
     */
    public LocalDateTime calculateDeadline(int remainingSeconds, String dropOffTime) {
        CurrentDateTime current = new CurrentDateTime(dropOffTime);

        while (remainingSeconds!=0) {
            /*
            ---------------
            SPECIFIC DATE SECTION
            ---------------
            */
            for (DayTimeRange day:
                 specificDates) {
                //Closed
                if(current.getCurrentDate().isEqual(day.getDate()) && day.isClosed()){
                    //Move to 00:00 next day
                    current.moveToNextDay();
                }
                //Open
                else if(current.getCurrentDate().isEqual(day.getDate()) && !day.isClosed()){
                    //Calculate seconds between business open and close
                    int daySeconds = (int) ChronoUnit.SECONDS.between(day.getStartTime(),day.getEndTime());
                    //Item given before business hours
                    if(day.getStartTime().isAfter(current.getCurrentTime())){
                        //Item due on current day
                        if(daySeconds > remainingSeconds) {
                           current.setCurrentDateTime(LocalDateTime.of(current.getCurrentDate()
                                           , day.getStartTime()).plusSeconds(remainingSeconds));
                            remainingSeconds = 0;
                        }
                        else{
                            //Subtracting full day's seconds and moving to next day 00:00.
                            remainingSeconds -= daySeconds;
                            current.moveToNextDay();
                        }
                    }
                    //Item given during business hours
                    else if(day.getStartTime().isBefore(current.getCurrentTime())){
                        //Seconds between start and end of business hours.
                        int daysRemainingSeconds = (int) ChronoUnit.SECONDS.between(current.getCurrentTime(),
                                day.getEndTime());
                        //Subtract day's remaining seconds and move onto next day 00:00.
                        if(daysRemainingSeconds < remainingSeconds) {
                            remainingSeconds -= daysRemainingSeconds;
                            current.moveToNextDay();
                        }
                        //Item due today, add remaining seconds to current time.
                        else {
                            current.setCurrentDateTime(LocalDateTime.of(current.getCurrentDate()
                                            ,current.getCurrentTime()).plusSeconds(remainingSeconds));
                            remainingSeconds = 0;
                        }
                    }
                    //Item given after business hours, move onto next day 00:00.
                    else if(day.getEndTime().isBefore(current.getCurrentTime())){
                        current.moveToNextDay();
                    }
                }
            }

            /*
            ---------------
            WEEKDAY SECTION
            ---------------
            */

            //Does not continue if current date is a date with special hours
            if(!specificDates.contains(current.getCurrentDate())) {
                //Get value of day as int
                DayTimeRange currentDay = WEEKDAYS.get(current.getCurrentDayOfWeek().getValue());
                //Closed
                if(currentDay.isClosed()){
                    //Move all dates to next day 00:00.
                    current.moveToNextDay();
                }
                //Open
                else{
                    //Item given before open time
                    if(currentDay.getStartTime().isAfter(current.getCurrentTime())){
                        int daySeconds = (int) ChronoUnit.SECONDS.between(currentDay.getStartTime()
                                ,currentDay.getEndTime());
                        //Item due today
                        if(daySeconds > remainingSeconds) {
                            current.setCurrentDateTime(LocalDateTime.of(current.getCurrentDate()
                                            , currentDay.getStartTime()).plusSeconds(remainingSeconds));
                            remainingSeconds = 0;
                        }
                        else{
                            //Subtract all day's seconds and move onto next day 00:00.
                            remainingSeconds -= daySeconds;
                            current.moveToNextDay();
                        }
                    }
                    //Item given during business hours
                    else if(currentDay.getStartTime().isBefore(current.getCurrentTime())){
                        int daySeconds = (int) ChronoUnit.SECONDS.between(current.getCurrentTime()
                                ,currentDay.getEndTime());
                        if(daySeconds < remainingSeconds) {
                            remainingSeconds -= daySeconds;
                            current.moveToNextDay();
                        }
                        else {
                            current.setCurrentDateTime(LocalDateTime.of(current.getCurrentDate()
                                            ,current.getCurrentTime()).plusSeconds(remainingSeconds));
                            remainingSeconds = 0;
                        }
                    }
                    //Item given after business hours
                    else if(currentDay.getEndTime().isBefore(current.getCurrentTime())){
                        current.moveToNextDay();
                    }
                }
            }
        }

        //Requested output format
        DateTimeFormatter formatterDTF = DateTimeFormatter.ofPattern(DATE_FORMATTER);
        String formatDateTime = current.getCurrentDateTime().format(formatterDTF);
        System.out.println("=> " + formatDateTime);
        //Return Date
        return current.getCurrentDateTime();
    }
}

