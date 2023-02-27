package com.adeniy.michael.Credoussd.controller;

import com.adeniy.michael.Credoussd.entity.Menu;
import com.adeniy.michael.Credoussd.services.MenuService;
import com.adeniy.michael.Credoussd.services.UssdRoutingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class DemoController {

    private final MenuService menuService;

    private final UssdRoutingService ussdRoutingService;


    @GetMapping(path = "menus")
    public Map<String, Menu> menusLoad() throws IOException {
        return menuService.loadMenus();
    }

    @GetMapping(path = "")
    public String index() {
        return "Your have reached us";
    }


    @PostMapping(path = "ussd")
    public String ussdIngress(@RequestParam String sessionId, @RequestParam String serviceCode,
                              @RequestParam String phoneNumber, @RequestParam String text) {
        try {
            return ussdRoutingService.menuLevelRouter(sessionId, serviceCode, phoneNumber, text);
        } catch (IOException e) {
            return "END " + e.getMessage();
        }
    }
}
