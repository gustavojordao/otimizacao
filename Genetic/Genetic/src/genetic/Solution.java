/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package genetic;

import java.util.Random;

/**
 *
 * @author 123437
 */
public class Solution {
    
    private int numberDrivers;
    
    public Solution(){
        Random r = new Random();
        
        numberDrivers = r.nextInt(100)+1;
    }
    
    public int getNumberDrivers(){
        return numberDrivers;
    }    
}
