import org.lwjgl.Sys;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

public class Form {
    Field[] fields=new Field[3];
    Planet planet=null;
    Shape rectangle = null;
    Button button= null;
    Button getPlanetSelector=null;
    Button[] imageButton = new Button[10];
    Button cancel=null;
    float x,y;
    void summon(float x, float y) throws SlickException {
        if ((x+200)>1600){
            x-=210;
        }
        if ((y+410)>900){
            y-=420;
        }
        this.x= x;
        this.y=y;
        rectangle= new Rectangle(x, y, 200, 410);

        for (int i =0;i<3;i++){
        fields[i]=new Field(x+10, y+40+55*i, 180, 30,i);
        }
        getPlanetSelector = new Button(x+40,y+40+55*3+5,100,100,0);

        button = new Button(x+30,y+40+55*3+110,140,30,0);
        button.label="Create";


        cancel = new Button(x+30,y+40+55*4+110,140,30,0);
        //rectangle[4]=new Rectangle(x+40,y+320,100,50);
        fields[0].label= "Planet Name:";
        fields[1].label= "Planet Mass:";
        fields[2].label= "Planet Radius:";
        cancel.label="Cancel";

    }
    void summon(Planet planet){
        this.planet=planet;
        int x,y;
        if ((planet.Xcoord+planet.circle.radius+200)>1600){
                x = (int) (planet.Xcoord-0.75F*planet.circle.radius-210);
            } else {
                x= (int) (planet.Xcoord+0.75F*planet.circle.radius);
        }
        if ((planet.Ycoord+planet.circle.radius+410)>900){
            y= (int) (planet.Ycoord-0.75F*planet.circle.radius-420);
        } else {
            y= (int) (planet.Ycoord+0.75F*planet.circle.radius);
        }


        rectangle= new Rectangle(x, y, 200, 410);
        for (int i =0;i<3;i++){
            fields[i]=new Field(x+10, y+40+55*i, 180, 30,i);
        }
        getPlanetSelector = new Button(x+40,y+40+55*3+5,100,100,0);
        button = new Button(x+30,y+40+55*3+110,140,30,0);
        button.label="Edit";


        cancel = new Button(x+30,y+40+55*4+110,140,30,0);
        //rectangle[4]=new Rectangle(x+40,y+320,100,50);
        fields[0].label= "Planet Name:";
        fields[1].label= "Planet Mass:";
        fields[2].label= "Planet Radius:";
        fields[0].data = planet.name;
        fields[1].data = Float.toString(planet.mass);
        fields[2].data = Float.toString(planet.circle.radius);
       // System.out.println(fields[1].data);
        cancel.label="Close";
    }
    public void startDraw(Graphics graphics){
        graphics.setColor(Color.black);
        graphics.fill(this.rectangle);
        graphics.setColor(Color.white);
        graphics.draw(this.rectangle);
        for (int i = 0; i < 3; i++) {
            graphics.setColor(Color.black);
            graphics.fill(this.fields[i].field);
            graphics.setColor(Color.white);
            graphics.draw(this.fields[i].field);
        }
        for (int i = 0; i < 3; i++) {
            graphics.drawString(this.fields[i].label, this.rectangle.getX() + 5, this.rectangle.getY() + 17 + 55 * i);
        }
        graphics.setColor(Color.black);
        graphics.fill(getPlanetSelector.btn);
        graphics.fill(this.button.btn);
        graphics.fill(this.cancel.btn);
        graphics.setColor(Color.white);
        graphics.draw(getPlanetSelector.btn);
        graphics.draw(this.button.btn);
        graphics.draw(this.cancel.btn);
        graphics.drawString(this.button.label, this.rectangle.getX() + 72, this.rectangle.getY() + 50 + 55 * 3+110);
        graphics.drawString(this.cancel.label, this.rectangle.getX() + 72, this.rectangle.getY() + 50 + 55 * 4+110);
    }
    public void finishDrawForm(Graphics graphics,Image planet){
        planet.draw(this.rectangle.getX()+45,this.rectangle.getY()+40+55*3+10,90,90);
        for (Field field : this.fields) {
            for (int k = 0; k < field.charData.size(); k++) {
                graphics.drawString(String.valueOf(field.charData.get(k)), field.xl + 10 * k, field.yt + 5);
            }
        }
    }

    public void finishDrawInfo(Graphics graphics){
        this.planet.icon.draw(this.rectangle.getX()+40,this.rectangle.getY()+40+55*3+5,100,100);
        for (Field field : this.fields) {
            graphics.drawString(String.valueOf(field.data), field.xl + 10, field.yt + 5);
        }
    }
    public void summonPlanetSelector(){
        if (getPlanetSelector.xl+105*7+100<1600) {
            for (int i = 0; i < 10; i++) {
                imageButton[i] = new Button(getPlanetSelector.xl + 105 * (i + 1), getPlanetSelector.yt, 100, 100, i);
            }
        }else{for (int i = 0; i < 10; i++) {
            imageButton[i] = new Button(getPlanetSelector.xl - 105 * (i + 1), getPlanetSelector.yt, 100, 100, i);
        }}
    }
    public void drawPlanetSelector(Image[] planets,Graphics graphics){
        for (int i=0;i<4;i++){
            graphics.setColor(Color.black);
            graphics.fill(imageButton[i].btn);
            graphics.setColor(Color.white);
            graphics.draw(imageButton[i].btn);
        }
        if (getPlanetSelector.xl+105*7+100<1600) {
            for (int i = 0; i < 4; i++) {
                planets[i].draw(getPlanetSelector.xl + 105 * (i + 1) + 5, getPlanetSelector.yt + 5, 90, 90);
            }
        }else{
            for (int i = 0; i < 4; i++) {
                planets[i].draw(getPlanetSelector.xl - 105 * (i + 1) + 5, getPlanetSelector.yt + 5, 90, 90);
            }
        }
    }
    public void drag(int dX,int dY){
        float c =rectangle.getX()+dX;
        rectangle.setX(c);
        x+=dX;
        c =rectangle.getY()+dY;
        rectangle.setY(c);
        y+=dY;
        for (Field field : fields) {
            c = field.field.getCenterX()+dX;
            field.field.setCenterX(c);
            field.xl +=dX;
            c = field.field.getCenterY()+dY;
            field.field.setCenterY(c);
            field.yt +=dY;
        }
        c = button.btn.getCenterX()+dX;
        button.btn.setCenterX(c);
        button.xl+=dX;
        c = button.btn.getCenterY()+dY;
        button.btn.setCenterY(c);
        button.yt+=dY;

        c = cancel.btn.getCenterX()+dX;
        cancel.btn.setCenterX(c);
        cancel.xl+=dX;
        c = cancel.btn.getCenterY()+dY;
        cancel.btn.setCenterY(c);
        cancel.yt+=dY;

        c = getPlanetSelector.btn.getCenterX()+dX;
        getPlanetSelector.btn.setCenterX(c);
        getPlanetSelector.xl+=dX;
        c = getPlanetSelector.btn.getCenterY()+dY;
        getPlanetSelector.btn.setCenterY(c);
        getPlanetSelector.yt+=dY;
    }
    }

