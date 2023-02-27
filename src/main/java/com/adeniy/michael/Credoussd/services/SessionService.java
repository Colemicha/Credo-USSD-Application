package com.adeniy.michael.Credoussd.services;

import com.adeniy.michael.Credoussd.entity.UssdSession;

public interface SessionService {
    UssdSession createUssdSession(UssdSession session);

    UssdSession get(String id);

    UssdSession update(UssdSession session);
}
