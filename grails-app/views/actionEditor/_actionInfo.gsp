<%@ page import="game.NpcAction; game.MapMessage" %>
<div>
    <table>
        <tr>
            <td>class</td>
            <td>${action.class}</td>
        </tr>
        <tr>
            <td>positionX</td>
            <td>${action.positionX}</td>
        </tr>
        <tr>
            <td>positionY</td>
            <td>${action.positionY}</td>
        </tr>
        <g:if test="${action in MapMessage}">


        </g:if>
        <g:elseif test="${action in NpcAction}">
             <tr>
                 <td>

                 </td>
                 <td>

                 </td>
             </tr>

        </g:elseif>
    </table>
</div>
