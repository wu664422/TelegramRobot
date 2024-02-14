package com.yy.bscRobot.entity;

public class TokenPoolInfo {

    /**
     * 代币合约地址
     */
    private String contract;
    /**
     * 代币名称
     */
    private String symbol;
    /**
     * 去中心化交易所名称
     */
    private String exchange;
    /**
     * 市场总价值
     */
    private String marketCap;
    /**
     * 流动市值
     */
    private String liquidity;
    /**
     * 价格
     */
    private String price;
    /**
     * 流动池里的BNB
     */
    private String poolBNB;
    /**
     * 是否抛弃管理员权限
     */
    private boolean renounced;

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(String marketCap) {
        this.marketCap = marketCap;
    }

    public String getLiquidity() {
        return liquidity;
    }

    public void setLiquidity(String liquidity) {
        this.liquidity = liquidity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPoolBNB() {
        return poolBNB;
    }

    public void setPoolBNB(String poolBNB) {
        this.poolBNB = poolBNB;
    }

    public boolean isRenounced() {
        return renounced;
    }

    public void setRenounced(boolean renounced) {
        this.renounced = renounced;
    }
}
