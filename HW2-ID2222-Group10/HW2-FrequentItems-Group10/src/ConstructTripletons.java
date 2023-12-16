import java.util.*;

public class ConstructTripletons {
    private Map<Set<Integer>, Integer> allTripletons;
    private Map<Set<Integer>, Integer> thirdFreItemMap;

    //constructor to construct tripleton
    public ConstructTripletons(double support, Map<Set<Integer>, Integer> doubletonMap, List<List<Integer>> basketSet) {
        allTripletons = new HashMap<>(); //create new HashMap  
        thirdFreItemMap = new HashMap<>();
        Set<Set<Integer>> doubletonKeys = doubletonMap.keySet();//define the keys of doubletonMap and store in a set
        //System.out.println(doubletonKeys);
        for (Set<Integer> set1 : doubletonKeys) {//Iterates over each set1 in the doubletonKeys set
            for (Set<Integer> set2 : doubletonKeys) {//Iterates over each set2 in the doubletonKeys set
            	if (!set1.equals(set2)) {//check both set are equal or not
                    Set<Integer> tripleton = new HashSet<>(set1);//create hashset and store set1 there
                    tripleton.addAll(set2);//add set2 on hashset
                    if (tripleton.size() == 3) {//check if hashset size is 3 or not
                        allTripletons.put(tripleton, 0);//if size is 3 then store the set on hashmap
                    }
                }
            }
        }
        
    	int val = (int) Math.round(support * basketSet.size());
    	for (Map.Entry<Set<Integer>, Integer> entry : allTripletons.entrySet()) {//iterate on each set of allTripletons    		
			int frequency = countFrequency(entry.getKey(), basketSet);//get the occurances of current set elents on basketSet
			//System.out.println(entry + " - " + allTripletons.entrySet()+" - "+entry.getKey()+" - "+frequency);
			if (frequency > val) {//check with main formula
				thirdFreItemMap.put(entry.getKey(), frequency);//store in hashmap
			}
		}
    }
    
    public Map<Set<Integer>, Integer> getThirdFreItemMap() {
		return thirdFreItemMap;
	}	
    
    private int countFrequency(Set<Integer> item, List<List<Integer>> basketList) {
        int frequency = 0;
        for (List<Integer> basket : basketList) {//iterate basket on basketSet
            if (basket.containsAll(item)) {//if all value of set found on current basket then increase by 1
                frequency++;
            }
        }
        return frequency;
    }
}