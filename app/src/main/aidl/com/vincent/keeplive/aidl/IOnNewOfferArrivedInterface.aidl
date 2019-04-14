// IOnNewOfferArrivedInterface.aidl
package com.vincent.keeplive.aidl;

import com.vincent.keeplive.aidl.Offer;
//  offer 观察接口
interface IOnNewOfferArrivedInterface {
    void onNewOfferArrived(in Offer offer);
}
