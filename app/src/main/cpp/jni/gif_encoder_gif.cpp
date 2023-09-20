#include <jni.h>
#include <string>
#include <vector>
#include <android/log.h>
#include "src/webp/encode.h"
#include "src/webp/mux.h"
#include <android/bitmap.h>
#include <jni.h>
#include <string>
#include <vector>
#include <android/log.h>
#include "src/webp/encode.h"
#include "src/webp/mux.h"
#include "src/webp/decode.h"
#include "imageio/image_dec.h"
#include "imageio/imageio_util.h"
#include "src/webp/demux.h"
#include <android/bitmap.h>


int ConvertGifToWebP(const char* gifPath, const char* webpPath, int quality) {
    __android_log_print(ANDROID_LOG_DEBUG, "Native", " ruta: %s", gifPath);

    int result = 0; // Resultado de la conversión
    int width = 512;
    int height = 512;
    int numFrames = 15;
    uint8_t* gifData = NULL;
    WebPAnimEncoder* enc = NULL;
    FILE* webpFile = NULL;
    // Leer el archivo GIF en memoria
    FILE* gifFile = fopen(gifPath, "rb");
    if (!gifFile) {
        __android_log_print(ANDROID_LOG_ERROR, "YourTag", "Error al abrir el archivo GIF.");
        return -1;
    }
    fseek(gifFile, 0, SEEK_END);
    size_t gifSize = ftell(gifFile);
    rewind(gifFile);
    gifData = (uint8_t*)malloc(gifSize);
    if (!gifData) {
        __android_log_print(ANDROID_LOG_ERROR, "YourTag", "Error al asignar memoria para el GIF.");
        fclose(gifFile);
        return -1;
    }
    if (fread(gifData, 1, gifSize, gifFile) != gifSize) {
        __android_log_print(ANDROID_LOG_ERROR, "YourTag", "Error al leer el archivo GIF.");
        fclose(gifFile);
        free(gifData);
        return -1;
    }
    fclose(gifFile);
    __android_log_print(ANDROID_LOG_DEBUG, "Native", " width1:");


    WebPData webp_data;
    WebPDataInit(&webp_data);
    webp_data.bytes = gifData;
    webp_data.size = gifSize;
    __android_log_print(ANDROID_LOG_DEBUG, "Native", " gifSize. %p", gifData);

    WebPAnimDecoderOptions dec_options;
    WebPAnimDecoderOptionsInit(&dec_options);
  //
   dec_options.color_mode = MODE_ARGB;

    WebPAnimDecoder* dec = WebPAnimDecoderNew(&webp_data, &dec_options);
    WebPAnimInfo anim_info;

    VP8StatusCode status = static_cast<VP8StatusCode>(WebPAnimDecoderGetInfo(dec, &anim_info));
    if (status != VP8_STATUS_OK) {
        __android_log_print(ANDROID_LOG_DEBUG, "Native", "Error al obtener información del GIF: %d", status);
        WebPAnimDecoderDelete(dec);
        free(gifData);
        return -1;
    }
    __android_log_print(ANDROID_LOG_DEBUG, "Native", "Canvas width: %d, canvas height: %d, frame count: %d", anim_info.canvas_width, anim_info.canvas_height, anim_info.frame_count);

    __android_log_print(ANDROID_LOG_DEBUG, "Native", " anim_info.canvas_width: %d",   webp_data.size);
    __android_log_print(ANDROID_LOG_DEBUG, "Native", " color: %d",  anim_info.canvas_width);


    // Inicializar el codificador WebPAnimEncoder
    WebPAnimEncoderOptions enc_options;
    WebPAnimEncoderOptionsInit(&enc_options);
    enc = WebPAnimEncoderNew(width, height, &enc_options);
    __android_log_print(ANDROID_LOG_DEBUG, "Native", " llego: ");

    // Configurar opciones del codificador WebP
    WebPConfig webp_config;
    WebPConfigInit(&webp_config);
    webp_config.lossless = 1; // Compresión con pérdida
    webp_config.quality = 10; // Calidad (ajusta según tus necesidades)

    // Crear un archivo WebP para escritura
    webpFile = fopen(webpPath, "wb");
    if (!webpFile) {
        fprintf(stderr, "Error al abrir el archivo WebP para escritura.\n");
        free(gifData);
        WebPAnimEncoderDelete(enc);
        return -1;
    }

    // Iterar a través de los cuadros del GIF y agregarlos al codificador WebP
    int timestamp_ms = 0;
    // Finalizar la animación y escribir los datos en el archivo WebP

    for (int i = 0; i < anim_info.frame_count; ++i) {
        uint8_t* frame_data;
        int timestamp_ms;

        if (WebPAnimDecoderGetNext(dec, &frame_data, &timestamp_ms, &dec_options) != VP8_STATUS_OK) {
            __android_log_print(ANDROID_LOG_ERROR, "YourTag", "Error al obtener el siguiente cuadro del GIF.");

            result = -1;
            break;
        }
        __android_log_print(ANDROID_LOG_DEBUG, "Native", " frame_data: %d",  frame_data);

        // Ahora, frame_data contiene los datos de píxeles del cuadro y timestamp_ms
        // contiene el tiempo del cuadro.

        // Configura los valores adecuados en frame.width y frame.height según el tamaño
        // de los datos de píxeles.

        WebPPicture frame;
        WebPPictureInit(&frame);
        frame.use_argb = 1;
        frame.width = 512;  // Configura el ancho del cuadro
        frame.height = 512; // Configura la altura del cuadro
        frame.argb = reinterpret_cast<uint32_t*>(frame_data);
        frame.argb_stride = frame.width * 4;
        __android_log_print(ANDROID_LOG_DEBUG, "Native", " widthsdds: %d", frame.width);


        if (WebPAnimEncoderAdd(enc, &frame, timestamp_ms, &webp_config) != WEBP_MUX_OK) {
            __android_log_print(ANDROID_LOG_ERROR, "YourTag", "Error al agregar el cuadro al codificador Web.");

            result = -1;
            break;
        }

        timestamp_ms += 100; // Ajusta el tiempo del cuadro según sea necesario
        WebPPictureFree(&frame);
    }

    WebPAnimDecoderDelete(dec);



    if (WebPAnimEncoderAssemble(enc, &webp_data) != WEBP_MUX_OK) {
        __android_log_print(ANDROID_LOG_ERROR, "YourTag", "Error al ensamblar el archivo WebP animado..");

        result = -1;
    } else {
        if (fwrite(webp_data.bytes, 1, webp_data.size, webpFile) != webp_data.size) {
            fprintf(stderr, "Error al escribir el archivo WebP.\n");
            result = -1;
        }
    }

    // Liberar recursos
    fclose(webpFile);
    free(gifData);
    WebPAnimEncoderDelete(enc);

    return result;
}


extern "C" {
JNIEXPORT jint JNICALL
Java_com_emojixer_activities_MainActivity_convertFrameToWebP(JNIEnv *env, jobject clazz,
                                                                    jstring gifPath,
                                                                    jstring webpPath,
                                                                    jint quality) {
    const char* gifPathStr = env->GetStringUTFChars(gifPath, NULL);
    const char* webpPathStr = env->GetStringUTFChars(webpPath, NULL);
    __android_log_print(ANDROID_LOG_DEBUG, "Native", "ruta: %s", gifPathStr);

    int result = ConvertGifToWebP(gifPathStr, webpPathStr, quality);

    env->ReleaseStringUTFChars(gifPath, gifPathStr);
    env->ReleaseStringUTFChars(webpPath, webpPathStr);

    return result;
}
}