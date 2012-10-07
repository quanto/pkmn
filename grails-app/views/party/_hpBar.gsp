<%
    int percentage = Math.round(100 / maxHp * hp);
%>
<img src='${resource(uri:'')}/images/bar/left.png'><g:if test="${percentage != 0}"><img src='${resource(uri:'')}/images/bar/green.png' style='width:${percentage / 2}px;height:6px;'></g:if><g:if test="${percentage != 100}"><img src='${resource(uri:'')}/images/bar/grey.png' style='width:${((100 - percentage) / 2)}px;height:6px;'></g:if><img src='${resource(uri:'')}/images/bar/right.png'>
