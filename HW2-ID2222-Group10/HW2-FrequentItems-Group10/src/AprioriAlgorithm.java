import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
public class AprioriAlgorithm {
	
	private static Set<Integer> uniqueItems = new TreeSet<>();

	public static void main(String[] args) throws FileNotFoundException {
		double support = 0.01;
        String dataPath = "";
//        List<Long> timeCheck = new ArrayList<Long>();
//        long startTm = System.currentTimeMillis();
        // data read-in
        List<List<Integer>> basketSet = read(dataPath);
//        long endTm = System.currentTimeMillis();
//		long crntSetTime = endTm-startTm;
//		timeCheck.add(crntSetTime);
		
        // printout
        System.out.println(uniqueItems);
        System.out.println("Number of unique items: " + uniqueItems.size());
        System.out.println("Number of baskets: " + basketSet.size());

        // first pass sending unique items
        AprioriAlgorithmPass aprioriAlgorithmPass = new AprioriAlgorithmPass(uniqueItems);

        // printout
        System.out.println("Forward Mapping:");
        System.out.println(aprioriAlgorithmPass.getFwdMap());//Mapping index -> hash value of items
        System.out.println("Backward Mapping:");
        System.out.println(aprioriAlgorithmPass.getBckMap());//Mapping hash value -> index of items

        //count total number of each items happening on basketset
        int[] countBasketItems = aprioriAlgorithmPass.firstCount(basketSet); 

        System.out.println("Each Item iterate on basketset:");
        System.out.println(Arrays.toString(countBasketItems));
        System.out.println();
        
        //Singleton handling process
        ConstructFItems betweenPasses = new ConstructFItems(countBasketItems,
                (int) Math.round(support * basketSet.size()), aprioriAlgorithmPass.getFwdMap());//initialize singleton
        int[] freItems = betweenPasses.getFreItemTable();//get the frequent table
        Map<Integer, Integer> freItemMap = betweenPasses.getFreItemMap();//get frequent singleton hashmap
        int stonSize = freItemMap.size();//size of frequent singleton
        
        //print out
        System.out.println("------ Frequent SingleTon ------");
        System.out.println("Frequent singletons (size: " + freItemMap.size() + "):");
        System.out.println(freItemMap);
        System.out.println("Frequent singletons table:");
        System.out.println(Arrays.toString(freItems));
        System.out.println();

        DoubleTonCounter secondPass = new DoubleTonCounter();//create object of secon pass
        
        //An array where each element shows the count of single item like countBasketItems
        int[] secondCount = secondPass.secondCount(basketSet, freItems, aprioriAlgorithmPass.getBckMap(), stonSize);
        //System.out.println(Arrays.toString(secondCount));
        //hashmap using secondCount, frequent table and forward map
        Map<Integer, Set<Integer>> setMap = secondPass.getMap(
                secondCount, freItems, stonSize, aprioriAlgorithmPass.getFwdMap());
        //System.out.println(setMap);
        //initialize doubleton
        ConstructFItems betweenPasses1 = new ConstructFItems(secondCount,
                support, basketSet.size(), setMap);
        Map<Set<Integer>, Integer> secFreItemMap = betweenPasses1.getSecFreItemMap();//get frequent doubleton hashmap

        //print out
        System.out.println("------ Frequent DoubleTon ------");
        System.out.println("Frequent doubletons (size: " + secFreItemMap.size() + "):");
        System.out.println(secFreItemMap);
        System.out.println();
        
        //generate tripletons by sending doubleton frequent map on new class
        ConstructTripletons constructTripletons = new ConstructTripletons(support, secFreItemMap, basketSet);

        //print out
        System.out.println("------ Frequent TripleTon  ------");
        System.out.println("Frequent tripletons (size: " + constructTripletons.getThirdFreItemMap().size() + "):");
        System.out.println(constructTripletons.getThirdFreItemMap());
        System.out.println("------ END ------\n");
    }
	
	public static List<List<Integer>> read(String dataPath) {
    	List<List<Integer>> baskets = new ArrayList<>();

        try (Scanner scanner = new Scanner(new FileInputStream(dataPath))) {
            scanner.useDelimiter("\\n");
            while (scanner.hasNextLine()) {
                ArrayList<Integer> basket = new ArrayList<>();
                Scanner lineInt = new Scanner(scanner.nextLine());
                lineInt.useDelimiter("\\s");

                while (lineInt.hasNextInt()) {
                    int curInt = lineInt.nextInt();
                    basket.add(curInt);
                    uniqueItems.add(curInt);
                }
                baskets.add(basket);
                lineInt.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return baskets;
    }

}
