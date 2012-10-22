<div>
    <h3>Market</h3>
    <table cellpadding="0" cellspacing="0" border='0'>
        <tr>
            <td></td>
            <td>Name</td>
            <td>Cost</td>
            <td></td>
        </tr>
        <g:each in="${marketItems}" var="marketItem">
            <tr>
                <td><img src='${resource(uri:'')}/images/items/${marketItem.item.image}'></td>
                <td style="width:200px;">${marketItem.item.name}</td>
                <td style="width:50px;">${marketItem.item.cost}</td>
                <td><a href='${createLink(action:'buy',id:marketItem.item.id)}'>buy</a></td>
            </tr>
        </g:each>
    </table>
</div>