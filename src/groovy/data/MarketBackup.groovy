package data

import game.Market
import game.MarketItem

class MarketBackup {

    public static void saveMarket(Market market){
        try{
            def file = Thread.currentThread().getContextClassLoader().getResourceAsStream("import/markets/" + market.identifier + ".txt")

            FileWriter fstream = new FileWriter(file);
            BufferedWriter out = new BufferedWriter(fstream);

            out.write(getMarketData(market))

            out.close();
        }catch (Exception e){
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static String getMarketData(Market market){
        String marketData = """<marketData>
${market.identifier}
</marketData>
${getMarketItemData2(market)}
"""
        return marketData
    }

    public static String getMarketItemData2(Market market){
        String marketItemData = ""
        market.marketItems.each{ MarketItem marketItem ->
            marketItemData += """<marketItemData>
${marketItem.item.name}
</marketItemData>
"""


        }
        return marketItemData
    }

}
