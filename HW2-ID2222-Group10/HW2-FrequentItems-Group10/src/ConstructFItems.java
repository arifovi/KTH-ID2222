import java.util.*;

class ConstructFItems {
    private int index;
    private Map<Integer, Integer> freItemMap;
    private int[] freItems;
    private Map<Set<Integer>, Integer> secfreItemMap;

    //constructor for create singleton
    public ConstructFItems(int[] singleton, int s, Map<Integer, Integer> fwdMap) {
    	index = 1;
        freItemMap = new HashMap<>();//create new hashmap
        freItems = new int[singleton.length];
        for (int i = 0; i < singleton.length; i++) {
            if (singleton[i] >= s) {//condition to check current item is bigger than s or not
                freItems[i] = index++;
                //System.out.print(freItems[i]+"-"+fwdMap.get(i)+"-"+singleton[i]+", ");
                freItemMap.put(fwdMap.get(i), singleton[i]);//store hashmap where key using forward map(i) and value is current item
            }
        }
    }

    public ConstructFItems(int[] doubleton, double s, int size, Map<Integer, Set<Integer>> setMap) {
    	int val = (int) Math.round(s * size);
    	index = 1;
        secfreItemMap = new HashMap<>();//create new hashmap
        freItems = new int[doubleton.length];
        for (int i = 0; i < doubleton.length; i++) {
            if (doubleton[i] >= val) {//condition to check current item is bigger than val or not
                freItems[i] = index++;
                secfreItemMap.put(setMap.get(i), doubleton[i]);//store hashmap where key using forward map(i) and value is current item
                //System.out.print(i+" - "+setMap.get(i)+" - "+ doubleton[i]+", ");
            }
        }
    }

    public int[] getFreItemTable() {
        return freItems;
    }

    public Map<Integer, Integer> getFreItemMap() {
        return freItemMap;
    }

    public Map<Set<Integer>, Integer> getSecFreItemMap() {
        return secfreItemMap;
    }
}
