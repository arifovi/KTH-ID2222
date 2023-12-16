import java.util.*;

public class DoubleTonMapper {
	public Map<Integer, Set<Integer>> getMap(int[] secondCount, int[] freItems, int n, Map<Integer, Integer> fwdMap) {
        Map<Integer, Set<Integer>> secFreItemMap = new HashMap<>();//create a hashmap

        for (int k = 0; k < secondCount.length; k++) {
            int i = n - 2;
            int row = 1;
            while (i < k) {//loop for generate row and i value
                row++;
                i += n - row;
            }
            //System.out.print(i+", ");
            int newi = getArrayIndex(freItems, row);//get the index number from frequent table where value == row
            int newj = getArrayIndex(freItems, n - i + k);//get the index number from frequent table where value == (n-i+k) 
            //System.out.print(newi+" - "+newj+", ");
            Set<Integer> curKey = new HashSet<>();
            curKey.add(fwdMap.get(newi));//item number of index(newi) on forwardmap
            curKey.add(fwdMap.get(newj));//item number of index(newj) on forwardmap
            //System.out.print(fwdMap.get(newi)+"-"+fwdMap.get(newj)+", ");
            secFreItemMap.put(k, curKey);//k is key and curKey is value which store on HashMap
        }
        return secFreItemMap;//return the hashmap
    }

    //method to check the items of frequent Item table
	private int getArrayIndex(int[] arr, int value) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == value) {
                return i;
            }
        }
        return -1;
    }
}
