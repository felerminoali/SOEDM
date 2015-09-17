/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mz.com.model;

/**
 *
 * @author Lenovo
 */
public class OptionsObject {
    
    private String value="";
    
    public OptionsObject(){}
    public OptionsObject(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    
    
    @Override
    public String toString(){
        if(!value.isEmpty()){
            return "True";
        }
        
        return "False";
    }
}
