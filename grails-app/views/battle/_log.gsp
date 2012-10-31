<%@ page import="game.context.Fight" %>
<%
    Fight fight = fight
%>

$("#menu").html("");
string = "${fight.roundResult.toBattleString(myFightPlayer)}";
prepareActions();
combatActions();

<g:if test="${myFightPlayer.waitOnOpponentMove}">
    <%-- Set timeout that triggers this log again --%>
    setTimeout(function(){ doAction("${createLink(action:'logRequest')}"); },2000)
</g:if>
