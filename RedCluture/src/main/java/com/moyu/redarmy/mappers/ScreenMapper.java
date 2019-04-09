package com.moyu.redarmy.mappers;


import com.moyu.redarmy.model.Screen;
import org.apache.ibatis.annotations.Param;

public interface ScreenMapper {
    Screen selectScreen(@Param("deviceId") int deviceId);
    int insertScreen(Screen screen);
    int deleteScreen(@Param("deviceId") int deviceId);

}
