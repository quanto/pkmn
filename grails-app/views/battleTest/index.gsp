<%@ page import="game.Pokemon" contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <title></title>
        <meta name='layout' content='main'/>
    </head>
    <body>
        <g:form>
            Level: <g:textField name="level" />
            <g:select name="pokemon" from="${Pokemon.list()}" optionKey="id" />
            <g:submitButton name="start" value="start" />
        </g:form>
        <g:link action="moveOverview">Move Overview</g:link>
    </body>
</html>