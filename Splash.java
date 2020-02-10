
package javaapplication9;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 *
 * @author 计算机
 */
public class Splash extends JFrame{

    Image startImg = null;
    Container container;
    
   //Timer timer=new Timer(1000,this);

    public Splash() {


        startImg = new ImageIcon("img/start.jpg").getImage();

	setUndecorated(true);//让Frame窗口失去边框和标题栏的修饰

	setSize(420,594);
      
        
	setLocationRelativeTo(null);

	setVisible(true);

    }

	

    @Override
    public void paint(Graphics g) {

	g.drawImage(startImg, 0, 0, this);

    }

}
