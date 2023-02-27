package com.adeniy.michael.Credoussd.services;

import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
@AllArgsConstructor
public class SmsService {

    private String accessKey = "Ak2NPcmNEq4p3GCfK2Ecv6tQA68nslopTnnNZhF91XSz5NgdrYylS855rbc1";
    private String baseUrl = "https://www.bulksmsnigeria.com/api/v1/sms/create";
    private final RestTemplate restTemplate;


    public  String sendSms(String recipient, String message) {

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        String connection=baseUrl+"?api_token="+accessKey+"&from=BulkSMS.ng&to="+recipient+"&body="+message+"&dnd=2";

        ResponseEntity<String> response= restTemplate.exchange(connection, HttpMethod.GET, entity, String.class);
        if(response.getStatusCode().is2xxSuccessful()){
            return "Message sent";
        }else{
            return "Unable to to send Message";
        }
    }

}
