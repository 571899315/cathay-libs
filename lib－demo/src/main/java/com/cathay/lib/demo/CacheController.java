package com.cathay.lib.demo;

import com.cathay.jvm.cache.annotation.Cached;
import com.google.common.collect.Lists;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CacheController {


    @GetMapping("/api/listPersion")
    @Cached(maxSize = 1000, expireMilliSeconds = 999999)
    public JsonResponse<List<Persion>> listPersion(String name) {
        JsonResponse<List<Persion>> listJsonResponse = new JsonResponse<List<Persion>>();
        List<Persion> list = Lists.newArrayList();

        for (int i = 0; i < 99; i++) {
            Persion persion = new Persion();
            persion.setAge(i);
            persion.setName("44" + i);
            list.add(persion);
        }
        listJsonResponse.setData(list);
        listJsonResponse.setResult(500);
        return listJsonResponse;
    }


}
