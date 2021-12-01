package com.toolbox.util;

@FunctionalInterface
public interface EventListener<T extends EventData> {
	void onEvent(T t);
}
