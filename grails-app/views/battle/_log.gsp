<%@ page import="game.context.Fight" %>
<%
    Fight fight = fight
%>
$("#menu").html("");
string = "${fight.roundResult.toBattleString()}";

prepareActions();
combatActions();
