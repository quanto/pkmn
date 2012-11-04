package game

import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import java.awt.Graphics2D
import java.awt.Image
import java.awt.AlphaComposite
import java.awt.Toolkit
import java.awt.image.RenderedImage
import java.awt.GraphicsEnvironment
import java.awt.GraphicsDevice
import java.awt.GraphicsConfiguration
import java.awt.HeadlessException
import java.awt.Graphics
import javax.swing.ImageIcon
import java.awt.image.PixelGrabber
import java.awt.image.ColorModel
import java.awt.Transparency

/**
 * Created with IntelliJ IDEA.
 * User: kevinverhoef
 * Date: 04-10-12
 * Time: 15:12
 * To change this template use File | Settings | File Templates.
 */
class MapLayout {

    Map map
    def background = []
    def foreground = []

    public int getRows(){
        return background.size()
    }

    public int getColumns(){
        if (background.size()){
            return background.last().size()
        }
    }

    public static MapLayout createMapArray(Map map)
    {

        MapLayout mapLayout = new MapLayout()
        mapLayout.map = map

        def mapRowsBackground = map.dataBackground.split('-')

        mapLayout.background = []
        for(int y=0; y<mapRowsBackground.length; y++)
        {
            def tileNr = mapRowsBackground[y].split(',')

            def row = []
            for(int x=0; x < tileNr.length; x++)
            {
                row.add(tileNr[x])
            }
            mapLayout.background.add(row)
        }

        def mapRowsForeground = map.dataForeground.split('-')
        mapLayout.foreground = []
        for(int y=0; y<mapRowsForeground.length; y++)
        {
            def tileNr = mapRowsForeground[y].split(',')

            def row = []
            for(int x=0; x < tileNr.length; x++)
            {
                row.add(tileNr[x])
            }
            mapLayout.foreground.add(row)
        }

        return mapLayout;
    }

    public RenderedImage writeTiles(def data) {
        int width = getRows()*16;
        int height = getColumns()*16;

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = bufferedImage.createGraphics();

        g2d.setComposite(AlphaComposite.Src)



        data.eachWithIndex { def row, def y ->

            row.eachWithIndex { def tileNr, def x ->
                if (tileNr && tileNr != "0" && tileNr != "undefined"){
                    Image img
                    if (new File("web-app/images/tiles/sheet1/${tileNr}.png").exists()){
                        while (!img || img.getHeight(null) < 0 || img.getWidth(null) < 0)
                            img = Toolkit.getDefaultToolkit().getImage("web-app/images/tiles/sheet1/${tileNr}.png")
                    }

                    if (img && img.getWidth(null) > 0 && img.getHeight(null) > 0){
                        g2d.drawImage(toBufferedImage(img), x*16, y*16,img.getWidth(null),img.getHeight(null), null);
                    }
                }
            }

        }

        g2d.dispose();

        return bufferedImage;
    }

    public static BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage)image;
        }

        // This code ensures that all the pixels in the image are loaded
        image = new ImageIcon(image).getImage();

        // Determine if the image has transparent pixels; for this method's
        // implementation, see Determining If an Image Has Transparent Pixels
        boolean hasAlpha = hasAlpha(image);

        // Create a buffered image with a format that's compatible with the screen
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            // Determine the type of transparency of the new buffered image
            int transparency = Transparency.OPAQUE;
            if (hasAlpha) {
                transparency = Transparency.BITMASK;
            }

            // Create the buffered image
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(
                    image.getWidth(null), image.getHeight(null), transparency);
        } catch (HeadlessException e) {
            // The system does not have a screen
        }

        if (bimage == null) {
            // Create a buffered image using the default color model
            int type = BufferedImage.TYPE_INT_RGB;
            if (hasAlpha) {
                type = BufferedImage.TYPE_INT_ARGB;
            }
            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }

        // Copy image to buffered image
        Graphics g = bimage.createGraphics();

        // Paint the image onto the buffered image
        g.drawImage(image, 0, 0, null);
        g.dispose();

        return bimage;
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

}
