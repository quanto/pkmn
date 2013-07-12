<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <title></title>
        <meta name="layout" content="game"/>
    </head>
    <body>
        <g:form method="post" action="forgotPassword">
            <table>
                <tr>
                    <td>
                        E-mail
                    </td>
                    <td>
                        <g:textField name="email" />
                    </td>
                </tr>
                <tr>
                    <td></td>
                    <td>
                        <g:submitButton name="Recover" />
                    </td>
                </tr>
            </table>
        </g:form>
    </body>
</html>