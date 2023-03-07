package com.pu.demo.utils

import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {


    fun chatMessageShowTime(before: Long, current: Long): String? {

        // 时间差
        val c = current - before

        val day = 1000 * 60 * 60 * 24
        val minute2 = 1000 * 60 * 1

        // 大于1天显示日期
        if (c > day) {
            return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(current)?.toString()
        }

        // 大于5分钟显示时分秒
        if (c > minute2) {
            return SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(current)?.toString()
        }
        return null
    }

    fun contactLastSeenShowTime(lastSeen: Long): String? {
        val current = System.currentTimeMillis()

        // 时间差
        val c = current - lastSeen

        val day:Long = 1000 * 60 * 60 * 24
        val minute2 = 1000 * 60 * 1


        // 大于1天显示日期
        if (c > day * 30) {
            return "一个月前"
        }

        if (c > day * 7) {
            return "一周前"
        }

        if (c > day * 3) {
            return "最近在线 " +SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(lastSeen)?.toString()
        }

        if (c > day * 2) {
            return "前天 " + SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(lastSeen)?.toString()
        }

        if (c > day) {
            return "昨天 " + SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(lastSeen)?.toString()
        }

        // 大于5分钟显示时分秒
        if (c > minute2) {
            return "今天 " +  SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(lastSeen)?.toString()
        }

        return "刚刚"
    }

}