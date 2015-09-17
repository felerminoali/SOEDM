/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mz.com.view;

import javafx.scene.control.TextField;

/**
 *
 * @author Lenovo
 */
public class ChosserPanelFactory {

    public ChooserPanel createPanel(MainGUIController mainGUIController, TextField txtChoosed) {

        
       
        
        String input = txtChoosed.getText();

        if (input != null) {

            String args[] = input.split("\\s");

            String choosed = args[0];

            if (args.length > 1) {
                String options[] = new String[args.length - 1];
                for (int i = 1; i < args.length; i++) {
                    options[i - 1] = args[i];
                }

                // filters
                if (choosed.equals("weka.filters.unsupervised.attribute.Discretize")) {
                    return new FilterOptDiscretize(mainGUIController, choosed, options);
                } //             
                else if (choosed.equals("weka.filters.unsupervised.attribute.ManualDiscretize")) {
                    return new FilterOptManualDiscretize(mainGUIController, choosed, options);
                }
                else if (choosed.equals("weka.filters.unsupervised.attribute.Remove")) {
                    return new FilterOptRemove(mainGUIController, choosed, options);
                }
                
                // classifiers
                
                 else if (choosed.equals("weka.classifiers.bayes.NaiveBayes")) {
                    return new ClassifierOptNaiveBayes(mainGUIController, choosed, options);
                }
               
                 else if (choosed.equals("weka.classifiers.rules.ZeroR")) {
                    return new ClassfierOptZeroRule(mainGUIController, choosed, options);
                }
                
                  else if (choosed.equals("weka.classifiers.trees.J48")) {
                return new ClassifierOptJ48(mainGUIController, choosed, options);
            }
            

            }

            // choosed
            if (choosed.equals("weka.filters.AllFilter")) {
                return new FilterOptAll();
            } else if (choosed.equals("weka.filters.unsupervised.attribute.Discretize")) {
                return new FilterOptDiscretize(mainGUIController, choosed);
            } 
            else if (choosed.equals("weka.filters.unsupervised.attribute.ManualDiscretize")) {
                return new FilterOptManualDiscretize(mainGUIController,choosed);
            }
            else if (choosed.equals("weka.filters.unsupervised.attribute.Remove")) {
                return new FilterOptRemove(mainGUIController, choosed);
            }
            
            // classifier
            
            else if (choosed.equals("weka.classifiers.bayes.NaiveBayes")) {
                return new ClassifierOptNaiveBayes(mainGUIController, choosed);
            }
            
             else if (choosed.equals("weka.classifiers.rules.ZeroR")) {
                return new ClassfierOptZeroRule(mainGUIController, choosed);
            }
            
             else if (choosed.equals("weka.classifiers.trees.J48")) {
                return new ClassifierOptJ48(mainGUIController, choosed);
            }
            
//            
        }

        return null;
    }

}
