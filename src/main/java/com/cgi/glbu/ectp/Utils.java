package com.cgi.glbu.ectp;

import com.cgi.glbu.ectp.model.Transaction;
import com.cgi.glbu.ectp.model.TransactionItem;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.time.temporal.ChronoUnit.DAYS;

public class Utils {

	private final static int ITEMNUMBERCOUNT			= 1000;
	private final static int ITEMNUMBERSIZE				= 10;
	private final static int TRANSACTIONCOUNT			= 500000;
	private final static int TRANSACTIONITEMMAXCOUNT	= 10;
	private final static int MAXPRICE					= 100;
	private final static int STORECOUNT					= 100;

	private static final LocalDate start	= LocalDate.of(2019, 1, 1);
	private static final int period			= (int) DAYS.between(start,  LocalDate.now());
	public static final ObjectMapper om		= new ObjectMapper();
	private final static MathContext mc		= new MathContext(2);

	public static final List<String> itemNumbers;
	static {
		System.out.println("Ready to create Item Numbers");
		itemNumbers = Collections.unmodifiableList(	IntStream.range(0, ITEMNUMBERCOUNT)
													.mapToObj(i -> createItemNumber())
													.collect(Collectors.toList())	);
		System.out.println("Created " + ITEMNUMBERCOUNT + " Item Numbers");
	}

	public static String createItemNumber() {
		return	IntStream.range(0, ITEMNUMBERSIZE)
				.map(d -> (ThreadLocalRandom.current().nextInt(9) + 1))
				.mapToObj(r -> String.valueOf(r))
				.collect(Collectors.joining());
	}

	public static List<String> transactions;
	static {
		System.out.println("Ready to create Transactions");
		transactions =	IntStream.range(0, TRANSACTIONCOUNT)
						.mapToObj(i -> makeTransaction())
						.map(t -> pojoToJson(t))
						.filter(o -> o.isPresent())
						.map(o -> o.get())
						.collect(Collectors.toList());
		System.out.println("Created " + transactions.size() + " Transactions");
	}

	public static Transaction makeTransaction() {
		return Transaction.of(	ThreadLocalRandom.current().nextInt(STORECOUNT),
								fetchTransactionDate(),
									IntStream.rangeClosed(1, ThreadLocalRandom.current().nextInt(TRANSACTIONITEMMAXCOUNT))
									.boxed()
									.map(x -> makeTransactionItem())
									.collect(Collectors.toList()));
	}

	private static TransactionItem makeTransactionItem() {
		return TransactionItem.of(	itemNumbers.get(ThreadLocalRandom.current().nextInt(itemNumbers.size())),
									new BigDecimal((ThreadLocalRandom.current().nextInt(MAXPRICE * 100) / 100f), mc),
									ThreadLocalRandom.current().nextInt(10),
									true,
									new BigDecimal("0.07")	);
	}

	public static Date fetchTransactionDate() {
		return Date.from(	start.plusDays(ThreadLocalRandom.current().nextInt(period))
							.atStartOfDay(ZoneId.systemDefault())
							.toInstant()	);
	}

	public static Optional<String> pojoToJson(final Object pPojo) {
		try {
			return Optional.of(om.writeValueAsString(pPojo));
		} catch (IOException e) {
			e.printStackTrace();
			return Optional.empty();
		}
	}

	public static <T> Optional<T> jsonToPojo(final String pJson, final Class<T> pClass) {
		try {
			return Optional.of(om.readValue(pJson, pClass));
		} catch (IOException e) {
			e.printStackTrace();
			return Optional.empty();
		}
	}
}
