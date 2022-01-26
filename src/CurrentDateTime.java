import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class CurrentDateTime {

    private LocalDateTime currentDateTime;
    private LocalDate currentDate;
    private LocalTime currentTime;
    private DayOfWeek currentDayOfWeek;

    public CurrentDateTime(String dropOffTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        this.currentDateTime = LocalDateTime.parse(dropOffTime, formatter);
        this.currentDate = currentDateTime.toLocalDate();
        this.currentTime = currentDateTime.toLocalTime();
        this.currentDayOfWeek = currentDateTime.getDayOfWeek();
    }

    public void moveToNextDay(){
        this.currentDate = currentDate.plusDays(1);
        this.currentDateTime = currentDate.atStartOfDay();
        this.currentTime = currentDateTime.toLocalTime();
        this.currentDayOfWeek =currentDate.getDayOfWeek();
    }

    public LocalDateTime getCurrentDateTime() {
        return currentDateTime;
    }

    public void setCurrentDateTime(LocalDateTime currentDateTime) {
        this.currentDateTime = currentDateTime;
    }

    public LocalDate getCurrentDate() {
        return currentDate;
    }


    public LocalTime getCurrentTime() {
        return currentTime;
    }

    public DayOfWeek getCurrentDayOfWeek() {
        return currentDayOfWeek;
    }

}
