package cronutils.validation;

import cronutils.model.CronType;
import cronutils.model.definition.CronDefinition;
import cronutils.model.definition.CronDefinitionBuilder;
import cronutils.parser.CronParser;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CronValidator implements ConstraintValidator<Cron, String> {

    private CronType type;

    @Override
    public void initialize(Cron constraintAnnotation) {
        this.type = constraintAnnotation.type();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        CronDefinition cronDefinition = CronDefinitionBuilder.instanceDefinitionFor(type);
        CronParser cronParser = new CronParser(cronDefinition);
        try {
            cronParser.parse(value).validate();
            return true;
        } catch (IllegalArgumentException e) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage()).addConstraintViolation();
            return false;
        }
    }
}
