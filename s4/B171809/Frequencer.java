package s4.B171809;
import java.lang.*;
import s4.specification.*;
import java.util.function.*;


/*package s4.specification;
  ここは、１回、２回と変更のない外部仕様である。
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



public class Frequencer implements FrequencerInterface{
    // Code to start with: This code is not working, but good start point to work.
    byte [] myTarget;
    byte [] mySpace;
    boolean targetReady = false;
    boolean spaceReady = false;

    int []  suffixArray; // Suffix Arrayの実装に使うデータの型をint []とせよ。
    private Sort sort = new Sort();

    // The variable, "suffixArray" is the sorted array of all suffixes of mySpace.                                    
    // Each suffix is expressed by a integer, which is the starting position in mySpace. 
                            
    // The following is the code to print the contents of suffixArray.
    // This code could be used on debugging.                                                                

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
        // suffixCompareはソートのための比較メソッドである。
        // 次のように定義せよ。
        // comparing two suffixes by dictionary order.
        // suffix_i is a string starting with the position i in "byte [] mySpace".
        // Each i and j denote suffix_i, and suffix_j.                            
        // Example of dictionary order                                            
        // "i"      <  "o"        : compare by code                              
        // "Hi"     <  "Ho"       ; if head is same, compare the next element    
        // "Ho"     <  "Ho "      ; if the prefix is identical, longer string is big  
        //  
        //The return value of "int suffixCompare" is as follows. 
        // if suffix_i > suffix_j, it returns 1   
        // if suffix_i < suffix_j, it returns -1  
        // if suffix_i = suffix_j, it returns 0;   

        // ここにコードを記述せよ 

      //iかjの長さを超えるまで再帰が回るようだったら文字列が同じだと判定して０を返す
      //iの長さ　＞　全体の長さ || jの長さ > 全体の長さ


TAIL_RECURSION:
      do {
        if(mySpace[i] < mySpace[j]){		// if suffix_i < suffix_j
          return -1;
        }
        else if(mySpace[i] > mySpace[j]){	// if suffix_i > suffix_j
          return 1;
        }
        else if(mySpace[i] == mySpace[j]){		//一文字目が同じだったら
          if(i + 1 == mySpace.length && j + 1 == mySpace.length){     //両方とも終わり
            return 0;
          }
          else if(i + 1 == mySpace.length){	//どちらかが終わったとき終わってないほうに文字列が存在するか？
            return -1;                      //i + 1 が終わり
          }
          else if(j + 1 == mySpace.length){   //j + 1 が終わり
            return 1;
          }

          //return suffixCompare(i + 1, j + 1);   //どちらも終わりでないので次の文字を見に行く
          i = i + 1;
          j = j + 1;
          continue TAIL_RECURSION;
        }
      } while (true);
    }

    public void setSpace(byte []space) { 
        // suffixArrayの前処理は、setSpaceで定義せよ。
        mySpace = space; if(mySpace.length>0) spaceReady = true;
        // First, create unsorted suffix array.
        suffixArray = new int[space.length];
        // put all suffixes in suffixArray.
        for(int i = 0; i< space.length; i++) {
            suffixArray[i] = i; // Please note that each suffix is expressed by one integer.      
        }
        //                                            
        // ここに、int suffixArrayをソートするコードを書け。
        // 　順番はsuffixCompareで定義されるものとする。    
        this.sort.sort(suffixArray, this::suffixCompare);
    }

    // Suffix Arrayを用いて、文字列の頻度を求めるコード
    // ここから、指定する範囲のコードは変更してはならない。

    public void setTarget(byte [] target) {
        myTarget = target; if(myTarget.length>0) targetReady = true;
    }

    public int frequency() {
        if(targetReady == false) return -1;
        if(spaceReady == false) return 0;
        return subByteFrequency(0, myTarget.length);
    }



    public int subByteFrequency(int start, int end) {
        /* This method be work as follows, but much more efficient
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
    // 変更してはいけないコードはここまで。

    private int targetCompare(int i, int j, int k) {
        // suffixArrayを探索するときに使う比較関数。
        // 次のように定義せよ
        // suffix_i is a string in mySpace starting at i-th position.
        // target_i_k is a string in myTarget start at j-th postion ending k-th position.
        // comparing suffix_i and target_j_k.
        // if the beginning of suffix_i matches target_i_k, it return 0.
        // The behavior is different from suffixCompare on this case.
        // if suffix_i > target_i_k it return 1; 
        // if suffix_i < target_i_k it return -1;
        // It should be used to search the appropriate index of some suffix.
        // Example of search 
        // suffix          target
        // "o"       >     "i"
        // "o"       <     "z"
        // "o"       =     "o"
        // "o"       <     "oo"
        // "Ho"      >     "Hi"
        // "Ho"      <     "Hz"
        // "Ho"      =     "Ho"
        // "Ho"      <     "Ho "   : "Ho " is not in the head of suffix "Ho"
        // "Ho"      =     "H"     : "H" is in the head of suffix "Ho"
        //
        // ここに比較のコードを書け 
        //

        // suffix_i: mySpace[i..]
        // target_i_k: myTarget[j..k]
        final int SUFFIX_IS_BIGGER_THAN_TARGET = 1;
        final int SUFFIX_IS_LESS_THAN_TARGET = -1;
TAIL_RECURSION:
        do {
          if (j >= k) return 0;
          if (i >= mySpace.length) return SUFFIX_IS_LESS_THAN_TARGET;

          if (mySpace[i] < myTarget[j]) {
            return SUFFIX_IS_LESS_THAN_TARGET;
          }
          else if (mySpace[i] > myTarget[j]) {
            return SUFFIX_IS_BIGGER_THAN_TARGET;
          }
          else /*if (mySpace[i] == myTarget[j])*/ {

            //return targetCompare(i + 1, j + 1, k);
            i = i + 1;
            j = j + 1;
            continue TAIL_RECURSION;
          }
        } while(true);
    }


    private int subByteStartIndex(int start, int end) {
        //suffix arrayのなかで、目的の文字列の出現が始まる位置を求めるメソッド
        // 以下のように定義せよ。
        /* Example of suffix created from "Hi Ho Hi Ho"
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

        // It returns the index of the first suffix 
        // which is equal or greater than target_start_end.                         
        // Assuming the suffix array is created from "Hi Ho Hi Ho",                 
        // if target_start_end is "Ho", it will return 5.                           
        // Assuming the suffix array is created from "Hi Ho Hi Ho",                 
        // if target_start_end is "Ho ", it will return 6.                
        //                                                                          
        // ここにコードを記述せよ。                                                 
        //                                                                         

        final int SUFFIX_IS_BIGGER_THAN_TARGET = 1;
        final int SUFFIX_IS_LESS_THAN_TARGET = -1;
        int result = binarySearch(suffixArray, (array, index) -> {
          // target: targetCompare(suffixArray[index]) == 0 && targetCompare(suffixArray[index - 1]) == SUFFIX_IS_LESS_THAN_TARGET
          switch (targetCompare(array[index], start, end)) {
            case SUFFIX_IS_LESS_THAN_TARGET:
              return BinarySearchDirection.FORWARD;
            case SUFFIX_IS_BIGGER_THAN_TARGET:
              return BinarySearchDirection.BACKWARD;
            case 0:
              if (index == 0) return BinarySearchDirection.STOP;
              if (targetCompare(array[index - 1], start, end) == SUFFIX_IS_LESS_THAN_TARGET) return BinarySearchDirection.STOP;
              else return BinarySearchDirection.BACKWARD;
            default:
              throw new IllegalStateException();
          }
        });
        if (result == -1) return 0;
        return result;
    }

    private int subByteEndIndex(int start, int end) {
        //suffix arrayのなかで、目的の文字列の出現しなくなる場所を求めるメソッド
        // 以下のように定義せよ。
        /* Example of suffix created from "Hi Ho Hi Ho"
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
        // It returns the index of the first suffix 
        // which is greater than target_start_end; (and not equal to target_start_end)
        // Assuming the suffix array is created from "Hi Ho Hi Ho",                   
        // if target_start_end is "Ho", it will return 7 for "Hi Ho Hi Ho".  
        // Assuming the suffix array is created from "Hi Ho Hi Ho",          
        // if target_start_end is"i", it will return 9 for "Hi Ho Hi Ho".    
        //                                                                   
        //　ここにコードを記述せよ                                           
        //                                                                   
        final int SUFFIX_IS_BIGGER_THAN_TARGET = 1;
        final int SUFFIX_IS_LESS_THAN_TARGET = -1;

        // binarySearch finds the index of the last suffix.
        // if the target is "Ho", it returns >>> 6 <<<
        // if the terget is not found in suffixArray, it returns -1

        final int OFFSET = 1;
        return OFFSET + binarySearch(suffixArray, (array, index) -> {
          // target: targetCompare(suffixArray[index]) == 0 && targetCompare(suffixArray[index + 1]) == SUFFIX_IS_BIGGER_THAN_TARGET
          switch (targetCompare(array[index], start, end)) {
            case SUFFIX_IS_LESS_THAN_TARGET:
              return BinarySearchDirection.FORWARD;
            case SUFFIX_IS_BIGGER_THAN_TARGET:
              return BinarySearchDirection.BACKWARD;
            case 0:
              if (index == array.length - 1) return BinarySearchDirection.STOP;
              if (targetCompare(array[index + 1], start, end) == SUFFIX_IS_BIGGER_THAN_TARGET) return BinarySearchDirection.STOP;
              else return BinarySearchDirection.FORWARD;
            default:
              throw new IllegalStateException();
          }
        });
    }

    private static enum BinarySearchDirection {
      BACKWARD,
      STOP,
      FORWARD,
    }
    @FunctionalInterface
    private static interface BinarySearchFunction {
      public BinarySearchDirection search(int[] array, int index);
    }

    private int binarySearch(int[] array, BinarySearchFunction nextDirection) {
      return binarySearch(array, nextDirection, 0, array.length);
    }
    private int binarySearch(int[] array, BinarySearchFunction nextDirection, int start, int endOpen) {
TAIL_RECURSION:
      do {
        if (start == endOpen) return -1;
        int index = (start + endOpen) / 2;
        switch (nextDirection.search(array, index)) {
          case STOP:
            return index;
          case BACKWARD:
            //return binarySearch(array, nextDirection, start, index);
            start = start; endOpen = index;
            continue TAIL_RECURSION;
          case FORWARD:
            //return binarySearch(array, nextDirection, index + 1, endOpen);
            start = index + 1; endOpen = endOpen;
            continue TAIL_RECURSION;
        }
      } while(true);
    }

    // Suffix Arrayを使ったプログラムのホワイトテストは、
    // privateなメソッドとフィールドをアクセスすることが必要なので、
    // クラスに属するstatic mainに書く方法もある。
    // static mainがあっても、呼びださなければよい。
    // 以下は、自由に変更して実験すること。
    // 注意：標準出力、エラー出力にメッセージを出すことは、
    // static mainからの実行のときだけに許される。
    // 外部からFrequencerを使うときにメッセージを出力してはならない。
    // 教員のテスト実行のときにメッセージがでると、仕様にない動作をするとみなし、
    // 減点の対象である。
    public static void main(String[] args) {
        Frequencer frequencerObject;
        try {
            frequencerObject = new Frequencer();
            frequencerObject.setSpace("Hi Ho Hi Ho".getBytes());
            frequencerObject.printSuffixArray(); // you may use this line for DEBUG
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

