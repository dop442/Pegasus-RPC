package com.zc.rpc.protocol.meta;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;


/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-03-07
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceMethodMeta implements Serializable {
    private static final long serialVersionUID = 8542908689444673042L;

    private String methodName;

    private int parametersLength;

    private List<String> parametersType;

    private String returnType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceMethodMeta that = (ServiceMethodMeta) o;
        return parametersLength == that.parametersLength && Objects.equals(methodName, that.methodName) && Objects.equals(parametersType, that.parametersType) && Objects.equals(returnType, that.returnType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(methodName, parametersLength, parametersType, returnType);
    }
}
