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
public class Solution {
    
    private Solution(){
        
    }
    
    private static void initialize(){
        Data.initialize();
        
        // Dados serão lidos de arquivo, mas segue exemplo de como adicionar dados
        
        Data.addShift(new Shift(1, "MORNING"));
        Data.addShift(new Shift(2, "AFTERNOON"));
        Data.addShift(new Shift(3, "NIGHT"));
        Data.addShift(new Shift(4, "DAWN"));
        
        Data.addDriver(new Driver(1, "Driver 01"));
        Data.addDriver(new Driver(2, "Driver 02"));
        Data.addDriver(new Driver(3, "Driver 03"));
        Data.addDriver(new Driver(4, "Driver 04"));
        
        Data.addAssistant(new Assistant(1, "Assistant 01"));
        Data.addAssistant(new Assistant(2, "Assistant 02"));
        Data.addAssistant(new Assistant(3, "Assistant 03"));
        Data.addAssistant(new Assistant(4, "Assistant 04"));
        
        Data.addBus(new Bus(1));
        Data.addBus(new Bus(2));
        Data.addBus(new Bus(3));
        Data.addBus(new Bus(4));
        
    }
    
    private static void process(){
        
        // TODO: Lógica do algoritmo, assumindo que os dados já foram importados.
    }
    
    public static void generate(){
        
        initialize();
        
        process();
    }
    
}
