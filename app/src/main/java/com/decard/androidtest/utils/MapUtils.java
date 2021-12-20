package com.decard.androidtest.utils;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Time:2021/12/20
 * Author:SunnyLuna
 */
class MapUtils {

	private class LRUMap<K, V> extends LinkedHashMap<K, V> {
		private int maxSize;

		public LRUMap(int maxSize) {
			//默认初始长度为16.负载因子为0.75   优化：减少扩容
			super(16, 0.75f, true);
			this.maxSize = maxSize;
		}

		@Override
		protected boolean removeEldestEntry(Entry<K, V> eldest) {
			return size() > this.maxSize;
		}
	}


	/*
	  默认是升序
	 */
	private void compare(){
		Map<String,String> treeMap =new TreeMap<String,String>(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				//自定义为降序的
				return o2.compareTo(o1);
			}
		});
	}
}
