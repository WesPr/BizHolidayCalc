public enum DayOfWeekEnum {
    MONDAY(1), TUESDAY(2), WEDNESDAY(3), THURSDAY(4),
    FRIDAY(5), SATURDAY(6), SUNDAY(7);

    //Variables
    public final int value;

    /**
     * Constructor
     * @param value the value of each respective Enum.
     */
    DayOfWeekEnum(int value) {
        this.value = value;
    }


    /**
     * Getter
     * @return the value of each respective Enum.
     */
    public int getValue() {
        return value;
    }
}




