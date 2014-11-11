package profolio.control;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import profolio.base.BaseServlet;
import profolio.util.ReportUtil;


@WebServlet("/Works_StockServlet")
public class Works_StockServlet extends BaseServlet { 
    Logger logger = Logger.getLogger(Works_StockServlet.class);

    @Override
    protected void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        ReportUtil rutil= new ReportUtil();
        Map<String, Object> params = new HashMap<String, Object>();
        List toJasperList=new ArrayList();   
        PrintFormat printformat=null;
        //通用函數
        Calendar cal = Calendar.getInstance();
        NumberFormat nf = NumberFormat.getInstance(); 
        SimpleDateFormat sdfmt = new SimpleDateFormat("yyyyMMdd"); 
        String sdate = sdfmt.format(new java.util.Date());

        String actionKey=request.getParameter("printName");       
        switch(actionKey){
            
            case "CDStock": //定存股資訊parse to xls
                printformat =new PrintFormat("Stock_Detail","定存股觀察清單","xls");  
                
                String sn[]={"1210","1216","2887"};
                
                                
                for (int i=0 ; i<sn.length ; i++){
                    //列印名單
                    Map JMap=new HashMap();
                    Map toJasperMap=parse1(sn[i],JMap);

                    toJasperMap=parse2(sn[i],toJasperMap);
                    
                    toJasperList.add(toJasperMap);
                    
                    
                }
                
                params.put("DATE", convertDate(sdate));
                break;
            default :
                logger.info("報表沒有符合條件!!");
                break;
            
        }

        OutputStream output;
        String basePath=new URI(this.getClass().getResource("/").toString()).getPath();
        basePath=basePath.substring(1);
        logger.info(basePath);
        //File f = new File(this.getClass().getResource("/").getPath());

