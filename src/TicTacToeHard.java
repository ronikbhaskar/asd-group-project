import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;
import java.io.File;
public class TicTacToeHard extends GameState implements ActionListener {

    private ArrayList<Platform> platforms;
    private Player player;
    private ArrayList<Enemy> enemies;
    private ArrayList<Button> buttons;
    //private JPanel panel;
    //private JButton[][]buttons;
    private Exit exit;
    private Button label;
    private boolean gameOver =false, messageSent;

    private final int DELAY_LENGTH = 60;
    private int moveDelay;
    private boolean moved;
    public ArrayList<Platform> getPlatforms()
    {
        return platforms;
    }

    @Override
    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    @Override
    public ArrayList<Button> getButtons() {
        return buttons;
    }

    public TicTacToeHard(GameStateManager gsm) {
        super(gsm);
        init();
    }

    @Override
    public void init() {
        player = new Player(GamePanel.WIDTH/2-45, GamePanel.HEIGHT-45, 0);
        platforms = new ArrayList<Platform>();

        Color pColor = new Color(135, 77, 191);
        //x, y, height, width, color
        platforms.add(new Platform(0,GamePanel.HEIGHT,20,GamePanel.WIDTH, Color.BLACK));//keeps person from falling thru floor
        platforms.add(new Platform(GamePanel.WIDTH/4+20,600,20,50, pColor));
        platforms.add(new Platform(GamePanel.WIDTH/2-30,500,20,80, pColor));
        platforms.add(new Platform(GamePanel.WIDTH/2,340,20,50, pColor));

        platforms.add(new Platform(GamePanel.WIDTH/2-30,GamePanel.HEIGHT*3/4+100,20,50, pColor));//first
        platforms.add(new Platform(100,GamePanel.HEIGHT/2+70,20,60,pColor));

        platforms.add(new Platform(GamePanel.WIDTH-200,520,20,30, pColor));
        platforms.add(new Platform(GamePanel.WIDTH*2/3-10,160,20,50, pColor));
        platforms.add(new Platform(GamePanel.WIDTH/2-50,200,20,40,pColor));
        platforms.add(new Platform(700,GamePanel.HEIGHT/2-150,20,30,pColor));
        platforms.add(new Platform(220,140,18,20,pColor));
        platforms.add(new Platform(100,310,10,10,pColor));


        enemies = new ArrayList<Enemy>();
        //x,y,width,height
        enemies.add(new Enemy(200,160,60,70));
        enemies.add(new Enemy(500,420,65,90));
        enemies.add(new Enemy(100,560,80,60));
        enemies.add(new Enemy(GamePanel.WIDTH/2-30,400,30,30));
        enemies.add(new Enemy(610,350,50,20));
        enemies.add(new Enemy(70,90,150,90));


        Game.communicator.sendMessage("To pass this level, you must get 3 X's in a row by selecting squares from the 9x9 board.");
        Game.communicator.sendMessage("Mind the viruses.");

        buttons = new ArrayList<Button>();
        initButtons();
        exit = new Exit(700,700,100,100);
        moveDelay=DELAY_LENGTH;
        moved=false;
        messageSent=false;
    }

    private void initButtons(){
        for(int x = 0; x<3;x++){
            for(int y = 0; y<4; y++){
                Color tempColor;
                String tempOutput="";
                if (y==3) {
                    tempColor=new Color(95, 49, 140);
                    if (x==0)
                        tempOutput="Tic";
                    else if (x==1)
                        tempOutput="Tac";
                    else
                        tempOutput="Toe";
                }else {
                    if ((x+y)%2==0)
                        tempColor=new Color(212, 185, 240);
                    else
                        tempColor=new Color(228, 207, 250);
                }

                buttons.add(new Button(x*GamePanel.WIDTH/3,
                        y*GamePanel.HEIGHT/4,
                        GamePanel.WIDTH/4,
                        GamePanel.HEIGHT/3,
                        tempColor,
                        tempOutput));

            }
        }
    }

