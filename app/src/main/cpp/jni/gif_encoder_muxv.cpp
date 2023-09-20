#include <jni.h>
#include <string>
#include <vector>
#include <android/log.h>
#include "src/webp/mux.h"
#include "src/webp/encode.h"
#include "src/webp/decode.h"
#include <android/bitmap.h>
#include <iostream>
#include <jni.h>
#include <string>
#include <vector>
#include <android/bitmap.h>

bool IsWebPFormatARGB(uint8_t *data, long size);

extern "C" {
JNIEXPORT jint JNICALL
Java_pro_emoji_maker_capturaview_CaptureActivity_convertFrameToWebP(JNIEnv *env, jobject clazz,
                                                                    jobjectArray webpPaths,
                                                                    jstring outputPath,
                                                                    jint quality) {
    int numFrames = env->GetArrayLength(webpPaths);
    __android_log_print(ANDROID_LOG_DEBUG, "Native", "Número de frames recibidos: %d", numFrames);

    int width = 703; // Define el ancho de tu animación aquí
    int height = 703; // Define la altura de tu animación aquí
    WebPAnimEncoderOptions enc_options;
    WebPAnimEncoderOptionsInit(&enc_options);


    WebPAnimEncoder* enc = WebPAnimEncoderNew(width, height, &enc_options);

    const char* output_path = env->GetStringUTFChars(outputPath, NULL);
    FILE* output_file = fopen(output_path, "wb");

    if (!output_file) {
        __android_log_print(ANDROID_LOG_ERROR, "WebPConverter",
                            "Error al abrir el archivo WebP para escritura");
        return -1;
    }



    for (int i = 0; i < numFrames; i++) {
        jstring webpPath = static_cast<jstring>(env->GetObjectArrayElement(webpPaths, i));
        const char *webpPathStr = env->GetStringUTFChars(webpPath, nullptr);
        __android_log_print(ANDROID_LOG_DEBUG, "Native", "ruta frame recibido: %s", webpPathStr);

        FILE* webpFile = fopen(webpPathStr, "rb");
        if (!webpFile) {
            __android_log_print(ANDROID_LOG_DEBUG, "Native", "no puedo abrir el frame recibido: %s", webpPathStr);
            env->ReleaseStringUTFChars(webpPath, webpPathStr);
            continue;
        }

        fseek(webpFile, 0, SEEK_END);
        long fileSize = ftell(webpFile);
        fseek(webpFile, 0, SEEK_SET);

        uint8_t* webpData = new uint8_t[fileSize];
        if (fread(webpData, 1, fileSize, webpFile) != static_cast<size_t>(fileSize)) {
            fclose(webpFile);
            delete[] webpData;
            __android_log_print(ANDROID_LOG_DEBUG, "Native", "no puedo leer el frame recibido: %s", webpPathStr);
            env->ReleaseStringUTFChars(webpPath, webpPathStr);
            continue;
        }

// Verifica si el archivo WebP es ARGB
        WebPBitstreamFeatures features;
        if (WebPGetFeatures(webpData, fileSize, &features) == VP8_STATUS_OK) {
            if (features.has_alpha && features.format == 1) {
                // Es un formato ARGB válido
                __android_log_print(ANDROID_LOG_DEBUG, "Native", "Es un formato ARGB válido");
            } else {
                // No es ARGB
                __android_log_print(ANDROID_LOG_DEBUG, "Native", "No es un formato ARGB válido");
            }
        } else {
            // No se pudo obtener información de las características del archivo WebP
            __android_log_print(ANDROID_LOG_DEBUG, "Native", "No se pudo obtener información de las características");
        }

        fclose(webpFile);


        // Inicializa una instancia de WebPPicture para el cuadro actual
        WebPPicture picture;
        WebPPictureInit(&picture);

        picture.use_argb = 1; // Indica que estás utilizando datos ARGB
        picture.width = 703; // Establece las dimensiones de la imagen
        picture.height = 703;
        picture.argb = reinterpret_cast<uint32_t*>(webpData);
        picture.argb_stride = 703; // Configura el stride adecuadamente


        __android_log_print(ANDROID_LOG_DEBUG, "Native", "picture puedo leer el frame recibido: %s", picture.argb );

        __android_log_print(ANDROID_LOG_DEBUG, "Native", "picture dimensiones: %d x %d", picture.width, picture.height);

        int duration = 33; // Duración del fotograma en milisegundos (ajusta según tus necesidades)

        // Agrega el cuadro actual al encoder
        WebPAnimEncoderAdd(enc, &picture, duration, NULL);

        delete[] webpData;
        env->ReleaseStringUTFChars(webpPath, webpPathStr);
    }


    if (!enc) {
        __android_log_print(ANDROID_LOG_ERROR, "WebPConverter", "No se encontraron frames válidos");
        fclose(output_file);
        env->ReleaseStringUTFChars(outputPath, output_path);
        return -1;
    }

    WebPData webp_data;
    WebPDataInit(&webp_data);  // Inicializa webp_data
    // Finaliza la animación y escribe los datos en el archivo
    if (WebPAnimEncoderAssemble(enc, &webp_data) != WEBP_MUX_OK) {
        __android_log_print(ANDROID_LOG_ERROR, "WebPConverter", "Error al ensamblar el archivo WebP animado");
        fclose(output_file);
        WebPAnimEncoderDelete(enc);
        env->ReleaseStringUTFChars(outputPath, output_path);
        return -1;
    }

    fwrite(webp_data.bytes, 1, webp_data.size, output_file);

    // Limpia los datos de webp_data y cierra el archivo
    WebPDataClear(&webp_data);
    fclose(output_file);
    WebPAnimEncoderDelete(enc);

    return 0; // Conversión exitosa
}
}

bool IsWebPFormatARGB(uint8_t *data, long size) {
    // Verifica si el archivo WebP tiene un encabezado que indica formato ARGB
    // Puedes modificar esto según el formato real que estás utilizando
    if (size < 12) {
        return false;  // El archivo es demasiado pequeño para ser un WebP válido
    }

    // Comprueba la firma del archivo WebP (Riff y WebP)
    if (memcmp(data, "RIFF", 4) != 0 || memcmp(data + 8, "WEBP", 4) != 0) {
        return false;  // No es un archivo WebP válido
    }

    // Comprueba el chunk VP8L que indica el formato ARGB
    if (memcmp(data + 12, "VP8L", 4) != 0) {
        return false;  // No es un archivo WebP con formato ARGB
    }

    // Puedes agregar más comprobaciones si es necesario, dependiendo de tu formato ARGB específico

    return true;  // Es un archivo WebP con formato ARGB
}