        String filepath;
        switch(printformat.printType){
            case "doc":
                output=rutil.OutputToWord(printformat.JasperName, printformat.OutputName, params, toJasperList, response);
                filepath =  basePath + "jasperreports/report/" + printformat.OutputName +".docx";
                logger.info("filepath"+filepath);
                rutil.returnFile(filepath, output);  
                break;
            case "xls":
                output=rutil.OutputToExcel(printformat.JasperName, printformat.OutputName, params, toJasperList, response);
                filepath =  basePath + "jasperreports/report/" + printformat.OutputName +".xls";
                logger.info("filepath"+filepath);
                rutil.returnFile(filepath, output); 
                break;
            default :
                logger.info("Output類型錯誤!!");
                break;
        }

        
    }
        
    private String subJasperGetPath(String path) throws URISyntaxException{
        
        String basePath=new URI(this.getClass().getResource("/").toString()).getPath();
        basePath=basePath.substring(1);
        logger.info("basePath:  "+ basePath);
        //File webPath = new File(this.getClass().getResource("/").getPath());
        File subJasperFileName = new File(basePath + path);
        logger.info("subJasperGetPath:"+subJasperFileName.getPath());
        return subJasperFileName.getPath();
        
    }

    private Map performList(List performList, String table){
        int totalService=0;
        int totalValid=0;
        for (int i=0; i<performList.size(); i++){
            Map jMap=(Map)performList.get(i);
            int Service = jMap.get("service").toString().equals("") ? 0 : Integer.parseInt(jMap.get("service").toString());
            int Valid = jMap.get("valid").toString().equals("") ? 0 : Integer.parseInt(jMap.get("valid").toString()); 
            float percent=((float)Valid/Service ) * 100;                   
            jMap.put("percent", String.valueOf((int)percent));
            jMap.put("type", table);
            totalService+=Service;
            totalValid+=Valid;
        }   
        ArrayList alist=new ArrayList();
        Map<String,Integer> performMap=new HashMap<>();
        performMap.put("totalService",totalService);
        performMap.put("totalValid",totalValid);
        return performMap;
    }
    
    //列印格式
    private class PrintFormat {
    String OutputName;
    String JasperName;
    String printType;
    
        PrintFormat(String JasperName, String OutputName, String printType) {
            this.OutputName = OutputName;
            this.JasperName = JasperName;
            this.printType = printType;
        }
    } 
    
    //去除 "元"
    private String remoDol(Object o){
        return o.toString().substring(0, o.toString().length()-1);
    }
    
    //推估平均 eps 
    private String AVEeps(Object o1,Object o2,Object o3,Object o4){
        Double average=Double.parseDouble(o1.toString())+Double.parseDouble(o2.toString())
                +Double.parseDouble(o3.toString())+Double.parseDouble(o4.toString());
        
        DecimalFormat df = new DecimalFormat("#,##0.00");  
        return String.valueOf(df.format(average));
    }
    
    //20141104 to 103年11月04日
    private String convertDate(String sdate){
        String year=String.valueOf(Integer.parseInt(sdate.substring(0,4))-1911);
        return year+"年"+sdate.substring(4,6)+"月"+sdate.substring(6,8)+"日";
    }
    
    
    private Map parse1 (String sn,Map JMap) throws IOException{
        
                    List JList=new ArrayList();
                    
                    String SN="https://tw.stock.yahoo.com/d/s/company_"+sn+".html";
                    
                    Document doc = Jsoup.connect(SN).get();
                    //logger.info(doc);
                    
                    Elements name = doc.getElementsByTag("b"); //代號、名稱

                    Element elementsByTag = doc.getElementsByTag("table").get(7); 
                    Elements trCount = elementsByTag.getElementsByTag("tr");

                    for (int r=0 ; r < trCount.size() ; r++){
                        Map<String,String> toJasper = new HashMap<String,String>();

                        Element row = elementsByTag.getElementsByTag("tr").get(r);
                        logger.info("row: "+r);
                        Elements column=row.getElementsByTag("td");
                        for (int c=0 ; c<column.size() ; c++){
                            //logger.info("test:"+column.get(c).text());
                            toJasper.put("key"+c , column.get(c).text() );
                        }
                        JList.add(toJasper);
                    }

                    Map tempMap16=(Map)JList.get(16);
                    Map tempMap17=(Map)JList.get(17);
                    Map tempMap18=(Map)JList.get(18);
                    Map tempMap19=(Map)JList.get(19);
                    Map tempMap20=(Map)JList.get(20);

                    //1.no
                    JMap.put("no",name.text().substring(0,4));
                    //2.股票名稱
                    JMap.put("name",name.text().substring(4));

                    //4.每股淨值
                    String realprice=tempMap20.get("key2").toString().replace("每股淨值: 　　","");
                    JMap.put("realprice",realprice.substring(0,realprice.length()-1));

                    JMap.put("eps_seasontitle1", tempMap16.get("key2").toString().substring(0,3)+" Q"+tempMap16.get("key2").toString().substring(4,5));                
                    JMap.put("eps_seasontitle2", tempMap17.get("key2").toString().substring(0,3)+" Q"+tempMap17.get("key2").toString().substring(4,5));  
                    JMap.put("eps_seasontitle3", tempMap18.get("key2").toString().substring(0,3)+" Q"+tempMap18.get("key2").toString().substring(4,5));  
                    JMap.put("eps_seasontitle4", tempMap19.get("key2").toString().substring(0,3)+" Q"+tempMap19.get("key2").toString().substring(4,5));  

                    JMap.put("eps_season1", remoDol(tempMap16.get("key3")));
                    JMap.put("eps_season2", remoDol(tempMap17.get("key3")));
                    JMap.put("eps_season3", remoDol(tempMap18.get("key3")));
                    JMap.put("eps_season4", remoDol(tempMap19.get("key3")));

                    JMap.put("eps_pasttitle1", tempMap16.get("key4"));
                    JMap.put("eps_pasttitle2", tempMap17.get("key4"));
                    //JMap.put("eps_pasttitle3", tempMap18.get("key4"));
                    //JMap.put("eps_pasttitle4", tempMap19.get("key4"));                

                    JMap.put("eps_past1", remoDol(tempMap16.get("key5")));
                    JMap.put("eps_past2", remoDol(tempMap17.get("key5")));
                    //JMap.put("eps_past3", remoDol(tempMap18.get("key5")));
                    //JMap.put("eps_past4", remoDol(tempMap19.get("key5")));

                    String AVEeps= AVEeps(remoDol(tempMap16.get("key3")),remoDol(tempMap17.get("key3")),
                            remoDol(tempMap18.get("key3")),remoDol(tempMap19.get("key3")));
                    //推估eps
                    JMap.put("aveeps",AVEeps);

                    logger.info(JMap);
                    return JMap;
    }
    
    //股利
    private Map parse2(String sn,Map JMap) throws IOException{
        
                    List JList=new ArrayList();
                    
                    //股利政策
                    String SN="https://tw.stock.yahoo.com/d/s/dividend_"+sn+".html";
                    
                    Document doc = Jsoup.connect(SN).get();
                    //logger.info(doc);
                    
                    Elements name = doc.getElementsByTag("b"); //代號、名稱

                    Element elementsByTag = doc.getElementsByTag("table").get(7); 
                    Elements trCount = elementsByTag.getElementsByTag("tr");

                    for (int r=0 ; r < trCount.size() ; r++){
                        Map<String,String> toJasper = new HashMap<String,String>();

                        Element row = elementsByTag.getElementsByTag("tr").get(r);
                        logger.info("row: "+r);
                        Elements column=row.getElementsByTag("td");
                        for (int c=0 ; c<column.size() ; c++){
                            //logger.info("test:"+column.get(c).text());
                            toJasper.put("key"+c , column.get(c).text() );
                        }
                        JList.add(toJasper);
                    }

                    //最近年度配股配息
                    Map tempMap3=(Map)JList.get(3); 
                    JMap.put("money1", tempMap3.get("key1"));//現金
                    JMap.put("divide1", tempMap3.get("key2"));//配股 
                    Double summoneydivide=Double.parseDouble(tempMap3.get("key1").toString())+Double.parseDouble(tempMap3.get("key2").toString());
                    JMap.put("summoneydivide",String.valueOf(summoneydivide));//股利合計
                    logger.info(JMap);
                    return JMap;
    }
}

