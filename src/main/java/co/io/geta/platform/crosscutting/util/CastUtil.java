package co.io.geta.platform.crosscutting.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Data
@Getter
@Setter
public class CastUtil {

	/**
	 * 
	 * @param <T>   Object generic
	 * @param clazz Class in which you want to convert Generic items
	 * @param c     Generic collection that contains the information to be processed
	 * @return List<T> List with items cast in the specified class
	 */
	public <T> List<T> castList(Class<? extends T> clazz, Collection<?> c) {
		List<T> r = new ArrayList<>(c.size());
		for (Object o : c)
			r.add(clazz.cast(o));
		return r;
	}
}
