import java.util.*;
public class AprioriAlgorithmPass{
	private int numUnique;
    private Map<Integer, Integer> fwdMap = new HashMap<>();;
    private Map<Integer, Integer> bckMap = new HashMap<>();;

    public AprioriAlgorithmPass(Set<Integer> uniqueItems) {
        numUnique = uniqueItems.size();//store the size of unique items
        int index = 0;
        for (int item : uniqueItems) {//iterate upon length of uniqueItems
            fwdMap.put(index, item);//create forwardmap where index is key and item is value
            bckMap.put(item, index);//create backwardmap where item is key and index is value
            index++;
        }
    }

    //return forwardmap
    public Map<Integer, Integer> getFwdMap() {
        return fwdMap;
    }

    //return backwardmap
    public Map<Integer, Integer> getBckMap() {
        return bckMap;
    }
    //count total occurrence of each items on basketset
    public int[] firstCount(List<List<Integer>> basketSet) {
        int[] count = new int[numUnique];
        for (List<Integer> basket : basketSet) {
            for (int item : basket) {
                count[bckMap.get(item)]++;//count goes here on each item
                //System.out.print(bckMap.get(item)+"--"+count[bckMap.get(item)]+", ");    
            }
        }
        return count;
    }
    
    
}
