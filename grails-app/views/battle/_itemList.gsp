<p>Item<br>
<table>
    <g:each in="${ownerItems}" var="ownerItem">
        <tr>
            <td>img</td>
            <td>${ownerItem.item.name}</td>
            <td>${ownerItem.quantity}</td>
            <td><a href="" onclick="return doAction('${createLink(action:'useItem',id:ownerItem.id)}');">use</a></td>

        </tr>
    </g:each>
</table>

<p>

<a href='' onclick="return getMenu('');">Back</a>