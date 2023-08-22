package com.caizi.edu.sms.biz.utils;

import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.poi.ss.usermodel.Workbook;
import org.jxls.common.Context;
import org.jxls.expression.JexlExpressionEvaluator;
import org.jxls.transform.poi.PoiTransformer;
import org.jxls.util.JxlsHelper;
import org.springframework.core.io.ClassPathResource;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Jxls2 工具类
 */
public class JxlsUtils{

    public static Workbook exportExcel(InputStream is, OutputStream os, Map<String, Object> model) throws Exception {
        Context context = PoiTransformer.createInitialContext();
        if (model != null) {
            for (String key : model.keySet()) {
                context.putVar(key, model.get(key));
            }
        }
        JxlsHelper jxlsHelper = JxlsHelper.getInstance();
        PoiTransformer transformer  = PoiTransformer.createTransformer(is, os);
        //获得配置
        JexlExpressionEvaluator evaluator = (JexlExpressionEvaluator)transformer.getTransformationConfig().getExpressionEvaluator();

        //函数增强，自定义功能
        Map<String, Object> funcs = new HashMap<>();
        funcs.put("func", new JxlsFunctions()); //添加自定义功能
        JexlBuilder jb = new JexlBuilder();
        jb.namespaces(funcs);
        jb.silent(true); //设置静默模式，不报警告
        JexlEngine je = jb.create();
        evaluator.setJexlEngine(je);

        //必须要这个，否者表格函数统计会错乱
        jxlsHelper.setUseFastFormulaProcessor(true).setProcessFormulas(true).processTemplate(context, transformer);

        return transformer.getWorkbook();
    }

    public static Workbook exportExcel(String templatePath, OutputStream os, Map<String, Object> model) throws Exception {
        InputStream inputStream = getTemplate(templatePath);
        if(inputStream != null){
            return exportExcel(inputStream , os, model);
        } else {
            throw new Exception("Excel 模板未找到。");
        }
    }

    /**
     * 获取jxls模版文件
     * @param path  模板文件路径
     * @return
     */
    public static InputStream getTemplate(String path) throws IOException {
        //解决打包后无法获取jar包中的excel模板文件
        ClassPathResource classPathResource = new ClassPathResource(path);
        if(!classPathResource.exists()){
            FileInputStream fis = new FileInputStream(path);
            return fis;
        }else{
            return classPathResource.getInputStream();
        }
    }
}
