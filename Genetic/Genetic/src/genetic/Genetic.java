/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package genetic;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author 123437
 */
public class Genetic {

    // http://conteudo.icmc.usp.br/pessoas/andre/research/genetic/
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        ArrayList<Solution> ranked = new ArrayList<Solution>();
        
        for(int i=0; i<10; i++){
            Solution s = new Solution();
            System.out.println(s.getNumberDrivers());
            ranked = rank(ranked, s, 0, ranked.size()-1);
        }
        System.out.println("");
        for(Solution s : ranked){
            System.out.println(s.getNumberDrivers());
        }
        
        System.out.println("SIZE: "+ranked.size());
    }
    
    private int POPULATION_SIZE;
    private double MUTATION_PERCENT;
    private double CROSSING_PERCENT;
    private int NUMBER_GENERATIONS;
    
    private Solution[] population;
    private Solution[] rankedPopulation;
    
    private Random r;
    
    public Genetic(int populationSize, double mutationPercent, double crossingPercent, int numberGenerations){
        POPULATION_SIZE = populationSize;
        MUTATION_PERCENT = mutationPercent;
        CROSSING_PERCENT = crossingPercent;
        NUMBER_GENERATIONS = numberGenerations;
        
        population = new Solution[POPULATION_SIZE];
        
        r = new Random();
    }
    
    public void run(){
        
        populate();
        
        
    }
    
    private void populate(){
        
        for(int i=0; i<POPULATION_SIZE; i++){
            // TODO: Run solution
            // population[i] = new Solution();
        }
        
    }
    
    private void cross(){
        
        
        
    }
    
    private void mutate(){
        
        
    }
    
    private void select(){
        
        for(int i=0; i<POPULATION_SIZE; i++){
            double random = r.nextDouble();
            
   /*         if(random < ){
                
            }
            
            population[i] = ;*/
        }
        
    }
    
    private void evaluate(){
        
        ArrayList<Solution> ranked = new ArrayList<Solution>();
        
        for(int i=0; i<POPULATION_SIZE; i++){
            
            rank(ranked, population[i], 0, ranked.size()-1);
        }
        
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
        return 1/s.getNumberDrivers();
    }
    
}
