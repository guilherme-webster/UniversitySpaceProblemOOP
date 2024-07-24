package src.Schedule;

/**
 * Enum that represents possible hours of class
 */
public enum HourOfClass {
    EigthAM_TenAM,
    TenAM_Midday,
    TwoPM_FourPM,
    FourPM_SixPM,
    SevenPM_NinePM,
    NinePM_ElevenPM;

    /**
     * Compare two hours. Return a negative value if hour1 comes before hour2,
     * zero if the hours are equal or a positive value if hour1 comes after hour2
     * 
     * @param hour1
     * @param hour2
     * @return
     */
    public static int compare(HourOfClass hour1, HourOfClass hour2){
        int hour1Value = getNumericValue(hour1);
        int hour2value = getNumericValue(hour2);
        return hour1Value - hour2value;        
    }

    public static int getNumericValue(HourOfClass hour){
        return switch (hour) {
            case EigthAM_TenAM -> 1;
            case TenAM_Midday -> 2;
            case TwoPM_FourPM -> 3;
            case FourPM_SixPM -> 4;
            case SevenPM_NinePM -> 5;
            case NinePM_ElevenPM -> 6;
            default -> throw new Error("Indefinite hour of class");
        };
    }

    public static HourOfClass get(int index) {
        return values()[index];
    }
}
