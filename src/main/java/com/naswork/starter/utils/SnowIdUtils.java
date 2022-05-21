package com.naswork.starter.utils;

import cn.hutool.core.net.NetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author xub
 * @Description: 雪花算法
 * 2019/8/14 下午8:22
 */
@Slf4j
public class SnowIdUtils {
  /**
   * 私有的 内部静态类
   */
  private static class SnowFlake {

    /**
     * 内部类对象
     */
    private static final SnowIdUtils.SnowFlake SNOW_FLAKE = new SnowIdUtils.SnowFlake();

    /**
     * 起始的时间戳
     */
    private final long START_TIMESTAMP = 1557489395327L;
    /**
     * 序列号占用位数
     */
    private final long SEQUENCE_BIT = 12;
    /**
     * 机器标识占用位数
     */
    private final long MACHINE_BIT = 10;

    /**
     * 时间戳位移位数
     */
    private final long TIMESTAMP_LEFT = SEQUENCE_BIT + MACHINE_BIT;

    /**
     * 最大序列号  （4095）
     */
    private final long MAX_SEQUENCE = ~(-1L << SEQUENCE_BIT);
    /**
     * 最大机器编号 （1023）
     */
    private final long MAX_MACHINE_ID = ~(-1L << MACHINE_BIT);
    /**
     * 生成id机器标识部分
     */
    private long machineIdPart;
    /**
     * 序列号
     */
    private long sequence = 0L;

    /**
     * 上一次时间戳
     */
    private long lastStamp = -1L;

    /**
     * 数据中心ID(0~31),配置文件获取，为空则默认为0
     */
    @Value("${snowflake.datacenter:0}")
    private long datacenterId;

    /**
     * 数据标识id向左移22位(SEQUENCE_BIT+MACHINE_BIT)
     */
    private final long datacenterIdShift = SEQUENCE_BIT + MACHINE_BIT;

    /**
     * 构造函数初始化机器编码
     */
    private SnowFlake() {
      //模拟这里获得本机机器编码
      //long localIp = 4321;
      long localIp = NetUtil.ipv4ToLong(NetUtil.getLocalhost().getHostAddress());
      //localIp & MAX_MACHINE_ID最大不会超过1023,在左位移12位
      machineIdPart = (localIp & MAX_MACHINE_ID) << SEQUENCE_BIT;
    }

    /**
     * 获取雪花ID
     */
    public synchronized long nextId() {
      long currentStamp = timeGen();
      //避免机器时钟回拨
      while (currentStamp < lastStamp) {
        // //服务器时钟被调整了,ID生成器停止服务.
        throw new RuntimeException(String.format("时钟已经回拨.  Refusing to generate id " +
            "for %d milliseconds", lastStamp - currentStamp));
      }
      if (currentStamp == lastStamp) {
        // 每次+1
        sequence = (sequence + 1) & MAX_SEQUENCE;
        // 毫秒内序列溢出
        if (sequence == 0) {
          // 阻塞到下一个毫秒,获得新的时间戳
          currentStamp = getNextMill();
        }
      } else {
        //不同毫秒内，序列号置0
        sequence = 0L;
      }
      lastStamp = currentStamp;
      //时间戳部分+机器标识部分+序列号部分
      return (currentStamp - START_TIMESTAMP) << TIMESTAMP_LEFT
          | (datacenterId << datacenterIdShift)
          | machineIdPart
          | sequence
          ;
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     */
    private long getNextMill() {
      long mill = timeGen();
      //
      while (mill <= lastStamp) {
        mill = timeGen();
      }
      return mill;
    }

    /**
     * 返回以毫秒为单位的当前时间
     */
    protected long timeGen() {
      return System.currentTimeMillis();
    }
  }

  /**
   * 获取long类型雪花ID
   */
  public static long uniqueLong() {
    return SnowIdUtils.SnowFlake.SNOW_FLAKE.nextId();
  }

  /**
   * 获取String类型雪花ID
   */
  public static String uniqueLongHex() {
    return String.format("%016x", uniqueLong());
  }

  public static void main(String[] args) {
    System.out.println(uniqueLong());
  }

}
