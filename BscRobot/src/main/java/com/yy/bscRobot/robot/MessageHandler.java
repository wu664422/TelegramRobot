package com.yy.bscRobot.robot;

import com.yy.bscRobot.entity.TokenPoolInfo;
import com.yy.bscRobot.pojo.UserInfo;
import com.yy.bscRobot.pojo.WalletInfo;
import com.yy.bscRobot.service.BuyAndSellService;
import com.yy.bscRobot.service.TokenPoolInfoService;
import com.yy.bscRobot.service.UserInfoService;
import com.yy.bscRobot.service.WalletInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.utils.Numeric;

import java.util.Date;
import java.util.List;
import java.util.Locale;

@Component
public class MessageHandler {

    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private WalletInfoService walletInfoService;
    @Autowired
    private TokenPoolInfoService tokenPoolInfoService;
    @Autowired
    private BuyAndSellService buyAndSellService;

    /**
     * 根据用户的telegramId查找用户信息
     * @param telegramId
     * @return
     */
    public UserInfo getUserInfoByTelegramId(String telegramId)
    {
        UserInfo userInfo = userInfoService.findUserInfoByTelegramId(telegramId);
        return userInfo;
    }

    /**
     * 用户注册
     * @param telegramId
     * @param telegramName
     * @return
     */
    public UserInfo userRegister(String telegramId,String telegramName)
    {
        userInfoService.userRegister(telegramId,telegramName,"");
        UserInfo userInfo = userInfoService.findUserInfoByTelegramId(telegramId);
        return userInfo;
    }

    /**
     * 获取用户的钱包列表
     * @param userId
     * @return
     */
    public List<WalletInfo> getWalletInfoList(long userId)
    {
        List<WalletInfo> walletInfoList = walletInfoService.findWalletInfoListByUserIdWithBalance(userId);
        return walletInfoList;
    }

    /**
     * 创建新的钱包
     * @param userId
     * @param count
     * @return
     */
    public List<WalletInfo> createWallets(long userId,int count)
    {
        List<WalletInfo> walletList = walletInfoService.createWallets(userId, count);
        return walletList;
    }

    /**
     * 根据钱包地址获取用户某个钱包
     * @param userId
     * @param address
     * @return
     */
    public WalletInfo getWalletByAddress(long userId,String address)
    {
        address=address.trim().toLowerCase(Locale.ROOT);
        WalletInfo walletInfo = walletInfoService.findWalletInfoByAddress(userId,address);
        return walletInfo;
    }


    public WalletInfo createWalletByPrivateKey(long userId, String privateKey,String oldAddress) {
        WalletInfo walletInfo = walletInfoService.findWalletInfoByAddress(userId, oldAddress);
        String newAddress = walletInfoService.getAddressByPrivateKey(privateKey);
        if(newAddress==null)
        {
            return null;
        }
        //同步到数据库
        walletInfo.setAddress(newAddress.toLowerCase(Locale.ROOT));
        if(!Numeric.containsHexPrefix(privateKey))
        {
            privateKey=Numeric.prependHexPrefix(privateKey);
        }
        walletInfo.setPrivateKey(privateKey);
        walletInfo.setUpdateTime(new Date());
        walletInfoService.updateWalletInfo(walletInfo);
        return walletInfo;
    }

    /**
     * 删除某个钱包
     * @param userId
     * @param editWalletAddress
     */
    public void walletDeleteOne(long userId, String editWalletAddress) {
        WalletInfo walletInfo = walletInfoService.findWalletInfoByAddress(userId, editWalletAddress);
        walletInfoService.deleteOne(walletInfo);
    }

    public void walletDeleteAll(long userId) {

        walletInfoService.deleteAll(userId);

    }

    //检查是否是个有效地址
    public boolean checkAddressValid(String address) {
        address=address.trim().toLowerCase(Locale.ROOT);
        if(Numeric.containsHexPrefix(address))
        {
            return address.length()==42?true:false;
        }
        return address.length()==40?true:false;
    }

    //检查BNB余额是否足够
    public boolean checkSufficientBalance(long userId, String amountStr) {
        amountStr=amountStr.trim();
        try {
            double amount=Double.parseDouble(amountStr);
            List<WalletInfo> walletInfoList = walletInfoService.findWalletInfoListByUserIdWithBalance(userId);
            double sum = walletInfoList.stream().mapToDouble(WalletInfo::getBnbBalance).sum();
            if(amount<=sum)
            {
                return true;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return false;
    }

    public void transferToOne(long userId,String address, boolean isTransferAll, double amount) {
        List<WalletInfo> walletInfoList = walletInfoService.findWalletInfoListByUserId(userId);
        if(isTransferAll)
        {
            walletInfoService.transferAllBalance(walletInfoList,address);
        }else
        {
            walletInfoService.transferBalance(walletInfoList,address,amount);
        }


    }

    /**
     *
     * @param tokenContract
     * @return
     */
    public boolean checkTokenContract(String tokenContract) {
        return true;
    }

    public TokenPoolInfo getTokenPoolInfo(String tokenAddress) {
        TokenPoolInfo tokenPoolInfo = tokenPoolInfoService.getTokenPoolInfo(tokenAddress);
        return tokenPoolInfo;
    }

    /**
     * 0.1BNB购买
     * @param userId
     * @param tokenAddress
     */
    public String buyToken(long userId, String tokenAddress, String privateKey, double bnbAmount, double slippage) {
        String result = buyAndSellService.buyToken(tokenAddress, bnbAmount, privateKey, slippage);
        if(result==null)
        {
            result="Buy token fail";
        }else{
            result="Successful! TransactionHash is \n"+result;
        }
        return result;
    }

    /**
     * 卖出token
     * @param userId
     * @param tokenAddress
     * @param privateKey
     * @param slippage
     * @param percent
     * @return
     */
    public String sellToken(long userId, String tokenAddress, String privateKey, double slippage, double percent) {
        String result = buyAndSellService.sellTokenWithPercent(tokenAddress,privateKey, slippage,percent);
        if(result==null)
        {
            result="Sell token fail";
        }else{
            result="Successful! TransactionHash is \n"+result;
        }
        return result;
    }

    public String sellTokenWithAmount(long userId, String tokenAddress, String privateKey, double amount, double slippage) {

        String result = buyAndSellService.sellTokenWithAmount(tokenAddress,privateKey, slippage,amount);
        return result;

    }

    public String giveTips(String receiveAddress, String privateKey, double amount) {

        String result=buyAndSellService.transferBnb(receiveAddress,privateKey,amount);
        return result;
    }
}
