package javaapplication9;

import java.io.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import static javax.swing.JFrame.EXIT_ON_CLOSE;



public class FiveChess extends JFrame implements ActionListener{

    Container container; 

    GamePanel  gamePanel; // 游戏面板   
        
    private String[] imgArr = {"img/bg_1.png", "img/bg_2.jpg", "img/bg_3.jpg"};

    
    
    
    public FiveChess() {
            
	this.setIconImage(new ImageIcon("img/ic_launcher.png").getImage());                               
	this.setTitle("五子棋");
	this.setSize(642, 700);  
	this.setResizable(false);
	this.setLocationRelativeTo(null);
        
        
	gamePanel = new GamePanel();
        //gamePanel.setOpaque(false);//将面板设置透明
        container = this.getContentPane();
	container.add(gamePanel);
		
	initMyUI();

        this.setVisible(true);
        
	this.setDefaultCloseOperation(EXIT_ON_CLOSE);

    }

    private void initMyUI() {

	String[] captions = { "开始(S)", "音效(M)", "难度(D)", "设置(B)", "帮助(H)" };

	String[][] titles = { { "重新游戏", "保存",  "读档", "-", "悔棋", "-", "退出" },
            
            { "开启音效", "-", "关闭音效" }, {"简单", "中级", "困难"},
            
            {"切换背景","电脑先手","玩家先手"},{ "帮助", "-", "关于本游戏" } };
        
        String[] bg_name = { "背景1", "背景2", "背景3" };

                
	// 绘制菜单栏
	JMenuBar menuBar = new JMenuBar();
	JMenu menu;
	JMenuItem item;

	for (int i = 0; i < captions.length; i++) {

            menu = new JMenu(captions[i]);
            menu.setFont(new Font("黑体", Font.PLAIN, 15));
            menuBar.add(menu);
                        
            for (int j = 0; j < titles[i].length; j++) {
                            
		String caption = titles[i][j];
                                
		if ("-".equals(caption)) {
                    
                    menu.addSeparator(); // 给menuItem加一个分隔符
                    continue;
		}
                
                if ("切换背景".equals(caption)){
                    JMenu sonMenu = new JMenu("切换背景");              
                    JMenuItem sonItem ;                 
                    for (String Bg_Name : bg_name) {                 
                        sonItem = new JMenuItem(Bg_Name);
                        sonItem.setPreferredSize(new Dimension(60, 35)); 
                        sonItem.setFont(new Font("黑体", Font.PLAIN, 14));
                        sonMenu.add(sonItem);
                        sonItem.addActionListener(this);
                    }
                    sonMenu.setPreferredSize(new Dimension(80, 35));
                    sonMenu.setFont(new Font("黑体", Font.PLAIN, 14));
                    menu.add(sonMenu);
                } 
                else {
                    
                    item = new JMenuItem(titles[i][j]);
                    item.setPreferredSize(new Dimension(80, 35)); //设置大小
                    item.setFont(new Font("黑体", Font.PLAIN, 14));

                    menu.add(item);
                    item.addActionListener(this);
                }          
            }
	}

	this.setJMenuBar(menuBar);

    }

 

	

    /**

     * 菜单栏的操作
     * @param e     
     */

    @Override

