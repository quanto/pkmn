<%@ page import="grails.plugin.springsecurity.SpringSecurityUtils" %>
<g:render template="itemCategories" />
<table cellpadding="0" cellspacing="0" border='0'>
    <tr>
        <td></td>
        <td>
            Name
        </td>
        <g:if test="${!(ownerItems.count{it.item in game.item.KeyItem})}">
            <td>
                Quantity
            </td>
        </g:if>
    </tr>
    <g:each in="${ownerItems}" var="ownerItem">
        <g:if test="${!ownerItem.item.hidden || SpringSecurityUtils.ifAllGranted('ROLE_ADMIN')}">
            <tr>
                <td>
                    <g:if test="${ownerItem.item.image}">
                        <img src='${resource(uri:'')}/images/items/${ownerItem.item.image}'>
                    </g:if>
                </td>
                <td style="width:200px;">${ownerItem.item.name}</td>
                <g:if test="${!(ownerItem.item in game.item.KeyItem)}">
                    <td>${ownerItem.quantity}</td>
                </g:if>
            </tr>
        </g:if>
    </g:each>
</table>