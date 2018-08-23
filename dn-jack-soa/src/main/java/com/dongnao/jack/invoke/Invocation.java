package com.dongnao.jack.invoke;

import com.dongnao.jack.configBean.Reference;

import java.lang.reflect.Method;

public class Invocation {
    private Method method;
    private Object[] args;
    private Reference reference;
    private Invoke invoke;
    public Invocation() {
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public Method getMethod() {
        return method;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setReference(Reference reference) {
        this.reference = reference;
    }

    public Reference getReference() {
        return reference;
    }

    public void setInvoke(Invoke invoke) {
        this.invoke = invoke;
    }

    public Invoke getInvoke() {
        return invoke;
    }
}
