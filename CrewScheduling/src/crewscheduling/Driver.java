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
public class Driver {
    
    private int id;
    private String name;

    public Driver(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj != null && obj instanceof Driver){
            Driver driver = (Driver) obj;
            return driver.getId() == this.getId() && driver.getName().equals(this.getName());
        }
        else{
            return false;
        }
    }
    
    public static boolean equals(Driver d1, Driver d2){
        return d1.equals(d2);
    }
}
