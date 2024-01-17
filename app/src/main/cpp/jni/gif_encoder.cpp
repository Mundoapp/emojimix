#include <jni.h>
#include <string>
#include <vector>
#include <android/log.h>
#include "src/webp/encode.h"
#include "src/webp/mux.h"
#include <android/bitmap.h>

extern "C" {
JNIEXPORT jint JNICALL
Java_com_emojixer_activities_MainActivity_convertFrameToWebP(JNIEnv *env, jobject clazz,
                                                             jobjectArray bitmaps,
                                                             jstring outputPath, jint quality) {
    int numBitmaps = env->GetArrayLength(bitmaps);
    __android_log_print(ANDROID_LOG_DEBUG, "Native", "Número de bitmaps recibidos: %d", numBitmaps);
    int duration = 60; // Esto debe coincidir con FRAME_DELAY_MS en Java
    int timestamp_ms = 0;
    int width = 512; // Define el ancho de tu animación aquí
    int height = 512; // Define la altura de tu animación aquí
    float calidad = 0.1;
    WebPAnimEncoderOptions enc_options;
    WebPAnimEncoderOptionsInit(&enc_options);
 // enc_options.allow_mixed = true;
 // enc_options.minimize_size = true;

    WebPAnimEncoder* enc = WebPAnimEncoderNew(width, height, &enc_options);
    WebPPicture picture;
    if (!WebPPictureInit(&picture)) {
        __android_log_print(ANDROID_LOG_ERROR, "WebPConverter", "Error al inicializar WebPPicture");
        // Manejar el error, si es necesario
        return -1;}

    picture.use_argb = 1;
    picture.width = 512;
    picture.height = 512;
   picture.argb_stride = 512;
  // picture.extra_info_type= 2;
    for (int i = 0; i < numBitmaps; i++) {
        jobject bitmap = env->GetObjectArrayElement(bitmaps, i);
        void* pixels;
        if (AndroidBitmap_lockPixels(env, bitmap, &pixels) < 0) {
            continue;
        }
        AndroidBitmap_unlockPixels(env, bitmap);


        picture.argb = static_cast<uint32_t*>(pixels);
        __android_log_print(ANDROID_LOG_DEBUG, "WebPConverter", "agregado %d ", i);


        WebPConfig config;
        WebPConfigInit(&config);
        config.lossless = 0;

        config.method = 0;          // Ajusta el método de compresión (3 puede proporcionar una buena calidad)

        config.quality = quality;
     //   config.partition_limit = 10;
     //   config.target_size = 4500;
       //  config.segments = 4;        // Puedes probar con más segmentos para una mayor compresión
    // config.sns_strength = 20;   // Ajusta la fuerza del ruido espacial (50 es un valor moderado)
     // config.filter_strength = 50; // Ajusta la fuerza del filtro (50 es un valor moderado)
   // config.alpha_filtering =  2;
//
 //   config.near_lossless = 0;   // Desactiva el modo de pérdida cercana
//     //   config.exact = 0;           // Desactiva el modo de compresión exacta
 //
 // config.alpha_quality = 10; // Establece la calidad del canal alfa al máximo
//
//
//        if (i == 0) {
//            timestamp_ms += duration;
//        } else {
//            timestamp_ms += duration;
//        }

        timestamp_ms += duration;


        WebPAnimEncoderAdd(enc, &picture, timestamp_ms, &config);
      //  __android_log_print(ANDROID_LOG_DEBUG, "WebPConverter", "agregados %d ", duration);

    }

    WebPPictureFree(&picture);


    const char* output_path = env->GetStringUTFChars(outputPath, NULL);
    FILE* output_file = fopen(output_path, "wb");

    if (output_file == NULL) {
        __android_log_print(ANDROID_LOG_ERROR, "WebPConverter", "Error al abrir el archivo WebP para escritura");
        WebPAnimEncoderDelete(enc);
        return -1;
    }

    WebPData webp_data;
    WebPDataInit(&webp_data);

    if (WebPAnimEncoderAssemble(enc, &webp_data) != WEBP_MUX_OK) {
        __android_log_print(ANDROID_LOG_ERROR, "WebPConverter", "Error al ensamblar el archivo WebP animado");
        __android_log_print(ANDROID_LOG_DEBUG, "WebPConverter", "Tamaño del archivo WebP: %d bytes", webp_data.size);

        fclose(output_file);
        WebPAnimEncoderDelete(enc);
        env->ReleaseStringUTFChars(outputPath, output_path);
        return -1;
    }
    __android_log_print(ANDROID_LOG_DEBUG, "WebPConverter", "webpdata 2 %d ", webp_data.bytes);


// Ahora, escribe los datos en el archivo
    fwrite(webp_data.bytes, 1, webp_data.size, output_file);

// Limpia los datos de webp_data y cierra el archivo
    WebPDataClear(&webp_data);
    fclose(output_file);
    WebPAnimEncoderDelete(enc);




    return 0;
}
}