<div style='float:left;'>
    <h3>Item</h3>
    <table cellpadding="0" cellspacing="0" border='0'>
        <g:each in="${ownerItems}" var="ownerItem">
            <tr>
                <%-- <td><img src='images/items/" . $row["img"] . "'></td> --%>
                <td>${ownerItem.item.name}</td>
                <td>${ownerItem.quantity}</td>
            </tr>
        </g:each>
    </table>
</div>