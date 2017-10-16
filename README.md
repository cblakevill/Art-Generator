# Art-Generator
Generate a drawing based on an image

# Build
Build the jar executable by running
```
ant
```
# Usage
The program uses the following arguments
* input - input image
* brush type - type of brush (line, circle, or wavy)
* iterations - number of iterations until the drawing is complete
* size - size of brush (line length/circle radius) in pixels
* enhanced coloring - more accurate coloring at the cost of processing time

Example
```
java -jar ArtGenerator.jar image.jpg line 3000000 60 true
```
---
![](https://i.imgur.com/ifGmHf7.jpg)
```
img.jpg circle 1000000 8 true
```
---
![](https://i.imgur.com/sdaThn9.jpg)
```
img.jpg wavy 1500000 40 true
```
