import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class UI {

    private UI(){
        // создаем панель интерфейса
        JFrame jfrm = new JFrame("SorDoc");
        jfrm.setLocation(800,500);
        jfrm.setLayout(null);
        jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jfrm.setSize(800, 400);
        jfrm.setResizable(false);

        //Тут все про шрифт
        Font BigFontTR = new Font("Arial", Font.BOLD, 20);

        // создаем иконку для интерфейса
        JLabel lableicon = new JLabel(new ImageIcon(getClass().getResource("/LablePicture/Lable.jpg")));
        lableicon.setBounds(0,0, 800,400);

        // создаем кнопку старта и задаем ее параметры
        JButton Startbtn = new JButton("Start");
        Startbtn.setBounds(5,260,300,80);
        Startbtn.setFont(BigFontTR);//применяем шрифт к кнопке
        Startbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { new DS();}
        });

        // создаем кнопку выхода и задаем ее параметры
        JButton Exitbtn = new JButton("Close");
        Exitbtn.setBounds(490,260,300,80);
        Exitbtn.setFont(BigFontTR);//применяем шрифт к кнопке
        Exitbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jfrm.setVisible(false);
            }
        });

        // все добавляем на панель интерфейса
        jfrm.add(Startbtn);
        jfrm.add(Exitbtn);
        jfrm.add(lableicon);
        jfrm.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                new UI();
            }
        });
    }
}