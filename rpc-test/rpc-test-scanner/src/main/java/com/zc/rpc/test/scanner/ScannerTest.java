package com.zc.rpc.test.scanner;

import com.zc.rpc.common.scanner.ClassScanner;
import com.zc.rpc.common.scanner.reference.RpcReferenceScanner;
import com.zc.rpc.common.scanner.server.RpcServiceScanner;
import org.junit.Test;

import java.util.List;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-01-17
 */
public class ScannerTest {

    private final String packageName = "com.zc.rpc.test.scanner";

    @Test
    public void testScannerClassNameList() throws Exception{
        List<String> classNameList = ClassScanner.getClassNameList(packageName);
        classNameList.forEach(System.out::println);
    }

    @Test
    public void testScannerClassNameListByRpcService() throws Exception{
        RpcServiceScanner.doScannerWithRpcServiceAnnotationFilterAndRegistryService(packageName);
    }

    @Test
    public void testScannerClassNameListByRpcReference() throws Exception{
        RpcReferenceScanner.doScannerWithRpcReferenceAnnotationFilter(packageName);
    }


}
