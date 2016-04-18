package wsweka.core.converts;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import moodle.xml.response.*;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

@SuppressWarnings("deprecation")
public class MoodleDataSource {

	private Instances dataset;

	public MoodleDataSource(String dataxml) throws JAXBException {
		convertXmlToArrf(dataxml);
	}

	@SuppressWarnings("deprecation")
	private void convertXmlToArrf(String dataxml) throws JAXBException {
		
		

		// JAXB - parse from xml to object
		// try {
		// JFile file = new File("C:\\file.xml");

		// http://theopentutorials.com/examples/java/jaxb/generate-java-class-from-xml-schema-in-eclipse-ide/
		StringReader xml = new StringReader(dataxml);
		JAXBContext jaxbContext = JAXBContext
				.newInstance(moodle.xml.response.RESPONSE.class);

		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		RESPONSE resposta = (RESPONSE) jaxbUnmarshaller.unmarshal(xml);

		List<RESPONSE.SINGLE.KEY> keys = resposta.getSINGLE().getKEY();

		// ================================ conversao de xml to arrf format

		List<Attribute> attrArray = new ArrayList<Attribute>();
		List<AttributeRetrival> attrRetr = new ArrayList<AttributeRetrival>();
		String relation = "";
		// Relation
		for (RESPONSE.SINGLE.KEY key : keys) {

			if (key.getName().equals("relation")) {
				relation = key.getVALUE();
			}
		}

		boolean f = false;
		// Attributes
		for (RESPONSE.SINGLE.KEY key : keys) {

			// if (key.getName().equals("attribute")) {
			// }

			if (f) {
				List<RESPONSE.SINGLE> singles = key.getMULTIPLE().getSINGLE();

				for (RESPONSE.SINGLE single : singles) {
					List<RESPONSE.SINGLE.KEY> keyss = single.getKEY();

					String field = "";
					String type = "";
					for (RESPONSE.SINGLE.KEY kkey : keyss) {
						if (kkey.getName().equals("field")) {
							field = kkey.getVALUE();
						}

						if (kkey.getName().equals("type")) {
							type = kkey.getVALUE();
						}
					}

					// creating the attributes and type
					if (!field.isEmpty()) {
						if (type.equals("string")) {
							attrArray.add(new Attribute(field,
									(FastVector<String>) null));
							attrRetr.add(new AttributeRetrival(field, type));
						} else if (type.equals("numeric")) {
							attrArray.add(new Attribute(field));
							attrRetr.add(new AttributeRetrival(field, type));
						}
					}

				}
			}

			f = true;
		}

		@SuppressWarnings("deprecation")
		FastVector<Attribute> fvWekaAttributes = new FastVector<Attribute>(
				attrArray.size());

		for (Attribute attribute : attrArray) {

			fvWekaAttributes.addElement(attribute);
		}

		dataset = new Instances(relation, fvWekaAttributes, 500);

		boolean t = false;
		// data

		for (RESPONSE.SINGLE.KEY key : keys) {


			if (t) {
				List<RESPONSE.SINGLE> singles = key.getMULTIPLE().getSINGLE();

				Instance instance = new DenseInstance(attrArray.size());

				for (RESPONSE.SINGLE single : singles) {
					List<RESPONSE.SINGLE.KEY> keyss = single.getKEY();

					// i - loops to all attributes elements
					int i = 0;
					boolean containAtrr = false;
					for (RESPONSE.SINGLE.KEY kkey : keyss) {

						// find the type of this value
						AttributeRetrival attrTemp = null;
						for (AttributeRetrival at : attrRetr) {
							if (at.getField().equals(kkey.getName())) {
								containAtrr = true;
								attrTemp = at;
								break;
							}
						}

						if (containAtrr) {
							// add instances according they type
							if (attrTemp.getType().equals("string")) {
								instance.setValue(
										fvWekaAttributes.elementAt(i),
										kkey.getVALUE());
							} else {
								instance.setValue(
										fvWekaAttributes.elementAt(i),
//										Integer.parseInt(kkey.getVALUE()));
										Double.parseDouble(kkey.getVALUE()));
							}
						}
						i++;
					}
					if (containAtrr) {
						dataset.add(instance);
					}
				}
			}

			t = true;
		}

	}

	public Instances getDataSet() {

		return dataset;
	}

}
