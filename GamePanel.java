package javaapplication9;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Stack;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;



//游戏的面板
public class GamePanel extends JPanel{ 
    
    private boolean isStart= true; //游戏是否开始，默认开始
    private final int FIRST=1;//先手颜色为常量1（黑色）
    private  int COUNT=0;
    private int playerChessColour =1;//玩家选择的棋子的颜色，默认黑色
    private boolean isComputer=false;//判断是否电脑可以走棋
    private int level = 1;  //游戏难度    
    private Image background;

        //棋盘布局
    private final int[][] chessBoard = new int[15][15]; // 棋子颜色：1为黑，2为白，0表示没有棋子
    private static final int GRID_WIDTH = 40;// 网格宽度，每一个格子的大小为40 
    private static final int LINE_COUNT = 15;// 行数和列数  
    //通过行和列来刻画棋子在棋盘的位置
    
    
    Music music = new Music();
    /**  关于音效的开关  */
    private boolean soundswitch = true;
    
    /** 悔棋栈  */
    private final Stack<Point > stack = new Stack<>();
    
    
    
    
    public GamePanel() {    
        
        background=new ImageIcon("img/bg_1.png").getImage();

        this.setLayout(null);
        this.setFocusable(true);//获得焦点
        this.addMouseListener(new ChessListener());
    }

    @Override
    public void paintComponent(Graphics g) {
        
        super.paintComponent(g);//清屏
        
        g.drawImage( background, 0, 0, 640,640, this);//绘制棋盘背景
            
        //绘制棋盘纵横线条
        for (int i = 1; i < LINE_COUNT; i++) {
            for (int j = 1; j < LINE_COUNT; j++) {
                g.drawRect( GRID_WIDTH * j ,GRID_WIDTH * i, GRID_WIDTH, GRID_WIDTH);
            }
        }
                    
        // 画棋盘上的实心小圆 
        g.fillOval(4*GRID_WIDTH-5,4*GRID_WIDTH-6,10,10); 
        g.fillOval(4*GRID_WIDTH-5,12*GRID_WIDTH-5,10,10);
        g.fillOval(12*GRID_WIDTH-5,4*GRID_WIDTH-5,10,10);
        g.fillOval(12*GRID_WIDTH-5,12*GRID_WIDTH-5,10,10);
        g.fillOval(8*GRID_WIDTH-5,8*GRID_WIDTH-5,10,10);           
            
        // 绘制棋子
        drawChessItem(g);
        
    }
    
    
    private void drawChessItem(Graphics g) {

        for (int i = 0; i < 15; i++) {
            
            for (int j = 0; j < 15; j++) {
                    
                if (chessBoard[i][j]==1){//绘制黑子
                    
                    //g.setColor(Color.BLACK);
                    //g.fillOval( GRID_WIDTH* j+24 ,GRID_WIDTH * i+24, GRID_WIDTH-3, GRID_WIDTH-3 );
                    
                    Image img =new ImageIcon("img/black.png").getImage();
                    g.drawImage(img,GRID_WIDTH* j+22 ,GRID_WIDTH * i+22, GRID_WIDTH -4, GRID_WIDTH -4, this);
                    /**棋子位置，x的坐标=方格边长40 * j - 棋子半径18 + 距离窗口边界的距离40 */  
                }
                    
                else if (chessBoard[i][j]==2){//绘制白子
                    //g.setColor(Color.WHITE);
                    //g.fillOval( GRID_WIDTH* j+26 ,GRID_WIDTH * i+6, GRID_WIDTH /2, GRID_WIDTH /2 );
                    Image img =new ImageIcon("img/white.png").getImage();
                    g.drawImage(img,GRID_WIDTH* j+22 ,GRID_WIDTH * i+22, GRID_WIDTH -4, GRID_WIDTH -4, this);
                }
            }
        }
        
    }

 
    public void reset() {
        
        // 清空棋盘
        for (int[] chessBoard1 : chessBoard) {
            
            for (int j = 0; j < chessBoard1.length; j++) {
                
                chessBoard1[j] = 0;
            }
        }
        
        stack.clear();// 清空棋子栈
        COUNT=0;//当前棋子数量清零
        
        
        //从新开始后，从新判断谁先下
        if(getComputerColour()==FIRST){          
            computerPlay(); 
        }
        else{
            isComputer=false;
        }
        
        setIsStart(true);
        
	repaint();	

    }

  
    public class ChessListener extends MouseAdapter{

