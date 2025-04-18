package io.hugang;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 生成二维码并在下方添加文字 TODO experiment
 */
public class QRCodeWithText {
    public static void main(String[] args) {
        String inputText = "https://github.com/copilot/c/852fda11-b2be-4feb-9782-12f26900ae83"; // 输入的文字
        int qrWidth = 400;  // 二维码图片宽度
        int qrHeight = 400; // 二维码图片高度
        int totalHeight = 500; // 最终图片高度（含文字部分）

        try {
            // 生成二维码图片
            BufferedImage qrCodeImage = generateQRCodeImage(inputText, qrWidth, qrHeight);

            // 在二维码下方添加文字
            BufferedImage finalImage = addTextBelowImage(qrCodeImage, inputText, totalHeight);

            // 保存最终图片
            ImageIO.write(finalImage, "png", new File("qrcode_with_text.png"));
            System.out.println("二维码图片已生成，并保存为 qrcode_with_text.png");
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成二维码图片
     *
     * @param text   要编码的文字
     * @param width  二维码宽度
     * @param height 二维码高度
     * @return 生成的二维码图片
     * @throws WriterException 如果生成二维码失败
     */
    private static BufferedImage generateQRCodeImage(String text, int width, int height) throws WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0x000000 : 0xffe5b0); // 黑白颜色
            }
        }
        return image;
    }

    /**
     * 在图片下方添加文字
     *
     * @param image       原始图片
     * @param text        要添加的文字
     * @param totalHeight 最终图片高度
     * @return 带有文字的图片
     */
    private static BufferedImage addTextBelowImage(BufferedImage image, String text, int totalHeight) {
        int width = image.getWidth();
        BufferedImage newImage = new BufferedImage(width, totalHeight, BufferedImage.TYPE_INT_RGB);

        Graphics2D g2d = newImage.createGraphics();

        // 绘制白色背景
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, totalHeight);

        // 绘制原始图片
        g2d.drawImage(image, 0, 0, null);

        // 设置文字样式
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.PLAIN, 24));

        // 计算文字位置并逐字处理换行
        FontMetrics fontMetrics = g2d.getFontMetrics();
        int lineHeight = fontMetrics.getHeight();
        int y = image.getHeight() + lineHeight; // 初始文字位置（第一行）

        StringBuilder line = new StringBuilder();
        for (char c : text.toCharArray()) {
            String testLine = line.toString() + c;
            int textWidth = fontMetrics.stringWidth(testLine);
            if (textWidth > width-20) {
                // 绘制当前行并换行
                int x = (width - fontMetrics.stringWidth(line.toString())) / 2; // 水平居中
                g2d.drawString(line.toString(), x, y);
                line = new StringBuilder(String.valueOf(c));
                y += lineHeight; // 移动到下一行
            } else {
                line.append(c);
            }
        }
        // 绘制最后一行
        if (!line.isEmpty()) {
            int x = (width - fontMetrics.stringWidth(line.toString())) / 2; // 水平居中
            g2d.drawString(line.toString(), x, y);
        }

        g2d.dispose();
        return newImage;
    }
}
