package com.visionvera.common.validator.util;

import com.visionvera.remoteservice.exception.MyException;
import java.util.Locale;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import org.hibernate.validator.HibernateValidator;

/**
 * hibernate-validator校验工具类
 *
 * 参考文档：http://docs.jboss.org/hibernate/validator/5.4/reference/en-US/html_single/
 *
 * Created by EricShen on 2017/09/13
 */
public class ValidateUtil {

  private static Validator validator;

  static {
    Locale.setDefault(Locale.SIMPLIFIED_CHINESE); // specify language
    validator = Validation.byProvider(HibernateValidator.class).configure().failFast(true)
        .buildValidatorFactory().getValidator();
  }

  /**
   * 校验对象
   *
   * @param object 待校验对象
   * @param groups 待校验的组
   * @throws MyException 校验不通过，则抛出MyException异常
   */
  public static void validate(Object object, Class<?>... groups)
      throws MyException {
    Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
    if (!constraintViolations.isEmpty()) {
      throw new MyException(constraintViolations.iterator().next().getMessage());
    }
  }

}
