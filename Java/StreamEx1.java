import java.util.*;
/**
 * 모던 자바 인 액션 책에 있는 스트림의 기초 문제 풀이를 통해 연습한다.
 * 
 * 클래스를 정의하고 풀이!
 */
class Solution {
	public static void main(String args[]) throws Exception {
		Trader matty = new Trader("Matty", "Cambridge");
		Trader adam = new Trader("Adam", "Milan");
		Trader ross = new Trader("Ross", "Cambridge");
		Trader george = new Trader("George", "Cambridge");

		List<Transaction> transactions = Arrays.asList(
				new Transaction(matty, 2012, 1000),
				new Transaction(matty, 2011, 400), 
				new Transaction(adam, 2012, 710), 
				new Transaction(adam, 2012, 700),
				new Transaction(ross, 2012, 950), 
				new Transaction(george, 2011, 300)
		);
		
		int no = 1;
		// 1. 2011년에 일어난 모든 트랜잭션을 찾아 값(value)을 오름차순으로 정리
		System.out.println("A" + (no++) + " : ");
		List<Transaction> answer1 = transactions.stream().filter(transaction -> transaction.getYear() == 2011)
				.sorted(Comparator.comparing(Transaction::getValue)).collect(Collectors.toList());
		for (Transaction t : answer1)
			System.out.println(t.toString());
		
		// 2. 거래자가 근무하는 모든 도시를 중복없이 나열하시오
		System.out.print("A" + (no++) + " : ");
		List<String> answer2 = transactions.stream().map(Transaction::getTrader).map(Trader::getCity).distinct()
				.collect(Collectors.toList());
		System.out.println(answer2);
		
		// 3.케임브리지에서 근무하는 모든 거래자를 찾아서 이름순으로 정렬하시오
		System.out.println("A" + (no++) + " : ");
		List<Trader> answer3 = transactions.stream()
				.filter(transaction -> transaction.getTrader().getCity().equals("Cambridge"))
				.map(Transaction::getTrader).distinct().collect(Collectors.toList());
		for (Trader t : answer3)
			System.out.println(t.toString());
		
		// 4. 모든 거래자의 이름을 알파벳순으로 정리해서 반환하시오
		System.out.println("A" + (no++) + " : ");
		List<String> answer4 = transactions.stream().map(Transaction::getTrader).map(Trader::getName).distinct()
				.sorted().collect(Collectors.toList());
		for (String t : answer4)
			System.out.println(t.toString());

		// 5. 밀라노에 거래자가 있는가?
		System.out.print("A" + (no++) + " : ");
		boolean answer5 = transactions.stream()
				.anyMatch(transaction -> transaction.getTrader().getCity().equals("Milan"));
		System.out.println(answer5);

		// 6. 케임브리지에 거주하는 거래자의 모든 트랜잭션값을 출력하시오
		System.out.println("A" + (no++) + " : ");
		List<Transaction> answer6 = transactions.stream()
				.filter(transaction -> transaction.getTrader().getCity().equals("Cambridge"))
				.collect(Collectors.toList());
		for (Transaction t : answer6)
			System.out.println(t.toString());

		// 7. 전체 트랜잭션 중 최댓값은 얼마인가?
		System.out.print("A" + (no++) + " : ");
		Optional<Integer> answer7 = transactions.stream().map(Transaction::getValue).reduce(Integer::max);
		System.out.println(answer7);

		// 8. 전체 트랜잭션 중 최솟값은 얼마인가?
		System.out.print("A" + (no++) + " : ");
		Optional<Integer> answer8 = transactions.stream().map(Transaction::getValue).reduce(Integer::min);
		System.out.println(answer8.get());
	}

	public static class Trader {
		private final String name;
		private final String city;

		public Trader(String name, String city) {
			this.name = name;
			this.city = city;
		}

		public String getName() {
			return name;
		}

		public String getCity() {
			return city;
		}

		@Override
		public String toString() {
			return "Trader [name=" + name + ", city=" + city + "]";
		}

	}

	public static class Transaction {
		private final Trader trader;
		private final int year;
		private final int value;

		public Transaction(Trader trader, int year, int value) {
			this.trader = trader;
			this.year = year;
			this.value = value;
		}

		public Trader getTrader() {
			return trader;
		}

		public int getYear() {
			return year;
		}

		public int getValue() {
			return value;
		}

		@Override
		public String toString() {
			return "Transaction [trader=" + trader.getName() + ", year=" + year + ", value=" + value + "]";
		}

	}
}
