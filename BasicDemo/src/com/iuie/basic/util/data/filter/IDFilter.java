package com.iuie.basic.util.data.filter;

import static org.apache.commons.lang3.StringUtils.removeEnd;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.tuple.Pair;
/**
 * 数据过滤接口
 * @author liu.jie
 * @since 2019年9月25日
 */
public interface IDFilter {
	
	public static final List<Pair<String, Class<?>>> OPAIRS = Arrays.asList(
			Pair.of("_gte", GteFilter.class), Pair.of("_gt", GtFilter.class));
	
	<T> Predicate<T> of();
	
	static <T> Predicate<T> of(Map.Entry<String, Object> ent) {
		Optional<Pair<String, Class<?>>> op = OPAIRS.stream()
				.filter(p -> ent.getKey().toLowerCase().endsWith(p.getLeft())).findFirst();
		if(op.isPresent()) {
			try {
				Class[] parameterTypes = {Map.Entry.class};
				Constructor constructor = op.get().getRight().getConstructor(parameterTypes);
				return ((IDFilter) constructor.newInstance(ent)).of();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			return new EqualsFilter(ent).of();
		}
		return null;
	}
	
	class GteFilter extends DefFilter {
		public GteFilter(Map.Entry<String, Object> ent) {
			setFilter(removeEnd(ent.getKey(), "_gte"), ent.getValue());
		}

		@Override
		public <T> Predicate<T> of() {
			return new Predicate<T>() {
				@Override
				public boolean test(T t) {
					Object v = getObjectVal(t);
					if(value instanceof Integer) {
						return NumberUtils.toDouble(v.toString()) >= (Integer) value;
					} else if(value instanceof Double) {
						return NumberUtils.toDouble(v.toString()) >= (Double) value;
					} else if(value instanceof String) {
						return NumberUtils.toDouble(v.toString()) >= NumberUtils.toDouble(value.toString());
					}
					return false;
				}
			};
		}
	}
	class GtFilter extends DefFilter {
		public GtFilter(Map.Entry<String, Object> ent) {
			setFilter(removeEnd(ent.getKey(), "_gt"), ent.getValue());
		}

		@Override
		public <T> Predicate<T> of() {
			return new Predicate<T>() {
				@Override
				public boolean test(T t) {
					Object v = getObjectVal(t);
					if(value instanceof Integer) {
						return NumberUtils.toDouble(v.toString()) > (Integer) value;
					} else if(value instanceof Double) {
						return NumberUtils.toDouble(v.toString()) > (Double) value;
					} else if(value instanceof String) {
						return NumberUtils.toDouble(v.toString()) > NumberUtils.toDouble(value.toString());
					}
					return false;
				}
			};
		}
	}
	class EqualsFilter extends DefFilter {
		public EqualsFilter(Map.Entry<String, Object> ent) {
			setFilter(ent.getKey(), ent.getValue());
		}

		@Override
		public <T> Predicate<T> of() {
			return new Predicate<T>() {
				@Override
				public boolean test(T t) {
					Object v = getObjectVal(t);
					if(v == null) {
						return false;
					}
					return v.equals(value);
				}
			};
		}
	}
}
