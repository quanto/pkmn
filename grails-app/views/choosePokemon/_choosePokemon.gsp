Welcome to the world of Pok&eacute;mon!<br />
<br />
Please choose the Pok&eacute;mon you want to start with.<br />
<br />
<table>
    <tr>
        <td><img src="${resource(uri:'')}/images/pkmn/001ani.gif" /></td>
        <td><img src="${resource(uri:'')}/images/pkmn/004ani.gif" /></td>
        <td><img src="${resource(uri:'')}/images/pkmn/007ani.gif" /></td>
    </tr>
    <tr>
        <td><input type="radio" value='1' ng-model='pokemonId' /></td>
        <td><input type="radio" value='4' ng-model='pokemonId' /></td>
        <td><input type="radio" value='7' ng-model='pokemonId' /></td>
    </tr>
</table>
<input value='Choose' type='button' ng-click='choose();' />