package s4.B193349; // Please modify to s4.Bnnnnnn, where nnnnnn is your student ID. 
import java.lang.*;
import s4.specification.*;

/*
interface FrequencerInterface {     // This interface provides the design for frequency counter.
    void setTarget(byte[]  target); // set the data to search.
    void setSpace(byte[]  space);  // set the data to be searched target from.
    int frequency(); //It return -1, when TARGET is not set or TARGET's length is zero
                    //Otherwise, it return 0, when SPACE is not set or Space's length is zero
                    //Otherwise, get the frequency of TAGET in SPACE
    int subByteFrequency(int start, int end);
    // get the frequency of subByte of taget, i.e target[start], taget[start+1], ... , target[end-1].
    // For the incorrect value of START or END, the behavior is undefined.
}
*/

/*
package s4.specification;
public interface InformationEstimatorInterface{
    void setTarget(byte target[]); // set the data for computing the information quantities
    void setSpace(byte space[]); // set data for sample space to computer probability
    double estimation(); // It returns 0.0 when the target is not set or Target's length is zero;
// It returns Double.MAX_VALUE, when the true value is infinite, or space is not set.
// The behavior is undefined, if the true value is finete but larger than Double.MAX_VALUE.
// Note that this happens only when the space is unreasonably large. We will encounter other problem anyway.
// Otherwise, estimation of information quantity, 
}                        
*/


public class TestCase {
  public static void main(String[] args) {

		System.out.println("Start FrequencerInterface Test");
		try{
			FrequencerInterface  myObject;
			int freq;
			System.out.println("checking s4.B193349.Frequencer");
			myObject = new s4.B193349.Frequencer();
			myObject.setSpace("Hi Ho Hi Ho".getBytes());
		}	catch(Exception e) {
			System.out.println("Exception occurred by setSpace : STOP");
		}

		try{
			FrequencerInterface  myObject;
			int freq;
			System.out.println("checking s4.B193349.Frequencer");
			myObject = new s4.B193349.Frequencer();
			myObject.setTarget("Hi Ho Hi Ho".getBytes());
		}	catch(Exception e) {
			System.out.println("Exception occurred by setTarget : STOP");
		}

		try {
			FrequencerInterface  myObject;
			int freq;
			System.out.println("checking s4.B193349.Frequencer");
			myObject = new s4.B193349.Frequencer();
			myObject.setSpace("Hi Ho Hi Ho".getBytes());
			myObject.setTarget("HiHi".getBytes());
			freq = myObject.frequency();
			System.out.print("\"HiHi\" in \"Hi Ho Hi Ho\" appears "+freq+" times. ");
			if(4 == freq) { System.out.println("OK"); } else {System.out.println("WRONG"); }
		}
		catch(Exception e) {
			System.out.println("Exception occurred by frequwncy: STOP");
		}

		try {
			FrequencerInterface  myObject;
			int freq;
			System.out.println("checking s4.B193349.Frequencer");
			myObject = new s4.B193349.Frequencer();
			myObject.setSpace("Hi Ho Hi Ho".getBytes());
			myObject.setTarget("HoiHoi".getBytes());
			freq = myObject.frequency();
			System.out.print("\"HiHi\" in \"Hi Ho Hi Ho\" appears "+freq+" times. ");
			if(4 == freq) { System.out.println("OK"); } else {System.out.println("WRONG"); }
		}
		catch(Exception e) {
			System.out.println("Exception occurred by frequwncy when Target is HoiHoi and Space Hi Ho Hi Ho: STOP");
		}

		try {
			FrequencerInterface  myObject;
			int freq;
			System.out.println("checking s4.B193349.Frequencer");
			myObject = new s4.B193349.Frequencer();
			myObject.setSpace("Hi Ho Hi Ho".getBytes());
			myObject.setTarget("H".getBytes());
			freq = myObject.frequency();
			System.out.print("\"H\" in \"Hi Ho Hi Ho\" appears "+freq+" times. ");
			if(4 == freq) { System.out.println("OK"); } else {System.out.println("WRONG"); }
		}
		catch(Exception e) {
			System.out.println("Exception occurred by frequwncy: STOP");
		}

		try {
			FrequencerInterface  myObject;
			int freq;
			System.out.println("checking s4.B193349.Frequencer");
			myObject = new s4.B193349.Frequencer();
			myObject.setSpace("Hi Ho Hi Ho".getBytes());
			myObject.setTarget("H".getBytes());
			freq = myObject.subByteFrequency(2,8);
			System.out.print("\"H\" in \"Hi Ho Hi Ho\" appears "+freq+" times. ");
			if(2 == freq) { System.out.println("OK"); } else {System.out.println("WRONG"); }
		}
		catch(Exception e) {
			System.out.println("Exception occurred by frequwncy: STOP");
		}

		System.out.println("End FrequencerInterface Test");

		System.out.println("Start InformationEstimatorInterface Test");

		try {
	    InformationEstimatorInterface myObject;
	    System.out.println("checking s4.B193349.InformationEstimator.setSpace(byte[])");
	    myObject = new s4.B193349.InformationEstimator();
	    myObject.setSpace("3210321001230123".getBytes());
		}
		catch(Exception e) {
				System.out.println("Exception occurred by setSpace: STOP");
		}

		try {
	    InformationEstimatorInterface myObject;
	    System.out.println("checking s4.B193349.InformationEstimator.setTarget(byte[])");
	    myObject = new s4.B193349.InformationEstimator();
	    myObject.setTarget("0".getBytes());
		}
		catch(Exception e) {
				System.out.println("Exception occurred by setTarget: STOP");
		}

		try{
			InformationEstimatorInterface myObject;
	    double value;
	    System.out.println("checking s4.B193349.InformationEstimator.estimation()");
	    myObject = new s4.B193349.InformationEstimator();
	    myObject.setSpace("3210321001230123".getBytes());
	    myObject.setTarget("0".getBytes());
	    value = myObject.estimation();
	    System.out.println(">0 "+value);
		}
		catch(Exception e) {
			System.out.println("Exception occurred by estimation: STOP");
		}
		System.out.println("End InformationEstimatorInterface Test");
  }
}	    
	    
