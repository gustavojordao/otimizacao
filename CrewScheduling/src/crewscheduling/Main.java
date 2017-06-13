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
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Solution s = new Solution(1.0);
        
        for(int i=0; i<Data.getBuses().size(); i++){
            System.out.println("");
            for(int j=0; j<Data.getDrivers().size(); j++){
                System.out.print("/");
                for(int k=0; k<Data.getTrips().size(); k++){
                    System.out.print(Decision.getValue(i, j, k)+" ");
                }
            }
        }
        
    }
    
}
