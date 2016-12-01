import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SorDoc extends JFrame{
    private JButton Otsordocat;
    private JPanel Windowmain;
    private JButton chooseFileButton;



    private SorDoc() {
        Otsordocat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,"File has been renamed");
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("SorDoc");
        frame.setContentPane(new SorDoc().Windowmain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
