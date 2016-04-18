package wsweka.core;

import java.util.Vector;

import weka.classifiers.Evaluation;
import weka.clusterers.AbstractClusterer;
import weka.clusterers.ClusterEvaluation;
import weka.core.Instances;
import weka.core.OptionHandler;
import weka.core.Utils;
import weka.filters.Filter;

public class ClusterTask extends Task {

    private String clusterer = null;
    private String filter = null;
    private String evaluation = null;

    private Vector<String> clusterOptions = new Vector<String>();
    private Vector<String> filterOptions = new Vector<String>();
    Vector<String> evaluationOption = new Vector<String>();

    public ClusterTask() {
    }

    public ClusterTask(String cluster, Vector<String> clusterOptions,
            String filter, Vector<String> filterOptions, String evaluation,
            Vector<String> evaluationOption) {
        this.clusterer = cluster;
        this.clusterOptions = clusterOptions;
        this.filter = filter;
        this.filterOptions = filterOptions;
        this.evaluation = evaluation;
        this.evaluationOption = evaluationOption;
    }

    public ClusterTask(String cluster, Vector<String> clusterOptions,
            String evaluation, Vector<String> evaluationOption) {
        this.clusterer = cluster;
        this.clusterOptions = clusterOptions;
        this.evaluation = evaluation;
        this.evaluationOption = evaluationOption;

    }

    @Override
    public String buildModel(Instances instances) throws Exception {

        try {

            AbstractClusterer cluster = null;

            // the filter to use
            Filter m_filter = null;

            // the training instance
            Instances m_Training = null;

            // for evaluating the classifier
            ClusterEvaluation m_Evalution = null;

            cluster = (AbstractClusterer) AbstractClusterer.forName(
                    this.clusterer, (String[]) this.clusterOptions
                    .toArray(new String[this.clusterOptions.size()]));

            // gathering data from path data source
            m_Training = instances;

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

//            System.out.println(m_Training.toString());
//            cluster.buildClusterer(m_Training);
            // start time taken to build the model
            long starttime = System.currentTimeMillis();

            // Evaluation Cross-validation or for given percentage split
            // original dataset
            // into train and test sets
            if (this.evaluation != null) {

                if (this.evaluation.equals("p-split")) {
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

                    cluster.buildClusterer(train);
                    m_Evalution = new ClusterEvaluation();

                    m_Evalution.setClusterer(cluster);
                    m_Evalution.evaluateClusterer(test);

                }
            } else { // default

                // Evaluation on training set
                cluster.buildClusterer(m_Training);
                m_Evalution = new ClusterEvaluation();

                m_Evalution.setClusterer(cluster);
                m_Evalution.evaluateClusterer(m_Training);

            }

            // stop time taken to build the model
            long stoptime = System.currentTimeMillis();
            // total time taken to build the model
            long elapsedtime = stoptime - starttime;

            StringBuffer result = new StringBuffer("");

            result.append("Weka - Clustering Task\n==================\n\n");

            result.append("Cluster....: " + cluster.getClass().getName() + " ");
//					+ Utils.joinOptions(cluster.getOptions()) + "\n");

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
            result.append("\n");
            result.append(cluster.toString() + "\n");
            result.append("Time taken to build the model: " + elapsedtime + " miliseconds" + "\n");
            try {
                result.append(m_Evalution.clusterResultsToString() + "\n");
            } catch (Exception e) {
                e.printStackTrace();
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
