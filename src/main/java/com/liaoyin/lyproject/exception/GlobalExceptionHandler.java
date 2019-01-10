package com.liaoyin.lyproject.exception;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.liaoyin.lyproject.base.api.JsonRestResponse;
import com.liaoyin.lyproject.base.entity.ValidResultBean;
import com.liaoyin.lyproject.util.ClientUtil;
import com.liaoyin.lyproject.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @项目名：公司内部模板项目
 * @作者：
 * @描述：全局异常处理类
 * @日期：Created in 2018/6/8 14:50
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }

    /**
     * @方法名：jsonErrorHandler
     * @描述： 运行时异常
     * @作者：
     * @日期： Created in 2018/6/8 14:51
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public JsonRestResponse jsonErrorHandler(HttpServletRequest req, Exception e){
        JsonRestResponse res = new JsonRestResponse();
        res.setCode("common.systemErro");
        res.setAttach(e.getMessage());
        res.setDesc("内部错误");
        logError(req, res, e);
        e.printStackTrace();
        return res;
//        throw e;
    }

   /**
    * @方法名：jsonGetReqValidHandler
    * @描述： 验证异常
    * @作者：
    * @日期： Created in 2018/6/8 14:51
    */
    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseBody
    public JsonRestResponse jsonGetReqValidHandler(HttpServletRequest req, ConstraintViolationException e) {
        JsonRestResponse res = new JsonRestResponse();
        res.setCode("验证失败");
        StringBuilder sb = new StringBuilder();
        List<ValidResultBean> list = new ArrayList<ValidResultBean>();
        for (ConstraintViolation c : e.getConstraintViolations()) {
            ValidResultBean bean = new ValidResultBean();
            bean.setFieldName(c.getPropertyPath().toString());
            bean.setErrorMassage(c.getMessage());
            list.add(bean);
            sb.append(c.getMessage());
            sb.append("||");
        }
        sb.delete(sb.length() - 2, sb.length());
        String json = JSONArray.toJSONString(list);
        res.setResult(json);
        res.setDesc(sb.toString());
        return res;
    }

    /**
     * @方法名：jsonValidHandler
     * @描述： 验证异常
     * @作者：
     * @日期： Created in 2018/6/8 14:51
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public JsonRestResponse jsonValidHandler(HttpServletRequest req, MethodArgumentNotValidException e) {
        JsonRestResponse res = new JsonRestResponse();
        res.setCode("验证失败");

        BindingResult validResult = e.getBindingResult();
        StringBuilder sb = new StringBuilder();
        List<ValidResultBean> list = new ArrayList<ValidResultBean>();
        for (int i = 0; i < validResult.getErrorCount(); i++) {
            ObjectError error = validResult.getAllErrors().get(i);
            DefaultMessageSourceResolvable arg = (DefaultMessageSourceResolvable) error.getArguments()[0];
            ValidResultBean bean = new ValidResultBean();
            bean.setFieldName(arg.getDefaultMessage());
            bean.setErrorMassage(error.getDefaultMessage());
            sb.append(error.getDefaultMessage());
            if (i + 1 != validResult.getErrorCount()) {
                sb.append("||");
            }
            list.add(bean);
        }
        String json = JSONArray.toJSONString(list);
        res.setResult(json);
        res.setDesc(sb.toString());
        return res;
    }

    /**
     * @方法名：jsonErrorHandler
     * @描述： 业务类异常在service中主动调用throw new BusinessException(MsgCodeConstant.XXXX)抛出业务异常
     * @作者：
     * @日期： Created in 2018/6/8 14:52
     */
    @ExceptionHandler(value = BaseException.class)
    @ResponseBody
    public JsonRestResponse jsonErrorHandler(HttpServletRequest req, BusinessException e) {
        JsonRestResponse res = new JsonRestResponse();
        res.setCode(e.getCode());
        res.setDesc(PropertiesUtil.get(e.getCode(), e.args));
        logError(req, res, e);

        return res;
    }

    /**
     * @方法名：logError
     * @描述： 异常打印
     * @作者：
     * @日期： Created in 2018/6/8 14:53
     */
    private void logError(HttpServletRequest req, JsonRestResponse res, Exception e) {

        String requestIp = ClientUtil.getClientIp(req);
        String requestUrl = req.getRequestURL().toString();

        //log.error("content type  :" + req.getContentType());
        log.error("ip:" + requestIp);
        log.error("request   type:" + req.getMethod());
        log.error("request    url:" + requestUrl);
        if ("GET".equalsIgnoreCase(req.getMethod())) {
            log.error("request params:" + req.getQueryString());
        } else {
            Map<String, String[]> params = req.getParameterMap();
            if (params != null) {
                log.error("request params:" + JSONObject.toJSONString(params));
            }
        }
        log.error(res.toString());
        log.error(e.toString());
    }

}
