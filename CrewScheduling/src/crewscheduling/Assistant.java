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
public class Assistant {
    
    private int id;
    private String name;

    public Assistant(int id, String name) {
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
        if(obj != null && obj instanceof Assistant){
            Assistant assistant = (Assistant) obj;
            return assistant.getId() == this.getId() && assistant.getName().equals(this.getName());
        }
        else{
            return false;
        }
    }
    
    public static boolean equals(Assistant a1, Assistant a2){
        return a1.equals(a2);
    }
    
}
