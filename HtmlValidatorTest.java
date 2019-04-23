// Wenjie Ma
// CSE 143 Section AJ
// 4/11/17
// Programming Assignment: HtmlValidatorTest
// This testing program stub creates a queue of HTML tags 
// in a valid sequence.
// You may use this as a starting point for being a client of your
// HtmlValidator object
import java.util.*;

public class HtmlValidatorTest {
	public static void main(String[] args) {
		// <b>Hi</b><br/>
		// A Queue of tags you may modify and pass to your HtmlValidator object
		Queue<HtmlTag> tags = new LinkedList<HtmlTag>();
		tags.add(new HtmlTag("b", true)); // <b>
		tags.add(new HtmlTag("b", false)); // </b>
		tags.add(new HtmlTag("br")); // <br/>
      //constructs a new html validor object
      HtmlValidator htmlFile = new HtmlValidator();
      int tagSize = tags.size();
      //copies the tag queue from the htmlValidator class
      for(int i = 0; i < tagSize; i++) {
         HtmlTag currentTag = tags.remove();
         htmlFile.addTag(currentTag); //to copy the tags from tag Queue to the htmlfile object
         tags.add(currentTag); //add back the original tags to maintain the same state
      }
      String removeElement = "br";
      System.out.println("Elements to be removed \"" + removeElement + "\"");
      System.out.println("Queue Before    : " + htmlFile.getTags().toString());
      System.out.println("Queue Validation Before : ");
      htmlFile.validate();
      System.out.println();
      htmlFile.removeAll(removeElement);
      System.out.println("Queue After : " + htmlFile.getTags().toString());
      System.out.println("Queue Validation After :");
      htmlFile.validate();

  	}
}