package org.luck.xhga.common.util;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.utils.PdfMerger;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.font.FontProvider;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
public class Html2Pdf {
    // 字体下载: http://www.downcc.com/k/songti/
    private static final String SHIPPED_FONT_RESOURCE_PATH = Objects.requireNonNull(Html2Pdf.class.getResource("/fonts/simsun.ttc")).toString();

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
     * 获取模板内容
     * @param templateName      模板文件名
     * @param paramMap          模板参数
     * @return html
     * @throws Exception 异常
     */
    public static String getTemplateContent(String templateName, Map<String, Object> paramMap) throws Exception {
        ClassLoader classLoader = Html2Pdf.class.getClassLoader();
        URL resource = classLoader.getResource("templates");
        assert resource != null;
        String templateDirectory = resource.toURI().getPath();
        Configuration configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        try {
            configuration.setDirectoryForTemplateLoading(new File(templateDirectory));
        } catch (Exception e) {
            log.error("[getTemplateContent] exception, {}", e.getMessage());
        }
        Writer out = new StringWriter();
        Template template = configuration.getTemplate(templateName,"UTF-8");
        template.process(paramMap, out);
        out.flush();
        out.close();
        return out.toString();
    }

    /**
     * 获取模板内容
     * @param inputStream 输入流
     * @param paramMap    模板参数
     * @return html
     * @throws Exception 异常
     */
    private static String getTemplateContent(InputStream inputStream, Map<String, Object> paramMap) throws Exception {
        ClassLoader classLoader = Html2Pdf.class.getClassLoader();
        URL resource = classLoader.getResource("templates");
        assert resource != null;
        String templateDirectory = resource.toURI().getPath();
        Configuration configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        try {
            configuration.setDirectoryForTemplateLoading(new File(templateDirectory));
        } catch (Exception e) {
            log.error("[getTemplateContent] exception, {}", e.getMessage());
        }
        Writer out = new StringWriter();
        Template template = new Template("myTemplate", new InputStreamReader(inputStream), configuration);
        template.process(paramMap, out);
        out.flush();
        out.close();
        return out.toString();
    }

    /**
     * HTML 转 PDF
     *
     * @param content html内容
     * @param outPath 输出pdf路径
     */
    public static void html2Pdf(String content, String outPath) {
        try {
            html2Pdf(content, new FileOutputStream(outPath));
        } catch (FileNotFoundException e) {
            log.info("[html2Pdf] exception, :{}", e.getMessage());
        }
    }

    /**
     * HTML 转 PDF
     *
     * @param templateName      模板文件名
     * @param paramMap          模板参数
     * @param outputStream outputStream
     */
    public static void html2Pdf(String templateName, Map<String, Object> paramMap, OutputStream outputStream) {
        try {
            String content = getTemplateContent(templateName, paramMap);
            ConverterProperties converterProperties = Html2Pdf.getConverterProperties();
            HtmlConverter.convertToPdf(content, outputStream, converterProperties);
        } catch (Exception e) {
            log.info("[html2Pdf] exception: {}", e.getMessage());
        }
    }

    /**
     * HTML 转 PDF
     *
     * @param content      html内容
     * @param outputStream outputStream
     */
    public static void html2Pdf(String content, OutputStream outputStream) {
        try {
            ConverterProperties converterProperties = Html2Pdf.getConverterProperties();
            HtmlConverter.convertToPdf(content, outputStream, converterProperties);
        } catch (Exception e) {
            log.info("[html2Pdf] exception: {}", e.getMessage());
        }
    }

    /**
     * HTML 转 PDF
     * @param content html内容
     * @return PDF字节数组
     */
    public static byte[] html2Pdf(String content) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();;
        try {
            ConverterProperties converterProperties = Html2Pdf.getConverterProperties();
            HtmlConverter.convertToPdf(content, outputStream, converterProperties);
        } catch (Exception e) {
            log.info("[html2Pdf] exception: {}", e.getMessage());
        }
        return outputStream.toByteArray();
    }

    /**
     * 合并多个 HTML 转 PDF
     * @param contents html内容
     * @param outputStream 输出
     */
    public static void mergeHtml2Pdf(List<String> contents, OutputStream outputStream, PageSize pageSize) {
        ConverterProperties converterProperties = Html2Pdf.getConverterProperties();
        PdfDocument pdf = new PdfDocument(new PdfWriter(outputStream));
        PdfMerger merger = new PdfMerger(pdf);
        log.info("[mergeHtml2Pdf] contents: {}", contents.size());
        for (String html:contents) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfDocument temp = new PdfDocument(new PdfWriter(baos));

            temp.setDefaultPageSize(pageSize);
            HtmlConverter.convertToPdf(html, temp, converterProperties);
            try {
                temp = new PdfDocument(new PdfReader(new ByteArrayInputStream(baos.toByteArray())));
            } catch (IOException e) {
                log.info("[mergeHtml2Pdf] IOException: {}", e.getMessage());
            }
            merger.merge(temp, 1, temp.getNumberOfPages());
            temp.close();
        }
        merger.close();
        pdf.close();
    }

    public static FontProvider getFontProvider() {
        FontProvider fontProvider = new FontProvider();
        try {
            FontProgram fontProgram = FontProgramFactory.createFont(SHIPPED_FONT_RESOURCE_PATH+",0");
            fontProvider.addFont(fontProgram);

            // PdfFont sysFont = PdfFontFactory.createFont("STSongStd-Light", "UniGB-UCS2-H");
            // fontProvider.addFont(sysFont.getFontProgram(), "UniGB-UCS2-H");
        } catch (IOException e) {
            log.info("[getFontProvider] exception: {}", e.getMessage());
        }
        fontProvider.addSystemFonts();
        return fontProvider;
    }

    public static ConverterProperties getConverterProperties() {
        ConverterProperties converterProperties = new ConverterProperties();
        converterProperties.setCharset("UTF-8");
        FontProvider fontProvider = Html2Pdf.getFontProvider();
        converterProperties.setFontProvider(fontProvider);
        return converterProperties;
    }

    public static float pxToPt(float pxValue) {
        // 假设屏幕分辨率为 96 像素/英寸
        float inches = pxValue / 96f;
        // 1 英寸 = 72 磅
        return inches * 72f;
    }

    public static void main(String[] args) throws Exception {
        Map<String, Object> paramMap = new HashMap<>();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        paramMap.put("date_time", dateTimeFormatter.format(LocalDateTime.now()));
        paramMap.put("date", dateTimeFormatter.format(LocalDateTime.now()).substring(0, 10));
        String outPath = "test1.pdf";
        ClassLoader classLoader = Html2Pdf.class.getClassLoader();
        URL resource = classLoader.getResource("templates");
        String templateContent = Html2Pdf.getTemplateContent( resource.toURI().getPath(), "test.html", paramMap);
        // 将html转pdf
        //Html2Pdf.html2Pdf(templateContent, outPath);
        PageSize pageSize = null;
        pageSize = new PageSize(pxToPt(1382.0F), pxToPt(1000F));
        FileOutputStream fileOutputStream = new FileOutputStream(outPath);
        // 合并多个pdf
        Html2Pdf.mergeHtml2Pdf(Arrays.asList(templateContent, templateContent), fileOutputStream, pageSize);
    }


}


