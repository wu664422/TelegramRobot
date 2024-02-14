package com.yy.bscRobot.robot;

import com.yy.bscRobot.entity.TokenPoolInfo;
import com.yy.bscRobot.entity.UserTradeSettingInfo;
import com.yy.bscRobot.pojo.WalletInfo;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;

@Component
public class SendMessageUIHandler {

    public SendMessage mainUI(String chatId)
    {
        InlineKeyboardButton button11 = InlineKeyboardButton.builder().text("\uD83C\uDF89 Buy&Sell").callbackData("tradePage").build();
        InlineKeyboardButton button12 = InlineKeyboardButton.builder().text("\uD83D\uDE80 Token Sniper").callbackData("tokenSniper").build();
        InlineKeyboardButton button21 = InlineKeyboardButton.builder().text("⚙️Settings").callbackData("settings").build();
        InlineKeyboardButton button22 = InlineKeyboardButton.builder().text("\uD83D\uDC5D Wallets").callbackData("wallets").build();
        InlineKeyboardButton button23 = InlineKeyboardButton.builder().text("⏰  History").callbackData("history").build();
        List<InlineKeyboardButton> rowList1 = new ArrayList<>();
        List<InlineKeyboardButton> rowList2 = new ArrayList<>();
        Collections.addAll(rowList1,button11,button12);
        Collections.addAll(rowList2,button21,button22,button23);
        List<List<InlineKeyboardButton>> list = new ArrayList<>();
        Collections.addAll(list, rowList1, rowList2);
        InlineKeyboardMarkup inlineKeyboardMarkup = InlineKeyboardMarkup.builder().keyboard(list).build();

        String text="\uD83D\uDE80An Trading Bot: Your Best";
        text+="Sniper and DeFi Tool\uD83D\uDE80\n";
        text+="\n";
        text+="Create an wallet by clicking /Wallets\n";

        SendMessage sendMessage=SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .replyMarkup(inlineKeyboardMarkup)
                .build();
        return sendMessage;
    }

    public SendMessage walletsUI(String chatId, List<WalletInfo>walletInfoList)
    {
        double totalBalance = walletInfoList.stream().mapToDouble(WalletInfo::getBnbBalance).sum();
        String totalBalanceStr =String.format("%.5f",totalBalance);
        List<List<InlineKeyboardButton>> list=new ArrayList<>();
        String text=null;
        if(walletInfoList==null||walletInfoList.size()==0)
        {
            text="you don't have any wallet yet,please create a wallet first";
        }else{
            text="you have "+walletInfoList.size()+" wallets "+totalBalanceStr+" $BNB";
            for(int i=0;i<walletInfoList.size();i++)
            {
                //格式化显示
                String balance =String.format("%.5f",walletInfoList.get(i).getBnbBalance());
                String buttonName=i+1+".("+balance+" BNB) "+walletInfoList.get(i).getAddress();
                String callbackData="wallet/details-"+walletInfoList.get(i).getAddress();
                InlineKeyboardButton button = InlineKeyboardButton.builder().text(buttonName).callbackData(callbackData).build();
                list.add(Arrays.asList(button));
            }
        }

        InlineKeyboardButton button21 = InlineKeyboardButton.builder().text("+Create New Wallet").callbackData("wallet/createOne").build();
        InlineKeyboardButton button22 = InlineKeyboardButton.builder().text("+Create 5 Wallets").callbackData("wallet/createFive").build();
        InlineKeyboardButton button31 = InlineKeyboardButton.builder().text("Transfer All to one").callbackData("wallet/transfer").build();
        InlineKeyboardButton button32 = InlineKeyboardButton.builder().text("Refresh").callbackData("wallet/refresh").build();
        InlineKeyboardButton button41 = InlineKeyboardButton.builder().text("Delete All").callbackData("wallet/deleteAll").build();
        List<InlineKeyboardButton> rowList2 = new ArrayList<>();
        List<InlineKeyboardButton> rowList3 = new ArrayList<>();
        List<InlineKeyboardButton> rowList4 = new ArrayList<>();
        Collections.addAll(rowList2,button21,button22);
        Collections.addAll(rowList3,button31,button32);
        Collections.addAll(rowList4,button41);
        Collections.addAll(list,rowList2,rowList3,rowList4);
        InlineKeyboardMarkup keyboardMarkup = InlineKeyboardMarkup.builder().keyboard(list).build();
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .replyMarkup(keyboardMarkup)
                .build();
        return sendMessage;
    }




