import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

public class Button {
    public Shape btn = null;
    int xl,yt;
    int btnNumber;
    public String label;

    Button(float x, float y, int width, int height, int btnNumber){
        btn = new Rectangle(x,y,width,height);
        xl= (int) x;
        yt= (int) y;
        this.btnNumber=btnNumber;
        label=""+btnNumber;

    }
    boolean clickCheck(int x,int y) throws SlickException {
        if (btn.contains(x,y)){
            Sound click = new Sound("Music/Click.wav");
            click.play();
            return true;
        }
        return false;
    }

}
