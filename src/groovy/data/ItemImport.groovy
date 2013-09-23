package data

import game.item.UsableItem
import game.item.Badge
import game.item.KeyItem
import game.item.PokeBallItem

class ItemImport {


    public static void importItems(){
        println "Import item lines"

        importUsableItems()

        importBadges()

        importKeyItems()
    }

    public static void importUsableItems(){
        def file = new File('import/items.txt')

        int index = 0
        def parts = []

        file.eachLine { line ->
            parts.add( line )
            if (index%5==4){

                UsableItem item

                if (parts[0].toString().toLowerCase().contains("ball")){
                    item = new PokeBallItem(
                            name : parts[0],
                            effect : parts[1],
                            cost : Integer.parseInt(parts[2]),
                            implemented : parts[3] == '1',
                            image:parts[4]
                    )
                }
                else{
                    item = new UsableItem(
                            name : parts[0],
                            effect : parts[1],
                            cost : Integer.parseInt(parts[2]),
                            implemented : parts[3] == '1',
                            image:parts[4]
                    )
                }
                item.save()

                parts = []

            }
            index++
        }
    }

    public static void importBadges(){
        //http://bulbapedia.bulbagarden.net/wiki/Badge#Boulder_Badge
        def file = new File('import/badges.txt')

        int index = 0
        def parts = []

        file.eachLine { line ->
            parts.add( line )
            if (index%2==1){

                Badge badge = new Badge(
                        name : parts[0],
                        image:parts[1]
                )

                badge.save()

                parts = []

            }
            index++
        }
    }

    public static void importKeyItems(){
        def file = new File('import/keyitems.txt')

        int index = 0
        def parts = []

        file.eachLine { line ->
            parts.add( line )
            if (index%2==1){

                KeyItem keyItem = new KeyItem(
                        name : parts[0],
                        image:parts[1],
                        cost : Integer.parseInt(parts[2]),
                )

                keyItem.save()

                parts = []

            }
            index++
        }
    }

}