    public SendMessage createWallets(String chatId, List<WalletInfo> newWalletInfoList) {
        String text1="Adress:\n";
        String text2="Private Key:\n";
        for(int i=0;i<newWalletInfoList.size();i++)
        {
            WalletInfo walletInfo=newWalletInfoList.get(i);
            text1+=(i+1)+". "+walletInfo.getAddress()+"\n";
            text2+=(i+1)+". "+walletInfo.getPrivateKey()+"\n";
        }
        SendMessage sendMessage = SendMessage
                .builder()
                .chatId(chatId)
                .text(text1+text2)
                .build();
        return sendMessage;
    }

    public SendMessage walletDetails(String chatId,WalletInfo walletInfo)
    {
        String  text="Adress:\n";
        text+=walletInfo.getAddress()+"\n";
        text+="PrivateKey:\n";
        text+=walletInfo.getPrivateKey()+"\n";
        String editCallbackData="wallet/detail/edit-"+walletInfo.getAddress();
        String deleteCallbackData="wallet/detail/delete-"+walletInfo.getAddress();
        String returnCallbackData="wallet/detail/return";
        InlineKeyboardButton button11 = InlineKeyboardButton.builder().text("Edit Private Key").callbackData(editCallbackData).build();
        InlineKeyboardButton button12 = InlineKeyboardButton.builder().text("Delete").callbackData(deleteCallbackData).build();
        InlineKeyboardButton button21 = InlineKeyboardButton.builder().text("Return Wallet List").callbackData(returnCallbackData).build();

        List<InlineKeyboardButton> rowList1 = new ArrayList<>();
        List<InlineKeyboardButton> rowList2 = new ArrayList<>();
        Collections.addAll(rowList1,button11,button12);
        Collections.addAll(rowList2,button21);
        List<List<InlineKeyboardButton>> list=new ArrayList<>();
        Collections.addAll(list,rowList1,rowList2);
        InlineKeyboardMarkup keyboardMarkup = InlineKeyboardMarkup.builder().keyboard(list).build();

        SendMessage sendMessage = SendMessage
                .builder()
                .chatId(chatId)
                .text(text)
                .replyMarkup(keyboardMarkup)
                .build();
        return sendMessage;
    }

    public SendMessage walletEditWaitPrivateKey(String chatId) {
        SendMessage sendMessage = SendMessage
                .builder()
                .chatId(chatId)
                .text("Update Private key To:")
                .build();
        return sendMessage;
    }

    public SendMessage walletDeleteOne(String chatId) {
        SendMessage sendMessage = SendMessage
                .builder()
                .chatId(chatId)
                .text("Are you going to delete wallet? please note that this action can't not be removed. type YES to delete wallet")
                .build();
        return sendMessage;
    }

    public SendMessage walletDeleteAll(String chatId) {
        SendMessage sendMessage = SendMessage
                .builder()
                .chatId(chatId)
                .text("Are you going to remove all your wallet? please note that this action can't not be removed. type YES to delete wallet")
                .build();
        return sendMessage;
    }

    public SendMessage walletTransfer(String chatId) {
        String text="Please enter the receiving wallet address and amount\n" +
                "Note that:\n" +
                " If you don't enter amount,it will transfer the entire remaining balance.\n" +
                " The address and amount are separeted by comma\n" +
                "\n" +
                "Example:\n" +
                "\n" +
                "Address with remaining amount:\n" +
                "0xf7bb32019da0940bd16377e8649d502e23fcfdf1\n" +
                "\n" +
                "Address with specified amount:\n" +
                "0xf7bb32019da0940bd16377e8649d502e23fcfdf1,0.35\n";
        SendMessage sendMessage = SendMessage
                .builder()
                .chatId(chatId)
                .text(text)
                .build();
        return sendMessage;
    }

