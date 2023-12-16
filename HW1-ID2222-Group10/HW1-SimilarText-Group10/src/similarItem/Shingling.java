package similarItem;

import java.util.HashMap;

public class Shingling {
	public HashMap<Integer,String> ShingleHashMap(String st, int key) {
		HashMap<Integer,String> sh = new HashMap<Integer,String>();//create a hashmap
		st = st.replaceAll("[\t\n\r]", " ");//replace all new line, tab to space in text file 
		
		// for loop to create each shingle
		for (int i = 0; i <= st.length() - key; i++) {
			String sh1 = st.substring(i, i + key);
			sh.put(sh1.hashCode(), sh1);  //save the shingle(key) into the map using a hash code
		}		
		return sh;
	}
}
