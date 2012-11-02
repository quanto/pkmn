<% import grails.persistence.Event %>
<% import scaffolding.ScaffoldTool %>
<%=packageName%>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="\${message(code: '${domainClass.propertyName}.label', default: '${className}')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-${domainClass.propertyName}" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="\${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-${domainClass.propertyName}" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="\${flash.message}">
			<div class="message" role="status">\${flash.message}</div>
			</g:if>
            <g:form method="GET" action="\${actionName}">
                <table>
                    <thead>

                        <%
                            // Try to get list properties from the domainClass
                            def scaffoldList = domainClass.getPropertyOrStaticPropertyOrFieldValue('scaffoldList', List)
                            def scaffoldSearch = domainClass.getPropertyOrStaticPropertyOrFieldValue('scaffoldSearch', List)

                            // Get the list propties the old way
                            if (!scaffoldList){
                                excludedProps = Event.allEvents.toList() << 'version'
                                allowedNames = domainClass.persistentProperties*.name << 'dateCreated' << 'lastUpdated' << 'id'
                                props = domainClass.properties.findAll { allowedNames.contains(it.name) && !excludedProps.contains(it.name) && it.type != null && !Collection.isAssignableFrom(it.type) }
                                Collections.sort(props, comparator.constructors[0].newInstance([domainClass] as Object[]))
                                scaffoldList = props.collect { it.name }
                            }

                            // Search header
                            if (scaffoldSearch){
                                %>
                                <tr>
                                    <%
                                    scaffoldList.eachWithIndex { p, i ->
                                        def clazz = ScaffoldTool.getDownLevelClass(domainClass.getClazz(), p)

                                        if (clazz in Enum) {
                                            def enumValues = [:]
                                            clazz?.values().each{ enumValues.put(it.name(),"'" + it.toString() + "'") }
                                            %>
                                            <td><g:select name="${p}" optionKey="key" optionValue="value" from="\${${enumValues}}" value="\${params.'${p}'}" noSelection="['':'']" /></td>
                                        <%
                                        }
                                        else if (clazz in Boolean || clazz in boolean.class) { %>
                                            <td><g:select name="${p}" from="\${['Ja', 'Nee']}" value="\${params.'${p}'}" noSelection="['':'']" /></td>
                                        <%
                                        }
                                        else if (clazz in Date) { %>
                                            <td>
                                                <g:hiddenField name="${p}" value="java.util.Date" class="datepicker" />
                                                <div class="datepickerContainer">
                                                    <g:textField name="${p}_from" value="\${params.'${p}_from'}" title="Van" class="datepicker" />
                                                    <g:textField name="${p}_to" value="\${params.'${p}_to'}" title="Tot" class="datepicker" />
                                                </div>
                                            </td>
                                        <%
                                        }
                                        else { %>
                                            <td><g:textField name="${p}" value="\${params.'${p}'}"/></td>
                                        <%
                                        }
                                        %>

                                        <%
                                    }
                                %>
                                </tr>
                                <%
                            }
                        %>
                        <tr>
                        <%
                            // The header
                            scaffoldList.eachWithIndex { p, i ->

                                    def clazz = ScaffoldTool.getDownLevelClass(domainClass.getClazz(), p)

                                    if (clazz in List) { %>
                                        <th><g:message code="${domainClass.propertyName}.${p}.label" default="${p}" /></th>
                                    <%
                                    } else { %>
                                        <g:sortableColumn property="${p}" title="\${message(code: '${domainClass.propertyName}.${p}.label', default: '${p}')}" />
                                    <%
                                    }
                            } %>
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="\${${propertyName}List}" status="i" var="${propertyName}">
                        <tr class="\${(i % 2) == 0 ? 'even' : 'odd'}">
                        <%  scaffoldList.eachWithIndex { p, i ->
                            def clazz = ScaffoldTool.getDownLevelClass(domainClass.getClazz(), p)

                                if (i == 0) { %>
                                    <td><g:link action="show" id="\${${propertyName}.id}">\${fieldValue(bean: ${propertyName}, field: "${p}")}</g:link></td>
                        <%      } else {
                                    if (clazz in Boolean || clazz in boolean) { %>
                                        <td><g:formatBoolean boolean="\${${propertyName}.${p}}" /></td>
                        <%          } else if (clazz in Date || clazz in java.sql.Date || clazz in java.sql.Time || clazz in Calendar) { %>
                                        <td><g:formatDate date="\${${propertyName}.${p}}" /></td>
                        <%          } else { %>
                                        <td>\${fieldValue(bean: ${propertyName}, field: "${p}")}</td>
                        <%  }   }   } %>
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </g:form>
			<div class="pagination">
				<g:paginate total="\${${propertyName}Total}" />
			</div>
		</div>
	</body>
</html>
