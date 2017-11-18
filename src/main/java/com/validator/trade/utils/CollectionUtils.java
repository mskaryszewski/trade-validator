package com.validator.trade.utils;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CollectionUtils {
	
	public static <T> Collection<T> concat(Collection<? extends T> a, Collection<? extends T> b) {
		return Stream
			.concat(
					a.stream(),
					b.stream())
			.collect(
					Collectors.toList());
	}
}
