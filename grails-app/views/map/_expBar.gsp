<%@ page import="game.EXP" %>
<%
    int percentage = EXP.getExpPercentage(level, levelRate, xp);
%>
<img src='${resource(uri:'')}/images/bar/exp.png'><g:if test="${percentage != 0}"><img src='${resource(uri:'')}/images/bar/blue.png' style='width:${percentage / 2}px;height:6px;'></g:if><g:if test="${percentage != 100}"><img src='${resource(uri:'')}/images/bar/grey.png' style='width:${(100 - percentage) / 2}px;height:6px;'></g:if><img src='${resource(uri:'')}/images/bar/right.png'>