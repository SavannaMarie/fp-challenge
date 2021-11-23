package com.cgi.glbu.ectp.model;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class TransactionItem {

	private String skuNumber;
	private BigDecimal unitPrice;
	private int count;
	private boolean taxable;
	private BigDecimal taxRate;
}
