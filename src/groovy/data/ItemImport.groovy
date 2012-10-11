package data

import game.Item

class ItemImport {


    public static void importItems(){
        println "Import item lines"

        def file = new File('import/items.txt')

        int index = 0
        def parts = []

        file.eachLine { line ->
            parts.add( line )
            if (index%4==3){

                Item item = new Item(
                        name : parts[0],
                        effect : parts[1],
                        cost : Integer.parseInt(parts[2]),
                        active : parts[3] == '1'
                )

                item.save()

                parts = []

            }
            index++
        }
    }

}
