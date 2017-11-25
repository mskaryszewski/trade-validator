package com.validator.trade.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Useful only for non-managed Spring objects - mainly validator implementations.
 * 
 * Instance of usage:
 * - enriches {@link com.validator.trade.validator.CounterPartyValidator} with list of valid counterParties
 *   defined in application.properties {@link com.validator.trade.config.Configuration}
 * 
 * - enriches a {@link com.validator.trade.validator.ValueDateOnWorkingDayValidator}
 *   with {@link com.validator.trade.service.HolidayApiService} to consume REST service
 * 
 * @author Michal
 *
 */
@Component
public class ApplicationContextProvider implements ApplicationContextAware {
    private static ApplicationContext context;

    public static ApplicationContext getApplicationContext() {
        return context;
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx) {
        context = ctx;
    }
}