package com.example.transport.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

public class graphicsutils {

    public static BufferedImage graphicsGeneration(List<List<List<String>>> allValue,List<String> titles,List<String[]> headers ,String receiver,int totalcol,String bigtitle) throws Exception {
        int rows = 0;
        for (List<List<String>> typeV : allValue) {
            if (typeV != null && typeV.size() > 0) {
                rows += (2+typeV.size());
            }
        }
        // 实际数据行数+标题+备注
        int totalrow = 2+rows;
//        int totalrow = rows;
        int imageWidth = 800;
        int imageHeight = totalrow * 30 + 20;
        int rowheight = 30;
        int startHeight = 10;
        int startWidth = 10;
        int colwidth = ((imageWidth - 20) / totalcol);

        BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = image.getGraphics();

        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, imageWidth, imageHeight);
        //画背景
        graphics.setColor(new Color(0, 112, 192));
        int startH = 2;
        for (List<List<String>> typeV : allValue) {
            if (typeV != null && typeV.size() > 0) {
                graphics.fillRect(startWidth+1, startHeight+startH*rowheight+1, imageWidth - startWidth-5-1,rowheight-1);
                if(typeV.size() == 3){
                    startH+=2;
                    graphics.fillRect(startWidth+1, startHeight+startH*rowheight+1, imageWidth - startWidth-5-1,rowheight-1);
                }
                startH+=2+typeV.size();
            }
        }


        graphics.setColor(new Color(220, 240, 240));
        // 画横线
        for (int j = 1; j < totalrow - 1; j++) {
            graphics.setColor(Color.black);
            graphics.drawLine(startWidth, startHeight + (j + 1) * rowheight, imageWidth - 5,
                    startHeight + (j + 1) * rowheight);
        }

        // 画竖线
        graphics.setColor(Color.black);
        startH = 2;
        int rightLine = 0 ;
        int j = 0;
        for (List<List<String>> typeV : allValue) {
            if(j<2){
                if (typeV != null && typeV.size() > 0) {
                    for (int k = 0; k < totalcol+1; k++) {
                        rightLine = getRightMargin(k,startWidth, colwidth,imageWidth);
                        graphics.drawLine(rightLine, startHeight + startH*rowheight, rightLine,
                                startHeight + (typeV.size()+1+startH)*rowheight);
                    }
                    startH+=2+typeV.size();
                }
            }else{
                if (typeV != null && typeV.size() > 0) {
                    for (int k = 0; k < totalcol+2; k++) {
                        rightLine = getTable3RightMargin(k,startWidth, colwidth,imageWidth);
                        graphics.drawLine(rightLine, startHeight + startH*rowheight, rightLine,
                                startHeight + (typeV.size()+1+startH)*rowheight);
                    }
                    startH+=2+typeV.size();
                }
            }
            j++;
        }

        //设置大标题
        Font font = new Font("华文楷体", Font.BOLD, 22);
        graphics.setFont(font);
        graphics.drawString(bigtitle, startWidth+1*colwidth+110, startHeight + 1*rowheight - 10);

        // 设置字体
        font = new Font("华文楷体", Font.BOLD, 16);
        graphics.setFont(font);

        // 写标题
        startH = 2;
        int i = 0;
        j = 0;
        for (List<List<String>> typeV : allValue) {
            if(j<3){
                if (typeV != null && typeV.size() > 0) {
                    graphics.drawString(titles.get(i), startWidth+10, startHeight + startH*rowheight - 10);
                    if(j<2){
                        startH+=2+typeV.size();
                    }else{
                        startH+=1+typeV.size();
                    }
                }
                i++;
            }
            j++;
        }


