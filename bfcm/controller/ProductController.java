package com.seanco.bfcm.controller;

import com.seanco.bfcm.pojo.User;
import com.seanco.bfcm.service.IProductService;
import com.seanco.bfcm.vo.ProductDetailVo;
import com.seanco.bfcm.vo.ProductVo;
import com.seanco.bfcm.vo.ResponseInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("product")
public class ProductController {

    @Autowired
    private IProductService productService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    @RequestMapping(value = "toList", produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toList(HttpServletRequest request, HttpServletResponse response, Model model, User user) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("productList");
        if (!StringUtils.isBlank(html)) {
            // if static page exists in cache, just return
            return html;
        }
        model.addAttribute("user", user);
        model.addAttribute("productList", productService.findProductVo());

        // if static page not exist in cache, render and return
        WebContext webContext = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("productList", webContext);
        if (!StringUtils.isBlank(html)) {
            valueOperations.set("productList", html, 60, TimeUnit.SECONDS);
        }
        return html;
    }

    @RequestMapping(value = "toDetail/{productId}")
    @ResponseBody
    public ResponseInfo toDetail(@PathVariable Long productId, User user) {
        ProductVo productVo = productService.findProductVoById(productId);
        Date startDate = productVo.getStartDate();
        Date endDate = productVo.getEndDate();
        Date currentDate = new Date();
        int countdownStatus;
        int remainSeconds;
        if (currentDate.after(endDate)) {
            // countdown buy finished
            countdownStatus = 2;
            remainSeconds = -1;
        } else if (currentDate.after(startDate) && currentDate.before(endDate)) {
            // countdown buy in progress
            countdownStatus = 1;
            remainSeconds = 0;
        } else {
            // counting down
            countdownStatus = 0;
            remainSeconds = (int) ((startDate.getTime() - currentDate.getTime()) / 1000);
        }

        ProductDetailVo productDetailVo = new ProductDetailVo();
        productDetailVo.setUser(user);
        productDetailVo.setProductVo(productVo);
        productDetailVo.setCountdownStatus(countdownStatus);
        productDetailVo.setRemainSeconds(remainSeconds);
        return ResponseInfo.success(productDetailVo);
    }

    @RequestMapping(value = "toDetail2/{productId}", produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toDetail2(HttpServletRequest request, HttpServletResponse response, Model model, @PathVariable Long productId, User user) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("productDetail");
        if (!StringUtils.isBlank(html)) {
            // if static page exists in cache, just return
            return html;
        }
        model.addAttribute("user", user);
        ProductVo productVo = productService.findProductVoById(productId);
        Date startDate = productVo.getStartDate();
        Date endDate = productVo.getEndDate();
        Date currentDate = new Date();
        int countdownStatus;
        int remainSeconds;
        if (currentDate.after(endDate)) {
            // countdown buy finished
            countdownStatus = 2;
            remainSeconds = -1;
        } else if (currentDate.after(startDate) && currentDate.before(endDate)) {
            // countdown buy in progress
            countdownStatus = 1;
            remainSeconds = 0;
        } else {
            // counting down
            countdownStatus = 0;
            remainSeconds = (int) ((startDate.getTime() - currentDate.getTime()) / 1000);
        }
        model.addAttribute("remainSeconds", remainSeconds);
        model.addAttribute("countdownStatus", countdownStatus);
        model.addAttribute("product", productVo);
        // if static page not exist in cache, render and return
        WebContext webContext = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("productDetail", webContext);
        if (!StringUtils.isBlank(html)) {
            valueOperations.set("productDetail", html, 60, TimeUnit.SECONDS);
        }
        return html;
    }
}
