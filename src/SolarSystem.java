import org.newdawn.slick.*;
import org.newdawn.slick.geom.Line;

import static java.lang.Math.abs;

public class SolarSystem {

    Planet[] Planets = new Planet[30];

    int planetsQuantity = 0;


    void createPlanetFromInput(String name, String mass, String radius, float formX, float formY, float scale, Image thisImage,int iconNumber) {
        int thisMass = Integer.parseInt(mass);
        int thisRadius = (int) (Integer.parseInt(radius)*scale);
        Planets[planetsQuantity] = new Planet(name, 0, 0, formX, formY, thisMass, thisRadius, planetsQuantity,thisImage,iconNumber);
        planetsQuantity++;
    }

    void calculateTrajectories(float scale) throws SlickException {
        collisionAlarm();
        GravityField.calculateField(Planets, planetsQuantity, scale);
    }

    void collisionAlarm() throws SlickException {
        Sound collision = new Sound("Music/Collision.wav");
        for (int i = 0; i < planetsQuantity; i++) {
            for (int k = 0; k < planetsQuantity; k++) {
                if (Planets[i].circle.intersects(Planets[k].circle) & i != k) {
                    if (!Planets[i].intouch[k]){
                        Planets[i].intouch[k] = true;
                        Planets[k].intouch[i] = true;
                        collision.play();
                    }
                        float e = 0.1F;
                        float curVelIX = Planets[i].Xvel;
                        float curVelKX = Planets[k].Xvel;
                        float curVelIY = Planets[i].Yvel;
                        float curVelKY = Planets[k].Yvel;
                        float genMass = Planets[i].mass + Planets[k].mass;
                        float Imass = Planets[i].mass;
                        float Kmass = Planets[k].mass;
                        Planets[i].Xvel = ((Imass - e * Kmass) * curVelIX + (1 + e) * Kmass * curVelKX) / genMass;
                        Planets[k].Xvel = ((Kmass - e * Imass) * curVelKX + (1 + e) * Imass * curVelIX) / (genMass);
                        Planets[i].Xvel = ((Imass - e * Kmass) * curVelIY + (1 + e) * Kmass * curVelKY) / (genMass);
                        Planets[k].Xvel = ((Kmass - e * Imass) * curVelKY + (1 + e) * Imass * curVelIY) / (genMass);
                }
                if (!Planets[i].circle.intersects(Planets[k].circle) & (Planets[i].intouch[k]) &i!=k){
                    Planets[i].intouch[k] = false;
                    Planets[k].intouch[i] = false;
                }

            }
        }
    }
    void draw(Graphics graphics,boolean isLightingOn,boolean isNamesOn) {
        for (int i = 0; i < this.planetsQuantity; i++) {

                this.Planets[i].icon.draw(this.Planets[i].circle.getX(), this.Planets[i].circle.getY(),
          2*Planets[i].circle.getBoundingCircleRadius(),2*this.Planets[i].circle.getBoundingCircleRadius());
            float r = this.Planets[i].circle.radius;
            float x = this.Planets[i].Xcoord;
            float y = this.Planets[i].Ycoord;
            if (isLightingOn){
                this.Planets[i].circle.setRadius(r+10);
                this.Planets[i].circle.setCenterX(this.Planets[i].Xcoord);
                this.Planets[i].circle.setCenterY(this.Planets[i].Ycoord);
                graphics.draw(this.Planets[i].circle);
                this.Planets[i].circle.setRadius(r);
                this.Planets[i].circle.setCenterX(x);
                this.Planets[i].circle.setCenterY(y);
            }
            if (isNamesOn){
                if(x+r>1600) {
                    x-=2*r;
                }else{x+=r;}
                if(y+r>900) {
                    y-=2*r;
                }else{y+=r;}
                graphics.drawString(Planets[i].name,x,y);
            }

        }
    }

    void update(int x, int y, PlanetList planetList,int startNumber) throws SlickException {
        for (int i = 0; i <5; i++) {
            if (planetList.planetList.get(i).clickCheck(x, y)) {
                float dX = this.Planets[i+startNumber].Xcoord - 800;
                float dY = this.Planets[i+startNumber].Ycoord - 450;
                for (int k = 0; k < this.planetsQuantity; k++) {
                        this.Planets[k].circle.setCenterX(this.Planets[k].Xcoord - dX);
                        this.Planets[k].circle.setCenterY(this.Planets[k].Ycoord - dY);
                        this.Planets[k].Xcoord -= dX;
                        this.Planets[k].Ycoord -= dY;
                    }
                }
            }

        }

    void giveZeroSpeed(){
        this.Planets[planetsQuantity-1].Xvel=0;
        this.Planets[planetsQuantity-1].Yvel=0;
    }
    void shift(int dX,int dY){
        for (int i = 0; i < this.planetsQuantity; i++) {
                float newCenterX = this.Planets[i].circle.getCenterX() +dX;
                float newCenterY = this.Planets[i].circle.getCenterY() + dY;
                this.Planets[i].circle.setCenterX(newCenterX);
                this.Planets[i].circle.setCenterY(newCenterY);
                this.Planets[i].Xcoord=newCenterX;
                this.Planets[i].Ycoord=newCenterY;

            }
        }

    void scale(float dScale){
        for (int i = 0; i < this.planetsQuantity; i++) {
                float newCenterX = 800-(800-this.Planets[i].circle.getCenterX())*dScale;
                float newCenterY = 450-(450-this.Planets[i].circle.getCenterY())*dScale;
                this.Planets[i].circle.setCenterX(newCenterX);
                this.Planets[i].circle.setCenterY(newCenterY);
                this.Planets[i].Xcoord=newCenterX;
                this.Planets[i].Ycoord=newCenterY;
                this.Planets[i].circle.setRadius(this.Planets[i].circle.getRadius()*dScale);
                this.Planets[i].Xvel*=dScale;
                this.Planets[i].Yvel*=dScale;

        }
    }
}



