/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mz.com.model;


import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author feler
 */
public final class DataOutputHistory {

    private StringProperty output = new SimpleStringProperty("");
    private  SimpleLongProperty timeTaken = new SimpleLongProperty();
    private StringProperty title = new SimpleStringProperty("");

    public DataOutputHistory() {
        this(null, 0);
    }

    public DataOutputHistory(String output, long timeTaken) {
        this.output.set(output);
        this.timeTaken.set(timeTaken);

        this.title.set(toString());
    }

    public StringProperty outputProperty() {
        return this.output;
    }

    public  SimpleLongProperty timeTakenProperty() {
        return this.timeTaken;
    }

    public StringProperty titleProperty() {
        return this.title;
    }

    public String getOutput() {
        return output.get();
    }

    public void setOutput(String output) {
        this.output.set(output);
    }

    public long getTimeTaken() {
        return timeTaken.get();
    }

    public void setTimeTaken(long timeTaken) {
        this.timeTaken.set(timeTaken);
    }

    @Override
    public String toString() {
        return "Time taken: " + timeTaken.get() + " milliseconds";
    }
}
