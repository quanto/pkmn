<%@ page import="game.Fight" %>
<%
    Fight fight = fight
%>
$("#menu").html("");
string = "${fight.log}";
player1health = ${fight.fightPlayer1.hp};
player2health = ${fight.fightPlayer2.hp};
prepareActions();
combatActions();
