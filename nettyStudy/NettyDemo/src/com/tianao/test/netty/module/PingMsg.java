package com.tianao.test.netty.module;

import com.tianao.test.netty.module.enums.MsgType;

public class PingMsg extends BaseMsg{

	private static final long serialVersionUID = -1287809029734521425L;

	public PingMsg(String clientId) {
		super(clientId);
		this.setType(MsgType.PING);
	}
}
