import brushes.Brush;
import brushes.Line;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.NoSuchElementException;

public class Main
{
    public static void main(String[] args)
    {
        String input = args[0];
        String shape = args[1];
        String iterations = args[2];
        String size = args[3];
        String colorEnhance = args[4];

        if(args.length != 5)
        {
            System.out.println("Usage: <input> <brush type> <iterations> <size> <output dir> <enhance drawPixel>");
            System.out.println("input - image or directory of images to process");
            System.out.println("brush type - type of brush <line/circle/wavy>");
            System.out.println("iterations - number of iterations to perform (usually > 1 million)");
            System.out.println("size - size of brush (line length/circle radius) in pixels");
            System.out.println("enhance drawPixel - more accurate coloring at the cost of processing time <true/false>");

            System.exit(1);
        }

        JFrame frame = new JFrame(input + " - " + shape + " - " + iterations);;
        Brush brush;
        ImageCanvas canvas;
        try
        {
            brush = Brush.createBrush(shape, Integer.parseInt(size), Boolean.parseBoolean(colorEnhance));
        }
        catch (NoSuchElementException e)
        {
            System.out.println("Brush not found - defaulting to line brush");
            brush = new Line(Integer.parseInt(size), Boolean.parseBoolean(colorEnhance));
        }
        canvas = new ImageCanvas(brush, Integer.parseInt(iterations));

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);
        frame.setVisible(true);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowOpened(WindowEvent e)
            {
                canvas.requestFocus();
            }
        });
        frame.add(canvas, BorderLayout.CENTER);

        try
        {
            canvas.setup(input);
            frame.pack();
            long start = System.currentTimeMillis();
            canvas.draw();
            System.out.println(System.currentTimeMillis() - start + " ms");
            canvas.save(input);
            canvas.freeze();
        }
        catch(IOException e)
        {
            System.out.println("Error loading " + input);
        }
    }
}
