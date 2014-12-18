Money: {{model.money}}

<div>
    <h3>Market</h3>
    <table cellpadding="0" cellspacing="0" border='0'>
        <tr>
            <td></td>
            <td>Name</td>
            <td>Cost</td>
            <td></td>
        </tr>
        <tr ng-repeat="marketItem in model.items">
            <td><img ng-src='${resource(uri:'')}/images/items/{{marketItem.image}}'></td>
            <td style="width:200px;">{{marketItem.name}}</td>
            <td style="width:50px;">{{marketItem.cost}}</td>
            <td><a href='#' ng-click="buyItem(marketItem.id)">buy</a></td>
        </tr>
    </table>
</div>
<p>
    <a href='#' ng-click="exit()">Exit</a>
</p>