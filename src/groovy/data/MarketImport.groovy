package data

import game.Market
import game.item.Item
import game.MarketItem

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
            else if (line.contains("</marketItemData>")){
                node = ""
                createMarketItem(market, parts)
                parts = []
            }
            if (node){
                parts.add(line)
            }

            if (line.contains("<market>")){
                node = "market"
            }
            else if (line.contains("<marketItemData>")){
                node = "marketItem"
            }

        }

    }

    public static void createMarketItem(Market market,def parts){
        MarketItem marketItem = new MarketItem(
                market: market,
                item: Item.findByName(parts[0])
        )
        market.addToMarketItems(marketItem)
    }

}
