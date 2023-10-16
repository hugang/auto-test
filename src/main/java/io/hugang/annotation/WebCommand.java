package io.hugang.annotation;

import java.lang.annotation.*;

/**
 * WebCommand annotation
 * <p>
 * This annotation is used to mark a class as a web command.
 * <br>If exist web command, it will create a webdriver before execute the commands.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WebCommand {
}
