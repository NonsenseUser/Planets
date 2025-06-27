import java.util.Arrays;

import static java.lang.Math.sqrt;

public class GravityField {
    static float deltaT=1;
    static void calculateField(Planet[] planets,int planetsQuantity,float scale){

        float Xacc,Yacc;
        for (int g=0;g<planetsQuantity;g++){
            Xacc=0;
            Yacc=0;
        for (int i = 0; i < planetsQuantity; i++) {
            if (g!=i) {
                if (!planets[g].intouch[i]) {
                    float xR = planets[g].Xcoord - planets[i].Xcoord;
                    float yR = planets[g].Ycoord - planets[i].Ycoord;
                    float radius = (float) sqrt((xR) * (xR) + (yR) * (yR));
                    radius/=scale;
                    xR/=scale;
                    yR/=scale;
                    Xacc -= (planets[i].mass *xR) / (radius * radius * radius);
                    Yacc -= (planets[i].mass * yR) / (radius * radius * radius);
                    }

            }
            }
            float[] acc = {Xacc, Yacc};
            planets[g].calculateTrajectory(acc, deltaT);

        }
    }
}
