package com.siteminder.webmail.model.validator;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom validator for list of email addresses input
 * Check each email address is in valid format, Also check the email address is NOT empty
 * as Hibernate {@link EmailValidator} does not validate empty strings
 */
public class EmailListValidator implements ConstraintValidator<EmailListConstraint, List<String>> {

    private final Logger logger = LoggerFactory.getLogger(EmailListValidator.class);

    private EmailValidator emailValidator;

    @Override
    public void initialize(EmailListConstraint emailListConstraint) {
        emailValidator = new EmailValidator();
    }

    @Override
    public boolean isValid(List<String> emailList, ConstraintValidatorContext constraintValidatorContext) {
        List<String> invalidList = new ArrayList<>();
        emailList.forEach((emailAddress) -> {
            boolean recordReslt =
                !StringUtils.isEmpty(emailAddress) && emailValidator.isValid(emailAddress, constraintValidatorContext);
            if (!recordReslt) {
                invalidList.add(emailAddress);
            }
        });

        if (!CollectionUtils.isEmpty(invalidList)) {
            logger.debug("Invalid emails in request : " + String.join(", ", invalidList));
            return false;
        } else {
            return true;
        }
    }
}
