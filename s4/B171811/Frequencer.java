package s4.B171811;
import java.lang.*;
import s4.specification.*;


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
    if(this.mySpace.length - 1 < i) {
      return -1;
    } 
    else if(this.mySpace.length - 1 < j) {
      return 1;
    }
    else if(i == j){
      return 0;
    }

    // 先頭が同じ文字
    if(this.mySpace[i] == this.mySpace[j]) {
      return suffixCompare(i+1, j+1);
    }
    if(this.mySpace[i] > this.mySpace[j]) {
      return 1;
    }
    else {
      return -1;
    }
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
    for(int i = 1; i < this.suffixArray.length - 1; i++) {
      for(int k = 0; k < this.suffixArray.length - i; k++) {
        if(suffixCompare(suffixArray[k], suffixArray[k+1]) == 1) {
          //swap
          var tmp = this.suffixArray[k];
          this.suffixArray[k] = this.suffixArray[k+1];
          this.suffixArray[k+1] = tmp;
        }
      }
    }
  }

  // --- 交代 ---

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
    // System.out.println(this.suffixArray[6] + ": " + (char)this.mySpace[this.suffixArray[6]]);
    // System.out.println("target: " + (char)this.myTarget[0]);
    int n = 0;
    while(true) {
      if(this.mySpace[this.suffixArray[i] + n] == this.myTarget[j + n]) {
        n++;
        if(n < k - j) {
          if(n < this.mySpace.length - this.suffixArray[i]) {
            continue;
          }
          else {
            return -1;
          }
        }
        else {
          return 0;
        }
      }
      else {
        if(this.mySpace[this.suffixArray[i] + n] > this.myTarget[j + n]) {
          return 1;
        }
        else {
          return -1;
        }
      }
    }
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
    int n;
    for(n = 0; n < this.suffixArray.length; n++) {
      if(targetCompare(n, start, end) == 0) {
        break;
      }
    }
    return n;
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
    //ここにコードを記述せよ                                           
    int n;
    for(n = subByteStartIndex(start, end); n < this.suffixArray.length; n++) {
      if(targetCompare(n, start, end) != 0) {
        break;
      }
    }
    return n;
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
    try {
      Frequencer frequencerObject;
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
      int result = frequencerObject.frequency();
      System.out.print("Freq = "+ result+" ");
      if(4 == result) { System.out.println("OK"); } else {System.out.println("WRONG"); }
      
      //                                         
      // ****  Please write code to check subByteStartIndex, and subByteEndIndex
      //

      // ----- suffixCompare -----
      System.out.println("suffixCompare(0, 0) -> return  0: " + frequencerObject.suffixCompare(0, 0)); // return 0
      System.out.println("suffixCompare(1, 0) -> return  1: " + frequencerObject.suffixCompare(1, 0)); // return 1
      System.out.println("suffixCompare(0, 1) -> return -1: " + frequencerObject.suffixCompare(0, 1)); // return -1

      System.out.println("suffixCompare(0, 2) -> return  1: " + frequencerObject.suffixCompare(0, 2)); // return 1
      System.out.println("suffixCompare(2, 0) -> return -1: " + frequencerObject.suffixCompare(2, 0)); // return -1

      // ----- targetCompare -----
      frequencerObject = new Frequencer();
      frequencerObject.setSpace("Hi Ho Hi Ho".getBytes());
      frequencerObject.setTarget("izooHiHzHo ".getBytes());
      System.out.println("\"o\" > \"i\"    -> return  1: " + frequencerObject.targetCompare(9, 0, 1));  // "o" > "i"
      System.out.println("\"o\" < \"z\"    -> return -1: " + frequencerObject.targetCompare(9, 1, 2));  // "o" < "z"
      System.out.println("\"o\" = \"o\"    -> return  0: " + frequencerObject.targetCompare(9, 2, 3));  // "o" = "o"
      System.out.println("\"o\" < \"oo\"   -> return -1: " + frequencerObject.targetCompare(9, 2, 4));  // "o" < "oo"
      System.out.println("\"Ho\" > \"Hi\"  -> return  1: " + frequencerObject.targetCompare(5, 4, 6));  // "Ho" > "Hi"
      System.out.println("\"Ho\" < \"Hz\"  -> return -1: " + frequencerObject.targetCompare(5, 6, 8));  // "Ho" < "Hz"
      System.out.println("\"Ho\" = \"Ho\"  -> return  0: " + frequencerObject.targetCompare(5, 8, 10));  // "Ho" = "Ho"
      System.out.println("\"Ho\" < \"Ho \" -> return -1: " + frequencerObject.targetCompare(5, 8, 11)); // "Ho"  "Ho "
      System.out.println("\"Ho\" < \"H\"   -> return  0: " + frequencerObject.targetCompare(5, 8, 9));  // "Ho" = "H"

      System.out.println("\"Ho Hi Ho\" = \"i\"   -> return  -1: " + frequencerObject.targetCompare(6, 0, 1));  // "Ho" = "H"

      // ----- subByteStartIndex -----
      System.out.println("subByteStartIndex(\"Ho\") -> return  5: " + frequencerObject.subByteStartIndex(8, 10));
      System.out.println("subByteStartIndex(\"Ho \")-> return  6: " + frequencerObject.subByteStartIndex(8, 11));

      // ----- subByteEndIndex -----
      System.out.println("subByteEndIndex(\"Ho\") -> return  7: " + frequencerObject.subByteEndIndex(8, 10));
      System.out.println("subByteEndIndex(\"i\")  -> return  9: " + frequencerObject.subByteEndIndex(0, 1));

    }
    catch(Exception e) {
      System.out.println("STOP");
      e.printStackTrace();
    }
  }
}

