package com.housaire.security.crypto;

import com.housaire.security.utils.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.util.CollectionUtils;

import javax.crypto.Cipher;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 加解密帮助类
 *
 * @author Zhang Kai
 */
@Slf4j
public final class CryptoUtils {

    private static ConcurrentMap<Class, ConcurrentMap<String, Cryptor>> cryptorMap = new ConcurrentHashMap<>();

    private static ConcurrentMap<Class, ConcurrentMap<String, Encryptor>> encryptorMap = new ConcurrentHashMap<>();

    private static ConcurrentMap<Class, ConcurrentMap<String, Decryptor>> decryptorMap = new ConcurrentHashMap<>();

    private final static Lock lock = new ReentrantLock();

    public static String encryptForAES(String value, String salt) {
        Encryptor encryptor = getEncryptor(AESCryptor.class, salt);
        return encryptor.encrypt(value);
    }

    public static String decryptForAES(String value, String salt) {
        Decryptor decryptor = getDecryptor(AESCryptor.class, salt);
        return decryptor.decrypt(value);
    }

    public static Object encrypt(Object value, AnnotatedElement annotatedElement) {
        return encodeDecodeCrypto(value, annotatedElement, Cipher.ENCRYPT_MODE);
    }

    public static Object decrypt(Object value, AnnotatedElement annotatedElement) {
        return encodeDecodeCrypto(value, annotatedElement, Cipher.DECRYPT_MODE);
    }

    private static Object encodeDecodeCrypto(Object value, AnnotatedElement annotatedElement, int mode) {
        if (Objects.isNull(value)) {
            return value;
        }
        Class<? extends Annotation> annotationClass = null;
        if (Cipher.DECRYPT_MODE == mode) {
            annotationClass = Decryption.class;
        } else if (Cipher.ENCRYPT_MODE == mode) {
            annotationClass = Encryption.class;
        }
        Class<?> valueType = value.getClass();
        if (Collection.class.isAssignableFrom(valueType)) {
            Collection<?> collection = (Collection<?>) value;
            List newCollection = new ArrayList(collection.size());
            for (Object item : collection) {
                newCollection.add(encodeDecodeCrypto(item, annotatedElement, mode));
            }
            collection.clear();
            collection.addAll(newCollection);
            value = collection;
        } else if (valueType.isArray()) {
            Object[] array = (Object[]) value;
            for (int i = 0; i < array.length; i++) {
                array[i] = encodeDecodeCrypto(array[i], annotatedElement, mode);
            }
        } else if (String.class.isAssignableFrom(valueType)) {
            if (Cipher.DECRYPT_MODE == mode && hasNeedDecrypt(annotatedElement)) {
                return getDecryptor(annotatedElement).decrypt((String)value);
            } else if (Cipher.ENCRYPT_MODE == mode && hasNeedEncrypt(annotatedElement)) {
                return getEncryptor(annotatedElement).encrypt((String)value);
            }
        } else if (ReflectionUtils.isReferenceType(valueType)) {
            if (CryptoUtils.hasNeedCrypt(annotatedElement) || CryptoUtils.hasNeedCrypt(valueType)) {
                Field[] fields = FieldUtils.getAllFields(valueType);
                if (ArrayUtils.isEmpty(fields)) {
                    return value;
                }
                for (Field field : fields) {
                    try {
                        field.setAccessible(true);
                        if (!field.isAnnotationPresent(annotationClass)) {
                            continue;
                        }
                        Object fieldValue = field.get(value);
                        if (Objects.isNull(fieldValue)) {
                            continue;
                        }
                        field.set(value, encodeDecodeCrypto(fieldValue, field, mode));
                    }
                    catch (Exception e) {
                        log.error(value.getClass().getName() + "." + field.getName() + "加解密失败", e);
                    }
                }
            }
        } else {
            if (Cipher.DECRYPT_MODE == mode && hasNeedDecrypt(annotatedElement)) {
                return getDecryptor(annotatedElement).decrypt(String.valueOf(value));
            } else if (Cipher.ENCRYPT_MODE == mode && hasNeedEncrypt(annotatedElement)) {
                return getEncryptor(annotatedElement).encrypt(String.valueOf(value));
            }
        }
        return value;
    }

    public static boolean hasNeedCrypt(AnnotatedElement annotatedElement) {
        if (null != annotatedElement && annotatedElement.isAnnotationPresent(Crypto.class)) {
            return true;
        }
        return false;
    }

    public static boolean hasNeedEncrypt(AnnotatedElement annotatedElement) {
        return null != annotatedElement && (annotatedElement.isAnnotationPresent(Encryption.class)
                || annotatedElement.isAnnotationPresent(Cryption.class)
                || annotatedElement.isAnnotationPresent(Crypto.class)) ? true : false;
    }

    public static boolean hasNeedDecrypt(AnnotatedElement annotatedElement) {
        return null != annotatedElement && (annotatedElement.isAnnotationPresent(Decryption.class)
                || annotatedElement.isAnnotationPresent(Cryption.class)
                || annotatedElement.isAnnotationPresent(Crypto.class)) ? true : false;
    }

