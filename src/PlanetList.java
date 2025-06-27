import org.lwjgl.Sys;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

import java.util.*;

public class PlanetList {
    Rectangle list = new Rectangle(1400,50,180,800);
    List<Button> planetList= new ArrayList(5);
    List<Button> scrollButton = new ArrayList<>(2);
    boolean enough=false;
    int startNumber=0;



    PlanetList() throws SlickException {
        scrollButton.add(new Button(1405, 855, 170, 35, 0));
        scrollButton.add(new Button(1405, 5, 170, 35, 1));
        scrollButton.get(1).label = "Up";
        scrollButton.get(0).label = "Down";
        for (int i = 0; i < 5; i++) {
            planetList.add(new Button(1410, 55 + 160 * (i), 150, 150, i));
        }
    }
    void update(int x,int y,int planetsQuantity) throws SlickException {
        Sound click = new Sound("Music/Click.wav");
        if ((scrollButton.get(0).clickCheck(x, y)) & enough) {
            startNumber += 1;
            click.play();
        }

        if (scrollButton.get(1).clickCheck(x, y) & startNumber > 0) {
            startNumber -= 1;
            click.play();
        }
        if ((!enough) & (planetsQuantity>5+startNumber)) {
            enough = true;
        }
        if ((enough) & (planetsQuantity<=5+startNumber)){
            enough = false;
        }
    }
    public void draw(Graphics graphics,SolarSystem sol) {
            graphics.setColor(Color.black);
            graphics.fill(this.list);
            graphics.setColor(Color.white);
            graphics.draw(this.list);
            for (int i=0;i<5;i++){
                 if (sol.planetsQuantity>i+startNumber) {
                    graphics.setColor(Color.black);
                    graphics.fill(this.planetList.get(i).btn);
                    graphics.setColor(Color.white);
                    graphics.draw(this.planetList.get(i).btn);
                    sol.Planets[i+startNumber].icon.draw(1430, 55 + 160 * (i)+5, 120, 120);
                    graphics.drawString(sol.Planets[i+startNumber].name,1450,55 + 160 * (i)+130);
                 }
            }
            if (enough) {
                graphics.setColor(Color.black);
                graphics.fill(scrollButton.get(0).btn);
                graphics.setColor(Color.white);
                graphics.draw(scrollButton.get(0).btn);
                graphics.drawString(scrollButton.get(0).label,1470,860);
                }
            if (startNumber>0){
                graphics.setColor(Color.black);
                graphics.fill(scrollButton.get(1).btn);
                graphics.setColor(Color.white);
                graphics.draw(scrollButton.get(1).btn);
                graphics.drawString(scrollButton.get(1).label,1470,10);
            }
            }

}

