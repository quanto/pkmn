package data

import game.Owner
import game.OwnerItem

class ItemBackup {

    public static String getItemBackupData(Owner owner){
        String itemData = ""

        owner.ownerItems.each { OwnerItem ownerItem ->
            itemData += """<ownerItem>
${ownerItem.item.name}
${ownerItem.quantity}
</ownerItem>
            """
        }
        return itemData
    }

}
