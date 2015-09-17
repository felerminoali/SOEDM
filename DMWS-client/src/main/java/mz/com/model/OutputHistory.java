/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mz.com.model;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Lenovo
 */
public final class OutputHistory {

    private StringProperty output = new SimpleStringProperty("");
    private final ObjectProperty<Date> date;
    private StringProperty task = new SimpleStringProperty("");
    private StringProperty evaluation = new SimpleStringProperty("");

    private StringProperty titleProperty = new SimpleStringProperty("");

    public OutputHistory() {
        this(null, null, null);
    }

    public OutputHistory(String output, String task, String evaluation) {
        this.output.set(output);
        this.task.set(task);
        this.evaluation.set(evaluation);

        this.date = new SimpleObjectProperty<Date>(Calendar.getInstance().getTime());

        titleProperty.set(toString());
    }

    public StringProperty titleProperty() {
        return titleProperty;
    }

    public StringProperty outputProperty() {
        return this.output;
    }

    public String getOutput() {
        return output.get();
    }

    public void setOutput(String output) {
        this.output.set(output);
    }

    public String getTask() {
        return task.get();
    }

    public void setTask(String task) {
        this.task.set(task);
    }

    public String getEvaluation() {
        return evaluation.get();
    }

    public void setEvaluation(String evaluation) {
        this.evaluation.set(evaluation);
    }

    public boolean isTree() {

        boolean result = false;
        String[] args = getTask().split("\\s");
        if (args[0].equals("weka.classifiers.trees.J48")) {
            result = true;
        }

        if (args[0].equals("weka.classifiers.trees.ADTree")) {
            result = true;
        }

        if (args[0].equals("weka.classifiers.trees.DecisionStump")) {
            result = true;
        }

        if (args[0].equals("weka.classifiers.trees.FT")) {
            result = true;
        }

        if (args[0].equals("weka.classifiers.trees.Id3")) {
            result = true;
        }

        if (args[0].equals("weka.classifiers.trees.J48graft")) {
            result = true;
        }

        if (args[0].equals("weka.classifiers.trees.LADTree")) {
            result = true;
        }

        if (args[0].equals("weka.classifiers.trees.LMT")) {
            result = true;
        }

        if (args[0].equals("weka.classifiers.trees.M5P")) {
            result = true;
        }

        if (args[0].equals("weka.classifiers.trees.NBTree")) {
            result = true;
        }

        if (args[0].equals("weka.classifiers.trees.RandomForest")) {
            result = true;
        }

        if (args[0].equals("weka.classifiers.trees.RandomTree")) {
            result = true;
        }

        if (args[0].equals("weka.classifiers.trees.REPTree")) {
            result = true;
        }

        if (args[0].equals("weka.classifiers.trees.SimpleCart")) {
            result = true;
        }

        if (args[0].equals("weka.classifiers.trees.UserClassifier")) {
            result = true;
        }

        return result;

    }

    public boolean isDrawable() {

        boolean result = false;
        String[] args = getTask().split("\\s");
        if (isTree()) {

            if (args[0].equals("weka.classifiers.trees.J48")) {
                result = true;
            }

            if (args[0].equals("weka.classifiers.trees.ADTree")) {
                result = true;
            }

            if (args[0].equals("weka.classifiers.trees.DecisionStump")) {
                result = false;
            }

            if (args[0].equals("weka.classifiers.trees.FT")) {
                result = true;
            }

            if (args[0].equals("weka.classifiers.trees.Id3")) {
                result = false;
            }

            if (args[0].equals("weka.classifiers.trees.J48graft")) {
                result = true;
            }

            if (args[0].equals("weka.classifiers.trees.LADTree")) {
                result = true;
            }

            if (args[0].equals("weka.classifiers.trees.LMT")) {
                result = true;
            }

            if (args[0].equals("weka.classifiers.trees.M5P")) {
                result = true;
            }

            if (args[0].equals("weka.classifiers.trees.NBTree")) {
                result = true;
            }

            if (args[0].equals("weka.classifiers.trees.RandomForest")) {
                result = false;
            }

            if (args[0].equals("weka.classifiers.trees.RandomTree")) {
                result = true;
            }

            if (args[0].equals("weka.classifiers.trees.REPTree")) {
                result = true;
            }

            if (args[0].equals("weka.classifiers.trees.SimpleCart")) {
                result = false;
            }

            if (args[0].equals("weka.classifiers.trees.UserClassifier")) {
                result = true;
            }

        }

        return result;

    }

    @Override
    public String toString() {
        String[] taskS = task.get().split("\\s");
        int lastOccur = taskS[0].lastIndexOf(".");
        String simplifiedTask = taskS[0].substring(lastOccur + 1);
        return this.date.get().toString() + " - " + simplifiedTask;
    }
}
