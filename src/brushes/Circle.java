package brushes;

import java.util.Random;

public class Circle extends Brush
{
    private int radius;

    public Circle(int radius, boolean colorEnhance)
    {
        super(radius, colorEnhance);
        this.radius = radius;
    }

    @Override
    protected void drawShape()
    {
        Random r = new Random();

        int x0 = r.nextInt(imageWidth + 2 * radius) - radius;
        int y0 = r.nextInt(imageHeight + 2 * radius) - radius;

        for (int i = 0; i < 2 * radius; i++)
        {
            int y = y0 - radius + i;
            for (int j = 0; j < 2 * radius; j++)
            {
                int x = x0 - radius + j;
                if (!(y < 0 || y >= imageHeight || x < 0 || x >= imageWidth))
                {
                    if ((j - radius) * (j - radius) + (i - radius) * (i - radius) < radius * radius)
                    {
                        drawPixel(x, y);
                    }
                }

            }
        }
    }


    @Override
    protected int pixelsPerDraw()
    {
        return 4 * radius * radius;
    }

    @Override
    public String toString()
    {
        return "circle-" + radius;
    }
}
