package com.shopping.order_service.client;
//import org.slf4j.Logger;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

//@FeignClient(value = "inventory",url="http://localhost:8083")
public interface InventoryClient {

//    @RequestMapping(method = RequestMethod.GET , value = "/api/inventory")
    @GetExchange("/api/inventory")
    @CircuitBreaker(name= "inventory",fallbackMethod = "fallbackMethod")
    @Retry(name= "inventory")
    boolean isInStock(@RequestParam String skuCode, @RequestParam Integer quantity);

    default boolean fallbackMethod(String code,Integer quantity,Throwable throwable){
//        log.info("Cannot get Inventory for skucode {}, failure reason: {}",code,throwable.getMessage());
        return false;
    }
}
