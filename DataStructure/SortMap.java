//https://stackoverflow.com/questions/109383/sort-a-mapkey-value-by-values

package com.example.demo;

import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.List;
import java.util.Comparator;
import java.util.ArrayList;
//you can also use imports, for example:
//import java.util.*;

//you can write to stdout for debugging purposes, e.g.
//System.out.println("this is a debug message");

class Solution {
	public void solution(int A, int B, int C) {
		// write your code in Java SE 8
		
		String result = "";
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		map.put(65, A);
		map.put(66, B);
		map.put(67, C);
		
		//순위 매기기
		List<Integer> keySetList = new ArrayList<>(map.keySet());
		Collections.sort(keySetList, (o1, o2) -> (map.get(o2).compareTo(map.get(o1))));
		
		for (int i = 0; i < keySetList.size(); i++) {
			System.out.println(i+":"+(char)(int)keySetList.get(i));
		}
		
		Collections.sort(keySetList,new Comparator(){
			public int compare(Object o1,Object o2){
				Object v1 = map.get(o1);
				Object v2 = map.get(o2);
				
				return ((Comparable) v1).compareTo(v2);
			}
		});
		
		for (int i = 0; i < keySetList.size(); i++) {
			System.out.println(i+":"+(char)(int)keySetList.get(i));
		}
 }

 public static void main(String[] args) {
	Solution s = new Solution();
	s.solution(6, 1, 5);
  }
}
