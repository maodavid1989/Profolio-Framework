package profolio.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import org.apache.log4j.Logger;


public class ReportUtil {
    Logger logger = Logger.getLogger(ReportUtil.class);
    
    public OutputStream OutputToWord(String JasperName, String OutputName, Map<String, Object> params, List list, HttpServletResponse response ) throws UnsupportedEncodingException, IOException, URISyntaxException{
        String jasperFilePath = "jasperreports/jrxml/"+ JasperName +".jasper";//模板
        logger.info("jasperFilePath : "+jasperFilePath);
        String docxFilePath = "jasperreports/report/" + OutputName +".docx";//output檔名
        logger.info("jasperFilePath : "+docxFilePath);
        
        createWord(params, list, jasperFilePath, docxFilePath);
        
        response.setContentType("application/x-download");
        String fileName = new String(OutputName.getBytes(), "ISO8859-1");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".docx");
        OutputStream output = response.getOutputStream();
                
        return output;
    }
    
    public void createWord(Map<String, Object> params , List list,
                            String jasperFilePath , String docxFilePath) throws URISyntaxException {
        
        String basePath=new URI(this.getClass().getResource("/").toString()).getPath();
        basePath=basePath.substring(1);//去除'/'
        logger.info("basePath:  "+ basePath);
        File jasperFileName = new File(basePath + jasperFilePath);
        logger.info("jasperFileName:"+jasperFileName);
        
        try {
            // 建立JRMapCollectionDataSource
            JRDataSource datasource = new JRMapCollectionDataSource(list);
            JasperPrint jasperPrint = null;
            if (list.isEmpty()){
                jasperPrint = JasperFillManager.fillReport(jasperFileName.getPath(), params, new JREmptyDataSource());
            }else if (list.size()>0){
                jasperPrint = JasperFillManager.fillReport(jasperFileName.getPath(), params, datasource);
            }
            ByteArrayOutputStream oStream = new ByteArrayOutputStream();
            JRAbstractExporter exporter = new JRDocxExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, oStream);
            exporter.exportReport();
            

            byte[] bytes = oStream.toByteArray();
            if (bytes != null && bytes.length > 0) {
                OutputStream ouputStream = new FileOutputStream(new File(basePath + docxFilePath));
                ouputStream.write(bytes, 0, bytes.length);
                ouputStream.flush();
                ouputStream.close();
            } 

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public OutputStream OutputToExcel(String JasperName, String OutputName, Map<String, Object> params, List list, HttpServletResponse response ) throws UnsupportedEncodingException, IOException, URISyntaxException{
        String jasperFilePath = "/jasperreports/jrxml/"+ JasperName +".jasper";//模板
        logger.info("jasperFilePath : "+jasperFilePath);
        String xlsFilePath = "/jasperreports/report/" + OutputName +".xls";//output檔名
        logger.info("jasperFilePath : "+xlsFilePath);
        
        createExcel(params, list, jasperFilePath, xlsFilePath);
        
        response.setContentType("application/vnd.ms-excel");
        String fileName = new String(OutputName.getBytes(), "ISO8859-1");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xls");
        OutputStream output = response.getOutputStream();
                
        return output;
    } 
    
    public void createExcel(Map<String, Object> params , List list,
                            String jasperFilePath , String xlsFilePath) throws URISyntaxException {
        String basePath=new URI(this.getClass().getResource("/").toString()).getPath();
        basePath=basePath.substring(1);//去除'/'
        logger.info("basePath:  "+ basePath);
        File jasperFileName = new File(basePath + jasperFilePath);
        logger.info("jasperFileName:"+jasperFileName);
        try {
            // 建立JRMapCollectionDataSource
            JRDataSource datasource = new JRMapCollectionDataSource(list);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperFileName.getPath(), params, datasource);
            
            JRXlsExporter exporter = new JRXlsExporter();
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            OutputStream outputfile= new FileOutputStream(new File(basePath + xlsFilePath));

            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputfile);
            exporter.exportReport();
            
           
        } catch (Exception ex) {
            logger.info(ex);
            ex.printStackTrace();
        }

    }   

    public void returnFile(String filename, OutputStream out) throws FileNotFoundException, IOException {
        InputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(filename));
            byte[  ] buf = new byte[4 * 1024];  // 4K buffer
            int bytesRead;
            while ((bytesRead = in.read(buf)) != -1) {
                out.write(buf, 0, bytesRead);
            }
        }finally {
            if (in != null) in.close();
        }
    }

}

