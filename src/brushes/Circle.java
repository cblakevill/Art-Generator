package brushes;

import java.util.Random;

public class Circle extends Brush
{
    private int radius;
    private int radiusSquared;
    private Random random;

    public Circle(int radius, boolean colorEnhance)
    {
        super(radius, colorEnhance);
        this.radius = radius;
        this.radiusSquared = radius * radius;
        this.random = new Random();
    }

    @Override
    protected void drawShape()
    {
        int x0 = random.nextInt(imageWidth + 2 * radius) - radius;
        int y0 = random.nextInt(imageHeight + 2 * radius) - radius;

        for (int i = 0; i < 2 * radius; i++)
        {
            int y = y0 - radius + i;
            for (int j = 0; j < 2 * radius; j++)
            {
                int x = x0 - radius + j;
                if ((j - radius) * (j - radius) + (i - radius) * (i - radius) < radiusSquared)
                {
                    drawPixel(x, y);
                }
            }
        }
    }


    @Override
    protected int pixelsPerDraw()
    {
        return 4 * size * size;
    }

    @Override
    public String toString()
    {
        return "circle-" + radius;
    }
}
