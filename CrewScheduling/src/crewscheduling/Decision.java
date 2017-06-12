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
public class Decision {
    
    private static int[][][] x;
    
    private Decision(){
        
    }
    
    public static void initialize(){
        int numBuses = Data.getBuses().size();
        int numDrivers = Data.getDrivers().size();
        int numTrips = Data.getTrips().size();
        
        x = new int[numBuses][numDrivers][numTrips];
        
        for(int b=0; b<numBuses; b++){
            for(int d=0; d<numDrivers; d++){
                for(int t=0; t<numTrips; t++){
                    x[b][d][t] = 0;
                }
            }

        }
    }
    
    public static int[][][] getX(){
        return x;
    }
    
    public static void setValue(int b, int d, int t, int value){
        x[b][d][t] = value;
    }

    public static int getValue(int b, int d, int t){
        return x[b][d][t];
    }
    
}
