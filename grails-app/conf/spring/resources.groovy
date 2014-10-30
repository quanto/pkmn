import org.springframework.security.web.authentication.session.ConcurrentSessionControlStrategy
import org.springframework.security.web.session.ConcurrentSessionFilter
import org.springframework.security.core.session.SessionRegistryImpl
import grails.ApplicationContextHolder

// Place your Spring DSL code here
beans = {

    sessionRegistry(SessionRegistryImpl)

    sessionAuthenticationStrategy(ConcurrentSessionControlStrategy, sessionRegistry) {
        maximumSessions = -1
    }

    concurrentSessionFilter(ConcurrentSessionFilter){
        sessionRegistry = sessionRegistry
      //  expiredUrl = '/login/concurrentSession'
    }

    securityEventListener(security.AuthenticationSuccessListener)

    applicationContextHolder(ApplicationContextHolder) { bean ->
        bean.factoryMethod = 'getInstance'
    }

}
