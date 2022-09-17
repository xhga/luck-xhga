package org.luck.xhga.middleware.mybatisplus;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author GEEX1123
 * 持久层逆向工程，仅需修改URL和DATABASE_NAME，并在控制台输入表名即可
 */
@Slf4j
public class DaoCodeGenerator {

    private static final String AUTHOR = "xhga";

    private static final String URL = "jdbc:mysql://127.0.0.1:3306/luck-seata?useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&useSSL=false&serverTimezone=Asia/Shanghai";

    private static final String JDBC_DRIVER_NAME = "com.mysql.cj.jdbc.Driver";

    private static final String USERNAME = "root";

    private static final String PASSWORD = "";

    private static final String DATABASE_NAME = "";

    private static final String ENTITY_BASE_PACKAGE = "org.luck";

    private static final String TEMPLATE_PATH = "/templates/mapper.xml.ftl";

    public static void main(String[] args) {
        AutoGenerator autoGenerator = new AutoGenerator();
        autoGenerator.setGlobalConfig(generateGlobalConfig());
        autoGenerator.setDataSource(generateDataSourceConfig());
        final PackageConfig packageConfig = generatePackageConfig();
        autoGenerator.setPackageInfo(packageConfig);
        InjectionConfig injectionConfig = new InjectionConfig() {
            @Override
            public void initMap() {

            }
        };
        List<FileOutConfig> focList = new ArrayList<FileOutConfig>();
        focList.add(new FileOutConfig() {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return System.getProperty("user.dir") + "/src/main/resources/mapper/" + DATABASE_NAME + "/" + packageConfig.getModuleName() +
                       "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        injectionConfig.setFileOutConfigList(focList);
        autoGenerator.setCfg(injectionConfig);
        autoGenerator.setTemplate(generateTemplateConfig());
        autoGenerator.setStrategy(generateStrategyConfig(packageConfig));
        //autoGenerator.setTemplateEngine(new FreemarkerTemplateEngine());
        autoGenerator.execute();
    }

    private static GlobalConfig generateGlobalConfig(){
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setOutputDir(System.getProperty("user.dir") + "/src/main/java");
        globalConfig.setAuthor(AUTHOR);
        globalConfig.setOpen(false);
        globalConfig.setBaseResultMap(true);
        globalConfig.setBaseColumnList(true);
        return globalConfig;
    }

    private static DataSourceConfig generateDataSourceConfig(){
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUrl(URL);
        dataSourceConfig.setDriverName(JDBC_DRIVER_NAME);
        dataSourceConfig.setUsername(USERNAME);
        dataSourceConfig.setPassword(PASSWORD);
        return dataSourceConfig;
    }

    private static PackageConfig generatePackageConfig(){
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setEntity("dao.entity");
        packageConfig.setMapper("dao.mapper." + DATABASE_NAME);
        packageConfig.setParent(ENTITY_BASE_PACKAGE);
        return packageConfig;
    }

    private static StrategyConfig generateStrategyConfig(PackageConfig packageConfig){
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig.setNaming(NamingStrategy.underline_to_camel);
        strategyConfig.setColumnNaming(NamingStrategy.underline_to_camel);
        strategyConfig.setSuperEntityClass("");
        strategyConfig.setEntityLombokModel(true);
        strategyConfig.setRestControllerStyle(true);
        strategyConfig.setSuperControllerClass("");
        strategyConfig.setSuperEntityColumns("id");
        strategyConfig.setInclude(scanner().split(","));
        strategyConfig.setControllerMappingHyphenStyle(true);
        strategyConfig.setTablePrefix(packageConfig.getModuleName() + "_");
        return strategyConfig;
    }

    private static TemplateConfig generateTemplateConfig(){
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setController("");
        templateConfig.setService("");
        templateConfig.setServiceImpl("");
        templateConfig.setXml("");
        return templateConfig;
    }

    private static String scanner(){
        Scanner scanner = new Scanner(System.in);
        log.info("请输入" + "表名, 多个英文逗号分割" + ": ");
        if (scanner.hasNext()){
            String input = scanner.next();
            if (StringUtils.isNotBlank(input)){
                return input;
            }
        }
        throw new MybatisPlusException("请输入正确的" + "表名, 多个英文逗号分割" + "! ");
    }

}
