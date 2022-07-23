import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
/**
 * map, filter, reduce, collect 연습
 */
class Solution {
	public static void main(String args[]) throws Exception {
		List<String> names = Arrays.asList("louis", "tom", "metty", "mark", "andy", "peter", "adam", "ross", "liam");
		int q = 1;
		// 1. list element 를 모두 대문자로 변경
		System.out.println(q++);
		names.stream().map(name -> name.toUpperCase()).forEach(System.out::println);
		// 2. m로 시작하는 이름 출력 -> filter
		System.out.println(q++);
		names.stream().filter(name -> name.startsWith("m")).forEach(System.out::println);
		// 3. 특정 스트링값의 길이보다 크고, 리스트의 가장 긴 이름을 가진 엘리먼트를 출력 -> reduce
		System.out.println(q++);
		String result3 = names.stream().reduce("jack", (name1, name2) -> name1.length() >= name2.length()? name1 : name2);
		System.out.println(result3);
		// 4. 리스트의 엘리먼트를 콤마로 구분하여 출력하되, 마지막 엘리먼트에는 콤마가 없어야한다. -> collect
		System.out.println(q++);
		System.out.println(names.stream().collect(Collectors.joining(", ")));
	}
}
