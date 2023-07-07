package com.jt.service;

import com.jt.pojo.User;
import com.jt.util.ObjectMapperUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class HttpClientServiceImpl implements HttpClientService {
    /*
    * jt-web 需要访问jt-sso获取数据
    * http://sso.jt.com/userfindUserList
     * 1.实例化httpClient
     * 2.定义URL请求
     * 3.定义请求对象
     * 4.发起httpclient
     * 5.获取响应结果 200，404，500，504，502
     * 6.获取结果，进行后续操作*/
    @Override
    public List<User> findUserlist() {
        List<User> userlist = new ArrayList<>();
        String url = "http://sso.jt.com/user/findUserList";
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode()==200){

                String json = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");
                userlist = ObjectMapperUtil.toObject(json,userlist.getClass());
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return userlist;
    }
}
