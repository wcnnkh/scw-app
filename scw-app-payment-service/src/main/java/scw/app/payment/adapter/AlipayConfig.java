package scw.app.payment.adapter;

import java.io.Serializable;

import scw.beans.annotation.ConfigurationProperties;

@ConfigurationProperties(prefix="alipay")
public class AlipayConfig implements Serializable {
	private static final long serialVersionUID = 1L;
	private String publicKey;
	private String appId;
	private String dataType = "json";// json
	private String charset = "UTF-8";
	private String signType;// RSA2
	private String privateKey;

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}
}
