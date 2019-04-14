// JobsInterface.aidl
package com.vincent.keeplive.aidl;

// Declare any non-default types here with import statements
// 使用import语句在此声明任何非默认类型

import com.vincent.keeplive.aidl.Offer;
import com.vincent.keeplive.aidl.IOnNewOfferArrivedInterface;
interface JobsInterface {
    List<Offer> queryOffers();
    void addOffer(in Offer offer);
    void registerListener(IOnNewOfferArrivedInterface listener);
    void unregisterListener(IOnNewOfferArrivedInterface listener);
}
