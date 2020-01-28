package s4.B183341;
import java.lang.*;
import s4.specification.*;

/*package s4.specification;

public interface FrequencerInterface {     // This interface provides the design for frequency counter.
    void setTarget(byte  target[]); // set the data to search.
    void setSpace(byte  space[]);  // set the data to be searched target from.
    int frequency(); //It return -1, when TARGET is not set or TARGET's length is zero
                    //Otherwise, it return 0, when SPACE is not set or SPACE's length is zero
                    //Otherwise, get the frequency of TAGET in SPACE
    int subByteFrequency(int start, int end);
    // get the frequency of subByte of taget, i.e target[start], taget[start+1], ... , target[end-1].
    // For the incorrect value of START or END, the behavior is undefined.

}
*/



public class Frequencer{
    // Code to start with: This code is not working, but good start point to work.
    byte [] myTarget;
    byte [] mySpace;
    boolean targetReady = false;
    boolean spaceReady = false;

    int []  suffixArray;

    // The variable, "suffixArray" is the sorted array of all suffixes of mySpace.
    // Each suffix is expressed by a integer, which is the starting position in mySpace.
    // The following is the code to print the variable
    
    private void printSuffixArray() {
		if(spaceReady) {
		    for(int i=0; i< mySpace.length; i++) {
				int s = suffixArray[i];
				for(int j=s;j<mySpace.length;j++) {
				    System.out.write(mySpace[j]);
				}
				System.out.write('\n');
		    }
		}
    }

    private int suffixCompare(int i, int j) {
		// comparing two suffixes by dictionary order.
		// i and j denotes suffix_i, and suffix_j
		// if suffix_i > suffix_j, it returns 1
		// if suffix_i < suffix_j, it returns -1
		// if suffix_i = suffix_j, it returns 0;
		// It is not implemented yet, 
		// It should be used to create suffix array.
		// Example of dictionary order
		// suffix_i    suffix_j
		// "i"      <  "o"        : compare by code
		// "Hi"     <  "Ho"       ; if head is same, compare the next element
		// "Ho"     <  "Ho "      ; if the prefix is identical, longer string is big
		//
		// ****  Please write code here... ***
    	int compResult = 0;
	    String str = new String(mySpace);
	    String suffix_i = str.substring(i);
	    String suffix_j = str.substring(j);
	    if(suffix_i.compareTo(suffix_j) > 0) {
	    	compResult = 1;
	    }else if(suffix_i.compareTo(suffix_j) < 0) {
	    	compResult = -1;
	    }else {
	    	compResult = 0;
	    }
		
		return compResult; // This line should be modified.
    }

    public void setSpace(byte []space) { 
		mySpace = space;
		if(mySpace.length > 0) spaceReady = true; 
		suffixArray = new int[space.length];
		
		// put all suffixes  in suffixArray. Each suffix is expressed by one integer.
		
		for(int i = 0; i< space.length; i++) {
		    suffixArray[i] = i;
		}
		// Sorting is not implmented yet.
		// ****  Please write code here... ***
		int compResult = 0;
		for(int i = 0; i < suffixArray.length - 1; i++) {
			for(int j = suffixArray.length - 1; j > i ; j--) {
				compResult = suffixCompare(suffixArray[j-1], suffixArray[j]);
				if(compResult == 1) {
					int tmp = suffixArray[j-1];
					suffixArray[j-1] = suffixArray[j];
					suffixArray[j] = tmp;
				}
			}
		}
    }

    private int targetCompare(int i, int start, int end) {
		// comparing suffix_i and target_start_end by dictonary order with limitation of length;
		// if the beginning of suffix_i matches target_start_end, and suffix is longer than target  it returns 0;
		//  suffix_i --> mySpace[i], mySpace[i+1], .... , mySpace[mySpace.length -1]
		//  target_start_end -> myTarget[start], myTarget[start+1], .... , myTarget[end-1]
		// if suffix_i > target_start_end it return 1;
		// if suffix_i < target_start_end it return -1
		// It is not implemented yet.
		// It should be used to search the apropriate index of some suffix.
		// Example of search
		// suffix_i        target_start_end
	        // "o"       >     "i"    
	        // "o"       <     "z"
		// "o"       =     "o"
	        // "o"       <     "oo"
		// "Ho"      >     "Hi"
		// "Ho"      <     "Hz"
		// "Ho"      =     "Ho"
	        // "Ho"      <     "Ho "   : "Ho " is not in the head of suffix "Ho"
		// "Ho"      =     "H"     : "H" is in the head of suffix "Ho"
		
		// ****  Please write code here... ***
    	int compResult = -1;
		String suffix_i = new String(mySpace).substring(i);
		String target_start_end = new String(myTarget).substring(start,end);
		//System.out.println(target_start_end);
		if(target_start_end.length() <= suffix_i.length()) {
	    	String str = suffix_i.substring(0,end-start);
	    	if(str.contains(target_start_end)) {
	    		compResult = 0;
	    	}else {
	    		if(suffix_i.compareTo(target_start_end) > 0) {
	    			compResult = 1;
	    		}else {
	    			compResult = -1;
	    		}
	    	}
    	}else {
    		if(suffix_i.compareTo(target_start_end) > 0) {
    			compResult = 1;
    		}else {
    			compResult = -1;
    		}
    	}
    	
		return compResult; // This line should be modified.
    }

