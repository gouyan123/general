package org.springframework.data.mongodb.dao;

import org.springframework.data.mongodb.entity.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface OrderRepository extends MongoRepository<Order, String>/*,OrderOperations*/ {
    /**根据customer从文档中获取Order集合，JPA能够自动创建接口实现；*/
    /**JPA自动实现自定义接口中方法条件：自定义接口继承 MongoRepository<Order, String>*/
    //@Query会接受一个JSON查询，而不是JPA查询。?0 表示第一个参数，?1 表示第二个参数，以此类推
    @Query("{'customer':?0,'type':'type'}")
    List<Order> findByCustomer(String customer);

    /**根据customer 和 type 从文档中获取Order集合*/
    List<Order> findByCustomerAndType(String customer, String type);

    /**根据customer 和 type 从文档中获取Order集合（customer 在对比的时候使用的是like 而不是equals*/
    List<Order> findByCustomerLikeAndTypeLike(String customer, String type);

}
