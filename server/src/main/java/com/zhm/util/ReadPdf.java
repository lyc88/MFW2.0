package com.zhm.util;
import java.io.File;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 读取pdf文件内容
 */
public class ReadPdf {
    private static Logger logger= LoggerFactory.getLogger(ReadPdf.class);


    public static String ReadPdfInfo(String pathUrl){
        try{
            StringBuffer sb=new StringBuffer();
            PDDocument document = PDDocument.load(new File(pathUrl));
            document.getClass();
            if(!document.isEncrypted()) {
                PDFTextStripperByArea stripper = new PDFTextStripperByArea();
                stripper.setSortByPosition(true);
                PDFTextStripper tStripper = new PDFTextStripper();
                String pdfFileInText = tStripper.getText(document);

                String[] lines = pdfFileInText.split("\\r?\\n");
                for(String line : lines) {
                    logger.info(line);
                    sb.append(line).append("\n");
                }
            }
            return sb.toString();
        } catch (Exception e) {
            logger.error("读取pdf文件异常,{}",e);
            return null;
        }
    }



    public static void main(String[] args) {

        String info=ReadPdfInfo("C:/photo/3.pdf");
        System.out.println("读取pdf数据是："+info);
    }
}