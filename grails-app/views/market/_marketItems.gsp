<h3>Market</h3>
<table cellpadding="0" cellspacing="0" border='0'>
    <tr>
        <td></td>
        <td>Name</td>
        <td>Cost</td>
    </tr>
    <g:each in="${marketItems}" var="marketItem">
        <tr>
            <%-- <td><img src='images/items/" . $row["img"] . "'></td> --%>
            <td>${marketItem.item.name}</td>
            <td>${marketItem.item.cost}</td>
            <td><a href='markt.php?action=buy&id=" . $row["itemId"] . "'>buy</td>
        </tr>
    </g:each>
</table>