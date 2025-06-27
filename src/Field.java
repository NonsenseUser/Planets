import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Field {
    int xl,yt;
    int fldNumber;
    String label;
    Shape field =null;
    boolean isActive;
    Shape activeIndicator;
    String data="";
    List<Character> charData = new ArrayList<Character>(20);

    Field(float x, float y, int width, int height, int fldNumber){
        field = new Rectangle(x,y,width,height);
        xl= (int) x;
        yt= (int) y;
        this.fldNumber=fldNumber;
        activeIndicator= new Rectangle(x+3,y+3,2,height-6);
    }
    boolean clickCheck(int x, int y){
        if (field.contains(x,y)) {
            isActive = true;
        }
        return isActive;
    }
    void convertInputToData(){
        StringBuilder builder = new StringBuilder(charData.size());
        for(Character ch: charData)
        {
            builder.append(ch);
        }
        data = builder.toString();
    }
    void draw(Graphics graphics){
        this.activeIndicator.setCenterX(this.xl + 10 * this.charData.size() + 3);
        graphics.fill(this.activeIndicator);
    }
}
