import java.io.*;
import java.util.*;

public class HuffmanTree {
   private HuffmanNode rootNode;
   
   public HuffmanTree(int[] count) {
      PriorityQueue<HuffmanNode> huffTree = new PriorityQueue<HuffmanNode>();
      for(int i = 0; i < count.length; i++) {
         if(count[i] != 0) {
            huffTree.add(new HuffmanNode(i, count[i]));
         }
      }
      HuffmanNode eofNode = new HuffmanNode((count.length), 1);
      huffTree.add(eofNode);
      while(huffTree.size() > 1) {
         HuffmanNode leftNode = huffTree.remove();
         HuffmanNode rightNode = huffTree.remove();
         int totalCount = leftNode.charCount + rightNode.charCount;
         int tempFreq = 0;
         HuffmanNode nodeHolder = new HuffmanNode(tempFreq, totalCount, leftNode, rightNode);
         huffTree.add(nodeHolder);
      }
      rootNode = huffTree.remove();
   }
      
   public void write(PrintStream output) {
      if(rootNode != null) {
         writeHelper(output, rootNode, "");
      }
   }
   
   private void writeHelper(PrintStream output, HuffmanNode testNode, String charCode) {
      if(testNode.leftNode == null && testNode.rightNode == null) {
         output.println(testNode.charVal);
         output.println(charCode);
      } else {
         charCode += "0";
         writeHelper(output, testNode.leftNode, charCode);
         charCode = charCode.substring(0, charCode.length() - 1);
         charCode += "1";
         writeHelper(output, testNode.rightNode, charCode);
         charCode = charCode.substring(0, charCode.length() - 1);
      }
   }
   
   public HuffmanTree(Scanner input) {
      while(input.hasNextLine()) {
         int charVal = Integer.parseInt(input.nextLine());
         String charCode = input.nextLine();
         rootNode = treeConstructor(charCode, charVal, rootNode);
      }
   }
   
   private HuffmanNode treeConstructor(String charCode, int charVal, HuffmanNode tempNode) {
         int tempFreq = 0;
         if(tempNode == null) {
            tempNode = new HuffmanNode(tempFreq, 0);
         } else if(charCode.isEmpty()) {
            tempNode = new HuffmanNode(charVal, 0);
         } else if(charCode.charAt(0) == '0') {
            tempNode.leftNode = treeConstructor(charCode.substring(1), charVal,
            tempNode.leftNode);
         } else {
            tempNode.rightNode = treeConstructor(charCode.substring(1), charVal,
            tempNode.rightNode);
         }
         return tempNode;
   }
   
   public void decode(BitInputStream input, PrintStream output, int eof) {
      HuffmanNode tempNode = rootNode;
      while(tempNode.charVal != eof) {
         if(tempNode.leftNode == null && tempNode.rightNode == null) {
            output.write(tempNode.charVal);
         } else if(input.readBit() == 0) {
            tempNode = tempNode.leftNode;
         } else {
            tempNode = tempNode.rightNode;
         }
      }
   }
}