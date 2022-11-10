package org.luck.xhga.common.util;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import lombok.SneakyThrows;

/**
 * @author hwb
 * @date 2022/11/9
 */
public class PdfEditUtil {
    /**
     *
     * @param inputStream  pdf文件输入流
     * @param dataParamMap 文本域参数
     * @param imgParamMap  图片域参数
     * @param userPass    打开pdf密码
     * @param ownerPass   编辑pdf密码
     * @param outputStream pdf文件输出流
     * @return
     */
    public static boolean editPdf(InputStream inputStream, Map<String, String> dataParamMap, Map<String, Image> imgParamMap, String userPass, String ownerPass, OutputStream outputStream) {
        try {
            BaseFont baseFont = BaseFont.createFont();
            PdfReader pdfReader = new PdfReader(inputStream);
            PdfStamper pdfStamper = new PdfStamper(pdfReader, outputStream);
            pdfStamper.setEncryption(true, userPass, ownerPass, PdfWriter.ALLOW_PRINTING);
            AcroFields form = pdfStamper.getAcroFields();
            form.addSubstitutionFont(baseFont);
            // 写入数据
            if (dataParamMap != null) {
                for (String key : dataParamMap.keySet()) {
                    String value = dataParamMap.get(key);
                    //key对应模板数据域的名称
                    form.setField(key, value);
                }
            }
            if (imgParamMap != null) {
                for (Map.Entry<String, Image> entry:imgParamMap.entrySet()) {
                    String key = entry.getKey();
                    Image value = entry.getValue();
                    List<AcroFields.FieldPosition> fieldPositions = form.getFieldPositions(key);
                    if (fieldPositions != null) {
                        for (int i = 0; i < fieldPositions.size(); i++) {
                            //添加图片
                            int pageNo = form.getFieldPositions(key).get(i).page;
                            Rectangle signRect = form.getFieldPositions(key).get(i).position;
                            float x = signRect.getLeft();
                            float y = signRect.getBottom();
                            PdfContentByte under = pdfStamper.getOverContent(pageNo);
                            //设置图片大小
                            value.scaleAbsolute(signRect.getWidth(), signRect.getHeight());
                            //设置图片位置
                            value.setAbsolutePosition(x, y);
                            under.addImage(value);
                        }
                    }
                }
            }
            pdfStamper.setFormFlattening(true);
            if (pdfStamper != null) {
                pdfStamper.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public static byte[] editPdf(InputStream inputStream, Map<String, String> dataParamMap, Map<String, Image> imgParamMap, String userPass, String ownerPass) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfEditUtil.editPdf(inputStream, dataParamMap, imgParamMap, userPass, ownerPass, outputStream);
        return outputStream.toByteArray();
    }

    @SneakyThrows
    public static void main(String[] args) {
        Map<String, String> dataParamMap = new HashMap<>();
        dataParamMap.put("username", "test");
        ClassLoader classLoader = PdfUtilTest.class.getClassLoader();
        URL resource = classLoader.getResource("templates");
        String templateDirectory = resource.toURI().getPath() + "\\pdf_template.pdf";
        FileInputStream inputStream = new FileInputStream(templateDirectory);
        FileOutputStream outputStream = new FileOutputStream("L:\\edit.pdf");
        PdfEditUtil.editPdf(inputStream, dataParamMap, null, "", "", outputStream);
    }
}
