package similarItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MinHashing {
	private List<Integer> Boolean;
	private Set<Integer> set;
	
	public List<Integer> MinHashSignature(int n, Set<Integer> AllSet, Set<Integer> s, List<Integer> a, List<Integer> b) {
		this.set = current_set(s);
		List<Integer> signature = new ArrayList<Integer>();
		int count = 0;
		for (int i = 0; i < n; i++) {
			signature.add(Integer.MAX_VALUE);
		}	
		//System.out.println(signature);
		
		for(Integer key : AllSet){
			if(set.contains(key)){
				this.Boolean.add(1);
			}else{
				this.Boolean.add(0);
			}
		}
		for (int i : this.Boolean) {
			if(i == 1) {
				for (int j = 0; j < n; j++) {
					int new_sign = Math.min(signature.get(j), ((a.get(j)*count + b.get(j)) % AllSet.size()));
					signature.set(j, new_sign);
				}
			}
			count++;
		}
		return signature;
	}	
	
	private Set<Integer> current_set(Set<Integer> cset){
		this.Boolean=new ArrayList<Integer>();
		return this.set=cset;
	}
	
}
