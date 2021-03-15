import java.awt.*;

public class PauseState extends State {

    private String[] options = {"RESUME", "Exit to HOME", "Quit"};
    private int currentSelection = 0;
    private Polygon p;

    public PauseState(GameStateManager gsm) {
        super(gsm);
        init();
    }

    @Override
    public void init() {
        // TODO Auto-generated method stub
        p = new Polygon();
        p.addPoint(0,0);
        p.addPoint(GamePanel.WIDTH *3/4,0);
        p.addPoint(GamePanel.WIDTH /4, GamePanel.HEIGHT);
        p.addPoint(0,GamePanel.HEIGHT);
    }

    @Override
    public void tick() {
        if(GamePanel.down) {
            currentSelection++;
            currentSelection%=options.length;
            GamePanel.down = false;

        }else if(GamePanel.up) {
            currentSelection--;
            if(currentSelection==-1)
                currentSelection=options.length-1;
            GamePanel.up = false;
        }


        if(GamePanel.select) {
            GamePanel.select=false;
            switch(currentSelection) {
                case 0: gsm.backState();
                    break;
                case 1: gsm.iterateBackState(2);
                    break;
                case 2: System.exit(0);
            }
        }

    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        //g.fillRect(0, 0, GamePanel.WIDTH/2, GamePanel.HEIGHT);

        g.fillRect(0,0,GamePanel.WIDTH,GamePanel.HEIGHT);

        g.setColor(Color.GRAY);
        g.setFont(new Font("Dialog",Font.BOLD+Font.ITALIC,56));
        g.drawString("PAUSED",50,68);

        for(int i = 0; i < options.length; i++) {
            if(i==currentSelection) {
                g.setColor(Color.GREEN);
                g.setFont(new Font("Dialog", Font.BOLD, 60));
            }else {
                g.setColor(Color.WHITE);
                g.setFont(new Font("Dialog", Font.BOLD, 48));
            }


            g.drawString(options[i], 20, 168 + i * 72);
        }
    }
}
