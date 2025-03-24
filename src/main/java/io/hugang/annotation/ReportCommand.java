package io.hugang.annotation;

import java.lang.annotation.*;

/**
 * ReportCommand annotation
 * <p>
 * This annotation is used to mark a class as a web command.
 * <br>If exist web command, it will create a webdriver before execute the commands.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ReportCommand {

    /**
     * The value of the annotation.
     * <p>
     * The default value is "after".
     * <br>It means the report will be created after the command.
     * <br>If the value is "before", the report will be created before the command.
     */
    String value() default "after";
}
