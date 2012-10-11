import org.codehaus.groovy.grails.commons.ApplicationHolder

class UrlMappings {

    def grailsApplication

	static mappings = {

        // Add scaffold controllers
        ApplicationHolder.application.controllerClasses.sort { it.fullName }.each {
            if (it.fullName.contains('scaffold')){
                def controllerName = it.logicalPropertyName
                "/scaffold/${controllerName}/$action?/$id?"{
                    controller = controllerName
                    constraints {
                        // apply constraints here
                    }
                }
            }
        }


		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}

		"/"(controller:'game', view:"/index")
		"500"(view:'/error')
	}
}
