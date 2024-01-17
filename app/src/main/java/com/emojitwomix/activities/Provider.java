package com.emojitwomix.activities;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.emojitwomix.BuildConfig;
import com.emojitwomix.functions.FileUtil;
import com.emojitwomix.functions.Sticker;
import com.emojitwomix.functions.StickerPack;
import com.emojitwomix.functions.VariablesEnMemoria;
import com.orhanobut.hawk.Hawk;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Provider extends ContentProvider {

    /**
     * Do not change the strings listed below, as these are used by WhatsApp. And changing these will break the interface between sticker app and WhatsApp.
     */
    public static final String STICKER_PACK_IDENTIFIER_IN_QUERY = "sticker_pack_identifier";
    public static final String STICKER_PACK_NAME_IN_QUERY = "sticker_pack_name";
    public static final String STICKER_PACK_PUBLISHER_IN_QUERY = "sticker_pack_publisher";
    public static final String STICKER_PACK_ICON_IN_QUERY = "sticker_pack_icon";
    public static final String ANDROID_APP_DOWNLOAD_LINK_IN_QUERY = "android_play_store_link";
    public static final String IOS_APP_DOWNLOAD_LINK_IN_QUERY = "ios_app_download_link";
    public static final String PUBLISHER_EMAIL = "sticker_pack_publisher_email";
    public static final String PUBLISHER_WEBSITE = "sticker_pack_publisher_website";
    public static final String PRIVACY_POLICY_WEBSITE = "sticker_pack_privacy_policy_website";
    public static final String LICENSE_AGREENMENT_WEBSITE = "sticker_pack_license_agreement_website";
    public static final String IMAGE_DATA_VERSION = "image_data_version";
    public static final String AVOID_CACHE = "whatsapp_will_not_cache_stickers";

    public static final String STICKER_FILE_NAME_IN_QUERY = "sticker_file_name";
    public static final String STICKER_FILE_EMOJI_IN_QUERY = "sticker_emoji";
    public static final String CONTENT_FILE_NAME = "contents.json";
    public static final String ANIMATED_STICKER = "animated_sticker_pack";
    public static final String STICKER_FILE_ANIMATED = "is_animated_sticker";

    public static Uri AUTHORITY_URI = new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT).authority(BuildConfig.CONTENT_PROVIDER_AUTHORITY).appendPath(Provider.METADATA).build();
    String stickertype;
    /**
     * Do not change the values in the UriMatcher because otherwise, WhatsApp will not be able to fetch the stickers from the ContentProvider.
     */
    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    static final String METADATA = "metadata";
    private static final int METADATA_CODE = 1;
    private static final int METADATA_CODE_FOR_SINGLE_PACK = 2;
    static final String STICKERS = "stickers";
    private static final int STICKERS_CODE = 3;
    static final String STICKERS_ASSET = "stickers_asset";
    private static final int STICKERS_ASSET_CODE = 4;
    private static final int STICKER_PACK_TRAY_ICON_CODE = 5;

    @Override
    public boolean onCreate() {
        Hawk.init(getContext()).build();
        VariablesEnMemoria.readStickerWhassap(getContext());
        final String authority = BuildConfig.CONTENT_PROVIDER_AUTHORITY;
        if (!authority.startsWith((getContext()).getPackageName())) {
            throw new IllegalStateException("your authority (" + authority + ") for the content provider should start with your package name: " + getContext().getPackageName());
        }
        stickertype = FileUtil.get_selectedSticker(getContext());
        MATCHER.addURI(authority, METADATA, METADATA_CODE);
        MATCHER.addURI(authority, METADATA + "/*", METADATA_CODE_FOR_SINGLE_PACK);
        MATCHER.addURI(authority, STICKERS + "/*", STICKERS_CODE);

        MATCHER.addURI(authority, STICKERS_ASSET + "/sticker.webp", STICKER_PACK_TRAY_ICON_CODE);

        /*for (String direccion : GlobalVariable.stickersWhassap) {
            MATCHER.addURI(authority, getContext().getFilesDir() + "/stickers/" + direccion, STICKERS_ASSET_CODE);
            Log.e("Stickers", getContext().getFilesDir() + "/stickers/" + direccion);
        }*/


        for (StickerPack stickerPack : getStickerPackList()) {
            //MATCHER.addURI(authority,  getContext().getFilesDir() + "/stickers/" + stickerPack., STICKERS_ASSET_CODE);
            if (stickerPack.getStickers() != null) {
                for (Sticker sticker : stickerPack.getStickers()) {
                    MATCHER.addURI(authority,  "/stickers/" + sticker.imageFileName, STICKERS_ASSET_CODE);
                }

            }
        }

        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        final int code = MATCHER.match(uri);
        if (code == METADATA_CODE) {
            return getPackForAllStickerPacks(uri);
        } else if (code == METADATA_CODE_FOR_SINGLE_PACK) {
            return getCursorForSingleStickerPack(uri);
        } else if (code == STICKERS_CODE) {
            return getStickersForAStickerPack(uri);
        } else {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }
    public List<StickerPack> getStickerPackList() {

        return (List) Hawk.get("sticker_packs",new ArrayList<StickerPack>());
    }

    @Override
    public String getType(@NonNull Uri uri) {
        final int matchCode = MATCHER.match(uri);
        switch (matchCode) {
            case METADATA_CODE:
                return "vnd.android.cursor.dir/vnd." + BuildConfig.CONTENT_PROVIDER_AUTHORITY + "." + METADATA;
            case METADATA_CODE_FOR_SINGLE_PACK:
                return "vnd.android.cursor.item/vnd." + BuildConfig.CONTENT_PROVIDER_AUTHORITY + "." + METADATA;
            case STICKERS_CODE:
                return "vnd.android.cursor.dir/vnd." + BuildConfig.CONTENT_PROVIDER_AUTHORITY + "." + STICKERS;
            case STICKERS_ASSET_CODE:
                return "image/webp";
            case STICKER_PACK_TRAY_ICON_CODE:
                return "image/png";
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        throw new UnsupportedOperationException("Not supported");
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public AssetFileDescriptor openAssetFile(@NonNull Uri uri, @NonNull String mode) {
        final List<String> pathSegments = uri.getPathSegments();
        String filename = pathSegments.get(pathSegments.size() - 1);
        if (filename.equals("sticker.webp") || filename.equals("vacio.webp")) {
            AssetManager am = Objects.requireNonNull(getContext()).getAssets();
            try {
                return am.openFd(filename);
            } catch (IOException e) {
                Log.e(Objects.requireNonNull(getContext()).getPackageName(), "IOException when getting asset file, uri:" + uri, e);
                return null;
            }
        } else {
            ParcelFileDescriptor pfd = null;
            try {
                pfd = ParcelFileDescriptor.open(new File(getContext().getFilesDir() + "/stickers/" + filename), ParcelFileDescriptor.MODE_READ_ONLY);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if (pfd != null)
                return new AssetFileDescriptor(pfd, 0, AssetFileDescriptor.UNKNOWN_LENGTH);
            else
                return null;
        }
    }

    private Cursor getPackForAllStickerPacks(@NonNull Uri uri) {
        return getStickerPackInfo(uri, true,getStickerPackList());
    }

    private Cursor getCursorForSingleStickerPack(@NonNull Uri uri) {
        final String identifier = uri.getLastPathSegment();
        for (StickerPack stickerPack : getStickerPackList()) {
            if (identifier.equals(stickerPack.identifier)) {
                return getStickerPackInfo(uri,false, Collections.singletonList(stickerPack));
            }
        }

        return getStickerPackInfo(uri, false,new ArrayList<StickerPack>());
    }

    @NonNull
    private Cursor getStickersForAStickerPack(@NonNull Uri uri) {
        final String identifier = uri.getLastPathSegment();
        boolean type = false;
        MatrixCursor  cursor = new MatrixCursor(new String[]{STICKER_FILE_NAME_IN_QUERY, STICKER_FILE_EMOJI_IN_QUERY, STICKER_FILE_ANIMATED});
        for (StickerPack stickerPack : getStickerPackList()) {
            if (identifier.equals(stickerPack.identifier)) {
                List<Sticker> stickers = Hawk.get(stickerPack.identifier);

                for (Sticker sticker : stickers) {
                    if (stickerPack.identifier.equals("AImage")){
                        type = true;
                    }
                    else {
                        type = false;
                    }
                    cursor.addRow(new Object[]{sticker.imageFileName, TextUtils.join(",", new ArrayList<>()),type});
                }
            }
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }
   /* @NonNull
    private Cursor getStickersForAStickerPack(@NonNull Uri uri) {
       // int identifier = Integer.parseInt(uri.getLastPathSegment());
        MatrixCursor cursor;
        stickertype = FileUtil.get_selectedSticker(getContext());
        boolean type = false;
        if (stickertype.equals("AImage")){
            type = true;
        }
        else {
            type = false;
        }
        *//* {
             cursor = new MatrixCursor(new String[]{STICKER_FILE_NAME_IN_QUERY, STICKER_FILE_EMOJI_IN_QUERY});
            for (int i = 0; i < GlobalVariable.stickersWhassap.size() && cursor.getCount() < 29; i++) {
                cursor.addRow(new Object[]{GlobalVariable.stickersWhassap.get(i), TextUtils.join(",", new ArrayList<>())});
            }
            if (cursor.getCount() < 1)
                cursor.addRow(new Object[]{"vacio.webp", TextUtils.join(",", new ArrayList<>())});
            if (cursor.getCount() < 2)
                cursor.addRow(new Object[]{"emoji_llorando.webp", TextUtils.join(",", new ArrayList<>())});
            if (cursor.getCount() < 3)
                cursor.addRow(new Object[]{"emoji_enamorado.webp", TextUtils.join(",", new ArrayList<>())});
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }*//*
        cursor = new MatrixCursor(new String[]{STICKER_FILE_NAME_IN_QUERY, STICKER_FILE_EMOJI_IN_QUERY, STICKER_FILE_ANIMATED});
        for (int i = 0 ; i < GlobalVariable.stickersWhassap.size() && cursor.getCount() < 29; i++) {
            cursor.addRow(new Object[]{GlobalVariable.stickersWhassap.get(i), TextUtils.join(",", new ArrayList<>()), type});
        }
        if (cursor.getCount() < 1)
            cursor.addRow(new Object[]{"vacio.webp", TextUtils.join(",", new ArrayList<>()), type});
        if (cursor.getCount() < 2)
            cursor.addRow(new Object[]{"emoji_llorando.webp", TextUtils.join(",", new ArrayList<>()), type});
        if (cursor.getCount() < 3)
            cursor.addRow(new Object[]{"emoji_enamorado.webp", TextUtils.join(",", new ArrayList<>()), type});
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }*/




    @NonNull
    private Cursor getStickerPackInfo(@NonNull Uri uri, boolean all,@NonNull List<StickerPack> stickerPackList) {
        String AndroidPlayStore_Link = "https://play.google.com/store/apps/details?id=com.emoji2.mix";
        String IosAppStore_Link = "https://itunes.apple.com/us/app/wasticker-emoji-maker-stickers/id1609761163";
        stickertype = FileUtil.get_selectedSticker(getContext());
      /*  MatrixCursor cursor = new MatrixCursor(
                new String[]{
                        STICKER_PACK_IDENTIFIER_IN_QUERY,
                        STICKER_PACK_NAME_IN_QUERY,
                        STICKER_PACK_PUBLISHER_IN_QUERY,
                        STICKER_PACK_ICON_IN_QUERY,
                        ANDROID_APP_DOWNLOAD_LINK_IN_QUERY,
                        IOS_APP_DOWNLOAD_LINK_IN_QUERY,
                        PUBLISHER_EMAIL,
                        PUBLISHER_WEBSITE,
                        PRIVACY_POLICY_WEBSITE,
                        LICENSE_AGREENMENT_WEBSITE,
                        IMAGE_DATA_VERSION,
                        AVOID_CACHE,
                });
        if(stickertype.equals("AImage")) {*/
          //  Log.e("Animatedsticker","Coming true "+stickertype);
            MatrixCursor  cursor = new MatrixCursor(
                    new String[]{
                            STICKER_PACK_IDENTIFIER_IN_QUERY,
                            STICKER_PACK_NAME_IN_QUERY,
                            STICKER_PACK_PUBLISHER_IN_QUERY,
                            STICKER_PACK_ICON_IN_QUERY,
                            ANDROID_APP_DOWNLOAD_LINK_IN_QUERY,
                            IOS_APP_DOWNLOAD_LINK_IN_QUERY,
                            PUBLISHER_EMAIL,
                            PUBLISHER_WEBSITE,
                            PRIVACY_POLICY_WEBSITE,
                            LICENSE_AGREENMENT_WEBSITE,
                            IMAGE_DATA_VERSION,
                            AVOID_CACHE,
                            ANIMATED_STICKER
                    });
      //  }
        for (StickerPack stickerPack : stickerPackList){
            MatrixCursor.RowBuilder builder = cursor.newRow();
            builder.add(stickerPack.identifier);
            builder.add("Emoji2 mix");
            builder.add("Emoji2 mix");
            builder.add("sticker.webp");
            builder.add(AndroidPlayStore_Link);
            builder.add(IosAppStore_Link);
            builder.add("");
            builder.add("");
            builder.add("");
            builder.add("");
            builder.add(1);
            builder.add(1);
            if(stickerPack.identifier.equals("AImage")) {
                builder.add(1);
            }else
                builder.add(0);
        }

    /*    if(all)
            for (int i=1;i<=1+(GlobalVariable.stickersWhassap.size()/30);i++) {
                MatrixCursor.RowBuilder builder = cursor.newRow();
                builder.add(stickertype);
                builder.add("sticker.webp");
                builder.add(AndroidPlayStore_Link);
                builder.add(IosAppStore_Link);
                builder.add("");
                builder.add("");
                builder.add("");
                builder.add("");
                builder.add(1);
                builder.add(1);
                if(stickertype.equals("AImage")) {
                    builder.add(1);
                }else
                    builder.add(0);
            }
        else{
            String i = uri.getLastPathSegment();
            MatrixCursor.RowBuilder builder = cursor.newRow();
            builder.add(stickertype);
            builder.add("sticker.webp");
            builder.add(AndroidPlayStore_Link);
            builder.add(IosAppStore_Link);
            builder.add("");
            builder.add("");
            builder.add("");
            builder.add("");
            builder.add(1);
            builder.add(1);
            if(stickertype.equals("AImage")) {
                builder.add(1);
            }else
                builder.add(0);
        }*/
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }
}
