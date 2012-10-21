package data

import game.Market

class MarketBackup {

    public static void saveMarket(Market market){
        try{
            File file = new File("import/markets/" + market.identifier + ".txt")

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
"""
        return marketData
    }

}
