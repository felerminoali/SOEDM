package wsweka;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Vector;

import javax.xml.bind.JAXBException;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.OptionHandler;
import weka.core.Utils;
import weka.filters.Filter;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.unsupervised.attribute.Remove;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.J48;
import wsweka.core.ClassificationTask;
import wsweka.core.DataSet;
import wsweka.core.Task;
import wsweka.core.TaskFactory;
import wsweka.core.converts.ArffDataSource;
import wsweka.core.converts.MoodleDataSource;

public class MethodsUtility {

    public String execute(String input) throws Exception {

        AbstractClassifier m_Classifier = null;

        // the filter to use
        Filter m_filter = null;

        // the training file
        String m_TrainingFile = null;

        // the training instance
        Instances m_Training = null;

        // for evaluating the classifier
        Evaluation m_Evalution = null;

        String classifier = "";
        String filter = "";
        String dataset = "";

        Vector<String> classifierOptions = new Vector<String>();
        Vector<String> filterOptions = new Vector<String>();

        System.out.println(input);

        // splintting the input into six parts
        String args[] = input.split("\\s");
        for (int i = 0; i < args.length; i++) {
            System.out.println(args[i]);
        }

        int i = 0;

        String current = "";
        boolean newPart = false;

        // to check if has an filter
        boolean isFiltered = false;

        do {

            // determine part of command line
            if (args[i].equals("CLASSIFIER")) {
                System.out.println(args[i]);
                current = args[i];
                i++;
                newPart = true;
            } else if (args[i].equals("FILTER")) {
                current = args[i];
                i++;
                newPart = true;
            } else if (args[i].equals("DATASET")) {
                current = args[i];
                i++;
                newPart = true;
            }

            //
            if (current.equals("CLASSIFIER")) {
                if (newPart) {
                    classifier = args[i];
                } else {
                    classifierOptions.add(args[i]);
                }

            } else if (current.equals("FILTER")) {
                if (newPart) {
                    filter = args[i];
                    isFiltered = true;
                } else {
                    filterOptions.add(args[i]);
                }
            } else if (current.equals("DATASET")) {
                if (newPart) {
                    dataset = args[i];
                }
            }

            // next parameter
            i++;
            newPart = false;

        } while (i < args.length);

        m_Classifier = (AbstractClassifier) AbstractClassifier.forName(
                classifier, (String[]) classifierOptions
                .toArray(new String[classifierOptions.size()]));

		// if there any filter
        // if(!filter.isEmpty()){
        // m_filter = (Filter) Class.forName(filter).newInstance();
        //
        // if (m_filter instanceof OptionHandler) {
        // ((OptionHandler) m_filter).setOptions((String[]) filterOptions
        // .toArray(new String[filterOptions.size()]));
        // }
        // }
        // gathering data from path data source
        m_TrainingFile = dataset;
        m_Training = new weka.core.Instances(new BufferedReader(new FileReader(
                m_TrainingFile)));
        // class attribute
        m_Training.setClassIndex(m_Training.numAttributes() - 1);

		// run filter
        if (isFiltered) {
            m_filter = (Filter) Class.forName(filter).newInstance();

            if (m_filter instanceof OptionHandler) {
                ((OptionHandler) m_filter).setOptions((String[]) filterOptions
                        .toArray(new String[filterOptions.size()]));
            }
            m_filter.setInputFormat(m_Training);
            Instances filtered = Filter.useFilter(m_Training, m_filter);
            m_Training = filtered;
        }

		// train classifier on complete file for tree
        // m_Classifier.buildClassifier(filtered);
        m_Classifier.buildClassifier(m_Training);

		// 10fold CV with seed=1
        // m_Evalution = new Evaluation(filtered);
        m_Evalution = new Evaluation(m_Training);
		// m_Evalution.crossValidateModel(m_Classifier, filtered, 10,
        // m_Training.getRandomNumberGenerator(1));
        m_Evalution.crossValidateModel(m_Classifier, m_Training, 10,
                m_Training.getRandomNumberGenerator(1));

        StringBuffer result;

        result = new StringBuffer();
        result.append("Weka - Demo\n==================\n\n");
        result.append("Classifier....: " + m_Classifier.getClass().getName()
                + " " + Utils.joinOptions(m_Classifier.getOptions()) + "\n");

        if (isFiltered) {
            if (m_filter instanceof OptionHandler) {
                result.append("Filter....: "
                        + m_filter.getClass().getName()
                        + " "
                        + Utils.joinOptions(((OptionHandler) m_filter)
                                .getOptions()) + "\n");
            } else {
                result.append("Filter....: " + m_filter.getClass().getName()
                        + "\n");
            }
        }

        result.append("Training file: " + m_TrainingFile + "\n");
        result.append("\n");
        result.append(m_Classifier.toString() + "\n");
        result.append(m_Evalution.toSummaryString() + "\n");

        try {
            result.append(m_Evalution.toMatrixString() + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    public String treeClassifer(String url) throws Exception {

		// load dataset
        DataSource source = new DataSource(
                "G:/desktop/dummy_data/interdata6/Summary_InterDataCategorical_TwoClass.arff");
        Instances dataset = source.getDataSet();
        // set class index to the last attribute
        dataset.setClassIndex(dataset.numAttributes() - 1);

        // the base classifier
        J48 tree = new J48();
        // the filter
        Remove remove = new Remove();
        // remove.setAttributeIndices("1");
        String[] opts = new String[]{"-R", "1"};
        // set the filter options
        remove.setOptions(opts);

        // Create the FilteredClassifier object
        FilteredClassifier fc = new FilteredClassifier();
        // specify filter
        fc.setFilter(remove);
        // specify base classifier
        fc.setClassifier(tree);
        // Build the meta-classifier
        fc.buildClassifier(dataset);

        return tree.graph().toString();

        // System.out.println(tree.graph());
    }

    public String xmlToArrf(String xml) throws JAXBException {

        try {

            MoodleDataSource data = new MoodleDataSource(xml);

            String result = data.getDataSet().toString();

            return result;

        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String executeTwo(String xml, String optTask) throws Exception {

        String result = "";

        MoodleDataSource data = new MoodleDataSource(xml);

        DataSet dataset = new DataSet(data.getDataSet());

        TaskFactory taskFactory = new TaskFactory();

        // Factory pattern
        Task task = taskFactory.createTask(optTask);

        // strategy pattern
        dataset.setTask(task);

        if (task != null) {
            result = dataset.TryToBuildModel();
        }

        return result;
    }

    public String executeTwo(String xml, String optTask, String discritizeOpts)
            throws Exception {

        String result = "";

        MoodleDataSource data = new MoodleDataSource(xml);

        DataSet dataset = new DataSet(data.getDataSet());

        // discretizing data
        dataset.filter(discritizeOpts);

        // Factory pattern
        TaskFactory taskFactory = new TaskFactory();
		//
        // Factory pattern: return class object according the options given
        Task task = taskFactory.createTask(optTask);
        //
        dataset.setTask(task);
        //
        if (task != null) {
            result = dataset.TryToBuildModel();
        }

        return result;
    }

    public String executeTo(String xml, String optTask, String discritizeOpts)
            throws Exception {
        String result = "";

        MoodleDataSource data = new MoodleDataSource(xml);

        DataSet dataset = new DataSet(data.getDataSet());

        // discretizing data
        dataset.manualDescritezation(discritizeOpts);

        // Factory pattern
        TaskFactory taskFactory = new TaskFactory();

        // Factory pattern: return class object according the options given
        Task task = taskFactory.createTask(optTask);
        //
        dataset.setTask(task);

        if (task != null) {
            result = dataset.TryToBuildModel();
        }

        return result;
    }

	// ============================================================================
    // Final web methods
    public String executeFilter(String arff, String filter) throws Exception {
        String result = "";

        ArffDataSource data = new ArffDataSource(arff);

        DataSet dataset = new DataSet(data.getDataset());

        dataset.filter(filter);

        result = dataset.getInstances().toString();
        return result;

    }

	// manual descriteze
    public String executeManualDescritezation(String arff, String filter)
            throws Exception {
        String result = "";

        ArffDataSource data = new ArffDataSource(arff);

        DataSet dataset = new DataSet(data.getDataset());

        dataset.manualDescritezation(filter);

        result = dataset.getInstances().toString();
        return result;

    }

	// execute classifier
    public String executeClassifier(String arff, String classifier,
            String evaluation) throws Exception {
        String optTask = "";
        if (evaluation != null) {
            optTask += "CLASSIFIER " + classifier + " TEST-OPTIONS " + evaluation;
        } else {
            optTask += "CLASSIFIER " + classifier;

        }

        String result = "";
        ArffDataSource data = new ArffDataSource(arff);

        DataSet dataset = new DataSet(data.getDataset());

        TaskFactory taskFactory = new TaskFactory();

        // Factory pattern
        Task task = taskFactory.createTask(optTask);

        // strategy pattern
        dataset.setTask(task);

        if (task != null) {
            result = dataset.TryToBuildModel();
        }

        return result;

    }

	// execute cluster
    public String executeCluster(String arff, String cluster,
            String evaluation) throws Exception {
        String optTask = "";
        if (evaluation != null) {
            optTask += "CLUSTER " + cluster + " TEST-OPTIONS " + evaluation;
        } else {
            optTask += "CLUSTER " + cluster;

        }

        String result = "";
        ArffDataSource data = new ArffDataSource(arff);

        DataSet dataset = new DataSet(data.getDataset());

        TaskFactory taskFactory = new TaskFactory();

        // Factory pattern
        Task task = taskFactory.createTask(optTask);

        // strategy pattern
        dataset.setTask(task);

        if (task != null) {
            result = dataset.TryToBuildModel();
        }

        return result;

    }

		//  execute associate
    public String executeAssociate(String arff, String associator) throws Exception {
        String optTask = "";

        optTask += "ASSOCIATION " + associator;

        String result = "";
        ArffDataSource data = new ArffDataSource(arff);

        DataSet dataset = new DataSet(data.getDataset());

        TaskFactory taskFactory = new TaskFactory();

        // Factory pattern
        Task task = taskFactory.createTask(optTask);

        // strategy pattern
        dataset.setTask(task);

        if (task != null) {
            result = dataset.TryToBuildModel();
        }

        return result;

    }

    public String visualizeTreeGraph(String arff, String classifier,
            String evaluation) throws Exception {
        String optTask = "";
        if (evaluation != null) {
            optTask += "CLASSIFIER " + classifier + " TEST-OPTIONS " + evaluation;
        } else {
            optTask += "CLASSIFIER " + classifier;

        }

        String result = "";
        ArffDataSource data = new ArffDataSource(arff);

        DataSet dataset = new DataSet(data.getDataset());

        TaskFactory taskFactory = new TaskFactory();

        // Factory pattern
        Task task = taskFactory.createTask(optTask);

//			ClassificationTask c = (ClassificationTask) task;
        // strategy pattern
        dataset.setTask(task);

        if (task != null) {
            result = dataset.TryVisualizeGraph();
        }

        System.out.println(result);
        return result;

    }

}
