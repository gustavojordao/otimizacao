/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crewscheduling;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 *
 * @author gustavo
 */
public class Parameters {
    
    // Trip parameters
    
    public static Time[] InitialInstant;
    public static Time[] Duration;
    
    // Driver parameters
    
    public static Time WorkingTime;
    
    
    public static void generateParameters(int trips, Time workingTime){
        
        PrintStream ps = null;
        try {
            Random r = new Random();
            ps = new PrintStream(new File("parameters.dat"));
            
            ps.printf(workingTime.toString());
            
            ps.printf("\n");
            for(int i=0; i<trips; i++){
                Time t = new Time(r.nextInt(24), r.nextInt(60));
                ps.printf("%s", t.toString());
                if(i<trips-1)
                    ps.printf(" ");
            }
            
            ps.printf("\n");
            for(int i=0; i<trips; i++){
                Time t = new Time(r.nextInt(workingTime.getHour()), r.nextInt(60));
                ps.printf("%s", t.toString());
                if(i<trips-1)
                    ps.printf(" ");
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ps.close();
        }
        
    }
    
    public static void readParameters(String filename){
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filename));
            
            Iterator it = br.lines().iterator();
            ArrayList<String> str = new ArrayList<String>();
            while(it.hasNext()){
                str.add((String) it.next());
            }
            String[] lines = str.toArray(new String[str.size()]);
            
            String workingTime = lines[0];
            String initial = lines[1];
            String duration = lines[2];
            
            Parameters.WorkingTime = new Time(workingTime);
            
            String[] initial_parts = initial.split(" ");
            Parameters.InitialInstant = new Time[initial_parts.length];
            for(int i=0; i<initial_parts.length; i++){
                Parameters.InitialInstant[i] = new Time(initial_parts[i]);
            }
            
            String[] duration_parts = duration.split(" ");
            Parameters.Duration = new Time[duration_parts.length];
            for(int i=0; i<duration_parts.length; i++){
                Parameters.Duration[i] = new Time(duration_parts[i]);
            }
            
            if(initial_parts.length != duration_parts.length){
                System.err.println("Wrong file format.");
                System.exit(1);
            }
            
            for(int i=0; i<initial_parts.length; i++){
                Data.addBus(new Bus(i));
                Data.addDriver(new Driver(i, String.format("Driver %02d", i)));
                Data.addTrip(new Trip(i));
            }
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Parameters.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(Parameters.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
