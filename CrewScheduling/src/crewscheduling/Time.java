/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crewscheduling;

/**
 *
 * @author gustavo
 */
public class Time {
    
    private int hour;
    private int minute;
    
    public Time(String time){
        String[] parts = time.split(":");
        this.hour = Integer.valueOf(parts[0]);
        this.minute = Integer.valueOf(parts[1]);
    }
    
    public Time(int hour, int minute){
        this.hour = hour;
        this.minute = minute;
    }
    
    public static int compare(Time t1, Time t2){
        if(t1.hour < t2.hour){
            return -1;
        }
        else if(t1.hour > t2.hour){
            return 1;
        }
        else{
            if(t1.minute < t2.minute){
                return -1;
            }
            else if(t1.minute > t2.minute){
                return 1;
            }
            else{
                return 0;
            }
        }
    }
    
    public static Time add(Time t, Time duration){
        return new Time(t.hour+duration.hour, t.minute+duration.minute);
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    @Override
    public String toString() {
        return hour + ":" + minute;
    }
        
}
