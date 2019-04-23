//Wenjie Ma
//Assignment 8: HuffmanTree
//In this program, we will explore the coding scheme called Huffman coding. We will utilize
//concepts such as binary tree and priority queues. We will explore how text files can be
//encoded and decoded by frequency of the letters in the text files. It can output a encoded
//file in Huffman code and also decode an already encoded file.

import java.io.*;//Implementation to use Scanner and more.
import java.util.*;//Implementation to use List, maps, etc.

public class HuffmanTree {
   private HuffmanNode rootNode;//Root node for the huffman tree
   
   //post:given an array of integers, construct a huffman tree in which the order of the nodes
   //are sorted by frequencies. In which the higher frequency node will appear first in the tree.
   //this allows for easy access and efficient encoding.
   //it will not modify the original array of integers.
   public HuffmanTree(int[] count) {
      PriorityQueue<HuffmanNode> huffTree = new PriorityQueue<HuffmanNode>();
      for(int i = 0; i < count.length; i++) {
         if(count[i] != 0) {
            huffTree.add(new HuffmanNode(i, count[i]));
         }
      }
      HuffmanNode eofNode = new HuffmanNode((count.length), 1);
      huffTree.add(eofNode);
      rootNode = treeConstructor(huffTree);
   }
   
   //post: given a priority queue that consists of Huffman Nodes, it will simplify the queue
   //until there is nothing to simplify, which means the huffman tree has been successfully
   //organized.
   private HuffmanNode treeConstructor(PriorityQueue<HuffmanNode> huffTree) {
      while(huffTree.size() > 1) {
         HuffmanNode leftNode = huffTree.remove();
         HuffmanNode rightNode = huffTree.remove();
         HuffmanNode nodeHolder = new HuffmanNode(leftNode, rightNode);
         huffTree.add(nodeHolder);
      }
      return huffTree.remove();
   }
   
   //post: using recursive backtracing to write the huffman tree to the given output stream
   //in standard format. In which the format is consisted of the first line being the interger
   //value for the character stored in the leaf, then followed by the second line of the code for
   //that character. The codes will appear in traversal order.
   public void write(PrintStream output) {
      if(rootNode != null) {
         writeHelper(output, rootNode, "");
      }
   }
   
   //post: this is the helper method to the write method. This will help the write method
   //explore all the possible leaf nodes in the huffman tree using recursive backtracing.
   //It will then write leaf's integer value and code in seperate lines to the given output stream
   private void writeHelper(PrintStream output, HuffmanNode testNode, String charCode) {
      if(testNode.leftNode == null && testNode.rightNode == null) {
         output.println(testNode.charVal);
         output.println(charCode);
      } else {
         writeHelper(output, testNode.rightNode, charCode + "1");
         writeHelper(output, testNode.leftNode, charCode + "0");
      }
   }
   
   //pre: assuming the Scanner contains a tree stored in standard format
   //post: it will reconstruct the huffman tree. Frequencies of characters are
   //irrelevant in this because the tree has already been constructed once.
   public HuffmanTree(Scanner input) {
      while(input.hasNextLine()) {
         int charVal = Integer.parseInt(input.nextLine());
         String charCode = input.nextLine();
         rootNode = treeConstructor(charCode, charVal, 0, rootNode);
      }
   }
   
   //post: Using the given HuffmanNode, charCode, charVal and charCount - We will be able
   //to reconstruct the huffman tree by determing the node's properties. We are also utilizing
   //recursive backtracing to examine all possible nodes.
   private HuffmanNode treeConstructor(String charCode, int charVal, int charCount,
   HuffmanNode tempNode) {
      if(charCode.isEmpty()) {
         tempNode = new HuffmanNode(charVal, charCount);
      } else {
         if(tempNode == null) {
            tempNode = new HuffmanNode(0);
         }
         else if(charCode.charAt(0) == '1') {
            tempNode.rightNode = treeConstructor(charCode.substring(1), charVal,
            charCount + 1, tempNode.rightNode);
         } else {
            tempNode.leftNode = treeConstructor(charCode.substring(1), charVal,
            charCount + 1, tempNode.leftNode);
         }
      }
      return tempNode;
   }
   
   //pre: assuming that in the input stream contains a legal encoding of characters
   //for this tree's Huffman code.
   //post: this will read individual bits from the input stream and should write the
   //corresponding characters to the output. It should stop reading when it encounters a
   //character with value equal to the eof(end of file) parameter.
   //so it should not be written to the PrintStream.
   public void decode(BitInputStream input, PrintStream output, int eof) {
      HuffmanNode nodeHolder = rootNode;
      while(nodeHolder.charVal != eof) {
         if(nodeHolder.leftNode == null && nodeHolder.rightNode == null) {
            output.write(nodeHolder.charVal);
            nodeHolder = rootNode;
         } else {
            if(input.readBit() == 1) {
               nodeHolder = nodeHolder.rightNode;
            } else {
               nodeHolder = nodeHolder.leftNode;
            }
         }
      }
   }
}