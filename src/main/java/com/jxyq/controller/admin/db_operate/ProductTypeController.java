package com.jxyq.controller.admin.db_operate;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.jxyq.app.BaseInterface;
import com.jxyq.commons.constants.BeanProperty;
import com.jxyq.commons.constants.ErrorCode;
import com.jxyq.commons.mybatis.pagination.PagingCriteria;
import com.jxyq.commons.util.CommonUtil;
import com.jxyq.model.health.ProductType;
import com.jxyq.model.others.ResponseContent;
import com.jxyq.service.inf.DeviceService;
import com.jxyq.service.inf.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by wujj.fnst on 2015/4/12.
 */
@Controller
public class ProductTypeController extends BaseInterface {
    @Autowired
    private ProductTypeService productTypeService;
    @Autowired
    private DeviceService deviceService;

    //3.2.2.5.1	POST /admins/db/product_types
    @RequestMapping(value = "/admins/db/product_types", method = RequestMethod.POST)
    public void PostProduct_types(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"product_type_ids", "registered_from", "registered_to",
                    "page_size", "current_page"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            JsonArray product_type_ids = params.get("product_type_ids").getAsJsonArray();
            long registered_from = params.get("registered_from").getAsLong();
            long registered_to = params.get("registered_to").getAsLong();
            Integer page_size = params.get("page_size").getAsInt();
            Integer current_page = params.get("current_page").getAsInt();
            Map<String, Object> map = new HashMap<String, Object>();
            if (product_type_ids != null && product_type_ids.size() > 0) {
                List<Integer> ids = new ArrayList<>();
                for (JsonElement item : product_type_ids) {
                    int product_type_id = item.getAsInt();
                    ids.add(product_type_id);
                }
                map.put("product_type_ids", ids);
            }
            map.put("minRegisterTime", registered_from);
            map.put("maxRegisterTime", registered_to);
            long query_date = (new Date()).getTime() / 1000;
            if (params.has("query_date") && params.get("query_date").getAsLong() != -1) {
                query_date = params.get("query_date").getAsLong();
                map.put("query_date", query_date);
            }
            PagingCriteria pageInf = new PagingCriteria(current_page, page_size, "", "");
            List<Map<String, Object>> products = productTypeService.selProducTypeByPage(map, pageInf);
            ArrayList lists = new ArrayList();
            for (Map<String, Object> pro : products) {
                Map<String, Object> cnt = new HashMap<>();
                cnt.put("product_type_id", pro.get("product_type_id"));
                cnt.put("product_type_name", pro.get("name"));
                cnt.put("registered_at", pro.get("registered_at"));
                lists.add(cnt);
            }
            Map<String, Object> result = new HashMap<>();
            result.put("page_size", page_size);
            result.put("current_page", current_page);
            result.put("total_page", CommonUtil.getPageCount(pageInf.getTotal(), page_size));
            result.put("total_count", pageInf.getTotal());
            result.put("query_date", query_date);
            result.put("product_types", lists);
            content.setSuccess_message(result);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //3.2.2.5.2	POST /admins/db/product_types/fuzzy_search
    @RequestMapping(value = "/admins/db/product_types/fuzzy_search", method = RequestMethod.POST)
    public void postProduct_typesFuzzySearch(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"name", "registered_from", "registered_to",
                    "page_size", "current_page"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            String name = params.get("name").getAsString();
            Integer page_size = params.get("page_size").getAsInt();
            Integer current_page = params.get("current_page").getAsInt();
            long registered_from = params.get("registered_from").getAsLong();
            long registered_to = params.get("registered_to").getAsLong();
            Map<String, Object> map = new HashMap<>();
            map.put("name", name);
            map.put("minRegisterTime", registered_from);
            map.put("maxRegisterTime", registered_to);
            long query_date = (new Date()).getTime() / 1000;
            if (params.has("query_date") && params.get("query_date").getAsLong() != -1) {
                query_date = params.get("query_date").getAsLong();
                map.put("query_date", query_date);
            }
            PagingCriteria pageInf = new PagingCriteria(current_page, page_size, "", "");
            List<Map<String, Object>> products = productTypeService.selProducTypeByPage(map, pageInf);
            ArrayList lists = new ArrayList();
            for (Map<String, Object> pro : products) {
                Map<String, Object> cnt = new HashMap<>();
                cnt.put("product_type_id", pro.get("product_type_id"));
                cnt.put("product_type_name", pro.get("name"));
                cnt.put("registered_at", pro.get("registered_at"));
                lists.add(cnt);
            }
            Map<String, Object> result = new HashMap<>();
            result.put("page_size", page_size);
            result.put("current_page", current_page);
            result.put("total_page", CommonUtil.getPageCount(pageInf.getTotal(), page_size));
            result.put("total_count", pageInf.getTotal());
            result.put("query_date", query_date);
            result.put("product_types", lists);
            content.setSuccess_message(result);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }


    // 3.2.2.5.3	GET /admins/db/product_types/{pid}
    @RequestMapping(value = "/admins/db/product_types/{pid}", method = RequestMethod.GET)
    public void getProduct_typesDetail(@PathVariable("pid") int id,
                                       HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            List<Map<String, Object>> deviceDetail = productTypeService.selProductDetail(id);
            ArrayList lists = new ArrayList();
            for (Map<String, Object> device : deviceDetail) {
                lists.add(device.get("device_type_id"));
            }
            Map<String, Object> result = new HashMap<>();
            result.put("device_type_ids", lists);
            content.setSuccess_message(result);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //3.2.2.5.4	POST /admins/db/product_types/add
    @RequestMapping(value = "/admins/db/product_types/add", method = RequestMethod.POST)
    public void postProduct_typesAdd(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"name"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            String name = params.get("name").getAsString();
            ProductType productType = new ProductType();
            productType.setProduct_type_id(0);
            productType.setName(name);
            productType.setRegistered_at(new Date().getTime() / 1000);
            productType.setStatus(BeanProperty.DeviceTypeStatus.NORMAL);
            productTypeService.insertProductType(productType);
            Map<String, Object> map = new HashMap<>(1);
            map.put("product_type_id", productType.getProduct_type_id());
            content.setSuccess_message(map);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //   3.2.2.5.5	PUT /admins/db/product_types/{pid}
    @RequestMapping(value = "/admins/db/product_types/{pid}", method = RequestMethod.PUT)
    public void editProduct_types(@PathVariable("pid") int id,
                                  HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"name"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            String name = params.get("name").getAsString();
            /*JsonArray device_type_ids = params.get("device_type_ids").getAsJsonArray();
            Map<String, Object> map = new HashMap<>();
            map.put("product_type_id", id);
            if (device_type_ids != null && device_type_ids.size() > 0) {
                for (JsonElement device_type_id : device_type_ids) {
                    map.put("device_type_id", device_type_id.getAsInt());
                    productTypeService.upProductTypeInf(map);
                }
            }*/
            productTypeService.upProductName(id, name);
            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //3.2.2.5.6	DELETE /admins/db/product_types/{pid}
    @RequestMapping(value = "/admins/db/product_types/{pid}", method = RequestMethod.DELETE)
    public void deleteProduct_types(@PathVariable("pid") int id,
                                    HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            productTypeService.delProductType(id);
            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

}