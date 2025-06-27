import org.lwjgl.Sys;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;

import java.util.ArrayList;
import java.util.List;

public class Planet {
    public float  mass, Xvel, Yvel, Xcoord, Ycoord;

     public Circle circle=null;
    boolean[] intouch = new boolean[30];
    int planetNumber;
    String name;
    Image icon = null;
    int iconNumber;


     Planet(String name, float Xvel, float Yvel,float Xcoord,float Ycoord, float mass,int radius,int planetsQuantity,Image thisImage,int iconNumber){
        this.name = name;
        this.Xvel = Xvel;
        this.Yvel = Yvel;
        this.mass = mass;
        this.Xcoord = Xcoord;
        this.Ycoord = Ycoord;
        this.circle =new Circle(Xcoord,Ycoord,radius);
        this.planetNumber=planetsQuantity;
        this.iconNumber=iconNumber;
        icon = thisImage;
    };
    void calculateTrajectory(float[] acc, float deltaT){
        this.Xvel = this.Xvel + acc[0] * deltaT;
        this.Yvel =this.Yvel+ acc[1] * deltaT;
        this.Xcoord += Xvel * deltaT;
        this.Ycoord += Yvel * deltaT;
     //   System.out.println(Xvel+" "+Yvel);
        circle.setCenterX(Xcoord);
        circle.setCenterY(Ycoord);
}
}
