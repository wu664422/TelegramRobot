package com.yy.bscRobot.service;

import com.yy.bscRobot.mapper.WalletInfoMapper;
import com.yy.bscRobot.pojo.WalletInfo;
import com.yy.bscRobot.pojo.WalletInfoExample;
import com.yy.bscRobot.utils.WalletUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
public class WalletInfoService {

    private  final Logger logger = LoggerFactory.getLogger(WalletInfoService.class);

    @Autowired
    private WalletInfoMapper walletInfoMapper;
    @Autowired
    private Web3Service web3Service;

    /**
     * 根据用户id获取用户的钱包列表
     * 同时查询出BNB的余额
     * @param userId
     * @return
     */
    public List<WalletInfo> findWalletInfoListByUserIdWithBalance(long userId)
    {
        WalletInfoExample example = new WalletInfoExample();
        example.createCriteria().andUserIdEqualTo(userId).andDeleteStatusEqualTo(0);
        List<WalletInfo> walletInfoList = walletInfoMapper.selectByExample(example);

        //从区块链上获取钱包余额
        for(WalletInfo walletInfo:walletInfoList)
        {
            String address = walletInfo.getAddress();
            BigInteger balance = web3Service.getBnbBalance(address);
            //保留6位小数显示
            balance = balance.divide(new BigInteger("1000000000000"));
            Double balanceD=balance.doubleValue()/1000000.0;
            walletInfo.setBnbBalance(balanceD);
        }
        return walletInfoList;
    }

    /**
     * 根据用户id获取用户的钱包列表
     * @param userId
     * @return
     */
    public List<WalletInfo> findWalletInfoListByUserId(long userId)
    {
        WalletInfoExample example = new WalletInfoExample();
        example.createCriteria().andUserIdEqualTo(userId).andDeleteStatusEqualTo(0);
        List<WalletInfo> walletInfoList = walletInfoMapper.selectByExample(example);
        return walletInfoList;
    }

    /**
     * 根据地址获取钱包信息
     * @param address
     * @return
     */
    public WalletInfo findWalletInfoByAddress(long userId,String address)
    {
        WalletInfoExample example = new WalletInfoExample();
        example.createCriteria().andUserIdEqualTo(userId).andAddressEqualTo(address).andDeleteStatusEqualTo(0);
        List<WalletInfo> list = walletInfoMapper.selectByExample(example);
        return list.size()>0?list.get(0):null;
    }

    /**
     * 创建钱包
     * @param userId
     * @param count
     * @return
     */
    public List<WalletInfo> createWallets(long userId,int count)
    {
        Date date = new Date();
        List<WalletInfo> list=new ArrayList<>();
        for(int i=0;i<count;i++)
        {
            String privateKey = WalletUtils.generateBip39Wallet();
            Credentials credentials = Credentials.create(privateKey);
            String address = credentials.getAddress().toLowerCase(Locale.ROOT);
            logger.info("id为{}的用户生成地址为{}的新钱包",userId,address);
            WalletInfo walletInfo = new WalletInfo();
            walletInfo.setId(null);
            walletInfo.setAddress(address);
            walletInfo.setPrivateKey(privateKey);
            walletInfo.setUserId(userId);
            walletInfo.setBnbBalance(0.0);
            walletInfo.setDeleteStatus(0);
            walletInfo.setCreateTime(date);
            walletInfo.setUpdateTime(date);
            //插入数据库
            walletInfoMapper.insert(walletInfo);
            list.add(walletInfo);
        }
        return list;
    }

    /**
     * 删除某个钱包
     * @param walletInfo
     */
    public void deleteOne(WalletInfo walletInfo)
    {
        walletInfo.setDeleteStatus(1);
        walletInfo.setUpdateTime(new Date());
        walletInfoMapper.updateByPrimaryKey(walletInfo);
    }

    /**
     * 删除用户的所有钱包
     * @param userId
     */
    public void deleteAll(Long userId) {
        List<WalletInfo> walletInfoList = findWalletInfoListByUserIdWithBalance(userId);
        Date date = new Date();
        for(WalletInfo walletInfo:walletInfoList)
        {
            walletInfo.setDeleteStatus(1);
            walletInfo.setUpdateTime(date);
            walletInfoMapper.updateByPrimaryKey(walletInfo);
        }
    }

    /**
     * 根据私钥生成钱包地址
     * @param privateKey
     */
    public String getAddressByPrivateKey(String privateKey) {
        //先判断私钥是否有效，如果不是有效的私钥则返回null
        if(privateKey==null)
        {
            return null;
        }
        privateKey=privateKey.trim().toLowerCase(Locale.ROOT);
        if(privateKey.length()==64||((privateKey.length()==66&& Numeric.containsHexPrefix(privateKey))))
        {
            Credentials credentials = Credentials.create(privateKey);
            if(credentials==null)
            {
                return null;
            }
            return credentials.getAddress();
        }
        return null;
    }

    /**
     * 修改钱包信息
     * @param walletInfo
     */
    public void updateWalletInfo(WalletInfo walletInfo) {
        walletInfoMapper.updateByPrimaryKey(walletInfo);
    }

    /**
     *将所有的BNB归集
     * @param walletInfoList
     * @param toAddress
     */
    public void transferAllBalance(List<WalletInfo>walletInfoList, String toAddress)
    {
        for(WalletInfo walletInfo:walletInfoList)
        {
            String address = walletInfo.getAddress();
            BigInteger balance = web3Service.getBnbBalance(address);
            BigInteger gasPrice = web3Service.getGasPrice();
            BigInteger gasLimit = web3Service.getEthGasLimit();
            BigInteger gasFee=gasPrice.multiply(gasLimit);
            if(balance.compareTo(gasFee)>0)
            {
                BigInteger amount = balance.subtract(gasFee);
                web3Service.transferBnb(walletInfo.getPrivateKey(), toAddress,amount,gasPrice,gasLimit);
            }
        }
    }

    public void transferBalance(List<WalletInfo> walletInfoList, String toAddress, double amount) {

        BigDecimal amountDecimal= new BigDecimal(amount);
        amountDecimal=amountDecimal.multiply(new BigDecimal("1000000000000000000"));
        BigInteger amountB = amountDecimal.toBigInteger();
        for(WalletInfo walletInfo:walletInfoList)
        {
            String address = walletInfo.getAddress();
            BigInteger balance = web3Service.getBnbBalance(address);
            BigInteger gasPrice = web3Service.getGasPrice();
            BigInteger gasLimit = web3Service.getEthGasLimit();
            BigInteger gasFee=gasPrice.multiply(gasLimit);
            BigInteger transferAmount = balance.subtract(gasFee);
            if(transferAmount.compareTo(BigInteger.ZERO)>0&&amountB.compareTo(BigInteger.ZERO)>0)
            {
                if(amountB.compareTo(transferAmount)<0)
                {
                    transferAmount=amountB;
                }
                web3Service.transferBnb(walletInfo.getPrivateKey(), toAddress,transferAmount,gasPrice,gasLimit);
                amountB=amountB.subtract(transferAmount);
            }
        }
    }
}
