package mz.com.model;

import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Lenovo
 */
public class Topic {
    
    private String pageName;
    private String id; 
    private String attribute;
    private List<Value> values = new ArrayList<>();
    private String recommendation;
    private boolean concluding = false;

    
     public Topic() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
   
    public boolean isConcluding() {
        return concluding;
    }

    public void setConcluding(boolean concluding) {
        this.concluding = concluding;
    }
    

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public List<Value> getValues() {
        return values;
    }

    public void setValues(List<Value> values) {
        this.values = values;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }
    
    
    
}
