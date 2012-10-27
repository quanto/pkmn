<%@ page import="game.Fight" %>
<%
    Fight fight = fight
%>
$("#menu").html("");
string = "${fight.roundResult.toBattleString()}";

pokemon[1].health = ${fight.fightPlayer1.hp}
pokemon[2].health = ${fight.fightPlayer2.hp}

prepareActions();
combatActions();
