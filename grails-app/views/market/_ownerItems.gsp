<table cellpadding="0" cellspacing="0" border='0'>
    <tr>
        <td></td>
        <td>
            Name
        </td>
        <td>
            Quantity
        </td>
    </tr>
    <g:each in="${ownerItems}" var="ownerItem">
        <tr>
            <td><img src='${resource(uri:'')}/images/items/${ownerItem.item.image}'></td>
            <td style="width:200px;">${ownerItem.item.name}</td>
            <td>${ownerItem.quantity}</td>
        </tr>
    </g:each>
</table>