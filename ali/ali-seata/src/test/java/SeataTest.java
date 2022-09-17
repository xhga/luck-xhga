import org.junit.jupiter.api.Test;
import org.luck.xhga.ali.seata.SpringAliSeataApplication;
import org.luck.xhga.ali.seata.service.SeataService;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author GEEX1928
 * @date 2021/5/31
 */
@SpringBootTest(classes = SpringAliSeataApplication.class)
public class SeataTest {

    @Resource
    private SeataService seataService;

    @Test
    public void test() {
        seataService.handle("1");
    }
}
