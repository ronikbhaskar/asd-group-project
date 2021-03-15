import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Assets {

    public static BufferedImage player,  playerWin, playerLose, rainbow;
    public static BufferedImage [] playerStand, playerRight, playerLeft, playerJump,blob,win;
    public static void init() {
        playerStand = new BufferedImage[1];
        playerJump = new BufferedImage[6];
        playerRight = new  BufferedImage[6];
        playerLeft = new BufferedImage[6];
        blob = new BufferedImage[2];
        win = new BufferedImage[8];
        //player = imageLoader("./Standing-1.png.png");

        arrayBuilder(playerStand,0,"Standing");
        arrayBuilder(playerRight,0,"Running");
        arrayBuilder(playerLeft,6,"Running");
        arrayBuilder(playerJump,0,"Jumping");
        arrayBuilder(blob,0,"blob");
        arrayBuilder(win,0,"Win screen");

        for(int i = 0; i<blob.length;i++){
            blob[i] = blob[i].getSubimage(1,7,12,9);
        }
    }

    public static void arrayBuilder(BufferedImage [] list, int start, String name){
        for(int i = start; i<list.length+start; i++){
            //System.out.println(name + "-" + (i+1) + ".png.png");
            list[i-start] = imageLoader(name + "-" + (i+1) + ".png.png");
        }
    }

    public static BufferedImage imageLoader(String path) {
        try {
            return ImageIO.read(Assets.class.getResource(path));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit(1);
        }

        return null;
    }



}
