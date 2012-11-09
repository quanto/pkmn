<g:render template="itemCategories" />
<table>
    <g:each in="${ownerItems}" var="ownerItem">
        <tr>
            <td><img src='${resource(uri:'')}/images/badges/${ownerItem.item.image}'></td>
            <td style="width:200px;">${ownerItem.item.name}</td>
        </tr>
    </g:each>
</table>