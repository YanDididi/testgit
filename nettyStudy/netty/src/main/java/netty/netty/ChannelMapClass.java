package netty.netty;

import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.Channel;

public class ChannelMapClass {

	public static ConcurrentHashMap<String, Channel> channelMaps = new ConcurrentHashMap<String, Channel>();


	public static void add(String id, Channel ctx) {
		channelMaps.put(id, ctx);
	}
	
	public static Channel get(String id) {
		return channelMaps.get(id);
	}
}
