/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grasp;

import crewscheduling.Parameters;
import crewscheduling.Solution;
import crewscheduling.Time;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author 123437
 */
public class GRASP {

    // http://conteudo.icmc.usp.br/pessoas/andre/research/genetic/
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Parameters.generateParameters(15, new Time("08:00")); 
        
        Random r = new Random();
        ArrayList<Solution> ranked = new ArrayList<Solution>();
        
        for(int i=0; i<10; i++){
            Solution s = new Solution(r.nextDouble());
            System.out.println(s.getNumberOfDrivers());
//            ranked = rank(ranked, s, 0, ranked.size()-1);
        }
        System.out.println("");
        for(Solution s : ranked){
            System.out.println(s.getNumberOfDrivers());
        }
        
        System.out.println("SIZE: "+ranked.size());
    }
    
    private Solution[] population;
    private Solution[] rankedPopulation;
    
    private Random r;
    
    public GRASP(){
        r = new Random();
    }
    
    public void run(){
        
    }
    
    private static ArrayList<Solution> rank(ArrayList<Solution> ranked, Solution s, int min, int max){
        
        if(ranked.isEmpty()){
            ranked.add(s);
            return ranked;
        }
        
        Solution aux = ranked.get(min);
        
        if(min == max){
            if(grade(s) > grade(ranked.get(min))){
                ranked.add(min, s);
            }
            else if(grade(s) < grade(ranked.get(min))){
                ranked.add(min+1, s);
            }
            else{
                ranked.add(min, s);
            }
            return ranked;
        }
        
        int offset = min + (max-min)/2;
        
        if(grade(s) > grade(ranked.get(offset))){
            return rank(ranked, s, min, offset-1);
        }
        else if(grade(s) < grade(ranked.get(offset))){
            return rank(ranked, s, offset+1, max);
        }
        else{
            ranked.add(offset, s);
            return ranked;
        }
    }
    
    private static double grade(Solution s){
        return 1/s.getNumberOfDrivers();
    }
    
    private static void localSearch(){
        
    }
    
}
