import java.awt.BorderLayout;  
import java.awt.Color;  
import java.awt.Container;  
import java.awt.Component;  
import java.awt.Graphics;  
import java.awt.event.ActionEvent;  
import java.awt.event.ActionListener;  

import javax.swing.BorderFactory;  
import javax.swing.Icon;  
import javax.swing.JButton;  
import javax.swing.JFrame;  
import javax.swing.JPanel;  

public class HelloCsdn {
    public static void main(String[] args) {
        HelloCsdnFrame frame=new HelloCsdnFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

//this part we construct a new frame HelloCsdnFrame

class HelloCsdnFrame extends JFrame{
    public static final int WIDTH=300;
    public static final int HEIGHT=200;
    public HelloCsdnFrame() {
        setTitle("XML下载定时器");
        setSize(WIDTH,HEIGHT);
        HelloCsdnPanel panel=new HelloCsdnPanel();
        ButtonPanel buttonPanel=new ButtonPanel();
        Container c=getContentPane();
        c.add(panel);

        c.add(buttonPanel);
    }
}
/**this part we extend our HelloCsdnFram to JFrame and
*construct a new object HelloCsdnPanel and add it on the frame
*/
class HelloCsdnPanel extends JPanel {
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawString("Hello CSDN.NET",MESSAGE_X,MESSAGE_Y);
    }
    public static final int MESSAGE_X=100;
    public static final int MESSAGE_Y=100;
}

class ButtonPanel extends JPanel {  

    public ButtonPanel() {  

        JButton btn = new JButton("Push Me", new BoxIcon(Color.blue, 2));  

        //设置按钮的翻转图标。  
        btn.setRolloverIcon(new BoxIcon(Color.cyan, 3));  

        //设置按钮的按下图标。  
        btn.setPressedIcon(new BoxIcon(Color.yellow, 4));  

        //设置文本相对于图标的横向位置。  
        btn.setHorizontalTextPosition(JButton.LEFT);  

        //设置按钮边框样式  
        btn.setBorder(BorderFactory.createEtchedBorder());  
        ActionListener actionListener = new ActionListener() {  
            public void actionPerformed(ActionEvent actionEvent) {  
                System.out.println("I was selected.");  
            }  
        };  
        btn.addActionListener(actionListener);  
        add(btn);  
    }  



}  

class BoxIcon implements Icon {  
    private Color color;  

    private int borderWidth;  

    BoxIcon(Color color, int borderWidth) {  
        this.color = color;  
        this.borderWidth = borderWidth;  
    }  

    public int getIconWidth() {  
        return 20;  
    }  

    public int getIconHeight() {  
        return 20;  
    }  

    public void paintIcon(Component c, Graphics g, int x, int y) {  
        g.setColor(Color.black);  
        g.fillRect(x, y, getIconWidth(), getIconHeight());  
        g.setColor(color);  
        g.fillRect(x + borderWidth, y + borderWidth, getIconWidth() - 2 * borderWidth,  
                getIconHeight() - 2 * borderWidth);  
    }  
}  