         @Override
	public void mousePressed(MouseEvent e) {

			

            int x = e.getX();

            int y = e.getY();
                        
            int row; //行

            int column;//列
                        
                        
            // 确定对应的行和列， 判断该点是否落在棋子所在区域内
            int rowTemp1 = y / GRID_WIDTH ;

            int columnTemp1 = x / GRID_WIDTH ;

            int rowTemp2 = y % GRID_WIDTH ;

            int columnTemp2 = x % GRID_WIDTH ;
                        
            if( rowTemp2 + 18 >= GRID_WIDTH ) {
                //余数+棋子半径>=格子的边长，则该点属于下一条线上
		row = rowTemp1 + 1;
                

            } else {

                row = rowTemp1;

            }	

            if( columnTemp2+18 >= GRID_WIDTH ) {

                column = columnTemp1 + 1;

            } else {

                    column = columnTemp1;

            }

                      
            System.out.println("row = "+(row-1)+"; column = "+(column-1));
            System.out.println("mouse_x = " + x +"mouse_y = "+ y);
                       
                        
            if(!isStart) //游戏是否开始
                return;               
            if(outOfRange(row-1,column-1)) 
                return;
            if(isExist(row-1,column-1))
                return;
                                   
            if(!isComputer){//玩家走棋
               
                if(soundswitch){
                    music.putVoice();
                }
                
                addChess(row-1,column-1,playerChessColour);
                stack.add(new Point(row-1,column-1));
                COUNT++;               
                
                
                repaint();
                
                
                
                //判断胜负
                if(checkWin(row-1, column-1)){
                    
                    setIsStart(false);//胜利之后结束游戏，并弹出胜利提示

                    if(soundswitch){                                
                        music.winVoice();
                    }
                                
                    JOptionPane.showMessageDialog(null, "你赢了");
                    
                    return;
                }
                
                setIsComputer(true);//如果没胜利则轮到电脑下棋 
            }  
             
            /*if(COUNT==225){//棋子满了，电脑不能下
                //JOptionPane.showMessageDialog(null, "平局");
                //也可能是电脑下完最后一个，所以平局也可能在电脑下完之后
                return;
            }*/
            
            if(COUNT<226&&isComputer) {//电脑走棋
                            
                computerPlay();             
            } 
            
            
            
        }
        
    }
    
    private void computerPlay(){
        
        Point point=null;
        
        switch(level){
            
            case 1:
                    point=computerPlay2();
                    break;
            case 2:
                    point=(new Calculation03(getChessBoard())).computerPlay();
                    break;
            case 3:
                    //第3个难度算法
                    point=computerPlay3();
                    break;
        }

        addChess(point.x,point.y,getComputerColour());
        stack.add(new Point(point.x,point.y));
        System.out.println("入栈");
        COUNT++;
        
        repaint();        
        System.out.println("Random_row = "+point.x+"  Random_col= "+point.y+"\n");
        //System.out.println("进入设置computerPlay（）；将isComputer为："+isComputer);
        
        if(checkWin(point.x, point.y)){//失败之后结束游戏，并弹出失败提示
            setIsStart(false);
            if(soundswitch){                                
                 music.lostVoice();
            }                                
            JOptionPane.showMessageDialog(null, "你输了");
        }
                    
        setIsComputer(false);
        
    }
    
    
    //判断该位置是否超出范围
    private boolean outOfRange(int row, int col){
        if(row<0||row>=15)
            return true;
        return (col<0||col>=15);
    }
    
    //判断该位置是否已经有棋子
    private boolean isExist (int row, int col){
        return (chessBoard[row][col]!=0);
    }
       
    private void addChess(int row,int col,int colour){//落子
        
        chessBoard[row][col]=colour;
        
    }

