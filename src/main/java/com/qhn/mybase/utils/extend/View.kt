package com.qhn.mybase.utils.extend

import android.widget.EditText

/**
 * 1.首位输入.的时候,补全为0.
 * 2.删除“.”后面超过2位(默认)后的数据
 * 3.如果起始位置为0,且第二位跟的不是".",则无法后续输入
 */

fun EditText.editLimit(s: String, digits: Int = 2) {
    var temp = s
    //删除“.”后面超过2位后的数据
    if (temp.contains(".")) {
        if (temp.length - 1 - temp.indexOf(".") > digits) {
            temp = temp.substring(0,
                    temp.indexOf(".") + digits + 1)
            this.setText(temp)
            this.setSelection(temp.length) //光标移到最后
        }
    }

    //如果"."在起始位置,则起始位置自动补0
    if (temp.trim { it <= ' ' }.substring(0) == ".") {
        temp = "0$temp"
        this.setText(temp)
        this.setSelection(2)
    }

    //如果起始位置为0,且第二位跟的不是".",则无法后续输入
    if (temp.startsWith("0")
            && temp.trim { it <= ' ' }.length > 1) {
        if (temp.substring(1, 2) != ".") {
            this.setText(temp.subSequence(0, 1))
            this.setSelection(1)
            return
        }
    }
}