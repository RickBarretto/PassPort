package passport.resources.bdd;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Repeatable(AndContext.class)
public @interface And { String value(); }
