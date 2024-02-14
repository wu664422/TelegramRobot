package com.yy.bscRobot.controller;

import com.yy.bscRobot.entity.Result;
import com.yy.bscRobot.pojo.UserInfo;
import com.yy.bscRobot.service.UserInfoService;
import com.yy.bscRobot.service.WalletInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/userInfo")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private WalletInfoService walletInfoService;

    /**
     * 登陆
     * @param telegramId
     * @param telegramName
     * @param telegramToken
     * @return
     */
    @RequestMapping("/login")
    public Result login(String telegramId,String telegramName,String telegramToken)
    {
        UserInfo userInfo = userInfoService.findUserInfoByTelegramId(telegramId);
        if(userInfo==null)
        {
            //去注册
            userInfoService.userRegister(telegramId,telegramName,telegramToken);
        }else{
            //去登录
            userInfoService.userLogin(userInfo);
        }
        return Result.success(null);
    }



}
