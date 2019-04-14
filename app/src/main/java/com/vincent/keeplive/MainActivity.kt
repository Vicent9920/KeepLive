package com.vincent.keeplive

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import com.vincent.keeplive.aidl.IOnNewOfferArrivedInterface
import com.vincent.keeplive.aidl.JobsInterface
import com.vincent.keeplive.aidl.Offer

/**
 * 客户端
 */
class MainActivity : AppCompatActivity() {

    private val TAG = this.javaClass.simpleName
    var manager:JobsInterface? = null
    private val mConnection = object :ServiceConnection{
        override fun onServiceDisconnected(name: ComponentName?) {

        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
             manager = JobsInterface.Stub.asInterface(service)
            val list = manager?.queryOffers()
            Log.e(TAG,"list type：${list?.javaClass?.canonicalName}")
            Log.e(TAG,"queryOffers:${list.toString()}")
            manager?.registerListener(mArrivedListener)
            service?.linkToDeath({
                // Binder 连接死亡回调 此处需要重置 manager 并发起重连
            },0)
        }
    }

    private val mArrivedListener = object : IOnNewOfferArrivedInterface.Stub(){
        override fun onNewOfferArrived(offer: Offer?) {
            Log.e(TAG,"ThreadId:${Thread.currentThread().id}    offer:${offer}")
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            requestPermissions(arrayOf("com.vincent.keeplive.permission.ACCESS_OFFER_SERVICE"),1000)
//        }
        val i = Intent().setClassName("com.vincent.keeplive","com.vincent.keeplive.RemoteService")
//        startService(i)
        bindService(i,mConnection,Context.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        manager?.let {
            if(it.asBinder().isBinderAlive){
                it.unregisterListener(mArrivedListener)
            }
        }
        unbindService(mConnection)
        super.onDestroy()
    }
}
