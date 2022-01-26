import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
            boolean isClosed = false;
            WEEKDAYS.put(day.getValue(), new DayTimeRange(day, isClosed, defaultOpeningTime,
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
        //Parse dropoff time in required format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        //Variables
        LocalDateTime currentDateTime = LocalDateTime.parse(dropOffTime, formatter);
        LocalDate currentDate = currentDateTime.toLocalDate();
        LocalTime currentTime = currentDateTime.toLocalTime();
        DayOfWeek currentDayOfWeek = currentDateTime.getDayOfWeek();

        while (remainingSeconds!=0) {
            /*
            ---------------
            SPECIFIC DATE SECTION
            ---------------
            */
            for (DayTimeRange day:
                 specificDates) {

                //Closed
                if(currentDate.isEqual(day.getDate()) && day.isClosed){
                    //Moving all dates to 00:00 next day
                    currentDate = currentDate.plusDays(1);
                    currentDateTime = currentDate.atStartOfDay();
                    currentTime = currentDateTime.toLocalTime();
                    currentDayOfWeek =currentDate.getDayOfWeek();
                }

                //Open
                else if(currentDate.isEqual(day.getDate()) && !day.isClosed){
                    //Calculate seconds between business open and close
                    int daySeconds = (int) ChronoUnit.SECONDS.between(day.getStartTime(),day.getEndTime());

                    //Item given before business hours
                    if(day.getStartTime().isAfter(currentTime)){
                        //Item due on current day
                        if(daySeconds > remainingSeconds) {
                            currentDateTime = LocalDateTime.of(currentDate, day.getStartTime())
                                    .plusSeconds(remainingSeconds);
                            remainingSeconds = 0;
                        }
                        else{
                            //Subtracting full day's seconds and moving to next day 00:00.
                            remainingSeconds -= daySeconds;
                            currentDate = currentDate.plusDays(1);
                            currentDateTime = currentDate.atStartOfDay();
                            currentTime = currentDateTime.toLocalTime();
                            currentDayOfWeek =currentDate.getDayOfWeek();
                        }
                    }
                    //Item given during business hours
                    else if(day.getStartTime().isBefore(currentTime)){
                        //Seconds between start and end of business hours.
                        int daysRemainingSeconds = (int) ChronoUnit.SECONDS.between(currentTime,
                                day.getEndTime());
                        //Subtract day's remaining seconds and move onto next day 00:00.
                        if(daysRemainingSeconds < remainingSeconds) {
                            remainingSeconds -= daysRemainingSeconds;
                            currentDate = currentDate.plusDays(1);
                            currentDateTime = currentDate.atStartOfDay();
                            currentTime = currentDateTime.toLocalTime();
                            currentDayOfWeek =currentDate.getDayOfWeek();
                        }
                        //Item due today, add remaining seconds to current time.
                        else {
                            currentDateTime = LocalDateTime.of(currentDate,currentTime)
                                    .plusSeconds(remainingSeconds);
                            remainingSeconds = 0;
                        }
                    }
                    //Item given after business hours, move onto next day 00:00.
                    else if(day.getEndTime().isBefore(currentTime)){
                        currentDate = currentDate.plusDays(1);
                        currentDateTime = currentDate.atStartOfDay();
                        currentTime = currentDateTime.toLocalTime();
                        currentDayOfWeek =currentDate.getDayOfWeek();
                    }
                }
            }


            /*
            ---------------
            WEEKDAY SECTION
            ---------------
            */

            //Does not continue if current date is a date with special hours
            if(!specificDates.contains(currentDate)) {
                //Get value of day as int
                DayTimeRange currentDay = WEEKDAYS.get(currentDayOfWeek.getValue());

                //Closed
                if(currentDay.isClosed){
                    //Move all dates to next day 00:00.
                    currentDate = currentDate.plusDays(1);
                    currentDateTime = currentDate.atStartOfDay();
                    currentTime = currentDateTime.toLocalTime();
                    currentDayOfWeek =currentDate.getDayOfWeek();
                }

                //Open
                else{
                    //Item given before open time
                    if(currentDay.startTime.isAfter(currentTime)){
                        int daySeconds = (int) ChronoUnit.SECONDS.between(currentDay.getStartTime()
                                ,currentDay.getEndTime());
                        //Item due today
                        if(daySeconds > remainingSeconds) {
                            currentDateTime = LocalDateTime.of(currentDate, currentDay.getStartTime())
                                    .plusSeconds(remainingSeconds);
                            remainingSeconds = 0;
                        }
                        else{
                            //Subtract all day's seconds and move onto next day 00:00.
                            remainingSeconds -= daySeconds;
                            currentDate = currentDate.plusDays(1);
                            currentDateTime = currentDate.atStartOfDay();
                            currentTime = currentDateTime.toLocalTime();
                            currentDayOfWeek =currentDate.getDayOfWeek();
                        }
                    }
                    //Item given during business hours
                    else if(currentDay.getStartTime().isBefore(currentTime)){
                        int daySeconds = (int) ChronoUnit.SECONDS.between(currentTime,currentDay.getEndTime());
                        if(daySeconds < remainingSeconds) {
                            remainingSeconds -= daySeconds;
                            currentDate = currentDate.plusDays(1);
                            currentDateTime = currentDate.atStartOfDay();
                            currentTime = currentDateTime.toLocalTime();
                            currentDayOfWeek =currentDate.getDayOfWeek();
                        }
                        else {
                            currentDateTime = LocalDateTime.of(currentDate,currentTime)
                                    .plusSeconds(remainingSeconds);
                            remainingSeconds = 0;
                        }
                    }
                    //Item given after business hours
                    else if(currentDay.getEndTime().isBefore(currentTime)){
                        currentDate = currentDate.plusDays(1);
                        currentDateTime = currentDate.atStartOfDay();
                        currentTime = currentDateTime.toLocalTime();
                        currentDayOfWeek =currentDate.getDayOfWeek();
                    }
                }
            }
        }

        //Requested output format
        DateTimeFormatter formatterDTF = DateTimeFormatter.ofPattern(DATE_FORMATTER);
        String formatDateTime = currentDateTime.format(formatterDTF);
        System.out.println("=> " + formatDateTime);

        //Return Date
        return currentDateTime;
    }
}

