let sodiumInstance = null;

const getSodium = async () => {
  if (sodiumInstance) return sodiumInstance;
  const module = await import('https://cdn.jsdelivr.net/npm/libsodium-wrappers@0.7.11/+esm');
  const sodium = module.default;
  await sodium.ready;
  sodiumInstance = sodium;
  return sodium;
}

export const generateKeypair = async () => {
  const sodium = await getSodium();
  return sodium.crypto_box_keypair();   
}

export const encrypt = async (data, sharedKey) => {
  const sodium = await getSodium();
  const nonce = sodium.randombytes_buf(sodium.crypto_box_NONCEBYTES);
  const msg = sodium.from_string(data);

  const ciphertext = sodium.crypto_box_easy_afternm(msg, nonce, sharedKey);

  return {
    content: sodium.to_hex(ciphertext),
    nonce: sodium.to_hex(nonce),
  };
};

export const decrypt = async ({ content, nonce }, sharedKey) => {
  const sodium = await getSodium();
  const ciphertext = sodium.from_hex(content);
  const nonceBytes = sodium.from_hex(nonce);

  const plaintext = sodium.crypto_box_open_easy_afternm(ciphertext, nonceBytes, sharedKey);

  return sodium.to_string(plaintext);
};

export const convertTo = async (key) => {
  const sodium = await getSodium();
  return sodium.to_base64(key);
}

export const convertFrom = async (key) => {
  const sodium = await getSodium();
  return sodium.from_base64(key);
}

export const deriveSharedKey = async (privateKey, publicKey) => {
  const sodium = await getSodium();
  return sodium.crypto_box_beforenm(publicKey, privateKey);
};
