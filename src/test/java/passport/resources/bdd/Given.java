package passport.resources.bdd;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({
        ElementType.TYPE, ElementType.METHOD
})
public @interface Given { String value(); }
