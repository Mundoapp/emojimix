cmake_minimum_required(VERSION 3.10)

project(libwebp)

# Configura las opciones de compilación según la arquitectura
if(${ANDROID_ABI} STREQUAL "armeabi-v7a")
    set(ARCH "arm")
elseif(${ANDROID_ABI} STREQUAL "arm64-v8a")
    set(ARCH "arm64")
elseif(${ANDROID_ABI} STREQUAL "x86")
    set(ARCH "x86")
elseif(${ANDROID_ABI} STREQUAL "x86_64")
    set(ARCH "x86_64")
else()
    message(FATAL_ERROR "Unsupported architecture: ${ANDROID_ABI}")
endif()

# Directorio raíz del código fuente de WebP
set(WEBP_SRC_DIR ${CMAKE_CURRENT_SOURCE_DIR}/)

# Agrega los archivos fuente de WebP al proyecto
file(GLOB WEBP_WEBP_SRCS ${WEBP_SRC_DIR}/src/webp/*.c)
file(GLOB WEBP_DEC_SRCS ${WEBP_SRC_DIR}/src/dec/*.c)
file(GLOB WEBP_DEMUX_SRCS ${WEBP_SRC_DIR}/src/demux/*.c)
file(GLOB WEBP_DSP_SRCS ${WEBP_SRC_DIR}/src/dsp/*.c)
file(GLOB WEBP_DSP_ENC_SRCS ${WEBP_SRC_DIR}/src/dsp/enc/*.c)
file(GLOB WEBP_ENC_SRCS ${WEBP_SRC_DIR}/src/enc/*.c)
file(GLOB WEBP_MUX_SRCS ${WEBP_SRC_DIR}/src/mux/*.c)
file(GLOB WEBP_UTILS_SRCS ${WEBP_SRC_DIR}/src/utils/*.c)
file(GLOB WEBP_IMAGEIO ${WEBP_SRC_DIR}/imageio/image_dec.c)
file(GLOB WEBP_IMAGEIOO ${WEBP_SRC_DIR}/imageio/imageio_util.c)
file(GLOB WEBP_sharpyuv ${WEBP_SRC_DIR}/sharpyuv/*.c)



# Bibliotecas necesarias para enlazar con WebP
find_library( # Definir el nombre de la biblioteca como se encuentra en NDK
        log-lib
        log )

# Configurar la biblioteca compartida libwebp.so
add_library(webp SHARED
        ${WEBP_WEBP_SRCS}
        ${WEBP_DEC_SRCS}
        ${WEBP_DEMUX_SRCS}
        ${WEBP_DSP_SRCS}
        ${WEBP_DSP_ENC_SRCS}
        ${WEBP_ENC_SRCS}
        ${WEBP_MUX_SRCS}
        ${WEBP_UTILS_SRCS}
        ${WEBP_IMAGEIOO}
        ${WEBP_IMAGEIO}
        ${WEBP_sharpyuv}
        )
# Directorio de inclusión de las cabeceras de WebP
target_include_directories(webp PRIVATE
        ${WEBP_SRC_DIR}/src
        ${WEBP_SRC_DIR}/examples)

# Enlazar las bibliotecas necesarias
target_link_libraries(webp PRIVATE ${log-lib})

# Puedes agregar opciones adicionales de compilación si es necesario
# set_target_properties(webp PROPERTIES ...)

include_directories(${ANDROID_NDK}/sources/android/native_app_glue)
include_directories(${ANDROID_NDK}/sources/android/cpufeatures)

#
## Agrega la biblioteca libwebp.so
#add_library(lib_webp SHARED IMPORTED)
#set_target_properties(lib_webp PROPERTIES IMPORTED_LOCATION
#        ${CMAKE_SOURCE_DIR}/src/main/jniLibs/arm64-v8a/libwebp.so)
#
## Agrega la biblioteca libgif_encoder.so (si ya lo tienes en tu CMakeLists.txt)
#add_library(gif_encoder SHARED gif_encoder.cpp)
#
## Enlaza la biblioteca libwebp con libgif_encoder
#target_link_libraries(gif_encoder lib_webp)
#
#
add_library(native_app_glue STATIC
        ${ANDROID_NDK}/sources/android/native_app_glue/android_native_app_glue.c)

# Tu configuración adicional del proyecto JNI
# ...

# Enlaza las bibliotecas necesarias a tu biblioteca JNI

add_library(gif_encoder SHARED gif_encoder.cpp)
target_link_libraries(gif_encoder webp)
find_library(jnigraphics-lib jnigraphics)
target_link_libraries(gif_encoder ${jnigraphics-lib})

target_link_libraries(gif_encoder native_app_glue ${log-lib} ${android-lib})


