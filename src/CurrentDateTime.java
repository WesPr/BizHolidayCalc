import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class CurrentDateTime {

    //Variables
    private LocalDateTime currentDateTime;
    private LocalDate currentDate;
    private LocalTime currentTime;
    private DayOfWeek currentDayOfWeek;

    /**
     * Constructor
     * @param dropOffTime value to determine time item is dropped off.
     */
    public CurrentDateTime(String dropOffTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        this.currentDateTime = LocalDateTime.parse(dropOffTime, formatter);
        this.currentDate = currentDateTime.toLocalDate();
        this.currentTime = currentDateTime.toLocalTime();
        this.currentDayOfWeek = currentDateTime.getDayOfWeek();
    }

    /**
     * Method
     * Move all dates to next day and set time to 00:00
     */
    public void moveToNextDay(){
        this.currentDate = currentDate.plusDays(1);
        this.currentDateTime = currentDate.atStartOfDay();
        this.currentTime = currentDateTime.toLocalTime();
        this.currentDayOfWeek =currentDate.getDayOfWeek();
    }

    /**
     * Getter
     * @return the current date and time.
     */
    public LocalDateTime getCurrentDateTime() {
        return currentDateTime;
    }

    /**
     * Setter
     * Set the current date and time.
     */
    public void setCurrentDateTime(LocalDateTime currentDateTime) {
        this.currentDateTime = currentDateTime;
    }

    /**
     * Getter
     * @return the current date.
     */
    public LocalDate getCurrentDate() {
        return currentDate;
    }

    /**
     * Getter
     * @return the current time.
     */
    public LocalTime getCurrentTime() {
        return currentTime;
    }

    /**
     * Getter
     * @return current day of week.
     */
    public DayOfWeek getCurrentDayOfWeek() {
        return currentDayOfWeek;
    }

}
