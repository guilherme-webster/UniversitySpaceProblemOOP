package src.Schedule;

import java.util.Objects;

/**
 * Class that represents a schedule of a lecture,
 * with the day and hour of start and end.
 */
public class LectureSchedule {
    private WeekDay day;
    private HourOfClass hourOfClass;

    /**
     * Public constructor for LectureSchedule class
     * 
     * @param day: the week day of the lecture
     * @param hourOfClass: the time the lecture occurs
     */
    public LectureSchedule(WeekDay day, HourOfClass hourOfClass){
        this.day = day;
        this.hourOfClass = hourOfClass;
    }

    public WeekDay getDay(){
        return this.day;
    }

    public HourOfClass getHourOfClass(){
        return hourOfClass;
    }

    /**
     * Method that gives the first schedule of a week. If it's a fulltime course,
     * the first schedule is Monday, eight to ten AM. If it's a nighttime course,
     * the first is Monday, seven to nine PM.
     * 
     * @param courseShift: the course that will receive the schedule
     * @return: the first schedule of the week
     */
    public static LectureSchedule firstSchedule(Shift courseShift){
        if (courseShift == Shift.FULL_TIME)
            return new LectureSchedule(WeekDay.MONDAY, HourOfClass.EigthAM_TenAM);
        else 
            return new LectureSchedule(WeekDay.MONDAY, HourOfClass.SevenPM_NinePM);
    }

    /**
     * Gives the next schedule
     * 
     * @param schedule: the schedule you want the next one
     * @return: the next schedule
     */
    public static LectureSchedule nextSchedule(LectureSchedule schedule){
        WeekDay day = schedule.getDay();
        HourOfClass hour = schedule.getHourOfClass();

        if (hour == HourOfClass.EigthAM_TenAM) 
            return new LectureSchedule(day, HourOfClass.TenAM_Midday);
        else if (hour == HourOfClass.TenAM_Midday)
            return new LectureSchedule(day, HourOfClass.TwoPM_FourPM);
        else if (hour == HourOfClass.TwoPM_FourPM)
            return new LectureSchedule(day, HourOfClass.FourPM_SixPM);
        else if (hour == HourOfClass.SevenPM_NinePM)
            return new LectureSchedule(day, HourOfClass.NinePM_ElevenPM);
        else if (hour == HourOfClass.FourPM_SixPM){
            if (day == WeekDay.MONDAY)
                return new LectureSchedule(WeekDay.TUESDAY, HourOfClass.EigthAM_TenAM);
            else if (day == WeekDay.TUESDAY)
                return new LectureSchedule(WeekDay.WEDNESDAY, HourOfClass.EigthAM_TenAM);
            else if (day == WeekDay.WEDNESDAY)
                return new LectureSchedule(WeekDay.THURSDAY, HourOfClass.EigthAM_TenAM);
            else if (day == WeekDay.THURSDAY)
                return new LectureSchedule(WeekDay.FRIDAY, HourOfClass.EigthAM_TenAM);               
        }
        else if (hour == HourOfClass.NinePM_ElevenPM){
            if (day == WeekDay.MONDAY)
                return new LectureSchedule(WeekDay.TUESDAY, HourOfClass.SevenPM_NinePM);
            else if (day == WeekDay.TUESDAY)
                return new LectureSchedule(WeekDay.WEDNESDAY, HourOfClass.SevenPM_NinePM);
            else if (day == WeekDay.WEDNESDAY)
                return new LectureSchedule(WeekDay.THURSDAY, HourOfClass.SevenPM_NinePM);
            else if (day == WeekDay.THURSDAY)
                return new LectureSchedule(WeekDay.FRIDAY, HourOfClass.SevenPM_NinePM);
        }
        throw new Error("Schedule does not support so many classes");
    }

    /**
     * Implementing a particular equals() method so the schedules are properly compared
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LectureSchedule that = (LectureSchedule) o;
        return this.hourOfClass == that.hourOfClass && this.day == that.day;
    }

    public static int compare(LectureSchedule schedule1, LectureSchedule schedule2){
        WeekDay day1 = schedule1.getDay();
        WeekDay day2 = schedule2.getDay();
        HourOfClass hour1 = schedule1.getHourOfClass();
        HourOfClass hour2 = schedule2.getHourOfClass();

        if (schedule1.equals(schedule2))
            return 0;
        if (WeekDay.compare(day1, day2) > 0 ||
            WeekDay.compare(day1, day2) == 0 && HourOfClass.compare(hour1, hour2) > 0)
            return 1;
        else
            return -1; 
    }

    /**
     * Implementing a particular hashCode() method so the schedules are properly compared and to keep consistency with the equals() override
     */
    @Override
    public int hashCode() {
        return Objects.hash(day, hourOfClass);
    }
}