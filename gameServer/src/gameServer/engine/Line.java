package gameServer.engine;

import java.awt.*;
import java.io.Serializable;

public class Line implements Serializable {
    public int startX;
    public int startY;
    public int endX;
    public int endY;
    public int width;
    public boolean horizontal;
    public boolean block;
    public Color color;

    public Line(int x1, int y1, int x2, int y2, int w, boolean hor){
        startX = x1; endX = x2;
        startY = y1; endY = y2;
        width = w; horizontal = hor;
        color = Color.gray; block = false;
    }
    public boolean isLine(int x, int y){
        int d = width/2;
        if (horizontal){
            if ((startY - d <= y) && (startY + d >= y) && (startX <= x) && (endX >= x)){
                return true;
            } else {
                return false;
            }
        } else {
            if ((startX - d <= x) && (startX + d >= x) && (startY <= y) && (endY >= y)){
                return true;
            } else {
                return false;
            }
        }
    }

}