/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crewscheduling;

import java.util.ArrayList;

/**
 *
 * @author gustavo
 */
public class Data {
    
    private static ArrayList<Bus> buses;
    private static ArrayList<Driver> drivers;
    private static ArrayList<Assistant> assistants;
    private static ArrayList<Shift> shifts;
    
    private Data(){
        
    }

    public static void initialize(){
        buses = new ArrayList<Bus>();
        drivers = new ArrayList<Driver>();
        assistants = new ArrayList<Assistant>();
        shifts = new ArrayList<Shift>();
    }
    
    public static ArrayList<Bus> getBuses() {
        return buses;
    }

    public static void setBuses(ArrayList<Bus> buses) {
        Data.buses = buses;
    }

    public static ArrayList<Driver> getDrivers() {
        return drivers;
    }

    public static void setDrivers(ArrayList<Driver> drivers) {
        Data.drivers = drivers;
    }

    public static ArrayList<Assistant> getAssistants() {
        return assistants;
    }

    public static void setAssistants(ArrayList<Assistant> assistants) {
        Data.assistants = assistants;
    }

    public static ArrayList<Shift> getShifts() {
        return shifts;
    }

    public static void setShifts(ArrayList<Shift> shifts) {
        Data.shifts = shifts;
    }
    
    public static void addBus(Bus bus){
        Data.buses.add(bus);
    }
    
    public static void addDriver(Driver driver){
        Data.drivers.add(driver);
    }
        
    public static void addAssistant(Assistant assistant){
        Data.assistants.add(assistant);
    }
            
    public static void addShift(Shift shift){
        Data.shifts.add(shift);
    }
    
}
