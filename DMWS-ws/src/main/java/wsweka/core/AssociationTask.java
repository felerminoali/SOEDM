package wsweka.core;

import java.util.Vector;

import weka.associations.AbstractAssociator;
import weka.core.Instances;
import weka.core.OptionHandler;
import weka.core.Utils;
import weka.filters.Filter;

public class AssociationTask extends Task {

	private String associator = null;
	private String filter = null;

	private Vector<String> AssociationOptions = new Vector<String>();
	private Vector<String> filterOptions = new Vector<String>();

	public AssociationTask() {
	}

	public AssociationTask(String associator,
			Vector<String> AssociationOptions, String filter,
			Vector<String> filterOptions) {
		this.associator = associator;
		this.AssociationOptions = AssociationOptions;
		this.filter = filter;
		this.filterOptions = filterOptions;
	}

	public AssociationTask(String associator, Vector<String> AssociationOptions) {
		this.associator = associator;
		this.AssociationOptions = AssociationOptions;

	}

	@Override
	public String buildModel(Instances instances) throws Exception {

		try {

			AbstractAssociator association = null;

			// the filter to use
			Filter m_filter = null;

			// the training instance
			Instances m_Training = null;

			association = (AbstractAssociator) AbstractAssociator
					.forName(this.associator,
							(String[]) this.AssociationOptions
									.toArray(new String[this.AssociationOptions
											.size()]));

			// gathering data from path data source
			m_Training = instances;

			if (this.filter != null) {
				m_filter = (Filter) Class.forName(this.filter).newInstance();

				if (m_filter instanceof OptionHandler) {
					((OptionHandler) m_filter)
							.setOptions((String[]) this.filterOptions
									.toArray(new String[this.filterOptions
											.size()]));
				}
				m_filter.setInputFormat(m_Training);
				Instances filtered = Filter.useFilter(m_Training, m_filter);
				m_Training = filtered;
			}

			System.out.println(m_Training.toString());

			association.buildAssociations(m_Training);

			StringBuffer result = new StringBuffer("");

			result.append("Weka - Association Task\n==================\n\n");

			result.append("Associator....: "
					+ association.getClass().getName()
					+ " "
					+ Utils.joinOptions(((OptionHandler) association)
							.getOptions()) + "\n");

			if (filter != null) {
				if (m_filter instanceof OptionHandler) {
					result.append("Filter....: "
							+ m_filter.getClass().getName()
							+ " "
							+ Utils.joinOptions(((OptionHandler) m_filter)
									.getOptions()) + "\n");
				} else {
					result.append("Filter....: "
							+ m_filter.getClass().getName() + "\n");
				}
			}

			// result.append("Training file: " + m_TrainingFile + "\n");
			result.append("\n");
			result.append(association.toString() + "\n");
			return result.toString();

		} catch (Exception e) {
			return e.getMessage();
		}
	}

}
