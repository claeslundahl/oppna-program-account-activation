package se.vgregion.activation.domain.validation;

import org.apache.commons.lang.StringUtils;
import se.vgregion.activation.domain.form.PasswordFormBean;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CheckPasswordValidator implements ConstraintValidator<CheckPassword, PasswordFormBean> {
    @Override
    public void initialize(CheckPassword checkPassword) {
    }

    @Override
    public boolean isValid(PasswordFormBean passwordFormBean, ConstraintValidatorContext context) {
        System.out.println("jsr-303-validation");
        if (passwordFormBean  == null) return false;

        if (StringUtils.isBlank(passwordFormBean.getVgrId())) return false;
        if (StringUtils.isBlank(passwordFormBean.getPassword())) return false;

        if (!passwordFormBean.getPassword().equals(passwordFormBean.getPasswordCheck())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("{vgr.accoutactivation.password}")
                    .addNode("passwordCheck")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
