package decisiontree;

import support.decisiontree.DecisionTreeData;
import support.decisiontree.DecisionTreeNode;
import support.decisiontree.ID3;

/**
  * This class is where your ID3 algorithm should be implemented.
  */
public class MyID3 implements ID3 {

    /**
     * Constructor. You don't need to edit this.
     */
    public MyID3() {
        
    }

    /**
     * This is the trigger method that actually runs the algorithm.
     * This will be called by the visualizer when you click 'train'.
     */
    @Override
    public DecisionTreeNode id3Trigger(DecisionTreeData data) {
        // TODO run the algorithm, return the root of the tree
        return null;
    }

    /*
     * TODO implement the algorithm - this is one possible method signature, feel free to change!
     */
    private DecisionTreeNode myID3Algorithm(DecisionTreeData data, DecisionTreeData parentData) {
    	return null;
    }

}
