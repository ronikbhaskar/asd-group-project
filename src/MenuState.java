import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.KeyEvent;


public class MenuState extends State{

    private String[] options = {"BACK","LEVEL 1","LEVEL 2 LOCKED","LEVEL 3 LOCKED", "LEVEL 4 LOCKED", "LEVEL 5 LOCKED", "LEVEL 6 LOCKED", "LEVEL 7 LOCKED","LEVEL 8 LOCKED"};
    private int currentSelection = 0;
    private Polygon p;

    public MenuState(GameStateManager gsm, int nextLevel) {
        super(gsm);
        GamePanel.select = false;
        init();
        currentSelection = nextLevel;
    }

    public MenuState(GameStateManager gsm) {
        super(gsm);
        GamePanel.select = false;
        init();
    }

    @Override
    public void init() {

        p = new Polygon();
        p.addPoint(0,0);
        p.addPoint(GamePanel.WIDTH *3/4,0);
        p.addPoint(GamePanel.WIDTH /4, GamePanel.HEIGHT);
        p.addPoint(0,GamePanel.HEIGHT);

        for(int i = 0; i<Handler.getCurrentMax()+1; i++){
            if(options[i].length()>8 && options[i].substring(8).equals("LOCKED")){
                options[i]=options[i].substring(0,7);
            }
        }



    }

    @Override
    public void tick() {
        if(GamePanel.down) {
            currentSelection++;
            currentSelection%=Handler.getCurrentMax()+1;
            GamePanel.down = false;

        }else if(GamePanel.up) {
            currentSelection--;
            if(currentSelection==-1)
                currentSelection=Handler.getCurrentMax();
            GamePanel.up = false;
        }


        if(GamePanel.select) {
            GamePanel.select=false;
            switch(currentSelection) {
                case 0: gsm.backState();
                    break;
                case 1: gsm.backState();gsm.setState(new Tutorial(gsm));
                    break;
                case 2: gsm.backState();gsm.setState(new TileLightUpEasy(gsm));
                    break;
                case 3: gsm.backState();gsm.setState(new TicTacToeEasy(gsm));
                    break;
                case 4: gsm.backState();gsm.setState(new CalculatorEasy(gsm));
                    break;
                case 5: gsm.backState();gsm.setState(new TileLightUpHard(gsm));
                    break;
                case 6: gsm.backState();gsm.setState(new TicTacToeHard(gsm));
                    break;
                case 7: gsm.backState();gsm.setState(new CalculatorHard(gsm));
                    break;
                case 8: gsm.backState();gsm.setState(new TimerLevel(gsm));
                    break;

            }
        }

    }

    @Override
    public void draw(Graphics g) {

        g.setColor(Color.BLACK);
        //g.fillRect(0, 0, GamePanel.WIDTH/2, GamePanel.HEIGHT);

        g.fillRect(0,0,GamePanel.WIDTH,GamePanel.HEIGHT);

        for(int i = 0; i < options.length; i++) {
            if(i==currentSelection) {
                g.setColor(Color.GREEN);
                g.setFont(new Font("Dialog", Font.BOLD, 60));
            }else {
                g.setColor(Color.WHITE);
                g.setFont(new Font("Dialog", Font.BOLD, 48));
            }


            g.drawString(options[i], 20, 68 + i * 72);
        }

    }



}

