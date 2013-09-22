package game.admin

import javax.imageio.ImageIO
import game.MapLayout
import game.FightFactoryService
import game.Player
import game.OwnerPokemon
import game.OwnerMove

import javax.swing.ImageIcon
import java.awt.AlphaComposite
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.GraphicsConfiguration
import java.awt.GraphicsDevice
import java.awt.GraphicsEnvironment
import java.awt.HeadlessException
import java.awt.Image
import java.awt.Toolkit
import java.awt.Transparency
import java.awt.image.BufferedImage
import java.awt.image.ColorModel
import java.awt.image.PixelGrabber

class TestController {

    FightFactoryService fightFactoryService

    def sessionRegistry

    def gen(){

        Image img
        if (new File("web-app/images/pkmn.png").exists()){
            while (!img || img.getHeight(null) < 0 || img.getWidth(null) < 0){
                println "load"
                img = Toolkit.getDefaultToolkit().getImage("web-app/images/pkmn.png")
            }
        }

        def bufferedImg = MapLayout.toBufferedImage(img)

//        (25..26).each{ int count ->
            int count = 151 + 1
            int correctionLeft = 0
            int countCorrection = -1

            int row = Math.floor(count / 15)

            int blockWidth = (65*(count % 15))
            int blockHeight = (129*(row)) + 3
            if (blockWidth){
                blockWidth += 1
            }

            int width = 31
            width = 27

            int height = 31


//            println blockWidth


            def back1 = bufferedImg.getSubimage(blockWidth+0,blockHeight+0,width,height)
            def back2 = bufferedImg.getSubimage(blockWidth + 0,blockHeight+32,width,height)
            def front1 = bufferedImg.getSubimage(blockWidth + 0,blockHeight+64,width,height)
            def front2 = bufferedImg.getSubimage(blockWidth + 0,blockHeight+96,width,height)

            def left1 = bufferedImg.getSubimage(blockWidth + 32,blockHeight+0,width,height)
            def left2 = bufferedImg.getSubimage(blockWidth + 32,blockHeight+32,width,height)
            def right1 = bufferedImg.getSubimage(blockWidth + 32,blockHeight+64,width,height)
            def right2 = bufferedImg.getSubimage(blockWidth + 32,blockHeight+96,width,height)

            BufferedImage bufferedImage = new BufferedImage(32*4, 32*4, BufferedImage.TYPE_INT_ARGB);

            Graphics2D g2d = bufferedImage.createGraphics();

            g2d.setComposite(AlphaComposite.Src)

            g2d.drawImage(front1, 0, 0,back1.getWidth(null),back1.getHeight(null), null);
            g2d.drawImage(front2, 32, 0,back2.getWidth(null),back2.getHeight(null), null);
            g2d.drawImage(front1, 32*2, 0,back1.getWidth(null),back1.getHeight(null), null);
            g2d.drawImage(front2, 32*3, 0,back2.getWidth(null),back2.getHeight(null), null);

            g2d.drawImage(back1, 0, 32,back1.getWidth(null),back1.getHeight(null), null);
            g2d.drawImage(back2, 32, 32,back2.getWidth(null),back2.getHeight(null), null);
            g2d.drawImage(back1, 32*2, 32,back1.getWidth(null),back1.getHeight(null), null);
            g2d.drawImage(back2, 32*3, 32,back2.getWidth(null),back2.getHeight(null), null);

            g2d.drawImage(left1, 0, 32*2,back1.getWidth(null),back1.getHeight(null), null);
            g2d.drawImage(left2, 32, 32*2,back2.getWidth(null),back2.getHeight(null), null);
            g2d.drawImage(left1, 32*2, 32*2,back1.getWidth(null),back1.getHeight(null), null);
            g2d.drawImage(left2, 96, 32*2,back2.getWidth(null),back2.getHeight(null), null);

            g2d.drawImage(right1, 0, 32*3,back1.getWidth(null),back1.getHeight(null), null);
            g2d.drawImage(right2, 32, 32*3,back2.getWidth(null),back2.getHeight(null), null);
            g2d.drawImage(right1, 32*2, 32*3,back1.getWidth(null),back1.getHeight(null), null);
            g2d.drawImage(right2, 32*3, 32*3,back2.getWidth(null),back2.getHeight(null), null);

            g2d.dispose()

            ImageIO.write(bufferedImage, "png", new File("web-app/images/followers/${count+countCorrection}.png"))

            render text:"<img src='/game/images/followers/${count+countCorrection}.png'/>${count+countCorrection}"
//        }
    }

    public static boolean hasAlpha(Image image) {
        // If buffered image, the color model is readily available
        if (image instanceof BufferedImage) {
            BufferedImage bimage = (BufferedImage)image;
            return bimage.getColorModel().hasAlpha();
        }

        // Use a pixel grabber to retrieve the image's color model;
        // grabbing a single pixel is usually sufficient
        PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
        }

