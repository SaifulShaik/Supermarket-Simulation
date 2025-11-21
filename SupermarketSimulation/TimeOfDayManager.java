/**
 * Utility class that manages the in-game time of day.
 * 
 * Time is stored as a number of seconds since midnight (0–86,399).
 * Other classes can call the static methods to advance time,
 * adjust the speed, and read the current time and period.
 * 
 * @author:Owen Kung
 * @version:Nov 2025
 */
public class TimeOfDayManager
{

    private static int seconds = 8 * 3600;    //Start at 08:00:00  →  8 * 3600
    private static int secondsPerTick = 25;    //1 = 1 game second per act, 30, about 1:08 a day

    /**
     * Advances the in-game clock
     * 
     * If the time reaches or passes 24:00:00 (24 * 3600 seconds),
     * it wraps around to the start of the day (00:00:00).
     * 
     * Call this once per frame (for example from {ClockDisplay.act()}).
     */
    public static void updateTime()
    {
        seconds += secondsPerTick;
        //Rest at the end of the day;
        if (seconds >= 24 * 3600) {
            seconds -= 24 * 3600;
        }
    }
    /**
     * Sets how many in-game seconds pass each frame.
     *
     * @param secPerTick number of seconds to advance per call to {updateTime()
     *                   (must be positive; values ≤ 0 are ignored)
     */
    public static void setSpeed(int secPerTick) {
        if (secPerTick > 0) {
            secondsPerTick = secPerTick;
        }
    }
    /**
     * Returns the current in-game hour (0–23).
     *
     * @return hour of day
     */
    public static int getHour() {
        return seconds / 3600;
    }   
    /**
     * Returns the current in-game minute (0–59).
     *
     * @return minute of the current hour
     */
    public static int getMinute() {
        return (seconds % 3600) / 60;
    }
    /**
     * Returns the current in-game second (0–59).
     *
     * @return second of the current minute
     */ 
    public static int getSecond() {
        return seconds % 60;
    }
    /**
     * Reset the in gametime by resetting the seconds.
     *
     * @return second of the current minute
     */ 
    public static void setSecond(int newSeconds)
    {
        seconds=newSeconds;
    }
    /**
     * Returns the current time as a {"HH:MM:SS"} string
     * in 24-hour format (e.g. "17:35:09").
     *
     * @return formatted current time string
     */
    public static String getTimeString()
    {
        int h = getHour();
        int m = getMinute();
        int s = getSecond();

        String hh = (h < 10 ? "0" : "") + h;
        String mm = (m < 10 ? "0" : "") + m;
        String ss = (s < 10 ? "0" : "") + s;

        return hh + ":" + mm + ":" + ss;
    }

    /**
     * Returns a rough label for the current time of day
     *
     * @return a string describing the current period of the day
     */
    public static String getPeriod()
    {
        int h = getHour();
        if (h < 11)  return "Morning";
        if (h < 16)  return "Afternoon";
        if (h < 20)  return "Evening";
        return "Night";
    }
}