    private void setIsStart(boolean isStart) {
        this.isStart = isStart;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setSoundswitch(boolean soundswitch) {
        this.soundswitch = soundswitch;
    }

    public void setFirstHand(int who) {
        if(who==1)
            this.playerChessColour =FIRST;
        else 
            this.playerChessColour =2;
        
        reset();
    }
   
    
    public void setBackground(String bg_Path) {
       try {
            
            background = ImageIO.read(new File(bg_Path));
            
        } catch (IOException e) {
            
                System.out.println("找不到图片");
        }
       repaint();
    }       

    private void setIsComputer(boolean isComputer) {
        //设置是否到电脑下棋
        this.isComputer = isComputer;
    }

    public void loadLastTime (int[][] array){
        
        COUNT=0;
        stack.clear();
        //读档
        for (int i=0;i< chessBoard.length;i++){
           for (int j=0;j< chessBoard.length;j++){
               chessBoard[i][j]=array[i][j];
               if(array[i][j]!=0){
                   COUNT++;
               }
           }  
        }
        if(COUNT%2==0){
            playerChessColour=1;
        }
        else{//单数说明上局是电脑选了先手(黑棋)先走
            playerChessColour=2;
        }
        isComputer=false;
        setIsStart(true);
	repaint();	
    }

    public int[][] getChessBoard() {
        return chessBoard;
    }    

    public static int getLineCount() {
        return LINE_COUNT;
    }
    
    private int getComputerColour(){
        
        return ( playerChessColour==1 ? 2 :1 );
        
    }
     
    public void regret(){
        
        if(!isStart){
            System.out.println("游戏已经结束不能悔棋");
            return;
        }
        
        if(stack.size()>1){
            
            Point po1=stack.pop();
            COUNT--;
            
            Point po2=stack.pop();
            COUNT--;
            chessBoard[po1.x][po1.y]=0;
            chessBoard[po2.x][po2.y]=0;
            
            System.out.println("po1.x="+po1.x+" po1.y="+po1.y);
            System.out.println("po2.x="+po2.x+" po2.y="+po2.y);
            
            repaint();
        }
        //repaint();
    }
    
    private Point computerPlay1(){//人机对弈函数
        //随机在一个空的地方下棋
        Random r = new Random(System.currentTimeMillis());
        int row  ;
        int col  ;
        do{
            row = r.nextInt(15);
            col = r.nextInt(15);
        }
        while(isExist (row,col));
        
        return (new Point(row,col));
          
    }
    
    private boolean checkWin(int row, int col) {
	int cv = 0;	//垂直方向棋子数量
	int ch = 0;	//水平方向棋子数量
	int ci1 = 0; //左斜方向棋子数量
	int ci2 = 0; //右斜方向棋子数量
                
        //当前走棋者（电脑或者玩家）的棋子颜色
        int currentChess = chessBoard[row][col];
	
	//计算水平方向棋子数量，LINE_COUNT=15（15*15的棋盘）
         for (int j=col; j<LINE_COUNT; j++) {//往右边
            if (chessBoard[row][j]==currentChess) {
                ch++;
            }
            else {
                break;
            }      
        }
        for (int j=col-1; j>=0; j--) {//往左边
            if (chessBoard[row][j]==currentChess) {
                ch++;
            }
            else {
                break;
            }      
        }
        
	//计算垂直方向棋子数量
	for (int i=row-1; i>=0; i--) {//往上边
            if (chessBoard[i][col]==currentChess) {
		cv++;
            }
            else {
                break;
            }      
        }
    	for (int i=row; i<LINE_COUNT; i++) {//往下边
            if (chessBoard[i][col]==currentChess) {
		cv++;
            }
            else {
                break;
            }      
        }    
        
		
	//计算左斜线方向棋子数量              
        for (int i=row,j=col ; i>=0 && j<LINE_COUNT ; i--,j++) {
            //往右上对角线遍历
            if (chessBoard[i][j]==currentChess) {
		ci1++;
            }
            else break;
        }
        for (int i=row+1,j=col-1 ; j>=0 && i<LINE_COUNT ;i++,j--){
            //往左下对角线遍历
            if (chessBoard[i][j]==currentChess) {
		ci1++;
            } 
            else break;
        }
               

	//计算右斜线方向棋子数量
	for (int i=row,j=col ; i>=0 && j>=0 ; i--,j--){
             //往左上对角线遍历
            if (chessBoard[i][j]==currentChess) {
		ci2++;
            }
            else break;
        }
        for (int i=row+1,j=col+1;j<15 && i<15; i++,j++){
            //往右下对角线遍历
            if (chessBoard[i][j]==currentChess) {
                
                ci2++;
            } 
            else break;
        }
		
        return ch>=5 || cv>=5 ||ci1>=5 ||ci2>=5;         
    }

    private Point computerPlay3(){
        
        int[][] score=new int[LINE_COUNT][LINE_COUNT];
        //每次都初始化下score评分数组
        for(int i = 0; i  < LINE_COUNT; i++){
            for(int j = 0; j < LINE_COUNT; j++){
                score[i][j] = 0;
            }
        }
		
        //每次机器找寻落子位置，评分都重新算一遍（虽然算了很多多余的，因为上次落子时候算的大多都没变）
        //先定义一些变量
        int humanChessmanNum = 0;//五元组中的人类落子数量
        int machineChessmanNum = 0;//五元组中的电脑落子数量
        int tupleScoreTmp = 0;//五元组得分临时变量
		
        int goalX = -1;//目标位置x坐标
        int goalY = -1;//目标位置y坐标
        int maxScore = -1;//最大分数

        //1.扫描横向的15个行
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 11; j++){
                int k = j;
                while(k < j + 5){
					
                    if(chessBoard[i][k] == getComputerColour())    
                        machineChessmanNum++;
                    else if(chessBoard[i][k] ==playerChessColour)
                        humanChessmanNum++;
				
                    k++;
                }
                tupleScoreTmp = tupleScore(humanChessmanNum, machineChessmanNum);
                //为该五元组的每个位置添加分数
                for(k = j; k < j + 5; k++){
                    score[i][k] += tupleScoreTmp;
                }
                //置零
                humanChessmanNum = 0;
                machineChessmanNum = 0;
                tupleScoreTmp = 0;
            }
        }
		
