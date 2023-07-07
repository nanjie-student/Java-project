package com.jt;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class TestHttpClient {
    /*
     * 1.实例化httpClient
     * 2.定义URL请求
     * 3.定义请求对象
     * 4.发起httpclient
     * 5.获取响应结果 200，404，500，504，502
     * 6.获取结果，进行后续操作*/
    @Test
    public void testGet() throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        String url = "http://www.baidu.com";
        HttpGet httpGet = new HttpGet(url);
        HttpResponse httpResponse = httpClient.execute(httpGet);//异常两种处理方式。抛异常或者try catch
        if(httpResponse.getStatusLine().getStatusCode() == 200) {
            //表示请求一切正常
            HttpEntity httpEntity = httpResponse.getEntity();
            String result = EntityUtils.toString(httpEntity,"UTF-8");
        }else {
            //表示请求有误
            System.out.println("请求结果有误");
        }
    }
}
