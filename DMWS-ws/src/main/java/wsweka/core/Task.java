package wsweka.core;

import weka.core.Instances;

public abstract class Task {
	
	abstract public String buildModel(Instances instances) throws Exception;
}
