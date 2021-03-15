import javax.swing.*;
import java.awt.*;

public class Communicator extends JFrame {

    private JPanel panel;
    private JTextArea textArea;
    private JLabel label;


    //window off to the side of the game that tells the player what to do
    public Communicator()
    {
        super("Communication Terminal");
        Container window = getContentPane();
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        label = new JLabel("Terminal");
        textArea = new JTextArea();
        textArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(textArea);
        window.add(panel);
        //panel.add(label, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        setSize(320, 400);
        setVisible(true);

        textArea.setBackground(Color.BLACK);
        textArea.setForeground(Color.GREEN);
        textArea.setFont(new Font("Dialog",1,12));
        textArea.setText("Initializing Console Dialog 314159\n>>");

    }

    //method of communicating with the player
    public void sendMessage(String message)
    {
        textArea.setText(textArea.getText() + "Guide: " + message + "\n>>");
        //label.setText(message);
    }


    //for every time a level ends, automatically called by gamestatemanager.backState
    public void resetDialog(){
        textArea.setText("Initializing Console Dialog 314159\n>>");

    }



}
