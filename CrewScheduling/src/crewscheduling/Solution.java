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
    private Decision decision;
    
    public Solution(double alpha){
        if(!initialized){
            initialize();
            initialized = true;
        }
        
        // Inicializa variável de decisão
        
        decision = new Decision();
        
        workingTime = new Time[Data.getDrivers().size()];
        for(int i=0; i<Data.getDrivers().size(); i++){
            workingTime[i] = new Time("00:00");
        }
        
        this.alpha = alpha;
        
        build();
        
        localSearch();
        //process();
    }

    public Decision getDecision() {
        return decision;
    }

    public void setDecision(Decision decision) {
        this.decision = decision;
    }
    
    private static void initialize(){
        Random r = new Random();
        
        Data.initialize();
        
        // Dados serão lidos de arquivo, mas segue exemplo de como adicionar dados
        
        Parameters.readParameters();        
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
            
            int window = (int) alpha*pendingTrips.size();
            
            window = window == 0 ? 1 : window;
            
            int indexTrip = r.nextInt(window);
            boolean variableSet = false;
            for(Bus b : Data.getBuses()){
                
                for(int i=0; i<orderedDriversByTime.size(); i++){
                    Driver d = orderedDriversByTime.get(i);
                    //int indexDriver = r.nextInt((int) alpha*orderedDriversByTime.size());
                    decision.setValue(b.getId(), d.getId(), pendingTrips.get(indexTrip).getId(), 1);
                    
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
                        //System.out.println("Erro "+status);
                        decision.setValue(b.getId(), d.getId(), pendingTrips.get(indexTrip).getId(), 0);
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
    
    private void build(){
        Random r = new Random();
        ArrayList<Trip> pendingTrips = new ArrayList<Trip>(Data.getTrips());
        
        for(Driver d : Data.getDrivers()){
            workingTime[d.getId()] = new Time("00:00");
        }
        
        while(!pendingTrips.isEmpty()){
            int b_ind = r.nextInt(Data.getDrivers().size());
            int d_ind = r.nextInt(Data.getDrivers().size());
            int t_ind = r.nextInt(pendingTrips.size());
            
            int b = Data.getBuses().get(b_ind).getId();
            int d = Data.getDrivers().get(d_ind).getId();
            int t = pendingTrips.get(t_ind).getId();
            
            decision.setValue(b, d, t, 1);
            workingTime[d] = getDriverWorkingTime(Data.getDrivers().get(d_ind));
        
            if(valid() == 0){
                pendingTrips.remove(t_ind);
            }
            else{
                decision.setValue(b, d, t, 0);
                workingTime[d] = getDriverWorkingTime(Data.getDrivers().get(d_ind));
            }
        }
    }
    
    private void localSearch(){
        
        Random r = new Random();
        ArrayList<Driver> overAverage = new ArrayList<Driver>();
        ArrayList<Driver> underAverage = new ArrayList<Driver>();

        // 1 - Calcula média da ocupação dos motoristas
        int averageMinutes = 0;
        int countDrivers = 0;
        for(Driver d : Data.getDrivers()){
            Time d_time = workingTime[d.getId()];
            if(Time.compare(d_time, new Time("00:00")) != 0){
                countDrivers++;
            }
            averageMinutes += d_time.getHour()*60 + d_time.getMinute();
        }
        
        averageMinutes /= countDrivers;
        
        int minutes = averageMinutes % 60;
        int hours = averageMinutes / 60;
        
        Time average = new Time(hours, minutes);
        
        for(Driver d : Data.getDrivers()){
            if(Time.compare(workingTime[d.getId()], average) >= 0){
                overAverage.add(d);
            }
            else if(Time.compare(workingTime[d.getId()], new Time("00:00")) > 0){
                underAverage.add(d);
            }
        }

        // 2 - Tenta realocar viagens de motoristas abaixo da média
        
        for(int i=0; i<underAverage.size(); i++){
            Driver d_under = underAverage.get(i);
            for(Trip t : Data.getTrips()){
                boolean ended = false;
                for(Bus b : Data.getBuses()){
                    if(decision.getValue(b.getId(), d_under.getId(), t.getId()) == 1){
                        for(Driver d_over : overAverage){
//                            int d_ind = r.nextInt(overAverage.size());
//                            int d = overAverage.get(d_ind).getId();

                            decision.setValue(b.getId(), d_under.getId(), t.getId(), 0);
                            workingTime[d_under.getId()] = getDriverWorkingTime(d_under);
                            
                            decision.setValue(b.getId(), d_over.getId(), t.getId(), 1);
                            workingTime[d_over.getId()] = getDriverWorkingTime(d_over);

                            if(valid() == 0){
                                ended = true;
                                break;
                            }
                            else{
                                decision.setValue(b.getId(), d_under.getId(), t.getId(), 1);
                                workingTime[d_under.getId()] = getDriverWorkingTime(d_under);

                                decision.setValue(b.getId(), d_over.getId(), t.getId(), 0);
                                workingTime[d_over.getId()] = getDriverWorkingTime(d_over);
                            }
                        }
                        
                        for(int j=0; j<underAverage.size(); j++){
//                            int d_ind = r.nextInt(overAverage.size());
//                            int d = overAverage.get(d_ind).getId();
                            Driver d_new_over = underAverage.get(j);
                            if(d_under.getId() == d_new_over.getId())
                                continue;

                            decision.setValue(b.getId(), d_under.getId(), t.getId(), 0);
                            workingTime[d_under.getId()] = getDriverWorkingTime(d_under);
                            
                            decision.setValue(b.getId(), d_new_over.getId(), t.getId(), 1);
                            workingTime[d_new_over.getId()] = getDriverWorkingTime(d_new_over);

                            if(valid() == 0){
                                if(Time.compare(workingTime[d_new_over.getId()], average) > 0){
                                    overAverage.add(d_new_over);
                                    underAverage.remove(j);
                                }
                                ended = true;
                                break;
                            }
                            else{
                                decision.setValue(b.getId(), d_under.getId(), t.getId(), 1);
                                workingTime[d_under.getId()] = getDriverWorkingTime(d_under);

                                decision.setValue(b.getId(), d_new_over.getId(), t.getId(), 0);
                                workingTime[d_new_over.getId()] = getDriverWorkingTime(d_new_over);
                            }
                        }
                        
                        if(ended){
                            break;
                        }
                    }
                }
            }
        }
    }
    
    private int valid(){
        
        // Motorista não pode estar em duas viagens simultaneamente
        for(Driver d : Data.getDrivers()){
            for(int i=0; i<Data.getTrips().size(); i++){
                Trip t1 = Data.getTrips().get(i);
                for(int j=i+1; j<Data.getTrips().size(); j++){
                    Trip t2 = Data.getTrips().get(j);
                    
                    for(Bus b1 : Data.getBuses()){
                        for(Bus b2 : Data.getBuses()){
                            if(decision.getValue(b1.getId(), d.getId(), t1.getId()) == 1 &&
                                    decision.getValue(b2.getId(), d.getId(), t2.getId()) == 1){
                                
                                if(Time.compare(Parameters.InitialInstant[t1.getId()], Parameters.InitialInstant[t2.getId()]) <= 0){
                                    Time t1Final = Time.add(Parameters.InitialInstant[t1.getId()], Parameters.Duration[t1.getId()]);
                                    Time t2Initial = Parameters.InitialInstant[t2.getId()];

                                    if(Time.compare(t1Final, t2Initial) >= 0){
                                        return 1;
                                    }
                                }
                                else{
                                    Time t2Final = Time.add(Parameters.InitialInstant[t2.getId()], Parameters.Duration[t2.getId()]);
                                    Time t1Initial = Parameters.InitialInstant[t1.getId()];

                                    if(Time.compare(t2Final, t1Initial) >= 0){
                                        return 1;
                                    }
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
                    if(decision.getValue(b.getId(), d.getId(), t.getId()) == 1){
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
                for(int j=i+1; j<Data.getTrips().size(); j++){
                    Trip t2 = Data.getTrips().get(j);
                    
                    for(Driver d1 : Data.getDrivers()){
                        for(Driver d2 : Data.getDrivers()){
                            if(decision.getValue(b.getId(), d1.getId(), t1.getId()) == 1 &&
                                    decision.getValue(b.getId(), d2.getId(), t2.getId()) == 1){
                                
                                if(Time.compare(Parameters.InitialInstant[t1.getId()], Parameters.InitialInstant[t2.getId()]) <= 0){
                                    Time t1Final = Time.add(Parameters.InitialInstant[t1.getId()], Parameters.Duration[t1.getId()]);
                                    Time t2Initial = Parameters.InitialInstant[t2.getId()];

                                    if(Time.compare(t1Final, t2Initial) >= 0){
                                        return 3;
                                    }
                                }
                                else{
                                    Time t2Final = Time.add(Parameters.InitialInstant[t2.getId()], Parameters.Duration[t2.getId()]);
                                    Time t1Initial = Parameters.InitialInstant[t1.getId()];

                                    if(Time.compare(t2Final, t1Initial) >= 0){
                                        return 3;
                                    }
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
                    if(decision.getValue(b.getId(), d.getId(), t.getId()) == 1){
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
    
    public Time getDriverWorkingTime(Driver d){
        
        Time time = new Time("00:00");
        
        for(Trip t : Data.getTrips()){
            for(Bus b : Data.getBuses()){
                if(decision.getValue(b.getId(), d.getId(), t.getId()) > 0)
                    time = Time.add(time, Parameters.Duration[t.getId()]);
            }
        }
        
        return time;
    }
    
    public int getNumberOfDrivers(){
        
        int sum = 0;
        boolean found = false;
        
        for(Driver d : Data.getDrivers()){
            found = false;
            for(Trip t : Data.getTrips()){
                for(Bus b : Data.getBuses()){
                    if(decision.getValue(b.getId(), d.getId(), t.getId()) == 1){
                        found = true;
                        break;
                    }
                }
                if(found){
                    break;
                }
            }

            if(found){
                sum++;
            }
            
        }
        
        return sum;
    }
    
    public int getNumberOfBuses(){
        
        int sum = 0;
        boolean found = false;
        
        for(Bus b : Data.getBuses()){
            found = false;
            for(Trip t : Data.getTrips()){
                for(Driver d : Data.getDrivers()){
                    if(decision.getValue(b.getId(), d.getId(), t.getId()) == 1){
                        found = true;
                        break;
                    }
                }
                if(found){
                    break;
                }
            }

            if(found){
                sum++;
            }
            
        }
        
        return sum;
    }
    
}
