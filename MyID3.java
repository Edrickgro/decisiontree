package decisiontree;

import support.decisiontree.Attribute;
import support.decisiontree.DecisionTreeData;
import support.decisiontree.DecisionTreeNode;
import support.decisiontree.ID3;

import java.util.ArrayList;


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


        DecisionTreeNode theTree =  this.myID3Algorithm(data, data, data.getAttributeList());
        return theTree;
    }

    /**
     * myID3Algorithm: data, parentData, attributeList -> DecisionTreeNode
     Purpose: Returns a decision tree of the data
     Consumes: data, parent data, attribute list
     Produces: tree
     **/
    private DecisionTreeNode myID3Algorithm(DecisionTreeData data, DecisionTreeData parentData, ArrayList<Attribute> attributes) {
        // new instance of the data to prevent direct modification  **/
        DecisionTreeData theData = data;
        // empty arrayList to pass into entropy helper method (won't be used)  **/
        ArrayList<Integer> e = new ArrayList<>();

        // if there are no examples left in data, use parent's data **/
        if(data.getExamples().length == 0){
            // create new node whose element is the most frequent classification of parent
            DecisionTreeNode newNode = new DecisionTreeNode();
            newNode.setElement(this.getFClass(parentData));
            return newNode;
        }
        // if entropy of data is 0 meaning it's homogenous, create a node with that classification **/
        else if(this.getEntropy(theData.getExamples(),e,theData.getClassifications(),2)[0] == 0){
            DecisionTreeNode newNode = new DecisionTreeNode();
            newNode.setElement(theData.getClassifications()[1]);
            return newNode;
        }
        // if the attribute list is 0, create a new node whose element is the most frequent classification of parent **/
        else if(theData.getAttributeList().size() == 0){
            DecisionTreeNode newNode = new DecisionTreeNode();
            newNode.setElement(this.getFClass(parentData));
            return newNode;
        }
        else{
            // find the attribute with the greatest information gain **/
            Attribute bestAttribute = this.getImportance(theData, attributes);
            DecisionTreeNode tree = new DecisionTreeNode();
            // create a root node with with this attribute **/
            tree.setElement(bestAttribute.getName());

            //  loop through all possible values of said attribute and create a subset of the examples containing said value **/
            for(String val: bestAttribute.getValues()){
                String[][] subSet = this.subSet(val, theData.getExamples(), bestAttribute);
                // Create a new data set with the subset and pass in the same attributes minus the best attribute **/
                DecisionTreeData newData = new DecisionTreeData(subSet, attributes, theData.getClassifications());
                ArrayList<Attribute> newAttributes = newData.getAttributeList();
                newAttributes.remove(bestAttribute);

                // recursively call the function with the new data and add each node to the root node of each iteration **/
                DecisionTreeNode subtree = myID3Algorithm(newData, theData, newAttributes);
                tree.addChild(val, subtree);
            }
            return tree;
        }


    }

    /**
     * getImportance: data, attribute list -> attribute
     Purpose: returns the attribute with the most information gain
     Consumes: data, attribute list
     Produces: bestAttribute
     **/

    public Attribute getImportance(DecisionTreeData data, ArrayList<Attribute> attributes){
        String[][] examples = data.getExamples();

        String[] diffClass = data.getClassifications();
        double maxImportance = 0;
        Attribute bestAttribute = attributes.get(0);

        // iterate through the examples and calculate importance factor **/
        for(int i = 0; i < examples[0].length - 1; i++){
             Attribute currentAtt = attributes.get(i);
             ArrayList<Integer>[] infoArray = getInfo(currentAtt, examples);

             // calculate entropy of the current examples and calculate the remainder and calculate importance by entropy - remainder **/
             double importanceFactor = this.getEntropy(examples, infoArray[0], diffClass,2)[0] - this.calcRemainder(infoArray, currentAtt,diffClass,examples);

             //  find largest importance **/
             if(importanceFactor > maxImportance){
                 maxImportance = importanceFactor;
                 bestAttribute = currentAtt;
             }


        }
        // return attribute with biggest importance/information gain **/
        return bestAttribute;
    }


    /**
     * getInfo: attribute, examples -> ArrayList
     Purpose: returns an ArrayList with subsets for all values of an attribute and their respective proportions
     Consumes: current Attribute, examples
     Produces: ArrayList<Integer>
     **/

    private ArrayList<Integer>[] getInfo(Attribute currentAtt, String[][] examples){
        // get column of attribute **/
        int column = currentAtt.getColumn();

        // create an arrayList with capacity of number of different values **/
        ArrayList<Integer>[] infoArray = new ArrayList[currentAtt.getValues().size()];

        // fill each space in array with another array **/
        for(int j = 0; j < currentAtt.getValues().size(); j++){
            infoArray[j] = new ArrayList<>();
        }

        // counter is used to fill infoArray **/
        int counter = 0;
        // iterate through values and fill their respective spot in infoArray with the indices of the examples that have said value **/
        for(String val: currentAtt.getValues()){
            // value counter counts how many of each value exists in the examples **/
            int valueCounter = 0;
            for(int i = 0; i < examples.length; i++){
                if(examples[i][column].equals(val)){
                    valueCounter++;
                    infoArray[counter].add(i);
                }
            }
            infoArray[counter].add(valueCounter);
            counter++;
        }
        return infoArray;
    }

    /**
     * calcRemainder: ArrayList, attribute, class list, examples -> double
     Purpose: calculates the remainder
     Consumes: infoArray, currentAtt, diffClass, examples
     Produces: double
     **/

    private double calcRemainder(ArrayList<Integer>[] infoArray, Attribute currentAtt, String[] diffClass, String[][] examples){
        /** keeps track of the total remainder **/
        double sum = 0;
        /** iterate through the values and access their respective data from infoArray to calculate each value's entropy **/
        for(int i = 0; i < currentAtt.getValues().size(); i++){
            /** use getEntropy to calculate each value's entropy. access their proportion as well **/
            double valEntropy = this.getEntropy(examples, infoArray[i], diffClass, 1)[0];
            sum+= valEntropy * ((double)infoArray[i].get(infoArray[i].size()-1)/examples.length);
        }
        return sum;
    }

    /**
     * subSet: String, String[][], Attribute -> String[][]
     Purpose: returns a subset of examples that have a certain value
     Consumes: val, examples, attribute
     Produces: array with new set of examples from value given
     **/

    private String[][] subSet(String val, String[][] examples, Attribute attribute){
        /** how many attributes will there be and therefore columns ? **/
        int numAttributes = this.getCapacity(examples);
        String[][] subset = new String[examples.length][examples[0].length];
        int column = attribute.getColumn();
        int numExamples = 0;

        /** how many examples will there be and therefore rows? Also copies examples related to values  **/
        for (String[] example : examples) {
            if(example[column].equals(val)){
                subset[numExamples] = example;
                numExamples++;
            }
        }

        /** create a new array with previously calculated capacities **/

        String[][] realSubset = new String[numExamples][numAttributes];

        /** copy values into realSubSet **/
        for(int i = 0; i < numExamples; i++){
            for(int j = 0; j < numAttributes; j++){
                realSubset[i][j] = subset[i][j];
            }
        }

        return realSubset;
    }


    /**
     * getEntropy: String[][], ArrayList<Integer>, String[], int -> double[]
     Purpose: returns double with columns 1 and 2 representing positive and negative classifications respectively
     Consumes: examples, subSet, diffClass, int
     Produces: double[]
     **/

    private double[] getEntropy(String[][] examples, ArrayList<Integer> subSet, String[] diffClass, int close){
        double p = 0;
        double n = 0;
        double[] entropyArray = new double[2];

        // close integer differentiates between a subset and an examples set so it doesn't rely on attribute column **/
        if(close == 1){
            // iterate through subset and calculate p and n **/
            for(int i = 0; i < subSet.size() - 1; i++){
                if(examples[subSet.get(i)][examples[i].length - 1].equals(diffClass[0])){
                    p++;
                }
                else{
                    n++;
                }
            }
        }
        else{
            // calculate examples p and n **/
            for(int i = 0; i < examples.length; i++) {
                if(examples[i][examples[i].length - 1] == null){
                    entropyArray[0] = 1;
                    entropyArray[1] = 1;
                    return entropyArray;
                }
                if(examples[i][examples[i].length-1].equals(diffClass[0])) {
                    p++;

                }
                else{
                    n++;
                }
            }
        }
        /** calculate q and then entropy **/
        double q  = p/(p+n);
        double entropy = -(q*this.getLog(q)+(1-q)*this.getLog((1-q)));
        entropyArray[0] = entropy;

        /** add to the end which classification is most present **/
        if(p > n){
            entropyArray[1] = 0;
        }
        else{
            entropyArray[1] = 1;
        }
        return entropyArray;
    }

    /**
     * getLog: double -> double
     Purpose: calculate logarithm with base 2
     Consumes: double
     Produces: calculate logarithm in double
     **/
    public double getLog(double logNumber){
        if(logNumber == 0){
            return 0;
        }
        return Math.log(logNumber)/Math.log(2);
    }

    /**
     * getCapacity: String[][] -> int
     Purpose: returns the number of attributes in an example set
     Consumes: example set
     Produces: int
     **/
    public int getCapacity(String[][] examples){

        int attributes = 0;
        for(int i = 0; i < examples[0].length - 1; i++){
            if(examples[0][i] != null){
                attributes++;
            }
        }
        return attributes;
    }

    /**
     * getFClass: data -> String
     Purpose: returns the string representing the most frequent classification in an example list
     Consumes: data
     Produces: String classification
     **/
    public String getFClass(DecisionTreeData data){
        String[] cLass = data.getClassifications();
        int counter = 0;
        int max = 0;
        String maxClass = cLass[0];
        for(String classification: cLass){
            for(int i = 0; i < data.getExamples().length; i++){
                if(data.getExamples()[i][data.getExamples()[i].length - 1].equals(classification)){
                    counter++;
                }
            }
            if(counter > max){
                max = counter;
                maxClass = classification;
            }
        }
        return maxClass;
    }

}
