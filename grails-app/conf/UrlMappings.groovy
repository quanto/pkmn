import grails.ApplicationContextHolder

class UrlMappings {

	static mappings = {

        // Add scaffold controllers
        getGrailsApplication().controllerClasses.sort { it.fullName }.each {
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


        "/$controller/$action?/$id?(.$format)?"{
			constraints {
				// apply constraints here
			}
		}

		"/"(controller:'game')
		"500"(view:'/error')
	}
}
