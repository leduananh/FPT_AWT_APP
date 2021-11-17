package com.fpt.app;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

public class MethodSignature {
    private final Method mMethod;
    private final String mName;
    private final Class<?>[] mParameterTypes;

    public MethodSignature(Method method) {
        mMethod = method;
        mName = mMethod.getName();
        mParameterTypes = mMethod.getParameterTypes();
    }

    public Method getMethod() {
        return mMethod;
    }

    public String getName() {
        return mName;
    }

    public Class<?>[] getParameterTypes() {
        return mParameterTypes;
    }

    @Override
    public boolean equals(Object object) {
        if(this == object) {
            return true;
        }
        if(object == null) {
            return false;
        }
        if(!getClass().equals(object.getClass())) {
            return false;
        }
        MethodSignature obj = (MethodSignature) object;
        if(hashCode() != obj.hashCode()) {
            return false;
        }
        return mName.equals(obj.getName()) && Arrays
                .equals(mParameterTypes, obj.getParameterTypes());
    }

    @Override
    public int hashCode() {
        int hash = 11;
        hash = 37 * hash + Objects.hash(mName, Arrays.hashCode(mParameterTypes));
        return hash;
    }
}

