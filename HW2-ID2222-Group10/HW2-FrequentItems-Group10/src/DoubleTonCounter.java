import java.util.*;

class DoubleTonCounter {
    public int[] secondCount(List<List<Integer>> basketSet, int[] freItems, Map<Integer, Integer> bckMap, int n) {
        if (n < 2) return null;
        int[] itemPairs = new int[(n - 1) * n / 2];//set the length of new array
        for (List<Integer> basket : basketSet) {//Iterates over each basket in the basket set
            List<Integer> curFreItems = new ArrayList<>();//create list on each basket
            for (int item : basket) {//Iterates over each item in the basket
                if (freItems[bckMap.get(item)] != 0) {//check with backmap using current item is 0 or not
                	curFreItems.add(item);//if not 0 the add the item on the list
                }
            }
            //System.out.print(curFreItems+" - ");
            for (int i = 0; i < curFreItems.size(); i++) {//loop runs from 0 to until the size of list
                for (int j = i + 1; j < curFreItems.size(); j++) {//nested loop of j which start 1 step ahead of i
                    int newi = freItems[bckMap.get(curFreItems.get(i))];//get i value(x) from list, then get x value(y) from backmap, then get y value(newi) from frequent table
                    int newj = freItems[bckMap.get(curFreItems.get(j))];//get j value(x) from list, then get x value(y) from backmap, then get y value(newj) from frequent table
                    //System.out.print(newi+"-"+newj+", ");
                    int countVal = (newi - 1) * (2 * n - newi) / 2 + newj - newi - 1;//formula to count the value
                    itemPairs[countVal]++;//count goes here on each item
                  
                }
            }
        }
        return itemPairs;//return the array where each element shows the count of single item
    }
    
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
