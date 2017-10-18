package brushes;

import java.util.Random;

public class Line extends Brush
{
    private Random random;

    public Line(int length, boolean colorEnhance)
    {
        super(length, colorEnhance);

        if(sin == null || cos == null)
        {
            initTrigTable();
        }

        this.random = new Random();
    }

    @Override
    protected void drawShape()
    {
        int angle = random.nextInt(181);
        int x0 = random.nextInt(imageWidth);
        int y0 = random.nextInt(imageHeight);


        for (int i = 0; i < size; i++)
        {
            int dx = (int) (i * cos[angle]);
            int dy = -(int) (i * sin[angle]);

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
        return "line-" + size;
    }
}
