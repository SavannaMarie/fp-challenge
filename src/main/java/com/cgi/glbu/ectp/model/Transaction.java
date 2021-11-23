package com.cgi.glbu.ectp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class Transaction {

	private int storeNumber;
	private Date transactionDate;
	private List<TransactionItem> items;

	@JsonIgnore
	public BigDecimal getTotalSales() {
		return	items.stream()
			.map(i -> i.getUnitPrice().multiply(BigDecimal.valueOf(i.getCount())))
			.reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
	}
}
