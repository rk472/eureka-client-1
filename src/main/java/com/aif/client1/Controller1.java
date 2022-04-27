package com.aif.client1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class Controller1 {

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    private RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/callClient1")
    public ResponseEntity<String> callFirst() throws Exception {
        return new ResponseEntity<String>( "Hello Client 1 ", HttpStatus.OK);
    }

    @GetMapping("/callClient2Via1")
    public ResponseEntity<String> callSecond() throws Exception {
        try{
            return new ResponseEntity<String>( restTemplate.getForObject(getBaseUrl() + "/callClient2OutSide", String.class), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<String>( "Error Occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String getBaseUrl() {
        ServiceInstance instance = loadBalancerClient.choose("CLIENT2");
        return instance.getUri().toString();
    }
}
