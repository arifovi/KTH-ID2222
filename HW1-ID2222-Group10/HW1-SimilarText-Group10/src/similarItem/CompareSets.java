package similarItem;

import java.util.HashSet;
import java.util.Set;

public class CompareSets {
	public double GetJaccardValue(Set<Integer> set1, Set<Integer> set2) {
		if (set1.isEmpty() && set2.isEmpty()) {
			return 0;
		} 		
		//create intersection of set 1 and 2
		Set<Integer> new_set1 = new HashSet<Integer>(set1);
		new_set1.retainAll(set2);
		int intersection = new_set1.size();
		
		//create union of set 1 and 2
		Set<Integer> new_set2 = new HashSet<Integer>(set1);
		new_set2.addAll(set2);
		int union = new_set2.size();
		
		//now calculate the Jaccard similarity
		return (double)intersection/union;
	}	
}
