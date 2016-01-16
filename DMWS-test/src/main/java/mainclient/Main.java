package mainclient;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


import wsweka.ElearningWekaWS;
import wsweka.ElearningWekaWSService;
import wsweka.Exception_Exception;
import wsweka.JAXBException_Exception;

public class Main {

	/**
	 * @param args
	 * @throws Exception_Exception 
	 * @throws IOException 
	 * @throws MalformedURLException 
	 * @throws JAXBException_Exception 
	 */
	public static void main(String[] args) throws Exception_Exception, MalformedURLException, IOException, JAXBException_Exception {
		// TODO Auto-generated method stub
		
		String token = "9c4991bde66fe656ecc942d9b61e0f04";

		String domainName = "http://127.0.0.1:8282/moodle-latest-27/moodle";
//		 String domainName = "http://172.19.100.1170/moodle-latest-27/moodle";

		// / REST RETURNED VALUES FORMAT
		String restformat = "xml"; // Also possible in Moodle 2.2 and later:
									// 'json'
									// Setting it to 'json' will fail all calls
									// on earlier Moodle version
		if (restformat.equals("json")) {
			restformat = "&moodlewsrestformat=" + restformat;
		} else {
			restformat = "";
		}

		// / PARAMETERS - NEED TO BE CHANGED IF YOU CALL A DIFFERENT FUNCTION
		String functionName = "local_miningdata_get_students_data";

		String urlParameters = "Default, ";

		// REST CALL

		// Send request
		String serverurl = domainName + "/webservice/rest/server.php"
				+ "?wstoken=" + token + "&wsfunction=" + functionName;

		HttpURLConnection con = (HttpURLConnection) new URL(serverurl)
				.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		con.setRequestProperty("Content-Language", "en-US");
		con.setDoOutput(true);
		con.setUseCaches(false);
		con.setDoInput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		// Get Response
		InputStream is = con.getInputStream();
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		String line;
		StringBuilder response = new StringBuilder();
		while ((line = rd.readLine()) != null) {
			response.append(line);
			response.append('\r');
		}
		rd.close();

//		System.out.println(response.toString());
		
		ElearningWekaWSService service = new ElearningWekaWSService();
		
		ElearningWekaWS port = service.getElearningWekaWSPort();
		
		System.out.println(port.xmlToArrf(response.toString()));
		
		//CLASSIFIER weka.classifiers.trees.J48 -U FILTER weka.filters.unsupervised.instance.Randomize DATASET G:/OneDrive/Documentos/WekaDataset/iris.arff
		
//		System.out.println(port.execute("CLASSIFIER weka.classifiers.trees.J48 -C 0.25 -M 2 DATASET G:/OneDrive/Documentos/WekaDataset/iris.arff"));
		
//		System.out.println(port.execute("CLASSIFIER weka.classifiers.bayes.NaiveBayes DATASET G:/OneDrive/Documentos/WekaDataset/iris.arff"));
		
//		System.out.println(port.execute("CLASSIFIER weka.classifiers.functions.SMO DATASET G:/OneDrive/Documentos/WekaDataset/iris.arff"));
		
//		System.out.println(port.execute("CLASSIFIER weka.classifiers.trees.J48 -U FILTER weka.filters.unsupervised.instance.Randomize DATASET G:/OneDrive/Documentos/WekaDataset/iris.arff"));
		
//		System.out.println(port.executeTwo(response.toString(), "CLASSIFIER weka.classifiers.trees.J48 -C 0.25 -M 2 FILTER weka.filters.unsupervised.attribute.Remove -R 2"));
		
	
		
		// 
//		System.out.println(port.executeWithDiscritize(response.toString(), 
//				"CLASSIFIER weka.classifiers.trees.J48 -C 0.25 -M 2", 
//				"FILTER weka.filters.unsupervised.attribute.Discretize -B 4 -R first-last"));
		
		// 
//		System.out.println(port.executeWithDiscritize(response.toString(), 
//				"CLASSIFIER weka.classifiers.trees.J48 FILTER weka.filters.unsupervised.attribute.Remove -R 2", 
//				"FILTER weka.filters.unsupervised.attribute.Discretize -B 4 -R first-last"));
		
//		System.out.println(port.executeWithDiscritize(response.toString(), 
//				"CLASSIFIER weka.classifiers.trees.J48 FILTER weka.filters.unsupervised.attribute.Remove -R 2", 
//				"FILTER weka.filters.unsupervised.attribute.Discretize -B 4 -R 6"));
		
//		System.out.println(port.executeWithManualDiscritize(response.toString(), 
//				"CLASSIFIER weka.classifiers.trees.J48 FILTER weka.filters.unsupervised.attribute.Remove -R 2", 
//				"-A low medium high -C fail pass good execellent"));
		
//		System.out.println(port.executeWithManualDiscritize(response.toString(), 
//				"CLASSIFIER weka.classifiers.trees.J48 FILTER weka.filters.unsupervised.attribute.Remove -R 2 TEST-OPTIONS cross-validation -f 2 -S 1", 
//				"-A low medium high -C fail pass good execellent"));
		
//		System.out.println(response.toString());
//		try {
//			System.out.println(port.executeWithManualDiscritize(response.toString(), 
//					"CLASSIFIER weka.classifiers.trees.J48 FILTER weka.filters.unsupervised.attribute.Remove -R 2 TEST-OPTIONS cv-split -Z 50.0 -S 1", 
//					"-A low medium high -C fail pass good execellent"));
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//		}
//		
//		
		// CLASSIFIER weka.classifiers.trees.J48 FILTER weka.filters.unsupervised.attribute.Remove -R TEST-OPTIONS cross-validation | percentage-split 
		
		
		// CLUSTER
//		System.out.println(port.executeWithManualDiscritize(response.toString(), "CLUSTER weka.clusterers.SimpleKMeans -N 2 FILTER weka.filters.unsupervised.attribute.Remove -R 2", "-A low medium high -C fail pass good execellent"));
//		System.out.println(port.executeWithManualDiscritize(response.toString(), "CLUSTER weka.clusterers.EM -N 1 FILTER weka.filters.unsupervised.attribute.Remove -R 2", "-A low medium high -C fail pass good execellent"));
		
		
		// ASSOCIATION
		
//		System.out.println(port.executeWithManualDiscritize(response.toString(), "ASSOCIATION weka.associations.Apriori FILTER weka.filters.unsupervised.attribute.Remove -R 1-3", "-A low medium high -C fail pass good execellent"));
		
	}

}
