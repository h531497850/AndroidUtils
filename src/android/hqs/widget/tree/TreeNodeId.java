package android.hqs.widget.tree;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解，在运行时可见{@value #annotationType()}，
 * 这是一个{@link #Annotation}类的注解标签。
 * @author hqs2063594
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TreeNodeId {

	Class<?> type();
	
}
