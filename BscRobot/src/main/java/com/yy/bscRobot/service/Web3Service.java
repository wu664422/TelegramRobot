package com.yy.bscRobot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class Web3Service {

    private Logger logger=LoggerFactory.getLogger(Web3Service.class);

    private Web3j web3jClient;
    String mainNet="https://bsc-dataseed.binance.org/";

    private Web3j getClient()
    {
        if(web3jClient==null)
        {
            web3jClient=Web3j.build(new HttpService(mainNet));
        }
        //判断是否处于连接状态
        NetVersion netVersion = null;
        try {
            netVersion = web3jClient.netVersion().send();
            String version = netVersion.getNetVersion();
            logger.info("当前web3的版本信息是：{}",version);
        } catch (IOException e) {
            logger.info("web3的连接已经断开，需要重新连接");
            web3jClient=Web3j.build(new HttpService(mainNet));
            e.printStackTrace();
        }
        return web3jClient;
    }

    public BigInteger getBnbBalance(String address)
    {
        BigInteger balance=BigInteger.ZERO;
        Web3j client = getClient();
        try {
            balance = client.ethGetBalance(address, DefaultBlockParameter.valueOf("latest")).send().getBalance();
            logger.info("地址{}的BNB余额为{}",address,balance);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  balance;
    }


    public String transferBnb(String privateKey, String toAddress, BigInteger amount,BigInteger gasPrice,BigInteger gasLimit) {
        Web3j client = getClient();
        Credentials credentials = Credentials.create(privateKey);
        String address = credentials.getAddress();
        BigInteger nonce = getNonce(client,address);

        RawTransaction etherTransaction = RawTransaction.createEtherTransaction(nonce, gasPrice, gasLimit, toAddress, amount);
        byte[] signMessage = TransactionEncoder.signMessage(etherTransaction, credentials);
        String data = Numeric.toHexString(signMessage);
        try {
            EthSendTransaction ethSendTransaction = client.ethSendRawTransaction(data).sendAsync().get();
            if(ethSendTransaction!=null)
            {
                return ethSendTransaction.getTransactionHash();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Type> readContract(String contractAddress, String functionName, List<Type>inputParams, List<TypeReference<?>> outputParams)
    {
        Web3j client = getClient();
        //构造函数
        Function function = new Function(functionName, inputParams, outputParams);
        //构造交易
        String encodedFunction = FunctionEncoder.encode(function);
        //发送交互调用
        EthCall response = null;
        try {
            response = client.ethCall(
                            Transaction.createEthCallTransaction(null, contractAddress, encodedFunction),
                            DefaultBlockParameterName.LATEST)
                    .sendAsync().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        List<Type> results=null;
        if(response!=null)
        {
            results = FunctionReturnDecoder.decode(response.getValue(), function.getOutputParameters());
        }
        return results;
    }

    public String writeContract(Credentials credentials, String contractAddress, String functionName, List<Type>inputParams, List<TypeReference<?>> outputParams)
    {
        Web3j client = getClient();
        // 创建一个 Function 对象来表示 swapExactTokensForTokens 函数
        Function function = new Function(
                functionName,
                inputParams,
                outputParams
        );
        String encodedFunction = FunctionEncoder.encode(function);
        EthSendTransaction response = null;
        try {
            RawTransaction rawTransaction = RawTransaction.createTransaction(getNonce(client,credentials.getAddress()), getGasPrice(),getEthGasLimit(), contractAddress, encodedFunction);
            response = client.ethSendRawTransaction(Numeric.toHexString(TransactionEncoder.signMessage(rawTransaction, credentials)))
                    .sendAsync()
                    .get();
            return response.getTransactionHash();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public BigInteger getNonce(Web3j client,String address)
    {
        EthGetTransactionCount transactionCount = null;
        try {
            transactionCount = client.ethGetTransactionCount(address, DefaultBlockParameterName.LATEST).send();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BigInteger nonce = transactionCount.getTransactionCount();
        return nonce;
    }

    public BigInteger getNonce(String address)
    {
        Web3j client=getClient();
        EthGetTransactionCount transactionCount = null;
        try {
            transactionCount = client.ethGetTransactionCount(address, DefaultBlockParameterName.LATEST).send();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BigInteger nonce = transactionCount.getTransactionCount();
        return nonce;
    }

    /**
     * 获取最近一笔交易的gasprice
     * @return
     */
    public BigInteger getGasPrice()
    {
        Web3j client=getClient();
        EthGasPrice ethGasPrice = null;
        try {
            ethGasPrice = client.ethGasPrice().sendAsync().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return ethGasPrice.getGasPrice();
    }

    /**
     * 获取转账默认的gasLimit
     * @return
     */
    public BigInteger getEthGasLimit()
    {
        return  BigInteger.valueOf(21000);
    }

    /**
     * 获取转账默认的gasLimit
     * @return
     */
    public BigInteger getGasLimit()
    {
        return  BigInteger.valueOf(1000000);
    }

    /**
     * 用bnb购买token
     * @param tokenAddress
     * @param bnbAmount
     * @param minTokenAmount
     */
    public String buyToken(String tokenAddress, BigInteger bnbAmount, BigInteger minTokenAmount,Credentials credentials) {
        String contract="0x10ED43C718714eb63d5aA57B78B54704E256024E";
        String functionName="swapExactETHForTokens";
        String wbnbAddress="0xbb4CdB9CBd36B01bD1cBaEBF2De08d9173bc095c";
        Web3j web3j = getClient();

        // 交换路径，其中第一个地址是输入代币，最后一个地址是输出代币
        DynamicArray<Address> path = new DynamicArray<Address>(
                Address.class,  // 动态数组中元素的类型
                new Address(wbnbAddress),  // 第一个元素
                new Address(tokenAddress)  // 第二个元素
        );
        //接收的地址
        Address to = new Address(credentials.getAddress());

        // 交易截止时间
        BigInteger deadline = BigInteger.valueOf(System.currentTimeMillis() / 1000 + 3600); // 1 小时后

        // 创建一个 Function 对象来表示 swapExactTokensForTokens 函数
        Function function = new Function(
                functionName,
                Arrays.<Type>asList( new Uint256(minTokenAmount),  path, to, new Uint256(deadline)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {})
        );
        String encodedFunction = FunctionEncoder.encode(function);
        RawTransaction rawTransaction = RawTransaction.createTransaction(getNonce(web3j,credentials.getAddress()), getGasPrice(),getGasLimit(), contract,bnbAmount, encodedFunction);
        EthSendTransaction response = null;
        String transactionHash=null;
        try {
            response = web3j.ethSendRawTransaction(Numeric.toHexString(TransactionEncoder.signMessage(rawTransaction, credentials)))
                    .sendAsync()
                    .get();
            transactionHash=response.getTransactionHash();
            if(transactionHash!=null)
            {
                for(int i=0;i<10;i++)
                {
                    try {
                        TransactionReceipt transactionReceipt = web3j.ethGetTransactionReceipt(transactionHash).send().getResult();
                        if(transactionReceipt!=null)
                        {
                            if (transactionReceipt.isStatusOK())
                            {
                                return transactionHash;
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Thread.sleep(1000);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;

    }


    public String sellToken(String tokenAddress, BigInteger sellAmount, BigInteger minBnbAmount, Credentials credentials,BigInteger nonce) {

        String contract="0x10ED43C718714eb63d5aA57B78B54704E256024E";
        String functionName="swapExactTokensForETH";
        String wbnbAddress="0xbb4CdB9CBd36B01bD1cBaEBF2De08d9173bc095c";
        Web3j web3j = getClient();

        // 交换路径，其中第一个地址是输入代币，最后一个地址是输出代币
        DynamicArray<Address> path = new DynamicArray<Address>(
                Address.class,  // 动态数组中元素的类型
                new Address(tokenAddress),  // 第一个元素
                new Address(wbnbAddress)  // 第二个元素
        );
        //接收的地址
        Address to = new Address(credentials.getAddress());

        // 交易截止时间
        BigInteger deadline = BigInteger.valueOf(System.currentTimeMillis() / 1000 + 3600); // 1 小时后

        // 创建一个 Function 对象来表示 swapExactTokensForTokens 函数
        Function function = new Function(
                functionName,
                Arrays.<Type>asList(new Uint256(sellAmount), new Uint256(minBnbAmount),  path, to, new Uint256(deadline)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {})
        );
        String encodedFunction = FunctionEncoder.encode(function);
        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, getGasPrice(),getGasLimit(), contract, encodedFunction);
        EthSendTransaction response = null;
        String transactionHash=null;
        try {
            response = web3j.ethSendRawTransaction(Numeric.toHexString(TransactionEncoder.signMessage(rawTransaction, credentials)))
                    .sendAsync()
                    .get();
            transactionHash=response.getTransactionHash();
            if(transactionHash!=null)
            {
                for(int i=0;i<10;i++)
                {
                    try {
                        TransactionReceipt transactionReceipt = web3j.ethGetTransactionReceipt(transactionHash).send().getResult();
                        if(transactionReceipt!=null)
                        {
                            if (transactionReceipt.isStatusOK())
                            {
                                return transactionHash;
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Thread.sleep(1000);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 授权转账
     * @param tokenAddress
     * @param sellAmount
     * @param credentials
     */
    public String approve(String tokenAddress, BigInteger sellAmount, Credentials credentials,BigInteger nonce) {
        String approvedAddress="0x10ED43C718714eb63d5aA57B78B54704E256024E";
        String contract=tokenAddress;
        String functionName="approve";
        Web3j web3j = getClient();

        // 创建一个 Function 对象来表示 swapExactTokensForTokens 函数
        Function function = new Function(
                functionName,
                Arrays.<Type>asList(new Address(approvedAddress),new Uint256(sellAmount)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {})
        );
        String encodedFunction = FunctionEncoder.encode(function);
        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, getGasPrice(),getGasLimit(), contract, encodedFunction);
        EthSendTransaction response = null;
        String transactionHash=null;
        try {
            response = web3j.ethSendRawTransaction(Numeric.toHexString(TransactionEncoder.signMessage(rawTransaction, credentials)))
                    .sendAsync()
                    .get();
            transactionHash=response.getTransactionHash();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return transactionHash;
    }

}
