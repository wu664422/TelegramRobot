package com.yy.bscRobot.mapper;

import com.yy.bscRobot.pojo.WalletInfo;
import com.yy.bscRobot.pojo.WalletInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WalletInfoMapper {
    long countByExample(WalletInfoExample example);

    int deleteByExample(WalletInfoExample example);

    int deleteByPrimaryKey(Long id);

    int insert(WalletInfo record);

    int insertSelective(WalletInfo record);

    List<WalletInfo> selectByExample(WalletInfoExample example);

    WalletInfo selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") WalletInfo record, @Param("example") WalletInfoExample example);

    int updateByExample(@Param("record") WalletInfo record, @Param("example") WalletInfoExample example);

    int updateByPrimaryKeySelective(WalletInfo record);

    int updateByPrimaryKey(WalletInfo record);
}