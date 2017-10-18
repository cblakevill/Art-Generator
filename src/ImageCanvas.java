import brushes.Brush;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ImageCanvas extends Canvas
{
    private Brush brush;
    private int iterations;
    private boolean running;
    private int imageWidth;
    private int imageHeight;
    private int windowWidth;
    private int windowHeight;
    private BufferedImage srcImage;
    private BufferedImage drawingImage;

    public ImageCanvas(Brush brush, int iterations)
    {
        this.brush = brush;
        this.iterations = iterations;

        this.addKeyListener(new KeyListener()
        {
            @Override
            public void keyTyped(KeyEvent e){}

            @Override
            public void keyPressed(KeyEvent e)
            {
                if(e.getKeyCode() == KeyEvent.VK_SPACE)
                    running = false;
            }

            @Override
            public void keyReleased(KeyEvent e){}
        });
    }

    public void setup(String file) throws IOException
    {
        running = true;
        this.initImage(file);
        this.loadColors(((DataBufferInt) srcImage.getRaster().getDataBuffer()).getData());
        this.setPreferredSize(new Dimension(windowWidth, windowHeight));
        this.setMaximumSize(new Dimension(windowWidth, windowHeight));
        this.setMinimumSize(new Dimension(windowWidth, windowHeight));
        this.createBufferStrategy(1);
    }

    public void draw()
    {
        brush.setDimensions(imageWidth, imageHeight);
        brush.setSrc(((DataBufferInt)srcImage.getRaster().getDataBuffer()).getData());
        brush.setDrawing(((DataBufferInt)drawingImage.getRaster().getDataBuffer()).getData());

        Graphics g = getBufferStrategy().getDrawGraphics();
        int iter = 0;
        while (running && iter < iterations)
        {
            brush.draw();
            if (iter % 5000 == 0)
            {
                g.drawImage(drawingImage, 0, 0, windowWidth, windowHeight, null);
            }
            iter++;
        }
        System.out.println(iter + " iterations");
        g.dispose();
        running = false;
    }

    public void save(String file)
    {
        try
        {
            String name = file.substring(0, file.lastIndexOf('.'));
            String extension = file.substring(file.lastIndexOf('.')+1);
            ImageIO.write(drawingImage, extension, new File(name + "-" + brush + "." + extension));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void freeze()
    {
        running = true;
        Graphics g = getBufferStrategy().getDrawGraphics();
        while (running)
        {
            g.drawImage(drawingImage, 0, 0, windowWidth, windowHeight, null);
        }
    }

    private void loadColors(int[] data)
    {
        Set<Integer> colorSet = new HashSet<>();
        for(int i : data)
        {
            colorSet.add(i);
        }
        brush.setColors(new ArrayList<>(colorSet));
    }

    private void initImage(String file) throws IOException
    {
        System.out.println(file);

        BufferedImage imgBytes = ImageIO.read(new File(file));
        imageWidth = imgBytes.getWidth();
        imageHeight = imgBytes.getHeight();
        windowWidth = imageWidth;
        windowHeight = imageHeight;

        if (imageWidth > 1000 && imageWidth > imageHeight)
        {
            windowWidth = 1000;
            windowHeight = (int) (((double) imageHeight / imageWidth) * 1000);
        } else if (imageHeight > 1000 && imageHeight > imageWidth)
        {
            windowHeight = 1000;
            windowWidth = (int) (((double) imageWidth / imageHeight) * 1000);
        }

        srcImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        int[] dataSrc = ((DataBufferInt) srcImage.getRaster().getDataBuffer()).getData();
        imgBytes.getRGB(0, 0, imageWidth, imageHeight, dataSrc, 0, imageWidth);

        drawingImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
    }
}
