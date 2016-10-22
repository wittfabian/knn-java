package mapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Class to map the continious values and get the class with knn.
 * @author Steven Brandt, Fabian Witt
 */
public class Overlap {
 
    private final ArrayList<String[]> m_trainData;
    private final int m_k;
    
    /**
     * Constructor to fill the attributes.
     * @param m_trainData Training data.
     * @param m_k Number of neighbors.
     */
    public Overlap(ArrayList<String[]> m_trainData, int m_k) {
        this.m_trainData = m_trainData;
        this.m_k = m_k;
    }
    
    /**
     * This method calculates the class.
     * @param example An example from the test data.
     * @return Returns the computed class.
     */
    public String getClass(String[] example){
        ArrayList<double[]> sim_matrix = new ArrayList<>();
        ArrayList<String> class_matrix = new ArrayList<>();
        int classpos = m_trainData.get(0).length-1;
        int attanz = 0;
        
        //iterate through training examples
        Iterator iter_train = m_trainData.iterator();
        while(iter_train.hasNext()){
            String[] tmp = (String[])iter_train.next();
            double[] sim_new = new double[tmp.length-1];
            //iterate through attributes
            attanz = tmp.length-1;
            for(int i = 0; i<attanz;i++){
                if(example[i].equals(tmp[i])){
                    sim_new[i] = 1;
                }
                else{
                    sim_new[i] = 0;
                }
                sim_matrix.add(sim_new);
                class_matrix.add(tmp[classpos]);
            }
        }
        
        //iterate through sim_matrix to calculate distances
        ArrayList<Double> dist_matrix = new ArrayList<>();
        Iterator iter_sim = sim_matrix.iterator();
        while(iter_sim.hasNext()){
            double[] sim = (double[])iter_sim.next();
            double dist = 0.0;
            //iterate through sim entries and calculate distances
            for(int i = 0; i<sim.length-1;i++){
                dist = dist+(1.0/attanz*sim[i]);
            }
            dist_matrix.add(dist);
        }
        
        // get the k nearest classes
        ArrayList<String> kclasses = new ArrayList<>();
        for(int i = 0; i<m_k;i++){
            int index = getMaxValIndex(dist_matrix);
            kclasses.add(class_matrix.get(index));
            dist_matrix.remove(index);
            class_matrix.remove(index);
        }
        
        //get the count of the classes
        HashMap<String,Integer> anzclasses = new HashMap<>();
        for(int i = 0; i<kclasses.size();i++){
            String current = kclasses.get(i);
            if(!anzclasses.containsKey(current)){
                anzclasses.put(current, count(kclasses,current));
            }
        }
        
        //get the most often class
        Iterator iter_anzclasses = anzclasses.entrySet().iterator();
        double maxanz = 0;
        String maxclass = "";
        while(iter_anzclasses.hasNext()){
            Map.Entry classpairs = (Map.Entry)iter_anzclasses.next();
            if((int)classpairs.getValue() >= maxanz){
                maxanz = (int)classpairs.getValue();
                maxclass = (String)classpairs.getKey();
            }
        }
        
        return maxclass;
    }
    
    /**
     * Counts how often a value is in a list.
     * @param list The list.
     * @param value The value.
     * @return Returns the number.
     */
    private int count(ArrayList<String> list, String value){
        
        int result = 0;
        
        Iterator<String> iter = list.iterator();
        
        while(iter.hasNext()){
            String aktIter = iter.next();
            
            if(aktIter.equals(value))
                result++;
        }
        
        return result;
    }
    
    
    /**
     * Finds the maximum value in a list.
     * @param list The list with the values.
     * @return Returns the index of the maximum value.
     */
    private int getMaxValIndex(ArrayList<Double> list){
        double maxval = 0;
        int maxindex = 0;
        
        for(int i = 0; i<list.size()-1;i++){
            double tmp = list.get(i);
            if(maxval<tmp){
                maxval = tmp;
                maxindex = i;
            }
        }
        
        return maxindex;
    }
}
