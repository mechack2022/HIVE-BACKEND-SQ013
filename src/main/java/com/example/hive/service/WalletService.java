package com.example.hive.service;

import com.example.hive.dto.response.TransactionResponse;
import com.example.hive.entity.Task;
import com.example.hive.entity.User;

import java.math.BigDecimal;
import com.example.hive.dto.response.WalletResponseDto;
import com.example.hive.entity.Wallet;

import java.security.Principal;
import java.util.List;

public interface WalletService {

    //TODO :make sure the withrawal code has new a new transaction log with set time(when the withdrawal is made

    boolean creditDoerWallet(User doer, BigDecimal creditAmount);

    WalletResponseDto getWalletByUser(Principal principal);

    boolean fundTaskerWallet(User tasker, BigDecimal amountToFund);

    boolean debitTaskerWalletToEscrow(Wallet wallet, BigDecimal amount);

    boolean refundTaskerFromEscrowWallet(Task taskToCancel);

    List<TransactionResponse> getWalletHistory(Principal principal);
}
