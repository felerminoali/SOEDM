package wsweka.core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Vector;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.Evaluation;
import weka.core.Drawable;
import weka.core.Instances;
import weka.core.OptionHandler;
import weka.core.Utils;
import weka.filters.Filter;

public class ClassificationTask extends Task {

    private String classifier = null;
    private String filter = null;
    private String evaluation = null;

    private Vector<String> classifierOptions = new Vector<String>();
    private Vector<String> filterOptions = new Vector<String>();
    Vector<String> evaluationOption = new Vector<String>();

    public ClassificationTask(String classifier,
            Vector<String> classifierOptions, String filter,
            Vector<String> filterOptions, String evaluation,
            Vector<String> evaluationOption) {
        this.classifier = classifier;
        this.classifierOptions = classifierOptions;
        this.filter = filter;
        this.filterOptions = filterOptions;
        this.evaluation = evaluation;
        this.evaluationOption = evaluationOption;
    }

    public ClassificationTask(String classifier,
            Vector<String> classifierOptions, String evaluation,
            Vector<String> evaluationOption) {
        this.classifier = classifier;
        this.classifierOptions = classifierOptions;
        this.evaluation = evaluation;
        this.evaluationOption = evaluationOption;
    }

    @Override
    public String buildModel(Instances instances) throws Exception {

        try {

            AbstractClassifier m_Classifier = null;

            // the filter to use
            Filter m_filter = null;

			// // the training file
            // String m_TrainingFile = null;
            // the training instance
            Instances m_Training = null;

            // for evaluating the classifier
            Evaluation m_Evalution = null;

            m_Classifier = (AbstractClassifier) AbstractClassifier
                    .forName(this.classifier, (String[]) this.classifierOptions
                            .toArray(new String[this.classifierOptions.size()]));

            // gathering data from path data source
            m_Training = instances;
			// m_Training = new weka.core.Instances(new BufferedReader(new
            // FileReader(
            // "G:/OneDrive/Documentos/WekaDataset/iris.arff")));
            // class attribute
            m_Training.setClassIndex(m_Training.numAttributes() - 1);

			// run filter
            if (this.filter != null) {
                m_filter = (Filter) Class.forName(this.filter).newInstance();

                if (m_filter instanceof OptionHandler) {
                    ((OptionHandler) m_filter)
                            .setOptions((String[]) this.filterOptions
                                    .toArray(new String[this.filterOptions
                                            .size()]));

//					((OptionHandler) m_filter).
                }
                m_filter.setInputFormat(m_Training);
                Instances filtered = Filter.useFilter(m_Training, m_filter);
                m_Training = filtered;
            }

            System.out.println(m_Training.toString());

			// Evaluation Cross-validation or for given percentage split
            // original dataset
            // into train and test sets
            if (this.evaluation != null) {

                if (this.evaluation.equals("cross-validation")) {

                    int i = 0;
                    int kfolds = 10;
                    int seed = 1;
                    m_Classifier.buildClassifier(m_Training);
                    do {
                        if (this.evaluationOption.get(i).equals("-f")) { // k-folds
                            // option
                            kfolds = Integer.parseInt(evaluationOption
                                    .get(i + 1));
                            i++;
                        }

                        if (this.evaluationOption.get(i).equals("-S")) { // Seed
                            // option
                            seed = Integer.parseInt(this.evaluationOption
                                    .get(i + 1));
                        }
                        i++;
                    } while (i < this.evaluationOption.size());

                    m_Evalution = new Evaluation(m_Training);
                    m_Evalution.crossValidateModel(m_Classifier, m_Training,
                            kfolds, m_Training.getRandomNumberGenerator(seed));

                } else if (this.evaluation.equals("cv-split")) {
                    int i = 0;
                    double percnt = 66.0;
                    int seed = 1;

                    do {
                        if (this.evaluationOption.get(i).equals("-Z")) { // percentage
                            // option
                            percnt = Double.parseDouble(this.evaluationOption
                                    .get(i + 1));
                            i++;
                        }

                        if (this.evaluationOption.get(i).equals("-S")) { // seed
                            // option
                            seed = Integer.parseInt(this.evaluationOption
                                    .get(i + 1));
                        }
                        i++;
                    } while (i < this.evaluationOption.size());

                    // split the original data set into two: train and test
                    String trainOptionFilter = "FILTER weka.filters.unsupervised.instance.Resample -S "
                            + seed + " -Z " + percnt + " -no-replacement";
                    String testOptionFilter = "FILTER weka.filters.unsupervised.instance.Resample -S "
                            + seed + " -Z " + percnt + " -no-replacement -V";

                    Instances train = resample(trainOptionFilter, m_Training);
                    Instances test = resample(testOptionFilter, m_Training);

                    System.out
                            .println(">>>>>>>>>>>>>>>>>>>>>>>>>>> oginal instances: "
                                    + m_Training.numInstances()
                                    + "| train after filter: "
                                    + train.numInstances()
                                    + "| test instances: "
                                    + test.numInstances());

                    m_Classifier.buildClassifier(train);
                    m_Evalution = new Evaluation(train);
                    m_Evalution.evaluateModel(m_Classifier, test);

                }

            } else { // default

                // Evaluation on training set
                m_Classifier.buildClassifier(m_Training);
                // 10fold CV with seed=1 will be the default evaluation method
                m_Evalution = new Evaluation(m_Training);
//				m_Evalution.crossValidateModel(m_Classifier, m_Training, 10,
//						m_Training.getRandomNumberGenerator(1));
                m_Evalution.evaluateModel(m_Classifier, m_Training);
            }

            StringBuffer result;

            result = new StringBuffer();
            result.append("Weka - Classification task\n==================\n\n");
            result.append("Classifier....: "
                    + m_Classifier.getClass().getName() + " "
                    + Utils.joinOptions(m_Classifier.getOptions()) + "\n");

            if (filter != null) {
                if (m_filter instanceof OptionHandler) {
                    result.append("Filter....: "
                            + m_filter.getClass().getName()
                            + " "
                            + Utils.joinOptions(((OptionHandler) m_filter)
                                    .getOptions()) + "\n");
                } else {
                    result.append("Filter....: "
                            + m_filter.getClass().getName() + "\n");
                }
            }

			// result.append("Training file: " + m_TrainingFile + "\n");
			// If is drawable tree 
//			try {
//				Drawable d = (Drawable) m_Classifier;
//				result.append(d);
//			} catch (Exception e) {
//			}
            result.append("\n");
            result.append(m_Classifier.toString() + "\n");

            try {
                result.append(m_Evalution.toSummaryString() + "\n");
                result.append(m_Evalution.toMatrixString() + "\n");
            } catch (Exception e) {
                e.printStackTrace();
            }

            return result.toString();

        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String buildModelVisualizeGraph(Instances instances) throws Exception {

        try {

            AbstractClassifier m_Classifier = null;

            // the filter to use
            Filter m_filter = null;

            // the training instance
            Instances m_Training = null;

            // for evaluating the classifier
            Evaluation m_Evalution = null;

            m_Classifier = (AbstractClassifier) AbstractClassifier
                    .forName(this.classifier, (String[]) this.classifierOptions
                            .toArray(new String[this.classifierOptions.size()]));

            // gathering data from path data source
            m_Training = instances;
            m_Training.setClassIndex(m_Training.numAttributes() - 1);

			// run filter
            if (this.filter != null) {
                m_filter = (Filter) Class.forName(this.filter).newInstance();

                if (m_filter instanceof OptionHandler) {
                    ((OptionHandler) m_filter)
                            .setOptions((String[]) this.filterOptions
                                    .toArray(new String[this.filterOptions
                                            .size()]));

                }
                m_filter.setInputFormat(m_Training);
                Instances filtered = Filter.useFilter(m_Training, m_filter);
                m_Training = filtered;
            }

            System.out.println(m_Training.toString());

			// Evaluation Cross-validation or for given percentage split
            // original dataset
            // into train and test sets
            if (this.evaluation != null) {

                if (this.evaluation.equals("cross-validation")) {

                    int i = 0;
                    int kfolds = 10;
                    int seed = 1;
                    m_Classifier.buildClassifier(m_Training);
                    do {
                        if (this.evaluationOption.get(i).equals("-f")) { // k-folds
                            // option
                            kfolds = Integer.parseInt(evaluationOption
                                    .get(i + 1));
                            i++;
                        }

                        if (this.evaluationOption.get(i).equals("-S")) { // Seed
                            // option
                            seed = Integer.parseInt(this.evaluationOption
                                    .get(i + 1));
                        }
                        i++;
                    } while (i < this.evaluationOption.size());

                    m_Evalution = new Evaluation(m_Training);
                    m_Evalution.crossValidateModel(m_Classifier, m_Training,
                            kfolds, m_Training.getRandomNumberGenerator(seed));

                } else if (this.evaluation.equals("cv-split")) {
                    int i = 0;
                    double percnt = 66.0;
                    int seed = 1;

                    do {
                        if (this.evaluationOption.get(i).equals("-Z")) { // percentage
                            // option
                            percnt = Double.parseDouble(this.evaluationOption
                                    .get(i + 1));
                            i++;
                        }

                        if (this.evaluationOption.get(i).equals("-S")) { // seed
                            // option
                            seed = Integer.parseInt(this.evaluationOption
                                    .get(i + 1));
                        }
                        i++;
                    } while (i < this.evaluationOption.size());

                    // split the original data set into two: train and test
                    String trainOptionFilter = "FILTER weka.filters.unsupervised.instance.Resample -S "
                            + seed + " -Z " + percnt + " -no-replacement";
                    String testOptionFilter = "FILTER weka.filters.unsupervised.instance.Resample -S "
                            + seed + " -Z " + percnt + " -no-replacement -V";

                    Instances train = resample(trainOptionFilter, m_Training);
                    Instances test = resample(testOptionFilter, m_Training);

                    System.out
                            .println(">>>>>>>>>>>>>>>>>>>>>>>>>>> oginal instances: "
                                    + m_Training.numInstances()
                                    + "| train after filter: "
                                    + train.numInstances()
                                    + "| test instances: "
                                    + test.numInstances());

                    m_Classifier.buildClassifier(train);
                    m_Evalution = new Evaluation(train);
                    m_Evalution.evaluateModel(m_Classifier, test);

                }

            } else { // default

                // Evaluation on training set
                m_Classifier.buildClassifier(m_Training);
                // 10fold CV with seed=1 will be the default evaluation method
                m_Evalution = new Evaluation(m_Training);
//				m_Evalution.crossValidateModel(m_Classifier, m_Training, 10,
//						m_Training.getRandomNumberGenerator(1));
                m_Evalution.evaluateModel(m_Classifier, m_Training);
            }

            StringBuffer result;

            result = new StringBuffer();

//			 If is drawable tree 
            try {
                Drawable d = (Drawable) m_Classifier;
                result.append(d.graph());
            } catch (Exception e) {
            }

            return result.toString();

        } catch (Exception e) {
            return e.getMessage();
        }

    }

    private Instances resample(String input, Instances instances)
            throws Exception {

        String filter = "";
        Vector<String> filterOpts = new Vector<String>();

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
                    filterOpts.add(args[i]);
                }
            }

            // next parameter
            i++;
            newPart = false;

        } while (i < args.length);

        System.out.println("filter: " + filter + " " + " options: "
                + filterOpts.toString());
        // the filter to use
        Filter m_filter = null;

        m_filter = (Filter) Class.forName(filter).newInstance();

        if (m_filter instanceof OptionHandler) {
            try {
                ((OptionHandler) m_filter).setOptions((String[]) filterOpts
                        .toArray(new String[filterOpts.size()]));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }
        m_filter.setInputFormat(instances);
        Instances filtered = Filter.useFilter(instances, m_filter);

        return filtered;

    }

}
