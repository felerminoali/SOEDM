//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.06.03 at 07:12:46 PM SGT 
//


package jaxb.weka.filters;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the jaxb.weka.filters package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: jaxb.weka.filters
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Filters }
     * 
     */
    public Filters createFilters() {
        return new Filters();
    }

    /**
     * Create an instance of {@link Filters.Strategy }
     * 
     */
    public Filters.Config createFiltersStrategy() {
        return new Filters.Config();
    }

    /**
     * Create an instance of {@link Filters.Strategy.InputType }
     * 
     */
    public Filters.Config createFiltersStrategyInputType() {
        return new Filters.Config();
    }

    /**
     * Create an instance of {@link Filters.Config }
     * 
     */
    public Filters.Config createFiltersConfig() {
        return new Filters.Config();
    }

    /**
     * Create an instance of {@link Filters.Strategy.InputType.Config }
     * 
     */
    public Filters.Config createFiltersStrategyInputTypeConfig() {
        return new Filters.Config();
    }

}
