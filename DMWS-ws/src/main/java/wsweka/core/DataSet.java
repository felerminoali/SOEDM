package wsweka.core;

import java.util.Vector;


import weka.core.Attribute;
import weka.core.Instances;
import weka.core.OptionHandler;
import weka.filters.Filter;

public class DataSet {

	private Instances instances = null;

	// Strategy pattern: different algorithm {Classifier, Cluster or Association}
	public Task task;

	public DataSet() {
	}

	public DataSet(Instances instances) {
		this.instances = instances;
	}

	public String TryToBuildModel() throws Exception {
		return this.task.buildModel(getInstances());
	}
	
	public String TryVisualizeGraph() throws Exception {
		return ((ClassificationTask)this.task).buildModelVisualizeGraph(getInstances());
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public Instances getInstances() {
		return instances;
	}

	public String filter(String input) throws Exception {

		String filter = "";
		Vector<String> filterOptions = new Vector<String>();

		String args[] = input.split("\\s");

		int i = 0;
		String current = "";
		boolean newPart = false;

		do {

			// determine part of command line
			if (args[i].equals("FILTER")) {
				current = args[i];
				i++;
				newPart = true;
			}

			//
			if (current.equals("FILTER")) {
				if (newPart) {
					filter = args[i];
				} else {
					filterOptions.add(args[i]);
				}
			}

			// next parameter
			i++;
			newPart = false;

		} while (i < args.length);

		System.out.println("filter: " + filter + " " + " options: "
				+ filterOptions.toString());
		// the filter to use
		Filter m_filter = null;

		m_filter = (Filter) Class.forName(filter).newInstance();

		if (m_filter instanceof OptionHandler) {
			try {
				((OptionHandler) m_filter).setOptions((String[]) filterOptions
						.toArray(new String[filterOptions.size()]));
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

		}
		m_filter.setInputFormat(getInstances());
		Instances filtered = Filter.useFilter(getInstances(), m_filter);
		this.instances = filtered;

		System.out.println(this.instances.toString());
		return "filter: " + filter + " " + " options: "
				+ filterOptions.toString();

	}
/* 
 * Discretize data set accordingly with given attributes and class
 * 
 * */
	public String manualDescritezation(String input) throws Exception {

		String args[] = input.split("\\s");

		int i = 0;
		String current = "";

		Vector<String> attrCategories = new Vector<String>();
		Vector<String> classCategories = new Vector<String>();
		do {

			// determine part of command line
			if (args[i].equals("-A")) { // attributes option
				current = args[i];
				i++;
			} else if (args[i].equals("-C")) { // class option
				current = args[i];
				i++;
			}

			//
			if (current.equals("-A")) {
				attrCategories.add(args[i]); // retrieving attribute categories
			} else if (current.equals("-C")) {
				classCategories.add(args[i]); // retrieving class categories
			}

			// next parameter
			i++;

		} while (i < args.length);

		// discretize first all attributes expect the class
		filter("FILTER weka.filters.unsupervised.attribute.Discretize -B "
				+ attrCategories.size() + " -R " + "first-"
				+ (instances.numAttributes() - 1));

		// discretize the class
		filter("FILTER weka.filters.unsupervised.attribute.Discretize -B "
				+ classCategories.size() + " -R " + "first-"
				+ (instances.numAttributes()));

		// Rename the all attributes with given categories
		renameAttributes(attrCategories);

		// Rename the class attribute with given categories
		renameOneAttribute(classCategories, instances.numAttributes());

		return "attr : " + attrCategories.toString() + " " + " class: "
				+ classCategories.toString();
	}

	/*
	 * Rename all attributes except class attribute with given categories
	 * */
	private void renameAttributes(Vector<String> categories) {

//		instances.setClassIndex(instances.numAttributes() - 1);

		Integer[] indices = null;

		Vector<Integer> v = new Vector<Integer>();
		for (int i = 0; i < instances.numAttributes() - 1; i++) {
			if (instances.attribute(i).isNominal())
				v.add(new Integer(i));
		}
		indices = (Integer[]) v.toArray(new Integer[v.size()]);

		for (int i = 0; i < indices.length; i++) {
			int attInd = indices[i].intValue();
			Attribute att = instances.attribute(attInd);
			for (int n = 0; n < att.numValues(); n++) {
				instances.renameAttributeValue(att, att.value(n),
						categories.get(n));
			}
		}

		System.out.println(instances.toString());

	}

	/*
	 * Rename one attribute with given categories and attribute index
	 * */
	private void renameOneAttribute(Vector<String> categories, int index) {

//		instances.setClassIndex(instances.numAttributes() - 1);

		Integer[] indices = null;

		indices = new Integer[1];
		indices[0] = new Integer(index - 1);

		for (int i = 0; i < indices.length; i++) {
			int attInd = indices[i].intValue();
			Attribute att = instances.attribute(attInd);
			for (int n = 0; n < att.numValues(); n++) {
				instances.renameAttributeValue(att, att.value(n),
						categories.get(n));
			}
		}

		System.out.println(instances.toString());

	}
	
	
 

//	public static void main(String[] args) throws Exception {
//
//		System.out.println(new DataSet()
//				.manualDescritezation("-A low medium high -C fail pass"));
//	}
}
