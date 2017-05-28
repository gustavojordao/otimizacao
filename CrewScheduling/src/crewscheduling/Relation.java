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
public class Relation {
    
    private Bus bus;
    private Driver driver;
    private Assistant assistant;
    private Shift shift;    

    public Relation(Bus bus, Driver driver, Assistant assistant, Shift shift) {
        this.bus = bus;
        this.driver = driver;
        this.assistant = assistant;
        this.shift = shift;
    }

    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Assistant getAssistant() {
        return assistant;
    }

    public void setAssistant(Assistant assistant) {
        this.assistant = assistant;
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }
    
}
