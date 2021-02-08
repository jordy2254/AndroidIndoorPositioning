package com.jordan.renderengine;

import com.jordan.renderengine.data.Point2d;
import com.jordan.renderengine.data.Point2i;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

public class Screen {

    private static Screen INSTANCE;

    private final boolean[] BRESENHAM_DILATION_MASK = new boolean[]{
            false, true, false,
             true, true,  true,
            false, true, false
    };

    private final boolean[] FILL_MASK = new boolean[]{
            false, true, false,
            true, true,  true,
            false, true, false
    };

    private int width, height;
    private int[] pixels;
    private int clearColor = 0xffeeff;

    private Screen() {}

    public void drawPixels(int[] pixels, Point2i position, Point2i dimensions){
        for (int ox = 0; ox < dimensions.x; ox++) {
            for (int oy = 0; oy < dimensions.y; oy++) {
                int dx = position.x + ox;
                int dy = position.y + oy;
                if(validateDrawBounds(dx, dy)){
                    pixels[dx + dy * width] = pixels[ox + oy * dimensions.x];
                }
            }
        }
    }
    public void setSize(int width, int height){
        this.width = width;
        this.height = height;
        this.pixels = new int[width * height];
        clear();
    }

    public int[] getPixels() {
        return pixels;
    }

    public int getPixel(int x, int y) {
        return pixels[x + y * width];
    }

