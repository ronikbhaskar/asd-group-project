import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class TileLightUpEasy extends GameState implements ActionListener {

    private int check, light;
    private ArrayList<Platform> platforms;
    private ArrayList<Integer> chosen;
    private Player player;
    private ArrayList<Enemy> enemies;
    private ArrayList<Button> buttons;
    //private JPanel panel;
    //private JButton[][]buttons;
    private Exit exit;
    private boolean messageSent;

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

    public TileLightUpEasy(GameStateManager gsm) {
        super(gsm);
        init();
        check=0;
        chosen=new ArrayList<Integer>(25);
        for(int x=0; x<25; x++)
            chosen.add(x);
        light=randomButton();
        Game.communicator.sendMessage("In order to move on, all of the\nbackground tiles must be re-lit.");
        Game.communicator.sendMessage("Press enter on a tile to activate it, then\nmove to the next one.");
        messageSent=false;
    }

    private void initButtons(){
        int create=0;
        for(int x = 0; x<5;x++){
            for(int y = 0; y<5; y++){
                buttons.add(new Button(x*GamePanel.WIDTH/5,
                        y*GamePanel.HEIGHT/5,
                        GamePanel.WIDTH/5,
                        GamePanel.HEIGHT/5,
                        new Color(148,255,191),create+""));
                create++;
            }
        }
    }

    @Override
    public void init() {


        player = new Player(10, 755, 0);
        platforms = new ArrayList<Platform>();
        platforms=LevelLoader.loadPlatforms("TileLightUpEasyPlatforms.txt");
        enemies = new ArrayList<Enemy>();
        //enemies.add(new Enemy(300,750,40,40));
        buttons = new ArrayList<Button>();
        initButtons();
        exit = new Exit(700,700,100,100);
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
                if(out.equals(light+"")){
                    check++;
                    //System.out.println("check: "+check);
                    if(check>=25) {
                        exit.setVisible(true);
                        check++;
                        if(!messageSent) {
                            Game.communicator.sendMessage("Great Job! Now run to the exit!");
                            messageSent=true;
                        }
                    }
                    else {
                        light = randomButton();
                        Game.communicator.sendMessage("Now go to "+light);
                    }

                }
            }

        }
        if(player.getState()==1){
            gsm.backState();
            gsm.setState(new TileLightUpEasy(gsm));
        }

        //Game.container.updateBackPanel();

        player.tick();

        if(exit.getVisible() && player.x>=exit.x &&
                player.x+player.width<=exit.x+exit.width &&
                player.y>=exit.y &&
                player.y+player.height<=exit.y+exit.height){
            try{ Handler.setMaxLevel(3);}catch(IOException e){e.printStackTrace();}
            gsm.backState();
            gsm.setState(new MenuState(gsm,3));
        }
        if(GamePanel.esc){
            gsm.setState(new PauseState(gsm));
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
        //System.out.println((e.getSource()));
    }

    public int randomButton()
    {
        Random randy=new Random();
        int next=0, limit=chosen.size();

        //System.out.println(limit);
        next=chosen.remove(randy.nextInt(chosen.size()));
        //System.out.println(next);
        buttons.get(next).setColor(new Color(235,255,59));

        return next;
    }
}
