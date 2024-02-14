package com.yy.bscRobot.service;

import com.yy.bscRobot.mapper.UserInfoMapper;
import com.yy.bscRobot.pojo.UserInfo;
import com.yy.bscRobot.pojo.UserInfoExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    public int insertUserInfo(UserInfo userInfo)
    {
        int row = userInfoMapper.insert(userInfo);
        return row;
    }

    /**
     * 根据telegramId获取用户的信息
     * @param telegramId
     * @return
     */
    public UserInfo findUserInfoByTelegramId(String telegramId)
    {
        UserInfoExample userInfoExample = new UserInfoExample();
        userInfoExample.createCriteria().andTelegramIdEqualTo(telegramId);
        List<UserInfo> list = userInfoMapper.selectByExample(userInfoExample);
        return list.size()>0?list.get(0):null;
    }

    /**
     * 用户注册
     * @param telegramId
     * @param telegramNmae
     * @param telegramToken
     */
    public void userRegister(String telegramId,String telegramNmae,String telegramToken)
    {
        Date date = new Date();
        UserInfo userInfo = new UserInfo();
        userInfo.setId(null);
        userInfo.setTelegramId(telegramId);
        userInfo.setTelegramName(telegramNmae);
        userInfo.setTelegramToken(telegramToken);
        userInfo.setBanStatus(0);
        userInfo.setRegisterTime(date);
        userInfo.setLastLoginTime(date);
        userInfo.setCreateTime(date);
        userInfo.setUpdateTime(date);
        userInfoMapper.insert(userInfo);
    }

    /**
     * 用户登录
     * @param userInfo
     */
    public void userLogin(UserInfo userInfo)
    {
        Date date = new Date();
        userInfo.setLastLoginTime(date);
        userInfo.setUpdateTime(date);
        userInfoMapper.updateByPrimaryKey(userInfo);
    }

}
