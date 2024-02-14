package com.yy.bscRobot.robot;

public enum MessageState {

    None(0,"初始状态"),
    MainPage(1,"首页"),
    WalletEdit(2,"钱包编辑"),
    WalletDeleteOne(2,"删除单个钱包"),
    WalletDeleteAll(3,"删除所有钱包"),
    WalletTransfer(4,"归集BNB"),
    TradePage(5,"交易界面"),
    SetBuySlippage(6,"设置购买的滑点"),
    BuyWithBnbAmount(7,"用多少BNB购买"),
    EnterSellAmount(8,"卖出多少数量的代币"),
    EnterGiveTips(9,"等待输入打赏金额");

    private int code;
    private String description;

    MessageState(int code,String description)
    {
        this.code=code;
        this.description=description;
    }

}
