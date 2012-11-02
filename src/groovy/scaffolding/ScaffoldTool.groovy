package scaffolding


/**
 * Tool to resolve classes of scaffolded attributes.
 * Used by scaffolding views.
 */
import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.codehaus.groovy.grails.commons.DomainClassArtefactHandler

class ScaffoldTool {

    public static def getDownLevelClass(def clazz, String scaffoldProperty){

        String[] attrParts = scaffoldProperty.split("\\.")
        String firstAttr = attrParts[0]

        def grailsApplication = ApplicationHolder.application

        def domainDescriptor = grailsApplication.getArtefact(DomainClassArtefactHandler.TYPE, clazz.name)

        if (!domainDescriptor){
            return null
        }
        Class attrType = domainDescriptor.getPropertyByName(firstAttr)?.getType()

        if (attrParts.length > 1){

            // Strip the first part
            scaffoldProperty = scaffoldProperty.replace(firstAttr + ".","")

            // In case of a Set we assume where dealing with an hasMany.
            // If we can find the hasMany class we will continue with this class.
            // This allows us to chain through a set.
            if (attrType in Set){
                Class hasManyClass = domainDescriptor.getPropertyOrStaticPropertyOrFieldValue('hasMany', Map)?.get(firstAttr)
                if (hasManyClass){
                    attrType = hasManyClass
                }
            }
            return getDownLevelClass(attrType,scaffoldProperty)

        }
        else {
            return attrType
        }
    }

    public static def getDownLevelProp(def var, String scaffoldProperty){
        String[] attrParts = scaffoldProperty.split("\\.")
        String firstAttr = attrParts[0]

        if (attrParts.length > 1){

            // Strip the first part
            scaffoldProperty = scaffoldProperty.replace(firstAttr + ".","")

            return getDownLevelProp(var."${firstAttr}",scaffoldProperty)

        }
        else {
            return var."${scaffoldProperty}"
        }

    }

}
