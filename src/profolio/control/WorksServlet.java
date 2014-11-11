package profolio.control;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import profolio.base.AjaxBaseServlet;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet("/WorksServlet")
public class WorksServlet extends AjaxBaseServlet {
    Logger logger = Logger.getLogger(WorksServlet.class);

    @Override
    protected void executeAjax(HttpServletRequest request, HttpServletResponse response, HttpSession session,
        JSONObject argJsonObj, JSONObject returnJasonObj) throws Exception {
        logger.info("into WorksServlet.....");
        logger.info("into WorksServlet.....");
        logger.info("into WorksServlet.....");
        JSONArray dataArray = new JSONArray();
        dataArray.put("loading backend successful");
        this.setFormData(returnJasonObj, dataArray);           
    }


}
