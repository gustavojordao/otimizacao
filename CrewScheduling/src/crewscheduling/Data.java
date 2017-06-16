/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crewscheduling;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gustavo
 */
public class Data {
    
    private static ArrayList<Bus> buses;
    private static ArrayList<Driver> drivers;
    private static ArrayList<Trip> trips;
    
    private Data(){
        
    }

    public static void initialize(){
        buses = new ArrayList<Bus>();
        drivers = new ArrayList<Driver>();
        trips = new ArrayList<Trip>();
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

    public static ArrayList<Trip> getTrips() {
        return trips;
    }

    public static void setTrips(ArrayList<Trip> trips) {
        Data.trips = trips;
    }
    
    public static void addBus(Bus bus){
        Data.buses.add(bus);
    }
    
    public static void addDriver(Driver driver){
        Data.drivers.add(driver);
    }
    
    public static void addTrip(Trip trip){
        Data.trips.add(trip);
    }
    
}
