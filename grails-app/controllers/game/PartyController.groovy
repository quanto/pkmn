package game

class PartyController {

    def index() {
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        boolean computerView = false
        def ownerPokemonList = OwnerPokemon.findAllByPartyPositionGreaterThanAndOwner(0,player)

        render text: g.render(template: 'party', model: [computerView:computerView,ownerPokemonList:ownerPokemonList])
    }

    def moveUp = {

        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

//        // error checking
//        if ($ownerpokemon->ownerId != $owner->id)
//        {
//            die("Pokemon niet van eigenaar!");
//        }
//        else if ($ownerpokemon->partyPosition == 1)
//        {
//            die ("Pokemon kan niet verplaatst worden");
//        }
//        else if ($ownerpokemon->partyPosition == 0)
//        {
//            die ("Pokemon is niet in party");
//        }
//
//        $sql = "select * from ownerpokemon where partyPosition = '" . ($ownerpokemon->partyPosition - 1) . "' and ownerId = '" . $owner->id . "'";
//        $result = DatabaseQuery::execute($sql);
//
//        if (mysql_num_rows($result) != 0)
//        {
//            $row = mysql_fetch_array($result);
//            DatabaseQuery::execute("update ownerpokemon set partyPosition = '" . $ownerpokemon->partyPosition . "' where partyPosition = '" . $row["partyPosition"] . "' AND ownerId = '" . $owner->id . "'");
//        }
//
//        $ownerpokemon->partyPosition -= 1;
//        $ownerpokemon->update();
//
//        if ($owner->view != 2)
//            header("Location: game.php");
//        else
//            header("Location: computer.php");
    }

    def moveDown = {

    }

}