    public void clear(){
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = clearColor;
        }
    }

    public void drawLine(int x0, int y0, int x1, int y1, int color){
        drawLine(x0, y0, x1, y1, 1, color);
    }

    public void drawLine(int x1, int y1, int x2, int y2, int lineThickness, int color){
       //https://www.javatpoint.com/computer-graphics-bresenhams-line-algorithm
        //http://rosettacode.org/wiki/Bitmap/Bresenham%27s_line_algorithm#Java
        //dilation: https://en.wikipedia.org/wiki/Dilation_(morphology)
        int d = 0;
        boolean[] dilationMatrix = null;
        if(lineThickness > 1){
           dilationMatrix = createDilationMatrix(BRESENHAM_DILATION_MASK, lineThickness);
        }
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);

        int dx2 = 2 * dx; // slope scaling factors to
        int dy2 = 2 * dy; // avoid floating point

        int ix = x1 < x2 ? 1 : -1; // increment direction
        int iy = y1 < y2 ? 1 : -1;

        int x = x1;
        int y = y1;

        if (dx >= dy) {
            while (true) {
                if(validateDrawBounds(x,y)){
                    pixels[x + y * width] = color;
                    if(lineThickness > 1){
                        int ns = (3 + ((lineThickness - 1) * 2));
                        dilate(dilationMatrix, ns, x,y, color);
                    }
                }
                if (x == x2)
                    break;
                x += ix;
                d += dy2;
                if (d > dx) {
                    y += iy;
                    d -= dx2;
                }
            }
        } else {
            while (true) {
                if(validateDrawBounds(x,y)){
                    pixels[x + y * width] = color;
                    if(lineThickness > 1){
                        int ns = (3 + ((lineThickness - 1) * 2));
                        dilate(dilationMatrix, ns, x,y, color);
                    }
                }
                if (y == y2)
                    break;
                y += iy;
                d += dx2;
                if (d > dy) {
                    x += ix;
                    d -= dy2;
                }
            }
        }
    }

    public void drawPolygon(List<Point2d> points, int lineSize, int lineColor, boolean close){
        drawPolygon(points, lineSize, lineColor, 0x0, close);
    }

    public void drawPolygon(List<Point2d> points, int lineSize, int lineColor, int fillColor, boolean close){
        //https://www.cs.rit.edu/~icss571/filling/index.html

        for (int i = 1; i < points.size(); i++) {
            Point2d prevPoint = points.get(i - 1);
            Point2d cPoint = points.get(i);
            drawLine((int)prevPoint.x, (int)prevPoint.y, (int)cPoint.x, (int)cPoint.y, lineSize, lineColor);
        }

        if(close){
            Point2d firstPoint = points.get(0);
            Point2d lastPoint = points.get(points.size() - 1);
            drawLine((int)firstPoint.x, (int)firstPoint.y, (int)lastPoint.x, (int)lastPoint.y, lineSize, lineColor);
        }
    }

    public void drawPolygonUpdated(List<Point2d> points, int lineSize, int lineColor, int fillColor, boolean close){
        //https://www.cs.rit.edu/~icss571/filling/how_to.html
        List<Double[]> allEdgeTable = new ArrayList<>();

        //table indexes:
        int minYIndex = 0;
        int maxYIndex = 1;
        int xValIndex = 2;
        int slopeIndex = 3;

        List<Integer> globalEdgeIndexes = new ArrayList<>();

        //initialise all edges
        for (int i = 1; i <= points.size(); i++) {

            Point2d prevPoint = null;
            Point2d cPoint = null;
            if(i == points.size()){
                prevPoint = points.get(0);
                cPoint = points.get(points.size() - 1);
            }else{
                prevPoint = points.get(i - 1);
                cPoint = points.get(i);
            }


            double x0 = maximise(prevPoint.x, cPoint.x);
            double y0 = maximise(prevPoint.y, cPoint.y);
            double x1 = minimise(prevPoint.x, cPoint.x);
            double y1 = minimise(prevPoint.y, cPoint.y);
            double x;

            if(prevPoint.y < cPoint.y){
                x = prevPoint.x;
            }else{
                x = cPoint.x;
            }
            double slope = (y0 - y1) / (x0 - x1);

            allEdgeTable.add(new Double[]{y1, y0, x, slope});
        }

        //initilise globalEdgeTableIndexes
        for (int i = 0; i < allEdgeTable.size(); i++) {
            if(allEdgeTable.size() > 15){
                int p = 0;
            }
            Double[] d = allEdgeTable.get(i);
            //skip zero slopes
            if(d[slopeIndex] == 0){
                continue;
            }

            //add the first
            if(globalEdgeIndexes.size() == 0){
                globalEdgeIndexes.add(i);
                continue;
            }

            //insert new value order is smallest X, smallestY
            int j = 0;
            for (j = 0; j <= globalEdgeIndexes.size(); j++) {
                if(j == globalEdgeIndexes.size()){
                    globalEdgeIndexes.add(i);
                    break;
                }
                Double[] d2 = allEdgeTable.get(globalEdgeIndexes.get(j));
                //First spot that our current Y is greater meaning lower values are to the left
                if(d[minYIndex] > d2[minYIndex]){
                    continue;
                }
                //if our y values are equal
                if(d[minYIndex] == d2[minYIndex]){
                    //if our x value is greater than the current then we need to go further.
                    if(d[xValIndex] > d2[xValIndex]){
                        continue;
                    }
                }
                globalEdgeIndexes.add(j, i);
                break;
            }
        }

        int scanlineStart = allEdgeTable.get(globalEdgeIndexes.get(0))[minYIndex].intValue();
        if(scanlineStart > height){
            return;
        }
        int yMaxIndex = 0;
        int xValIndexActive = 1;
        int mIndex = 2;

        //setup active edge table
        List<Double[]> activeEdgeTable = new ArrayList<>();
        for (int i = 0; i < globalEdgeIndexes.size(); i++) {
            Double[] data = allEdgeTable.get(globalEdgeIndexes.get(i));
            if(data[minYIndex].intValue() == scanlineStart){
                activeEdgeTable.add(new Double[]{data[maxYIndex], data[xValIndex], 1/data[slopeIndex]});
            }else{
                break;
            }
        }

        int oneCOunt = 0;
        while(!activeEdgeTable.isEmpty()){
            if(scanlineStart > height){
                break;
            }
            //draw the pixels between our lines
            if(activeEdgeTable.size() == 1){
                oneCOunt ++;
            }
            if(oneCOunt > 5){
                break;
            }
            if(!(scanlineStart < 0)){
                for (int i = 0; i < activeEdgeTable.size() && i + 1< activeEdgeTable.size(); i+=2) {
                    int sx = activeEdgeTable.get(i)[xValIndexActive].intValue();
                    int ex = activeEdgeTable.get(i + 1)[xValIndexActive].intValue();
                    if(sx >= ex ){
                        continue;
                    }
                    for (int j = 0; j < ex - sx; j++) {
                        int dx = sx + j;
                        if(dx > width){
                            break;
                        }
                        if(validateDrawBounds(dx, scanlineStart)){
//                        int color = j <= 0 + 10 || (j >= (ex - sx - 10)) ? lineColor : fillColor;
                            pixels[dx + scanlineStart * width] = fillColor;
                        }
                    }
                }
            }

            scanlineStart ++;

            //remove any redundent edges
            for (int i = activeEdgeTable.size() -1; i >= 0; i--) {
                if(activeEdgeTable.get(i)[yMaxIndex].intValue() == scanlineStart){
                    activeEdgeTable.remove(i);
                }
            }
            //add our slope to all values
            for(Double[] data : activeEdgeTable){
                data[xValIndexActive] += data[mIndex];
            }

            //add any new edges
            for (int i = 0; i < globalEdgeIndexes.size(); i++) {
                Double[] data = allEdgeTable.get(globalEdgeIndexes.get(i));
                if(data[minYIndex].intValue() == scanlineStart){
                    int index = 0;
                    for (Double[] d : activeEdgeTable) {
                        if(d[xValIndexActive] > data[xValIndex]){
                            break;
                        }
                        index++;
                    }
                    activeEdgeTable.add(index, new Double[]{data[maxYIndex], data[xValIndex], 1/data[slopeIndex]});
                }
            }
        }
    }

    private Double maximise(Double point, double value) {

        if(point == null || point < value){
            return value;
        }

        return point;
    }

    private Double minimise(Double point, double value) {

        if(point == null || point > value){
            return value;
        }

        return point;
    }

    private boolean[] createDilationMatrix(boolean[] currentMatrix, int iterations){
        //each iteration adds 1 on each side,
        int ns = (3 + ((iterations - 1) * 2));
        int centerPoint = (int)Math.ceil((double)ns / 2) - 1;
        boolean[] returnVal = new boolean[ns * ns];
        fillDilationMatrixRecursive(currentMatrix, returnVal, ns, centerPoint, centerPoint, iterations);
        return returnVal;
    }

    private void fillDilationMatrixRecursive(boolean[] dilationMask, boolean[] currentMatrix, int size, int cx, int cy, int iterations){
        //each iteration adds 1 on each side,
        if(iterations == 0){
            return;
        }
        for (int xp = -1; xp <= 1; xp++) {
            for (int yp = -1; yp <= 1; yp++) {
                int index = (xp + 1) + (yp + 1) * 3;
                int cx2 = cx + xp;
                int cy2 = cy + yp;
                currentMatrix[cx2 + cy2 * size] = dilationMask[index];
                fillDilationMatrixRecursive(dilationMask, currentMatrix, size, cx2, cy2, iterations -1);
            }
        }
    }

    private void dilate(boolean[]mask, int ms, int x, int y, int color){

        int sp = (int) Math.floor((double)ms /2);

        for (int xp = -sp; xp <= sp; xp++) {
            for (int yp = -sp; yp <= sp; yp++) {
                int index = (xp + sp) + (yp + sp) * ms;
                if(mask[index]){
                    int dx = (x + xp);
                    int dy = (y + yp);
                    if(validateDrawBounds(dx, dy))
                    pixels[dx + dy * width] = color;
                }
            }
        }
    }

    public void setClearColor(int clearColor) {
        this.clearColor = clearColor;
    }

    private void dilateRecursive(int x, int y, int lineThickness, int color) {
        //TODO create a dilation mask  in line function for line thickness, then create another function to apply
        //the dilation mask
        if(lineThickness == 0){
            return;
        }
        for (int xp = -1; xp <= 1; xp++) {
            for (int yp = -1; yp <= 1; yp++) {
                int index = (xp + 1) + (yp + 1) * 3;

                if (BRESENHAM_DILATION_MASK[index]) {
                    int dx = x + xp;
                    int dy = y + yp;
                    if (validateDrawBounds(dx, dy)) {
                        pixels[dx + dy * width] = color;
                    }
                    dilateRecursive(dx,dy, lineThickness -1, color);
                }
            }
        }
    }

    public void renderRect(int xPos, int yPos, int width, int height, int color){
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int dx = x + xPos;
                int dy = y + yPos;
                if(!validateDrawBounds(dx, dy)){
                    continue;
                }
                pixels[dx + dy * this.width] = color;
            }
        }
    }

    public boolean validateDrawBounds(int dx, int dy){
        return dx >= 0 && dx < this.width && dy >= 0 && dy < height;
    }

    public static synchronized Screen getInstance(){
        if (INSTANCE == null) {
            INSTANCE = new Screen();
        }
        return INSTANCE;
    }
}
