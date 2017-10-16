package brushes;

import java.util.Random;

public class Wavy extends Brush
{
    public Wavy(int length, boolean colorEnhance)
    {
        super(length, colorEnhance);

        if(sin == null || cos == null)
        {
            initTrigTable();
        }
    }

    @Override
    protected void drawShape()
    {
        Random r = new Random();

        int x0 = r.nextInt(imageWidth);
        int y0 = r.nextInt(imageHeight);

        int x1 = (x0 - imageWidth/2);
        int y1 = (imageHeight/2 - y0);

        double vectorX = y1*Math.cos((double) y1/180*Math.PI/imageWidth*1000);
        double vectorY = x1*Math.sin((double) x1/180*Math.PI/imageHeight*1000);

        int angle = (int)(Math.atan((vectorY)/vectorX)/Math.PI*180);
        if(vectorX < 0 && vectorY < 0)
            angle += 180;
        if(angle < 0 && vectorX < 0)
            angle+=180;
        angle += r.nextInt(30)-15;

        for (int i = 0; i < size; i++)
        {
            int dx = (int) (i * Math.cos((double) angle/180*Math.PI));
            int dy = (int) (i * Math.sin((double) angle/180*Math.PI));

            int y = y0 + dy;
            int x = x0 + dx;

            drawPixel(x, y);
        }
    }

    @Override
    protected int pixelsPerDraw()
    {
        return size;
    }

    @Override
    public String toString()
    {
        return "wavy-" + size;
    }
}
