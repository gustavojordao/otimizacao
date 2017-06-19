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
        //Parameters.generateParameters(10, new Time("08:00")); 
        
        if(args.length < 4){
            System.out.println("Parâmetros inválidos. Use os parâmetros abaixo.");
            System.out.println("<JAR_NAME> <ITERATIONS=INT>" + " " + "<ALPHA=0..1>" + " " + "<WORKING_TIME=HH:MM>" + " " + "<GENERATE_PARAMETERS=0|1>" + " " + "<NUMBER_OF_TRIPS=INT>");
            System.out.println("Exemplo: crewsc.jar" + " " + "20" + " " + "1.0" + " " + "08:00" + " " + "0" + " " + "10");
            System.exit(0);
        }
        else{
            if(args.length == 5){
                if(args[3].equals("1")){
                    Parameters.generateParameters(Integer.parseInt(args[4]), new Time(args[2]));
                }
            }
        }
        
        System.out.println("---------------");
        
        int iterations = Integer.valueOf(args[0]);
        double alpha = Double.valueOf(args[1]);
        int i=0;
        Solution s = null;
        for(i=0; i<iterations; i++){
            Solution s_it = new Solution(alpha);
            
            System.out.println("\nNumber of Drivers: "+s_it.getNumberOfDrivers());
            System.out.println("Number of Buses: "+s_it.getNumberOfBuses());
        
            if(s == null || s_it.getNumberOfDrivers() < s.getNumberOfDrivers()){
                s = s_it;
                System.out.println("IT "+i);
            }
            else if(s_it.getNumberOfDrivers() == s.getNumberOfDrivers() && s_it.getNumberOfBuses() < s.getNumberOfBuses()){
                s = s_it;
                System.out.println("IT "+i);                
            }
        }
        
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
                    System.out.print(s.getDecision().getValue(j, i, k)+" ");
                }
                System.out.println("/");
                
            }
        }
        
        System.out.println("\nNumber of Drivers: "+s.getNumberOfDrivers());
        System.out.println("Number of Buses: "+s.getNumberOfBuses());
        
        
    }
    
}
