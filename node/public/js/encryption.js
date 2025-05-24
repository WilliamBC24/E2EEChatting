import { webcrypto } from "crypto";

const createRSA = async () => {
    return await webcrypto.subtle.generateKey(
        {
            name: "RSA-OAEP",
            modulusLength: 4096,
            publicExponent: new Uint8Array([1, 0, 1]),
            hash: "SHA-256",
        },
        true,
        ["encrypt", "decrypt"],
    )
}

const createAES = async () => {
     const key = await webcrypto.subtle.generateKey(
        {
            name: "AES-CBC",
            length: 256,
        },
        true,
        ["encrypt", "decrypt"],
    );
    const iv = webcrypto.getRandomValues(new Uint8Array(16));
    return { key, iv };
}

const exportRSA = async (RSAPair) => {
    const privateExport = await webcrypto.subtle.exportKey("jwk", RSAPair.privateKey);
    const publicExport = await webcrypto.subtle.exportKey("jwk", RSAPair.publicKey);
    return {privateExport, publicExport}
}

const exportAES = async (AES) => {
    return webcrypto.subtle.exportKey(
        "raw",
        AES
    )
}

const importPublicRSA = async (publicRSA) => {
    return await webcrypto.subtle.importKey(
        "jwk",
        publicRSA,
        {name: "RSA-OAEP", hash: "SHA-256"},
        true,
        ["encrypt"]
    )
}

const importPrivateRSA = async (privateRSA) => {
    return await webcrypto.subtle.importKey(
        "jwk",
        privateRSA,
        {name: "RSA-OAEP", hash: "SHA-256"},
        true,
        ["decrypt"]
    )
}

const importAES = async (AESdecrypted) => {
    return await webcrypto.subtle.importKey(
        "raw",
        AESdecrypted,
        {
            name: "AES-CBC"
        },
        true,
        ["encrypt","decrypt"]
    )
}

const getEncoding = (data) => {
    //Encode into Uint8Array
    let enc = new TextEncoder();
    return enc.encode(data);
}

const getDecoding = (dataBuffer) => {
    let dec = new TextDecoder();
    return dec.decode(dataBuffer);
}

const arrayBufferToBase64 = (buffer) => {
    return btoa(String.fromCharCode(...new Uint8Array(buffer)));
}

const base64ToArrayBuffer = (base64) => {
    const binaryString = atob(base64);
    const len = binaryString.length;
    const bytes = new Uint8Array(len);
    for (let i = 0; i < len; i++) {
        bytes[i] = binaryString.charCodeAt(i);
    }
    return bytes.buffer;
}

const encryptAES = async (AESencoded, publicKey) => {
    return webcrypto.subtle.encrypt(
        {
            name: "RSA-OAEP"
        },
        publicKey,
        AESencoded
    )
}

const decryptAES = async (privateKey, AESencrypted) => {
    //Returns an ArrayBuffer which contains the decrypted product that needs to be imported
    return webcrypto.subtle.decrypt(
        {
            name: "RSA-OAEP"
        },
        privateKey,
        AESencrypted
    )
}

const encryptData = async ({key, iv}, encoded) => {
    return webcrypto.subtle.encrypt(
        {
            name: "AES-CBC",
            iv: iv
        },
        key,
        encoded
    )
}

const decryptData = async (AES, iv, dataEncrypted) => {
    return webcrypto.subtle.decrypt(
        {
            name: "AES-CBC",
            iv
        },
        AES,
        dataEncrypted
    )
}

const saveRSA = async () => {
    const RSA = await createRSA();
    const RSAexported = await exportRSA(RSA);
    await fetch("/gateway/message/security/saveRSA", {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            publicKey: JSON.stringify(RSAexported.publicExport),
            privateKey: JSON.stringify(RSAexported.privateExport),
        })
    })
}

const encryptionFlow = async (data) => {
    const RSA = await getPublicRSA()
    const AESandIV = await createAES();

    const base64Entry = await encodeAndEncryptData(data, AESandIV)

    const exportedAES = await exportAES(AESandIV.key);
    const encryptedAES = await encryptAES(exportedAES, RSA);
    return {
        content: base64Entry,
        aes_key: arrayBufferToBase64(encryptedAES),
        iv: arrayBufferToBase64(AESandIV.iv),
    }
    
}

const decryptionFlow = async (data) => {
    const AESnIV = await getAESandIV(data)

    const dataBuffer = await base64ToArrayBuffer(data.data.content);
    const decryptedData = await decryptData(AESnIV.key, AESnIV.iv, dataBuffer);
    return getDecoding(decryptedData);
}

const encodeAndEncryptData = async (data, AESandIV) => {
    const encodedEntry = getEncoding(data);
    const encryptedEntry = await encryptData(AESandIV, encodedEntry);
    return arrayBufferToBase64(encryptedEntry);
}

const getAESandIV = async (data) => {
    const RSA = await getPrivateRSA()

    const AES = await base64ToArrayBuffer(data.data.aes_key);
    const AESdecrypted = await decryptAES(RSA, AES);
    const AESimported = await importAES(AESdecrypted);
    const iv = base64ToArrayBuffer(data.data.iv);
    return {iv: iv, key: AESimported}
}

const getPublicRSA = async () => {
    const {publicKey: publicRSAJSON} = (await fetch("/gateway/message/security/getRSA").then(res => res.json())).data;
    return await importPublicRSA(JSON.parse(publicRSAJSON));
}

const getPrivateRSA = async () => {
    const {privateKey: privateRSAJSON} = (await fetch("/gateway/message/security/getRSA").then(res => res.json())).data;
    return await importPrivateRSA(JSON.parse(privateRSAJSON));
}