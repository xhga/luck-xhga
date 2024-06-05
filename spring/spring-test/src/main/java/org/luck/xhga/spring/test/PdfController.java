package org.luck.xhga.spring.test;

import lombok.SneakyThrows;
import org.luck.xhga.common.util.Html2Pdf;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

/**
 * pdf
 * @author hwb
 */
@RestController
@RequestMapping("/pdf")
public class PdfController {

    @SneakyThrows
    @GetMapping("/generate")
    public void generate(HttpServletResponse response) {
        response.setContentType("application/pdf");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        String fileName = URLEncoder.encode("压疮护理", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename*=" + fileName + ".pdf");
        String content = "<html><head><meta charset=\"UTF-8\"></head><body><h1>压疮护理</h1></body></html>";
        Html2Pdf.html2Pdf(content, response.getOutputStream());
    }

}
