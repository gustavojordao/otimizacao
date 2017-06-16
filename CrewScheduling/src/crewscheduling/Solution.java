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
        
        Parameters.readParameters("parameters.dat");
        
/*        Data.addDriver(new Driver(0, "Driver 00"));
        Data.addDriver(new Driver(1, "Driver 01"));
        Data.addDriver(new Driver(2, "Driver 02"));
        Data.addDriver(new Driver(3, "Driver 03"));
        
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

*/

        // Inicializa variável de decisão
        
        Decision.initialize();
        
    }
    
    private void process(){
        
        // Lógica do algoritmo, assumindo que os dados já foram importados.
        
        Random r = new Random();
        ArrayList<Trip> pendingTrips = new ArrayList<Trip>(Data.getTrips());
        
        // Ordena as viagens pela maior duração
        
        for(int i=0; i<pendingTrips.size(); i++){
            for(int j=i+1; j<pendingTrips.size(); j++){
                Trip t1 = pendingTrips.get(i);
                Trip t2 = pendingTrips.get(j);
                if(Time.compare(Parameters.Duration[t2.getId()], Parameters.Duration[t1.getId()]) > 0){
                    pendingTrips.remove(i);
                    pendingTrips.add(i, t2);
                    pendingTrips.remove(j);
                    pendingTrips.add(j, t1);
                }
            }
        }
                
        ArrayList<Driver> orderedDriversByTime = new ArrayList<Driver>(Data.getDrivers());
        
        for(Driver d : Data.getDrivers()){
            //workingTime[d.getId()] = getDriverWorkingTime(d);
            workingTime[d.getId()] = new Time("00:00");
        }
        
        // Define ônibus e motorista para a viagem
        while(!pendingTrips.isEmpty()){
            
            int indexTrip = r.nextInt((int) alpha*pendingTrips.size());
            boolean variableSet = false;
            for(Bus b : Data.getBuses()){
                
                for(int i=0; i<orderedDriversByTime.size(); i++){
                    Driver d = orderedDriversByTime.get(i);
                    //int indexDriver = r.nextInt((int) alpha*orderedDriversByTime.size());
                    Decision.setValue(b.getId(), d.getId(), pendingTrips.get(indexTrip).getId(), 1);
                    
                    // Avalia restrições

                    int status = valid();

                    if(status == 0){
                        workingTime[d.getId()] = getDriverWorkingTime(d);

                        orderedDriversByTime.remove(i);
                        for(int j=0; j<orderedDriversByTime.size(); j++){
                            Driver dIt = orderedDriversByTime.get(j);
                            if(Time.compare(workingTime[d.getId()], workingTime[dIt.getId()]) >= 0){
                                orderedDriversByTime.add(j, d);
                                break;
                            }
                        }
                        variableSet = true;
                        break;
                    }
                    else{
                        System.out.println("Erro "+status);
                        Decision.setValue(b.getId(), d.getId(), pendingTrips.get(indexTrip).getId(), 0);
                        workingTime[d.getId()] = getDriverWorkingTime(d);
                    }
                }
                
                if(variableSet){
                    pendingTrips.remove(indexTrip);
                    break;
                }
            }
        }
        
    }
    
    private static int valid(){
        
        // Motorista não pode estar em duas viagens simultaneamente
        for(Driver d : Data.getDrivers()){
            for(int i=0; i<Data.getTrips().size(); i++){
                Trip t1 = Data.getTrips().get(i);
                for(int j=0; j<Data.getTrips().size(); j++){
                    Trip t2 = Data.getTrips().get(j);
                    if(t1.getId() == t2.getId())
                        continue;
                    
                    for(Bus b1 : Data.getBuses()){
                        for(Bus b2 : Data.getBuses()){
                            if(Decision.getValue(b1.getId(), d.getId(), t1.getId()) == 1 &&
                                    Decision.getValue(b2.getId(), d.getId(), t2.getId()) == 1){
                                Time t1Final = Time.add(Parameters.InitialInstant[t1.getId()], Parameters.Duration[t1.getId()]);
                                Time t2Initial = Parameters.InitialInstant[t2.getId()];

                                if(Time.compare(t1Final, t2Initial) >= 0){
                                    return 1;
                                }
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
                            return 2;
                        }
                    }
                }
            }
        }
                
        // Ônibus não pode estar em duas viagens simultaneamente
        for(Bus b : Data.getBuses()){
            for(int i=0; i<Data.getTrips().size(); i++){
                Trip t1 = Data.getTrips().get(i);
                for(int j=0; j<Data.getTrips().size(); j++){
                    Trip t2 = Data.getTrips().get(j);
                    if(t1.getId() == t2.getId())
                        continue;
                    
                    for(Driver d1 : Data.getDrivers()){
                        for(Driver d2 : Data.getDrivers()){
                            if(Decision.getValue(b.getId(), d1.getId(), t1.getId()) == 1 &&
                                    Decision.getValue(b.getId(), d2.getId(), t2.getId()) == 1){
                                Time t1Final = Time.add(Parameters.InitialInstant[t1.getId()], Parameters.Duration[t1.getId()]);
                                Time t2Initial = Parameters.InitialInstant[t2.getId()];

                                if(Time.compare(t1Final, t2Initial) >= 0){
                                    return 3;
                                }
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
                            return 4;
                        }
                    }
                }
            }
        }
        
        // Motorista não pode trabalhar mais que o limite de horas estabelecido
        for(Driver d : Data.getDrivers()){
            Time worked = getDriverWorkingTime(d);
            if(Time.compare(worked, Parameters.WorkingTime) >= 0){
                return 5;
            }
        }
                
        return 0;
    }
    
    public Time[] getWorkingTimes(){
        return workingTime;
    }
    
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
