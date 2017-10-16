package brushes;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public abstract class Brush
{
    protected List<Integer> colors;
    protected double[] cos;
    protected double[] sin;
    protected int imageWidth;
    protected int imageHeight;
    protected int size;
    protected int[] src;
    protected int[] pixelLocations;
    protected boolean colorEnhance;

    private int[] drawing;
    private int[] srcPixels;
    private int[] backupPixels;
    private int matchedPixels;
    private Set<Integer> currentColorSet;
    private Random random;

    public Brush(int size, boolean colorEnhance)
    {
        this.size = size;
        this.colorEnhance = colorEnhance;
        this.random = new Random();
    }

    protected abstract void drawShape();

    protected abstract int pixelsPerDraw();

    public void draw()
    {
        matchedPixels = 0;
        pixelLocations = new int[pixelsPerDraw()];
        srcPixels = new int[pixelsPerDraw()];
        backupPixels = new int[pixelsPerDraw()];

        if(colorEnhance)
            currentColorSet = new HashSet<>(size);

        drawShape();

        int selectedColor;
        if(colorEnhance && currentColorSet.size() > 0)
        {
            selectedColor = new ArrayList<>(currentColorSet).get(random.nextInt(currentColorSet.size()));
        }
        else
        {
            selectedColor = colors.get(random.nextInt(colors.size()));
        }

        int colorDistance1 = colorDistance(selectedColor, srcPixels);
        int colorDistance2 = colorDistance(backupPixels, srcPixels);
        if (colorDistance1 < colorDistance2)
        {
            for (int i = 0; i < matchedPixels; i++)
            {
                drawing[pixelLocations[i]] = selectedColor;
            }
        }
    }

    protected void drawPixel(int x, int y)
    {
        if (!(y < 0 || y >= imageHeight || x < 0 || x >= imageWidth))
        {
            pixelLocations[matchedPixels] = y * imageWidth + x;
            backupPixels[matchedPixels] = drawing[pixelLocations[matchedPixels]];
            srcPixels[matchedPixels] = src[pixelLocations[matchedPixels]];
            if (colorEnhance)
                currentColorSet.add(src[pixelLocations[matchedPixels]]);

            matchedPixels++;
        }
    }

    private int colorDistance(int[] color, int[] original)
    {
        double distance = 0;
        for (int i = 0; i < matchedPixels; i++)
        {
            int r1 = (color[i] & 0xff0000) >> 16;
            int r0 = (original[i] & 0xff0000) >> 16;
            int b1 = (color[i] & 0x00ff00) >> 8;
            int b0 = (original[i] & 0x00ff00) >> 8;
            int g1 = (color[i] & 0x0000ff);
            int g0 = (original[i] & 0x0000ff);
            distance += Math.sqrt((r1 - r0) * (r1 - r0) + (g1 - g0) * (g1 - g0) + (b1 - b0) * (b1 - b0));
        }
        return (int) distance;
    }

    private int colorDistance(int color, int[] original)
    {
        double distance = 0;

        int r1 = (color & 0xff0000) >> 16;
        int b1 = (color & 0x00ff00) >> 8;
        int g1 = (color & 0x0000ff);

        for (int i = 0; i < matchedPixels; i++)
        {
            int r0 = (original[i] & 0xff0000) >> 16;
            int b0 = (original[i] & 0x00ff00) >> 8;
            int g0 = (original[i] & 0x0000ff);
            distance += Math.sqrt((r1 - r0) * (r1 - r0) + (g1 - g0) * (g1 - g0) + (b1 - b0) * (b1 - b0));
        }
        return (int) distance;
    }

    protected void initTrigTable()
    {
        sin = new double[181];
        cos = new double[181];
        for(int i = 0; i <= 180; i++)
        {
            sin[i] = Math.sin(i);
            cos[i] = Math.cos(i);
        }
    }

    public static Brush createBrush(String brushName, int size, boolean colorEnhance)
    {
        Brush brush = null;
        Map<String, Class<? extends Brush>> brushes = new HashMap<>();
        brushes.put("line", Line.class);
        brushes.put("circle", Circle.class);
        brushes.put("wavy", Wavy.class);

        brushName = brushName.toLowerCase();
        if(!brushes.containsKey(brushName))
            throw new IllegalArgumentException("brush: " + brushName + "does not exist");

        Class<? extends Brush> brushClass = brushes.get(brushName);

        Constructor<? extends Brush> cons = null;
        try
        {
            cons = brushClass.getConstructor(int.class, boolean.class);
        }
        catch (NoSuchMethodException e)
        {
            throw new IllegalStateException();
        }

        try
        {
            if (cons != null)
            {
                brush = cons.newInstance(size, colorEnhance);
            }
        }
        catch (IllegalAccessException | InvocationTargetException | InstantiationException e)
        {
            throw new IllegalStateException("failed to construct brush");
        }

        return brush;
    }

    public void setColors(List<Integer> colors)
    {
        this.colors = colors;
    }

    public void setDimensions(int imageWidth, int imageHeight)
    {
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
    }

    public void setSrc(int[] src)
    {
        this.src = src;
    }

    public void setDrawing(int[] drawing)
    {
        this.drawing = drawing;
    }
}
