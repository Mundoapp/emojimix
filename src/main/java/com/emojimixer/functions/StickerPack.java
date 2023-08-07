package com.emojimixer.functions;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class StickerPack implements Parcelable {
    public String identifier;
    public String name;
    public String publisher;
    public String trayImageFile;
    public final String publisherEmail;
    public final String publisherWebsite;
    public final String privacyPolicyWebsite;
    public final String licenseAgreementWebsite;
    public String iosAppStoreLink;
    private List<Sticker> stickers;
    private long totalSize;
    public String androidPlayStoreLink;
    private boolean isWhitelisted;
    private boolean mia = false;

    public StickerPack(String identifier, String name, String publisher, String trayImageFile, String publisherEmail, String publisherWebsite, String privacyPolicyWebsite, String licenseAgreementWebsite) {
        this.identifier = identifier;
        this.name = name;
        this.publisher = publisher;
        this.trayImageFile = trayImageFile;
        this.publisherEmail = publisherEmail;
        this.publisherWebsite = publisherWebsite;
        this.privacyPolicyWebsite = privacyPolicyWebsite;
        this.licenseAgreementWebsite = licenseAgreementWebsite;
    }

    public void setIsWhitelisted(boolean isWhitelisted) {
        this.isWhitelisted = isWhitelisted;
    }

    public boolean getIsWhitelisted() {
        return isWhitelisted;
    }

    protected StickerPack(Parcel in) {
        identifier = in.readString();
        name = in.readString();
        publisher = in.readString();
        trayImageFile = in.readString();
        publisherEmail = in.readString();
        publisherWebsite = in.readString();
        privacyPolicyWebsite = in.readString();
        licenseAgreementWebsite = in.readString();
        iosAppStoreLink = in.readString();
        stickers = in.createTypedArrayList(Sticker.CREATOR);
        totalSize = in.readLong();
        androidPlayStoreLink = in.readString();
        isWhitelisted = in.readByte() != 0;
    }

    public static final Creator<StickerPack> CREATOR = new Creator<StickerPack>() {
        @Override
        public StickerPack createFromParcel(Parcel in) {
            return new StickerPack(in);
        }

        @Override
        public StickerPack[] newArray(int size) {
            return new StickerPack[size];
        }
    };
    public boolean getMia(){
        return mia;
    }
    public void setStickers(List<Sticker> stickers) {
        this.stickers = stickers;
        totalSize = 0;
        for (Sticker sticker : stickers) {
            totalSize += sticker.size;
        }
    }

    public void deleteSticker(int pos, Sticker stickers) {
        try {
            if (stickers.getMio())
                new File(stickers.getUri().getPath()).delete();
            this.stickers.remove(pos);
            Fresco.getImagePipeline().evictFromMemoryCache(stickers.getUri());
        } catch (Exception e) {
        }
    }


    public void setAndroidPlayStoreLink(String androidPlayStoreLink) {
        this.androidPlayStoreLink = androidPlayStoreLink;
    }

    public void setIosAppStoreLink(String iosAppStoreLink) {
        this.iosAppStoreLink = iosAppStoreLink;
    }
    public String getIdentifier() {
        return this.identifier;
    }
    public List<Sticker> getStickers() {
        return stickers;
    }

    public List<Sticker> getActualStickers() {
        List<Sticker> arrStickers = new ArrayList<>();
        for (Sticker s : stickers) {
            if (!TextUtils.isEmpty(s.getImageFileName()))
                arrStickers.add(s);
        }
        return arrStickers;
    }
    public long getTotalSize() {
        return totalSize;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(identifier);
        dest.writeString(name);
        dest.writeString(publisher);
        dest.writeString(trayImageFile);
        dest.writeString(publisherEmail);
        dest.writeString(publisherWebsite);
        dest.writeString(privacyPolicyWebsite);
        dest.writeString(licenseAgreementWebsite);
        dest.writeString(iosAppStoreLink);
        dest.writeTypedList(stickers);
        dest.writeLong(totalSize);
        dest.writeString(androidPlayStoreLink);
        dest.writeByte((byte) (isWhitelisted ? 1 : 0));
    }
}
