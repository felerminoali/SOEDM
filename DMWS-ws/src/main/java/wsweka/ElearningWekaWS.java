package wsweka;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.JAXBException;


@WebService
public class ElearningWekaWS {

	// Web service Operation
	MethodsUtility methodsUtility;
	
	public ElearningWekaWS(){
		methodsUtility = new MethodsUtility();
	}

	@WebMethod(operationName = "execute")
	public String execute(@WebParam(name = "input") String input)
			throws Exception {

		return methodsUtility.execute(input);
	}
	
	
	@WebMethod
	public String xmlToArrf(String xml) throws JAXBException{
		 return methodsUtility.xmlToArrf(xml);
	}
	
	@WebMethod
	 public String executeTreeClassifer(String url)
			 throws Exception {
		 return methodsUtility.treeClassifer(url);
	 }
	
	@WebMethod
	public String executeTwo(String xml, String optTask)throws Exception{
		 return methodsUtility.executeTwo(xml, optTask);
	}
	
	@WebMethod
	public String executeWithDiscritize(String xml, String optTask, String discritizeOpts)throws Exception{
		 return methodsUtility.executeTwo(xml, optTask, discritizeOpts);
	}
	
	@WebMethod
	public String executeWithManualDiscritize(String xml, String optTask, String discritizeOpts)throws Exception{
		 return methodsUtility.executeTo(xml, optTask, discritizeOpts);
	}
	
	@WebMethod
	public String executeFilter(String arff, String filter)throws Exception{
		 return methodsUtility.executeFilter(arff, filter);
	}
	
	@WebMethod
	public String executeClassifier(String arff, String classifier, String evaluation)throws Exception{
		 return methodsUtility.executeClassifier(arff, classifier, evaluation);
	}
	
	@WebMethod
	public String executeManualDescritezation(String arff, String filter)throws Exception{
		 return methodsUtility.executeManualDescritezation(arff, filter);
	}
	
	@WebMethod
	public String executeCluster(String arff, String cluster, String evaluation)throws Exception{
		 return methodsUtility.executeCluster(arff, cluster, evaluation);
	}
	
	
	@WebMethod
	public String executeAssociate(String arff, String associator)throws Exception{
		 return methodsUtility.executeAssociate(arff, associator);
	}
	
	@WebMethod
	public String visualizeTreeGraph(String arff, String classifier, String evaluation)throws Exception{
		 return methodsUtility.visualizeTreeGraph(arff, classifier, evaluation);
	}

	
	
}
