package com.yy.bscRobot.service;

import com.yy.bscRobot.entity.TokenPoolInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class TokenPoolInfoService {

    @Autowired
    private Web3Service web3Service;


    public TokenPoolInfo getTokenPoolInfo(String tokenAddress)
    {
        TokenPoolInfo tokenPoolInfo = new TokenPoolInfo();
        tokenPoolInfo.setContract(tokenAddress);
        tokenPoolInfo.setExchange("PancakeSwap");
        tokenPoolInfo.setRenounced(true);

        BigInteger bnbPrice = getBnbPrice();
        double bnbPriceDouble=new BigDecimal(bnbPrice).divide(new BigDecimal("1000000000000000000")).doubleValue();
        //代币名称
        String tokenSymbol = getTokenSymbol(tokenAddress);
        //价格
        BigInteger price = getPrice(tokenAddress);
        //总供应量
        BigInteger totalSupply = getTotalSupply(tokenAddress);
        //精度
        BigInteger decimals = getDecimals(tokenAddress);
        //流动池
        String poolAddress = getPoolAddress(tokenAddress);
        //流动池代币数量
        BigInteger poolBalance = getPoolBalance(tokenAddress, poolAddress);

        BigDecimal priceDecimal = new BigDecimal(price).divide(new BigDecimal(decimals));

        tokenPoolInfo.setSymbol(tokenSymbol);
        tokenPoolInfo.setPrice(priceDecimal.toString());
        tokenPoolInfo.setMarketCap(new BigDecimal(totalSupply).divide(new BigDecimal(decimals)).multiply(priceDecimal).toString());
        tokenPoolInfo.setLiquidity(new BigDecimal(poolBalance).divide(new BigDecimal(decimals)).multiply(priceDecimal).toString());
        double poolValue = new BigDecimal(poolBalance).divide(new BigDecimal(decimals)).multiply(priceDecimal).doubleValue();
        tokenPoolInfo.setPoolBNB(poolValue/bnbPriceDouble+"");
        return tokenPoolInfo;
    }

    public BigInteger getBnbPrice()
    {
        String contract="0x10ed43c718714eb63d5aa57b78b54704e256024e";
        String wbnbAddress="0xbb4CdB9CBd36B01bD1cBaEBF2De08d9173bc095c";
        String usdtAddress="0x55d398326f99059fF775485246999027B3197955";
        Uint256 amount=new Uint256(new BigInteger("1000000000000000000"));
        DynamicArray<Address> path = new DynamicArray<Address>(
                Address.class,  // 动态数组中元素的类型
                new Address(wbnbAddress),  // 第一个元素
                new Address(usdtAddress)  // 第二个元素
        );
        List<Type>inputParams=Arrays.asList(amount,path);
        List<TypeReference<?>> outputParams = Arrays.asList(new TypeReference<DynamicArray<Uint256>>() {});

        List<Type> result = web3Service.readContract(contract, "getAmountsOut", inputParams, outputParams);
        DynamicArray<Uint256> preValue = (DynamicArray<Uint256>)result.get(0);
        BigInteger priceBig=preValue.getValue().get(1).getValue();
        return priceBig;
    }

    public double getBnbDoublePrice()
    {
        BigInteger bnbPrice = getBnbPrice();
        return new BigDecimal(bnbPrice).divide(new BigDecimal("1000000000000000000")).doubleValue();
    }

    /**
     * 获取价格
     * @param tokenAddress
     * @return
     */
    public BigInteger getPrice(String tokenAddress)
    {
        String contract="0x10ed43c718714eb63d5aa57b78b54704e256024e";
        String usdtAddress="0x55d398326f99059fF775485246999027B3197955";
        Uint256 amount=new Uint256(new BigInteger("1000000000000000000"));
        DynamicArray<Address> path = new DynamicArray<Address>(
                Address.class,  // 动态数组中元素的类型
                new Address(tokenAddress),  // 第一个元素
                new Address(usdtAddress)  // 第二个元素
        );
        List<Type>inputParams=Arrays.asList(amount,path);
        List<TypeReference<?>> outputParams = Arrays.asList(new TypeReference<DynamicArray<Uint256>>() {});

        List<Type> result = web3Service.readContract(contract, "getAmountsOut", inputParams, outputParams);
        DynamicArray<Uint256> preValue = (DynamicArray<Uint256>)result.get(0);
        BigInteger priceBig=preValue.getValue().get(1).getValue();
        return priceBig;
    }

    public double getDoublePrice(String tokenAddress)
    {
        BigInteger price = getPrice(tokenAddress);
        BigInteger decimals = getDecimals(tokenAddress);
        BigDecimal priceDecimal = new BigDecimal(price).divide(new BigDecimal(decimals));
        return priceDecimal.doubleValue();
    }

    /**
     * 获取流动池地址
     * @param tokenAddress
     * @return
     */
    public String getPoolAddress(String tokenAddress)
    {
        String contract="0xcA143Ce32Fe78f1f7019d7d551a6402fC5350c73";
        List<Type>inputParams=Arrays.asList(new Address(tokenAddress),
                new Address("0x55d398326f99059fF775485246999027B3197955"));
        List<TypeReference<?>>outputParams=Arrays.asList(new TypeReference<Address>() {});
        List<Type> result = web3Service.readContract(contract, "getPair", inputParams, outputParams);
        Address poolAddress= (Address) result.get(0);
        return poolAddress.getValue();
    }

    /**
     * 代币符号
     * @param tokenAddress
     * @return
     */
    public String getTokenSymbol(String tokenAddress)
    {
        List<Type>inputParams=new ArrayList<>();
        List<TypeReference<?>>outputParams=Arrays.asList(new TypeReference<Utf8String>() {});
        List<Type> result = web3Service.readContract(tokenAddress, "symbol", inputParams, outputParams);
        Utf8String symbol= (Utf8String) result.get(0);
        return symbol.toString();
    }

    /**
     * 总供应量
     * @param tokenAddress
     * @return
     */
    public BigInteger getTotalSupply(String tokenAddress)
    {
        List<Type>inputParams=new ArrayList<>();
        List<TypeReference<?>>outputParams=Arrays.asList(new TypeReference<Uint256>() {});
        List<Type> result = web3Service.readContract(tokenAddress, "totalSupply", inputParams, outputParams);
        Uint256 totalSupply= (Uint256) result.get(0);
        return totalSupply.getValue();
    }

    /**
     * 精度
     * @param tokenAddress
     * @return
     */
    public BigInteger getDecimals(String tokenAddress)
    {
        List<Type>inputParams=new ArrayList<>();
        List<TypeReference<?>>outputParams=Arrays.asList(new TypeReference<Uint8>() {});
        List<Type> result = web3Service.readContract(tokenAddress, "decimals", inputParams, outputParams);
        Uint8 decimalsUint= (Uint8) result.get(0);
        BigInteger decimals=decimalsUint.getValue();

        BigInteger decimalB=BigInteger.ONE;
        for(int i=0;i<decimals.intValue();i++)
        {
            decimalB=decimalB.multiply(new BigInteger("10"));
        }
        return decimalB;
    }

    public BigInteger getBnbDecimals()
    {
        return new BigInteger("1000000000000000000");
    }

    /**
     * 获取池子里的代币数量
     * @param tokenAddress
     * @param poolAddress
     * @return
     */
    public BigInteger getPoolBalance(String tokenAddress,String poolAddress)
    {
        List<Type>inputParams=Arrays.asList(new Address(poolAddress));
        List<TypeReference<?>>outputParams=Arrays.asList(new TypeReference<Uint256>() {});
        List<Type> result = web3Service.readContract(tokenAddress, "balanceOf", inputParams, outputParams);
        Uint256 amount= (Uint256) result.get(0);
        return amount.getValue();
    }





}
