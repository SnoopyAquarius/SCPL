package web.sontan.action;

import cn.hutool.core.util.IdUtil;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import web.sontan.model.Goods;
import web.sontan.model.Order;
import web.sontan.model.User;
import web.sontan.service.ShopService;
import web.sontan.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.List;

/**
 * Created by Wang on 2019/5/17.
 */
@Controller("shopAction")
@Scope("prototype")
public class ShopAction extends ActionSupport implements SessionAware {
    @Resource
    private ShopService ShopService;
    @Resource
    private UserService userService;
    private Map<String, Object> session;

    private String tip;
    private Integer shopError = 0;
    private Goods goods;

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public Integer getShopError() {
        return shopError;
    }

    public void setShopError(Integer shopError) {
        this.shopError = shopError;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    private Order order;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    private List<Order> orderList;

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

    private List<Goods> goodsList;

    public List<Goods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<Goods> goodsList) {
        this.goodsList = goodsList;
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    /*
    * 跳转页面action
    * */
    public String viewIndex() {
        return SUCCESS;
    }

    public String viewAdd() {
        return SUCCESS;
    }


    /**
     * 上传商品信息
     */
    public String addGoods() {
        this.goods.setGoodsId(IdUtil.simpleUUID());// 设置post_id
        User user = (User) session.get("user");
        this.goods.setSeller(user);
        ShopService.addGoods(goods);
        return "addSuccerss";
    }

    /*
    * 查找所有的商品 (未完全)
    * */
    public String queryGoods() {
        goodsList = ShopService.queryGoods();
        return "json";
    }

    /*
    *  跳转到单个商品详情页面处理
    * */
    public String getGoodsInfo() {
        HttpServletRequest request = ServletActionContext.getRequest();
        String curGoddsId = request.getParameter("goods.goodsId");
        if (curGoddsId != null) {//防止无传值id
//            System.out.println("ceshi---"+goods.getGoods()+"-----");
            goods = ShopService.findGoodsById(goods);
        }
        return SUCCESS;
    }

    public String buyGoods() {
    /*    HttpServletRequest request = ServletActionContext.getRequest();
        String curGoddsId=request.getParameter("goods.goodsId");*/
        goods = ShopService.findGoodsById(goods);//得到当前商品
        User buyer = new User();
        User seller = new User();
        buyer = (User) session.get("user");
        if (goods.getSeller().getUserId().equals(buyer.getUserId())) {//防止自己购买自己的商品
            tip = "无法购买自己的商品";
            shopError = -2;
        } else {
            if (order == null) {
                Order order = new Order();
                order.setOrderId(IdUtil.simpleUUID());//设置Id
                order.setGoods(goods);//放入商品Id
                order.setBuyer(buyer);
                seller = userService.findById(goods.getSeller().getUserId());
//                System.out.println(seller.getUserId().equals(goods.getSellerId()));
                order.setSeller(seller);
                order.setOrderStatus(1);

                boolean buyFlag = ShopService.createOrder(order);
                if (buyFlag == false) {
                    tip = "无法创建订单,购买失败";
                    shopError = -3;
                } else {
                    tip = "购买成功";
                    shopError = 1;
                }
            }
        }
        return "json";
    }

    public String findGoodsByType() {
        // System.out.println(goods.getGoodsType());
        goodsList = ShopService.findGoodsByType(goods.getGoodsType());
        System.out.println(goodsList.size());
        if (goodsList.size() == 0) {
            shopError = -4;
        }
        return "json";
    }

    public String findGoodsByName() {
        goodsList = ShopService.findGoodsByName(goods.getGoodsName());
        System.out.println(goodsList.size());
        if (goodsList.size() == 0) {
            shopError = -4;
        }
        return "json";
    }

    public String checkOrder() {
        User user = (User) session.get("user");
        orderList = ShopService.findUserSellOrder(user.getUserId());
        return SUCCESS;
    }
}