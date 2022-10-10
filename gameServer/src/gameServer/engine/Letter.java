package gameServer.engine;

import java.awt.*;
import java.io.Serializable;

public class Letter implements Serializable {
    public int x;
    public int y;
    public Color color;
    public String letter;
    public static Font FONT = new Font("Sans", Font.BOLD, 40);
    public Letter(int x, int y){
        this.x = x; this.y = y;
        this.color = Color.black; this.letter = "";
    }
}
