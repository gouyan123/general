package org.springframework.data.mongodb;

import com.alibaba.fastjson.JSON;
import com.mongodb.Mongo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.config.MongoConfig;
import org.springframework.data.mongodb.core.MongoClientFactoryBean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.entity.Item;
import org.springframework.data.mongodb.entity.Order;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class Test01 {
    /*MongoOperations是MongoTemplate所实现的接口，不使用具体实现是一个好的做法，尤其是在注入的时候*/
    /**根据类型获取，其实mongoOperations就是 mongoTemplate对象*/
    @Autowired
    private MongoOperations mongoOperations;


    //1、计算有多少条文档
    @Test
    public void count() {
        long order = mongoOperations.getCollection("order").count();
        System.out.println(order);
    }

    //2、保存
    @Test
    public void save() {
        Order order = new Order();
        order.setCustomer("customer");
        order.setType("豪华型213");

        List itemList = new ArrayList();
        Item item = new Item();
        item.setPrice(5.5);
        item.setProduct("产品");
        item.setQuantity(5);
        itemList.add(item);
        itemList.add(item);
        order.setItemList(itemList);
        mongoOperations.save(order, "order");
    }

    /**3、查找，Order.class参数的作用，将数据库返回数据封装到 Order 对象中；*/
    @Test
    public void findById() {
        Order byId = mongoOperations.findById("5b1135c8cd403954fd5dc684", Order.class);
        System.out.println(JSON.toJSONString(byId));
    }

    @Test
    public void findAll() {
        List<Order> all = mongoOperations.findAll(Order.class);
        if (all != null && all.size() > 0){
            for (Order order : all){
                System.out.println(JSON.toJSONString(order));
            }
        }
    }


    //4、删除
    public void remove() {
        Order order = new Order();
        order.setId("1");
        mongoOperations.remove(order);
    }


}
