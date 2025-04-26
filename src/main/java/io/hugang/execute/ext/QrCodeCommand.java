package io.hugang.execute.ext;

import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import io.hugang.bean.OriginalCommand;
import io.hugang.execute.Command;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * qr code command
 */
public class QrCodeCommand extends Command {
    private static final Log log = Log.get();

    public QrCodeCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    @Override
    public String getCommand() {
        return "qrCode";
    }

    @Override
    public boolean _execute() {

        String target = this.render(this.getTarget());
        String value = this.render(this.getDictStr("value", this.getValue()));
        String size = this.getDictStr("size", "400x400");
        String centerText = this.getDictStr("centerText");
        String footerText = this.getDictStr("footerText");

        int qrWidth = 450;  // 二维码图片宽度
        int qrHeight = 400; // 二维码图片高度

        String[] sizeArray = size.split("x");
        if (sizeArray.length == 2) {
            qrWidth = Integer.parseInt(sizeArray[0]);
            qrHeight = Integer.parseInt(sizeArray[1]);
        }
        int totalHeight = qrHeight + 50; // 最终图片高度（含文字部分）

        try {
            // 配置二维码参数
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.MARGIN, 1); // 边距
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            // 生成二维码图片
            BufferedImage qrCodeImage = generateQRCodeImage(target, qrWidth, qrHeight, hints);
            // 在二维码中间添加文字
            BufferedImage tempImage = null;
            if (StrUtil.isNotEmpty(centerText)) {
                tempImage = addCenterText(qrCodeImage, centerText);
            }
            // 在二维码下方添加文字
            BufferedImage finalImage = null;
            if (StrUtil.isNotEmpty(footerText)) {
                finalImage = addTextBelowImage(tempImage == null ? qrCodeImage : tempImage, target, totalHeight);
            }
            // 保存最终图片
            ImageIO.write(finalImage == null ? (tempImage == null ? qrCodeImage : tempImage) : finalImage, "png", new File(this.getFilePath(value)));
        } catch (WriterException | IOException e) {
            log.error(e);
            return false;
        }
        return true;
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
    private BufferedImage generateQRCodeImage(String text, int width, int height, Map<EncodeHintType, ?> hint) throws WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hint);

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0x000000 : 0xffffff); // 黑白颜色
            }
        }
        return image;
    }

    /**
     * 在二维码中间添加文字
     *
     * @param image      原始二维码图片
     * @param centerText 中间要显示的文字
     * @return 带有中间文字的二维码图片
     */
    private BufferedImage addCenterText(BufferedImage image, String centerText) {
        int width = image.getWidth();
        int height = image.getHeight();

        Graphics2D g2d = image.createGraphics();

        // 设置文字样式
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));

        // 计算文字位置（居中）
        FontMetrics fontMetrics = g2d.getFontMetrics();
        int textWidth = fontMetrics.stringWidth(centerText);
        int textHeight = fontMetrics.getHeight();

        int x = (width - textWidth) / 2; // 水平居中
        int y = (height - textHeight) / 2 + fontMetrics.getAscent(); // 垂直居中

        // 绘制白色背景块（避免文字与二维码背景冲突）
        g2d.setColor(Color.WHITE);
        g2d.fillRect(x - 10, y - fontMetrics.getAscent(), textWidth + 20, textHeight);

        // 绘制文字
        g2d.setColor(Color.BLACK);
        g2d.drawString(centerText, x, y);

        g2d.dispose();
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
    private BufferedImage addTextBelowImage(BufferedImage image, String text, int totalHeight) {
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
        g2d.setFont(new Font("Arial", Font.PLAIN, 16));

        // 计算文字位置并逐字处理换行
        FontMetrics fontMetrics = g2d.getFontMetrics();
        int lineHeight = fontMetrics.getHeight();
        int y = image.getHeight() + lineHeight; // 初始文字位置（第一行）

        StringBuilder line = new StringBuilder();
        for (char c : text.toCharArray()) {
            String testLine = line.toString() + c;
            int textWidth = fontMetrics.stringWidth(testLine);
            if (textWidth > width - 20) {
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
