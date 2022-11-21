package kg.neobis.rentit.validator;

import lombok.experimental.UtilityClass;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class PhoneNumberValidator {

    public boolean isPhoneNumberValid(String phoneStr) {
        Pattern p = Pattern.compile("^\\+(?:[0-9] ?){6,14}[0-9]$");

        Matcher m = p.matcher(phoneStr);
        return (m.find() && m.group().equals(phoneStr));
    }
}
