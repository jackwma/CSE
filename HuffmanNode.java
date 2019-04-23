//Wenjie Ma
//Assignment 8: HuffmanNode
//This class will construct HuffmanNode and allow users to assign approriate parameters when it
//wishes. Those include the left and right subnode, integer value of a particular character,
//and frequency of a particular character.
//This program will implement the Comparable interface

public class HuffmanNode implements Comparable<HuffmanNode> {
   public int charVal;//Integer value
   public int charCount;//Frequency value
   public HuffmanNode leftNode;//left subnode
   public HuffmanNode rightNode;//right subnode
   
   //post: constructs the HuffmanNode with desired integer value and frequency value
   public HuffmanNode(int charVal, int charCount) {
      this(charVal, charCount, null, null);
   }
   
   //post: constructs the HuffmanNode with desired integer value
   public HuffmanNode(int charVal) {
      this(charVal, 0);
   }
   
   //post: constructs the HuffmanNode with desired left/right subnode
   public HuffmanNode(HuffmanNode leftNode, HuffmanNode rightNode) {
      this(0, leftNode.charCount + rightNode.charCount, leftNode, rightNode);
   }
   
   //post: constructs the HuffmanNode with desired integer value, frequency value and left/right
   //subnode
   public HuffmanNode(int charVal, int charCount, HuffmanNode leftNode, HuffmanNode rightNode) {
      this.charVal = charVal;
      this.charCount = charCount;
      this.leftNode = leftNode;
      this.rightNode = rightNode;
   }
   
   //post:returns 0 if they have the same frequency value and negative value if the node comparing
   //to is higher in terms of frequency value and positive if the current node is higher in terms
   //of frequency value
   public int compareTo(HuffmanNode otherNode) {
      return this.charCount - otherNode.charCount;
   }
}