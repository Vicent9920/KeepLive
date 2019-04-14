package com.vincent.keeplive

import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.*
import android.util.Log
import com.vincent.keeplive.aidl.IOnNewOfferArrivedInterface
import com.vincent.keeplive.aidl.JobsInterface
import com.vincent.keeplive.aidl.Offer

/**
 * <p>文件描述：服务端service<p>
 * <p>@author 烤鱼<p>
 * <p>@date 2019/4/14 0014 <p>
 * <p>@update 2019/4/14 0014<p>
 * <p>版本号：1<p>
 *
 */
class RemoteService : Service() {

    private val TAG = this.javaClass.simpleName
    // offer 容器
    private val mList = mutableListOf<Offer>()
    // aidl 接口专用容器
    private val mListenerList = RemoteCallbackList<IOnNewOfferArrivedInterface>()
    private val mBinder = object : JobsInterface.Stub(){
        override fun registerListener(listener: IOnNewOfferArrivedInterface?) {
            mListenerList.register(listener)
        }

        override fun unregisterListener(listener: IOnNewOfferArrivedInterface?) {
            mListenerList.unregister(listener)
        }

        override fun queryOffers(): MutableList<Offer> {
            return mList;
        }

        override fun addOffer(offer: Offer) {
            mList.add(offer)
            // 向客户端通信
            val size = mListenerList.beginBroadcast()
            for (i in 0 until size ){
                val listener = mListenerList.getBroadcastItem(i)
                listener.onNewOfferArrived(offer)
            }
            mListenerList.finishBroadcast()
        }

        override fun onTransact(code: Int, data: Parcel, reply: Parcel?, flags: Int): Boolean {
            // 验证权限 返回false代表权限未验证通过
            val check = checkCallingOrSelfPermission("com.vincent.keeplive.permission.ACCESS_OFFER_SERVICE")
            Log.e(TAG,"checkCallingOrSelfPermission:$check")
            if(check == PackageManager.PERMISSION_DENIED){
                return false
            }
            val packages = packageManager.getPackagesForUid(Binder.getCallingUid())
            if(packages != null && packages.size>0){
                Log.e(TAG,"packageName:${packages[0]}")
                if(!packages[0].endsWith("keeplive")){
                    return false
                }
            }
            return super.onTransact(code, data, reply, flags)
        }
    }


    override fun onCreate() {
        super.onCreate()
        mList.add(Offer(5000, "智联招聘"))

    }

    override fun onBind(intent: Intent): IBinder {
        Handler().postDelayed({
            mBinder.addOffer(Offer(4500,"51job"))
        },1000)
        return mBinder
    }
}