package data

import game.Market
import game.Npc
import game.Owner
import game.OwnerItem
import game.Item

class MarketImport {

    public static void importMarket(Market market){

        println "Import market ${market.identifier}"

        File file = new File('import/markets/' + market.identifier + '.txt')

        String node = ""

        def parts = []

        file.eachLine { line ->

            if (line.contains("</market>")){
                node = ""
//                ownerPokemon = PlayerImport.createOwnerPokemon(npc, parts)
//                ownerPokemon.save()
                parts = []
            }
            else if (line.contains("</marketItem>")){
                node = ""
//                PlayerImport.createOwnerMove(ownerPokemon, parts)
                parts = []
            }
            if (node){
                parts.add(line)
            }

            if (line.contains("<market>")){
                node = "market"
            }
            else if (line.contains("<marketItem>")){
                node = "marketItem"
            }

        }

    }

    public static void createMarketItem(Owner owner,def parts){
//        new MarketItem(
//                item: Item.findByName(parts[0]),
//                quantity: Integer.parseInt(parts[1])
//        ).save()
    }

}
