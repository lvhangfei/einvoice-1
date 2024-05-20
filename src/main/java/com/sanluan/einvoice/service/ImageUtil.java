package com.sanluan.einvoice.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * desc
 *
 * @author lvhangfei
 * @create 2023/7/12
 */
public class ImageUtil {
    public static void imageToPdf(File imageFile, File outFile) throws IOException {
        BufferedImage image = ImageIO.read(imageFile);

        PDDocument outDocument = new PDDocument();

        PDImageXObject imageXObject = LosslessFactory.createFromImage(outDocument, image);
        PDPage pdPage = new PDPage(PDRectangle.A4);
        outDocument.addPage(pdPage);
        PDPageContentStream pageContentStream = new PDPageContentStream(outDocument, pdPage);
        float height = pdPage.getMediaBox().getHeight();//要将图片在pdf中绘制多高，这里宽度直接使用了pdfpage的宽度，即横向铺满，这里的height也是使用了pdfpage的高度。因此最终结果是铺满整个pdf页。
        float y = pdPage.getMediaBox().getHeight() - height;//这里比较奇葩的是这个坐标是以左下角为原点坐标的。
        pageContentStream.drawImage(imageXObject, 0, y, pdPage.getMediaBox().getWidth(), height);
        pageContentStream.close();

        if (!outFile.getParentFile().exists()) {
            outFile.getParentFile().mkdirs();
        }

        outDocument.save(outFile);
        outDocument.close();
    }
}