        // Get the image's color model
        ColorModel cm = pg.getColorModel();
        return cm.hasAlpha();
    }

    def test2(){
        // 0, 407 rows





        int total = 407 * 24;

        int i=0;

        def data = []
        def rowData = []
        for(i=0;i<total;i++)
        {
            int x = i % 8;

            int row = Math.floor(i / 24);
            int cell = Math.floor((i % 24) / 8);
            int y = row + (cell * 15);

            rowData.add("${x}${y}")

            if (i % 24 == 23)
            {
                data.add(rowData)
                rowData = []
            }
        }

        String filePath = "/images/generatedMaps/tileset.png"

        File file = new File("web-app" + filePath)

        println data

        MapLayout mapLayout = new MapLayout(
            background: data
        )

        ImageIO.write(mapLayout.writeTiles(data), "png", file)
        render text: "done"
    }

    def index() {                  //
        def test = """
            if (pokemonType1 == "fire" && pokemonType2 == "")
                effectiveness = 0.5
            else if (pokemonType1 == "water" && pokemonType2 == "")
                effectiveness = 0.5
            else if (pokemonType1 == "electric" && pokemonType2 == "")
                effectiveness = 0.5
            else if (pokemonType1 == "ice" && pokemonType2 == "")
                effectiveness = 2
            else if (pokemonType1 == "rock" && pokemonType2 == "")
                effectiveness = 2
            else if (pokemonType1 == "steel" && pokemonType2 == "")
                effectiveness = 0.5
            else if (pokemonType1 == "normal" && pokemonType2 == "water")
                effectiveness = 0.5
            else if (pokemonType1 == "fire" && pokemonType2 == "fighting")
                effectiveness = 0.5
            else if (pokemonType1 == "fire" && pokemonType2 == "ground")
                effectiveness = 0.5
            else if (pokemonType1 == "fire" && pokemonType2 == "flying")
                effectiveness = 0.5
            else if (pokemonType1 == "fire" && pokemonType2 == "steel")
                effectiveness = 0.25
            else if (pokemonType1 == "water" && pokemonType2 == "electric")
                effectiveness = 0.25
            else if (pokemonType1 == "water" && pokemonType2 == "grass")
                effectiveness = 0.5
            else if (pokemonType1 == "water" && pokemonType2 == "fighting")
                effectiveness = 0.5
            else if (pokemonType1 == "water" && pokemonType2 == "poison")
                effectiveness = 0.5
            else if (pokemonType1 == "water" && pokemonType2 == "ground")
                effectiveness = 0.5
            else if (pokemonType1 == "water" && pokemonType2 == "flying")
                effectiveness = 0.5
            else if (pokemonType1 == "water" && pokemonType2 == "psychic")
                effectiveness = 0.5
            else if (pokemonType1 == "water" && pokemonType2 == "dragon")
                effectiveness = 0.5
            else if (pokemonType1 == "water" && pokemonType2 == "steel")
                effectiveness = 0.25
            else if (pokemonType1 == "water" && pokemonType2 == "dark")
                effectiveness = 0.5
            else if (pokemonType1 == "electric" && pokemonType2 == "flying")
                effectiveness = 0.5
            else if (pokemonType1 == "electric" && pokemonType2 == "ghost")
                effectiveness = 0.5
            else if (pokemonType1 == "electric" && pokemonType2 == "steel")
                effectiveness = 0.25
            else if (pokemonType1 == "ice" && pokemonType2 == "grass")
                effectiveness = 2
            else if (pokemonType1 == "ice" && pokemonType2 == "ground")
                effectiveness = 2
            else if (pokemonType1 == "ice" && pokemonType2 == "flying")
                effectiveness = 2
            else if (pokemonType1 == "ice" && pokemonType2 == "psychic")
                effectiveness = 2
            else if (pokemonType1 == "ice" && pokemonType2 == "ghost")
                effectiveness = 2
            else if (pokemonType1 == "fighting" && pokemonType2 == "steel")
                effectiveness = 0.5
            else if (pokemonType1 == "ground" && pokemonType2 == "rock")
                effectiveness = 2
            else if (pokemonType1 == "bug" && pokemonType2 == "water")
                effectiveness = 0.5
            else if (pokemonType1 == "bug" && pokemonType2 == "rock")
                effectiveness = 2
            else if (pokemonType1 == "bug" && pokemonType2 == "steel")
                effectiveness = 0.5
            else if (pokemonType1 == "rock" && pokemonType2 == "grass")
                effectiveness = 2
            else if (pokemonType1 == "rock" && pokemonType2 == "ground")
                effectiveness = 2
            else if (pokemonType1 == "rock" && pokemonType2 == "flying")
                effectiveness = 2
            else if (pokemonType1 == "rock" && pokemonType2 == "psychic")
                effectiveness = 2
            else if (pokemonType1 == "rock" && pokemonType2 == "bug")
                effectiveness = 2
            else if (pokemonType1 == "rock" && pokemonType2 == "dark")
                effectiveness = 2
            else if (pokemonType1 == "dark" && pokemonType2 == "fire")
                effectiveness = 0.5
            else if (pokemonType1 == "dark" && pokemonType2 == "ice")
                effectiveness = 2
            else if (pokemonType1 == "steel" && pokemonType2 == "ground")
                effectiveness = 0.5
            else if (pokemonType1 == "steel" && pokemonType2 == "flying")
                effectiveness = 0.5
            else if (pokemonType1 == "steel" && pokemonType2 == "psychic")
                effectiveness = 0.5
            else if (pokemonType1 == "steel" && pokemonType2 == "dragon")
                effectiveness = 0.5
        """
        test.split('pokemonType1 == "').each {
            def parts = it.split('"')
                if (parts.size() > 2){
                    def effect =  parts[3].split("effectiveness = ").last().split('\\n')[0]
                    if (effect != "1"){
                        render text:  "steel"
                        render text:  "<br />"
                        render text:  parts[0]
                        render text:  "<br />"
                        render text:  parts[2]
                        render text:  "<br />"

                        render text: effect
                        render text:  "<br />"
                    }
                }
//            it.split('" && pokemonType2 == "').each {
//                println it.split('"')
//            }
        }
    }
}
