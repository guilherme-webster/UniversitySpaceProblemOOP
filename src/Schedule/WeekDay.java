package src.Schedule;

/**
 * Enum that represents days of the week
 */
public enum WeekDay {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY;

    /**
     * Compare two days. Return a negative value if day1 comes before day2,
     * zero if the week days are equal or a positive value if day1 comes after day2
     * 
     * @param day1
     * @param day2
     * @return
     */
    public static int compare(WeekDay day1, WeekDay day2){
        int day1Value = getNumericValue(day1);
        int day2value = getNumericValue(day2);
        return day1Value - day2value;        
    }

    /**
     * Gets the corresponding numeric value of the week day.
     * 
     * @param day: the week day
     * @return: the numeric value
     */
    public static int getNumericValue(WeekDay day){
        return switch (day) {
            case MONDAY -> 1;
            case TUESDAY -> 2;
            case WEDNESDAY -> 3;
            case THURSDAY -> 4;
            case FRIDAY -> 5;
            default -> throw new Error("Indefinite week day");
        };
    }

    public static WeekDay get(int index) {
        return values()[index];
    }
}