    @Override
    public void tick() {
        for(Platform platform: platforms)
        {
            platform.tick();
        }
        for(Enemy enemy: enemies)
        {
            enemy.tick();
        }
        String out = "";
        for(Button button: buttons)
        {
            button.tick();
            if(GamePanel.select &&
                    player.x>=button.x &&
                    player.x+player.width<=button.x+button.width &&
                    player.y>=button.y &&
                    player.y+player.height<=button.y+button.height){
                out = button.getOutput();
                if(out.equals("") &&gameOver==false){
                    doMove(button);
                    moved=true;
                }
            }
        }
        if(moved && moveDelay==DELAY_LENGTH){
            moveDelay--;
        }
        else if(moveDelay<DELAY_LENGTH && moveDelay>0){
            moveDelay--;
        }
        else if(moveDelay==0){
            moved=false;
            enemyMove();
            moveDelay=DELAY_LENGTH;
        }

        player.tick();

        if(exit.getVisible() && player.x>=exit.x &&
                player.x+player.width<=exit.x+exit.width &&
                player.y>=exit.y &&
                player.y+player.height<=exit.y+exit.height){
            try{ Handler.setMaxLevel(7);}catch(IOException e){e.printStackTrace();}
            gsm.backState();
            gsm.setState(new MenuState(gsm,7));
        }
        if(GamePanel.esc){
            gsm.setState(new PauseState(gsm));
        }
        if(player.getState()==1){
            gsm.backState();
            gsm.setState(new TicTacToeHard(gsm));
        }
    }

    @Override
    public void draw(Graphics g) {
        for(Button button: buttons)
        {
            button.draw(g);
        }
        for(Platform platform: platforms)
        {
            platform.draw(g);
        }
        for(Enemy enemy: enemies)
        {
            enemy.draw(g);
        }
        player.draw(g);
        exit.draw(g);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println((e.getSource()));
    }


    static Random randy = new Random ();
    private int buttonsPressed=0;
    public void doMove (Button button){
        button.setOutput("X");
        buttonsPressed++;
        if (buttonsPressed==9 && messageSent==false) {
            boolean temp = checkForWinner();
            if (temp==false){
                Game.communicator.sendMessage("Objective failed.");
                enemies.add(new Enemy(0,750,50,50));
                Game.communicator.sendMessage("Use any virus to try again.");
                messageSent=true;
                return;
            }
        }
        checkForWinner();
    }

    public void enemyMove(){
        int nextSpot=-1;
        try{ nextSpot = checkForTwoInARow(); }//0-2 rows, 3-5 cols, 6-7 diagonals
        catch (IOException j){};

        if (gameOver==false)
            if (nextSpot!=-1){ //there is a place to win/not lose
                String rcd = toString(nextSpot);
                int nextOh = rcd.indexOf("-");//the blank spot
                if (nextSpot>2 && nextSpot<6){ //3 columns
                    if (nextSpot==4)
                        nextOh+=4;
                    else if (nextSpot==5)
                        nextOh+=8;
                    buttons.get(nextOh).setOutput("O");
                }else if (nextSpot<3){ //3 rows
                    buttons.get(nextSpot+(nextOh*4)).setOutput("O");
                }else if (nextSpot==6 || nextOh==1){ //tl to br
                    buttons.get(nextOh*5).setOutput("O");
                }else  //bl to tr
                    buttons.get(2+(3*nextOh)).setOutput("O");

                buttonsPressed++;
                checkForWinner();

            }else{ //random selection
                boolean flag=false;
                do {
                    int option=-1;
                    do
                        option=randy.nextInt(buttons.size());
                    while (option==3 || option==7 || option==11);
                    Button buttonOption=buttons.get(option);
                    if (!(buttonOption.getOutput().equals("X")) && !(buttonOption.getOutput().equals("O"))) {
                        buttonOption.setOutput("O");
                        buttonsPressed++;
                        checkForWinner();
                        flag=true;
                    }
                } while (flag==false);
            }
        return;
    }