    public void actionPerformed(ActionEvent e) {

	String s = e.getActionCommand();
       

	if ("重新游戏".equals(s)) {

            gamePanel.reset();

	}

	
        if ("保存".equals(s)) {
            
            try{
                FileOutputStream fo;
                fo = new FileOutputStream("programFile/data.txt");
                BufferedWriter writer ;
                writer =new BufferedWriter(new OutputStreamWriter(fo,"UTF-8"));
                
                int[][] temp1 = gamePanel.getChessBoard();
                
                for(int i=0; i< GamePanel.getLineCount(); i++){

                    for(int j=0; j< GamePanel.getLineCount(); j++){
                        //System.out.println(temp1[i][j]);
                        String str=Integer.toString(temp1[i][j]);
                        writer.write(str);
                        
                        writer.write(" ");
                    }
                    
                    writer.write("\n");
                }
                writer.close();
                
                JOptionPane.showMessageDialog(null, "存档成功！");
            }
            catch(IOException e1){
                
                JOptionPane.showMessageDialog(null, "存档失败！");
            }
            
        }
        
        if ("读档".equals(s)) {
            
            duDang();
            
        }
        
        if ("悔棋".equals(s)) {
            
            gamePanel.regret();
            
	}

        if ("开启音效".equals(s)) {

            gamePanel.setSoundswitch(true);

            JOptionPane.showMessageDialog(null, "音效已开启！");

	}
        
        if ("关闭音效".equals(s)) {

            gamePanel.setSoundswitch(false);

            JOptionPane.showMessageDialog(null, "音效已关闭！");

	}
        
	if ("退出".equals(s)) {

            this.dispose();               

        }

	if ("简单".equals(s)) {

            gamePanel.setLevel(1);

            JOptionPane.showMessageDialog(null, "已切换成简单！");

	}

	if ("中级".equals(s)) {

            gamePanel.setLevel(2);

            JOptionPane.showMessageDialog(null, "已切换成中级！");

	}

	if ("困难".equals(s)) {

            gamePanel.setLevel(3);

            JOptionPane.showMessageDialog(null, "已切换成困难！");

	}

	if ("背景1".equals(s)) {
            setImg(1);
        }
        
        if ("背景2".equals(s)) {
            setImg(2);
        }
        
        if ("背景3".equals(s)) {
            setImg(3);
        }
        
         if ("电脑先手".equals(s)) {
            
            gamePanel.setFirstHand(2);
            
	}
         
         if ("玩家先手".equals(s)) {
            
            gamePanel.setFirstHand(1);
            
	}
        
        
	if ("帮助".equals(s)) {

            new AboutHelp(container);

        }

	if ("关于本游戏".equals(s)) {

            new AboutAuthor(container);

	}
                
               

        
        
    }

    
    public void setImg(int i){
        gamePanel.setBackground(imgArr[i-1]);

        /*this.setVisible(false);

        long startTime = System.currentTimeMillis();

        long endTime = startTime + 1000;
        
        while(System.currentTimeMillis()<endTime);

        this.setVisible(true);*/

        JOptionPane.showMessageDialog(null, "切换背景成功！");
        
    }
    
    public void duDang(){
        int count1=0,count2=0;
        try{
            
            BufferedReader in ;
                
            in = new BufferedReader(new InputStreamReader
                        
            (new FileInputStream("programFile/data.txt"),"UTF-8"));
                
            int[][] temp2;
            temp2 = new int[GamePanel.getLineCount()][GamePanel.getLineCount()];
            String str=null;
            char[] ar=null;
                
            while((str=in.readLine())!=null){
                System.out.println(str);
                ar=str.toCharArray();
                //System.out.println("ar的长度为 "+ar.length);
                for(int j=0; j<ar.length; j++){
                    if(ar[j]!= ' ' ){
                        temp2[count1][count2] =ar[j]-'0';
                        count2++;
                    }   
                }
                count2=0;
                count1++;
            }
                
                
            /*//测试是否正确读入
            for(int i=0; i< GamePanel.getLineCount(); i++){

                for(int j=0; j< GamePanel.getLineCount(); j++){
                    System.out.print(temp2[i][j]);
                }
                System.out.println("");
            }*/
                
            in.close();
                
            gamePanel.loadLastTime(temp2);
 
            JOptionPane.showMessageDialog(null, "读档成功！\n已返回上局棋局");
        }
        catch(IOException e1){
                
            JOptionPane.showMessageDialog(null, "读档失败！");
        }
    }

    public static void main(String[] args) {

        //FiveChess fiveChess = new FiveChess();
       
        
        //3s启动图
        
	Splash splash = new Splash();

	long startTime = System.currentTimeMillis();

	long endTime = startTime + 3000;		

	while(System.currentTimeMillis()<endTime);

	new FiveChess();
        
        splash.dispose();
        
	//fiveChess.setVisible(true);
    }
    
    
}

 



class AboutHelp extends JDialog {// 关于窗口

	AboutHelp(Container container) {

		this.setIconImage(new ImageIcon("img/ic_launcher.png").getImage());

		this.setTitle("帮助");

 

		JTextArea t = new JTextArea();

		t.setFont(new Font("微软雅黑", 1, 16));

		String s = "  五子连珠：\n  操作： 只要能够横向，纵向，正45度，"
                            + "\n  负45度方向连5颗棋子，即为赢。";

		t.setText(s);
                
		t.setEditable(false);

		add(t);

		setSize(300, 120);

		setLocationRelativeTo(container);

		setResizable(false); // 设置为大小不可变的

                setVisible(true);

	}

}



class AboutAuthor extends JDialog {// 关于窗口

	AboutAuthor(Container container) {

		this.setIconImage(new ImageIcon("img/ic_launcher.png").getImage());

		this.setTitle("关于五子棋");

		JTextArea t = new JTextArea();

		t.setFont(new Font("微软雅黑", 1, 16));

		String s = "      作者： 黄静焕  曾宏耀  梁魁焕"+
                           "\n      时间： 2020.2.2"+
                           "\n      版本： 999999";

 

		t.setText(s);

		t.setEditable(false);

		add(t);

		setSize(400, 110);		

		setLocationRelativeTo(container);
                

		setResizable(false); // 设置为大小不可变的
                
                setVisible(true);

	}

}
