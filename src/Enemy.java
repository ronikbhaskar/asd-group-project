import java.awt.*;

public class Enemy {
    public float x,y;
    public int width, height;
    public int state;

    public Animation blobimate;

    public Enemy(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        blobimate = new Animation(18, Assets.blob);
    }
    public void tick(){
        blobimate.runAnimation();
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.drawRect((int)x,(int)y,width,height);
        blobimate.drawAnimation(g,(int)x,(int)y,width,height);
    }
}
