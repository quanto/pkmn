<%@ page import="game.Pokemon" contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <meta name="layout" content="game">
    </head>
    <body>
        <g:link action="moveOverview">Move Overview</g:link>
        <g:link action="recover">Recover party</g:link>

        <h1>Start Fight</h1>
        <g:form>
            Level: <g:textField name="level" />
            <g:select name="pokemon" from="${Pokemon.list()}" optionKey="id" />
            <g:submitButton name="start" value="start" />
        </g:form>


        <h1>Set PKMN</h1>
        <g:form action="setPkmn">
            Level: <g:textField name="level" />
            <g:select name="pokemon" from="${Pokemon.list()}" optionKey="id" />
            <g:submitButton name="set" value="set" />
        </g:form>
    </body>
</html>