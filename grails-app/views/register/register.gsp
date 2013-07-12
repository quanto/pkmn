<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <title>Register</title>
        <meta name="layout" content="game"/>
    </head>
    <body>
        <g:form>
            <g:hasErrors bean="${player}">
                <ul class="errors" role="alert">
                    <g:eachError bean="${player}" var="error">
                        <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>>
                            <g:message error="${error}"/>
                        </li>
                    </g:eachError>
                </ul>
            </g:hasErrors>

            <table>
                <tr>
                    <td>
                        Username:
                    </td>
                    <td>
                        <g:textField name="username" value="${player?.username}" />
                    </td>
                </tr>
                <tr>
                    <td>
                        Password:
                    </td>
                    <td>
                        <g:passwordField name="password" value="${player?.password}" />
                    </td>
                </tr>
                <tr>
                    <td>
                        Name:
                    </td>
                    <td>
                        <g:textField name="name" value="${player?.name}" />
                    </td>
                </tr>
                <tr>
                    <td>
                    </td>
                    <td>
                        <g:submitButton name="Register" />
                    </td>
                </tr>
            </table>
        </g:form>
    </body>
</html>