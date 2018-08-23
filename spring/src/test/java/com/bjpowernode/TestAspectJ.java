package com.bjpowernode;

import com.bjpowernode.aop.aspectJ.ISomeService;
import com.bjpowernode.aop.aspectJ.PasswordException;
import com.bjpowernode.aop.introduction.UserException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = "classpath:applicationContext-aspectJ.xml")
public class TestAspectJ {
    @Autowired
    @Qualifier(value = "someService")
    private ISomeService someService;

    @Test
    public void test01() throws PasswordException, UserException {
        this.someService.doFirst();
        System.out.println("========================");
        String result02 = this.someService.doSecond();
        System.out.println("========================result02" + result02);
        String result03 = this.someService.doThird();
        System.out.println("========================result03" + result03);
        this.someService.login("beijing","beijing");
    }
}