    private int subByteStartIndex(int start, int end) {
		// It returns the index of the first suffix which is equal or greater than subBytes;
		// not implemented yet;
		// If myTaget is "Hi Ho",  start=0, end=2 means "Hi".
		// For "Ho", it will return 5  for "Hi Ho Hi Ho".
		//   5 means suffix_5,
		//   Please note suffix_5 is "Ho" and "Ho" starts from here.
		// For "Ho ", it will return 6 for "Hi Ho Hi Ho".
		//   6 means suffix_6,
		//   Please note suffix_6 is "Ho Hi Ho", and "Ho " starts from here.
		//
		// ****  Please write code here... ***
	    int startIndex = -1;
	    for(int i = 0; i < suffixArray.length; i++) {
	    	if(targetCompare(suffixArray[i], start, end) == 0) {
	    		startIndex = i;
	    		break;
	    	}
	    }
	    
		return startIndex; // This line should be modified.
    }

    private int subByteEndIndex(int start, int end) {
		// It returns the next index of the first suffix which is greater than subBytes;
		// not implemented yet
		// If myTaget is "Hi Ho",  start=0, end=2 means "Hi".
		// For "Ho", it will return 7  for "Hi Ho Hi Ho".
		// For "Ho ", it will return 7 for "Hi Ho Hi Ho".
		//  7 means suffix_7,
		//  Please note suffix_7 is "i Ho Hi", which does not start with "Ho" nor "Ho ".
	        //  Whereas suffix_5 is "Ho Hi Ho", which starts "Ho" and "Ho ".
		//
		// ****  Please write code here... ***
	    int endIndex = -1;
	    for(int i = suffixArray.length - 1; i >= 0; i--) {
	    	if(targetCompare(suffixArray[i], start, end) == 0) {
	    		endIndex = i + 1;
	    		break;
	    	}
	    }
    	
		return endIndex; // This line should be modified.
    }

    public int subByteFrequency(int start, int end) {
		/* This method be work as follows, but
		int spaceLength = mySpace.length;
		int count = 0;
		for(int offset = 0; offset< spaceLength - (end - start); offset++) {
		    boolean abort = false;
		    for(int i = 0; i< (end - start); i++) {
			if(myTarget[start+i] != mySpace[offset+i]) { abort = true; break; }
		    }
		    if(abort == false) { count++; }
		}
		*/
		int first = subByteStartIndex(start, end);
		int last1 = subByteEndIndex(start, end);
		return last1 - first;
    }

    public void setTarget(byte [] target) { 
    	myTarget = target; if(myTarget.length>0) targetReady = true; 
    }

    public int frequency() {
		if(targetReady == false) return -1;
		if(spaceReady == false) return 0;
		return subByteFrequency(0, myTarget.length);
    }

    public static void main(String[] args) {
		Frequencer frequencerObject;
		try {
		    frequencerObject = new Frequencer();
		    frequencerObject.setSpace("Hi Ho Hi Ho".getBytes());
		    frequencerObject.printSuffixArray(); // you may use this line for DEBUG
		    System.out.println();
		    /* Example from "Hi Ho Hi Ho"
		       0: Hi Ho
		       1: Ho
		       2: Ho Hi Ho
		       3:Hi Ho
		       4:Hi Ho Hi Ho
		       5:Ho
		       6:Ho Hi Ho
		       7:i Ho
		       8:i Ho Hi Ho
		       9:o
		       A:o Hi Ho
		    */
	
		    frequencerObject.setTarget("H".getBytes());
		    //
		    // ****  Please write code to check subByteStartIndex, and subByteEndIndex
		    /*int start = frequencerObject.subByteStartIndex(0, 1);
		    int end   = frequencerObject.subByteEndIndex(0, 1);
		    System.out.println("start:" + start);
		    System.out.println("end:" + end);*/ //デバッグ用
		    //
	
		    int result = frequencerObject.frequency();
		    System.out.print("Freq = "+ result+" ");
		    if(4 == result) { System.out.println("OK"); } else {System.out.println("WRONG"); }
		}
		catch(Exception e) {
		    System.out.println("STOP");
		}
    }
}
