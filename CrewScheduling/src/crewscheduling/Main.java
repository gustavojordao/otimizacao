/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crewscheduling;

import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gustavo
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Parameters.generateParameters(4, new Time("08:00"));
        
        
        System.out.println("---------------");
        
        int i=0;
        Solution s = new Solution(1.0);
        
        System.out.println("Working Time");
        System.out.println(Parameters.WorkingTime.toString());
        
        System.out.println("");
        System.out.println("Trip initial time");
        for(i=0; i<Parameters.InitialInstant.length; i++){
            System.out.println("Trip "+i+": "+Parameters.InitialInstant[i]);
        }
        
        System.out.println("");
        System.out.println("Trip duration time");
        for(i=0; i<Parameters.Duration.length; i++){
            System.out.println("Trip "+i+": "+Parameters.Duration[i]);
        }
        
        i=0;
        System.out.println("");
        System.out.println("Worked hours");
        for(Time t : s.getWorkingTimes()){
            System.out.println("Driver "+i+": "+t.toString());
            i++;
        }
        
        for(i=0; i<Data.getDrivers().size(); i++){
            System.out.println("");
            System.out.println("Driver "+i);
            for(int j=0; j<Data.getBuses().size(); j++){
                System.out.print("Bus "+j+" -> ");
                for(int k=0; k<Data.getTrips().size(); k++){
                    System.out.print(Decision.getValue(i, j, k)+" ");
                }
                System.out.println("/");
                
            }
        }
        
    }
    
}
