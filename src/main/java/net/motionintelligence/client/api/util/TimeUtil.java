/**
 * 
 */
package net.motionintelligence.client.api.util;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author gerb
 *
 */
public class TimeUtil {
	
	/**
	 * Parses time values such as 09:16:45 from gtfs to an integer values 
	 * represented in seconds. For example: 9*3600 + 16*60 + 45
	 * 
	 * @param time the time to parse
	 * @return number of seconds for given time
	 */
	public static int timeToInt(final String time){
		
		String[] 	times 	= time.split(":");
		
		int hours 	= Integer.parseInt((String) times[0]); 
		int minutes	= Integer.parseInt((String) times[1]); 
		int seconds	= Integer.parseInt((String) times[2]); 
		
		return ( seconds + minutes * 60 + hours * 3600);
	}
	
	/**
	 * 
	 * @return
	 */
	public static int getCurrentDate(){
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		int year 	= calendar.get(Calendar.YEAR);
		int month 	= calendar.get(Calendar.MONTH) + 1; // Note: zero based!
		int day 	= calendar.get(Calendar.DAY_OF_MONTH);
		
		return Integer.valueOf(String.valueOf(year + "" + (month < 10 ? "0" + month : month) + "" + (day < 10 ? "0" + day : day)));
	}
	
	/**
	 * 
	 * @return
	 */
	public static int getCurrentTime(){
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		int hours 	= calendar.get(Calendar.HOUR_OF_DAY);
		int minutes = calendar.get(Calendar.MINUTE);
		int seconds = calendar.get(Calendar.SECOND);
		
		return hours * 3600 + minutes * 60 + seconds;
	}
	
	public static void main(String[] args) {
		
		System.out.println(getCurrentDate());
		System.out.println(getCurrentTime());
	}
	
	/**
     * Convert a millisecond duration to a string format
     * 
     * @param millis A duration to convert to a string form
     * @return A string of the form "X Days Y Hours Z Minutes A Seconds".
     */
	public static String getDurationBreakdown(long millis)
    {
        if(millis < 0)
        {
            throw new IllegalArgumentException("Duration must be greater than zero!");
        }

        long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        StringBuilder sb = new StringBuilder(64);
        sb.append(days);
        sb.append(" Days ");
        sb.append(hours);
        sb.append(" Hours ");
        sb.append(minutes);
        sb.append(" Minutes ");
        sb.append(seconds);
        sb.append(" Seconds");

        return(sb.toString());
    }

	/**
	 * @return today at noon 12:00:00
	 */
	public static Date getToday() {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.HOUR_OF_DAY, 12);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		return cal.getTime();
	}

	/**
	 * @return tomorrow at noon 12:00:00
	 */
	public static Date getTomorrow() {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(getToday());
		cal.add(Calendar.DAY_OF_YEAR, 1);
		
		return cal.getTime();
	}
	
	/**
	 * @return
	 */
	public static Date getYesterday() {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DAY_OF_MONTH, -1);
		
		return cal.getTime();
	}
}