    /**
     * 进入交易界面，等待输入token
     * @param chatId
     * @return
     */
    public SendMessage enterTradePage(String chatId) {
        String text="Please paste token contract that you want to buy&sell";
        SendMessage sendMessage = SendMessage
                .builder()
                .chatId(chatId)
                .text(text)
                .build();
        return sendMessage;
    }


    public SendMessage tradeBuyMode(String chatId, List<WalletInfo> walletInfoList, TokenPoolInfo tokenPoolInfo,
                                    UserTradeSettingInfo userTradeSettingInfo,boolean isAllWallet) {

        String marketCap=tokenPoolInfo.getMarketCap();
        double marketCapDouble=Double.parseDouble(tokenPoolInfo.getMarketCap());
        if(marketCapDouble>1000000)
        {
            marketCap=String.format("%.2f",marketCapDouble/1000000.0)+"M";
        }else if(marketCapDouble>1000){
            marketCap=String.format("%.2f",marketCapDouble/1000.0)+"K";
        }

        String liquidity=tokenPoolInfo.getLiquidity();
        double liquidityDouble=Double.parseDouble(liquidity);
        if(liquidityDouble>1000000)
        {
            liquidity=String.format("%.2f",liquidityDouble/1000000.0)+"M";
        }else if(liquidityDouble>1000){
            liquidity=String.format("%.2f",liquidityDouble/1000.0)+"K";
        }

        String text="\uD83D\uDE80 "+tokenPoolInfo.getSymbol()+"\n";
        text+="Contract:  "+tokenPoolInfo.getContract()+"\n";
        text+="Exchange:  "+tokenPoolInfo.getExchange()+"\n";
        text+="Market Cap:  $"+marketCap+"\n";
        text+="Liquidity:  $"+liquidity+"\n";
        text+="Token Price:  $"+String.format("%.7f",Double.parseDouble(tokenPoolInfo.getPrice()))+"\n";
        text+="Pool BNB:  $"+String.format("%.2f",Double.parseDouble(tokenPoolInfo.getPoolBNB()))+"\n";
        text+="Renounced: ❌";

        List<List<InlineKeyboardButton>>list=new ArrayList<>();

        InlineKeyboardButton button11 = InlineKeyboardButton.builder().text("---Select your trade mode---").callbackData("1").build();
        List<InlineKeyboardButton>rowList1=new ArrayList<>();
        rowList1.add(button11);
        list.add(rowList1);

        String buyModeText=userTradeSettingInfo.isBuy()?"✅ Buy Mode":"  Buy Mode";
        String sellModeText=userTradeSettingInfo.isBuy()?"  Sell Mode":"✅ Sell Mode";
        InlineKeyboardButton button21 = InlineKeyboardButton.builder().text(buyModeText).callbackData("trade/buyMode").build();
        InlineKeyboardButton button22 = InlineKeyboardButton.builder().text(sellModeText).callbackData("trade/sellMode").build();
        List<InlineKeyboardButton>rowList2=new ArrayList<>();
        rowList2.add(button21);
        rowList2.add(button22);
        list.add(rowList2);

        InlineKeyboardButton button31 = InlineKeyboardButton.builder().text("\uD83D\uDD04 Refresh").callbackData("trade/refreshBuyMode").build();
        InlineKeyboardButton button32 = InlineKeyboardButton.builder().text("\uD83D\uDD8B Set Slippage").callbackData("trade/setBuySlippage").build();
        InlineKeyboardButton button33 = InlineKeyboardButton.builder().text("Give Tips").callbackData("trade/giveTips").build();
        List<InlineKeyboardButton>rowList3=new ArrayList<>();
        rowList3.add(button31);
        rowList3.add(button32);
        rowList3.add(button33);
        list.add(rowList3);

        InlineKeyboardButton button41 = InlineKeyboardButton.builder().text("---Select your wallet---").callbackData("1").build();
        List<InlineKeyboardButton>rowList4=new ArrayList<>();
        rowList4.add(button41);
        list.add(rowList4);

        List<InlineKeyboardButton>walletList=new ArrayList<>();
        for(int i=0;i<walletInfoList.size();i++)
        {
            WalletInfo walletInfo = walletInfoList.get(i);
            String address = walletInfo.getAddress();
            Double balance = walletInfo.getBnbBalance();
            String buttonText="";
            //判断当前钱包是否处于选中状态
            if(walletInfo.getAddress().equals(userTradeSettingInfo.getWalletInfo().getAddress()))
            {
                buttonText="✅ ";
            }else{
                buttonText="❌ ";
            }
            String buttonCallbackData="trade/selectBuyWallet-"+walletInfo.getAddress();
            buttonText=buttonText+(i+1)+".("+String.format("%.5f",balance)+" $BNB) ...."+address.substring(address.length()-4);
            InlineKeyboardButton button = InlineKeyboardButton.builder().text(buttonText).callbackData(buttonCallbackData).build();
            walletList.add(button);
        }

        if(isAllWallet&&walletList.size()>3)
        {
            List<InlineKeyboardButton>rowList=null;
            for(int i=0;i<walletList.size();i++)
            {
                if(i%2==0)
                {
                    rowList=new ArrayList<>();
                }
                rowList.add(walletList.get(i));
                if(i%2==1||i==walletList.size()-1)
                {
                    list.add(rowList);
                }
            }
        }

        else if(walletList.size()==1)
        {
            List<InlineKeyboardButton>rowList5=new ArrayList<>();
            rowList5.add(walletList.get(0));
            list.add(rowList5);
        }
        else if(walletList.size()==2)
        {
            List<InlineKeyboardButton>rowList5=new ArrayList<>();
            rowList5.add(walletList.get(0));
            rowList5.add(walletList.get(1));
            list.add(rowList5);
        }
        else if(walletList.size()>2)
        {
            List<InlineKeyboardButton>rowList5=new ArrayList<>();
            rowList5.add(walletList.get(0));
            rowList5.add(walletList.get(1));
            list.add(rowList5);

            List<InlineKeyboardButton>rowList6=new ArrayList<>();
            rowList6.add(walletList.get(2));
            InlineKeyboardButton button = InlineKeyboardButton.builder().text("All Wallet").callbackData("trade/buyAllWallet").build();
            rowList6.add(button);
            list.add(rowList6);
        }

        InlineKeyboardButton button71 = InlineKeyboardButton.builder().text("---Buy Actions---").callbackData("1").build();
        List<InlineKeyboardButton>rowList7=new ArrayList<>();
        rowList7.add(button71);
        list.add(rowList7);

        InlineKeyboardButton button81 = InlineKeyboardButton.builder().text("\uD83D\uDCABBuy X BNB").callbackData("trade/buyWithBnbAmount").build();
        InlineKeyboardButton button82 = InlineKeyboardButton.builder().text("\uD83D\uDCABBuy 0.1 BNB").callbackData("trade/fixedBuy").build();
        List<InlineKeyboardButton>rowList8=new ArrayList<>();
        rowList8.add(button81);
        rowList8.add(button82);
        list.add(rowList8);

        InlineKeyboardMarkup inlineKeyboardMarkup = InlineKeyboardMarkup.builder().keyboard(list).build();

        SendMessage send = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .replyMarkup(inlineKeyboardMarkup)
                .build();
        return send;
    }

