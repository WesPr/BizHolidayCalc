public class Main {
    public static void main(String[] args) {
        BusinessHourCalculator businessHourCalculator = new BusinessHourCalculator("09:00", "15:00");
        businessHourCalculator.setOpeningHours(DayOfWeekEnum.FRIDAY, "10:00", "17:00");
        businessHourCalculator.setOpeningHours("2010-12-24", "08:00", "13:00");
        businessHourCalculator.setClosed(DayOfWeekEnum.SUNDAY, DayOfWeekEnum.WEDNESDAY);
        businessHourCalculator.setClosed("2010-12-25");

        businessHourCalculator.calculateDeadline(2*60*60, "2010-06-07 09:10");
        businessHourCalculator.calculateDeadline(15*60, "2010-06-08 14:48");
        businessHourCalculator.calculateDeadline(7*60*60, "2010-12-24 06:45");
    }
}
