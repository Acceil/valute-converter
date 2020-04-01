package org.itstep.msk.app.service;

import java.util.Map;

public interface MailService {
    void send(String email, String subject, String template, Map<String, Object> parameters) throws Exception;
}
