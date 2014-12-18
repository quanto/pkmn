<button ng-click="tab = 'usableItems'">Usable Items</button>
<button ng-click="tab = 'badges'">Badges</button>
<button ng-click="tab = 'keyItems'">Key Items</button>

<table cellpadding="0" cellspacing="0" border='0' ng-show="tab == 'usableItems'">
    <tr>
        <td></td>
        <td>
            Name
        </td>
        <td>
            Quantity
        </td>
    </tr>
    <tr ng-repeat="item in model.usableItems">
        <td>
            <span ng-show="item.image">
                <span ng-init="imageUrl = item.image?'${resource(uri:'')}/images/items/' + item.image:''"></span>
                <img ng-src="{{imageUrl}}">
            </span>
        </td>
        <td style="width:200px;">{{item.name}}</td>

        <td>{{item.quantity}}</td>

    </tr>

</table>

<table cellpadding="0" cellspacing="0" border='0' ng-show="tab == 'keyItems'">
    <tr>
        <td></td>
        <td>
            Name
        </td>
    </tr>
    <tr ng-repeat="item in model.keyItems">
        <td>
            <span ng-show="item.image">
                <img ng-src='${resource(uri:'')}/images/items/{{item.image}}'>
            </span>
        </td>
        <td style="width:200px;">{{item.name}}</td>

    </tr>

</table>

<table ng-show="tab == 'badges'">

    <tr ng-repeat="item in model.badges">
        <td><img ng-src='${resource(uri:'')}/images/badges/{{item.image}}'></td>
        <td style="width:200px;">{{item.name}}</td>
    </tr>

</table>