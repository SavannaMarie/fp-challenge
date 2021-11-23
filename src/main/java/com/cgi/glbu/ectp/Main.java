package com.cgi.glbu.ectp;

import com.cgi.glbu.ectp.model.Transaction;

import java.math.BigDecimal;

public class Main {

    public static void main(final String... pArgs) {
		//	Total sales for Store # 93
		System.out.println(	"Sales for Store #93 " +
								Utils.transactions.stream()
								.map(s -> Utils.jsonToPojo(s, Transaction.class))
								.filter(o -> o.isPresent())
								.map(o -> o.get())
								.filter(t -> t.getStoreNumber() == 93)
								.map(t -> t.getTotalSales())
								.reduce(BigDecimal.ZERO, (acc, value) -> /* acc = */ acc.add(value))	);

		//	Total sales over all transactions

		//	Total sales, grouped by Store #

		//	Total sales for 2019, grouped by Store # StoreNumber : Sales

		//	Top 5 best selling Items by total revenue $$ ItemNumber : Sales (limit of 5 highest sales)

		//	Total Sales Tax for 2020 - later

		//	Total Sales Tax for 2020 by Store # - later
    }
}