	//2.扫描纵向15行
	for(int i = 0; i < 15; i++){
            for(int j = 0; j < 11; j++){
                int k = j;
		while(k < j + 5){
                    if(chessBoard[k][i] == getComputerColour())    
                        machineChessmanNum++;
                    else if(chessBoard[k][i] ==playerChessColour)
                        humanChessmanNum++;
				
                    k++;
		}
		tupleScoreTmp = tupleScore(humanChessmanNum, machineChessmanNum);
		//为该五元组的每个位置添加分数
		for(k = j; k < j + 5; k++){
                    score[k][i] += tupleScoreTmp;
		}
		//置零
		humanChessmanNum = 0;
		machineChessmanNum = 0;
		tupleScoreTmp = 0;
            }
	}

	//3.扫描右上角到左下角上侧部分
	for(int i = 14; i >= 4; i--){
            for(int k = i, j = 0; j < 15 && k >= 0; j++, k--){
		int m = k;
		int n = j;
		while(m > k - 5 && k - 5 >= -1){
                    if(chessBoard[m][n] == getComputerColour())    
                        machineChessmanNum++;
                    else if(chessBoard[m][n] ==playerChessColour)
                        humanChessmanNum++;
					
                    m--;
                    n++;
		}
		//注意斜向判断的时候，可能构不成五元组（靠近四个角落），遇到这种情况要忽略掉
		if(m == k-5){
                    tupleScoreTmp = tupleScore(humanChessmanNum, machineChessmanNum);
                    //为该五元组的每个位置添加分数
                    for(m = k, n = j; m > k - 5 ; m--, n++){
			score[m][n] += tupleScoreTmp;
                    }
		}

		//置零
		humanChessmanNum = 0;
		machineChessmanNum = 0;
		tupleScoreTmp = 0;

            }
        }
		
