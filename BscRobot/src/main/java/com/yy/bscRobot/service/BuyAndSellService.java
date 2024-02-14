package com.yy.bscRobot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

@Service
public class BuyAndSellService {

    @Autowired
    private Web3Service web3Service;
    @Autowired
    private TokenPoolInfoService tokenPoolInfoService;

    public String buyToken(String tokenAddress,double amount,String privateKey,double slippage)
    {
        //BNB的价格
        double bnbPrice = tokenPoolInfoService.getBnbDoublePrice();
        //token的价格
        double tokenPrice = tokenPoolInfoService.getDoublePrice(tokenAddress);
        //算出可以买多少个token
        double minTokenAmountDouble= amount*bnbPrice/tokenPrice;
        //为了提高成功率，自带0.5%的滑点
        minTokenAmountDouble=minTokenAmountDouble*(1.0-0.01*slippage-0.01);
        BigInteger decimals = tokenPoolInfoService.getDecimals(tokenAddress);

        BigInteger bnbAmount=new BigDecimal(amount).multiply(new BigDecimal("1000000000000000000")).toBigInteger();
        BigInteger minTokenAmount=new BigDecimal(minTokenAmountDouble).multiply(new BigDecimal(decimals)).toBigInteger();
        Credentials credentials = Credentials.create(privateKey);
        String result = web3Service.buyToken(tokenAddress, bnbAmount, minTokenAmount, credentials);
        return result;
    }

    /**
     * 检查授权转账额度
     * @param tokenAddress
     * @param address
     * @return
     */
    public BigInteger checkApproveAmount(String tokenAddress,String address)
    {
        List<Type> inputParams=Arrays.asList(new Address(address),new Address("0x10ED43C718714eb63d5aA57B78B54704E256024E"));
        List<TypeReference<?>>outputParams= Arrays.asList(new TypeReference<Uint256>() {});
        List<Type> result = web3Service.readContract(tokenAddress, "allowance", inputParams, outputParams);
        Uint256 amount= (Uint256) result.get(0);
        return amount.getValue();
    }

    public String sellTokenWithPercent(String tokenAddress, String privateKey, double slippage, double percent) {
        Credentials credentials = Credentials.create(privateKey);
        //token的余额
        BigInteger tokenBalance = tokenPoolInfoService.getPoolBalance(tokenAddress, credentials.getAddress());
        BigInteger amount=new BigDecimal(tokenBalance).multiply(new BigDecimal(percent)).toBigInteger();
        String result = sellTokenWithAmount(tokenAddress, privateKey, slippage, amount);
        return result;
    }

    public String sellTokenWithAmount(String tokenAddress, String privateKey, double slippage, double amount) {
        //代币精度
        BigInteger tokenDecimals = tokenPoolInfoService.getDecimals(tokenAddress);
        BigInteger sellAmount=new BigDecimal(amount).multiply(new BigDecimal(tokenDecimals)).toBigInteger();
        String result = sellTokenWithAmount(tokenAddress, privateKey,slippage,sellAmount);
        return result;

    }

    public String sellTokenWithAmount(String tokenAddress, String privateKey, double slippage, BigInteger amount) {

        Credentials credentials = Credentials.create(privateKey);
        //BNB的价格
        double bnbPrice = tokenPoolInfoService.getBnbDoublePrice();
        //token的价格
        double tokenPrice = tokenPoolInfoService.getDoublePrice(tokenAddress);
        //代币精度
        BigInteger tokenDecimals = tokenPoolInfoService.getDecimals(tokenAddress);

        //算出卖出可以得到多少BNB
        BigDecimal minBnbAmount=new BigDecimal(tokenPrice/bnbPrice)
                .multiply(new BigDecimal(amount).divide(new BigDecimal(tokenPoolInfoService.getDecimals(tokenAddress)))
                        .multiply(new BigDecimal(tokenPoolInfoService.getBnbDecimals())));
        //为了提高成功率，自带0.5%的滑点
        minBnbAmount=minBnbAmount.multiply(new BigDecimal(1-0.01*slippage-0.01));

        BigInteger nonce = web3Service.getNonce(credentials.getAddress());

        //先检查授权转账的额度
        BigInteger approveAmount = checkApproveAmount(tokenAddress, credentials.getAddress());
        if(approveAmount.compareTo(amount)<0)
        {
            web3Service.approve(tokenAddress, new BigInteger("1000000000000000000000000000000000"), credentials,nonce);
            //web3Service.approve(tokenAddress,sellAmount, credentials,nonce);
            nonce=nonce.add(new BigInteger("1"));
        }

        String result = web3Service.sellToken(tokenAddress, amount, minBnbAmount.toBigInteger(), credentials,nonce);
        return result;

    }


    public String transferBnb(String receiveAddress, String privateKey, double amount) {

        Credentials credentials = Credentials.create(privateKey);
        BigInteger balance = web3Service.getBnbBalance(credentials.getAddress());
        BigInteger gasPrice = web3Service.getGasPrice();
        BigInteger gasLimit = web3Service.getEthGasLimit();
        BigInteger gasFee=gasPrice.multiply(gasLimit);
        BigInteger bnbAmount=new BigDecimal(amount).multiply(new BigDecimal("1000000000000000000")).toBigInteger();
        String result="Transfer fail";
        if(balance.compareTo(gasFee.add(bnbAmount))>=0)
        {
            String hash = web3Service.transferBnb(privateKey, receiveAddress, bnbAmount, gasPrice, gasLimit);
            if(hash!=null)
            {
                result="Thank you!";
            }
        }else{
            result="BNB Insufficient";
        }
        return result;
    }
}