    public int checkForTwoInARow () throws IOException{
        Scanner fr = new Scanner(new File("possibilities.txt"));
        while (fr.hasNext()) {
            String winCondition = fr.nextLine();
            for (int x=0; x<8; x++) { //3 rows, 3 cols, 2 diagonals
                String rcd = toString(x); //rcd = row col diagonal
                if (rcd.equals(winCondition))
                    return x;
            }
        }
        return -1;
    }

    public String toString (int rowOrCol) { //will give it the 3 rows, then the 3 columns
        Button [] list;
        if (rowOrCol>2&&rowOrCol<6){  //0-2 are rows, 3-5 are columns, 6 is tl->br diagonal, 7 is bl->tr diagonal
            if (rowOrCol==3)
                list= new Button [] {buttons.get(0), buttons.get(1), buttons.get(2)};
            else if (rowOrCol==4)
                list= new Button [] {buttons.get(4), buttons.get(5), buttons.get(6)};
            else //(if rowOrCol==5)
                list= new Button [] {buttons.get(8), buttons.get(9), buttons.get(10)};
        } else if (rowOrCol<3){
            list=new Button[] {buttons.get(rowOrCol),buttons.get(rowOrCol+4),buttons.get(rowOrCol+8)};
        } else if (rowOrCol==6) //top left to bottom right
            list= new Button [] {buttons.get(0), buttons.get(5), buttons.get(10)};
        else //bottom left to top right
            list= new Button [] {buttons.get(2), buttons.get(5), buttons.get(8)};

        String string="";
        for (int x=0; x<list.length; x++) {
            Button temp = list[x];
            String pic = temp.getOutput();
            if (pic==""){
                string+="-";
            }else if (pic.equals("X")){
                string+="x";
            } else{ //if (pic.equals("O")) {
                string+="o";
            }
        }
        return string;
    }

    public boolean checkForWinner() {
        Button [] list;
        for (int x=0; x<6;x++){ //3 rows, 3 cols
            String rorc = toString(x); //rorc = row or col
            if (x>2) { //0-2 are rows, 3-5 are columns
                if (x==3)
                    list= new Button [] {buttons.get(0), buttons.get(1), buttons.get(2)};
                else if (x==4)
                    list= new Button [] {buttons.get(4), buttons.get(5), buttons.get(6)};
                else //(if rowOrCol==5)
                    list= new Button [] {buttons.get(8), buttons.get(9), buttons.get(10)};
            }else //if x<3
                list=new Button[] {buttons.get(x),buttons.get(x+4),buttons.get(x+8)};
            String b1=list[0].getOutput();
            String b2=list[1].getOutput();
            String b3=list[2].getOutput();
            if (b1.equals(b2) && b1.equals(b3)) {
                if (b1.equals("X"))
                    winner(true);
                else if (b1.equals("O"))
                    winner(false);
                return true;
            }
        }

        //tests diagonals
        String tempB=buttons.get(5).getOutput();
        if (!(tempB.equals(""))){
            if (tempB.equals(buttons.get(0).getOutput()) && tempB.equals(buttons.get(10).getOutput())) {//tl to br
                winner(tempB.equals("X"));
                return true;
            }
            if (tempB.equals(buttons.get(2).getOutput()) && tempB.equals(buttons.get(8).getOutput())) {//bl to tr
                winner(tempB.equals("X"));
                return true;
            }
        }

        if (buttonsPressed==9 && messageSent==false) {
            Game.communicator.sendMessage("Objective failed.");
            enemies.add(new Enemy(0,750,50,50));
            Game.communicator.sendMessage("Use any virus to try again.");
            messageSent=true;
            return true;
        }
        return false;
    }

    public void winner(boolean didExesWin) {
        if (didExesWin){
            Game.communicator.sendMessage("Objective completed.");
            Game.communicator.sendMessage("Enter the exit.");
            exit.setVisible(true);
        } else if (messageSent==false){
            Game.communicator.sendMessage("Objective failed.");
            enemies.add(new Enemy(0,750,50,50));
            Game.communicator.sendMessage("Use any virus to try again.");
            messageSent=true;
        }
        gameOver=true;
    }
}




