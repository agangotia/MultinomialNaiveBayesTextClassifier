package com.anupam.naivebayes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/**
 * @author Anupam Gangotia
 * Profile::http://en.gravatar.com/gangotia
 * github::https://github.com/agangotia
 */


/**
 *Class Naive Bayes
 *Binary classifier
 *For Understanding: Yi and Yj be the two classes.
 *There are n total training instances
 *Ni = training instances classified as Yi
 *Nj = training instances classified as Yj
 *   Ni+Nj=n
 * V = unique words in all the intances 
 */
public class NaiveBayes {
	MatrixData matrixData;
	
	private int totalTrainingInstances;//N
	private int TrainingInstancesClassifiedTrue;//Ni
	private int TrainingInstancesClassifiedFalse;//Nj
	private int uniqueWordsInAllTrainingInstances;//Vocabulary V
	
	/**
	 * Word and its count in class True
	 * count(w,Yi)
	 * */
	private HashMap<String,Integer> numAttrClassifiedTrue;
	
	/**
	 * Word and its count in class False
	 * count(w,Yj)
	 * */
	private HashMap<String,Integer> numAttrClassifiedFalse;

	private int totalClassifiesTrue;//Total words in CLass Yi
	private int totalClassifiesFalse;//Total words in Class Yj
	
	public NaiveBayes(MatrixData matrixData){
		if(matrixData==null)
			System.out.print("error");
		
		numAttrClassifiedTrue=new HashMap<String,Integer>();
		numAttrClassifiedFalse=new HashMap<String,Integer>();
		this.matrixData=matrixData;
		
		totalClassifiesTrue=0;
		totalClassifiesFalse=0;
		totalTrainingInstances=0;
		TrainingInstancesClassifiedTrue=0;
		TrainingInstancesClassifiedFalse=0;
		uniqueWordsInAllTrainingInstances=0;
		
	
		
		this.matrixData.fillMapClassifiedAs(numAttrClassifiedTrue, 1);
		this.matrixData.fillMapClassifiedAs(numAttrClassifiedFalse, 0);
		
		for (Map.Entry entry : numAttrClassifiedTrue.entrySet()) {
			totalClassifiesTrue+=(int) entry.getValue();
		}
		
		for (Map.Entry entry : numAttrClassifiedFalse.entrySet()) {
			totalClassifiesFalse+=(int) entry.getValue();
		}
		
		totalTrainingInstances=matrixData.Numrows;
		TrainingInstancesClassifiedTrue=this.matrixData.getTrainingInstancesClassifiedAs(1);
		TrainingInstancesClassifiedFalse=this.matrixData.getTrainingInstancesClassifiedAs(0);
		uniqueWordsInAllTrainingInstances=matrixData.coloumns-1;
		printInitStage();
		
	}
	
	public void printInitStage(){
		
		System.out.println("totalTrainingInstances"+totalTrainingInstances);
		System.out.println("TrainingInstancesClassifiedTrue"+TrainingInstancesClassifiedTrue);
		System.out.println("TrainingInstancesClassifiedFalse"+TrainingInstancesClassifiedFalse);
		System.out.println("uniqueWordsInAllTrainingInstances"+uniqueWordsInAllTrainingInstances);
		System.out.println("totalClassifiesTrue"+totalClassifiesTrue);
		System.out.println("totalClassifiesFalse"+totalClassifiesFalse);
		

	}
	
	public HashMap<String, Integer> getNumAttrClassifiedTrue() {
		return numAttrClassifiedTrue;
	}

	public void setNumAttrClassifiedTrue(
			HashMap<String, Integer> numAttrClassifiedTrue) {
		this.numAttrClassifiedTrue = numAttrClassifiedTrue;

	}

	public HashMap<String, Integer> getNumAttrClassifiedFalse() {
		return numAttrClassifiedFalse;
	}

	public void setNumAttrClassifiedFalse(
			HashMap<String, Integer> numAttrClassifiedFalse) {
		this.numAttrClassifiedFalse = numAttrClassifiedFalse;
	
	}
	
	/**
	 *Function getProbClassifiedTrue()
	 *@param instance This is the lists of words in Test instance
	 *@return the probability of given instance classified as Class
	 */
	private double getProbClassifiedTrue(ArrayList<String> instance){
		
		double prob=log((double)TrainingInstancesClassifiedTrue/totalTrainingInstances);
		double[] individualProbs=new double[instance.size()];
		int index=0;
		for(String s:instance){
			if(numAttrClassifiedTrue.containsKey(s)){//If there are zero attributes classified as zero
				individualProbs[index]=numAttrClassifiedTrue.get(s);
			}else{
				individualProbs[index]=0;
			}
			index++;
		}
		
		for(index=0;index<instance.size();index++){
			prob+=log((individualProbs[index]+1)/(totalClassifiesTrue+uniqueWordsInAllTrainingInstances));
		}
		return prob;
	}

	/**
	 *Function getProbClassifiedFalse()
	 *@param instance This is the lists of words in Test instance
	 *@return the probability of given instance classified as !Class
	 */
	private double getProbClassifiedFalse(ArrayList<String> instance){

		double prob=log((double)TrainingInstancesClassifiedFalse/totalTrainingInstances);
		double[] individualProbs=new double[instance.size()];
		int index=0;
		for(String s:instance){
			if(numAttrClassifiedFalse.containsKey(s)){
				individualProbs[index]=numAttrClassifiedFalse.get(s);
				
			}else{
				individualProbs[index]=0;
			}
			index++;
		}
		
		
		for(index=0;index<instance.size();index++){
			prob+=log((individualProbs[index]+1)/(totalClassifiesFalse+uniqueWordsInAllTrainingInstances));
	
		}
	
		return prob;
	}
	
	
	/**
	 *Function getProbClassified()
	 *@param instance This is the lists of words in Test instance, which we want to predict the class
	 *@return value
	 */
	public Decision getProbClassified(ArrayList<String> instance){
		
		double pTrue=getProbClassifiedTrue(instance);
		double pFalse=getProbClassifiedFalse(instance);
		if(pTrue>pFalse){
			Decision a=new Decision();
			a.classification=true;
			a.Prob=pTrue;
			return a;
		}
		else
		{
			Decision a=new Decision();
			a.classification=false;
			a.Prob=pFalse;
			return a;
		}
	}
	

	public static void main(String[] args){
		MatrixData matrixSource=new MatrixData();
		matrixSource.prepareMatrix("data\\SpamTrain.csv", 100);
		matrixSource.printMatrix();
		
		ArrayList<String> TestInstance=new ArrayList<String>();
		TestInstance.add("chinese");
		TestInstance.add("chinese");
		TestInstance.add("chinese");
		TestInstance.add("tokyo");
		TestInstance.add("japan");
		
		NaiveBayes nbNode=new NaiveBayes(matrixSource);
		Decision a=nbNode.getProbClassified(TestInstance);
		System.out.println("The result is"+a.classification);
		System.out.println("The result is"+a.Prob);
	}
	
	/**
	 * Function : log
	 * custom function that returns log 0 = 0
	 */
	public static double log(double num) {
		if (num == 0)
			return 0.0;
		return (Math.log(num)/Math.log(10));
	}
}
