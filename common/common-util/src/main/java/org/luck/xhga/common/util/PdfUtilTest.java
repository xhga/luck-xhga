package org.luck.xhga.common.util;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.layout.font.FontProvider;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class PdfUtilTest {
    /**
     * 获取模板内容
     * @param templateDirectory 模板文件夹
     * @param templateName      模板文件名
     * @param paramMap          模板参数
     * @return
     * @throws Exception
     */
    private static String getTemplateContent(String templateDirectory, String templateName, Map<String, Object> paramMap) throws Exception {
        Configuration configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        try {
            configuration.setDirectoryForTemplateLoading(new File(templateDirectory));
        } catch (Exception e) {
            System.out.println("-- exception --");
        }

        Writer out = new StringWriter();
        Template template = configuration.getTemplate(templateName,"UTF-8");
        template.process(paramMap, out);
        out.flush();
        out.close();
        return out.toString();
    }
    /**
     * HTML 转 PDF
     * @param content html内容
     * @param outPath           输出pdf路径
     * @return 是否创建成功
     */
    public static boolean html2Pdf(String content, String outPath) {
        try {
            ConverterProperties converterProperties = new ConverterProperties();
            converterProperties.setCharset("UTF-8");
            FontProvider fontProvider = new FontProvider();
            fontProvider.addSystemFonts();
            converterProperties.setFontProvider(fontProvider);
            HtmlConverter.convertToPdf(content, new FileOutputStream(outPath), converterProperties);
        } catch (Exception e) {
            log.error("生成模板内容失败,{}",e);
            return false;
        }
        return true;
    }
    /**
     * HTML 转 PDF
     * @param content html内容
     * @return PDF字节数组
     */
    public static byte[] html2Pdf(String content) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();;
        try {
            ConverterProperties converterProperties = new ConverterProperties();
            converterProperties.setCharset("UTF-8");
            FontProvider fontProvider = new FontProvider();
            fontProvider.addSystemFonts();
            converterProperties.setFontProvider(fontProvider);
            HtmlConverter.convertToPdf(content,outputStream,converterProperties);
        } catch (Exception e) {
            log.error("生成 PDF 失败,{}",e);
        }
        return outputStream.toByteArray();
    }
    public static void main(String[] args) throws Exception {
        Map<String, Object> paramMap = new HashMap<>();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        paramMap.put("date_time", dateTimeFormatter.format(LocalDateTime.now()));
        paramMap.put("date", dateTimeFormatter.format(LocalDateTime.now()).substring(0, 10));
        String outPath = "L:\\A.pdf";
        ClassLoader classLoader = PdfUtilTest.class.getClassLoader();
        URL resource = classLoader.getResource("templates");
        String templateDirectory = resource.toURI().getPath();
        String templateContent = PdfUtilTest.getTemplateContent(templateDirectory, "pdf_template.html", paramMap);
        PdfUtilTest.html2Pdf(templateContent, outPath);
    }


}


