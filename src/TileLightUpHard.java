import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class TileLightUpHard extends GameState implements ActionListener {

    private int check, light;
    private ArrayList<Platform> platforms;
    private ArrayList<Integer> chosen;
    private Player player;
    private ArrayList<Enemy> enemies;
    private ArrayList<Button> buttons;
    //private JPanel panel;
    //private JButton[][]buttons;
    private Exit exit;

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

    public TileLightUpHard(GameStateManager gsm) {
        super(gsm);
        init();
        check=0;
        chosen=new ArrayList<Integer>(36);
        for(int x=0; x<36; x++)
            chosen.add(x);
        light=randomButton();
        Game.communicator.sendMessage("This level is just like the other tile one, but there are no numbers.");
        Game.communicator.sendMessage("So I can't help you by telling you the next location.");
        Game.communicator.sendMessage("There are more tiles, new viruses, and more difficult jumps. Good Luck!");
    }

    private void initButtons(){
        int create=0;
        for(int x = 0; x<6;x++){
            for(int y = 0; y<6; y++){
                buttons.add(new Button(x*GamePanel.WIDTH/6,
                        y*GamePanel.HEIGHT/6,
                        GamePanel.WIDTH/6,
                        GamePanel.HEIGHT/6,
                        new Color(148,255,191),create+""));
                create++;
            }
        }
    }

    @Override
    public void init() {


        player = new Player(10, 755, 0);
        platforms = new ArrayList<Platform>();
        platforms=LevelLoader.loadPlatforms("TileLightUpHardPlatforms.txt");
        enemies = new ArrayList<Enemy>();
        enemies=LevelLoader.loadEnemies("TileLightUpHardEnemies.txt");
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
                    //out.println("check: "+check);
                    if(check>=36) {
                        exit.setVisible(true);
                        check++;
                        Game.communicator.sendMessage("Nice! Now you can move on.");
                    }
                    else {
                        light = randomButton();
                    }

                }
            }

        }
        if(player.getState()==1){
            gsm.backState();
            gsm.setState(new TileLightUpHard(gsm));
        }

        //Game.container.updateBackPanel();

        player.tick();

        if(exit.getVisible() && player.x>=exit.x &&
                player.x+player.width<=exit.x+exit.width &&
                player.y>=exit.y &&
                player.y+player.height<=exit.y+exit.height){
            try{ Handler.setMaxLevel(6);}catch(IOException e){e.printStackTrace();}
            gsm.backState();
            gsm.setState(new MenuState(gsm,6));
        }
        if(GamePanel.esc){
            gsm.setState(new PauseState(gsm));
        }

    }

    @Override
    public void draw(Graphics g) {
        for(Button button: buttons)
        {
            button.hardDraw(g);
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
