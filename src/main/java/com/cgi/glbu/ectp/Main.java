package com.cgi.glbu.ectp;

import com.cgi.glbu.ectp.model.Transaction;

import java.math.BigDecimal;

 public static void main(final String... pArgs) {
		//	Total sales for Store # 93
		System.out.println(	"Sales for Store #93 " +
				Utils.transactions.stream()
						.map(s -> Utils.jsonToPojo(s, Transaction.class))
						.filter(o -> o.isPresent())
						.map(o -> o.get())
						.filter(t -> t.getStoreNumber() == 93)
						.map(t -> t.getTotalSales())
						.reduce(BigDecimal.ZERO, (acc, value) -> /* acc = */ acc.add(value))
		);

		//	Total sales over all transactions
		System.out.println("Total Sales for all transactions: " +
				Utils.transactions.stream()
						.map(s -> Utils.jsonToPojo(s, Transaction.class))
						.filter(o -> o.isPresent())
						.map(o -> o.get())
						.map(t -> t.getTotalSales())
						.reduce(BigDecimal.ZERO, (acc, value) -> acc.add(value))
		);

		//	Total sales, grouped by Store #
				Utils.transactions.stream()
						.map(s -> Utils.jsonToPojo(s, Transaction.class))
						.filter(o -> o.isPresent())
						.map(o -> o.get())
						.collect(Collectors.groupingBy(y -> y.getStoreNumber(), Collectors.counting()))
						.entrySet()
						.stream()
						.map(t -> ("Store " + t.getKey() + " : " + t.getValue()))
						.forEach(t -> System.out.println(t));

		// Total sales for 2019, grouped by Store # StoreNumber : Sales
		final LocalDate start2019  = LocalDate.of(2019, 1, 1);
		final LocalDate end2019 = LocalDate.of(2020, 1, 1);
		System.out.println("2019 Sales grouped by Store # " +
				Utils.transactions.stream()
						.map(s -> Utils.jsonToPojo(s, Transaction.class))
						.filter(o -> o.isPresent())
						.map(o -> o.get())
						.collect(Collectors.groupingBy(t -> t.getStoreNumber())).entrySet().stream()
						.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().stream()
						.map(t -> t.getTotalSales())
						.limit(ChronoUnit.DAYS.between(start2019, end2019))
						.reduce(BigDecimal.ZERO, BigDecimal::add)))
		);

		//	Top 5 best selling Items by total revenue $$ ItemNumber : Sales (limit of 5 highest sales)
				Utils.transactions.stream()
						.map(s -> Utils.jsonToPojo(s, Transaction.class))
						.filter(o -> o.isPresent())
						.map(o -> o.get())
						.flatMap(t -> t.getItems().stream())
						.collect(Collectors.groupingBy(t -> t.getSkuNumber(), Collectors.reducing(BigDecimal.ZERO, t -> t.getUnitPrice()
								.multiply(new BigDecimal(t.getCount())), (acc, val) -> acc.add(val))))
						.entrySet().stream()
						.sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
						.limit(5)
						.forEach(n -> System.out.println(n));

		//	Total Sales Tax for 2020 - later

		//	Total Sales Tax for 2020 by Store # - later
    }
}
