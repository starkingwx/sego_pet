package com.richitec.util;

import java.util.List;

public class ArrayUtil {

	public static int[] convertIntegerListToIntArray(List<Integer> list) {
		Integer[] tmp = new Integer[list.size()];
		tmp = list.toArray(tmp);
		int[] intArray = new int[tmp.length];
		for (int i = 0; i < tmp.length; i++) {
			intArray[i] = tmp[i];
		}
		return intArray;
	}
}
