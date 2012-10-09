<style>

    body
    {
        font-family:verdana;
    }

    table.computerList
    {
        font-size:11px;
        border-collapse: collapse;
        border-bottom:1px solid #CCCCCC;
    }

    table.computerList td
    {
        background-color:#DDDDDD;
        height:35px;
        border-top:1px solid #CCCCCC;
    }

    img
    {
        border:0;
    }

    a:link, a:visited
    {
        color:black;
    }

</style>
<table>
    <tr>
        <td valign='top'>
            <g:render template="party" model="${[computerView:computerView,ownerPokemonList:partyList]}" />
        </td>
        <td valign='top'>
            <g:render template="storedPokemon" model="${[computerList:computerList]}" />
        </td>
    </tr>
</table>

<a href='${createLink(action:'exit')}'>Close</a>