        // 写入表头
        graphics.setColor(Color.WHITE);
        font = new Font("华文楷体", Font.BOLD, 16);
        graphics.setFont(font);
        startH = 3;
        i = 0;
        j = 0;
        for (List<List<String>> typeV : allValue) {
            if(j<2){
                if (typeV != null && typeV.size() > 0) {

                    String[] headCells = headers.get(i);
                    for (int m = 0; m < headCells.length; m++) {
                        rightLine = getRightMargin(m,startWidth, colwidth,imageWidth)+5;
                        graphics.drawString(headCells[m].toString(), rightLine,
                                startHeight + rowheight * startH - 10);
                    }
                    startH+=2+typeV.size();
                }
            }else{
                if (typeV != null && typeV.size() > 0) {
                    String[] headCells = headers.get(i);
                    for (int m = 0; m < headCells.length; m++) {
                        rightLine = getTable3RightMargin(m,startWidth, colwidth,imageWidth)+5;
                        graphics.drawString(headCells[m].toString(), rightLine,
                                startHeight + rowheight * startH - 10);
                    }
                    startH+=2+typeV.size();
                }
            }
            i++;
            j++;
        }


        // 写入内容
        graphics.setColor(Color.black);
        font = new Font("华文楷体", Font.PLAIN, 14);
        graphics.setFont(font);
        startH = 4;
        i = 0;
        j = 0;
        for (List<List<String>> typeV : allValue) {
            if(j<2){
                if (typeV != null && typeV.size() > 0) {
                    for (int n = 0; n < typeV.size(); n++) {
                        List<String> arr = typeV.get(n);
                        for (int l = 0; l < arr.size(); l++) {
                            rightLine = getRightMargin(l,startWidth, colwidth,imageWidth)+5;
                            graphics.drawString(arr.get(l).toString(), rightLine,
                                    startHeight + rowheight * (n + startH) - 10);
                        }
                    }
                    startH+=2+typeV.size();
                }
            }else{
                if (typeV != null && typeV.size() > 0) {
                    for (int n = 0; n < typeV.size(); n++) {
                        List<String> arr = typeV.get(n);
                        for (int l = 0; l < arr.size(); l++) {
                            rightLine = getTable3RightMargin(l,startWidth, colwidth,imageWidth)+5;
                            graphics.drawString(arr.get(l).toString(), rightLine,
                                    startHeight + rowheight * (n + startH) - 10);
                        }
                    }
                    startH+=2+typeV.size();
                }
            }
            i++;
            j++;
        }

//        String path = "1.png";
//        ImageIO.write(image, "png", new File(path));

//        return path;

        return image;
    }

    public static String createImg(List<List<List<String>>> allValue,List<String> titles,List<String[]> headers ,String receiver,int totalcol,String bigtitle) throws Exception{
        BufferedImage image = graphicsGeneration(allValue,titles,headers,receiver,totalcol,bigtitle);
        String path = "1.png";
        ImageIO.write(image, "png", new File(path));
        return path;
    }

    /**
     * 获取竖线和文字的水平位置
     * @param k
     * @param startWidth
     * @param colwidth
     * @param imageWidth
     * @return
     */
    private static int getRightMargin(int k, int startWidth, int colwidth, int imageWidth) {
        int rightLine = 0;
        if (k == 0) {
            rightLine = startWidth;
        } else if (k == 1) {
            rightLine = startWidth + colwidth / 2 - 20;
        } else if (k == 2) {
            rightLine = startWidth + 3 * colwidth / 2 - 60;
        } else if (k == 3) {
            rightLine = startWidth + 5 * colwidth / 2 - 50;
        } else if (k == 4) {
            rightLine = imageWidth - 5;
        }
        return rightLine;
    }

    /**
     * 获取竖线和文字的水平位置
     * @param k
     * @param startWidth
     * @param colwidth
     * @param imageWidth
     * @return
     */
    private static int getTable3RightMargin(int k, int startWidth, int colwidth, int imageWidth) {
        int rightLine = 0;
        if (k == 0) {
            rightLine = startWidth;
        } else if (k == 1) {
            rightLine = startWidth + colwidth / 2 + 50;
        } else if (k == 2) {
            rightLine = startWidth + 2 * colwidth / 2 + 55;
        } else if (k == 3) {
            rightLine = startWidth + 4 * colwidth / 2;
        } else if (k == 4) {
            rightLine = startWidth + 5 * colwidth / 2 + 100;
        }else if(k == 5){
            rightLine = imageWidth - 5;
        }
        return rightLine;
    }
}

