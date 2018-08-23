package org.springframework.data.mongodb.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoClientFactoryBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
/**表示该类为配置类*/
@Configuration
/**启用MongoDB的Repository功能，会对其Repositories自动扫描*/
@EnableMongoRepositories(basePackages = "org.springframework.data.mongodb",repositoryImplementationPostfix = "Impl")
/**获取该包下所有 类全名，获取该包下所有 反射类对象*/
@ComponentScan(basePackages = "org.springframework.data.mongodb")
/** *.properties 文件路径*/
@PropertySource("classpath:mongo.properties")
public class MongoConfig {
    /** *FactoryBean 实现 FactoryBean<T> 接口，实现 public T getObject(){return new T();}
     * ioc容器获取 *FactoryBean 实例时 即 context.getBean("*FactoryBean")，实际返回的是
     * *FactoryBean里面的 T 对象*/
    @Bean(name = "mongo")
    /**Environment env 表示从 context 容器中获取 env 对象，该对象spring容器自动创建，并且封装
     * @PropertySource("classpath:mongo.properties") 里面的内容，通过 env.getProperty(key)取得*/
    public MongoClientFactoryBean mongoClientFactoryBean(Environment env) {
        MongoClientOptions.Builder builder = MongoClientOptions.builder();
        MongoClientOptions build = builder.build();

        MongoCredential credential = MongoCredential.createCredential(
                env.getProperty("mongo.user", String.class),
                env.getProperty("mongo.database", String.class),
                env.getProperty("mongo.password", String.class).toCharArray());
        MongoClientFactoryBean mongoClientFactoryBean = new MongoClientFactoryBean();
        mongoClientFactoryBean.setHost(env.getProperty("mongo.host", String.class));
        mongoClientFactoryBean.setPort(env.getProperty("mongo.port", Integer.class));
        mongoClientFactoryBean.setCredentials(new MongoCredential[]{credential});
        mongoClientFactoryBean.setMongoClientOptions(build);
        return mongoClientFactoryBean;
    }

    /* 2.0 之后不支持这种构造器了 */
    @Bean(name = "mongoTemplate")
    /**@Bean注释的方法参数从哪里来？ 答：从 IOC 容器中获取*/
    public MongoTemplate mongoTemplate(Mongo mongo, Environment env) {
        return new MongoTemplate(mongo, env.getProperty("mongo.database", String.class));
    }
}
