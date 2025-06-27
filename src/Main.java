

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.*;

public class Main extends BasicGame {
    SolarSystem sol = new SolarSystem();
    boolean formSum,formSummed,fieldActivated,vectorActivated,isPaused=true,isGridOn=false,isListOn=false;
    boolean isEditOn=false,isPlanetSelectorOn,willPlanetSelectorBeOn, isLightingOn,isMenuHidden=false,isMusicPlaying=false,isAboutOn=false;
    boolean isListCurrentlyOn=false,isNamesOn=true;
    boolean isInfoOn=false;
    float formX;
    float formY;
    int vectPlanet;
    int whichPlanetGrabbed;
    int mod=-1;
    float vectOriginX ;
    float vectOriginY;
    Field activeField;
    Shape cursor = new Circle(0,0,10);
    Instruments instruments = new Instruments();
    Line velVector=null,vel1Vector,vel1UnitVector;
    Button pause;
    Button clear;
    Button fastForward;
    Button slowDown;
    Button returnToX1;
    Button hideMenu;
    float scale=1;
    int oldX=0,oldY=0,gridStartX,gridStartY;
    PlanetList planetList = new PlanetList();
    Form info=new Form();
    Form Form=new Form();
    Image[] planets = new Image[10];
    Image background = null;
    Music spaceSounds = null;
    Sound disapearingSound,editSound,diminishingSound,error,creation,push,grab,bandjo,drums,fluete,garmonica,instrument,doctorWho,click,type;
    int iconNumber;
    public Main(String title) throws SlickException {
        super(title);
    }
    public void init(GameContainer container) throws SlickException {
        System.load("C://Users//ччч//IdeaProjects//Kursovaya.0//lib//OpenAL64.dll");
        whichPlanetGrabbed=-1;
        for (int i=0;i<4;i++){
            planets[i] = new Image("Images/img_"+i+".png");
        }
        planets[4]=new Image("Images/Tardis.png");
        pause = new Button(1000,100,100,20,10);
        clear = new Button(1150,100,100,20,11);
        fastForward = new Button(1100,150,40,30,11);
        slowDown = new Button(1000,150,40,30,11);
        returnToX1 = new Button(1050,150,40,30,11);
        hideMenu = new Button(5,30,100,30,11);
        pause.label="Play";
        clear.label="Clear";
        returnToX1.label = "X1";
        fastForward.label=">>";
        slowDown.label="<<";
        hideMenu.label="Hide GUI";
        background = new Image("Images/background.png");
        spaceSounds= new Music("Music/SpaceSounds.wav");
        spaceSounds.loop();
        disapearingSound = new Sound("Music/Disapearing.wav");
        diminishingSound = new Sound("Music/Diminishing.wav");
        editSound = new Sound("Music/Increase.wav");
        error=new Sound("Music/Error.wav");
        creation=new Sound("Music/Creation.wav");
        push = new Sound("Music/Push.wav");
        grab = new Sound("Music/Grab.wav");
        bandjo = new Sound("Music/Bandjo.wav");
        drums = new Sound("Music/Drums.wav");
        fluete = new Sound("Music/Fluete.wav");
        garmonica = new Sound("Music/Garmonica.wav");
        doctorWho = new Sound("Music/DoctorWho.wav");
        click = new Sound("Music/Click.wav");
        type = new Sound("Music/Type.wav");

    }
    public void keyPressed(int key,char code) {

        if (fieldActivated) {

            if ((key == 14) & (!activeField.charData.isEmpty())) {
                type.play();
                activeField.charData.remove(activeField.charData.size() - 1);
            } else {
                if (activeField.charData.size() < 10) {
                    if (activeField.fldNumber != 0) {
                        Pattern numberTemplate = Pattern.compile("\\d");
                        Matcher matcher = numberTemplate.matcher(code + "");
                        if (matcher.find()) {
                            type.play();
                            activeField.charData.add(code);
                        }
                    } else {
                        Pattern numberTemplate = Pattern.compile("\\w");
                        Matcher matcher = numberTemplate.matcher(code + "");
                        if (matcher.find()) {
                            type.play();
                            activeField.charData.add(code);
                        }
                    }
                }
            }
        }
    }
    public void mouseWheelMoved(int change){
        if (change<0) {
            scale/=2;
            sol.scale(0.5F);

        }else{
            scale*=2;
            sol.scale(2F);}
    }
    public void mouseClicked(int button, int x, int y, int clickCount){
        if (button==1) {
            int MouseY = 450 - (Mouse.getY() - 450);
            boolean isDeleted = false;
            for (int i = 0; i < sol.planetsQuantity; i++) {
                if (isDeleted) {
                    sol.Planets[i - 1] = sol.Planets[i];

                }
                if (sol.Planets[i].circle.contains(Mouse.getX(), MouseY) & clickCount == 2) {
                    disapearingSound.play();
                    sol.Planets[i] = null;
                    isDeleted = true;
                }
                if (isDeleted & i == sol.planetsQuantity - 1) {
                    sol.Planets[i] = null;
                    sol.planetsQuantity -= 1;
                    try {
                        planetList.update(0,0, sol.planetsQuantity);
                    } catch (SlickException e) {
                        throw new RuntimeException(e);
                    }
                }


            }
        }
    }
    public void update(GameContainer container, int delta) throws SlickException {

        if (!isPaused){
        sol.calculateTrajectories(scale);
        }
        Input input = container.getInput();

        int MouseY=450-(Mouse.getY()-450);
        if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
            if (vectorActivated){
                sol.Planets[vectPlanet].Xvel=velVector.getDX()/2000;
                sol.Planets[vectPlanet].Yvel=velVector.getDY()/2000;
                vectorActivated=false;
                push.play();
            }
            if(isListOn){
                planetList.update(Mouse.getX(),MouseY,sol.planetsQuantity);
            }

            if (instruments.buttons[1].clickCheck(Mouse.getX(),MouseY)){
                isGridOn=!isGridOn;
            }
            if (instruments.buttons[2].clickCheck(Mouse.getX(),MouseY)){
                isListOn=!isListOn;

            }
            if (instruments.buttons[3].clickCheck(Mouse.getX(),MouseY)){
                try {
                    instruments.save(sol);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (instruments.buttons[4].clickCheck(Mouse.getX(),MouseY)){
                try {
                    instruments.load(sol,planets);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            if (instruments.buttons[5].clickCheck(Mouse.getX(),MouseY)){
                isLightingOn =!isLightingOn;
            }
            if (instruments.buttons[6].clickCheck(Mouse.getX(),MouseY)){
                isNamesOn =!isNamesOn;
            }
            if (instruments.buttons[7].clickCheck(Mouse.getX(),MouseY)){
                System.exit(1);
            }
            if (fastForward.clickCheck(Mouse.getX(),MouseY)){
                GravityField.deltaT+=0.2F;
            }
            if (slowDown.clickCheck(Mouse.getX(),MouseY)){
                GravityField.deltaT-=0.2F;
            }
            if (returnToX1.clickCheck(Mouse.getX(),MouseY)){
                GravityField.deltaT=1;
            }
            if (clear.clickCheck(Mouse.getX(),MouseY)){
                for (int i = sol.planetsQuantity; i >=0 ; i--) {
                    sol.Planets[i]=null;
                }
                sol.planetsQuantity=0;
            }
            if (pause.clickCheck(Mouse.getX(),MouseY)){
                isPaused=!isPaused;
                if (isPaused){
                pause.label="Play";
                }else {
                    pause.label="Pause";
                }
            }
            if (hideMenu.clickCheck(Mouse.getX(),MouseY)){
                isMenuHidden=!isMenuHidden;
                if (isMenuHidden){
                    hideMenu.label="Show GUI";
                    isListCurrentlyOn=isListOn;
                    isListOn=false;
                } else {
                    isListOn=isListCurrentlyOn;
                    hideMenu.label="Hide GUI";

                }
            }
            for (int i =0;i<sol.planetsQuantity;i++){
                if (sol.Planets[i].circle.contains(Mouse.getX(),MouseY)){
                    info.summon(sol.Planets[i]);
                    iconNumber=sol.Planets[i].iconNumber;
                    isInfoOn=true;
                }
            }
            if (mod==0){
                formX = Mouse.getX();
                formY = MouseY;
                mod=-2;
                Form.summon(formX,formY);
            }

            if (mod==-2){
                if (Form.cancel.clickCheck(Mouse.getX(),MouseY)){
                    isMenuHidden=false;
                    isPlanetSelectorOn=false;
                    mod=-1;
                    isListOn=isListCurrentlyOn;
                }
                for(int i =0;i<Form.fields.length;i++) {
                    if (Form.fields[i].clickCheck(Mouse.getX(),MouseY)){
                        for(int k =0;k<Form.fields.length;k++) {
                            Form.fields[k].isActive=false;
                            fieldActivated=false;
                        }
                    Form.fields[i].isActive=true;
                    }
                }
                if (Form.button.clickCheck(Mouse.getX(),MouseY)){
                    for(int i =0;i<Form.fields.length;i++) {
                        Form.fields[i].convertInputToData();
                    }
                    boolean isDataConvertionOk=true;
                         for(int i =1;i<3;i++) {
                             if (Objects.equals(Form.fields[i].data, "")) {
                              isDataConvertionOk = false;
                              error.play();
                              break;
                            };
                        }
                if (isDataConvertionOk){
                    creation.play();
                    Image icon = planets[iconNumber];
                    sol.createPlanetFromInput(Form.fields[0].data,Form.fields[1].data,Form.fields[2].data,formX,formY,scale,icon,iconNumber);
                    mod=-1;
                    isMenuHidden=false;
                    isListOn=isListCurrentlyOn;
                    isPlanetSelectorOn=false;
                    vectorActivated=true;
                    vectPlanet=sol.planetsQuantity-1;
                    vectOriginX = sol.Planets[vectPlanet].Xcoord;
                    vectOriginY=sol.Planets[vectPlanet].Ycoord;
                    velVector = new Line(vectOriginX,vectOriginY,Mouse.getX(),MouseY);
                    planetList.update(0,0, sol.planetsQuantity);
                }
                }

                if (Form.getPlanetSelector.clickCheck(Mouse.getX(),MouseY) & isPlanetSelectorOn){
                    willPlanetSelectorBeOn=false;
                }
                if (isPlanetSelectorOn){
                    for (int i =0;i<5;i++) {
                        if (Form.imageButton[i].clickCheck(Mouse.getX(), MouseY)) {
                            willPlanetSelectorBeOn = false;
                            iconNumber=i;
                        }
                    }
                }

                if (Form.getPlanetSelector.clickCheck(Mouse.getX(),MouseY) & !isPlanetSelectorOn){
                    isPlanetSelectorOn=true;
                    Form.summonPlanetSelector();
                }
                if (!willPlanetSelectorBeOn){
                    isPlanetSelectorOn=false;
                    willPlanetSelectorBeOn=true;
                }
            }


            if (isInfoOn){
                if (info.cancel.clickCheck(Mouse.getX(),MouseY)){
                    isInfoOn=false;
                    isEditOn=false;
                }
                if (isEditOn){
                    if (info.getPlanetSelector.clickCheck(Mouse.getX(),MouseY) & isPlanetSelectorOn){
                        willPlanetSelectorBeOn=false;
                    }
                    if (isPlanetSelectorOn){
                        for (int i =0;i<4;i++) {
                            if (info.imageButton[i].clickCheck(Mouse.getX(), MouseY)) {
                                willPlanetSelectorBeOn = false;
                                iconNumber=i;
                            }
                        }
                    }

                    if (info.getPlanetSelector.clickCheck(Mouse.getX(),MouseY) & !isPlanetSelectorOn){
                        isPlanetSelectorOn=true;
                        willPlanetSelectorBeOn=true;
                        info.summonPlanetSelector();
                    }
                    if (!willPlanetSelectorBeOn){
                        isPlanetSelectorOn=false;
                        willPlanetSelectorBeOn=true;
                    }
                    for(int i =0;i<info.fields.length;i++) {
                        if (info.fields[i].clickCheck(Mouse.getX(),MouseY)){
                            for(int k =0;k<info.fields.length;k++) {
                                info.fields[k].isActive=false;
                                fieldActivated=false;
                            }
                            info.fields[i].isActive=true;
                        }
                    }
                    for (int i = 0; i < info.fields.length; i++) {
                        if (info.fields[i].isActive & !fieldActivated) {
                            activeField = info.fields[i];
                            fieldActivated = true;
                        }
                    }
                    if (info.button.clickCheck(Mouse.getX(),MouseY)){
                        click.play();
                        for(int i =0;i<info.fields.length;i++) {
                            info.fields[i].convertInputToData();
                            info.fields[i].isActive=false;
                        }
                        if (!Objects.equals(info.fields[0].data, "")) {
                            info.planet.name = info.fields[0].data;
                        }
                        Pattern numberTemplate = Pattern.compile("\\d+");
                        Matcher matcher = numberTemplate.matcher(info.fields[1].data);
                        if (matcher.find()){
                        info.planet.mass = Float.parseFloat(info.fields[1].data);
                        }
                        matcher = numberTemplate.matcher(info.fields[2].data);
                        if (matcher.find()) {
                            if (info.planet.circle.radius > Integer.parseInt((info.fields[2].data))){
                                diminishingSound.play();
                            }else{
                                editSound.play();
                            }
                            info.planet.circle.setRadius(Integer.parseInt((info.fields[2].data)));
                            info.planet.circle.radius = Integer.parseInt((info.fields[2].data));

                        }

                        info.planet.icon = planets[iconNumber];
                        info.planet.iconNumber = iconNumber;
                        isEditOn=false;
                        isInfoOn=false;
                        fieldActivated=false;
                        info.button.label="Edit";
                    }
                }
                if ((info.button.clickCheck(Mouse.getX(),MouseY))&(isInfoOn&!isEditOn)){
                    isEditOn=true;
                    isPaused=true;
                    for (int i = 0; i < info.fields.length; i++) {
                        info.fields[i].data="";
                    }
                    info.button.label="Save";
                }

            }
            if (instruments.buttons[0].clickCheck(Mouse.getX(),MouseY)){
                if (sol.planetsQuantity<30){
                mod=0;
                if (isListOn){
                isListCurrentlyOn= true;}
                isListOn=false;
                isMenuHidden=true;}
                else{
                    error.play();
                }
            }
            if (isListOn){
                sol.update(Mouse.getX(),MouseY,planetList,planetList.startNumber);
            }

        }

        if (isInfoOn & !isMusicPlaying){
            isMusicPlaying=true;
            switch (info.planet.iconNumber){
                case(0):{
                    instrument=bandjo;
                    break;
                }
                case(1):{
                    instrument=drums;
                    break;
                }
                case(2):{
                    instrument=fluete;
                    break;
                }
                case(3):{
                    instrument=garmonica;
                    break;
                }
                case(4):{
                    instrument=doctorWho;
                    break;
                }
            }
            assert instrument != null;
            instrument.loop();
        }
        if (!isInfoOn & isMusicPlaying){
            assert instrument != null;
            instrument.stop();
            isMusicPlaying=false;
        }

        if (input.isMousePressed(Input.MOUSE_RIGHT_BUTTON)){
            if(vectorActivated){
                sol.giveZeroSpeed();
                vectorActivated=false;

            }
        }
        if (input.isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON)){
            for (int i=0;i<sol.planetsQuantity;i++) {
                if (sol.Planets[i].circle.contains(Mouse.getX(), MouseY)) {
                    sol.Planets[i].Xcoord=Mouse.getX();
                    sol.Planets[i].circle.setCenterX(Mouse.getX());
                    sol.Planets[i].Ycoord=MouseY;
                    sol.Planets[i].circle.setCenterY(MouseY);
                    if (whichPlanetGrabbed==-1){
                        grab.play();
                        whichPlanetGrabbed=i;
                        break;
                    }
                }

            }

        }
        if (whichPlanetGrabbed!=-1) {
            if (sol.Planets[whichPlanetGrabbed]!=null) {
                if (sol.Planets[whichPlanetGrabbed].Xcoord != Mouse.getX() | sol.Planets[whichPlanetGrabbed].Ycoord != MouseY) {
                    whichPlanetGrabbed = -1;
                }
            }
        }
        if (input.isMousePressed(Input.MOUSE_MIDDLE_BUTTON)){
            if (!vectorActivated) {
                for (int i = 0; i < sol.planetsQuantity; i++) {
                    if (sol.Planets[i].circle.contains(Mouse.getX(), MouseY)) {
                        vectorActivated = true;
                        vectPlanet = i;
                        vectOriginX = sol.Planets[vectPlanet].Xcoord;
                        vectOriginY = sol.Planets[vectPlanet].Ycoord;
                        velVector = new Line(vectOriginX, vectOriginY, Mouse.getX(), MouseY);
                    }
                }
            }
        }
        if (vectorActivated){
            vectOriginX = sol.Planets[vectPlanet].Xcoord;
            vectOriginY=sol.Planets[vectPlanet].Ycoord;
            velVector.set(sol.Planets[vectPlanet].circle.getCenterX(),sol.Planets[vectPlanet].circle.getCenterY(),Mouse.getX(),MouseY);
            //velVector.set(vectOriginX,vectOriginY,Mouse.getX(),MouseY);
        }

        if (input.isMouseButtonDown(Input.MOUSE_MIDDLE_BUTTON)) {
            int dX=Mouse.getX() - oldX;
            int dY=MouseY - oldY;
            gridStartX=(gridStartX+dX)%100;
            gridStartY=(gridStartY+dY)%100;
            sol.shift(dX,dY);
        }
        if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
            if (isInfoOn) {
                if (info.rectangle.contains(Mouse.getX(), MouseY)) {
                    info.drag(Mouse.getX() - oldX, MouseY - oldY);
                }
            }
        }
        oldX = Mouse.getX();
        oldY = MouseY;

    }

    public void render(GameContainer container, Graphics graphics) throws SlickException {
        graphics.setLineWidth(2);
        background.draw(0, 0, 1600, 900);
            if (isGridOn) {
                for (int i = 0; i < 1600 / (100 * scale); i++) {
                    graphics.drawLine(gridStartX + (100 * scale) * i, gridStartY, gridStartX + (100 * scale) * i, 900 + gridStartY);
                }
                for (int i = 0; i < 900 / (100 * scale); i++) {
                    graphics.drawLine(gridStartX, gridStartY + (100 * scale) * i, 1600 + gridStartX, gridStartY + (100 * scale) * i);
                }
            }


            if (!vectorActivated) {
                sol.draw(graphics, isLightingOn, isNamesOn);
            } else
                sol.draw(graphics, isLightingOn, false);
            if (vectorActivated) {
                float velX = velVector.getDX();
                float velY = velVector.getDY();
                float r = (float) (sqrt(((velX * velX) / scale / scale) + abs(velY * velY) / scale / scale) / 1000);
                graphics.setColor(new Color(r, 0, 1 - (r * r / 16)));
                graphics.drawString(new DecimalFormat("#0.00").format(velX / 2000 / scale) + ";" + new DecimalFormat("#0.00").format(velY / 2000 / scale), sol.Planets[vectPlanet].Xcoord + sol.Planets[vectPlanet].circle.radius, sol.Planets[vectPlanet].Ycoord + sol.Planets[vectPlanet].circle.radius);
                graphics.setLineWidth(5);
                graphics.draw(velVector);
                graphics.setColor(Color.white);
                graphics.setLineWidth(2);
            }
            if (isListOn) {
                planetList.draw(graphics, sol);
            }
            if (!isMenuHidden) {
                instruments.draw(graphics);
                graphics.setColor(Color.black);
                graphics.fill(pause.btn);
                graphics.fill(clear.btn);
                graphics.fill(fastForward.btn);
                graphics.fill(returnToX1.btn);
                graphics.fill(slowDown.btn);
                graphics.setColor(Color.white);
                graphics.draw(pause.btn);
                graphics.drawString(pause.label, pause.xl + 5, pause.yt + 1);
                graphics.draw(clear.btn);
                graphics.drawString(clear.label, clear.xl + 5, clear.yt + 1);
                graphics.draw(fastForward.btn);
                graphics.drawString(fastForward.label, fastForward.xl + 5, fastForward.yt + 1);
                graphics.draw(slowDown.btn);
                graphics.drawString(slowDown.label, slowDown.xl + 5, slowDown.yt + 1);
                graphics.draw(returnToX1.btn);
                graphics.drawString(returnToX1.label, returnToX1.xl + 5, returnToX1.yt + 1);
                graphics.drawString("Simulation speed: " + new DecimalFormat("#0.00").format(GravityField.deltaT), fastForward.xl + 50, returnToX1.yt + 1);
                graphics.drawString("Scale: " + new DecimalFormat("#0.00").format(scale), fastForward.xl + 50, returnToX1.yt + 20);
            }
            if (mod == -1) {
                graphics.setColor(Color.black);
                graphics.fill(hideMenu.btn);
                graphics.setColor(Color.white);
                graphics.draw(hideMenu.btn);
                graphics.drawString(hideMenu.label, hideMenu.xl + 5, hideMenu.yt + 1);
            }
            if (isInfoOn) {
                if (isEditOn) {
                    info.startDraw(graphics);
                    info.finishDrawForm(graphics, planets[iconNumber]);
                    if (fieldActivated) {
                        activeField.draw(graphics);

                    }
                } else {
                    info.startDraw(graphics);
                    info.finishDrawInfo(graphics);
                }

            }
            if (mod == -2) {
                formSummed = true;
                formSum = false;

                Form.startDraw(graphics);
                Form.finishDrawForm(graphics, planets[iconNumber]);
                for (int i = 0; i < Form.fields.length; i++) {
                    if (Form.fields[i].isActive & !fieldActivated) {
                        activeField = Form.fields[i];
                        fieldActivated = true;
                    }
                }
                if (fieldActivated) {
                    activeField.draw(graphics);
                }
            }
            if (mod == 0) {
                // graphics.setColor(Color.red);
                graphics.drawString("Create Planet", 700, 200);
            }


            if (isPlanetSelectorOn & mod == -2) {
                Form.drawPlanetSelector(planets, graphics);
            }
            if (isPlanetSelectorOn & isEditOn) {
                info.drawPlanetSelector(planets, graphics);
            }
        }


    public static void main(String[] args) throws SlickException {
        AppGameContainer app = new AppGameContainer(new Main("Hello Space"));
        app.setDisplayMode(1600,900,true);
        app.start(); }}