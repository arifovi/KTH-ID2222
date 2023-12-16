package similarItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.io.*;

public class Main {

	public static void main(String[] args) {
		File f=new File("src/dataset/");//all file location
	    int totalCSVFile = countFile(f);//return total numbers of files
		String path="src/dataset/";//path of dataset
		Shingling sh = new Shingling();//create object of Shingling class 
		String file1 = readFile(path+1+".csv");//read from csv file
		HashMap<Integer,String> sh1 = sh.ShingleHashMap(file1, 6);//create hashmap of file1 with key value where every shingle have 6 characters 
		System.out.println("Shingle hash of csv 1"+" is: "+sh1);		

		Set<Integer> set1 = sh1.keySet();//All keys of sh1 saved in set1
		Set<Integer> AllSet = new HashSet<Integer>(set1);//create hashset of set1 and add set1 there
		for(int i=1;i<=totalCSVFile;i++){//go through other csv files for jaccard similarities
			String file2 = readFile(path+i+".csv");
			HashMap<Integer,String> sh2 = sh.ShingleHashMap(file2, 6);//create hashmap of file2 with key value where every shingle have 6 characters
			System.out.println("Shingle hash of csv "+i+" is: " + sh2);
			
			Set<Integer> set2 = sh2.keySet();//All keys of current sh2 saved in set2
			AllSet.addAll(set2);//add current csv set2 to AllSet
			
			System.out.println("CompareSets set1 and set"+i+" are: "+new CompareSets().GetJaccardValue(set1, set2));//compute the jaccard similarity of two fellow sets
		}
		
		Random rand = new Random(); //create random object
		int n=500;//take a numeric value
		List<Integer> a = new ArrayList<Integer>();//create Integer type arraylist a
		List<Integer> b = new ArrayList<Integer>();//create another arraylist b
		
		for (int i = 0; i < n; i++) {
			int val=rand.nextInt(n);//create a random value
			a.add(1*val+1);//generate a random number(n*1+1) and save to list of a
			b.add(2*val+1);//generate a random number(n*2+1) and save to list of b
		}
		
		System.out.println("Signature of MinHash");
		//go through all the file for minhash signature
		for (int i=1; i<=totalCSVFile; i++) {
			String file3 = readFile(path+i+".csv");
			HashMap<Integer,String> sh3 = sh.ShingleHashMap(file3, 6);//create hashmap of file3 with key value where every shingle have 6 characters
			Set<Integer> set3 = sh3.keySet();//All keys of current sh3 saved in set3
			List<Integer> mh1 = new MinHashing().MinHashSignature(n, AllSet, set3, a, b);//list of minhash signature1 using Allset, currentSet, a and b 
			System.out.println("Signature for csv "+i+": "+mh1);
			
			for(int j=1;j<=totalCSVFile;j++){
				String file4 = readFile(path+j+".csv");
				HashMap<Integer,String> sh4 = sh.ShingleHashMap(file4, 6);//create hashmap of file4 with key value where every shingle have 6 characters
				Set<Integer> set4 = sh4.keySet();//All keys of current sh4 saved in set4
				List<Integer> mh2 = new MinHashing().MinHashSignature(n, AllSet, set4, a, b);//list of minhash signature2 using Allset, currentSet, a and b
				System.out.println("Signature for csv "+j+": "+mh2);
				System.out.println("Similarity of signature for csv " + i + " and csv "+j+": "
					+new CompareSignatures().similarities_of_sign(mh1, mh2));//compare two signatures using rule
			}
		}

	}
	//return total csv files
	public static int countFile(File f){
	    int count=0;
	    for (final File fileEntry : f.listFiles()) 
	    {
	       if (fileEntry.isDirectory()){
	          count += countFile(fileEntry);
	       }
	       else if(fileEntry.getName().endsWith(".csv")){
	           count++;
	       }
	    }
	    return count;
	}
	//readFile method
	public static String readFile(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        String newS="";
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempS = null;
            while ((tempS = reader.readLine()) != null) {
            	newS+=tempS;
            }
            reader.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return newS;
    }

}