	//4.扫描右上角到左下角下侧部分
	for(int i = 1; i < 15; i++){
            for(int k = i, j = 14; j >= 0 && k < 15; j--, k++){
		int m = k;
		int n = j;
		while(m < k + 5 && k + 5 <= 15){
                    if(chessBoard[n][m] == getComputerColour())    
                        machineChessmanNum++;
                    else if(chessBoard[n][m] ==playerChessColour)
                        humanChessmanNum++;
					
                    m++;
                    n--;
                }
		//注意斜向判断的时候，可能构不成五元组（靠近四个角落），遇到这种情况要忽略掉
		if(m == k+5){
                    tupleScoreTmp = tupleScore(humanChessmanNum, machineChessmanNum);
                    //为该五元组的每个位置添加分数
                    for(m = k, n = j; m < k + 5; m++, n--){
                        score[n][m] += tupleScoreTmp;
                    }
                }
		//置零
		humanChessmanNum = 0;
		machineChessmanNum = 0;
		tupleScoreTmp = 0;

            }
        }

	//5.扫描左上角到右下角上侧部分
	for(int i = 0; i < 11; i++){
            for(int k = i, j = 0; j < 15 && k < 15; j++, k++){
                int m = k;
		int n = j;
		while(m < k + 5 && k + 5 <= 15){
                    if(chessBoard[m][n] == getComputerColour())    
                        machineChessmanNum++;
                    else if(chessBoard[m][n] ==playerChessColour)
                        humanChessmanNum++;
					
                    m++;
                    n++;
                }
                //注意斜向判断的时候，可能构不成五元组（靠近四个角落），遇到这种情况要忽略掉
		if(m == k + 5){
                    tupleScoreTmp = tupleScore(humanChessmanNum, machineChessmanNum);
                    //为该五元组的每个位置添加分数
                    for(m = k, n = j; m < k + 5; m++, n++){
                        score[m][n] += tupleScoreTmp;
                    }
                }

		//置零
		humanChessmanNum = 0;
		machineChessmanNum = 0;
		tupleScoreTmp = 0;

            }
        }	
	
        //6.扫描左上角到右下角下侧部分
	for(int i = 1; i < 11; i++){
            for(int k = i, j = 0; j < 15 && k < 15; j++, k++){
		int m = k;
		int n = j;
		while(m < k + 5 && k + 5 <= 15){
                    if(chessBoard[n][m] == getComputerColour())    
                        machineChessmanNum++;
                    else if(chessBoard[n][m] ==playerChessColour)
                        humanChessmanNum++;
					
                    m++;
                    n++;
		}
		//注意斜向判断的时候，可能构不成五元组（靠近四个角落），遇到这种情况要忽略掉
		if(m == k + 5){
			
                    tupleScoreTmp = tupleScore(humanChessmanNum, machineChessmanNum);
                    //为该五元组的每个位置添加分数
                    for(m = k, n = j; m < k + 5; m++, n++){
			score[n][m] += tupleScoreTmp;
                    }
		}

		//置零
		humanChessmanNum = 0;
		machineChessmanNum = 0;
		tupleScoreTmp = 0;

            }
        }	
	
