package similarItem;

import java.util.List;

public class CompareSignatures {
	
	public double similarities_of_sign(List<Integer> sign1, List<Integer> sign2){
		if (sign1.isEmpty() && sign2.isEmpty()) {
			return 0;
		} 
		int count=0;
		for(int i=0;i<sign1.size();i++){//go through with size of list1
			if(sign1.get(i).equals(sign2.get(i))){//when both of sign are same
				count++;//increase count
			}
		}
		double compSignature = (double)count/sign1.size();//Rule of similarities of signature
		return compSignature;//Return it
		
	}

}
