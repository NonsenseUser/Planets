import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


public class Instruments {
    public Rectangle panel = new Rectangle(5, 100, 150, 650);
    Button[] buttons = new Button[8];



    Instruments(){
        int x= (int) panel.getX()+8;
        int y = (int) panel.getY()+5;
        for (int i=0;i<buttons.length;i++){
            buttons[i]=new Button(x,y+80*i,130,70,i);

        }
        buttons[0].label= "Create Planet";
        buttons[1].label="Grid";
        buttons[2].label="Planet List";
        buttons[3].label="Save";
        buttons[4].label="Load";
        buttons[5].label="Lighting";
        buttons[6].label="Show Names";
        buttons[7].label="Exit";
    }

    void save(SolarSystem sol) throws IOException {
        File file=new File("Saves/","save_0.txt");
        try (FileWriter fileWriter = new FileWriter(file)) {
            for (int i = 0; i < sol.planetsQuantity; i++) {
                fileWriter.write(String.valueOf(sol.Planets[i].Xcoord)+'\n');
                fileWriter.write(String.valueOf(sol.Planets[i].Ycoord)+'\n');
                fileWriter.write(String.valueOf(sol.Planets[i].Xvel)+'\n');
                fileWriter.write(String.valueOf(sol.Planets[i].Yvel)+'\n');
                fileWriter.write(String.valueOf(sol.Planets[i].mass)+'\n');
                fileWriter.write(sol.Planets[i].name+'\n');
                fileWriter.write(String.valueOf(sol.Planets[i].circle.radius)+'\n');
                fileWriter.write(String.valueOf(sol.Planets[i].planetNumber)+'\n');
                fileWriter.write(String.valueOf(sol.Planets[i].iconNumber)+'\n');
            }
        }
    }
    void load(SolarSystem sol, Image[] planets) throws FileNotFoundException {
        File file=new File("Saves/","save_0.txt");
        Scanner reader = new Scanner(file);

        sol.planetsQuantity=0;
        String[] data = new String[10];
        while (reader.hasNextLine()) {
            for (int i=0;i<9;i++) {
                    data[i] = reader.nextLine();
            }
            sol.Planets[sol.planetsQuantity]=new Planet(data[5],
                    Float.parseFloat(data[2]),
                    Float.parseFloat(data[3]),Float.parseFloat(data[0]),
                    Float.parseFloat(data[1]),
                    Float.parseFloat(data[4]),
                    (int) Float.parseFloat(data[6]),
                    (int) Float.parseFloat(data[7]),
                    planets[Integer.parseInt(data[8])],
                    (int) Float.parseFloat(data[8]));

            sol.planetsQuantity++;
            }
        reader.close();

    }
    void draw(Graphics graphics){
        graphics.setColor(Color.black);
        graphics.fill(this.panel);
        graphics.setColor(Color.white);
        graphics.draw(this.panel);
        for (int i = 0; i < buttons.length; i++) {
            graphics.setColor(Color.black);
            graphics.fill(this.buttons[i].btn);
            graphics.setColor(Color.white);
            graphics.draw(this.buttons[i].btn);
            graphics.drawString(this.buttons[i].label, this.buttons[i].xl + 5, this.buttons[i].yt + 30);
        }
    }


}
