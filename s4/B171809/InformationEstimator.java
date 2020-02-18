package s4.B171809; // Please modify to s4.Bnnnnnn, where nnnnnn is your student ID. 
import java.lang.*;
import s4.specification.*;

/* What is imported from s4.specification
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

public class InformationEstimator implements InformationEstimatorInterface{
  // Code to tet, *warning: This code condtains intentional problem*
  byte [] target; // data to compute its information quantity
  byte [] space;  // Sample space to compute the probability
  boolean spaceSet = false;
  FrequencerInterface frequencer;  // Object for counting frequency
  double [] dp = null;

  // IQ: information quantity for a count,  -log2(count/sizeof(space))
  double iq(int freq) {
    // -log2(freq / space.length)
    return  - Math.log10((double) freq / (double) space.length)/ Math.log10(2.0);
  }

  public void setTarget(byte [] target) {
    this.target = target;
    this.dp = null;
  }
  public void setSpace(byte []space) {
    this.space = space;
    this.spaceSet = false;
  }

  public void prepare() {
    if (this.frequencer == null) {
      this.frequencer = new Frequencer();
    }
    if (!spaceSet) {
      this.frequencer.setSpace(space);
      spaceSet = true;
    }
    if (dp == null) {
      this.frequencer.setTarget(target);
      dp = new double[target.length];
      for(int i = 0; i < dp.length; i++){
        dp[i] = iq(frequencer.subByteFrequency(0, i + 1));
        for (int k = 0; k < i - 1; k++) {
          double tmp = dp[k] + iq(frequencer.subByteFrequency(k + 1, i + 1));
          if (dp[i] > tmp) {
            dp[i] = tmp;
          }
        }
        /*
           dp[i] = min(
           iq(frequencer.subByteFrequency(0, i)),
           dp[0] + iq(1, i),
           dp[1] + iq(2, i),
           dp[2] + iq(3, i),
           ...,
           dp[target.length - 1] + iq(target.length - 1, i));
           */
      }
    }
  }

  public double estimation(){
    prepare();
    return dp[target.length - 1];
  }

  public static void main(String[] args) {
    InformationEstimator myObject;
    double value;
    myObject = new InformationEstimator();
    myObject.setSpace("3210321001230123".getBytes());
    myObject.setTarget("0".getBytes());
    value = myObject.estimation();
    System.out.println(">0 "+value);
    myObject.setTarget("01".getBytes());
    value = myObject.estimation();
    System.out.println(">01 "+value);
    myObject.setTarget("0123".getBytes());
    value = myObject.estimation();
    System.out.println(">0123 "+value);
    myObject.setTarget("00".getBytes());
    value = myObject.estimation();
    System.out.println(">00 "+value);
  }
}

