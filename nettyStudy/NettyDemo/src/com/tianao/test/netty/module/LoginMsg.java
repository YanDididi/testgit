package com.tianao.test.netty.module;

import com.tianao.test.netty.module.enums.MsgType;

public class LoginMsg extends BaseMsg{

	private static final long serialVersionUID = -8966952564685219343L;
	
	public LoginMsg(String clientId) {
		super(clientId);
		this.setType(MsgType.LOGIN);
	}
}
