package com.yy.bscRobot.entity;

import com.yy.bscRobot.pojo.WalletInfo;

public class UserTradeSettingInfo {

    private String telegramId;
    private boolean isBuy;
    //滑点
    private double slippage;
    //当前选择的钱包
    private WalletInfo walletInfo;
    private String tokenAddress;
    private boolean isAllWallet;

    public boolean isAllWallet() {
        return isAllWallet;
    }

    public void setAllWallet(boolean allWallet) {
        isAllWallet = allWallet;
    }

    public String getTokenAddress() {
        return tokenAddress;
    }

    public void setTokenAddress(String tokenAddress) {
        this.tokenAddress = tokenAddress;
    }

    public String getTelegramId() {
        return telegramId;
    }

    public void setTelegramId(String telegramId) {
        this.telegramId = telegramId;
    }

    public boolean isBuy() {
        return isBuy;
    }

    public void setBuy(boolean buy) {
        isBuy = buy;
    }

    public double getSlippage() {
        return slippage;
    }

    public void setSlippage(double slippage) {
        this.slippage = slippage;
    }

    public WalletInfo getWalletInfo() {
        return walletInfo;
    }

    public void setWalletInfo(WalletInfo walletInfo) {
        this.walletInfo = walletInfo;
    }
}
