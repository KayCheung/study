package com.housaire;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.request.OapiMessageSendToConversationRequest;
import com.dingtalk.api.request.OapiServiceGetAuthInfoRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.dingtalk.api.response.OapiMessageCorpconversationAsyncsendV2Response;
import com.dingtalk.api.response.OapiMessageSendToConversationResponse;
import com.dingtalk.api.response.OapiServiceGetAuthInfoResponse;
import com.taobao.api.ApiException;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) throws Exception {
//		getAccessToken();
		sendCompanyMessage(getAccessToken());
//		System.out.println(getAgentId());
	}
	
	public static String getSign() throws Exception {
		String suiteSecret = "7dZQdp4kQ4PS9rOk0gQufiT-mzgLXoQL0CUiuhR0b_d9mJzGHeA02DUANrRYY6G8";
		String stringToSign = System.currentTimeMillis() + "\nTestSuiteTicket";
		Mac mac = Mac.getInstance("HmacSHA256");
		mac.init(new SecretKeySpec(suiteSecret.getBytes("UTF-8"), "HmacSHA256"));
		byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
		String sign = new String(Base64.getEncoder().encode(signData));
		try {
	        String encoded = URLEncoder.encode(sign, "utf-8");
	        return encoded.replace("+", "%20").replace("*", "%2A")
	            .replace("~", "%7E").replace("/", "%2F");
	    } catch (UnsupportedEncodingException e) {
	        throw new IllegalArgumentException("FailedToEncodeUri", e);
	    }
	}
	
	public static Long getAgentId() throws Exception {
		DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/service/get_auth_info");
		OapiServiceGetAuthInfoRequest req1 = new OapiServiceGetAuthInfoRequest();
		req1.setAuthCorpid("ding5ddffbe6364069b435c2f4657eb6378f");
		String sign = getSign();
		System.out.println(sign);
		OapiServiceGetAuthInfoResponse response = client.execute(req1,"suiteiigjs6r6m0jd6ple", sign, "TestSuiteTicket");
		return response.getAuthInfo().getAgent().get(0).getAgentid();
	}
	
	public static String getAccessToken() throws ApiException {
		DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/gettoken");
		OapiGettokenRequest request = new OapiGettokenRequest();
//		request.setCorpid("ding5ddffbe6364069b435c2f4657eb6378f");
//		request.setCorpsecret("BCElhSO5sfQPJhiWBbOvV6QckJHTITc3QmpaWgfFS24ps4o346EH22dXYibj-dQf");
		request.setCorpid("dingb1c1bcc86464244b35c2f4657eb6378f");
		request.setCorpsecret("dkFezqxy_ZFwAu-JiNk3toZaNpjYckc5x_6onNYoBUvOmAlBS9Rfyg4EUZK_sL-h");
		request.setHttpMethod("GET");
		OapiGettokenResponse response = client.execute(request);
		String accessToken = response.getAccessToken();
		System.out.println("AccessToken: " + accessToken);
		return accessToken;
	}
	
	/*public static String getAccessToken() throws ApiException {
		DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/sso/gettoken");
		OapiSsoGettokenRequest request = new OapiSsoGettokenRequest();
		request.setCorpid("ding5ddffbe6364069b435c2f4657eb6378f");
		request.setCorpsecret("BCElhSO5sfQPJhiWBbOvV6QckJHTITc3QmpaWgfFS24ps4o346EH22dXYibj-dQf");
		request.setHttpMethod("GET");
		OapiSsoGettokenResponse response = client.execute(request);
		String accessToken = response.getAccessToken();
		System.out.println("AccessToken: " + accessToken);
		return accessToken;
	}*/
	
	public static void sendNormalMessage(String accessToken) throws ApiException {
		DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/message/send_to_conversation");

		OapiMessageSendToConversationRequest req = new OapiMessageSendToConversationRequest();
		req.setSender("张凯");
		req.setCid("ding5ddffbe6364069b435c2f4657eb6378f");

		// 文本消息
		req.setMsgtype("text");
		OapiMessageSendToConversationRequest.Text text = new OapiMessageSendToConversationRequest.Text();
		text.setContent("测试测试");
		req.setText(text);

		// 图片
		req.setMsgtype("image");
		OapiMessageSendToConversationRequest.Image image = new OapiMessageSendToConversationRequest.Image();
		image.setMediaId("@lADOdvRYes0CbM0CbA");
		req.setImage(image);

		// 文件
		req.setMsgtype("file");
		OapiMessageSendToConversationRequest.File file = new OapiMessageSendToConversationRequest.File();
		file.setMediaId("@lADOdvRYes0CbM0CbA");
		req.setFile(file);

		req.setMsgtype("markdown");
		OapiMessageSendToConversationRequest.Markdown markdown = new OapiMessageSendToConversationRequest.Markdown();
		markdown.setText("# 这是支持markdown的文本 \\n## 标题2  \\n* 列表1 \\n![alt 啊](https://gw.alipayobjects.com/zos/skylark-tools/public/files/b424a1af2f0766f39d4a7df52ebe0083.png)");
		markdown.setTitle("首屏会话透出的展示内容");
		req.setMarkdown(markdown);

		req.setMsgtype("action_card");
		OapiMessageSendToConversationRequest.ActionCard actionCard = new OapiMessageSendToConversationRequest.ActionCard();
		actionCard.setTitle("是透出到会话列表和通知的文案");
		actionCard.setMarkdown("持markdown格式的正文内");
		actionCard.setSingleTitle("查看详情");
		actionCard.setSingleUrl("https://open.dingtalk.com");
		req.setActionCard(actionCard);

		// link消息
		req.setMsgtype("link");
		OapiMessageSendToConversationRequest.Link link = new OapiMessageSendToConversationRequest.Link();
		link.setMessageUrl("https://www.baidu.com/");
		link.setPicUrl("@lADOdvRYes0CbM0CbA");
		link.setText("步扬测试");
		link.setTitle("oapi");
		req.setLink(link);

		OapiMessageSendToConversationResponse response = client.execute(req, accessToken);
		System.out.println(response.getErrmsg());
	}
	
	public static void sendCompanyMessage(String accessToken) throws ApiException {
		DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2");
		OapiMessageCorpconversationAsyncsendV2Request request = new OapiMessageCorpconversationAsyncsendV2Request();
		request.setUseridList("manager7383");//,0369021000857962
		request.setAgentId(184872071L);
		request.setToAllUser(false);

		OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
		
		/*msg.setActionCard(new OapiMessageCorpconversationAsyncsendV2Request.ActionCard());
		msg.getActionCard().setTitle("移动学习");
		msg.getActionCard().setMarkdown("### 移动学习平台");
		msg.getActionCard().setSingleTitle("恭喜您完了服务员管理第一周第一天的学习");
		msg.getActionCard().setSingleUrl("https://xibei-learn.dev.51eparty.com");
		msg.setMsgtype("action_card");
		request.setMsg(msg);*/
		
		msg.setMsgtype("markdown");
		msg.setMarkdown(new OapiMessageCorpconversationAsyncsendV2Request.Markdown());
		msg.getMarkdown().setTitle("移动学习平台");
		msg.getMarkdown().setText("### 移动学习平台\n##### 恭喜您完了服务员管理第一周第一天的学习");
		request.setMsg(msg);

		OapiMessageCorpconversationAsyncsendV2Response response = client.execute(request,accessToken);
		System.out.println(response.getBody());
	}
	
}
