package com.yy.bscRobot.controller;

import com.yy.bscRobot.entity.Result;
import com.yy.bscRobot.pojo.UserInfo;
import com.yy.bscRobot.pojo.WalletInfo;
import com.yy.bscRobot.service.UserInfoService;
import com.yy.bscRobot.service.WalletInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.utils.Numeric;

import java.util.Date;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("walletInfo")
public class WalletInfoController {

    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private WalletInfoService walletInfoService;

    /**
     * 获取用户钱包信息列表
     * @param telegramId
     * @return
     */
    @RequestMapping("/getList")
    public Result getList(String telegramId)
    {
        UserInfo userInfo = userInfoService.findUserInfoByTelegramId(telegramId);
        if(userInfo==null)
        {
            return Result.fail(500,"用户信息异常");
        }
        Long userId = userInfo.getId();
        List<WalletInfo> walletInfoList = walletInfoService.findWalletInfoListByUserIdWithBalance(userId);
        return Result.success(walletInfoList);
    }

    /**
     * 获取用户钱包详细信息
     * @param telegramId
     * @return
     */
    @RequestMapping("/detail")
    public Result detail(String telegramId,String address)
    {
        if(address==null||address.equals(""))
        {
            return Result.fail(500,"钱包地址异常");
        }

        UserInfo userInfo = userInfoService.findUserInfoByTelegramId(telegramId);
        if(userInfo==null)
        {
            return Result.fail(500,"用户信息异常");
        }

        address=address.trim().toLowerCase(Locale.ROOT);
        WalletInfo walletInfo = walletInfoService.findWalletInfoByAddress(userInfo.getId(),address);
        if(walletInfo==null)
        {
            return Result.fail(500,"钱包地址异常");
        }
        return Result.success(walletInfo);
    }

    /**
     * 创建钱包
     * @param telegramId
     * @return
     */
    @RequestMapping("/createWallets")
    public Result createWallets(String telegramId,int count)
    {
        UserInfo userInfo = userInfoService.findUserInfoByTelegramId(telegramId);
        if(userInfo==null)
        {
            return Result.fail(500,"用户信息异常");
        }
        Long userId = userInfo.getId();
        walletInfoService.createWallets(userId,count);
        List<WalletInfo> walletInfoList = walletInfoService.findWalletInfoListByUserIdWithBalance(userId);
        return Result.success(walletInfoList);
    }

    /**
     * 删除某个钱包
     * @param telegramId
     * @return
     */
    @RequestMapping("/deleteOne")
    public Result deleteOne(String telegramId,String address)
    {
        if(address==null||address.equals(""))
        {
            return Result.fail(500,"钱包地址异常");
        }

        UserInfo userInfo = userInfoService.findUserInfoByTelegramId(telegramId);
        if(userInfo==null)
        {
            return Result.fail(500,"用户信息异常");
        }
        Long userId = userInfo.getId();
        address=address.trim().toLowerCase(Locale.ROOT);
        WalletInfo walletInfo = walletInfoService.findWalletInfoByAddress(userId, address);
        if(walletInfo==null)
        {
            return Result.fail(500,"钱包地址异常");
        }
        walletInfoService.deleteOne(walletInfo);
        List<WalletInfo> walletInfoList = walletInfoService.findWalletInfoListByUserIdWithBalance(userId);
        return Result.success(walletInfoList);
    }


    /**
     * 删除所有的钱包
     * @param telegramId
     * @return
     */
    @RequestMapping("/deleteAll")
    public Result deleteAll(String telegramId)
    {
        UserInfo userInfo = userInfoService.findUserInfoByTelegramId(telegramId);
        if(userInfo==null)
        {
            return Result.fail(500,"用户信息异常");
        }
        Long userId = userInfo.getId();

        walletInfoService.deleteAll(userId);
        List<WalletInfo> walletInfoList = walletInfoService.findWalletInfoListByUserIdWithBalance(userId);
        return Result.success(walletInfoList);
    }

    /**
     * 修改钱包
     * @param telegramId
     * @return
     */
    @RequestMapping("/updateWallet")
    public Result updateWallet(String telegramId,String address,String privateKey)
    {
        UserInfo userInfo = userInfoService.findUserInfoByTelegramId(telegramId);
        if(userInfo==null)
        {
            return Result.fail(500,"用户信息异常");
        }
        Long userId = userInfo.getId();

        //校验钱包地址是否存在
        address=address.trim().toLowerCase(Locale.ROOT);
        WalletInfo walletInfo = walletInfoService.findWalletInfoByAddress(userId, address);
        if(walletInfo==null)
        {
            return Result.fail(500,"钱包地址异常");
        }

        //根据私钥生成钱包地址
        String newAddress = walletInfoService.getAddressByPrivateKey(privateKey);
        if(newAddress==null)
        {
            return Result.fail(500,"非法的私钥");
        }

        //将新的钱包地址保存到数据库
        //旧的钱包设置成删除状态
        walletInfo.setAddress(newAddress.toLowerCase(Locale.ROOT));
        if(!Numeric.containsHexPrefix(privateKey))
        {
            privateKey=Numeric.prependHexPrefix(privateKey);
        }
        walletInfo.setPrivateKey(privateKey);
        walletInfo.setUpdateTime(new Date());
        walletInfoService.updateWalletInfo(walletInfo);

        List<WalletInfo> walletInfoList = walletInfoService.findWalletInfoListByUserIdWithBalance(userId);
        return Result.success(walletInfoList);
    }

}
