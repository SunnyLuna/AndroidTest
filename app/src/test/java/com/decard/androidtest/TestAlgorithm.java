package com.decard.androidtest;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * @author ZJ
 * create at 2021/11/3 11:44
 */

public class TestAlgorithm {


	@Test
	public void test() {
		int[] numbs = {1, 2, 3, 4, 5, 6};
		print("begin");
		int result = search(numbs, 8);
		print(result + "");
		assertEquals(4, 2 + 2);
	}

	public void print(String msg) {
		System.out.println(System.currentTimeMillis() + msg);
	}

	public int[] twoSum(int[] nums, int target) {
		Map<Integer, Integer> map = new HashMap<>();
		for (int i = 0; i < nums.length; i++) {
			if (map.containsKey(target - nums[i])) {
				print(map.get(target - nums[i]).toString());
				print("" + i);
				return new int[]{map.get(target - nums[i]), i};
			}
			map.put(nums[i], i);
		}
		throw new IllegalArgumentException("No two sum solution");
	}


	public int search(int[] nums, int target) {
		int low = 0, high = nums.length - 1;
		while (low <= high) {
			int mid = (high - low) / 2 + low;
			int num = nums[mid];
			if (num == target) {
				return mid;
			} else if (num > target) {
				high = mid - 1;
			} else {
				low = mid + 1;
			}
		}
		return -1;
	}
}
