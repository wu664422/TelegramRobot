package com.yy.bscRobot.robot;

import com.yy.bscRobot.entity.TokenPoolInfo;
import com.yy.bscRobot.entity.UserTradeSettingInfo;
import com.yy.bscRobot.pojo.UserInfo;
import com.yy.bscRobot.pojo.WalletInfo;
import com.yy.bscRobot.utils.SpringContextUtils;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

public class MyBot extends TelegramLongPollingBot {

//    private String  botUsername="jason665522_bot";
    private String  botUsername="bsc664422_bot";
//    private String botToken="6525017557:AAHRH0pt9wQhZKTndExij9VdnfYereauk0w";
    private String botToken="6774063561:AAGyiQS8jpydAfhq6G8UAjUY1-98Jb0ISPA";
    private MessageHandler messageHandler=null;
    private SendMessageUIHandler sendMessageUIHandler=null;
    //接受打赏的地址
    private final String receiveTipsAddress="0xEF16CeefEF7Fc3BCC242997930e3047C4B28a3cA";

    private Map<String,MessageState> userMessageStateMap=new HashMap<>();
    private Map<String,String> userWalletEditAddressMap=new HashMap<>();
    private Map<String,UserTradeSettingInfo> userTradeSettingInfoMap=new HashMap<>();
    private Map<String,Integer> userWalletSendMessageIdMap =new HashMap<>();
    private Map<String,SendMessage> userWalletSendMessageMap =new HashMap<>();
    private Map<String,Integer> userTradePageSendMessageIdMap =new HashMap<>();
    private Map<String,List<WalletInfo>>userWalletInfoListMap=new HashMap<>();
    private Map<String,TokenPoolInfo>userTokenPoolInfoMap=new HashMap<>();



    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {

        //初始化处理器
        if(messageHandler==null)
        {
            messageHandler = SpringContextUtils.getBean(MessageHandler.class);
        }
        if(sendMessageUIHandler==null)
        {
            sendMessageUIHandler = SpringContextUtils.getBean(SendMessageUIHandler.class);
        }
        ReceiveMessage receiveMessage = getReceiveMessage(update);
        UserInfo userInfo = messageHandler.getUserInfoByTelegramId(receiveMessage.getUserId());
        if(userInfo==null)
        {
            userInfo=messageHandler.userRegister(receiveMessage.getUserId(),receiveMessage.getUserName());
        }

        try {
            executeSendMessage(userInfo.getId(),receiveMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void executeSendMessage(long userId,ReceiveMessage receiveMessage) throws TelegramApiException {
        String chatId=receiveMessage.getChatId();

        //用户点击按钮
        if(receiveMessage.getMessageType()==ReceiveMessageType.BUTTON)
        {
            //钱包模块
            if(receiveMessage.getContent().equals("wallets"))
            {
                List<WalletInfo> walletInfoList = messageHandler.getWalletInfoList(userId);
                SendMessage sendMessage = sendMessageUIHandler.walletsUI(chatId, walletInfoList);
                Message execute = execute(sendMessage);
                userWalletSendMessageIdMap.put(receiveMessage.getUserId(),execute.getMessageId());
                userWalletSendMessageMap.put(receiveMessage.getUserId(),sendMessage);
            }
            //刷新钱包
            if(receiveMessage.getContent().equals("wallet/refresh"))
            {
                List<WalletInfo> walletInfoList = messageHandler.getWalletInfoList(userId);
                SendMessage sendMessage = sendMessageUIHandler.walletsUI(chatId, walletInfoList);
                //内容改变以后才刷新
                if(!sendMessage.getText().equals(userWalletSendMessageMap.get(receiveMessage.getUserId()).getText()))
                {
                    EditMessageText editMessageText = EditMessageText
                            .builder()
                            .messageId(userWalletSendMessageIdMap.get(receiveMessage.getUserId()))
                            .text(sendMessage.getText())
                            .replyMarkup((InlineKeyboardMarkup) sendMessage.getReplyMarkup())
                            .chatId(chatId)
                            .build();
                    execute(editMessageText);
                }
            }
            //创建一个新钱包
            if(receiveMessage.getContent().equals("wallet/createOne"))
            {
                List<WalletInfo> newWalletInfoList = messageHandler.createWallets(userId, 1);
                SendMessage sendMessage = sendMessageUIHandler.createWallets(chatId, newWalletInfoList);
                List<WalletInfo> walletInfoList = messageHandler.getWalletInfoList(userId);
                SendMessage sendMessage2 = sendMessageUIHandler.walletsUI(chatId, walletInfoList);
                EditMessageText editMessageText = EditMessageText
                        .builder()
                        .messageId(userWalletSendMessageIdMap.get(receiveMessage.getUserId()))
                        .text(sendMessage2.getText())
                        .replyMarkup((InlineKeyboardMarkup) sendMessage2.getReplyMarkup())
                        .chatId(chatId)
                        .build();
                execute(editMessageText);
                execute(sendMessage);
            }
            //创建5个新钱包
            if(receiveMessage.getContent().equals("wallet/createFive"))
            {
                List<WalletInfo> newWalletInfoList = messageHandler.createWallets(userId, 5);
                SendMessage sendMessage = sendMessageUIHandler.createWallets(chatId, newWalletInfoList);
                List<WalletInfo> walletInfoList = messageHandler.getWalletInfoList(userId);
                SendMessage sendMessage2 = sendMessageUIHandler.walletsUI(chatId, walletInfoList);
                EditMessageText editMessageText = EditMessageText
                        .builder()
                        .messageId(userWalletSendMessageIdMap.get(receiveMessage.getUserId()))
                        .text(sendMessage2.getText())
                        .replyMarkup((InlineKeyboardMarkup) sendMessage2.getReplyMarkup())
                        .chatId(chatId)
                        .build();
                execute(editMessageText);
                execute(sendMessage);
            }
            //钱包详情
            if(receiveMessage.getContent().indexOf("wallet/details")==0)
            {
                String[] contentArr = receiveMessage.getContent().split("-");
                String address=contentArr[1];
                userWalletEditAddressMap.put(receiveMessage.getUserId(),address);
                WalletInfo walletInfo = messageHandler.getWalletByAddress(userId, address);
                SendMessage sendMessage = sendMessageUIHandler.walletDetails(chatId, walletInfo);
                EditMessageText editMessageText = EditMessageText
                        .builder()
                        .messageId(userWalletSendMessageIdMap.get(receiveMessage.getUserId()))
                        .text(sendMessage.getText())
                        .replyMarkup((InlineKeyboardMarkup) sendMessage.getReplyMarkup())
                        .chatId(chatId)
                        .build();
                execute(editMessageText);
            }
            //编辑某个钱包
            if(receiveMessage.getContent().indexOf("wallet/detail/edit")==0)
            {
                SendMessage sendMessage = sendMessageUIHandler.walletEditWaitPrivateKey(chatId);
                execute(sendMessage);
                userMessageStateMap.put(receiveMessage.getUserId(),MessageState.WalletEdit);
            }
            //返回钱包列表
            if(receiveMessage.getContent().indexOf("wallet/detail/return")==0)
            {
                List<WalletInfo> walletInfoList = messageHandler.getWalletInfoList(userId);
                SendMessage sendMessage = sendMessageUIHandler.walletsUI(chatId, walletInfoList);
                EditMessageText editMessageText = EditMessageText
                        .builder()
                        .messageId(userWalletSendMessageIdMap.get(receiveMessage.getUserId()))
                        .text(sendMessage.getText())
                        .replyMarkup((InlineKeyboardMarkup) sendMessage.getReplyMarkup())
                        .chatId(chatId)
                        .build();
                execute(editMessageText);
                userMessageStateMap.put(receiveMessage.getUserId(),MessageState.MainPage);
            }
            //删除某个钱包
            if(receiveMessage.getContent().indexOf("wallet/detail/delete")==0)
            {
                SendMessage sendMessage = sendMessageUIHandler.walletDeleteOne(chatId);
                execute(sendMessage);
                userMessageStateMap.put(receiveMessage.getUserId(),MessageState.WalletDeleteOne);
            }

            //删除所有钱包
            if(receiveMessage.getContent().indexOf("wallet/deleteAll")==0)
            {
                SendMessage sendMessage = sendMessageUIHandler.walletDeleteAll(chatId);
                execute(sendMessage);
                userMessageStateMap.put(receiveMessage.getUserId(),MessageState.WalletDeleteAll);
            }

            //将BNB归集到一个钱包
            if(receiveMessage.getContent().indexOf("wallet/transfer")==0)
            {
                SendMessage sendMessage = sendMessageUIHandler.walletTransfer(chatId);
                execute(sendMessage);
                userMessageStateMap.put(receiveMessage.getUserId(),MessageState.WalletTransfer);
            }

            //进入交易界面(BuyAndSell)
            if(receiveMessage.getContent().indexOf("tradePage")==0)
            {
                SendMessage sendMessage = sendMessageUIHandler.enterTradePage(chatId);
                execute(sendMessage);
                userMessageStateMap.put(receiveMessage.getUserId(),MessageState.TradePage);
            }
            //点击 Buy Mode
            if(receiveMessage.getContent().indexOf("trade/buyMode")==0)
            {
                List<WalletInfo> walletInfoList = userWalletInfoListMap.get(receiveMessage.getUserId());
                TokenPoolInfo tokenPoolInfo = userTokenPoolInfoMap.get(receiveMessage.getUserId());
                Integer tradePageSendMessageId = userTradePageSendMessageIdMap.get(receiveMessage.getUserId());
                UserTradeSettingInfo userTradeSettingInfo = userTradeSettingInfoMap.get(receiveMessage.getUserId());
                userTradeSettingInfo.setBuy(true);

                SendMessage sendMessage = sendMessageUIHandler.tradeBuyMode(chatId,walletInfoList,tokenPoolInfo,userTradeSettingInfo,userTradeSettingInfo.isAllWallet());
                //更新界面
                EditMessageText editMessageText = EditMessageText
                        .builder()
                        .messageId(tradePageSendMessageId)
                        .text(sendMessage.getText())
                        .replyMarkup((InlineKeyboardMarkup) sendMessage.getReplyMarkup())
                        .chatId(chatId)
                        .build();
                execute(editMessageText);
            }
            //点击 Sell Mode
            if(receiveMessage.getContent().indexOf("trade/sellMode")==0)
            {
                List<WalletInfo> walletInfoList = userWalletInfoListMap.get(receiveMessage.getUserId());
                TokenPoolInfo tokenPoolInfo = userTokenPoolInfoMap.get(receiveMessage.getUserId());
                Integer tradePageSendMessageId = userTradePageSendMessageIdMap.get(receiveMessage.getUserId());
                UserTradeSettingInfo userTradeSettingInfo = userTradeSettingInfoMap.get(receiveMessage.getUserId());
                userTradeSettingInfo.setBuy(false);

                SendMessage sendMessage = sendMessageUIHandler.tradeSellMode(chatId,walletInfoList,tokenPoolInfo,userTradeSettingInfo,userTradeSettingInfo.isAllWallet());
                //更新界面
                EditMessageText editMessageText = EditMessageText
                        .builder()
                        .messageId(tradePageSendMessageId)
                        .text(sendMessage.getText())
                        .replyMarkup((InlineKeyboardMarkup) sendMessage.getReplyMarkup())
                        .chatId(chatId)
                        .build();
                execute(editMessageText);
            }

            //设置购买的滑点
            if(receiveMessage.getContent().indexOf("trade/setBuySlippage")==0)
            {
                SendMessage sendMessage = sendMessageUIHandler.enterBuySlippage(chatId);
                execute(sendMessage);
                userMessageStateMap.put(receiveMessage.getUserId(),MessageState.SetBuySlippage);
            }
            //打赏
            if(receiveMessage.getContent().indexOf("trade/giveTips")==0)
            {
                SendMessage sendMessage = sendMessageUIHandler.enterGiveTips(chatId);
                execute(sendMessage);
                userMessageStateMap.put(receiveMessage.getUserId(),MessageState.EnterGiveTips);
            }

            //刷新Buy Mode
            if(receiveMessage.getContent().indexOf("trade/refreshBuyMode")==0)
            {
                //刷新数据
                List<WalletInfo> walletInfoList =messageHandler.getWalletInfoList(userId);
                userWalletInfoListMap.put(receiveMessage.getUserId(),walletInfoList);
                TokenPoolInfo tokenPoolInfo = userTokenPoolInfoMap.get(receiveMessage.getUserId());
                tokenPoolInfo=messageHandler.getTokenPoolInfo(tokenPoolInfo.getContract());
                userTokenPoolInfoMap.put(receiveMessage.getUserId(),tokenPoolInfo);

                Integer tradePageSendMessageId = userTradePageSendMessageIdMap.get(receiveMessage.getUserId());
                UserTradeSettingInfo userTradeSettingInfo = userTradeSettingInfoMap.get(receiveMessage.getUserId());
                userTradeSettingInfo.setBuy(true);

                SendMessage sendMessage = sendMessageUIHandler.tradeBuyMode(chatId,walletInfoList,tokenPoolInfo,userTradeSettingInfo,userTradeSettingInfo.isAllWallet());
                //更新界面
                EditMessageText editMessageText = EditMessageText
                        .builder()
                        .messageId(tradePageSendMessageId)
                        .text(sendMessage.getText())
                        .replyMarkup((InlineKeyboardMarkup) sendMessage.getReplyMarkup())
                        .chatId(chatId)
                        .build();
                execute(editMessageText);
            }

            //点击购界面的所有钱包
            if(receiveMessage.getContent().indexOf("trade/buyAllWallet")==0)
            {
                List<WalletInfo> walletInfoList=userWalletInfoListMap.get(receiveMessage.getUserId());
                TokenPoolInfo tokenPoolInfo = userTokenPoolInfoMap.get(receiveMessage.getUserId());

                Integer tradePageSendMessageId = userTradePageSendMessageIdMap.get(receiveMessage.getUserId());
                UserTradeSettingInfo userTradeSettingInfo = userTradeSettingInfoMap.get(receiveMessage.getUserId());
                userTradeSettingInfo.setBuy(true);
                userTradeSettingInfo.setAllWallet(true);

                SendMessage sendMessage = sendMessageUIHandler.tradeBuyMode(chatId,walletInfoList,tokenPoolInfo,userTradeSettingInfo,userTradeSettingInfo.isAllWallet());
                //更新界面
                EditMessageText editMessageText = EditMessageText
                        .builder()
                        .messageId(tradePageSendMessageId)
                        .text(sendMessage.getText())
                        .replyMarkup((InlineKeyboardMarkup) sendMessage.getReplyMarkup())
                        .chatId(chatId)
                        .build();
                execute(editMessageText);
            }

            //刷新Sell Mode
            if(receiveMessage.getContent().indexOf("trade/refreshSellMode")==0)
            {
                //刷新数据
                List<WalletInfo> walletInfoList =messageHandler.getWalletInfoList(userId);
                userWalletInfoListMap.put(receiveMessage.getUserId(),walletInfoList);
                TokenPoolInfo tokenPoolInfo = userTokenPoolInfoMap.get(receiveMessage.getUserId());
                tokenPoolInfo=messageHandler.getTokenPoolInfo(tokenPoolInfo.getContract());
                userTokenPoolInfoMap.put(receiveMessage.getUserId(),tokenPoolInfo);

                Integer tradePageSendMessageId = userTradePageSendMessageIdMap.get(receiveMessage.getUserId());
                UserTradeSettingInfo userTradeSettingInfo = userTradeSettingInfoMap.get(receiveMessage.getUserId());
                userTradeSettingInfo.setBuy(false);

                SendMessage sendMessage = sendMessageUIHandler.tradeSellMode(chatId,walletInfoList,tokenPoolInfo,userTradeSettingInfo,userTradeSettingInfo.isAllWallet());
                //更新界面
                EditMessageText editMessageText = EditMessageText
                        .builder()
                        .messageId(tradePageSendMessageId)
                        .text(sendMessage.getText())
                        .replyMarkup((InlineKeyboardMarkup) sendMessage.getReplyMarkup())
                        .chatId(chatId)
                        .build();
                execute(editMessageText);
            }
            //点击sellAllWallet
            if(receiveMessage.getContent().indexOf("trade/sellAllWallet")==0)
            {
                List<WalletInfo> walletInfoList=userWalletInfoListMap.get(receiveMessage.getUserId());
                TokenPoolInfo tokenPoolInfo = userTokenPoolInfoMap.get(receiveMessage.getUserId());

                Integer tradePageSendMessageId = userTradePageSendMessageIdMap.get(receiveMessage.getUserId());
                UserTradeSettingInfo userTradeSettingInfo = userTradeSettingInfoMap.get(receiveMessage.getUserId());
                userTradeSettingInfo.setBuy(false);
                userTradeSettingInfo.setAllWallet(true);

                SendMessage sendMessage = sendMessageUIHandler.tradeSellMode(chatId,walletInfoList,tokenPoolInfo,userTradeSettingInfo,userTradeSettingInfo.isAllWallet());
                //更新界面
                EditMessageText editMessageText = EditMessageText
                        .builder()
                        .messageId(tradePageSendMessageId)
                        .text(sendMessage.getText())
                        .replyMarkup((InlineKeyboardMarkup) sendMessage.getReplyMarkup())
                        .chatId(chatId)
                        .build();
                execute(editMessageText);
            }

            //选择购买钱包
            if(receiveMessage.getContent().indexOf("trade/selectBuyWallet")==0)
            {
                String[] contentArr = receiveMessage.getContent().split("-");
                String address=contentArr[1];
                //更新选择的钱包
                List<WalletInfo> walletInfoList = userWalletInfoListMap.get(receiveMessage.getUserId());
                TokenPoolInfo tokenPoolInfo = userTokenPoolInfoMap.get(receiveMessage.getUserId());
                WalletInfo walletInfo = walletInfoList.stream().filter(a -> a.getAddress().equals(address)).findFirst().orElse(null);
                UserTradeSettingInfo userTradeSettingInfo = userTradeSettingInfoMap.get(receiveMessage.getUserId());
                userTradeSettingInfo.setWalletInfo(walletInfo);
                SendMessage sendMessage=null;
                if(userTradeSettingInfo.isBuy())
                {
                    sendMessage = sendMessageUIHandler.tradeBuyMode(chatId,walletInfoList,tokenPoolInfo,userTradeSettingInfo,userTradeSettingInfo.isAllWallet());
                }else{
                    sendMessage = sendMessageUIHandler.tradeSellMode(chatId,walletInfoList,tokenPoolInfo,userTradeSettingInfo,userTradeSettingInfo.isAllWallet());
                }

                //更新界面
                Integer tradePageSendMessageId = userTradePageSendMessageIdMap.get(receiveMessage.getUserId());
                EditMessageText editMessageText = EditMessageText
                        .builder()
                        .messageId(tradePageSendMessageId)
                        .text(sendMessage.getText())
                        .replyMarkup((InlineKeyboardMarkup) sendMessage.getReplyMarkup())
                        .chatId(chatId)
                        .build();
                execute(editMessageText);
            }


            //用多少BNB购买
            if(receiveMessage.getContent().indexOf("trade/buyWithBnbAmount")==0)
            {
                SendMessage sendMessage = sendMessageUIHandler.enterBuyWithBnbAmount(chatId);
                execute(sendMessage);
                userMessageStateMap.put(receiveMessage.getUserId(),MessageState.BuyWithBnbAmount);
            }

            //用0.1BNB购买
            if(receiveMessage.getContent().indexOf("trade/fixedBuy")==0)
            {
                UserTradeSettingInfo userTradeSettingInfo = userTradeSettingInfoMap.get(receiveMessage.getUserId());
                if(userTradeSettingInfo.getWalletInfo()!=null)
                {
                    String hash=messageHandler.buyToken(userId,userTradeSettingInfo.getTokenAddress(),
                            userTradeSettingInfo.getWalletInfo().getPrivateKey(), 0.0001,userTradeSettingInfo.getSlippage());
                    String text="";
                    if(hash!=null)
                    {
                        text=hash;
                    }else{
                        text="Buy Fail";
                    }
                    SendMessage send = SendMessage.builder()
                            .chatId(chatId)
                            .text(text)
                            .build();
                    execute(send);
                }
            }

            //卖出10%
            if(receiveMessage.getContent().indexOf("trade/sellPercent10")==0)
            {
                UserTradeSettingInfo userTradeSettingInfo = userTradeSettingInfoMap.get(receiveMessage.getUserId());
                if(userTradeSettingInfo.getWalletInfo()!=null)
                {
                    String result=messageHandler.sellToken(userId,userTradeSettingInfo.getTokenAddress(),
                            userTradeSettingInfo.getWalletInfo().getPrivateKey(),userTradeSettingInfo.getSlippage(),0.1);
                    SendMessage send = SendMessage.builder()
                            .chatId(chatId)
                            .text(result)
                            .build();
                    execute(send);
                }
            }
            //卖出50%
            if(receiveMessage.getContent().indexOf("trade/sellPercent50")==0)
            {
                UserTradeSettingInfo userTradeSettingInfo = userTradeSettingInfoMap.get(receiveMessage.getUserId());
                if(userTradeSettingInfo.getWalletInfo()!=null)
                {
                    String result=messageHandler.sellToken(userId,userTradeSettingInfo.getTokenAddress(),
                            userTradeSettingInfo.getWalletInfo().getPrivateKey(),userTradeSettingInfo.getSlippage(),0.5);
                    SendMessage send = SendMessage.builder()
                            .chatId(chatId)
                            .text(result)
                            .build();
                    execute(send);
                }
            }
            //全部卖出
            if(receiveMessage.getContent().indexOf("trade/sellAll")==0)
            {
                UserTradeSettingInfo userTradeSettingInfo = userTradeSettingInfoMap.get(receiveMessage.getUserId());
                if(userTradeSettingInfo.getWalletInfo()!=null)
                {
                    String result=messageHandler.sellToken(userId,userTradeSettingInfo.getTokenAddress(),
                            userTradeSettingInfo.getWalletInfo().getPrivateKey(),userTradeSettingInfo.getSlippage(),1);
                    SendMessage send = SendMessage.builder()
                            .chatId(chatId)
                            .text(result)
                            .build();
                    execute(send);
                }
            }
            //指定卖出的额度
            if(receiveMessage.getContent().indexOf("trade/enterSellAmount")==0)
            {
                SendMessage send = SendMessage.builder()
                        .chatId(chatId)
                        .text("Enter token amount you want to sell")
                        .build();
                execute(send);
                userMessageStateMap.put(receiveMessage.getUserId(),MessageState.EnterSellAmount);
            }


        }




        //用户聊天框输入
        else if(receiveMessage.getMessageType()==ReceiveMessageType.TEXT)
        {
            if(receiveMessage.getContent().equals("/start")||receiveMessage.getContent().equals("/wallet"))
            {
                SendMessage sendMessage = sendMessageUIHandler.mainUI(receiveMessage.getChatId());
                execute(sendMessage);
            }

//            //首页
//            if(userMessageStateMap.get(receiveMessage.getUserId())==MessageState.MainPage)
//            {
//                SendMessage sendMessage = sendMessageUIHandler.mainUI(receiveMessage.getChatId());
//                execute(sendMessage);
//            }
            //钱包更新私钥
            else if(userMessageStateMap.get(receiveMessage.getUserId())==MessageState.WalletEdit)
            {
                String content = receiveMessage.getContent();
                String privateKey=content.trim().toLowerCase(Locale.ROOT);
                WalletInfo walletInfo = messageHandler.createWalletByPrivateKey(userId, privateKey, userWalletEditAddressMap.get(receiveMessage.getUserId()));
                String text=null;
                if(walletInfo!=null)
                {
                    text="Your private key is Updated!";

                    SendMessage sendMessage = sendMessageUIHandler.walletDetails(chatId, walletInfo);
                    EditMessageText editMessageText = EditMessageText
                            .builder()
                            .messageId(userWalletSendMessageIdMap.get(receiveMessage.getUserId()))
                            .text(sendMessage.getText())
                            .replyMarkup((InlineKeyboardMarkup) sendMessage.getReplyMarkup())
                            .chatId(chatId)
                            .build();
                    execute(editMessageText);
                }else{
                    text="Invalid private key ";
                }

                SendMessage sendMessage = SendMessage.builder()
                        .chatId(chatId)
                        .text(text)
                        .build();
                execute(sendMessage);
            }
            //删除单个钱包
            else if(userMessageStateMap.get(receiveMessage.getUserId())==MessageState.WalletDeleteOne)
            {
                String content = receiveMessage.getContent();
                String yesCommand=content.trim().toLowerCase(Locale.ROOT);
                if(yesCommand.equals("yes"))
                {
                    messageHandler.walletDeleteOne(userId,userWalletEditAddressMap.get(receiveMessage.getUserId()));
                    SendMessage sendMessage = SendMessage.builder()
                            .chatId(chatId)
                            .text("Your wallet is deleted")
                            .build();
                    execute(sendMessage);

                    List<WalletInfo> walletInfoList = messageHandler.getWalletInfoList(userId);
                    SendMessage sendMessage2 = sendMessageUIHandler.walletsUI(chatId,walletInfoList);
                    EditMessageText editMessageText = EditMessageText
                            .builder()
                            .messageId(userWalletSendMessageIdMap.get(receiveMessage.getUserId()))
                            .text(sendMessage2.getText())
                            .replyMarkup((InlineKeyboardMarkup) sendMessage2.getReplyMarkup())
                            .chatId(chatId)
                            .build();
                    execute(editMessageText);
                    userMessageStateMap.put(receiveMessage.getUserId(),MessageState.MainPage);
                }
            }

            //删除
            else if(userMessageStateMap.get(receiveMessage.getUserId())==MessageState.WalletDeleteAll)
            {
                String content = receiveMessage.getContent();
                String yesCommand=content.trim().toLowerCase(Locale.ROOT);
                if(yesCommand.equals("yes"))
                {
                    messageHandler.walletDeleteAll(userId);
                    SendMessage sendMessage = SendMessage.builder()
                            .chatId(chatId)
                            .text("All your wallets are removed")
                            .build();
                    execute(sendMessage);

                    List<WalletInfo> walletInfoList = messageHandler.getWalletInfoList(userId);
                    SendMessage sendMessage2 = sendMessageUIHandler.walletsUI(chatId,walletInfoList);
                    EditMessageText editMessageText = EditMessageText
                            .builder()
                            .messageId(userWalletSendMessageIdMap.get(receiveMessage.getUserId()))
                            .text(sendMessage2.getText())
                            .replyMarkup((InlineKeyboardMarkup) sendMessage2.getReplyMarkup())
                            .chatId(chatId)
                            .build();
                    execute(editMessageText);
                    userMessageStateMap.put(receiveMessage.getUserId(),MessageState.MainPage);
                }
            }

            //归集BNB
            else if(userMessageStateMap.get(receiveMessage.getUserId())==MessageState.WalletTransfer)
            {
                String content = receiveMessage.getContent();
                String address=content;
                String amountStr="0";
                boolean isTransferAll=true;
                if(content.contains(","))
                {
                    String[] contentArr = content.split(",");
                    address=contentArr[0];
                    amountStr=contentArr[1];
                    isTransferAll=false;
                }
                boolean isValidAddress= messageHandler.checkAddressValid(address);
                String text=null;
                boolean isSufficient= messageHandler.checkSufficientBalance(userId,amountStr);
                if(!isValidAddress)
                {
                    text="Invalid address";
                }else if(!isSufficient)
                {
                    text="Insufficient balance";
                }else{
                    text="Your transfer has done";
                    messageHandler.transferToOne(userId,address,isTransferAll,Double.parseDouble(amountStr));
                    userMessageStateMap.put(receiveMessage.getUserId(),MessageState.MainPage);
                }

                SendMessage sendMessage = SendMessage.builder()
                        .chatId(chatId)
                        .text(text)
                        .build();
                execute(sendMessage);

            }

            //交易界面,输入代币地址
            else if(userMessageStateMap.get(receiveMessage.getUserId())==MessageState.TradePage) {
                String content = receiveMessage.getContent();
                String tokenAddress=content.trim().toLowerCase(Locale.ROOT);
                boolean isValid = messageHandler.checkTokenContract(tokenAddress);
                if(!isValid)
                {
                    SendMessage sendMessage = SendMessage.builder()
                            .chatId(chatId)
                            .text("Invalid token contract")
                            .build();
                    execute(sendMessage);
                    return;
                }

                TokenPoolInfo tokenPoolInfo = messageHandler.getTokenPoolInfo(tokenAddress);

                List<WalletInfo> walletInfoList = messageHandler.getWalletInfoList(userId);

                //初始化用户信息
                UserTradeSettingInfo userTradeSettingInfo = new UserTradeSettingInfo();
                userTradeSettingInfo.setTelegramId(receiveMessage.getUserId());
                userTradeSettingInfo.setBuy(true);
                userTradeSettingInfo.setSlippage(0);
                userTradeSettingInfo.setWalletInfo(walletInfoList.size()>0?walletInfoList.get(0):null);
                userTradeSettingInfo.setTokenAddress(tokenAddress);
                userTradeSettingInfoMap.put(receiveMessage.getUserId(),userTradeSettingInfo);
                userWalletInfoListMap.put(receiveMessage.getUserId(),walletInfoList);
                userTokenPoolInfoMap.put(receiveMessage.getUserId(),tokenPoolInfo);

                SendMessage sendMessage = sendMessageUIHandler.tradeBuyMode(chatId, walletInfoList,tokenPoolInfo,userTradeSettingInfo,userTradeSettingInfo.isAllWallet());
                Integer messageId = execute(sendMessage).getMessageId();
                userTradePageSendMessageIdMap.put(receiveMessage.getUserId(),messageId);

            }
            //设置滑点
            else if(userMessageStateMap.get(receiveMessage.getUserId())==MessageState.SetBuySlippage){
                String content = receiveMessage.getContent();
                String slippageStr=content.trim().toLowerCase(Locale.ROOT);
                try{
                    double slippage = Double.parseDouble(slippageStr);
                    UserTradeSettingInfo userTradeSettingInfo = userTradeSettingInfoMap.get(receiveMessage.getUserId());
                    userTradeSettingInfo.setSlippage(slippage);
                    SendMessage send = SendMessage.builder()
                            .chatId(chatId)
                            .text("Set slippage success!")
                            .build();
                    execute(send);
                    userMessageStateMap.put(receiveMessage.getUserId(),MessageState.TradePage);
                }catch (NumberFormatException exception)
                {
                    exception.printStackTrace();
                }
            }
            //打赏
            else if(userMessageStateMap.get(receiveMessage.getUserId())==MessageState.EnterGiveTips){
                String content = receiveMessage.getContent();
                String amountStr=content.trim();
                try{
                    double amount = Double.parseDouble(amountStr);
                    UserTradeSettingInfo userTradeSettingInfo = userTradeSettingInfoMap.get(receiveMessage.getUserId());
                    String result=messageHandler.giveTips(receiveTipsAddress, userTradeSettingInfo.getWalletInfo().getPrivateKey(), amount);
                    SendMessage send = SendMessage.builder()
                            .chatId(chatId)
                            .text(result)
                            .build();
                    execute(send);
                    userMessageStateMap.put(receiveMessage.getUserId(),MessageState.TradePage);
                }catch (NumberFormatException exception)
                {
                    exception.printStackTrace();
                }
            }
            //用多少BNB去购买
            else if(userMessageStateMap.get(receiveMessage.getUserId())==MessageState.BuyWithBnbAmount){
                String amountStr = receiveMessage.getContent();
                try{
                    double amount = Double.parseDouble(amountStr);
                    UserTradeSettingInfo userTradeSettingInfo = userTradeSettingInfoMap.get(receiveMessage.getUserId());
                    String hash=messageHandler.buyToken(userId,userTradeSettingInfo.getTokenAddress(),
                            userTradeSettingInfo.getWalletInfo().getPrivateKey(), amount,userTradeSettingInfo.getSlippage());
                    String text="";
                    if(hash!=null)
                    {
                        text=hash;
                    }else{
                        text="Buy Fail";
                    }

                    SendMessage send = SendMessage.builder()
                            .chatId(chatId)
                            .text(text)
                            .build();
                    execute(send);
                    userMessageStateMap.put(receiveMessage.getUserId(),MessageState.TradePage);
                }catch (NumberFormatException exception)
                {
                    exception.printStackTrace();
                }
            }

            //卖出多少代币
            else if(userMessageStateMap.get(receiveMessage.getUserId())==MessageState.EnterSellAmount){
                String amountStr = receiveMessage.getContent();
                try{
                    double amount = Double.parseDouble(amountStr);
                    UserTradeSettingInfo userTradeSettingInfo = userTradeSettingInfoMap.get(receiveMessage.getUserId());
                    String result=messageHandler.sellTokenWithAmount(userId,userTradeSettingInfo.getTokenAddress(),
                            userTradeSettingInfo.getWalletInfo().getPrivateKey(), amount,userTradeSettingInfo.getSlippage());
                    String text="";
                    if(result!=null)
                    {
                        text=result;
                    }else{
                        text="Buy Fail";
                    }

                    SendMessage send = SendMessage.builder()
                            .chatId(chatId)
                            .text(text)
                            .build();
                    execute(send);
                    userMessageStateMap.put(receiveMessage.getUserId(),MessageState.TradePage);
                }catch (NumberFormatException exception)
                {
                    exception.printStackTrace();
                }
            }



        }
    }


    private ReceiveMessage getReceiveMessage(Update update)
    {
        ReceiveMessage message = new ReceiveMessage();
        message.setId(update.getUpdateId().toString());
        if(update.getMessage()!=null)
        {
            message.setMessageType(ReceiveMessageType.TEXT);
            message.setChatId(update.getMessage().getChat().getId().toString());
            message.setUserId(update.getMessage().getFrom().getId().toString());
            message.setUserName(update.getMessage().getFrom().getUserName());
            message.setContent(update.getMessage().getText());
        }else if(update.getCallbackQuery()!=null)
        {
            message.setMessageType(ReceiveMessageType.BUTTON);
            message.setChatId(update.getCallbackQuery().getMessage().getChat().getId().toString());
            message.setUserId(update.getCallbackQuery().getFrom().getId().toString());
            message.setUserName(update.getCallbackQuery().getFrom().getUserName());
            message.setContent(update.getCallbackQuery().getData());
        }
        return message;
    }


}
