/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crewscheduling;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author gustavo
 */
public class Solution {
    
    private static boolean initialized = false;
    private Time[] workingTime;
    private double alpha;
    
    public Solution(double alpha){
        if(!initialized){
            initialize();
            initialized = true;
        }
        
        workingTime = new Time[Data.getDrivers().size()];
        for(int i=0; i<Data.getDrivers().size(); i++){
            workingTime[i] = new Time("00:00");
        }
        
        this.alpha = alpha;
        
        process();
    }
    
    private static void initialize(){
        Random r = new Random();
        
        Data.initialize();
        
        // Dados serão lidos de arquivo, mas segue exemplo de como adicionar dados
        
        Data.addDriver(new Driver(0, "Driver 01"));
        Data.addDriver(new Driver(1, "Driver 02"));
        Data.addDriver(new Driver(2, "Driver 03"));
        Data.addDriver(new Driver(3, "Driver 04"));
        
        Data.addBus(new Bus(0));
        Data.addBus(new Bus(1));
        Data.addBus(new Bus(2));
        Data.addBus(new Bus(3));
        
        Data.addTrip(new Trip(0));
        Data.addTrip(new Trip(1));
        Data.addTrip(new Trip(2));
        Data.addTrip(new Trip(3));
        
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
    
    private void process(){
        
        // TODO: Lógica do algoritmo, assumindo que os dados já foram importados.
        
        Random r = new Random();
        ArrayList<Trip> pendingTrips = new ArrayList<Trip>(Data.getTrips());
        ArrayList<Driver> orderedDriversByTime = new ArrayList<Driver>(Data.getDrivers());
        
        for(Driver d : Data.getDrivers()){
            workingTime[d.getId()] = getDriverWorkingTime(d);
        }
        
        // Define ônibus e motorista para a viagem
        for(int i=0; i<pendingTrips.size(); i++){
            
            int indexTrip = r.nextInt((int) alpha*pendingTrips.size());
            for(Bus b : Data.getBuses()){
                int indexDriver = r.nextInt((int) alpha*orderedDriversByTime.size());
                Decision.setValue(b.getId(), orderedDriversByTime.get(indexDriver).getId(), pendingTrips.get(indexTrip).getId(), 1);
                pendingTrips.get(indexDriver);
                
                // Avalia restrições
                
                if(valid()){
                    Driver d = orderedDriversByTime.get(indexDriver);
                    workingTime[d.getId()] = getDriverWorkingTime(d);
                    
                    orderedDriversByTime.remove(indexDriver);
                    for(int j=0; j<orderedDriversByTime.size(); j++){
                        Driver dIt = orderedDriversByTime.get(j);
                        if(Time.compare(workingTime[d.getId()], workingTime[dIt.getId()]) >= 0){
                            orderedDriversByTime.add(j, d);
                            break;
                        }
                    }
                    break;
                }
                else{
                    Decision.setValue(b.getId(), indexDriver, pendingTrips.get(indexTrip).getId(), 0);
                }
            }
        }
        
    }
    
    private static boolean valid(){
        
        // Motorista não pode estar em duas viagens simultaneamente
        for(Driver d : Data.getDrivers()){
            for(int i=0; i<Data.getTrips().size(); i++){
                Trip t1 = Data.getTrips().get(i);
                for(int j=i+1; j<Data.getTrips().size(); j++){
                    Trip t2 = Data.getTrips().get(j);
                    for(Bus b : Data.getBuses()){
                        if(Decision.getValue(b.getId(), d.getId(), t1.getId()) == 1){
                            Time t1Final = Time.add(Parameters.InitialInstant[t1.getId()], Parameters.Duration[t1.getId()]);
                            Time t2Initial = Parameters.InitialInstant[t2.getId()];
                            
                            if(Time.compare(t1Final, t2Initial) >= 0){
                                return false;
                            }
                        }
                    }
                }
            }
        }
        
        // Só pode ter um motorista por ônibus por viagem
        for(Trip t : Data.getTrips()){
            for(Bus b : Data.getBuses()){
                boolean foundDriver = false;
                for(Driver d : Data.getDrivers()){
                    if(Decision.getValue(b.getId(), d.getId(), t.getId()) == 1){
                        if(!foundDriver){
                            foundDriver = true;
                        }
                        else{
                            return false;
                        }
                    }
                }
            }
        }
        
        // Ônibus não pode estar em duas viagens simultaneamente
        for(Bus b : Data.getBuses()){
            for(int i=0; i<Data.getTrips().size(); i++){
                Trip t1 = Data.getTrips().get(i);
                for(int j=i+1; j<Data.getTrips().size(); j++){
                    Trip t2 = Data.getTrips().get(j);
                    for(Driver d : Data.getDrivers()){
                        if(Decision.getValue(b.getId(), d.getId(), t1.getId()) == 1){
                            Time t1Final = Time.add(Parameters.InitialInstant[t1.getId()], Parameters.Duration[t1.getId()]);
                            Time t2Initial = Parameters.InitialInstant[t2.getId()];
                            
                            if(Time.compare(t1Final, t2Initial) >= 0){
                                return false;
                            }
                        }
                    }
                }
            }
        }
        
        // Só pode ter um ônibus por motorista por viagem
        for(Trip t : Data.getTrips()){
            for(Driver d : Data.getDrivers()){
                boolean foundBus = false;
                for(Bus b : Data.getBuses()){
                    if(Decision.getValue(b.getId(), d.getId(), t.getId()) == 1){
                        if(!foundBus){
                            foundBus = true;
                        }
                        else{
                            return false;
                        }
                    }
                }
            }
        }
                
        return true;
    }
    
/*    public static void generate(){
        
        initialize();
        
        process();
    }
*/    
    public static Time getDriverWorkingTime(Driver d){
        
        Time time = new Time("00:00");
        
        for(Trip t : Data.getTrips()){
            for(Bus b : Data.getBuses()){
                if(Decision.getValue(b.getId(), d.getId(), t.getId()) > 0)
                    time = Time.add(time, Parameters.Duration[t.getId()]);
            }
        }
        
        return time;
    }
    
}
