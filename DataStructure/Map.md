# MAP
key-value의 형태로 어떠한 형태의 자료를 통해 그 자료가 의미하는 값을 알 수 있는 자료형을 의미한다.  
key는 중복 없이 유일하고, value는 다른 key의 value와 중복되어도 상관없다.
Map의 Key들은 순서 없이 존재한다.

# HashMap
Map의 Key를 Hash형태로 관리하는 Map  
Map형태여도 리스트로 불러올 수 있는데, lambda식이나 for문의 **forEach**를 이용하여 가능하다.
```
import java.io.*;
import java.util.*;
class Main {
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		HashMap<String, Integer> hashMap = new HashMap<>();
		String cur = br.readLine();
    
		while(!".".equals(cur)) {
			int cnt = 0;

			if(hashMap.containsKey(cur)) cnt = hashMap.get(cur);
			
			cnt++;
			hashMap.put(cur, cnt);		
			cur = br.readLine();
		}
	
		hashMap.forEach((key, value) ->{
			System.out.println("Lambda ver. "+key + "  : " + value);
		});
		
		for(Map.Entry entry : hashMap.entrySet()) {
			System.out.println("forEach ver. " + entry.getKey() + " : " + entry.getValue());
		}
	}	
}
```
> Result
```
Lambda ver. 곰  : 2
Lambda ver. 치타  : 3
Lambda ver. 토끼  : 6
Lambda ver. 햄스터  : 3
Lambda ver. 돌고래  : 3
Lambda ver. 여우  : 5
Lambda ver. 강아지  : 10

forEach ver. 곰 : 2
forEach ver. 치타 : 3
forEach ver. 토끼 : 6
forEach ver. 햄스터 : 3
forEach ver. 돌고래 : 3
forEach ver. 여우 : 5
forEach ver. 강아지 : 10
```
# LinkedHashMap
HashMap형태에 값이 입력된 순서대로 연결한 형태의 맵.
## public LinkedHashMap(int initialCapacity, float loadFactor, boolean accessOrder)
map의 key가 들어온 순서대로 정렬되도록 하는 생성자 -> 값이 변경되면 그 값이 최신이 되어 제일 마지막에 출력된다.
```
import java.io.*;
import java.util.*;
class Main {
	public static void main(String[] args) throws Exception {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		HashMap<String, Integer> hashMap = new HashMap<>();
		LinkedHashMap<String, Integer> linkedHashMap = new LinkedHashMap<>();
		LinkedHashMap<String, Integer> linkedHashMapWithaccessOrder = new LinkedHashMap<>(16, 0.75f, true);
		//public LinkedHashMap(int initialCapacity, float loadFactor, boolean accessOrder)
    
		String cur = br.readLine();
		while(!".".equals(cur)) {
			
			int cnt = 0;
			if(hashMap.containsKey(cur)) cnt = hashMap.get(cur);
			if(linkedHashMap.containsKey(cur)) cnt = linkedHashMap.get(cur);
			if(linkedHashMapWithaccessOrder.containsKey(cur)) cnt = linkedHashMapWithaccessOrder.get(cur);
			
			cnt++;
			hashMap.put(cur, cnt);
			linkedHashMap.put(cur, cnt);
			linkedHashMapWithaccessOrder.put(cur, cnt);
			
			cur = br.readLine();
		}
		
		System.out.println("hashMap ver. 				"+ hashMap.toString());
		System.out.println("linkedHashMap ver. 			"+ linkedHashMap.toString());
		System.out.println("linkedHashMapWithaccessOrder ver.	"+ linkedHashMapWithaccessOrder.toString());

	}	
}
```
> Input
```
~
토끼
강아지
여우
강아지
.
```
> Output
```
hashMap ver. 				{곰=2, 치타=3, 토끼=6, 햄스터=3, 돌고래=3, 여우=5, 강아지=10}
linkedHashMap ver. 			{돌고래=3, 여우=5, 곰=2, 강아지=10, 토끼=6, 햄스터=3, 치타=3}
linkedHashMapWithaccessOrder ver.	{곰=2, 돌고래=3, 치타=3, 햄스터=3, 토끼=6, 여우=5, 강아지=10}
```
**linkedHashMapWithaccessOrder ver.** 의 경우 제일 늦게 입력된 **토끼 여우 강아지** 순으로 출력됨을 알 수 있고,  
토끼 뒤에 강아지가 입력되었지만 제일 마지막에 다시 강아지가 입력되며 강아지가 제일 마지막에 출력된다.
# MultiValueMap
- Map 은 Key는 중복아니고, Value는 중복이 혀용되는데 Value가 리스트 형식으로 되어있어 Key가 중복해서 들어갈 있는 자료형
- Spring util에서 제공한다.

