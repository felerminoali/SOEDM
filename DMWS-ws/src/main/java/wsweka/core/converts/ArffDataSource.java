package wsweka.core.converts;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.Charset;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class ArffDataSource {

	private Instances dataset;
	
	public ArffDataSource(String arff) throws Exception{
		
		convertToArrf(arff);
	}
	
	private void convertToArrf(String arrf) throws Exception{
		

		//use ByteArrayInputStream to get the bytes of the String and convert them to InputStream.
		InputStream inputStream = new ByteArrayInputStream(arrf.getBytes(Charset.forName("UTF-8")));
		
		
        DataSource source = new DataSource(inputStream);
        
        dataset = source.getDataSet();

	}
	
	
	public Instances getDataset() {
		return dataset;
	}

	

	public static void main(String[] args) throws Exception {
		String arrf = "@relation student_summarization \n" +
				"@attribute StudentID numeric \n" +
				"@attribute course string \n" +
				"@attribute n_quiz numeric \n" +
				"@attribute n_quiz_a numeric \n" +
				"@attribute n_quiz_s numeric \n" +
				"@attribute total_time_quiz numeric \n" +
				"@data \n" +
				"3,OOAD,2,1,0,146 \n" +
				"4,OOAD,1,0,1,31 \n";
		
		ArffDataSource coverter = new ArffDataSource(arrf); 
		
		System.out.println(coverter.getDataset().toString());
		
		
		
	}
}