    /**
     * 等待输入滑点
     * @param chatId
     * @return
     */
    public SendMessage enterBuySlippage(String chatId) {
        SendMessage send = SendMessage.builder()
                .chatId(chatId)
                .text("Enter slippage, please enter a number under 100")
                .build();
        return send;
    }

    /**
     * 等待输入打赏金额
     * @param chatId
     * @return
     */
    public SendMessage enterGiveTips(String chatId) {
        SendMessage send = SendMessage.builder()
                .chatId(chatId)
                .text("Enter $BNB amount you want to give")
                .build();
        return send;
    }

    /**
     * 等待设置话费的BNB数量
     * @param chatId
     * @return
     */
    public SendMessage enterBuyWithBnbAmount(String chatId) {
        SendMessage send = SendMessage.builder()
                .chatId(chatId)
                .text("Enter BNB amount you want to buy")
                .build();
        return send;
    }

    /**
     * Sell Mode 界面
     * @param chatId
     * @param walletInfoList
     * @param tokenPoolInfo
     * @param userTradeSettingInfo
     * @return
     */
    public SendMessage tradeSellMode(String chatId, List<WalletInfo> walletInfoList, TokenPoolInfo tokenPoolInfo, UserTradeSettingInfo userTradeSettingInfo,boolean isAllWallet) {

        String marketCap=tokenPoolInfo.getMarketCap();
        double marketCapDouble=Double.parseDouble(tokenPoolInfo.getMarketCap());
        if(marketCapDouble>1000000)
        {
            marketCap=String.format("%.2f",marketCapDouble/1000000.0)+"M";
        }else if(marketCapDouble>1000){
            marketCap=String.format("%.2f",marketCapDouble/1000.0)+"K";
        }

        String liquidity=tokenPoolInfo.getLiquidity();
        double liquidityDouble=Double.parseDouble(liquidity);
        if(liquidityDouble>1000000)
        {
            liquidity=String.format("%.2f",liquidityDouble/1000000.0)+"M";
        }else if(liquidityDouble>1000){
            liquidity=String.format("%.2f",liquidityDouble/1000.0)+"K";
        }

        String text="\uD83D\uDE80 "+tokenPoolInfo.getSymbol()+"\n";
        text+="Contract:  "+tokenPoolInfo.getContract()+"\n";
        text+="Exchange:  "+tokenPoolInfo.getExchange()+"\n";
        text+="Market Cap:  $"+marketCap+"\n";
        text+="Liquidity:  $"+liquidity+"\n";
        text+="Token Price:  $"+String.format("%.7f",Double.parseDouble(tokenPoolInfo.getPrice()))+"\n";
        text+="Pool BNB:  $"+String.format("%.2f",Double.parseDouble(tokenPoolInfo.getPoolBNB()))+"\n";
        text+="Renounced: ❌";


        List<List<InlineKeyboardButton>>list=new ArrayList<>();

        InlineKeyboardButton button11 = InlineKeyboardButton.builder().text("---Select your trade mode---").callbackData("1").build();
        List<InlineKeyboardButton>rowList1=new ArrayList<>();
        rowList1.add(button11);
        list.add(rowList1);

        String buyModeText=userTradeSettingInfo.isBuy()?"✅ Buy Mode":"  Buy Mode";
        String sellModeText=userTradeSettingInfo.isBuy()?"  Sell Mode":"✅ Sell Mode";
        InlineKeyboardButton button21 = InlineKeyboardButton.builder().text(buyModeText).callbackData("trade/buyMode").build();
        InlineKeyboardButton button22 = InlineKeyboardButton.builder().text(sellModeText).callbackData("trade/sellMode").build();
        List<InlineKeyboardButton>rowList2=new ArrayList<>();
        rowList2.add(button21);
        rowList2.add(button22);
        list.add(rowList2);

        InlineKeyboardButton button31 = InlineKeyboardButton.builder().text("\uD83D\uDD04 Refresh").callbackData("trade/refreshSellMode").build();
        InlineKeyboardButton button32 = InlineKeyboardButton.builder().text("\uD83D\uDD8B Set Slippage %").callbackData("trade/setBuySlippage").build();
        InlineKeyboardButton button33 = InlineKeyboardButton.builder().text("Give Tips").callbackData("trade/giveTips").build();
        List<InlineKeyboardButton>rowList3=new ArrayList<>();
        rowList3.add(button31);
        rowList3.add(button32);
        rowList3.add(button33);
        list.add(rowList3);

        InlineKeyboardButton button41 = InlineKeyboardButton.builder().text("---Select your wallet---").callbackData("1").build();
        List<InlineKeyboardButton>rowList4=new ArrayList<>();
        rowList4.add(button41);
        list.add(rowList4);

        List<InlineKeyboardButton>walletList=new ArrayList<>();
        for(int i=0;i<walletInfoList.size();i++)
        {
            WalletInfo walletInfo = walletInfoList.get(i);
            String address = walletInfo.getAddress();
            Double balance = walletInfo.getBnbBalance();
            String buttonText="";
            //判断当前钱包是否处于选中状态
            if(walletInfo.getAddress().equals(userTradeSettingInfo.getWalletInfo().getAddress()))
            {
                buttonText="✅ ";
            }else{
                buttonText="❌ ";
            }
            String buttonCallbackData="trade/selectBuyWallet-"+walletInfo.getAddress();
            buttonText=buttonText+(i+1)+".("+String.format("%.5f",balance)+" $BNB) ...."+address.substring(address.length()-4);
            InlineKeyboardButton button = InlineKeyboardButton.builder().text(buttonText).callbackData(buttonCallbackData).build();
            walletList.add(button);
        }


        if(isAllWallet&&walletList.size()>3)
        {
            List<InlineKeyboardButton>rowList=null;
            for(int i=0;i<walletList.size();i++)
            {
                if(i%2==0)
                {
                    rowList=new ArrayList<>();
                }
                rowList.add(walletList.get(i));
                if(i%2==1||i==walletList.size()-1)
                {
                    list.add(rowList);
                }
            }
        }
        else if(walletList.size()==1)
        {
            List<InlineKeyboardButton>rowList5=new ArrayList<>();
            rowList5.add(walletList.get(0));
            list.add(rowList5);
        }
        else if(walletList.size()==2)
        {
            List<InlineKeyboardButton>rowList5=new ArrayList<>();
            rowList5.add(walletList.get(0));
            rowList5.add(walletList.get(1));
            list.add(rowList5);
        }
        else if(walletList.size()>2)
        {
            List<InlineKeyboardButton>rowList5=new ArrayList<>();
            rowList5.add(walletList.get(0));
            rowList5.add(walletList.get(1));
            list.add(rowList5);

            List<InlineKeyboardButton>rowList6=new ArrayList<>();
            rowList6.add(walletList.get(2));
            InlineKeyboardButton button = InlineKeyboardButton.builder().text("All Wallet").callbackData("trade/buyAllWallet").build();
            rowList6.add(button);
            list.add(rowList6);
        }

        InlineKeyboardButton button71 = InlineKeyboardButton.builder().text("---Buy Actions---").callbackData("1").build();
        List<InlineKeyboardButton>rowList7=new ArrayList<>();
        rowList7.add(button71);
        list.add(rowList7);

        InlineKeyboardButton button81 = InlineKeyboardButton.builder().text("Sell 10%").callbackData("trade/sellPercent10").build();
        InlineKeyboardButton button82 = InlineKeyboardButton.builder().text("Sell 50%").callbackData("trade/sellPercent50").build();
        List<InlineKeyboardButton>rowList8=new ArrayList<>();
        rowList8.add(button81);
        rowList8.add(button82);
        list.add(rowList8);

        InlineKeyboardButton button91 = InlineKeyboardButton.builder().text("\uD83D\uDCABSell all").callbackData("trade/sellAll").build();
        InlineKeyboardButton button92 = InlineKeyboardButton.builder().text("\uD83D\uDCABSell X token").callbackData("trade/enterSellAmount").build();
        List<InlineKeyboardButton>rowList9=new ArrayList<>();
        rowList9.add(button91);
        rowList9.add(button92);
        list.add(rowList9);

        InlineKeyboardMarkup inlineKeyboardMarkup = InlineKeyboardMarkup.builder().keyboard(list).build();

        SendMessage send = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .replyMarkup(inlineKeyboardMarkup)
                .build();
        return send;


    }



}
