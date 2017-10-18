package brushes;

import java.util.Random;

public class Wavy extends Brush
{
    private final double rads = Math.PI / 180;
    private int yDistortion;
    private int xDistortion;
    private Random random;

    public Wavy(int length, boolean colorEnhance)
    {
        super(length, colorEnhance);

        if(sin == null || cos == null)
        {
            initTrigTable();
        }

        this.random = new Random();

        xDistortion = random.nextInt(21) - 10;
        yDistortion = random.nextInt(21) - 10;

        while (xDistortion == 0)
            xDistortion = random.nextInt(21) - 10;
        while (yDistortion == 0)
            yDistortion = random.nextInt(21) - 10;
    }

    @Override
    protected void drawShape()
    {
        int x0 = random.nextInt(imageWidth);
        int y0 = random.nextInt(imageHeight);

        int x1 = (x0 - imageWidth/2);
        int y1 = (imageHeight/2 - y0);

        double vectorX = xDistortion*y1*Math.cos(1000*y1/imageWidth * rads);
        double vectorY = yDistortion*x1*Math.sin(1000*x1/imageHeight * rads);

        int angle = (int)(Math.atan((vectorY)/vectorX)/rads);
        if(vectorX < 0 && vectorY < 0)
            angle += 180;
        if(angle < 0 && vectorX < 0)
            angle += 180;
        angle += random.nextInt(30)-15;

        for (int i = 0; i < size; i++)
        {
            int dx = (int) (i * cos(angle));
            int dy = (int) (i * sin(angle));

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
        return "wavy-" + xDistortion + "-" + yDistortion + "-" + size;
    }

    private double sin(int a)
    {
        if(a < 0)
            return -sin[-a];

        return sin[a];
    }

    private double cos(int a)
    {
        if(a < 0)
            a = -a;

        return cos[a];
    }
}
