import java.awt.*;

public class Button extends Platform {

    private String output;
    private Color color;
    public Button(int x, int y, int height, int width, Color color, String output) {
        super(x, y, height, width, color);
        this.color = color;
        this.output = output;
    }

    public String getOutput(){
        return output;
    }

    public void setOutput(String output){
        this.output = output;
    }

    public void setColor(Color color){this.color = color;}

    @Override
    public void draw(Graphics g)
    {
        g.setColor(color);
        int xOffset=0;
        if(output.length()>0)
            xOffset = width/(3*output.length());
        g.fillRect((int)x,(int)y,width,height);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Dialog",1,height/(output.length()+1)));
        g.drawString(output, (int)x+xOffset,(int)y+2*height/3);
        g.setColor(Color.BLACK);
        g.drawRect((int)x,(int)y,width,height);
    }


    public void hardDraw(Graphics g)
    {
        g.setColor(color);
        g.fillRect((int)x,(int)y,width,height);
        g.setColor(Color.BLACK);
        g.drawRect((int)x,(int)y,width,height);
    }
    /*
    @Override
   public void draw(Graphics g)
   {
       g.setColor(color);
       g.fillRect((int)x,(int)y,width,height);
       g.setColor(Color.WHITE);
       g.setFont(new Font("Dialog",1,height/2));
       g.drawString(output, (int)x,(int)y+height/2);
       g.setColor(Color.BLACK);
       g.drawRect((int)x,(int)y,width,height);
   }

     */
}
