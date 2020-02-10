
package javaapplication9;

import java.awt.Point;


public class Calculation03 {

    private static final int LINE_COUNT =15; 

    private static final int CHESS_BLACK =1;

    private static final int CHESS_WHITE= 2;

    private final int[][] items; 

    private final int[] scores = {100, 90, 80, 80, 80, 70, 60, 60, 60, 60, 50, 30, 5};

    private final String[] styleBlack = { "11111", "011110", "11110", "01111", "01110",
                                "1110", "0111", "1101", "1011", "0110", "11", "1" };

    private final String[] styleWhite = { "22222", "022220", "22220", "02222", "02220",
                                "2220", "0222", "2202", "2022", "0220", "22", "2" };

    int[] res = new int[2];

	

    public Calculation03(int[][] items) {

	this.items = items;

    }

    
    public Point computerPlay() {

	// 遍历棋盘上所有的空位，计算每个位置的进攻指数和防守指数，选择指数最大的位置下棋
        int rowTemp = -1, colTemp = -1;

        int maxValue = 0;

        for (int i = 0; i < LINE_COUNT; i++) {

            for (int j = 0; j < LINE_COUNT; j++) {

                if (items[i][j] > 0) {
                    
                    continue;// 非空位，跳过
                }
                int maxAttck = checkMax(i, j, CHESS_BLACK);// 进攻指数

                int maxDedend = checkMax(i, j,  CHESS_WHITE);// 防守指数

                int maxCurrentPosition = Math.max(maxAttck, maxDedend);

		// 如果当前位置的指数大于其他位置的最大指数，则重新设置应该落子的位置和指数
                if (maxCurrentPosition > maxValue) {

                    maxValue = maxCurrentPosition;

                    rowTemp = i;

                    colTemp = j;

                }

                if (maxValue == 100) {

                    break;

                }

            }

            if (maxValue == 100) {

                break;

            }

        }

        return new Point(rowTemp,colTemp);

    }

 
    
    
    private int checkMax(int row, int col, int colour) {

        String[] keys = (colour == CHESS_BLACK ? styleBlack : styleWhite);

        StringBuffer[] buffer = new StringBuffer[4];

		// 注意：这里创建引用类型变量要再new一次
        buffer[0] = new StringBuffer();

        buffer[1] = new StringBuffer();

        buffer[2] = new StringBuffer();

        buffer[3] = new StringBuffer();

        int maxAll = 0, max = 0;

		// 判断-45度方向
        for (int i = -4; i <= 4; i++) {

            int rowTemp = row + i;

            int colTemp = col + i;

            if (rowTemp < 0 || rowTemp >= LINE_COUNT) {

                continue;

            }

            if (colTemp < 0 || colTemp >= LINE_COUNT) {

                continue;

            }

            buffer[0].append(i == 0 ? colour : items[rowTemp][colTemp]);

        }

        for (int i = 0; i < keys.length; i++) {

            String str = keys[i];

            if (buffer[0].indexOf(str) >= 0) {
                //buffer[0].indexOf("xxx")在字符串buffer[0]查找
                //子字符串xxx，找不到返回-1.
                max = scores[i];

                break;

            }

        }

        if (max == 100) {

            return max;

        }

        if (max > maxAll) {

            maxAll = max;

        }

		// 判断45度方向
        for (int i = -4; i <= 4; i++) {

            int rowTemp = row - i;

            int colTemp = col + i;

            if (rowTemp < 0 || rowTemp >= LINE_COUNT) {

                continue;

            }

            if (colTemp < 0 || colTemp >= LINE_COUNT) {

                continue;

            }

            buffer[1].append(i == 0 ? colour : items[rowTemp][colTemp]);

        }

        for (int i = 0; i < keys.length; i++) {

            String str = keys[i];

            if (buffer[1].indexOf(str) >= 0) {

                max = scores[i];

                break;

            }

        }

        if (max == 100) {

            return max;

        }

        if (max > maxAll) {

            maxAll = max;

        }

		// 判断水平方向
        for (int i = -4; i <= 4; i++) {

            int rowTemp = row;

            int colTemp = col + i;

            if (rowTemp < 0 || rowTemp >= LINE_COUNT) {

                continue;

            }

            if (colTemp < 0 || colTemp >= LINE_COUNT) {

                continue;

            }

            buffer[2].append(i == 0 ? colour : items[rowTemp][colTemp]);

        }

        for (int i = 0; i < keys.length; i++) {

            String str = keys[i];

            if (buffer[2].indexOf(str) >= 0) {

                max = scores[i];

                break;

            }

        }

        if (max == 100) {

            return max;

        }

        if (max > maxAll) {

            maxAll = max;

        }

		// 判断竖直方向
        for (int i = -4; i <= 4; i++) {

            int rowTemp = row + i;

            int colTemp = col;

            if (rowTemp < 0 || rowTemp >= LINE_COUNT) {

                continue;

            }

            if (colTemp < 0 || colTemp >= LINE_COUNT) {

                continue;

            }

            buffer[3].append(i == 0 ? colour : items[rowTemp][colTemp]);

        }

        for (int i = 0; i < keys.length; i++) {

            String str = keys[i];

            if (buffer[3].indexOf(str) >= 0) {

                max = scores[i];

                break;

            }

        }

        if (max > maxAll) {

            maxAll = max;

        }

        return maxAll;

    }

}

