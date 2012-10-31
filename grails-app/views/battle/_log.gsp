<%@ page import="game.context.Fight" %>
<%
    Fight fight = fight
%>
<g:if test="${myFightPlayer.waitOnOpponentMove}">

    $("#menu").html("");
    setTimeout(function(){ doAction("${createLink(action:'logRequest')}"); },2000)

</g:if>
<g:else>
    $("#menu").html("");
    string = "${fight.roundResult.toBattleString()}";
    alert(string)
    prepareActions();
    combatActions();
</g:else>