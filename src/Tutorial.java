import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class Tutorial extends GameState {

    private ArrayList<Platform> platforms;
    private Player player;
    private ArrayList<Enemy> enemies;
    private ArrayList<Button> buttons;
    private Exit exit;
    private int leftRightDelay,
                jumpDelay,
                enemyDelay;
    private boolean buttonShown, exitShown;
    private String out;
    private final int DELAY_LENGTH = 240;

    protected Tutorial(GameStateManager gsm) {
        super(gsm);

        init();
    }

    @Override
    public ArrayList<Platform> getPlatforms() {
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

    @Override
    public void init() {
        player = new Player(GamePanel.WIDTH/2-45, GamePanel.HEIGHT-45, 0);
        platforms = new ArrayList<Platform>();
        platforms.add(new Platform(0,GamePanel.HEIGHT,20,GamePanel.WIDTH, new Color(0,0,0)));
        enemies = new ArrayList<Enemy>();
        buttons = new ArrayList<Button>();
        exit = new Exit(GamePanel.WIDTH-50,GamePanel.HEIGHT-(int)(1.5*player.height),(int)(1.5*player.height),2*player.width);

        leftRightDelay = DELAY_LENGTH;
        jumpDelay = DELAY_LENGTH;
        enemyDelay = DELAY_LENGTH;

        out = "";

        buttonShown = false;
        exitShown = false;

        Game.communicator.sendMessage("Use LEFT and RIGHT to move around");

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
        out = "";
        for(Button button: buttons)
        {
            button.tick();
            if(GamePanel.select &&
                    player.x>=button.x &&
                    player.x+player.width<=button.x+button.width &&
                    player.y>=button.y &&
                    player.y+player.height<=button.y+button.height){
                out = button.getOutput();

            }

        }
        if(player.getState()==1){
            //Game.communicator.sendMessage("Yeah, I can't let you touch viruses unless you want your code deleted, so I restart the level. Try not to touch them.");
            gsm.backState();
            gsm.setState(new Tutorial(gsm));
        }

        //Game.container.updateBackPanel();

        player.tick();

        updateTutorialDelay();

        if(exit.getVisible() && player.x>=exit.x &&
                player.x+player.width<=exit.x+exit.width &&
                player.y>=exit.y &&
                player.y+player.height<=exit.y+exit.height){
            try{ Handler.setMaxLevel(2);}catch(IOException e){e.printStackTrace();}
            gsm.backState();
            gsm.setState(new MenuState(gsm,2));
        }
        if(GamePanel.esc){
            gsm.setState(new PauseState(gsm));
        }
    }

    private void updateTutorialDelay(){
        if(player.x!=GamePanel.WIDTH/2-45 && leftRightDelay == DELAY_LENGTH){
            leftRightDelay--;
            //System.out.println(leftRightDelay);
        }else if(leftRightDelay<DELAY_LENGTH && leftRightDelay > 0){
            leftRightDelay--;
            //System.out.println(leftRightDelay);
        }else if (leftRightDelay==0 && jumpDelay == DELAY_LENGTH){
            Game.communicator.sendMessage("Use UP to jump onto the platform");
            platforms.add(new Platform(GamePanel.WIDTH/2,7*GamePanel.HEIGHT/8,20,GamePanel.WIDTH/4, new Color(0,0,0)));
            leftRightDelay--;
        } else if(player.y<GamePanel.HEIGHT-45 && jumpDelay == DELAY_LENGTH && leftRightDelay==-1){
            jumpDelay--;
        }else if(jumpDelay<DELAY_LENGTH && jumpDelay > 0){
            jumpDelay--;
            //System.out.println(leftRightDelay);
        }else if(jumpDelay==0 && !buttonShown){
            buttonShown=true;
            Game.communicator.sendMessage("Use ENTER to press the button");
            buttons.add(new Button(GamePanel.WIDTH/2,7*GamePanel.HEIGHT/8-(int)(1.5*player.height),(int)(1.5*player.height),player.width*3,new Color(128,255,200),"X"));
        }else if(out.equals("X") && enemyDelay==DELAY_LENGTH){
            enemyDelay--;
            Game.communicator.sendMessage("This is a virus. Touching it would delete your code, so every time you try, I restart the\nlevel.");

            platforms.set(1,new Platform(0,7*GamePanel.HEIGHT/8,20,GamePanel.WIDTH, new Color(0,0,0)));
            player.falling=true;
            enemies.add(new Enemy(GamePanel.WIDTH/4,7*GamePanel.HEIGHT/8-player.height,player.height,player.height));
        }else if (enemyDelay<DELAY_LENGTH && enemyDelay>0){
            enemyDelay--;
        }else if(enemyDelay==0 && !exitShown){
            Game.communicator.sendMessage("Use DOWN to get to the exit");
            exit.setVisible(true);
            exitShown=true;
        }
    }

    @Override
    public void draw(Graphics g) {

        g.setColor(Color.WHITE);
        g.fillRect(0,0,GamePanel.WIDTH,GamePanel.HEIGHT);
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
}
