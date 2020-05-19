package me.kailip;

import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {
    public static void main(String args[]) throws Exception{
        // このファクトリインスタンスは再利用可能でスレッドセーフです
        Twitter twitter = TwitterFactory.getSingleton();
        twitter.setOAuthConsumer("Consumer", "Secret");
        RequestToken requestToken = twitter.getOAuthRequestToken();
        AccessToken accessToken = null;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (null == accessToken) {
            System.out.println(requestToken.getAuthorizationURL());
            System.out.print("上のリンクを開いてPINを入力してね！:");
            String pin = br.readLine();
            try{
                if(pin.length() > 0){
                    accessToken = twitter.getOAuthAccessToken(requestToken, pin);
                }else{
                    accessToken = twitter.getOAuthAccessToken();
                }
            } catch (TwitterException te) {
                if(401 == te.getStatusCode()){
                    System.out.println("Unable to get the access token.");
                }else{
                    te.printStackTrace();
                }
            }
        }
        User user = twitter.verifyCredentials();

        IDs id = twitter.getFollowersIDs(user.getScreenName(), -1);
        int count = 0;
        for (long i : id.getIDs()) {
            twitter.createBlock(i);
            count++;
            twitter.destroyBlock(i);
            Thread.sleep(50);
        }
        System.out.println("合計" + count + "人ブロ解しました！");
        System.exit(0);
    }
}
