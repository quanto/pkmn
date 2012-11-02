package scaffolding

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.codehaus.groovy.grails.commons.DomainClassArtefactHandler
import java.text.DateFormat
import java.text.SimpleDateFormat

/**
 *  Search functions for scaffold controller
 */
class ScaffoldSearch {

    private static final Logger log = LoggerFactory.getLogger(this)

    // Properties that are never searched with an "like" query, but always with an "equals" query (usually done for performance reasons)
    public static final List<String> SEARCH_EXACT_PROPERTIES = [ '' ]
    public static final List<String> SEARCH_UPPERCASE = [ '' ]

    public static def searchByParams(params, Class clazz, def searchProperties, Closure customCriteriaFilter, boolean doCount = false) {

        def grailsApplication = ApplicationHolder.application

        // Define the search clause that is used in the Hibernate search criteria below
        def searchClosure = {
            try
            {
                if (customCriteriaFilter){
                    customCriteriaFilter(delegate)
                }
                // Loop through all enabled search properties, and process the ones that are passed as param
                searchProperties?.each { attr ->

                    String attrValue = params[attr]

                    if (attr && attrValue) {

                        attrValue = attrValue.trim()

                        // The better way: check on type i.s.o. name.
                        def domainDescriptor = grailsApplication.getArtefact(DomainClassArtefactHandler.TYPE, clazz.name)

                        downLevelCriteria(delegate, attr, attrValue, domainDescriptor, params)
                    }
                }
            }
            catch(groovy.lang.MissingPropertyException mpe){
                // catch missing scaffoldSearch property
                // We don't need to filter then, continue
            }

            if (!doCount && params?.offset){
                firstResult(new Integer(params.offset))
            }
            if (params.max){
                maxResults(params.max)
            }

            if (!doCount) {
                if (params.sort && params.order) {
                    order(params.sort, params.order)
                } else if (params.sort) {
                    order(params.sort)
                }
            }
        }

        def criteria = clazz.createCriteria()
        try {
            if (doCount){
                return criteria.count(searchClosure)
            }
            else{
                return criteria.listDistinct(searchClosure)
            }
        }
        catch (Exception error) {
            log.error("Search failed", error)
            // For the best user experience, return a search with no results.
            if (doCount)
                return clazz.createCriteria().count { eq("id", -1L) }
            else
                return clazz.createCriteria().list { eq("id", -1L) }
        }
    }

    /**
     * This function passes down the attr var. It creates the criteria by
     * getting the first part of attr and resolves it's class.
     *
     * Example attr = "customer.address.streetName"
     * Becomes:
     *  customer {
     *      address {
     *          (-> whereCriteria(streetName) )
     *      }
     *  }
     *
     * The streetName is the last attr.
     * The last attr is passed to the function whereCriteria.
     *
     * @param builder
     * @param attr
     * @param attrValue
     * @param domainDescriptor
     * @param params
     * @return
     */
    private static def downLevelCriteria(def builder, def attr, def attrValue, def domainDescriptor, params){

        def grailsApplication = ApplicationHolder.application

        String[] attrParts = attr.split("\\.")
        String firstAttr = attrParts[0]

        Class attrType = domainDescriptor.getPropertyByName(firstAttr)?.getType()

        // If there are more parts, move down the classes
        if (attrParts.length > 1){

            // In case of a Set we assume where dealing with an hasMany.
            // If we can find the hasMany class we will continue with this class.
            // This allows us to chain through a set.
            if (attrType in Set){
                Class hasManyClass = domainDescriptor.getPropertyOrStaticPropertyOrFieldValue('hasMany', Map)?.get(firstAttr)
                if (hasManyClass){
                    domainDescriptor = grailsApplication.getArtefact(DomainClassArtefactHandler.TYPE, hasManyClass.name)
                }
            }
            else {
                // New domain level descriptor for the new attrType
                domainDescriptor = grailsApplication.getArtefact(DomainClassArtefactHandler.TYPE, attrType.name)
            }
            // Strip the first part
            attr = attr.replace(firstAttr + ".","")

            // Create the criteria
            builder."${firstAttr}"{
                downLevelCriteria(builder, attr, attrValue, domainDescriptor, params)
            }

        }
        else {
            // We can't go down,
            whereCriteria(builder, attr, attrType, attrValue, params)
        }
    }

    private static def whereCriteria(def builder, def attr, def attrType, def attrValue, def params){

        if (attrType in int || attrType in Integer) {
            try {
                builder.eq(attr, Integer.valueOf(attrValue) )
            }
            catch (java.lang.NumberFormatException e) {
                if (attrValue == "isNull"){
                    builder.isNull(attr)
                }
                else if (attrValue == "isNotNull"){
                    builder.isNotNull(attr)
                }
                else {
                    builder.eq(attr, -1)
                }
            }
        }
        else if (attrType in long || attrType in Long) {
            try {
                builder.eq(attr, Long.valueOf(attrValue) )
            }
            catch (java.lang.NumberFormatException e) {
                builder.eq(attr, -1L)
            }
        }
        else if (attrType in boolean) {
            builder.eq(attr, 'Ja'.equalsIgnoreCase(attrValue)?true:false)
        }
        else if (attrType in Enum) {
            def enumValue = attrType.valueOf(attrValue)

            builder.eq(attr, enumValue)
        }
        else if (attrType in Date){
            if (params[attr + "_from"]){

                DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy")
                Date dateFrom = null
                Date dateTo = null
                try
                {
                    dateFrom = formatter.parse(params[attr + "_from"])

                    try
                    {
                        dateTo = formatter.parse(params[attr + "_to"])
                    }
                    catch(java.text.ParseException pe){}

                    if (dateTo){
                        builder.between(attr,dateFrom,dateTo)
                    }
                    else{
                        builder.between(attr,dateFrom,dateFrom+1)
                    }
                }
                catch(java.text.ParseException pe){
                    // show no results
                    builder.between(attr,new Date(),new Date())
                }
            }
        }
        else if (SEARCH_EXACT_PROPERTIES.contains(attr)) {
            // No "like" search on these properties; users want an exact match (usually because that is much faster,
            // e.g. search on customer_number is a 100 times slower (on production db) with a like query.
            if ( (attrValue in String) && SEARCH_UPPERCASE.contains(attr)) {
                attrValue = attrValue.toUpperCase()
            }
            builder.eq(attr, attrValue)
        }
        else {
            builder.ilike(attr, '%'+attrValue+'%')
        }
    }
}
