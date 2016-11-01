package zzh.project.stocksystem.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class LruLinkedHashMap<K, V> extends LinkedHashMap<K, V> {
	private int capacity;
	private static final long serialVersionUID = 1L;

	public LruLinkedHashMap(int capacity) {
		super(16, 0.75f, true);
		this.capacity = capacity;
	}

	@Override
	public boolean removeEldestEntry(Map.Entry<K, V> eldest) {
		return size() > capacity;
	}
}