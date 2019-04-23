//Wenjie Ma
//CSE 143 Section AJ
//4/11/17
//Programming Assignment: HtmlValidator
//This program will validate a queue of HTML tags and can also modify them.

import java.util.*;

public class HtmlValidator {
   private Queue<HtmlTag> tagQueue;

   //Constructs an empty list of HTML tags
   public HtmlValidator() {
      this(new LinkedList<HtmlTag>());
   }
  
   //Constructs a copy of the list "tags" passed in
   //Pre: tags != null; throws an IllegalArgumentException otherwise
   public HtmlValidator(Queue<HtmlTag> tags) {
      if (tags == null) {
         throw new IllegalArgumentException();
      }
      tagQueue = duplicate(tags);
   }
   
   //Pre: tag != null; throws an IllegalArgumentException otherwise
   //adds the tag to the queue which is the tagQueue created in the constructor
   public void addTag(HtmlTag tag) {
      if (tag == (null)) {
         throw new IllegalArgumentException();
      }
      tagQueue.add(tag);
   }
   
   //Returns a copy of the list of tags
   //Post: The copy does not change with the list of tags
   public Queue<HtmlTag> getTags() {
      return duplicate(tagQueue);
   }
   
   //Removes all tags containing the passed in String "element" from the list
   //Throws IllegalArgumentException if the element is null
   //Pre: !element.equals(null); throws an IllegalArgumentException otherwise
   public void removeAll(String element) {
      if(element == (null)) {
         throw new IllegalArgumentException();      
      }
      for(int i = 0; i < getTags().size(); i++) {
         HtmlTag frontTag = tagQueue.remove();
         if(!frontTag.getElement().equals(element)) {
            tagQueue.add(frontTag);
         } else {
            i--;
         }
      }
   }
   
   //Post:Prints string representations of each tag with correct indentation
   //Prints error messages if a tag is incorrectly located, missing, or not closed properly
   public void validate() {
      Stack<HtmlTag> openTagStack = new Stack<HtmlTag>();
      int indentSum = 0;
      int tagSize = tagQueue.size();
      //keep a memory of the size of the queue before the loop so that it doesn't change 
      for(int i = 0; i < tagSize; i++) {
         HtmlTag currentTag = tagQueue.remove();
         if(currentTag.isOpenTag()) {
            indent(indentSum);
            if(!currentTag.isSelfClosing()) {
               openTagStack.push(currentTag);
               indentSum++;
            }
         } else {
            if(!openTagStack.isEmpty() && currentTag.matches(openTagStack.peek())) {
               openTagStack.pop();
               indentSum--;
               indent(indentSum);
            } else {
               System.out.println("ERROR unexpected tag: " + currentTag);
            }
         }
         System.out.println(currentTag); 
         //prints back out the tag
         tagQueue.add(currentTag);
         //add the tag back to the queue
      }
      //checks for the unclosed tags and prints them out in the following
      while(!openTagStack.isEmpty()) { 
         System.out.println("ERROR unclosed tag: " + openTagStack.pop());
      }
   }
   
   //Helper method that returns a copy of the list passed in, "original"
   //Post: the copy does not change, even if the list passed in is changed afterwards 
   private Queue<HtmlTag> duplicate(Queue<HtmlTag> original) {
      Queue<HtmlTag> copy = new LinkedList<HtmlTag>();
      while(!original.isEmpty()) {
         copy.add(original.remove());
      }
      while(!copy.isEmpty()) {
         HtmlTag tempTag = copy.remove();
         original.add(tempTag);
      }
      return original;   
   }
   
   //Helper method that makes four space long indents
   //int "indentSum" passes in how many times an indent will be made on a line
   private void indent(int indentSum) {
      for(int i = 0; i < indentSum * 4; i++) {
         System.out.print(" ");
      }
   }
}