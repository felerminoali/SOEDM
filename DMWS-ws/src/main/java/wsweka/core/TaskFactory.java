package wsweka.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TaskFactory {

	public Task createTask(String input) {

		Task newTask = null;

		

		Vector<String> taskOptions = new Vector<String>();
		Vector<String> filterOptions = new Vector<String>();
		Vector<String> evaluationOption  = new Vector<String>();
		
		
		// Splitting the input into six parts
//		String args[] = input.split("\\s");
//		for (int i = 0; i < args.length; i++) {
//			System.out.println(args[i]);
//		}
		
		List<String> matchList = new ArrayList<String>();
		Pattern regex = Pattern.compile("[^\\s\"']+|\"[^\"]*\"|'[^']*'");
		Matcher regexMatcher = regex.matcher(input);
		while (regexMatcher.find()) {
		    matchList.add(regexMatcher.group());
		  
		} 
		System.out.println(matchList);
		
		String args[] = new String[matchList.size()];
		for (int i = 0; i < matchList.size(); i++) {
			args[i] = (matchList.get(i).replace("\"", "")).replace("\\", "");
		}
		
		System.out.println(args);
		
//		String args[] = {"CLASSIFIER", "weka.classifiers.meta.FilteredClassifier", "-F", "weka.filters.supervised.attribute.Discretize -R first-last" ,"-W", "weka.classifiers.trees.J48", "--", "-C","0.25", "-M", "2"};
		
		
		int i = 0;

		String current = "";
		boolean newPart = false;

		// to check if has an filter
		boolean isFiltered = false;

		//Input example:
		//		CLASSIFIER weka.classifiers.trees.J48 -U FILTER weka.filters.unsupervised.instance.Randomize
		//		CLASSIFIER weka.classifiers.bayes.NaiveBayes
		
		boolean isClassifier= false;
		boolean isCluster = false;
		boolean isAssociation = false;
		
		String classifier = "";
		String clusterer ="";
		String associator ="";
		String filter = "";
		String evaluation = null;
		
		do {

			// determine part of command line
			if (args[i].equals("CLASSIFIER") 
					|| args[i].equals("CLUSTER") 
					|| args[i].equals("ASSOCIATION")) {
				
				System.out.println(args[i]);
				
				if(args[i].equals("CLASSIFIER")){
					isClassifier= true;
				}else if(args[i].equals("CLUSTER")){
					isCluster = true;
				}else if(args[i].equals("ASSOCIATION")){
					isAssociation = true;
				}
				
				current = args[i];
				i++;
				newPart = true;
			} else if (args[i].equals("FILTER")) {
				current = args[i];
				i++;
				newPart = true;
			} else if (args[i].equals("TEST-OPTIONS")) {
				current = args[i];
				i++;
				newPart = true;
			} 
			
			

			if (current.equals("CLASSIFIER")|| current.equals("CLUSTER") || current.equals("ASSOCIATION")) {
				if (newPart) {
					if(isClassifier){
						classifier = args[i];
					}
					
					if(isCluster){
						clusterer = args[i];
					}
					
					if(isAssociation){
						associator = args[i];
					}
				} else {
					taskOptions.add(args[i]);
				}

			} else if (current.equals("FILTER")) {
				if (newPart) {
					filter = args[i];
					isFiltered = true;
				} else {
					filterOptions.add(args[i]);
				}
			} else if (current.equals("TEST-OPTIONS")) {
				if (newPart) {
					evaluation = args[i];
				} else {
					evaluationOption.add(args[i]);
				}
				
			}
			
			// next parameter
			i++;
			newPart = false;

		} while (i < args.length);

		System.out.println("task option: "+taskOptions.toString());
		System.out.println("filter : "+filter+" opt: "+filterOptions.toString());
		System.out.println("Test opt: "+evaluation+" opt: "+evaluationOption.toString());
		
		if (isClassifier) {
			if(isFiltered){
				return new ClassificationTask(classifier, taskOptions, filter, filterOptions, evaluation, evaluationOption);
			}else{
				return new ClassificationTask(classifier, taskOptions,evaluation,evaluationOption);
			}
		} else if (isCluster) {
			if(isFiltered){
				return new ClusterTask(clusterer, taskOptions, filter, filterOptions, evaluation, evaluationOption);
			}else{
				return new ClusterTask(clusterer, taskOptions,evaluation,evaluationOption);
			}
		} else if (isAssociation) {
			if(isFiltered){
				return new AssociationTask(associator, taskOptions, filter, filterOptions);
			}else{
				return new AssociationTask(associator, taskOptions);
			}
		}

		return newTask;

	}
	
	
}
