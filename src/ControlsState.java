import java.awt.*;




public class ControlsState extends State {



    private String[] options = {"BACK"};
    private int currentSelection = 0;
    private Polygon p;

    public ControlsState(GameStateManager gsm) {
        super(gsm);
        init();
    }

    @Override
    public void init() {
        // TODO Auto-generated method stub
        p = new Polygon();
        p.addPoint(0,0);
        p.addPoint(GamePanel.WIDTH,0);
        p.addPoint(GamePanel.WIDTH, GamePanel.HEIGHT);
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
            GamePanel.select = false;
            switch(currentSelection) {
                case 0: gsm.backState();
                    break;

            }
        }

    }



    @Override
    public void draw(Graphics g) {

        g.setColor(Color.BLACK);
        g.drawImage(Assets.rainbow, 0,0,GamePanel.WIDTH, GamePanel.HEIGHT,null);
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

        g.setColor(Color.GRAY);
        g.fillRoundRect(100,400,75,75,10,10);
        g.fillRoundRect(20,400,75,75,10,10);
        g.fillRoundRect(180,400,75,75,10,10);
        g.fillRoundRect(100,320,75,75,10,10);

        g.fillRoundRect(600,320,75,75,10,10);

        g.fillRoundRect(340,320,175,75,10,10);


        g.setColor(Color.WHITE);
        g.setFont(new Font("Dialog", Font.BOLD, 48));
        g.drawString("MOVE",60,300);
        g.drawString("INTERACT",290,300);
        g.drawString("PAUSE",570,300);

        g.setFont(new Font("Dialog", Font.BOLD, 20));
        g.drawString("ENTER",420,344);
        g.drawString("UP",120,354);
        g.drawString("DOWN",105,444);
        g.drawString("LEFT",35,444);
        g.drawString("RIGHT",185,444);
        g.drawString("ESC",620,354);

    }



}


