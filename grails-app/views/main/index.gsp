<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <meta name="layout" content="game">
        <style>
            ul {
                list-style-position: inside;
            }
            #status, #page-body {
                padding-left:15px;
            }

        </style>
    </head>
    <body>
        <div class="nav" role="navigation">
            <ul>
                <g:render template="/scaffold/menu" />
            </ul>
        </div>

        <div id="status" role="complementary" style="float:left;">
            <h1>Application Status</h1>
            <ul>
                <li>App version: <g:meta name="app.version"/></li>
                <li>Grails version: <g:meta name="app.grails.version"/></li>
                <li>Groovy version: ${org.codehaus.groovy.runtime.InvokerHelper.getVersion()}</li>
                <li>JVM version: ${System.getProperty('java.version')}</li>
                <li>Reloading active: ${grails.util.Environment.reloadingAgentEnabled}</li>
                <li>Controllers: ${grailsApplication.controllerClasses.size()}</li>
                <li>Domains: ${grailsApplication.domainClasses.size()}</li>
                <li>Services: ${grailsApplication.serviceClasses.size()}</li>
                <li>Tag Libraries: ${grailsApplication.tagLibClasses.size()}</li>
            </ul>
            <h1>Installed Plugins</h1>
            <ul>
                <g:each var="plugin" in="${applicationContext.getBean('pluginManager').allPlugins}">
                    <li>${plugin.name} - ${plugin.version}</li>
                </g:each>
            </ul>
        </div>
        <div id="page-body" role="main" style="float:left;">

            <div id="controller-list" role="navigation">
                <h1>Available Controllers:</h1>
                <table style="width:auto;">
                    <tr>
                        <td valign="top">
                            <h2>Controllers:</h2>
                            <ul>
                                <g:each var="c" in="${grailsApplication.controllerClasses.sort { it.fullName } }">
                                    <g:if test="${!c.fullName.contains('scaffold')}">
                                        <li class="controller"><g:link controller="${c.logicalPropertyName}">${c.logicalPropertyName}</g:link></li>
                                    </g:if>
                                </g:each>
                            </ul>
                        </td>
                        <td style="padding-left: 100px" valign="top">
                            <h2>Scaffold Controllers:</h2>
                            <ul>
                                <g:each var="c" in="${grailsApplication.controllerClasses.sort { it.fullName } }">
                                    <g:if test="${c.fullName.contains('scaffold')}">
                                        <li class="controller"><g:link controller="${c.logicalPropertyName}">${c.logicalPropertyName}</g:link></li>
                                    </g:if>
                                </g:each>
                            </ul>
                        </td>
                    </tr>


                </table>
            </div>
        </div>
    </body>
</html>