	//从空位置中找到得分最大的位置
	for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                if(chessBoard[i][j] == 0 && score[i][j] > maxScore){
                    goalX = i;
                    goalY = j;
                    maxScore = score[i][j];
                }
            }
	}	


	if(goalX != -1 && goalY != -1){
            
           return new Point(goalX, goalY);
            
	}
	//没找到坐标说明平局了，笔者不处理平局
        return new Point(-1, -1);
    }
	
        //各种五元组情况评分表
    private int tupleScore(int humanChessmanNum, int machineChessmanNum){
		//1.既有人类落子，又有机器落子，判分为0
		if(humanChessmanNum > 0 && machineChessmanNum > 0){
			return 0;
		}
		//2.全部为空，没有落子，判分为7
		if(humanChessmanNum == 0 && machineChessmanNum == 0){
			return 7;
		}
                
		//3.机器落1子，判分为35
		if(machineChessmanNum == 1){
			return 35;
		}
		//4.机器落2子，判分为800
		if(machineChessmanNum == 2){
			return 800;
		}
		//5.机器落3子，判分为15000
		if(machineChessmanNum == 3){
			return 15000;
		}
		//6.机器落4子，判分为800000
		if(machineChessmanNum == 4){
			return 800000;
		}
		//7.人类落1子，判分为15
		if(humanChessmanNum == 1){
			return 15;
		}
		//8.人类落2子，判分为400
		if(humanChessmanNum == 2){
			return 400;
		}
		//9.人类落3子，判分为1800
		if(humanChessmanNum == 3){
			return 1800;
		}
		//10.人类落4子，判分为100000
		if(humanChessmanNum == 4){
			return 100000;
		}
		return -1;//若是其他结果肯定出错了。这行代码根本不可能执行
	}

    private Point computerPlay2(){//人机对弈主函数
        int max_black,max_white,max_temp,max=-1;
        int goalX = -1;
        int goalY = -1;
        
        for(int i= 0; i<LINE_COUNT;i++){//循环遍历整个棋盘
            for(int j= 0;j < LINE_COUNT; j++){
                if(!isExist(i,j)){//算法判断是否下子
                    max_white=checkMax(i,j,2);//判断该位置白子的最大值
                    max_black=checkMax(i,j,1);//判断该位置黑子的最大值
                    max_temp=Math.max(max_white,max_black);
                    //交换最大值，保存历次遍历中最大值最优的位置
                    if(max_temp>max){
                        max=max_temp;
                        goalX =i;
                        goalY =j;
                    }
                }
            }
        }
        return new Point(goalX, goalY); 
    }
        
        
    //计算棋盘上某一方格上八个方向棋子的最大值，
    //这八个方向分别是:左、右、上、下、左上、左下、右上、右下
    public int checkMax(int x, int y,int colour){
        int num=0,max_num,max_temp=0;
        //计数下侧同色无间隔棋子
        for(int i=x+1;i<x+5;i++){
            //最多循环4次，因为有5个的话游戏已经结束了
            if(i>=LINE_COUNT)
                break;
            if(this.chessBoard[i][y]==colour)
                num++;
            else
                break;
        }
        //计数上侧同色无间隔棋子
        for(int i=x-1;i<x-5;i--){
            if(i<0)
                break;
            if(this.chessBoard[i][y]==colour)
                num++;
            else
                break;
        }
        
        if(num<5)
            max_temp=num;
        
        num=0;//清零
        
        
        //计数右侧同色无间隔棋子
        for(int j=y+1;j<y+5;j++){
            if(j>=LINE_COUNT)
                break;
            if(this.chessBoard[x][j]==colour)
                num++;
            else
                break;
        }
        //计数左侧同色无间隔棋子
        for(int j=y-1;j<y-5;j--){
            if(j<0)
                break;
            if(this.chessBoard[x][j]==colour)
                num++;
            else
                break;
        }
        
        if(num>max_temp&&num<5)
            max_temp=num;
        
        num=0;//清零
        
        
        //计数右上同色无间隔棋子
        int count=1;//循环次数
        for(int i=x-1,j=y+1;count<5;i--,j++){
            if(i<0||j>=LINE_COUNT)
                break;
            if(chessBoard[i][j]==colour)
                num++;
            else
                break;
            
            count++;
        }
        //计数左下同色无间隔棋子
        count=1;
        for(int i=x+1,j=y-1;count<5;i++,j--){
            if(j<0||i>=LINE_COUNT)
                break;
            if(chessBoard[i][j]==colour)
                num++;
            else
                break;
            count++;
        }
        if(num>max_temp&&num<5)
            max_temp=num;
        
        num=0;//清零
        
        //计数左上同色无间隔棋子
        count=1;
        for(int i=x-1,j=y-1;count<5;i--,j--){
            if(i<0||j<0)
                break;
            if(chessBoard[i][j]==colour)
                num++;
            else
                break;
            
            count++;
        }
        //计数右下同色无间隔棋子
        count=1;
        for(int i=x+1,j=y+1;count<5;i++,j++){
            if(i>=LINE_COUNT||j>=LINE_COUNT)
                break;
            if(chessBoard[i][j]==colour)
                num++;
            else
                break;
            
            count++;
        }
        
        if(num>max_temp&&num<5)//返回各个方向.上最大的同色无间隔棋子数
            max_temp=num;
        max_num=max_temp;
        
        return max_num;
    }
}    
    
    




  
