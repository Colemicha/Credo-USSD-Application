package com.adeniy.michael.Credoussd.services;

import com.adeniy.michael.Credoussd.entity.Menu;
import com.adeniy.michael.Credoussd.entity.MenuOption;
import com.adeniy.michael.Credoussd.entity.UssdSession;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UssdRoutingService {

    private final MenuService menuService;
    private final SessionService sessionService;


    public String menuLevelRouter(String sessionId, String serviceCode, String phoneNumber, String text)
            throws IOException {
        Map<String, Menu> menus = menuService.loadMenus();
        UssdSession session = checkAndSetSession(sessionId, serviceCode, phoneNumber, text);
        return text.length() > 0 ? getNextMenuItem(session, menus) : menus.get(session.getCurrentMenuLevel()).getText();
    }

    public String getNextMenuItem(UssdSession session, Map<String, Menu> menus) throws IOException {
        String[] levels = session.getText().split("\\*");
        String lastValue = levels[levels.length - 1];
        Menu menuLevel = menus.get(session.getCurrentMenuLevel());

        if (Integer.parseInt(lastValue) <= menuLevel.getMaxSelections()) {
            MenuOption menuOption = menuLevel.getMenuOptions().get(Integer.parseInt(lastValue) - 1);
            return processMenuOption(session, menuOption);
        }

        return "CON ";
    }


    public String getMenu(String menuLevel) throws IOException {
        return menuService.loadMenus().get(menuLevel).getText();
    }

    public String processMenuOption(UssdSession session, MenuOption menuOption) throws IOException {
        switch (menuOption.getType()) {
            case "response":
                return processMenuOptionResponses(menuOption, session.getId());
            case "level":
                updateSessionMenuLevel(session, menuOption.getNextMenuLevel());
                return getMenu(menuOption.getNextMenuLevel());
            default:
                return "CON ";
        }
    }

    public String processMenuOptionResponses(MenuOption menuOption, String id) {
        String response = menuOption.getResponse();
        UssdSession session = sessionService.get(id);
        Map<String, String> variablesMap = new HashMap<>();

        switch (menuOption.getAction()) {
            case PROCESS_ACC_BALANCE:
                variablesMap.put("account_balance", session.getAccount().getAccountNumber());
                break;
            case PROCESS_ACC_NUMBER:
                variablesMap.put("account_number", session.getAccount().getAccountNumber());
                break;
            case PROCESS_ACC_PHONE_NUMBER:
                variablesMap.put("phone_number", session.getPhoneNumber());
                break;
        }

        response = replaceVariable(variablesMap, response);
        return response;
    }


    public String replaceVariable(Map<String, String> variablesMap, String response) {
        return new StringSubstitutor(variablesMap).replace(response);
    }

    public UssdSession updateSessionMenuLevel(UssdSession session, String menuLevel) {
        session.setPreviousMenuLevel(session.getCurrentMenuLevel());
        session.setCurrentMenuLevel(menuLevel);
        return sessionService.update(session);
    }

    public UssdSession checkAndSetSession(String sessionId, String serviceCode, String phoneNumber, String text) {
        UssdSession session = sessionService.get(sessionId);

        if (session != null) {
            session.setText(text);
            return sessionService.update(session);
        }

        session = new UssdSession();
        session.setCurrentMenuLevel("1");
        session.setPreviousMenuLevel("1");
        session.setPhoneNumber(phoneNumber);
        session.setServiceCode(serviceCode);
        session.setText(text);

        return sessionService.createUssdSession(session);
    }
}
