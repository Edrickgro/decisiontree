package decisiontree;

import org.junit.Test;
import support.decisiontree.DataReader;
import support.decisiontree.DecisionTreeData;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * This class can be used to test the functionality of your MyID3 implementation.
 * Use the Heap stencil and your heap tests as a guide!
 * 
 */

public class MyID3Test {
	
	@Test
	public void simpleTest() {
	    
	    MyID3 id3 = new MyID3();

	    // This creates a DecisionTreeData object that you can use for testing.
	    DecisionTreeData shortData = DataReader.readFile("/course/cs0160/lib/decisiontree-data/short-data-training.csv");
	    // FILL


	    assertThat(id3.getLog(0), is(0.0));
	    int totalAttribute = shortData.getAttributeList().size();
	    assertThat(id3.getCapacity(shortData.getExamples()), is(totalAttribute));

	    assertThat(id3.getFClass(shortData),is("True"));

		assertThat(id3.getImportance(shortData,shortData.getAttributeList()),is("Pat"));

	}
	

}