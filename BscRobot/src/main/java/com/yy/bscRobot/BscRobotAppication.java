package com.yy.bscRobot;

import com.yy.bscRobot.robot.MyBot;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@EnableScheduling
@SpringBootApplication
@MapperScan("com.yy.bscRobot.mapper")
public class BscRobotAppication {

    public static void main(String[] args) {
        try {
            TelegramBotsApi telegramBotApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotApi.registerBot(new MyBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        SpringApplication.run(BscRobotAppication.class, args);
    }

}
