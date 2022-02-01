import org.junit.Assert;
import org.junit.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class TestBusinessHourCalculator {

    //Test to determine response to incorrect time format input.
    @Test(expected = DateTimeParseException.class)
    public void constructorExceptionTest(){
        new BusinessHourCalculator("9:00", "15:00");
    }

    BusinessHourCalculator businessHourCalculatorTest =
            new BusinessHourCalculator("09:00", "15:00");

    //Test to see that default hours have been set correctly
    @Test
    public void defaultHoursTest(){
        LocalTime testTime = LocalTime.parse("09:00");
        Assert.assertEquals(testTime, businessHourCalculatorTest.getWEEKDAYS().get(1).getStartTime());
    }

    //Test to see that specific weekday hours are set
    @Test
    public void setOpeningHoursTest(){
        businessHourCalculatorTest.setOpeningHours(DayOfWeekEnum.FRIDAY, "10:00"
                , "17:00");
        LocalTime testTime = LocalTime.parse("10:00");
        Assert.assertEquals(testTime, businessHourCalculatorTest.getWEEKDAYS().get(5).getStartTime());
    }

    //Test to see that specific date hours are set
    @Test
    public void setOpeningHoursDateTest() {
        businessHourCalculatorTest.setOpeningHours("2010-12-24"
                , "08:00", "13:00");
        LocalTime testTime = LocalTime.parse("08:00");
        LocalTime openingTest = null;
        for (DayTimeRange day : businessHourCalculatorTest.getSpecificDates()) {
            if (day.getDate().isEqual(LocalDate.parse("2010-12-24"))) {
                openingTest = day.getStartTime();
            }
            Assert.assertEquals(testTime, openingTest);
        }
    }

    //Test to see that closed weekdays is set correctly
    @Test
    public void setClosingHoursTest(){
        businessHourCalculatorTest.setClosed(DayOfWeekEnum.SUNDAY, DayOfWeekEnum.WEDNESDAY);
        Assert.assertTrue(businessHourCalculatorTest.getWEEKDAYS().get(7).isClosed());
    }

    //Test to see that closed dates are set correctly
    @Test
    public void setClosingHoursDateTest() {
        businessHourCalculatorTest.setClosed("2010-12-25");
        boolean closed = false;
        for (DayTimeRange day : businessHourCalculatorTest.getSpecificDates()) {
            if (day.getDate().isEqual(LocalDate.parse("2010-12-25"))) {
                closed = day.isClosed();
            }
            Assert.assertTrue(closed);
        }
    }

    //Test to see if correct completion date is returned
    @Test
    public void expectedClosureDateTest(){
        businessHourCalculatorTest.setOpeningHours(DayOfWeekEnum.FRIDAY, "10:00"
                , "17:00");
        businessHourCalculatorTest.setOpeningHours("2010-12-24", "08:00"
                , "13:00");
        businessHourCalculatorTest.setClosed(DayOfWeekEnum.SUNDAY, DayOfWeekEnum.WEDNESDAY);
        businessHourCalculatorTest.setClosed("2010-12-25");
        LocalDateTime returnedDate = businessHourCalculatorTest.calculateDeadline(15*60
                , "2010-06-08 14:48");
        Assert.assertEquals("2010-06-10T09:03", returnedDate.toString());
    }
}