    public static Encryptor getEncryptor(AnnotatedElement annotatedElement) {
        Encryptor encryptor = null;
        if (annotatedElement.isAnnotationPresent(Encryption.class)) {
            Encryption encryption = annotatedElement.getAnnotation(Encryption.class);
            encryptor = getEncryptor(encryption.encryptor(), encryption.key());
        } else if (annotatedElement.isAnnotationPresent(Cryption.class)) {
            Cryption cryption = annotatedElement.getAnnotation(Cryption.class);
            if (Encryptor.class.isAssignableFrom(cryption.value())) {
                encryptor = (Encryptor) getCryptor(cryption.value(), cryption.salt());
            }
        }
        return encryptor;
    }

    public static Decryptor getDecryptor(AnnotatedElement annotatedElement) {
        Decryptor decryptor = null;
        if (annotatedElement.isAnnotationPresent(Decryption.class)) {
            Decryption decryption = annotatedElement.getAnnotation(Decryption.class);
            decryptor = getDecryptor(decryption.decryptor(), decryption.key());
        } else if (annotatedElement.isAnnotationPresent(Cryption.class)) {
            Cryption cryption = annotatedElement.getAnnotation(Cryption.class);
            if (Decryptor.class.isAssignableFrom(cryption.value())) {
                decryptor = (Decryptor) getCryptor(cryption.value(), cryption.salt());
            }
        }
        return decryptor;
    }

    public static Cryptor getCryptor(Class<? extends Cryptor> cryptorClass, String salt) {
        Cryptor cryptor = null;
        ConcurrentMap<String, Cryptor> valueMap = cryptorMap.get(cryptorClass);
        if (!CollectionUtils.isEmpty(valueMap) && valueMap.containsKey(salt)) {
            return valueMap.get(salt);
        } else {
            lock.lock();
            try {
                if (Objects.isNull(valueMap)) {
                    valueMap = new ConcurrentHashMap<>();
                    cryptorMap.put(cryptorClass, valueMap);
                    cryptor = newInstance(cryptorClass, salt);
                } else {
                    cryptor = valueMap.get(salt);
                    if (Objects.isNull(cryptor)) {
                        cryptor = newInstance(cryptorClass, salt);
                    }
                }
                valueMap.put(salt, cryptor);
            } finally {
                lock.unlock();
            }
        }
        return cryptor;
    }

    public static Encryptor getEncryptor(Class<? extends Encryptor> encryptorClass, String salt) {
        Encryptor encryptor = null;
        ConcurrentMap<String, Encryptor> valueMap = encryptorMap.get(encryptorClass);
        if (!CollectionUtils.isEmpty(valueMap) && valueMap.containsKey(salt)) {
            return valueMap.get(salt);
        } else {
            lock.lock();
            try {
                if (Objects.isNull(valueMap)) {
                    valueMap = new ConcurrentHashMap<>();
                    encryptorMap.put(encryptorClass, valueMap);
                    encryptor = (Encryptor) newInstance(encryptorClass, salt);
                } else {
                    encryptor = valueMap.get(salt);
                    if (Objects.isNull(encryptor)) {
                        encryptor = (Encryptor) newInstance(encryptorClass, salt);
                    }
                }
                valueMap.putIfAbsent(salt, encryptor);
            } finally {
                lock.unlock();
            }
        }
        return encryptor;
    }

    public static Decryptor getDecryptor(Class<? extends Decryptor> decryptorClass, String salt) {
        Decryptor decryptor = null;
        ConcurrentMap<String, Decryptor> valueMap = decryptorMap.get(decryptorClass);
        if (!CollectionUtils.isEmpty(valueMap) && valueMap.containsKey(salt)) {
            return valueMap.get(salt);
        } else {
            lock.lock();
            try {
                if (Objects.isNull(valueMap)) {
                    valueMap = new ConcurrentHashMap<>();
                    decryptorMap.put(decryptorClass, valueMap);
                    decryptor = (Decryptor) newInstance(decryptorClass, salt);
                } else {
                    decryptor = valueMap.get(salt);
                    if (Objects.isNull(decryptor)) {
                        decryptor = (Decryptor) newInstance(decryptorClass, salt);
                    }
                }
                valueMap.put(salt, decryptor);
            } finally {
                lock.unlock();
            }
        }
        return decryptor;
    }

    public static Cryptor newInstance(Class<? extends Cryptor> decryptorClass, String salt) {
        try {
            int parameterCount = null != salt ? 1 : 0;
            Constructor<? extends Cryptor>[] constructors = (Constructor<? extends Cryptor>[]) decryptorClass
                    .getConstructors();
            for (int i = 0; i < constructors.length; i++) {
                if (constructors[i].getParameterCount() == parameterCount) {
                    if (parameterCount == 1) {
                        return constructors[i].newInstance(salt);
                    } else {
                        return constructors[i].newInstance();
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        throw new RuntimeException("未找到加密实现类: " + decryptorClass.getName());
    }

}
