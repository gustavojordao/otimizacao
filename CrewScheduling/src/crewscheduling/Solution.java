/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crewscheduling;

import java.util.Random;

/**
 *
 * @author gustavo
 */
public class Solution {
    
    private Solution(){
        
    }
    
    private static void initialize(){
        Random r = new Random();
        
        Data.initialize();
        
        // Dados serão lidos de arquivo, mas segue exemplo de como adicionar dados
        
        Data.addDriver(new Driver(1, "Driver 01"));
        Data.addDriver(new Driver(2, "Driver 02"));
        Data.addDriver(new Driver(3, "Driver 03"));
        Data.addDriver(new Driver(4, "Driver 04"));
        
        Data.addBus(new Bus(1));
        Data.addBus(new Bus(2));
        Data.addBus(new Bus(3));
        Data.addBus(new Bus(4));
        
        Data.addTrip(new Trip(1));
        Data.addTrip(new Trip(2));
        Data.addTrip(new Trip(3));
        Data.addTrip(new Trip(4));
        
        // Inicializa parâmetros
        
        Parameters.WorkingTime = new Time("08:00");
        
        Parameters.InitialInstant = new Time[Data.getTrips().size()];
        for(int i=0; i< Parameters.InitialInstant.length; i++){
            Parameters.InitialInstant[i] = new Time(r.nextInt(24), r.nextInt(60));
        }
        
        Parameters.Duration = new Time[Data.getTrips().size()];
        for(int i=0; i< Parameters.Duration.length; i++){
            Parameters.Duration[i] = new Time(r.nextInt(8), r.nextInt(60));
        }
        
        // Inicializa variável de decisão
        
        Decision.initialize();
        
    }
    
    private static void process(){
        
        // TODO: Lógica do algoritmo, assumindo que os dados já foram importados.
    }
    
    public static void generate(){
        
        initialize();
        
        process();
    }
    
}
